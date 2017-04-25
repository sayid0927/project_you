package com.zxly.o2o.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshScrollView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CollectArticleRequest;
import com.zxly.o2o.request.MarkShareRequest;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.request.PlatFormArticleRequest;
import com.zxly.o2o.request.PraiseArticleRequest;
import com.zxly.o2o.service.RemoteInvokeService;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.CallBackWithParam;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by hejun on 2016/8/17.
 * 平台专享文章详情页面
 * 由于之前的平台专享文章是直接打开-WebViewAct.java页面 该页面有多处调用且类型较多 加上本次文章优化对平台专享页面等逻辑有新需求
 * 故新增此页面将其分离出来
 */
public class ArticlePlatformVipAct extends BasicAct implements View.OnClickListener{

    private String loadUrl;
    private LoadingView loadingview;
    private WebView webView;
    static ShareInfo shareInfo;
    private ShareDialog shareDialog;
    private RelativeLayout back;
    private ImageView btn_dianzan;
    private ImageView btn_collect;
    private long articleId;
    private TextView txt_dianzan_count;
    private String userId;
    private int articleType;
    private String url;
    private byte status;
    public static CallBackWithParam callBackWithParam;

    private Animation animation;
    NoDoubleClickListener noDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.img_dianzan:
                    if(Account.user==null){
                        ViewUtils.startActivity(new Intent(ArticlePlatformVipAct.this, LoginAct.class), ArticlePlatformVipAct.this);
                        ViewUtils.showToast(getResources().getString(R.string.actions_must_sign));
                    }
                    if(MyCircleRequest.shopArticle.getIsPraise()==0){
                        ViewUtils.showToast("介么喜欢呀，已经赞过了哦…");
                        return;
                    }
                    PraiseArticleRequest praiseArticleRequest = new PraiseArticleRequest(2, articleId);
                    praiseArticleRequest.start();
                    praiseArticleRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                        @Override
                        public void onOK() {
                            animation = AnimationUtils.loadAnimation(ArticlePlatformVipAct.this, R.anim.text_operate_anim);
                            btn_dianzan.setBackgroundResource(R.drawable.btn_dianzan_press);
                            MyCircleRequest.shopArticle.setIsPraise(0);
                            if(MyCircleRequest.shopArticle.getPraiseAmount()==0){
                                findViewById(R.id.txt_dianzan_count).setVisibility(View.VISIBLE);
                                ((TextView)findViewById(R.id.txt_dianzan_count)).setText(1+"");
                                ((TextView)findViewById(R.id.txt_dianzan_count)).setTextColor(getResources().getColor(R.color.orange_ff5f19));
                            }else{
                                ((TextView)findViewById(R.id.txt_dianzan_count)).setText(MyCircleRequest.shopArticle.getPraiseAmount()+1+"");
                                ((TextView)findViewById(R.id.txt_dianzan_count)).setTextColor(getResources().getColor(R.color.orange_ff5f19));
                            }
                            operate_text_anim_comments.setVisibility(View.VISIBLE);
                            operate_text_anim_comments.startAnimation(animation);
                            mMainHandler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    operate_text_anim_comments.setVisibility(View.INVISIBLE);
                                }
                            }, 1000);
                            if(callBackWithParam!=null){
                                callBackWithParam.onCall(1);
                            }
                        }

                        @Override
                        public void onFail(int code) {

                        }
                    });
                    break;
                case R.id.img_collect:

                    if(Account.user==null){
                        ViewUtils.startActivity(new Intent(ArticlePlatformVipAct.this, LoginAct.class), ArticlePlatformVipAct.this);
                        ViewUtils.showToast(getResources().getString(R.string.actions_must_sign));
                    }

                    //已经收藏过就传2 还没收藏就传1
                    CollectArticleRequest articleCollectRequst = new CollectArticleRequest(2,articleId, MyCircleRequest.shopArticle.getIsCollected()==1?1:2);
                    articleCollectRequst.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

                        @Override
                        public void onOK() {
                            ViewUtils.showToast("操作成功!");
                            btn_collect.setBackgroundResource(MyCircleRequest.shopArticle.getIsCollected()==1?R.drawable.btn_collect_press:R.drawable.btn_collect_normal);
                            if(MyCircleRequest.shopArticle.getIsCollected()==1){
                                MyCircleRequest.shopArticle.setIsCollected(0);
                            }else {
                                MyCircleRequest.shopArticle.setIsCollected(1);
                                if(isFromCollect&&status!=2){
                                    findViewById(R.id.img_collect).setEnabled(false);
                                }
                            }
                        }

                        @Override
                        public void onFail(int code) {
                        }
                    });
                    articleCollectRequst.start(ArticlePlatformVipAct.this);
                    break;

            }
        }
    };
    private boolean isFromCollect;
    private TextView operate_text_anim_comments;
    private Bitmap bmp;
    private PullToRefreshScrollView srollView;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                goBack();
                break;

            case R.id.img_share:
                if(isFromCollect&&status!=2){
                    return;
                }
                if(Account.user==null){
                    ViewUtils.startActivity(new Intent(ArticlePlatformVipAct.this, LoginAct.class), ArticlePlatformVipAct.this);
                    ViewUtils.showToast(getResources().getString(R.string.actions_must_sign));
                    return;
                }
                if(shareDialog==null)
                    shareDialog=new ShareDialog();
                shareDialog.show(shareInfo.getTitle(), shareInfo.getDesc(),shareInfo.getUrl(),MyCircleRequest.shopArticle.getHead_url(), new ShareListener() {
                    @Override
                    public void onComplete(Object var1) {
                        new MarkShareRequest(2,articleId).start();
                        ViewUtils.showToast("分享成功");
                    }

                    @Override
                    public void onFail(int errorCode) {

                    }
                });
                break;
        }
    }

    protected void goBack(){
            finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        findViewById(R.id.img_comments).setVisibility(View.GONE);
        loadUrl =getIntent().getStringExtra("loadUrl");
        articleId = getIntent().getLongExtra("id", 0);
        articleType = getIntent().getIntExtra("articleType", 0);
        status = getIntent().getByteExtra("status", (byte)2);
        isFromCollect =getIntent().getBooleanExtra("isFromCollect",false);
        back = (RelativeLayout) findViewById(R.id.button_back);
        back.setOnClickListener(this);
        btn_dianzan = (ImageView) findViewById(R.id.img_dianzan);
        btn_collect = (ImageView) findViewById(R.id.img_collect);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        srollView = (PullToRefreshScrollView) findViewById(R.id.srollView);
        ViewUtils.setRefreshText(srollView);
        loadingview.startLoading();
        srollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //产品说要体验一致
                if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    mMainHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            srollView.onRefreshComplete();
                        }
                    }, 500);

                } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    mMainHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            srollView.onRefreshComplete();
                        }
                    }, 500);
                }
            }
        });
        //点赞块
        operate_text_anim_comments = (TextView) findViewById(R.id.operate_text_anim_comments);
        initWebView();
        loadArticleInfo(articleId);
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
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
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingview.onLoadingComplete();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                loadingview.onLoadingFail("啊偶~网络不给力...",true);
            }
        });

        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                webView.reload();
            }
        });

        RemoteInvokeService jsObj = new RemoteInvokeService(this, webView, new CallBack() {
            @Override
            public void onCall() {

            }
        });

        webView.addJavascriptInterface(jsObj, "js_invoke");
    }

    private void loadArticleInfo(final long id) {
        userId = Account.user==null ? "" : "&userId="+Account.user.getId();
        String commonUrl=Config.articleUrl+"/articleDetail.html?shopId="+ Config.shopId +"&baseUrl=" + DataUtil.encodeBase64(Config.dataBaseUrl)+"&brandName=" + Build.BRAND + "&DeviceID=" + Config.imei + "&DeviceType=1"+ userId +"&articleType="+articleType+"&articleId="+id;
        url = commonUrl+"&from=app";

        ArticlePlatformVipAct.shareInfo.setUrl(commonUrl+"&from=userapp");
        PlatFormArticleRequest platFormArticleRequest = new PlatFormArticleRequest(id);
        platFormArticleRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                initViews();
            }

            @Override
            public void onFail(int code) {

            }
        });
        platFormArticleRequest.start();
    }

    private void loadH5(String loadUrl) {
//        loadingview.startLoading();
        webView.loadUrl(loadUrl);
    }

    private void initViews() {
        status=MyCircleRequest.shopArticle.getStatus();
        if(isFromCollect){
            if(status!=2){
                loadingview.onDataEmpty("哎~,文章不存在了...",R.drawable.kb_icon_d);
            }
        }

        if(isFromCollect){
            if(status==2){
                loadH5(url);
            }
        }else {
            loadH5(url);
        }
        shareDialog =new ShareDialog();
        btn_dianzan.setOnClickListener(noDoubleClickListener);
        btn_collect.setOnClickListener(noDoubleClickListener);
        findViewById(R.id.img_share).setOnClickListener(this);
        //点赞数
        txt_dianzan_count = (TextView) findViewById(R.id.txt_dianzan_count);

        //已收藏
        if(MyCircleRequest.shopArticle.getIsCollected()==0){
            btn_collect.setBackgroundResource(R.drawable.btn_collect_press);
        }else {
            btn_collect.setBackgroundResource(R.drawable.btn_collect_normal);
            if(isFromCollect){
                if(status!=2){
                    btn_collect.setEnabled(false);
                }
            }

        }
        if(isFromCollect){
            if(status!=2){
                btn_dianzan.setBackgroundResource(R.drawable.icon_dianzan_grey);
                findViewById(R.id.img_share).setBackgroundResource(R.drawable.icon_share_grey);
                ((TextView)findViewById(R.id.txt_dianzan_count)).setTextColor(getResources().getColor(R.color.gray_dcdcdc));
                btn_dianzan.setEnabled(false);
                findViewById(R.id.img_share).setEnabled(false);
                return;
            }
        }

        if(MyCircleRequest.shopArticle.getIsPraise()==0){
            btn_dianzan.setBackgroundResource(R.drawable.btn_dianzan_press);
            ((TextView)findViewById(R.id.txt_dianzan_count)).setTextColor(getResources().getColor(R.color.orange_ff5f19));
        }else{
            btn_dianzan.setBackgroundResource(R.drawable.btn_dianzan_normal);
        }

        //初始化点赞、收藏显示图标
        if(MyCircleRequest.shopArticle.getPraiseAmount()==0){
            txt_dianzan_count.setVisibility(View.GONE);
        }else{

            txt_dianzan_count.setText(MyCircleRequest.shopArticle.getPraiseAmount()+"");
        }

    }

    public int getLayoutId(){
        return R.layout.win_platform_vip;
    }

    @Override
    public void onPause() {
        //webView.reload();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareInfo=null;
        if(webView!=null)
        webView.destroy();
        callBackWithParam=null;
    }

    /**
     * @param title 文章标题
     * @param shareInfo 文章分享数据对象
     * @param articleType
     * @param shopArticle 文章对象
     */
    public static void start(String title, ShareInfo shareInfo, int articleType, ShopArticle shopArticle,boolean isFromCollect, CallBackWithParam callBack) {
        callBackWithParam = callBack;
        Intent intent = new Intent(AppController.getInstance().getTopAct(), ArticlePlatformVipAct.class);
        intent.putExtra("title", title);
        intent.putExtra("loadUrl",shopArticle.getUrl());
        intent.putExtra("id",shopArticle.getId());
        intent.putExtra("articleType",articleType);
        intent.putExtra("status",shopArticle.getStatus());
        intent.putExtra("isFromCollect",isFromCollect);
        shareInfo.setDesc(shopArticle.getContent());
        ArticlePlatformVipAct.shareInfo=shareInfo;
        ViewUtils.startActivity(intent, AppController.getInstance().getTopAct());
    }
}
