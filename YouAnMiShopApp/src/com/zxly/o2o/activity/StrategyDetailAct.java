package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.MakeMoneyArticle;
import com.zxly.o2o.request.ShopAppArticleRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2016/1/12.
 */
public class StrategyDetailAct extends BasicAct implements View.OnClickListener{
    private ShareDialog shareDialog;
    private static MakeMoneyArticle makeMoneyArticle;
    private String loadUrl;
    private String title;
    private LoadingView loadingview = null;
    private WebView webView;
    private int status;
    public static final int WEBVIEW_STATUS_NOMAL=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_h5_detail);
        title = getIntent().getStringExtra("title");
        loadUrl=getIntent().getStringExtra("loadUrl");
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText(title);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                status=WEBVIEW_STATUS_NOMAL;
                loadingview.startLoading();
                webView.loadUrl(loadUrl);
            }
        });
        webView = ((WebView) findViewById(R.id.web_view));
        ViewUtils.setGone(webView);
        status=WEBVIEW_STATUS_NOMAL;
        loadH5(loadUrl);
        new ShopAppArticleRequest(makeMoneyArticle.getId()).start();
        UmengUtil.onEvent(StrategyDetailAct.this,new UmengUtil().MONEY_GUIDE_ARTICLE_ENTER,null);
    }
    public static void start(Activity curAct, String title,MakeMoneyArticle _makeMoneyArticle) {
        makeMoneyArticle=_makeMoneyArticle;
        Intent intent = new Intent(curAct, StrategyDetailAct.class);
        intent.putExtra("title", title);
        intent.putExtra("loadUrl", _makeMoneyArticle.getUrl());
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        makeMoneyArticle=null;
    }
    private void loadH5(String loadUrl) {
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        //	webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSaveFormData(false); // 支持保存数据
        webView.clearCache(true); // 清除缓存
        webView.clearHistory(); // 清除历史记录
        webView.getSettings().setAppCacheEnabled(true);//是否使用缓存
        webView.getSettings().setDomStorageEnabled(true);//DOM Storage

        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            /** 错误返回 */
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
//                webView.loadData("", "text/html", "utf_8");
                ViewUtils.setGone(webView);
                loadingview.onLoadingFail();
                status = errorCode;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                loadingview.startLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //	ViewUtils.setVisible(webView);
                //loadingview.startLoading();
                if(status==WEBVIEW_STATUS_NOMAL) {
                    ViewUtils.setVisible(webView);
                    loadingview.onLoadingComplete();
                }
            }
        });
        loadingview.startLoading();
        webView.loadUrl(loadUrl);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_share:
                if (shareDialog == null) {
                    shareDialog = new ShareDialog();
                }
                shareDialog.show(makeMoneyArticle.getTitle(),makeMoneyArticle.getContent(), makeMoneyArticle.getUrl(), makeMoneyArticle.getHeadUrl(), new ShareListener() {
                    @Override
                    public void onComplete(Object var1) {

                    }

                    @Override
                    public void onFail(int errorCode) {

                    }
                });
                UmengUtil.onEvent(StrategyDetailAct.this,new UmengUtil().MONEY_GUIDE_SHARE_CLICK,null);
                break;
        }
    }
}
