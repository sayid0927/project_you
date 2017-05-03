package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chatuidemo.HXConstant;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.request.GetuiMsgCancleRequest;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.request.SysNoticReadCountRequest;
import com.easemob.easeui.widget.EaseMyFlipperView;
import com.easemob.easeui.widget.MyWebView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.shop.R;

/**
 * Created by hejun on 2016/8/19.
 * 个推消息详情页面 之前是商户用户统一在project中一个页面  后因为不同app端需要不同的跳转  所以商户用户分别写了一个
 * （里面的布局就用的同一个）
 */
public class GetuiShopH5DetailAct extends BasicAct implements View.OnClickListener{
    private MyWebView webView;
    private String loadUrl;
    private String title;
    protected EaseMyFlipperView viewContainer;
    private boolean isFromNotice;
    private int what;
    private RelativeLayout layout_cancle;
    //消息是否撤回  撤回则应该加在页面 默认应该加载
    private boolean shouldLoad=false;
    private SysNoticReadCountRequest sysNoticReadCountRequest;
    static final int SYS_NOTICE=201;
    private boolean isFromNotification;
    private boolean isDeleteNotice;
    private ImageView loading_fail_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.easemob.chatuidemo.R.layout.ease_h5_detail);

        if(Account.user==null){
            LoginAct.start(GetuiShopH5DetailAct.this);
            Toast.makeText(GetuiShopH5DetailAct.this,"请登录",Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        //阅读统计
        if(isFinishing()){
            return;
        }

        setFlipper();
        title = getIntent().getStringExtra("title");
        loadUrl = getIntent().getStringExtra("sys_msg_url");
        isFromNotice = getIntent().getBooleanExtra("isFromNotice", false);
        what = getIntent().getIntExtra("what", 0);
        //是否是从通知栏进入
        isFromNotification = getIntent().getBooleanExtra("isFromNotification",false);
        initViews();
        //商户后台与平台发的通知才需要请求确认是否撤销
        if(EaseConstant.shopID>0){
            //用户端可撤回消息分别为：柚安米公告 运营文章 门店公告
            if(what==HXConstant.SYS_NOTIC_KEY_USER||what==HXConstant.SYS_YAMARTICLE_KEY_USER||what==HXConstant.SYS_ARTICLE_KEY_USER||what==HXConstant.SYS_SHOPNOTICE_KEY_USER){
                checkMsgHasCancle();
            }else if(loadUrl != null){
                //其他类型不做访问是否被撤回请求
                loadH5(loadUrl);
            }

        }else{
            //商户可撤回消息分别为：柚安米公告  SYS_NOTICE为柚安米中一类消息
            if(what==HXConstant.SYS_NOTIC_KEY_SHOP||what==SYS_NOTICE){
                checkMsgHasCancle();
            }else if(loadUrl != null){
                loadH5(loadUrl);
            }
        }
    }

    private void NoticeRequest() {
        sysNoticReadCountRequest = new SysNoticReadCountRequest(getIntent().getLongExtra("id",0));
        sysNoticReadCountRequest.start();
    }

    private void checkMsgHasCancle() {
        GetuiMsgCancleRequest getuiMsgCancleRequest = new GetuiMsgCancleRequest(what, getIntent().getLongExtra("id", 0));
        getuiMsgCancleRequest.setOnResponseStateListener(new HXNormalRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                // 成功状态则说明消息已经被撤回
                layout_cancle.setVisibility(View.VISIBLE);
                if(EaseConstant.shopID<0){
                    loading_fail_imageView.setBackgroundResource(R.drawable.img_default_shy);
                }
                //是否是被撤回消息
                isDeleteNotice = true;
            }

            @Override
            public void onFail(int code) {
                layout_cancle.setVisibility(View.GONE);
                if(loadUrl != null){
                    loadH5(loadUrl);
                }
            }
        });
        getuiMsgCancleRequest.start();
    }

    private void setFlipper() {
        if (viewContainer == null) {
            viewContainer = (EaseMyFlipperView) findViewById(com.easemob.chatuidemo.R.id.view_loading);
        }
    }

    private void initViews() {
        findViewById(com.easemob.chatuidemo.R.id.tag_title_btn_back).setOnClickListener(this);
        switch (what){
            case HXConstant.SYS_NOTIC_KEY_USER:
            case HXConstant.SYS_SHOPNOTICE_KEY_USER:
            case HXConstant.SYS_NOTIC_KEY_SHOP:
                ((TextView) findViewById(com.easemob.chatuidemo.R.id.tag_title_title_name)).setText("公告详情");
                break;
            case HXConstant.SYS_YAMARTICLE_KEY_USER:
            case HXConstant.SYS_ARTICLE_KEY_USER:
                ((TextView) findViewById(com.easemob.chatuidemo.R.id.tag_title_title_name)).setText("文章详情");
                break;
            case HXConstant.SYS_ACTIVITY_KEY_USER:
                ((TextView) findViewById(com.easemob.chatuidemo.R.id.tag_title_title_name)).setText("活动详情");
                break;
            default:
                ((TextView) findViewById(com.easemob.chatuidemo.R.id.tag_title_title_name)).setText("公告详情");
                break;
        }
        webView = ((MyWebView) findViewById(com.easemob.chatuidemo.R.id.web_view));
        //撤回布局
        layout_cancle = (RelativeLayout) findViewById(com.easemob.chatuidemo.R.id.msg_cancle);
        layout_cancle.setVisibility(View.GONE);
        loading_fail_imageView = (ImageView) findViewById(com.easemob.chatuidemo.R.id.loading_fail_imageView);
    }

    public static void start(Activity curAct, String url, String title, long id, boolean isFromNotice, int what) {
        Intent intent = new Intent(curAct, GetuiShopH5DetailAct.class);
        intent.putExtra("sys_msg_url", url);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.putExtra("isFromNotice", isFromNotice);
        intent.putExtra("what", what);
        EaseConstant.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, String url,String title,long id) {
        Intent intent = new Intent(curAct, GetuiShopH5DetailAct.class);
        intent.putExtra("sys_msg_url", url);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        EaseConstant.startActivity(intent, curAct);
    }

    private void loadH5(String loadUrl) {
        NoticeRequest();

        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSaveFormData(false); // 支持保存数据
        webView.clearCache(true); // 清除缓存
        webView.clearHistory(); // 清除历史记录
        webView.getSettings().setAppCacheEnabled(false);//是否使用缓存
        webView.getSettings().setDomStorageEnabled(true);//DOM Storage

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);



//		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setSupportZoom(true);
//		webView.getSettings().setBuiltInZoomControls(true);
//		webView.getSettings().setUseWideViewPort(true);
////		webView.setIsIntercept(true);
//		webView.getSettings().setLayoutAlgorithm(
//				WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//		webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(loadUrl);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
                viewContainer.setDisplayedChild(EaseMyFlipperView.LOADSUCCESSFUL,true);
            }
        });
    }



    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == com.easemob.chatuidemo.R.id.tag_title_btn_back) {
            finish();

        }
    }
}
