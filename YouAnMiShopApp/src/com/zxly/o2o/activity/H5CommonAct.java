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

import com.zxly.o2o.account.Account;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2016/6/30.
 * 此类封装了些公共部分，有特殊要求的可继承此类
 */
public class H5CommonAct extends  BasicAct implements  View.OnClickListener{

    private LoadingView loadingview;
    private WebView webView;
    private String loadUrl;
    private String title;
    private int pageType;//1:柚安米广告 2: 其它（不用特殊处理）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        View btnShare=findViewById(R.id.btn_share);
        ViewUtils.setGone(btnShare);
        findViewById(R.id.btn_back).setOnClickListener(this);
        title = getIntent().getStringExtra("title");
        loadUrl=getIntent().getStringExtra("loadUrl");
        pageType=getIntent().getIntExtra("pageType",2);
        if(title != null){
            ((TextView) findViewById(R.id.txt_title)).setText(title);
        }
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        webView = ((WebView) findViewById(R.id.web_view));
        initWebView();
        loadingview.startLoading();
        webView.loadUrl(loadUrl);
    }
    public static void start(Activity curAct,String loadUrl,String title)
    {
        Intent intent = new Intent(curAct, H5CommonAct.class);
        intent.putExtra("title", title);
        intent.putExtra("loadUrl",loadUrl);
        ViewUtils.startActivity(intent, curAct);
    }
    public static void start(Activity curAct,String loadUrl,String title,int pageType)
    {
        Intent intent = new Intent(curAct, H5CommonAct.class);
        intent.putExtra("title", title);
        intent.putExtra("loadUrl",loadUrl);
        intent.putExtra("pageType",pageType);
        ViewUtils.startActivity(intent, curAct);
    }
    public boolean getShouldOverrideUrlLoading()
    {
        return  true;
    }
    private void initWebView()
    {
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
                loadingview.startLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                loadingview.onLoadingComplete();
            }
        });
    }
    public int getLayoutId(){
        return R.layout.win_h5_detail;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_back)
        {
            goBack();
        }
    }
    @Override
    public void onBackPressed() {
        goBack();
    }
    private void goBack()
    {
        if(pageType==1)//柚安米广告
        {

            if (Account.user != null) {
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_left_out,R.anim.slide_left_in);
            } else {
                LoginAct.start(H5CommonAct.this);
            }

        }
        finish();
    }

}
