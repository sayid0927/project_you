package com.zxly.o2o.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.provider.ContactsContract;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.CommonQuestionAct;
import com.zxly.o2o.activity.PayAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONObject;

public class RemoteInvokeService {
    private Activity context;
    private WebView webView;
    private LoadingDialog dialog;
    private CallBack callBack;
    private Object data;

    public RemoteInvokeService(Activity paramActivity, WebView webView, CallBack callBack) {
        this.context = paramActivity;
        this.webView = webView;
        this.callBack = callBack;
    }

    public RemoteInvokeService(Activity paramActivity, Object data, CallBack callBack) {
        this.context = paramActivity;
        this.callBack = callBack;
        this.data=data;
    }

    @JavascriptInterface
    public void toLogin() {
//        AppController.getInstance().deleteFile("user");
//        Account.user = null;
//        PreferUtil.getInstance().setLoginToken("");
//        LoginAct.start(context, callBack);
    }


    private ShareDialog shareDialog;
    @JavascriptInterface
    public void toShare(final String title, final String desc, final String targetUrl, final String iconUrl) {
        if(webView!=null){

            webView.post(new Runnable() {
                @Override
                public void run() {
                    if(shareDialog==null)
                        shareDialog=new ShareDialog();

                    shareDialog.show(title, desc, targetUrl, iconUrl, new ShareListener() {
                        @Override
                        public void onComplete(Object var1) {
                            if (webView!=null)
                                webView.loadUrl("javascript:submitTransmit()");
                        }

                        @Override
                        public void onFail(int errorCode) {

                        }
                    });
                }
            });
        }



    }


    @JavascriptInterface
    public void openContacts() {
        this.context.startActivityForResult(new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI), 0);
    }

    @JavascriptInterface
    public void doRecharge(String orderNo) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String topActName = cn.getClassName();
        if (Account.hasLogin() && !topActName.equals("com.zxly.o2o.activity.PayAct")) {
            PayAct.start(this.context, orderNo, Constants.TYPE_FLOW_PAY);
        }

//        if(Account.hasLogin()) {
//            PayAct.start(this.context, orderNo, Constants.TYPE_FLOW_PAY);
//        }
    }

    @JavascriptInterface
    public void showLoading() {
        if (dialog == null) {
            dialog = new LoadingDialog();
        }
        dialog.show();
    }

    @JavascriptInterface
    public void cancelLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

//    @JavascriptInterface
//    public void doShare() {
//        Banners banner= (Banners) data;
//        ViewUtils.share(context, config.dataBaseUrl + banner.getUrl()+"&DeviceID="+ config.imei+"&Authorization=null&type="+ Constants.OPEN_FROM_SHARE+"&shopId="+ config.shopId+"&baseUrl="+ config.dataBaseUrl);
//        new BigturntableCountRequest(Account.user.getId(), (long) banner.getId()).start();
//    }

    @JavascriptInterface
    public void toCommonQuestions() {
        CommonQuestionAct.start(context);
    }


    @JavascriptInterface
    public void openAct(String jsData){
        ViewUtils.showToast("打开 ："+jsData);
        try {
            JSONObject jo = new JSONObject(jsData);
            String pageName=jo.getString("pageName");
            if("PayAct".equals(pageName)){
                String orderNo=jo.getString("orderNo");
                if(Account.hasLogin()) {
                    PayAct.start(this.context, orderNo, Constants.TYPE_FLOW_PAY);
                }
            }else if("LoginAct".equals(pageName)){
                AppController.getInstance().deleteFile("user");
                Account.user = null;
                PreferUtil.getInstance().setLoginToken("");
//                LoginAct.start(context, callBack);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public String getPageData(){
        return (String) data;
    }

    @JavascriptInterface
    public void toRecharge() {
        if (callBack != null) {
            callBack.onCall();
        }
    }

    public void setPageData(String data){
        this.data=data;
    }

}