package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.service.RemoteInvokeService;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * @author fengrongjian 2015-8-17
 * @description 流量充值界面
 */
public class MobileDataAct extends BasicAct implements View.OnClickListener {
    private WebView webView;
    private String browserUrl;
    private View btnBack, btnRecharge;
    private LoadingView loadingView;
    private View viewTitle;
    private int status;
    public static final int WEBVIEW_STATUS_NOMAL=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_mobile_data);
        viewTitle = findViewById(R.id.title);
        btnBack = findViewById(R.id.btn_back);
        btnRecharge = findViewById(R.id.btn_recharge);
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        btnBack.setOnClickListener(this);
        btnRecharge.setOnClickListener(this);
        initViews();
        UmengUtil.onEvent(MobileDataAct.this,new UmengUtil().ADDDATA_ENTER,null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver reContentResolver = getContentResolver();
            Uri contactData = data.getData();
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            if (cursor.moveToFirst()) {
                String userNumber;
                userNumber = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phone = reContentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                + contactId, null, null);
                while (phone.moveToNext()) {
                    userNumber = phone
                            .getString(phone
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (userNumber.contains("-")) {
                        userNumber = userNumber.replaceAll("-", "");
                    }
                    if (userNumber.contains(" ")) {
                        userNumber = userNumber.replaceAll(" ", "");
                    }
                    if (userNumber.contains("+86")) {
                        userNumber = userNumber.substring(3);
                    }
                    if (userNumber.length() == 11) {
                        webView.loadUrl("javascript:showMobile('" + userNumber + "')");
                    } else {
                        ViewUtils.showToast("手机号码有误");
                    }
                }

            } else {
                ViewUtils.showToast("请开启读取联系人权限");
            }

        }
    }

    CallBack callBack = new CallBack() {
        @Override
        public void onCall() {
            String token = PreferUtil.getInstance().getLoginToken();
            StringBuilder builder = new StringBuilder();
            builder.append(token).append(",");
            builder.append(Config.imei).append(",");
            builder.append(System.currentTimeMillis()).append(",");
            builder.append(1).append(",");//设备类型1代表android
            builder.append(Account.user.getShopId());
            webView.loadUrl("javascript:setUserInfo('" + builder.toString() + "')");
            webView.loadUrl("javascript:appNotificationFn('" + Account.hasLogin() + "')");
        }
    };

    private void initViews() {
        browserUrl = Config.dataBaseUrl + "/static/flow/recharge.html?shopId=" + Account.user.getShopId();
        webView = (WebView) findViewById(R.id.web_view);
        // 支持JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        // 支持缩放
        webView.getSettings().setBuiltInZoomControls(false);
        // 支持保存数据
        webView.getSettings().setSaveFormData(false);
        // 清除缓存
        webView.clearCache(true);
        // 清除历史记录
        webView.clearHistory();

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            /** 开始载入页面 */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                setProgressBarIndeterminateVisibility(true);// 设置标题栏的滚动条开始
//                browserUrl = url;
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }

            /** 错误返回 */
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
//                webView.loadData("", "text/html", "utf_8");
                ViewUtils.setGone(webView);
                loadingView.onLoadingFail();
                status = errorCode;
            }

            /** 页面载入完毕 */
            @Override
            public void onPageFinished(WebView view, String url) {
                setProgressBarIndeterminateVisibility(false);// 设置标题栏的滚动条停止
                super.onPageFinished(view, url);
                if(status==WEBVIEW_STATUS_NOMAL) {
                    ViewUtils.setVisible(webView);
                    loadingView.onLoadingComplete();
                }
                ViewUtils.setVisible(viewTitle);
                String token = PreferUtil.getInstance().getLoginToken();
                StringBuilder builder = new StringBuilder();
                builder.append(token).append(",");
                builder.append(Config.imei).append(",");
                builder.append(System.currentTimeMillis()).append(",");
                builder.append(1).append(",");//设备类型1代表android
                builder.append(Account.user.getShopId());
                webView.loadUrl("javascript:setUserInfo('" + builder.toString() + "')");
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        status=WEBVIEW_STATUS_NOMAL;
        loadingView.startLoading();
        webView.loadUrl(browserUrl);
        webView.addJavascriptInterface(new RemoteInvokeService(MobileDataAct.this, webView, callBack), "js_invoke");
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                status=WEBVIEW_STATUS_NOMAL;
                loadingView.startLoading();
                webView.loadUrl(browserUrl);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                UmengUtil.onEvent(MobileDataAct.this,new  UmengUtil().ADDDATA_BACK_CLICK,null);
                break;
            case R.id.btn_recharge:
                RechargeRecordAct.start(MobileDataAct.this);
                UmengUtil.onEvent(MobileDataAct.this,new UmengUtil().ADDDATA_RECORDS_CLICK,null);
                break;
        }
    }

}
