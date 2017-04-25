package com.zxly.o2o.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.yintong.pay.utils.BaseHelper;
import com.yintong.pay.utils.MobileSecurePayer;
import com.yintong.pay.utils.PayOrder;
import com.yintong.pay.utils.Rsa;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.PayAct;
import com.zxly.o2o.activity.PayFailAct;
import com.zxly.o2o.activity.PaySuccessAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.dialog.PayConfirmDialog;
import com.zxly.o2o.dialog.PayTakeoutDialog;
import com.zxly.o2o.model.BankcardInfo;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.model.YinTongTradeInfo;
import com.zxly.o2o.o2o_user.wxapi.WXSecurePayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PayUtil {
    public static final int BANKCARD_NUMBER_INCORRECT = 11;
    public static final int FINISH_TOP_ACTIVITY = 12;
    public static final int SOME_REASON_PAY_FAIL = 13;

    /**
     * @param type       0：流量充值 1：订单支付，3：提现
     * @param payNo      流水号
     * @param payType    交易类型 01：银行卡交易，02：微信，03：余额，04：支付宝
     * @param orderMoney 交易金额
     */
    public static void confirmPay(Activity curAct, int type, String payNo, String payType, float orderMoney, UserBankCard userBankCard, CallBack callBack, CallBack callBack2, BankcardInfo bankcardInfo, String description, float moneyInput) {
        PayConfirmDialog confirmDialog = new PayConfirmDialog(curAct, type, payNo, payType, orderMoney, userBankCard, callBack, callBack2, bankcardInfo, description, moneyInput);
        confirmDialog.show();
    }

    public static void confirmPay(Activity curAct, int type, String payNo, String payType, float orderMoney, UserBankCard userBankCard, CallBack callBack, CallBack callBack2, BankcardInfo bankcardInfo, String description) {
        PayConfirmDialog confirmDialog = new PayConfirmDialog(curAct, type, payNo, payType, orderMoney, userBankCard, callBack, callBack2, bankcardInfo, description);
        confirmDialog.show();
    }

    public static void confirmPay(Activity curAct, int type, String payNo, String payType, float orderMoney, CallBack callBack) {
        PayConfirmDialog confirmDialog = new PayConfirmDialog(curAct, type, payNo, payType, orderMoney, callBack);
        confirmDialog.show();
    }

    public static void confirmTakeout(Activity curAct, int type, String payNo, String payType, float orderMoney, UserBankCard userBankCard, CallBack callBack, CallBack callBack2) {
        PayTakeoutDialog payTakeoutDialog = new PayTakeoutDialog(curAct, type, payNo, payType, orderMoney, userBankCard, callBack, callBack2);
        payTakeoutDialog.show();
    }

    /**
     * 连连支付入口
     *
     * @param ubc     银行卡信息
     * @param yttInfo 交易订单信息
     * @param payType 1:实物交易  2:虚拟交易
     */
    public static void doYinTongPay(UserBankCard ubc, YinTongTradeInfo yttInfo, ParameCallBack callBack, Activity activity, int payType, String mobile) {
        PayOrder order = new PayOrder();
        switch (payType) {
            case 1://实物交易
                order.setBusi_partner(PayOrder.BUSI_PARTNER_MATERIAL);
                order.setOid_partner(Config.PARTNER);
                // 风险控制参数
                order.setRisk_item(materialConstructRiskItem());
                break;
            case 2://虚拟交易
                order.setBusi_partner(PayOrder.BUSI_PARTNER_VIRTUAL);
                order.setOid_partner(Config.VIRTUAL_PARTNER);
                // 风险控制参数
                order.setRisk_item(virtualConstructRiskItem(mobile));
                break;
            default:
                return;
        }
        order.setNo_order(yttInfo.getNo_order());
        order.setDt_order(yttInfo.getDt_order());
//		order.setName_goods("龙禧大酒店中餐厅：2-3人浪漫套餐X1");
        order.setNotify_url(yttInfo.getNotify_url());
        order.setSign_type(PayOrder.SIGN_TYPE_RSA);
        order.setValid_order("10080");//订单有效时间 分钟为单位，默认为 10080 分钟（7 天）
        order.setUser_id(Account.user.getId() + "");
        order.setId_no(ubc.getIdCard());
        order.setAcct_name(ubc.getUserName());
        order.setMoney_order(StringUtil.getFormatPrice(yttInfo.getMoney_order()));
//		order.setCard_no(ubc.getBankNo());
        order.setCard_no(ubc.getBankNumber());
        order.setFlag_modify("1");

        String sign = "";
        String content = BaseHelper.sortParam(order);
        sign = Rsa.sign(content, Config.RSA_PRIVATE);
        order.setSign(sign);
        String content4Pay = BaseHelper.toJSONString(order);

        MobileSecurePayer msp = new MobileSecurePayer();
        msp.pay(content4Pay, callBack, activity, false);

    }

    //实物类风险控制参数
    private static String materialConstructRiskItem() {
        JSONObject mRiskItem = new JSONObject();
        try {
            mRiskItem.put("frms_ware_category", "4999");
            mRiskItem.put("user_info_mercht_userno", Account.user.getId());
            mRiskItem.put("user_info_dt_register", "20141015165530");
            mRiskItem.put("user_info_bind_phone", Account.user.getMobilePhone());
            if (!StringUtil.isNull(Account.user.getTakeDeliveryAddress())) {
                mRiskItem.put("delivery_addr_full", Account.user.getTakeDeliveryAddress());
            } else {
                mRiskItem.put("delivery_addr_full", Account.shopInfo.getAddress());
            }
            mRiskItem.put("delivery_phone", Account.user.getMobilePhone());
            mRiskItem.put("logistics_mode", 2);
            mRiskItem.put("delivery_cycle", "72h");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mRiskItem.toString();
    }

    //虚拟类风险控制参数
    private static String virtualConstructRiskItem(String mobile) {
        JSONObject mRiskItem = new JSONObject();
        try {
            mRiskItem.put("frms_ware_category", "1010");
            mRiskItem.put("user_info_mercht_userno", Account.user.getId());
            mRiskItem.put("user_info_dt_register", "20141015165530");
            mRiskItem.put("user_info_bind_phone", Account.user.getMobilePhone());
            mRiskItem.put("frms_charge_phone", mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mRiskItem.toString();
    }

    /**
     * 支付宝支付入口
     *
     * @param productName 交易订单商品名称
     * @param productInfo 交易订单商品描述
     * @param orderMoney  交易订单价钱
     * @param payNo       交易流水号
     * @param mHandler
     * @param activity
     */
    public static void doAlipay(String productName, String productInfo, String orderMoney, String payNo, final Handler mHandler, final Activity activity) {
        // 订单
        String orderInfo = getAlipayOrderInfo(productName, productInfo, orderMoney, payNo);

        // 对订单做RSA 签名
        String sign = SignUtils.sign(orderInfo, Config.payParams.getAliPrivateKey());
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + "sign_type=\"RSA\"";

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = PayAct.ALIPAY_RESULT;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建支付宝订单信息
     */
    public static String getAlipayOrderInfo(String subject, String body, String price, String payNo) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Config.payParams.getAliPartner() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Config.payParams.getAliSeller() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + payNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + Config.payParams.getAliNotifyUrl()
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    public static void wxpay(final Activity act, final String orderNo, final String out_trade_no, final float orderMoney, final String notify_url, final Handler callback) {
        WXSecurePayer.getInstance(act, callback).pay(orderNo, out_trade_no, orderMoney, notify_url);
    }


    public static void launchYinTongPay(final Handler handler, final Activity curAct, final UserBankCard userBankCard, final float orderMoney, final String orderNo, String money, final String payNo, final String userPaw, String createTime, String notifyUrl, String mobile) {
        YinTongTradeInfo ytti = new YinTongTradeInfo(Float.parseFloat(money), payNo, createTime, notifyUrl);
        PayUtil.doYinTongPay(userBankCard, ytti, new ParameCallBack() {
            @Override
            public void onCall(Object object) {
                String strRet = (String) object;
                JSONObject objContent = BaseHelper.string2JSON(strRet);
                String retCode = objContent.optString("ret_code");
                String retMsg = objContent.optString("ret_msg");
                AppLog.e("---retMsg---" + retMsg);
                // 成功
                if (com.yintong.pay.utils.Constants.RET_CODE_SUCCESS.equals(retCode)) {
                    // TODO 成功
                    AppLog.e("---success---, retCode is-----" + retCode);
                    PaySuccessAct.start(curAct, orderMoney, orderNo, "takeout_verify", userBankCard, orderMoney, userPaw);
                    handler.obtainMessage(FINISH_TOP_ACTIVITY).sendToTarget();
                } else if (com.yintong.pay.utils.Constants.RET_CODE_PROCESS.equals(retCode)) {
                    // TODO 处理中，掉单的情形
                    String resultPay = objContent.optString("result_pay");
                    if (com.yintong.pay.utils.Constants.RESULT_PAY_PROCESSING
                            .equalsIgnoreCase(resultPay)) {
                    }
                    AppLog.e("---ing---, retCode is---" + retCode);
                } else {
                    // TODO 失败
                    AppLog.e("---fail---, retCode is---" + retCode);
                    if ("1503".equals(retCode) || "1004".equals(retCode) || "1008".equals(retCode)) {
                        handler.obtainMessage(BANKCARD_NUMBER_INCORRECT).sendToTarget();
                        return;
                    }
                    if("1005".equals(retCode)){
                        handler.obtainMessage(SOME_REASON_PAY_FAIL).sendToTarget();
                        return;
                    }
                    if (!"1006".equals(retCode)) {
                        PayFailAct.start(curAct, orderNo);
                        handler.obtainMessage(FINISH_TOP_ACTIVITY).sendToTarget();
                    }
                }
            }
        }, curAct, 1, mobile);
    }

}
