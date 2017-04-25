package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.widget.MyWebView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.HotArticleDetailAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.dialog.ArticleCommentDialog;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.ArticleReply;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CollectArticleRequest;
import com.zxly.o2o.request.GetReplyRequest;
import com.zxly.o2o.request.MarkShareRequest;
import com.zxly.o2o.request.NewShopInfoRequest;
import com.zxly.o2o.request.PraiseArticleRequest;
import com.zxly.o2o.request.SendCommentsRequest;
import com.zxly.o2o.service.RemoteInvokeService;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.CallBackWithParam;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.MyFlipperView;

import java.util.List;

/**
 * Created by hejun on 2016/9/21.
 * 店铺热文页面
 */
public class ShopHotArticleAct extends BasicAct implements PullToRefreshBase.OnRefreshListener, View.OnClickListener {

    private  ShareInfo shareInfo;
    private PullToRefreshListView listView;
    private LinearLayout win_myarticle_bottom_view;
    private MyFlipperView viewContainer;
    private Animation animation;
    private HotArticleDetailAdapter objectAdapter;
    private View headerView;
    private ImageView img_dianzan;
    private ImageView img_collect;
    private ImageView img_share;
    private RelativeLayout edittextHead;
    private int edittextHeadHeight;
    private EditText publishForumContent;
    private InputMethodManager imm;
    private long shopArticleId;
    private String url;
    private GetReplyRequest myRequest;
    private int pageIndex;
    private ShopArticle shopArticle;
    private String loadUrl;
    private int articleType;
    private String userId;
    private View footView;
    private ShareDialog shareDialog;
    private byte status;
    private LoadingView loadingview;
    private ArticleCommentDialog articleCommentDialog;
    private boolean showNoNet;
    private MyWebView mWebView;
    public static CallBackWithParam callBackWithParam;
    private boolean isFromCollect;
    private TextView operate_text_anim_comments;
    private String articleTitle;
    private TextView nodata_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_shophot_article);
        shopArticleId = getIntent().getLongExtra("shopArticleId", 0);
        url = getIntent().getStringExtra("url");
        articleType = getIntent().getIntExtra("articleType", 0);
        articleTitle=getIntent().getStringExtra("articleTitle");
        status = getIntent().getByteExtra("status",(byte) 2);
        isFromCollect = getIntent().getBooleanExtra("isFromCollect", false);
        shareInfo=new ShareInfo();
        shareInfo.setUrl(url);
        shareInfo.setTitle(articleTitle);
        shareInfo.setDesc(getIntent().getStringExtra("content"));
        findView();
        initView();
    }

    private void initView() {
        setArticleTopData();
        final NewShopInfoRequest newShopInfoRequest = new NewShopInfoRequest(shopArticleId);
        newShopInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                shopArticle = newShopInfoRequest.getShopArticle();
                setView(shopArticle);
            }

            @Override
            public void onFail(int code) {
                loadingview.onLoadingFail("啊偶~网络不给力...",true);

            }
        });
        newShopInfoRequest.start();
    }

    private void loadListData(final int page) {
        myRequest = new GetReplyRequest(shopArticleId,page );
        myRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<ArticleReply> replyArticleModelList = myRequest.getReplyArticleModelList();
                listView.onRefreshComplete();
                if(replyArticleModelList.isEmpty()){

                    if (page == 1) {
                        objectAdapter.clear();
                        objectAdapter.notifyDataSetChanged();
                        setNoDate(page,View.VISIBLE);
//                        viewContainer.setDisplayedChild(2,false);
                    } else {
                        setNoDate(page,View.VISIBLE);
                        ViewUtils.showToast("没有更多了");
                    }
                }else{
                    setNoDate(page,View.GONE);

                    if (page == 1) {
                        objectAdapter.clear();
                    }
                    objectAdapter.addItem(replyArticleModelList, true);
                    pageIndex++;
                }
            }

            @Override
            public void onFail(int code) {
//                viewContainer.setDisplayedChild(1,false);
                listView.onRefreshComplete();
                showNoNet = true;
                setNoDate(page,View.VISIBLE);
                loadingview.onLoadingFail("啊偶~网络不给力...",true);
            }
        });
        myRequest.start();
    }

    private void setArticleTopData() {
        mWebView = ((MyWebView) headerView.findViewById(R.id.article_detail_maimcontent));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);


        mWebView.setWebViewClient(new WebViewClient(){

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
                loadingview.onLoadingComplete();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                img_dianzan.setBackgroundResource(R.drawable.icon_dianzan_grey);
                findViewById(R.id.img_comments).setBackgroundResource(R.drawable.icon_pinglun_grey);
                img_share.setImageResource(R.drawable.icon_share_grey);
                img_dianzan.setEnabled(false);
                findViewById(R.id.img_comments).setEnabled(false);
                img_share.setEnabled(false);
                loadingview.onLoadingFail("啊偶~网络不给力...",true);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }


        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100) {
                            listView.setVisibility(View.VISIBLE);

                }
            }
        });


        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                initView();
                loadingview.startLoading();
