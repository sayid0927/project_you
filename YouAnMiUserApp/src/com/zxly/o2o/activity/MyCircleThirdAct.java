package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.utils.GsonParser;
import com.easemob.easeui.widget.MyWebView;
import com.easemob.easeui.widget.VolleyImageView;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ArticleDetailAdapter;
import com.zxly.o2o.adapter.BasicMyCircleAdapter;
import com.zxly.o2o.adapter.CircleHeadGVAdapter;
import com.zxly.o2o.adapter.ForumCommunityDetailAdapter;
import com.zxly.o2o.adapter.GridImageWithDeleteAdapter;
import com.zxly.o2o.adapter.ListViewObjectAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.GetPictureDialog;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.ArticleReply;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.model.TopicDetailHeadsUser;
import com.zxly.o2o.model.TopicReply;
import com.zxly.o2o.model.UserInfoVO;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.ArticleReplyListRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyArticleDetailRequest;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.request.MyTopicDetailRequest;
import com.zxly.o2o.request.ShareAndUpStatusRequest;
import com.zxly.o2o.request.TopicCollectRequest;
import com.zxly.o2o.request.TopicShareCountRequest;
import com.zxly.o2o.request.TopicUpRequest;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.CallBackWithParam;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.MyImageManager;
import com.zxly.o2o.util.PicTools;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MyImageView;

import java.util.List;
import java.util.regex.Pattern;

/**
 * TODO 三级界面。 <p/> TODO 详细描述 <p/> TODO 示例代码 <p/> <pre> </pre> @author huangbin @version YIBA-O2O 2015-1-10 @since YIBA-O2O
 */
