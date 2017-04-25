package com.zxly.o2o.o2o_user.wxapi;


import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.PayAct;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.util.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by dsnx on 2015/10/14.
 */
public class WXSecurePayer {

    private boolean paying = false;
    PayReq req;
    IWXAPI wxApi;
    private Activity act;
    private Handler callback;
    private  LoadingDialog dialog;
    private static WXSecurePayer instance;
    private WXSecurePayer(Activity act, final Handler callback) {
        this.callback=callback;
        this.act = act;
        wxApi = WXAPIFactory.createWXAPI(act, Constants.WX_APP_ID, true);
        req = new PayReq();
        instance=this;
    }
    public static WXSecurePayer getInstance(Activity act, final Handler callback)
    {
        if(instance==null)
        {
            instance=new WXSecurePayer(act,callback);
        }
        return  instance;
    }
    public static void closePay()
    {
        if(instance!=null)
        {
            if(instance.isPaying())
            {
                instance.payResult(-2,"支付取消");
            }
        }
    }

    public boolean isPaying() {
        return paying;
    }

    /**
     * @param out_trade_no 流水号
     * @param orderMoney   金额
     * @param notify_url   0:成功,  -1:未知错误 ,-2:用户取消
     */
    public void pay(final String orderNo, final String out_trade_no, final float orderMoney, final String notify_url) {
        dialog = new LoadingDialog();
        dialog.show();
        if (!wxApi.isWXAppInstalled()) {
            payResult(-3,"微信没有安装");
            return;
        }
        if (paying) {
            return;
        }
        paying = true;
        start(Account.shopInfo.getName() + "-订单号" + orderNo, out_trade_no, changeY2F(orderMoney), notify_url);
    }


    private void start(final String body, final String out_trade_no, final String total_fee, final String notify_url) {


        new Thread(new Runnable() {

            @Override
            public void run() {

                String url = String
                        .format("https://api.mch.weixin.qq.com/pay/unifiedorder");
                String entity = genProductArgs(body, out_trade_no, total_fee, notify_url);
                byte[] buf = Util.httpPost(url, entity);
                if (buf == null) {
                    payResult(-1,"支付失败");
                    return;
                }
                String content = new String(buf);
                Map<String, String> result = decodeXml(content);
                req.appId = Constants.WX_APP_ID;
                req.partnerId = Constants.MCH_ID;
                req.prepayId = result.get("prepay_id");
                req.packageValue = "Sign=WXPay";
                req.nonceStr = genNonceStr();
                req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
                List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                signParams.add(new BasicNameValuePair("appid", req.appId));
                signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                signParams.add(new BasicNameValuePair("package", req.packageValue));
                signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

                req.sign = genAppSign(signParams);
                wxApi.sendReq(req);

            }
        }).start();


    }
    public static void payEnd(int resultCode, String msg){
        if(instance!=null)
        {
            instance.payResult(resultCode,msg);
        }
    }
    public void payResult(int resultCode, String msg)
    {
        paying = false;
        Message m = new Message();
        m.what = PayAct.WXPAY_RESULT;
        m.obj = resultCode;
        callback.sendMessage(m);
        callback.post(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });

        instance=null;
    }
    public static String changeY2F(float amount) {
        String currency = String.valueOf(amount);
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }

    private String genProductArgs(String body, String out_trade_no, String total_fee, String notify_url) {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams
                    .add(new BasicNameValuePair("appid", Constants.WX_APP_ID));
            packageParams.add(new BasicNameValuePair("body", body));
            packageParams
                    .add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", notify_url));
            packageParams.add(new BasicNameValuePair("out_trade_no", out_trade_no));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", getlocalip()));
            packageParams.add(new BasicNameValuePair("total_fee", total_fee));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genAppSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);

            return new String(xmlstring.toString().getBytes(), "ISO8859-1");

        } catch (Exception e) {

            return null;
        }
    }

    private String getlocalip() {
        WifiManager wifiManager = (WifiManager) this.act.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        if (ipAddress == 0) {
            return "127.0.0.1";

        }
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        return sb.toString();
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        String appSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // ʵ��student����
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }


}