//                mWebView.reload();
                mWebView.loadUrl(loadUrl);
            }
        });


        RemoteInvokeService jsObj = new RemoteInvokeService(this, mWebView, new CallBack() {
            @Override
            public void onCall() {

            }
        });

        mWebView.addJavascriptInterface(jsObj, "js_invoke");
    }


    private void setView(ShopArticle shopArticle) {
        if(isFromCollect){
            if(status!=2){
                loadingview.onDataEmpty("哎~,文章不存在了...",R.drawable.kb_icon_d);
            }
        }
        if(isFromCollect){
            if(status==2){
                mWebView.loadUrl(loadUrl);
            }
        }else {
            mWebView.loadUrl(loadUrl);
        }
        if(isFromCollect){
            if(status==2){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        loadingview.onLoadingComplete();
                        headerView.setVisibility(View.VISIBLE);
                        loadListData(1);
                    }
                },1000);
            }
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    loadingview.onLoadingComplete();
                    headerView.setVisibility(View.VISIBLE);
                    loadListData(1);
                }
            },1000);
//            loadListData(1);
        }
        if(shopArticle.getPraiseAmount()==0){
            findViewById(R.id.txt_dianzan_count).setVisibility(View.GONE);
        }else {
            findViewById(R.id.txt_dianzan_count).setVisibility(View.VISIBLE);
            if(shopArticle.getIsPraise()==0){
                ((TextView)findViewById(R.id.txt_dianzan_count)).setTextColor(Color.parseColor("#ff5f19"));
            }
            ((TextView)findViewById(R.id.txt_dianzan_count)).setText(shopArticle.getPraiseAmount()+"");
        }
        if(shopArticle.getIsCollected()==0){
            img_collect.setBackgroundResource(R.drawable.btn_collect_press);
        }else{
            img_collect.setBackgroundResource(R.drawable.btn_collect_normal);
        }

        if(shopArticle.getIsPraise()==0){
            img_dianzan.setBackgroundResource(R.drawable.btn_dianzan_press);
        }else{
            img_dianzan.setBackgroundResource(R.drawable.btn_dianzan_normal);
        }
        if(isFromCollect&&status!=2){
            img_dianzan.setBackgroundResource(R.drawable.icon_dianzan_grey);
            findViewById(R.id.img_comments).setBackgroundResource(R.drawable.icon_pinglun_grey);
            ((TextView)findViewById(R.id.txt_dianzan_count)).setTextColor(getResources().getColor(R.color.gray_dcdcdc));
            img_dianzan.setEnabled(false);
            findViewById(R.id.img_comments).setEnabled(false);
            img_share.setEnabled(false);
            img_share.setImageResource(R.drawable.icon_share_grey);
            return;
        }



    }

    private void findView() {
        userId = Account.user==null ? "" : "&userId="+Account.user.getId();
        StringBuilder shareUrl=new StringBuilder(Config.articleUrl);
        shareUrl.append("/articleDetail.html?shopId=").append(Config.shopId).append("&baseUrl=").append(DataUtil.encodeBase64(Config.dataBaseUrl))
                .append("&brandName=").append(Build.BRAND).append("&DeviceID=").append(Config.imei).append("&DeviceType=1").append(userId )
                .append("&articleType=").append(articleType).append("&articleId=").append(shopArticleId);
        String commonUrl=shareUrl.toString();
        loadUrl = commonUrl+"&from=app";
        shareInfo.setUrl(commonUrl+"&from=userapp");
        animation = AnimationUtils.loadAnimation(this, R.anim.text_operate_anim);
        listView = (PullToRefreshListView) findViewById(R.id.pull_to_refreshlistview);
        win_myarticle_bottom_view = (LinearLayout) findViewById(R.id.win_myarticle_bottom_view);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setIntercept(true);
        viewContainer = (MyFlipperView) findViewById(R.id.list_layout);
        ViewUtils.setRefreshText(listView);
        listView.setOnRefreshListener(this);
        //点赞块
        operate_text_anim_comments = (TextView) findViewById(R.id.operate_text_anim_comments);
        initHeadView();
        listView.addH(headerView);
        objectAdapter = new HotArticleDetailAdapter(this, animation, mMainHandler);
        objectAdapter.setIsshowHeadImage(false);
        listView.setAdapter(objectAdapter);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        loadingview.startLoading();
        //初始状态  评论部分(eidttext)设置不显示
        findViewById(R.id.win_myarticle_bottom_view).setVisibility(View.VISIBLE);
//        findViewById(R.id.win_mycircle_editinput_include_layout_new).setVisibility(View.GONE);
        //浮层返回按钮
        findViewById(R.id.button_back).setOnClickListener(this);
        //浮层点赞区
        img_dianzan = (ImageView) findViewById(R.id.img_dianzan);
        img_dianzan.setOnClickListener(noDoubleClickListener);
        img_collect = (ImageView) findViewById(R.id.img_collect);
        img_collect.setOnClickListener(noDoubleClickListener);
        img_share = (ImageView) findViewById(R.id.img_share);
        img_share.setOnClickListener(this);

       /* //edittext 的头部
        edittextHead = (RelativeLayout) findViewById(R.id.edittext_title);
        //由于有点击edittext其他任何区域 自动隐藏键盘的需求  故在此排除edittext的头部点击时不隐藏键盘（因为头部有发表点击事件 容易给用户带来不好的体验）
        int intw=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int inth=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        //故对头部高度进行测量
        edittextHead.measure(intw, inth);
        edittextHeadHeight = edittextHead.getMeasuredHeight();
        findViewById(R.id.comment_sendbtn_new).setOnClickListener(this); *//*        View view = findViewById(R.id.reply_pic_select_btn); view.setOnClickListener(this); view.setOnLongClickListener(this);*//*
        */
        findViewById(R.id.img_comments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Account.user==null){
                    ViewUtils.startActivity(new Intent(ShopHotArticleAct.this, LoginAct.class), ShopHotArticleAct.this);
                    ViewUtils.showToast(getResources().getString(R.string.actions_must_sign));
                    return;
                }
                if(articleCommentDialog==null){
                    articleCommentDialog = new ArticleCommentDialog();
                }
                articleCommentDialog.show(callBack);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {

                    //解决数据为空时refresh和headview滑动事件冲突问题
                    if (objectAdapter.getCount() == 0) {
                        if (view.getFirstVisiblePosition() == 0 &&
                                listView.getMode() != PullToRefreshBase.Mode
                                        .PULL_FROM_START) {
                            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        } else if (listView.getMode() != PullToRefreshBase.Mode.DISABLED) {
                            listView.setMode(PullToRefreshBase.Mode.DISABLED);
                        }

                    } else if (listView.getMode() != PullToRefreshBase.Mode.BOTH) {
                        listView.setMode(PullToRefreshBase.Mode.BOTH);
                    }

                    if (view.getFirstVisiblePosition() == 0) {
                        if (headerView == null) {
                            initHeadView();
                            listView.addH(headerView);

                        }
                    }
                    objectAdapter.isFastScrolling = false;
                } else if (scrollState == 1) {
                    objectAdapter.isFastScrolling = false;
                } else {
                    objectAdapter.isFastScrolling = true;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVifalsesibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        });
        //网页加载完成才显示评论
        listView.setVisibility(View.GONE);
        headerView.setVisibility(View.GONE);

        initDisplayPage();
    }

    CallBack callBack=new CallBack() {
        @Override
        public void onCall() {
            String content = articleCommentDialog.getContent();
            saveComment(content);
        }
    };

    private void initDisplayPage() {
        setFlipper();
        viewContainer.setDisplayedChild(3, true);
    }

    private void initHeadView() {
        headerView = this.getLayoutInflater().inflate(R.layout.article_detail_include_new, null);
    }



    /**
     * 点击edittext其他任何区域  如果键盘显示状态那么就自动隐藏
     * 在每次down事件的时候判断是否落在edittext上 是则拦截 不是则放开
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                    findViewById(R.id.win_mycircle_editinput_include_layout_new).setVisibility(View.GONE);
                    findViewById(R.id.win_mycircle_conments_include_layout).setVisibility(View.VISIBLE);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 判断是否在edittext上及计算edittext的区域
     * @param v
     * @param event
     * @return
     */
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            //高度减去edittext的头部高度 这样点击edittext的头部时键盘就不会隐藏
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top-edittextHeadHeight && event.getRawY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) { // 加载下拉数据
            listView.setSelection(1);
            pageIndex = 1;
            loadListData(pageIndex);
        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) { // 加载上拉数据
            loadListData(pageIndex);
        }
    }

    NoDoubleClickListener noDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.img_dianzan:
                    if(shopArticle==null){
                        return;
                    }
                    if(Account.user==null){
                        ViewUtils.startActivity(new Intent(ShopHotArticleAct.this, LoginAct.class), ShopHotArticleAct.this);
                        ViewUtils.showToast(getResources().getString(R.string.actions_must_sign));
                    }
                    if(shopArticle.getIsPraise()==0){
                        ViewUtils.showToast("介么喜欢呀，已经赞过了哦…");
                    }else {
                        dianzanRequest();
                    }
                    break;
                case R.id.img_collect:
                    if(shopArticle==null){
                        return;
                    }
                    if(Account.user==null){
                        ViewUtils.startActivity(new Intent(ShopHotArticleAct.this, LoginAct.class), ShopHotArticleAct.this);
                        ViewUtils.showToast(getResources().getString(R.string.actions_must_sign));
                    }
                    if(shopArticle.getIsCollected()==0){
                        collectRequest(2);
                        //如果是文章被禁 那么取消完赞后就不能继续点赞了
                        if(isFromCollect&&status!=2){
                            img_collect.setEnabled(false);
                        }
                    }else {

                        collectRequest(1);
                    }
                    break;

            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //分享
            case R.id.img_share:
                if (isFromCollect&&status!=2){
                    return;
                }
                if(Account.user==null){
                    ViewUtils.startActivity(new Intent(ShopHotArticleAct.this, LoginAct.class), ShopHotArticleAct.this);
                    ViewUtils.showToast(getResources().getString(R.string.actions_must_sign));
                    return;
                }
                if(shareDialog ==null)
                    shareDialog =new ShareDialog();
                shareDialog.show(shareInfo.getTitle(), shareInfo.getDesc(),shareInfo.getUrl(),shopArticle.getHead_url(), new ShareListener() {
                    @Override
                    public void onComplete(Object var1) {
                        ViewUtils.showToast("分享成功");
                        new MarkShareRequest(1,shopArticleId).start();
                    }

                    @Override
                    public void onFail(int errorCode) {

                    }
                });
                break;
            //返回
            case R.id.button_back:
                finish();
                break;
        }
    }

    private void saveComment(String content) {
        if (content.length() == 0 ) {
            ViewUtils.showToast("内容不能为空!");
        } else {
            if(Account.user==null){
                ViewUtils.startActivity(new Intent(this, LoginAct.class), this);
                ViewUtils.showToast(getResources().getString(R.string.actions_must_sign));
                return;
            }
            SendCommentsRequest sendCommentsRequest = new SendCommentsRequest(content, Account.user.getId(),shopArticleId);
            sendCommentsRequest.start();
            sendCommentsRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    loadListData(1);
                    ViewUtils.showToast("发布成功");
                }

                @Override
                public void onFail(int code) {
                    ViewUtils.showToast("发布失败");
                }
            });

        }
    }

    private void collectRequest(final int collect) {
        CollectArticleRequest collectArticleRequest = new CollectArticleRequest(1, shopArticleId, collect);
        collectArticleRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if(collect==1){
                    img_collect.setBackgroundResource(R.drawable.btn_collect_press);
                    ViewUtils.showToast("收藏成功");
                    shopArticle.setIsCollected(0);
                }else{
                    img_collect.setBackgroundResource(R.drawable.btn_collect_normal);
                    ViewUtils.showToast("已取消收藏");
                    shopArticle.setIsCollected(1);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        collectArticleRequest.start();
    }

    private void dianzanRequest() {
        PraiseArticleRequest praiseArticleRequest = new PraiseArticleRequest(1, shopArticleId);
        praiseArticleRequest.start();
        praiseArticleRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                img_dianzan.setBackgroundResource(R.drawable.btn_dianzan_press);
                shopArticle.setIsPraise(0);
                if(shopArticle.getPraiseAmount()==0){
                    findViewById(R.id.txt_dianzan_count).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.txt_dianzan_count)).setText(1+"");
                    ((TextView)findViewById(R.id.txt_dianzan_count)).setTextColor(Color.parseColor("#ff5f19"));
                }else{
                    ((TextView)findViewById(R.id.txt_dianzan_count)).setText(shopArticle.getPraiseAmount()+1+"");
                    ((TextView)findViewById(R.id.txt_dianzan_count)).setTextColor(Color.parseColor("#ff5f19"));
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
    }


    public static void start(Activity currentAct, ShopArticle shopArticle, int articleType, String title,
                             boolean isFromCollect,CallBackWithParam callBack) {
        Intent intent = new Intent();
        intent.putExtra("url", shopArticle.getUrl());
        intent.putExtra("shopArticleId", shopArticle.getId());
        intent.putExtra("articleType",articleType);
        intent.putExtra("status",shopArticle.getStatus());
        intent.putExtra("isFromCollect",isFromCollect);
        intent.putExtra("articleTitle",shopArticle.getTitle());
        intent.putExtra("content",shopArticle.getContent());
        intent.setClass(currentAct, ShopHotArticleAct.class);
        EaseConstant.startActivity(intent, currentAct);
//        MyCircleThirdAct.callBack = callBack;
         callBackWithParam = callBack;
    }

    protected void setFlipper() {
        viewContainer = (MyFlipperView) findViewById(R.id.list_layout);
        viewContainer.getRetryBtn().setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {
                viewContainer.setDisplayedChild(0,true);
                loadListData(1);
            }
        });
    }

    private void setNoDate(int page,int isVisible) {
        if (page == 1) {
            if (footView == null) {
                footView = this.getLayoutInflater().inflate(R.layout.mycircle_listview_foot_view, null);
                nodata_view = (TextView) footView.findViewById(R.id.nodata_view);
                if(showNoNet){
                    ((TextView)footView.findViewById(R.id.no_review)).setText("加载失败");
                }
                footView.setOnClickListener(this);
                listView.addF(footView);
            }
            footView.findViewById(R.id.no_review).findViewById(R.id.no_review).setVisibility(isVisible);
            nodata_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareInfo=null;
        callBackWithParam = null;
    }
}