public class MyCircleThirdAct extends MyCircleThirdActAssi
        implements OnRefreshListener, OnClickListener, OnLongClickListener {
    private boolean isLongClick = false;
    private Button rightBtn;
    private GridView mGridView;
    private ShareDialog shareDialog;
    private byte isShop;
    private String title;
    private ImageView collectBtn;
    private ImageView upToFirst;
    private boolean isGoToReply;

    /**
     * 帖子详情进口
     *
     * @param isShopTopic 是不是本门店帖子
     * @param mShopTopic  帖子对象
     */
    public static void start(Activity currentAct, byte isShopTopic, ShopTopic mShopTopic, String title) {
        MyCircleRequest.shopTopic = mShopTopic;
        Intent intent = new Intent();
        intent.putExtra("isShop", isShopTopic);  //默认否
        intent.putExtra("mycircle_third_title", title);
        intent.putExtra("mycircle_third_page", Constants.FORUM_COMMUNITY_DETAIL);
        intent.setClass(currentAct, MyCircleThirdAct.class);
        EaseConstant.startActivity(intent, currentAct);
    }

    public static void start(Activity currentAct, byte isShopTopic, ShopTopic mShopTopic, String title,
                             boolean isGoToReply) {
        MyCircleRequest.shopTopic = mShopTopic;
        Intent intent = new Intent();
        intent.putExtra("isShop", isShopTopic);  //默认否
        intent.putExtra("mycircle_third_title", title);
        intent.putExtra("mycircle_third_page", Constants.FORUM_COMMUNITY_DETAIL);
        intent.putExtra("isGoToReply", isGoToReply);
        intent.setClass(currentAct, MyCircleThirdAct.class);
        EaseConstant.startActivity(intent, currentAct);
    }

    public static void start(Activity currentAct, byte isShopTopic, ShopTopic mShopTopic, String title,
                             boolean isGoToReply, CallBackWithParam callBack) {
        MyCircleRequest.shopTopic = mShopTopic;
        Intent intent = new Intent();
        intent.putExtra("isShop", isShopTopic);  //默认否
        intent.putExtra("mycircle_third_title", title);
        intent.putExtra("mycircle_third_page", Constants.FORUM_COMMUNITY_DETAIL);
        intent.putExtra("isGoToReply", isGoToReply);
        intent.setClass(currentAct, MyCircleThirdAct.class);
        EaseConstant.startActivity(intent, currentAct);
        MyCircleThirdAct.callBack = callBack;
    }

    public static void start(Activity currentAct, ShopArticle shopArticle, String title,
                             CallBackWithParam callBack) {
        MyCircleRequest.shopArticle = shopArticle;
        Intent intent = new Intent();
        intent.putExtra("mycircle_third_title", title);
        intent.putExtra("mycircle_third_page", Constants.ARTICLE_DETAIL);
        intent.setClass(currentAct, MyCircleThirdAct.class);
        EaseConstant.startActivity(intent, currentAct);
        MyCircleThirdAct.callBack = callBack;
    }

    private void addUpToTopBtn() {
        upToFirst = (ImageView) findViewById(R.id.btn_up_to_first);
        upToFirst.setOnClickListener(new View.OnClickListener
                () {
            @Override
            public void onClick(View v) {
                MyCircleThirdAct.this.mListView.setSelection(0);
                upToFirst.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoBitmap = null;
        pageType = getIntent().getIntExtra("mycircle_third_page", 10);
        switch (pageType) {
            case Constants.ARTICLE_DETAIL:
                mInflater.inflate(R.layout.win_mycircle_item_detail, rootLayout);
                initArticle();
                initDisplayPage();
                loadData();
                UMengAgent.onEvent(this, UMengAgent.article_detail);
                softInputControl(false);
                break;

            case Constants.FORUM_COMMUNITY_DETAIL:

                isShop = getIntent().getByteExtra("isShop", (byte) 1);
                mInflater.inflate(R.layout.win_mycircle_item_detail, rootLayout);
                initForumCommunityDetail();
                initDisplayPage();
                loadData();
                UMengAgent.onEvent(this, UMengAgent.topic_detail);
                softInputControl(false);
                break;

            case Constants.FORUM_PUBLISH:
                mInflater.inflate(R.layout.win_mycircle_publish_forum, rootLayout);
                initPublishForum();
                UMengAgent.onEvent(this, UMengAgent.forumcommunity_publish_page);
                break;
        }
        title = getIntent().getStringExtra("mycircle_third_title");
        ((TextView) findViewById(R.id.mycircle_third_top_title))
                .setText(title);
        actionType = -1;

        addUpToTopBtn();
    }

    private void initDisplayPage() {
        setFlipper();
        viewContainer.setDisplayedChild(3, true);
    }

    private void setArticleTopData() {
        detailId = MyCircleRequest.shopArticle.getId();
        ViewUtils.setLastTime(((TextView) HeaderView.findViewById(R.id.article_detail_maimtime)),
                MyCircleRequest.shopArticle.getCreateTime());
        HeaderView.findViewById(R.id.reply_refresh_btn).setOnClickListener(this);
        ((TextView) HeaderView.findViewById(R.id.detail_upicon))
                .setText(MyCircleRequest.shopArticle.getPraiseAmount() + "");
        ((TextView) HeaderView.findViewById(R.id.article_detail_downicon))
                .setText(MyCircleRequest.shopArticle.getOpposeAmount() + "");
        MyWebView mWebView = ((MyWebView) HeaderView.findViewById(R.id.article_detail_maimcontent));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.loadUrl(MyCircleRequest.shopArticle.getUrl());
    }

    private void setTopIcTopData() {
        //set createtime
        ViewUtils.setLastTime(
                ((TextView) HeaderView.findViewById(R.id.forum_community_detail_mainuser_below_text)),
                MyCircleRequest.shopTopic.getCreateTime());

        //show the loadall button if you need in your requirement
        if (MyCircleRequest.shopTopic.getIsMyReply()) {
            findViewById(R.id.detail_loadall).setVisibility(View.VISIBLE);
            findViewById(R.id.detail_loadall).setOnClickListener(this);
        }

        initTopicTopUser(MyCircleRequest.shopTopic.getPublishUser().getId());

        if (MyCircleRequest.shopTopic.getContent().length() == 0) {
            (HeaderView.findViewById(R.id.forum_community_detail_maimcontent)).setVisibility(View.GONE);
        } else {
            ((TextView) HeaderView.findViewById(R.id.forum_community_detail_maimcontent))
                    .setText(MyCircleRequest.shopTopic.getContent());
            (HeaderView.findViewById(R.id.forum_community_detail_maimcontent)).setVisibility(View.VISIBLE);
        }

        ((TextView) HeaderView.findViewById(R.id.forum_community_detail_maimcontent))
                .setText(MyCircleRequest.shopTopic.getContent());

        if (MyCircleRequest.shopTopic.getOriginImageList() != null) {
            initImageList(MyCircleRequest.shopTopic.getOriginImageList());
        }


    }

    private void initTopicTopUser(long id) {
        String name;
        int url;
        if (id == 0) {
            name = "柚安米";
            url = R.drawable.icon_youanmi;

        } else {
            if (id == -1) {
                name = "匿名";
            } else {
                name = MyCircleRequest.shopTopic.getPublishUser().getNickname();
            }
            url = R.drawable.ease_default_avatar;
            findViewById(R.id.forum_community_detail_mainuser_photo).setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EaseConstant
                                    .startIMUserDetailInfo(MyCircleRequest.shopTopic.getPublishUser().getId(),
                                            true,
                                            MyCircleThirdAct.this, "个人详情", 1, null);
                        }
                    });

        }

        ImageUtil.setImage(
                ((NetworkImageView) HeaderView.findViewById(R.id.forum_community_detail_mainuser_photo)),
                MyCircleRequest.shopTopic.getPublishUser().getThumHeadUrl(),
                url, null);

        ((TextView) HeaderView.findViewById(R.id.forum_community_detail_mainuser))
                .setText(name);
    }


    private void initImageList(final List<String> images) {
        /*if (images.size() > 0) {
            ListView detailLV =
                    (ListView) HeaderView.findViewById(R.id.forum_community_detail_maimcontent_icon);
            detailLV.setVisibility(View.VISIBLE);//topic detail

            ListViewObjectAdapter listViewImageAdapter = new ListViewObjectAdapter(this);
            detailLV.setAdapter(listViewImageAdapter);

            listViewImageAdapter.addItem(images, true);

            setListHeight(detailLV, listViewImageAdapter);
        }*/
        if (images != null && !images.isEmpty()) {
            int size = images.size();
            for (int i = 0; i < size; i++) {
                String imgUrl = images.get(i);
                LinearLayout imgCcontainer = (LinearLayout) HeaderView.findViewById(R.id.img_ccontainer);
                MyImageView imageView = new MyImageView(this);
                imageView.setDefaultImageResId(R.drawable.icon_default);
                imageView.setImageUrl(imgUrl, AppController.imageLoader);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ViewGroup.LayoutParams layoutParams =
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setPadding(8, 8, 8, 8);
                final int finalI = i;
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GalleryViewPagerAct
                                .start(AppController.getInstance().getTopAct(),
                                        images.toArray(new String[images.size
                                                ()]),
                                        finalI);
                    }
                });
                imgCcontainer.addView(imageView, layoutParams);
            }


        }

    }

    BaseRequest.ResponseStateListener responseStateListener = new BaseRequest.ResponseStateListener() {
        public void onFail(int code) {
            isGoToReply = false;
            mListView.onRefreshComplete();
        }

        public void onOK() {
            switch (pageType) {
                case Constants.FORUM_COMMUNITY_DETAIL: /* 帖子*/
                    if (MyCircleRequest.shopTopic == null) {
                        isLastPage = 1;
                    } else {
                        if (MyCircleRequest.shopTopic.getOriginImageList() != null) {
                            initImageList(MyCircleRequest.shopTopic.getOriginImageList());
                        }

                        if (MyCircleRequest.shopTopic.getIsPraise() == 1) { /* 设置点赞图标*/
                            setUpPressIcon();
                        }
                        if (myRequest.topicReplys == null || (myRequest.topicReplys.size() == 0)) {
                            isLastPage = 1;
                            isGoToReply = false;
                            setNoDate(0x00000000);
                            //                            mListView.setIntercept(false);
                        } else {
                            setNoDate(0x00000004);
                            if (page == 1) {
                                objectAdapter.clear();
                                if (MyCircleRequest.shopTopic.getIsMyReply()) {
                                    ((ForumCommunityDetailAdapter) objectAdapter)
                                            .setMyTopicReplys(myRequest.topicReplys);
                                    objectAdapter.addItem2Head(myRequest.topicReplys, false);
                                } else {
                                    objectAdapter.addItem2Head(myRequest.topicReplys, true);
                                }
                                if (((ForumCommunityDetailAdapter) objectAdapter).getCachBitmapsSize() >
                                        0) {   /*下拉刷新跳到评论列表顶部*/
                                    mListView.onRefreshComplete();
                                    mListView.setSelection(2);
                                    isGoToReply = false;
                                    return;
                                }
                            } else if (MyCircleRequest.shopTopic.getIsMyReply()) {
                                boolean isNeedShowAll =
                                        ((ForumCommunityDetailAdapter) objectAdapter).getIsNeedShowAll();
                                objectAdapter.addItem(myRequest.topicReplys, isNeedShowAll);
                                ((ForumCommunityDetailAdapter) objectAdapter)
                                        .addMyTopicReplys(myRequest.topicReplys, !isNeedShowAll);
                            } else {
                                objectAdapter.addItem(myRequest.topicReplys, true);
                            }
                        }

                        initTopicTopDetail();
                    }
                    break;
                case Constants.ARTICLE_DETAIL: /* 文章*/
                    if (MyCircleRequest.shopArticle == null) {
                        isLastPage = 1;
                    } else {
                        if (MyCircleRequest.shopArticle.getIsPraise() == 1) { /* 设置点赞图标*/
                            setUpPressIcon();
                        } else if (MyCircleRequest.shopArticle.getIsOppose() == 1) { /* 设置点踩图标*/
                            ((TextView) HeaderView.findViewById(R.id.article_detail_downicon))
                                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.cai_big_press, 0, 0,
                                            0);
                        }
                        if (MyCircleRequest.shopArticle.getIsCollected() == 1) { /* 设置收藏图标*/
                            ((ImageButton) findViewById(R.id.mycircle_third_top_title_collect_btn))
                                    .setImageResource(R.drawable.shoucang_press);
                        }
                        if (myRequest.articleReplys == null || myRequest.articleReplys.size() == 0) {
                            isLastPage = 1;
                            setNoDate(0x00000000);
                        } else {
                            setNoDate(0x00000004);
                            if (page == 1) {
                                objectAdapter.clear();
                            }
                            objectAdapter.addItem(myRequest.articleReplys, true);
                        }
                    }
                    break;
            }
            mListView.onRefreshComplete();
        }
    };

    private void initTopicTopDetail() {
        if (HeaderView != null) {
            initTopicPraiseAmount();

            initTopicShareAmount();

            initPraiseHeads();
        }
        initCollectBtn(MyCircleRequest.shopTopic.getIsCollect());

    }

    private void initPraiseHeads() {
        if (MyCircleRequest.shopTopic != null &&
                MyCircleRequest.shopTopic.getHeadUrlList().size() >
                        0) {
            findViewById(R.id.up_members_layout).setVisibility(View.VISIBLE);
            //显示更多按钮
            HeaderView.findViewById(R.id.more_members).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicShareAndCollectMembersAct.start(detailId, isShop, MyCircleThirdAct.this);
                }
            });

            //显示分享和点赞头像
            GridView gridView = ((GridView) HeaderView.findViewById(R.id.up_members_gv));
            CircleHeadGVAdapter circleHeadGVAdapter = new CircleHeadGVAdapter(MyCircleThirdAct.this
            );
            gridView.setAdapter(circleHeadGVAdapter);
            circleHeadGVAdapter.addItem(MyCircleRequest.shopTopic.getHeadUrlList());
            HeaderView.findViewById(R.id.up_members_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.up_members_layout).setVisibility(View.GONE);
        }
    }

    private void initTopicShareAmount() {

        final TextView shareBtn = (TextView) HeaderView.findViewById(R.id.detail_share);
        if (TextUtils.isEmpty(MyCircleRequest.shopTopic.getShareUrl())) {
            shareBtn.setVisibility(View.GONE);
        }
        shareBtn.setText(
                String.format(getResources().getString(R.string.topic_share_count_text),
                        MyCircleRequest.shopTopic.getShareAmount()));
        shareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareDialog == null) {
                    shareDialog = new ShareDialog();
                }
                shareDialog.show(
                        title + "-" + getResources().getString(R.string.app_name),
                        MyCircleRequest.shopTopic.getContent(),
                        MyCircleRequest.shopTopic.getShareUrl(),
                        "".equals(MyCircleRequest.shopTopic.getCircleHeadUrl()) ?
                                BitmapUtil.drawableToBitmap(getResources().getDrawable(R.drawable.logo)) :
                                MyCircleRequest.shopTopic.getCircleHeadUrl(),
                        new ShareListener() {
                            @Override
                            public void onComplete(Object var1) {
                                if (Account.user != null && actionType != Constants.ACTION_TYPE_SHARE) {
                                    TopicShareCountRequest topicShareCountRequest = new TopicShareCountRequest
                                            (isShop, detailId);
                                    topicShareCountRequest.setOnResponseStateListener(
                                            new BaseRequest.ResponseStateListener() {
                                                @Override
                                                public void onOK() {
                                                    MyCircleRequest.shopTopic
                                                            .setShareAmount(
                                                                    MyCircleRequest.shopTopic.getPraiseAmout()
                                                                            + 1);
                                                    shareBtn.setText(String.format(
                                                            getResources().getString(
                                                                    R.string.topic_share_count_text),
                                                            MyCircleRequest.shopTopic.getShareAmount()
                                                    ));

                                                    loadData();//刷新点赞头像
                                                }

                                                @Override
                                                public void onFail(int code) {
                                                }
                                            });
                                    topicShareCountRequest.start();
                                }
                            }

                            @Override
                            public void onFail(int errorCode) {

                            }
                        });
            }
        });
    }

    private void initTopicPraiseAmount() {
        final TextView upBtn = (TextView) HeaderView.findViewById(R.id.detail_upicon);

        upBtn.setText(String.format(getResources().getString(R.string.topic_up_count_text),
                MyCircleRequest.shopTopic.getPraiseAmout()));
        upBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyCircleRequest.shopTopic.getIsPraise() == 2 && actionType != Constants.ACTION_TYPE_UP) {
                    actionType = Constants.ACTION_TYPE_UP;
                    TopicUpRequest topicUpRequest = new TopicUpRequest
                            (isShop, detailId);
                    topicUpRequest.setOnResponseStateListener(
                            new BaseRequest.ResponseStateListener() {
                                @Override
                                public void onOK() {
                                    if(MyCircleThirdActAssi.callBack!=null) {
                                        MyCircleThirdActAssi.callBack.onCall(0);
                                    }
                                    actionType = -1;
                                    MyCircleRequest.shopTopic.setIsPraise((byte) 1);
                                    upBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable
                                            .dianzan_press, 0, 0, 0);
                                    MyCircleRequest.shopTopic
                                            .setPraiseAmout(MyCircleRequest.shopTopic.getPraiseAmout()
                                                    + 1);
                                    upBtn.setText(String.format(
                                            getResources().getString(R.string.topic_up_count_text),
                                            MyCircleRequest.shopTopic.getPraiseAmout()
                                    ));

                                    loadData();
                                    //刷新点赞头像
                                }

                                @Override
                                public void onFail(int code) {
                                    actionType = -1;
                                }
                            });
                    topicUpRequest.start();
                } else {
                    ViewUtils.showToast("已经点赞");
                }
            }
        });

        if (MyCircleRequest.shopTopic.getIsPraise() == 1) {
            upBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable
                    .dianzan_press, 0, 0, 0);
        } else {
            upBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable
                    .bt_zan_small_selector, 0, 0, 0);
        }

    }

    private void addCollectBtn(final byte isCollect) {
        collectBtn = new ImageView(MyCircleThirdAct.this);
        collectBtn.setImageResource(R.drawable.white_collect_selector);
        collectBtn.setScaleType(ImageView.ScaleType.CENTER);

        //set collect btn click listener
        collectBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicCollectRequest topicCollectRequest = new TopicCollectRequest
                        (MyCircleRequest.shopTopic.getIsCollect() == 1 ? (byte) 2 : 1,
                                isShop, MyCircleRequest.shopTopic.getId());
                topicCollectRequest.setOnResponseStateListener(
                        new BaseRequest.ResponseStateListener() {
                            @Override
                            public void onOK() {

                                if (MyCircleRequest.shopTopic.getIsCollect() == 1) {
                                    MyCircleRequest.shopTopic.setIsCollect((byte) 2);
                                    ViewUtils.showToast("取消收藏成功");
                                } else {
                                    ViewUtils.showToast("收藏成功");
                                    MyCircleRequest.shopTopic.setIsCollect((byte) 1);
                                }
                                //                                MyCircleRequest.shopTopic.setIsCollect(
                                //                                        MyCircleRequest.shopTopic.getIsCollect() == 1 ?
                                //                                                (byte) 2 : 1);

                                //                                initCollectBtn(MyCircleRequest.shopTopic.getIsCollect());

                                if (MyCircleRequest.shopTopic.getIsCollect() == 1) {
                                    collectBtn.setImageResource(R.drawable.collect_white_press);
                                } else {
                                    collectBtn.setImageResource(R.drawable.white_collect_selector);
                                }
                            }

                            @Override
                            public void onFail(int code) {

                            }
                        });
                topicCollectRequest.start();
            }
        });


        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, FrameLayout.LayoutParams.WRAP_CONTENT, EaseUI
                        .displayMetrics),
                (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                                EaseUI.displayMetrics));
        layoutParams.gravity = Gravity.RIGHT;
        layoutParams.setMarginEnd((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                        EaseUI.displayMetrics));
        collectBtn.setLayoutParams(layoutParams);
        addContentView(collectBtn, layoutParams);

        if (isCollect == 1) {
            collectBtn.setImageResource(R.drawable.collect_white_press);
        } else {
            collectBtn.setImageResource(R.drawable.white_collect_selector);
        }
    }

    private void initCollectBtn(final byte isCollect) {
        if (collectBtn == null) {
            addCollectBtn(isCollect);
        }

    }

    private void setNoDate(int isVisible) {
        if (page == 1) {
            if (footView == null) {
                footView = this.getLayoutInflater().inflate(R.layout.mycircle_listview_foot_view, null);
                footView.setOnClickListener(this);
                mListView.addF(footView);
            }
            footView.findViewById(R.id.no_review).setVisibility(isVisible);
        }
    }

    private void setUpPressIcon() {
        if(HeaderView!=null) {
            ((TextView) HeaderView.findViewById(R.id.detail_upicon))
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.zan_press, 0, 0, 0);
        }
    }

    private void setListViewAvailable() {
        animation = AnimationUtils.loadAnimation(this, R.anim.text_operate_anim);
        mListView = (PullToRefreshListView) findViewById(R.id.pull_to_refreshlistview);

        ViewUtils.setRefreshText(mListView);
        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        mListView.setOnRefreshListener(this);
        mListView.setIntercept(true);

        isGoToReply = getIntent().getBooleanExtra("isGoToReply", false);
        if (!isGoToReply) {
            initHeadView();
            mListView.addH(HeaderView);
            if(pageType==Constants.FORUM_COMMUNITY_DETAIL) {
                setTopIcTopData();
                initTopicTopDetail();
            }
        }

        mListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {

                    //解决数据为空时refresh和headview滑动事件冲突问题
                    if (objectAdapter.getCount() == 0) {
                        if (view.getFirstVisiblePosition() == 0 &&
                                mListView.getMode() != PullToRefreshBase.Mode
                                        .PULL_FROM_START) {
                            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        } else if (mListView.getMode() != PullToRefreshBase.Mode.DISABLED) {
                            mListView.setMode(PullToRefreshBase.Mode.DISABLED);
                        }

                    } else if (mListView.getMode() != PullToRefreshBase.Mode.BOTH) {
                        mListView.setMode(PullToRefreshBase.Mode.BOTH);
                    }

                    if (view.getFirstVisiblePosition() == 0) {
                        if (isGoToReply || HeaderView == null) {
                            initHeadView();
                            mListView.addH(HeaderView);
                            if(pageType==Constants.FORUM_COMMUNITY_DETAIL) {
                                setTopIcTopData();
                                initTopicTopDetail();
                                if (MyCircleRequest.shopTopic.getIsPraise() == 1) { /* 设置点赞图标*/
                                    setUpPressIcon();
                                }
                            }
                        }
                        upToFirst.setVisibility(View.GONE);
                    } else {
                        upToFirst.setVisibility(View.VISIBLE);
                    }
                    //                    upToFirst.setVisibility(view.getFirstVisiblePosition() == 0 ? View.GONE : View.VISIBLE);
                    //                    findViewById(R.id.win_mycircle_editinput_include_layout).setVisibility(View.VISIBLE);
                    objectAdapter.isFastScrolling = false;
                } else if (scrollState == 1) {
                    objectAdapter.isFastScrolling = false;
                    //                    findViewById(R.id.win_mycircle_editinput_include_layout).setVisibility(View.VISIBLE);
                } else {
                    objectAdapter.isFastScrolling = true;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVifalsesibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        });

    }

    /**
     * 初始化HeadView
     */
    private void initHeadView() {
        if (pageType == Constants.FORUM_COMMUNITY_DETAIL) {
            HeaderView = this.getLayoutInflater().inflate(R.layout.forum_community_detail_include, null);
            (HeaderView.findViewById(R.id.forum_community_detail_mainuser_photo)).setOnClickListener(this);
        } else if (pageType == Constants.ARTICLE_DETAIL) {
            HeaderView = this.getLayoutInflater().inflate(R.layout.article_detail_include, null);
            (HeaderView.findViewById(R.id.article_detail_downicon)).setOnClickListener(this);
        }

        (HeaderView.findViewById(R.id.detail_upicon)).setOnClickListener(this);
    }


    private void initArticle() {
        setListViewAvailable();
        objectAdapter = new ArticleDetailAdapter(this, animation, mMainHandler);
        mListView.setAdapter(objectAdapter);
        publishForumContent = (EditText) findViewById(R.id.comment_content);
        publishForumContent.addTextChangedListener(textWatcher);
        findViewById(R.id.mycircle_third_top_title_publish_btn).setVisibility(View.GONE);
        findViewById(R.id.mycircle_third_article_detail_top_title).setVisibility(View.VISIBLE);
        findViewById(R.id.mycircle_third_top_title_collect_btn).setOnClickListener(this);
        findViewById(R.id.mycircle_third_top_title_share_btn).setOnClickListener(this);
        findViewById(R.id.comment_sendbtn).setOnClickListener(this); /*        View view = findViewById(R.id.reply_pic_select_btn); view.setOnClickListener(this); view.setOnLongClickListener(this);*/
    }

    private void initForumCommunityDetail() {
        //初始化listview

        setListViewAvailable();

        objectAdapter = new ForumCommunityDetailAdapter(this, MyCircleRequest.shopTopic.getId(), isShop,
                animation, mMainHandler);
        ((ForumCommunityDetailAdapter) objectAdapter).setOnItemClickListener(
                new ForumCommunityDetailAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TextView view, int positionan) {
                        publishForumContent.setText("");
                        publishForumContent.setHint(
                                "@" + ((TopicReply) (objectAdapter).getItem(positionan)).getReplyer()
                                        .getNickname() + ":");
                        parentId = ((TopicReply) (objectAdapter).getItem(positionan)).getId();

                        softInputControl(true);
                    }
                });

        mListView.setAdapter(objectAdapter);
        publishForumContent = (EditText) findViewById(R.id.comment_content);
        publishForumContent.addTextChangedListener(textWatcher); /* initInputManger(publishForumContent);*/
        findViewById(R.id.mycircle_third_article_detail_top_title).setVisibility(View.GONE);
        findViewById(R.id.reply_pic_select_btn).setOnClickListener(this);
        findViewById(R.id.comment_sendbtn).setOnClickListener(this);

        //        View view = findViewById(R.id.reply_pic_select_btn);
        //        view.setVisibility(View.VISIBLE);
        //        view.setOnLongClickListener(this);
    }

    private void softInputControl(boolean b) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (b) {
            //显示软键盘
            imm.showSoftInput(publishForumContent, InputMethodManager.SHOW_FORCED);
        } else {
            //隐藏软键盘
            imm.hideSoftInputFromWindow(publishForumContent.getWindowToken(), 0);
        }
    }


    private void initPublishForum() {
        ((TextView) findViewById(R.id.mycircle_third_top_title))
                .setText(getResources().getString(R.string.mycircle_publish_forum));
        publishForumContent = (EditText) findViewById(R.id.forum_publish_text);
        findViewById(R.id.mycircle_third_article_detail_top_title).setVisibility(View.GONE);
        publishForumContent.addTextChangedListener(textWatcher);
        rightBtn = (Button) findViewById(R.id.mycircle_third_top_title_publish_btn);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setOnClickListener(this);

        softInputControl(true);

        mGridView = (GridView) findViewById(R.id.refundment_pic_gridview);
        imageAdapter = new GridImageWithDeleteAdapter(this);
        imageAdapter.addItem("_default", false);
        mGridView.setAdapter(imageAdapter);


    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (isFinishing()) {
            return;
        }
        switch (msg.what) {
            case Constants.GET_PIC_FROM_CELLPHONE: // 解决4.4以上的版本图片url获取不到
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    startActivityForResult(intent, Constants.GET_PIC_FROM_CELLPHONE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    startActivityForResult(intent, Constants.GET_PIC_FROM_CELLPHONE);
                }
                break;

            case Constants.GET_PIC_FROM_CAMERA:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photo = PicTools.getOutputPhotoFile(true, "");
                fileUri = Uri.fromFile(photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, Constants.GET_PIC_FROM_CAMERA);
                break;

            case Constants.DELETE_PIC:
                fileUri = null;
                photo = null;
                photoBitmap = null;

                if (pageType == Constants.FORUM_COMMUNITY_DETAIL) {
                    ((ImageButton) findViewById(R.id.reply_pic_select_btn))
                            .setImageResource(R.color.transparent);

                    if (publishForumContent.length() == 0) {
                        findViewById(R.id.comment_sendbtn)
                                .setBackgroundResource(R.drawable.chongdianbao);
                        ((TextView) findViewById(R.id.comment_sendbtn))
                                .setTextColor(getResources().getColor(R.color.dark_grey));
                    }
                }

                break;

            case Constants.MSG_SUCCEED:
                String response = (String) msg.obj;
                countreply++;
                if (pageType == Constants.FORUM_COMMUNITY_DETAIL) {
                    if (response.startsWith("{")) {
                        TypeToken<TopicReply> token = new TypeToken<TopicReply>() {
                        };
                        try {
                            TopicReply topicReply = GsonParser.getInstance().fromJson(response, token);
                            doSucceedTopicReply(topicReply);
                        } catch (AppException e) {
                            e.printStackTrace();
                        }

                    } else {
                        doSucceedTopicReply(null);  //重复提交
                    }
                } else {
                    if (response != null && (Pattern.compile("[0-9]*")).matcher(response).matches()) {
                        long id = Long.valueOf(response);
                        doSucceed(id);
                    } else {
                        doSucceed(-101);  //重复提交
                    }
                }
                break;

            case Constants.MSG_FAILED:
                actionType = -1;
                String failedResponse = (String) msg.obj;
                if (failedResponse != null && failedResponse.equals("Token无效")) {
                    ViewUtils.startActivity(new Intent(this, LoginAct.class), this);
                    ViewUtils.showToast(getResources().getString(R.string.actions_must_sign));
                } else {
                    ViewUtils.showToast("提交失败");
                }
                cleanAtWho();
                break;
        }
    }

    private void doSucceedTopicReply(TopicReply topicReply) {
        actionType = -2;
        if (objectAdapter != null) {
            if (topicReply == null) {
                TopicDetailHeadsUser userVO = new TopicDetailHeadsUser();
                userVO.setThumHeadUrl(Account.user.getThumHeadUrl());
                userVO.setNickname(Account.user.getNickName());
                topicReply = new TopicReply();
                int floor =
                        objectAdapter.getContent().size() == 0 ? 1 : objectAdapter.getContent().size() + 1;
                topicReply.setFloor(floor);  //刚发成功的回复楼层加一

                if (parentId != -1) {
                    topicReply.setParentNickname(publishForumContent.getHint().toString()
                            .substring((publishForumContent.getHint().toString().indexOf("@") + 1),
                                    (publishForumContent.getHint().toString().indexOf(":"))));

                    topicReply.setContent(publishForumContent.getText().toString());

                    topicReply.setId(-101);

                    topicReply.setReplyer(userVO);
                }
            }

            topicReply.setIsNeedShowAddAnim(true);
            topicReply.setIsPraise((byte) 2);

            boolean isNeedShowAll = ((ForumCommunityDetailAdapter) objectAdapter)
                    .getIsNeedShowAll();
            ((ForumCommunityDetailAdapter) objectAdapter)
                    .addMyTopicReply(topicReply, !isNeedShowAll);
            ((ForumCommunityDetailAdapter) objectAdapter).addObject(topicReply, isNeedShowAll);
            setNoDate(0x00000004);
            mListView.setSelection(objectAdapter.getCount() + 1);
        }
        publishForumContent.setText("");
        cleanAtWho();
    }

    private void doSucceed(long id) {
        actionType = -2;
        if (pageType == Constants.ARTICLE_DETAIL) {
            ArticleReply articleReply = new ArticleReply();
            articleReply.setIsNeedShowAddAnim(true);
            UserInfoVO userVO = new UserInfoVO();
            userVO.setThumHeadUrl(Account.user.getThumHeadUrl());
            userVO.setNickname(Account.user.getNickName());
            articleReply.setContent(publishForumContent.getText().toString());
            articleReply.setReplyer(userVO);
            articleReply.setId(id);
            objectAdapter.addItem2Head(articleReply, true);
            mListView.setSelection(2);
            setNoDate(0x00000004);
        } else if (pageType == Constants.FORUM_PUBLISH) {
            MyCircleRequest.publishTopic.setIsShown(true);
            finish();
        }
        mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
        publishForumContent.setText("");
        cleanAtWho();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        actionType = requestCode;
        switch (requestCode) {
            case Constants.GET_PIC_FROM_CELLPHONE:

                if (data == null) {
                    mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                    ViewUtils.showToast("图片获取失败!");
                    actionType = -1;
                    return;
                }
                fileUri = data.getData();
                if (fileUri != null) {
                    photo = PicTools.getOutputPhotoFile(true, "");
                    resizeImage();
                } else {
                    mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                }
                break;

            case Constants.GET_PIC_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (fileUri != null) {
                        imageAdapter.addItem(fileUri.getPath());
                        imageAdapter.addItem("_default");
                        imageAdapter.notifyDataSetChanged();
                    } else if (mMainHandler != null) {
                        mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                    }
                } else if (resultCode == RESULT_CANCELED && mMainHandler != null) {
                    if (imageAdapter != null && !imageAdapter.getContent().contains("_default")) {
                        imageAdapter.addItem("_default");
                    }
                    mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                } else if (resultCode == Constants.GET_PIC_FROM_CAMERA && mMainHandler != null) {  //触发拍照
                    mMainHandler.sendEmptyMessage(Constants.GET_PIC_FROM_CAMERA);
                } else {
                    if (mMainHandler != null) {
                        mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                    }
                    ViewUtils.showToast("图片获取失败");
                }
                break;

            case Constants.RESULT_PICTURE:
                if (resultCode == 0) {
                    mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                    ViewUtils.showToast("图片获取失败");
                    actionType = -1;
                    return;
                }

                setBtnSrc();

                if (Constants.GET_PIC_FROM_CAMERA == actionType) {
                    photo = PicTools.savePicAndReturn(true, "");
                }
                break;
        }
        actionType = -1;
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void resizeImage() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(fileUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", Constants.CIRCLE_PIC_DEFAULT_SIZE);
        intent.putExtra("aspectY", Constants.CIRCLE_PIC_DEFAULT_SIZE);
        intent.putExtra("outputX", Constants.CIRCLE_PIC_DEFAULT_SIZE * 7);
        intent.putExtra("outputY", Constants.CIRCLE_PIC_DEFAULT_SIZE * 7);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, Constants.RESULT_PICTURE);
    }

    private void setBtnSrc() {
        if (pageType == Constants.FORUM_PUBLISH) {
            MyImageManager.from(this)
                    .displayImage(((ImageView) findViewById(R.id.pic_select_btn)), photo.getPath(),
                            R.drawable.pic_normal, Constants.CIRCLE_PIC_DEFAULT_SIZE * 7,
                            Constants.CIRCLE_PIC_DEFAULT_SIZE * 7);
        } else {
            MyImageManager.from(this)
                    .displayImage(((ImageView) findViewById(R.id.reply_pic_select_btn)),
                            photo.getPath(), R.drawable.pic_normal, Constants.CIRCLE_PIC_DEFAULT_SIZE * 7,
                            Constants.CIRCLE_PIC_DEFAULT_SIZE * 7);
            findViewById(R.id.comment_sendbtn)
                    .setBackgroundResource(R.drawable.bt_publish_selector);
            ((TextView) findViewById(R.id.comment_sendbtn))
                    .setTextColor(getResources().getColor(R.color.white));
        }
        ViewUtils.showToast("长按可删除");
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.reply_pic_select_btn && fileUri != null) {
            new GetPictureDialog(true).show(MyCircleThirdAct.this.mMainHandler);
        }
        return false;
    }


    private void loadRequest() {
        mMainHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                myRequest.setOnResponseStateListener(responseStateListener);
                myRequest.start(this);
            }
        }, 500);
    }

    @Override
    protected void loadData() {
        if (pageType == Constants.FORUM_COMMUNITY_DETAIL) {
            if (page == 0) {
                //get detailId from static object which is init in listview's itemclick
                detailId = MyCircleRequest.shopTopic.getId();
                page++;
            }
            myRequest = new MyTopicDetailRequest(detailId, isShop, page);
            loadRequest();
        } else if (pageType == Constants.ARTICLE_DETAIL) {
            if (MyCircleRequest.shopArticle != null) {
                if (page == 0) {
                    setArticleTopData();
                    page++;
                    myRequest = new MyArticleDetailRequest(detailId);
                    loadRequest();
                    return;
                }
                myRequest = new ArticleReplyListRequest(page, detailId);
                loadRequest();
            }
        }

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (Constants.FORUM_PUBLISH == pageType) {
                if (s.length() >= 200) {
                    ViewUtils.showToast("不能输入更多了!");
                }
                ((TextView) findViewById(R.id.sum_edit)).setText("(" + s.length() + "/200)");
            } else {
                if (s.length() > 0) {
                    findViewById(R.id.comment_sendbtn)
                            .setBackgroundResource(R.drawable.bt_publish_selector);
                    ((TextView) findViewById(R.id.comment_sendbtn))
                            .setTextColor(getResources().getColor(R.color.white));
                } else {
                    if (photo == null) {
                        findViewById(R.id.comment_sendbtn)
                                .setBackgroundResource(R.drawable.chongdianbao);
                        ((TextView) findViewById(R.id.comment_sendbtn))
                                .setTextColor(getResources().getColor(R.color.dark_grey));
                    }
                }
            }
        }
    };

    public void setListHeight(ListView listHeight, ListViewObjectAdapter listViewObjectAdapter) {
        int totalHeight;
        int count = listViewObjectAdapter.getCount();
        if (count > 0) {
            View listItem = listViewObjectAdapter.getView(0, null, listHeight).findViewById(R.id.image);
            listItem.measure(0, 0);
            totalHeight = 150 * count;
            //            totalHeight = listItem.getMeasuredHeight() * count;

            ViewGroup.LayoutParams params = listHeight.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    totalHeight + (listHeight.getDividerHeight() * (listViewObjectAdapter
                            .getCount() -
                            1)), AppController
                            .displayMetrics);
            ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
            listHeight.setLayoutParams(params);
        }
    }
}
