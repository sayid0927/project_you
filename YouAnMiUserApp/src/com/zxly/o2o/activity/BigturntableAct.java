package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.Banners;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.DzpInfoRequest;
import com.zxly.o2o.service.RemoteInvokeService;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * @author wuchenhui 2015-10-21
 * @description 大转盘活动界面
 */
public class BigturntableAct extends BasicAct implements View.OnClickListener {
    private WebView webView;
    private String browserUrl;
    private View btnBack;
    private LoadingView loadingView;

    private  static Banners banner;
    RemoteInvokeService jsObj;
    private boolean isOpenFromUrl;
    private ShareDialog shareDialog;
    private String name, remark;
    private String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wim_bigturntabe);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        loadingView = (LoadingView) findViewById(R.id.view_loading);

        initViews();
        loadDzpInfo();
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
            builder.append(Config.shopId);
            if(!StringUtil.isNull(token)){
                webView.loadUrl("javascript:setUserInfo('" + builder.toString() + "')");
                webView.loadUrl("javascript:appNotificationFn('" + Account.hasLogin() + "')");
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Account.user.getId()).append(",");
            stringBuilder.append(token);
            webView.loadUrl("javascript:getUserId('" + stringBuilder.toString() + "')");
        }
    };




    private void initViews() {
       String url= getIntent().getStringExtra("url");

        if(!DataUtil.stringIsNull(url)){
           isOpenFromUrl=true;
            browserUrl=url;
        }else {
            browserUrl =banner.getShareUrl()+"&DeviceID="+Config.imei+
                    "&Authorization="+PreferUtil.getInstance().getLoginToken()+
                    "&type="+Constants.OPEN_FROM_APP+"&brand="+Build.BRAND+"&TargetID=" + Config.shopId;
            if (Account.hasLogin()) {
                browserUrl = browserUrl + "&userId=" + Account.user.getId();
            }

//            browserUrl="http://192.168.1.160/style/zhuanpan_app/zhuanpan.html?drawId=255&baseUrl=http://192.168.1.33:28008/&shopId=1&DeviceID=4d8a64d6263f0e74&Authorization=null&type=1&shopId=1&baseUrl=http://192.168.1.33:28005/&brand=iPhone&TargetID=1&userId=";
//            if(Account.hasLogin()){
//                browserUrl = browserUrl + Account.user.getId();
//            }
        }

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

        webView.setVisibility(View.GONE);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            /** 开始载入页面 */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                setProgressBarIndeterminateVisibility(true);// 设置标题栏的滚动条开始
                browserUrl = url;
                super.onPageStarted(view, url, favicon);
                loadingView.startLoading();
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
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            /** 页面载入完毕 */
            @Override
            public void onPageFinished(WebView view, String url) {
                setProgressBarIndeterminateVisibility(false);// 设置标题栏的滚动条停止
                super.onPageFinished(view, url);
//                String token = PreferUtil.getInstance().getLoginToken();
//                StringBuilder builder = new StringBuilder();
//                builder.append(token).append(",");
//                builder.append(Config.imei).append(",");
//                builder.append(System.currentTimeMillis()).append(",");
//                builder.append(1).append(",");//设备类型1代表android
//                builder.append(Config.shopId);
//                webView.loadUrl("javascript:setUserInfo('" + builder.toString() + "')");
             //   jsObj.doShare();
                loadingView.onLoadingComplete();
                webView.setVisibility(View.VISIBLE);
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        webView.loadUrl(browserUrl);
        jsObj=new RemoteInvokeService(BigturntableAct.this, banner, callBack);
        webView.addJavascriptInterface(jsObj, "js_invoke");


    }

    private void loadDzpInfo(){
        final DzpInfoRequest dzpInfoRequest = new DzpInfoRequest(banner.getProductId(), Config.shopId);
        dzpInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                name = dzpInfoRequest.getName();
                remark = dzpInfoRequest.getRemark();
            }

            @Override
            public void onFail(int code) {

            }
        });
        dzpInfoRequest.start(this);
    }


    private void goBack(){
        if(isOpenFromUrl){
            Intent intent = new Intent(this, HomeAct.class);
            ViewUtils.startActivity(intent, this);
            finish();
        }else{
            finish();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        goBack();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                goBack();
                break;
            case R.id.btn_share:
                if(name == null || remark == null){
                    return;
                }
                if (shareDialog == null) {
                    shareDialog = new ShareDialog();
                }
                Object shareImage = BitmapUtil.drawableToBitmap(getResources().getDrawable(R.drawable.icon_share_dzp));
                if(StringUtil.isNull(appName))
                {
                    appName="【"+getResources().getString(R.string.app_name)+"】";
                }
                StringBuilder  sb=new StringBuilder(banner.getShareUrl());
                sb.append("&DeviceID=").append(Config.imei);
                sb.append("&type=").append(Constants.OPEN_FROM_SHARE);
                if (Account.hasLogin()) {
                    sb.append("&userId=").append(Account.user.getId());
                }
                sb.append("&title=").append(appName).append(name).append("&desc=").append(remark);
                shareDialog.show(appName+name, remark, sb.toString(), shareImage,"icon_share_dzp.png", null);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        banner=null;
    }

    public static void start(Activity curAct, Banners banner) {
        Intent intent = new Intent(curAct, BigturntableAct.class);
        BigturntableAct.banner=banner;
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, String url) {
        Intent intent = new Intent(curAct, BigturntableAct.class);
        intent.putExtra("url",url);
        ViewUtils.startActivity(intent, curAct);
    }

}
