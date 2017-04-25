package com.zxly.o2o.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.model.EaseTuWenVO;
import com.easemob.easeui.ui.EaseBaseMyListPageActivity;
import com.easemob.easeui.ui.EaseSelectMembersActivity;
import com.easemob.easeui.utils.GsonParser;
import com.easemob.easeui.widget.EaseMyFlipperView;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.IMSelectActivityAdapter;
import com.zxly.o2o.adapter.IMSelectArticleAdapter;
import com.zxly.o2o.adapter.IMSelectProduceAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.ActivityVO;
import com.zxly.o2o.model.CommissionProduct;
import com.zxly.o2o.model.PromotionArticle;
import com.zxly.o2o.request.ActivityListRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetuiPushRequest;
import com.zxly.o2o.request.PromotionArticleListRequest;
import com.zxly.o2o.request.PromotionProductListRequest;
import com.zxly.o2o.request.SendMsgStatisticsRequest;
import com.zxly.o2o.service.IMMultiPushTask;
import com.zxly.o2o.service.IMMultiSendTask;
import com.zxly.o2o.service.IMMultiTxtSendTask;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.KeyBoardUtils;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/12/8.
 */
public class IMMultiPushActivity extends EaseBaseMyListPageActivity implements
        AdapterView.OnItemClickListener {
    private int PUSH_TYPE;  //推送的消息类型  小于0表示singlePush 大于0表示multiPush
    private String title;
    private BaseRequest myRequest;
    private ObjectAdapter objectAdapter;
    private List<?> objects;
    private TextView sendMsgBtn;

    private int checkPosition = -1;  //item check position
    private ImageView checkView;   //checked view
    private boolean isResume;
    //    private LoadingDialog loading;
    private int multiPushOrSend;
    private String commission;
    private boolean isRegistMembers;
    private String registerTime;

    private EditText editText, searchBtn;
    public static final int SEARCH_ACTICLE_CODE = 0x110;
    public static final int SEARCH_PRODUCE_CODE = 0x111;
    public static final int SEARCH_ACTIVITY_CODE = 0x112;
    private boolean asyncTaskLock;
    private LinearLayout btn_send;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SEARCH_ACTIVITY_CODE:
                TypeToken<ActivityVO> activityToken = new TypeToken<ActivityVO>() {
                };
                addResultToList(activityToken, data);
                break;

            case SEARCH_PRODUCE_CODE:
                TypeToken<CommissionProduct> produceToken = new TypeToken<CommissionProduct>() {
                };
                addResultToList(produceToken, data);
                break;

            case SEARCH_ACTICLE_CODE:
                TypeToken<PromotionArticle> articleToken = new TypeToken<PromotionArticle>() {
                };
                addResultToList(articleToken, data);
                break;
        }
    }

    private <T> void addResultToList(TypeToken<T> token, Intent data) {
        if (data != null && data.hasExtra("object")) {
            try {
                Object object = GsonParser.getInstance().fromJson(data.getStringExtra("object"),
                        token);

                if (object != null) {
                    if (checkView != null) {
                        cleanCheckView(checkView);
                    }

                    removedSameItem(object);
                    objectAdapter.addItem2Head(object, true);
                    mListView.callItemClick(mListView, 1, 0);
                }
            } catch (AppException e) {
                e.printStackTrace();
            }
        }
    }

    private void removedSameItem(Object object) {
        for (int i = 0; i < objectAdapter.getCount(); i++) {
            if ((object).equals(objectAdapter.getItem(i))) {
                objectAdapter.getContent().remove(i);
                return;
            }
        }
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.im_multi_push_layout);

        AppController.addAct(this);

        page = 1;
        PUSH_TYPE = getIntent().getIntExtra("push_type", 0);

        multiPushOrSend = getIntent().getIntExtra("multiPushOrSend", -1);
        commission = getIntent().getStringExtra("commission");
        isRegistMembers = getIntent().getBooleanExtra("isRegistMembers", false);
        registerTime = getIntent().getStringExtra("registerTime");
        //init listview
        setListView();
        mListView.setOnItemClickListener(this);

        setFlipper();

        if (PUSH_TYPE > 0) {
            /*初始化群发*/
            initMutilPush();
            if (PUSH_TYPE == HXConstant.PUSH_TXT) {

                View editView = findViewById(R.id.push_test_layout);
                editView.setVisibility(View.VISIBLE);
                editText = (EditText) findViewById(R.id.push_text);
                KeyBoardUtils.openKeybord(editText, this);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (s.length() >= 200) {
                            ViewUtils.showToast("不能输入更多了!");
                        }
                        ((TextView) findViewById(R.id.sum_edit)).setText("(" + s.length() + "/200)");

                    }
                });
                findViewById(R.id.search_bar_view).setVisibility(View.GONE);
                findViewById(R.id.multi_push_type_test).setVisibility(View.GONE);
                mListView.setVisibility(View.GONE);
            } else {

                setSearchBtn();

            }
        } else if (Math.abs(PUSH_TYPE) != HXConstant.PUSH_TXT) {
            setSearchBtn();
        }

        loadData();


        //初始化title
        sendMsgBtn = (TextView) setUpActionBar(title)
                .getCustomView().findViewById(R.id.tag_title_right);
        sendMsgBtn.setText("发送");
        sendMsgBtn.setVisibility(View.VISIBLE);
        //修改：商户改版一期  统一样式
        btn_send = (LinearLayout) findViewById(R.id.btn_send);
        if(PUSH_TYPE==-HXConstant.PUSH_ARTICLE||PUSH_TYPE==-HXConstant.PUSH_SHOP_INFO){
            sendMsgBtn.setVisibility(View.GONE);
            btn_send.setVisibility(View.VISIBLE);
            findViewById(R.id.multi_push_type_test).setVisibility(View.GONE);
        }else {
            btn_send.setVisibility(View.GONE);
        }
        btn_send.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                asyncTaskLock=true;
                if (checkPosition != -1 || editText != null && !"".equals(editText.getText().toString())) {

                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            new SendMsgStatisticsRequest(commission, isRegistMembers, registerTime, multiPushOrSend).start();

                            switch (Math.abs(PUSH_TYPE)) {
                                case HXConstant.PUSH_ARTICLE:
                                    PromotionArticle article = ((PromotionArticle) objectAdapter
                                            .getItem(checkPosition - 1));
//                                    initArticleTuWenVOAndSendMsg(article);
                                    if(multiPushOrSend==HXConstant.MUL_PUSH){
                                        pushArticle(article);
                                    }else {
                                        initArticleTuWenVOAndSendMsg(article);
                                    }
                                    break;

                                case HXConstant.PUSH_CAMPAIGN:
                                    ActivityVO activityVo = ((ActivityVO)
                                            objectAdapter.getItem(checkPosition - 1));
                                    initActivityTuWenVOAndSendMsg(activityVo);
                                    break;

                                case HXConstant.PUSH_SHOP_INFO:
                                    CommissionProduct product = ((CommissionProduct)
                                            objectAdapter.getItem(checkPosition - 1));
//                                    initProduceTuWenVOAndSendMsg(product);
                                    if(multiPushOrSend==HXConstant.MUL_PUSH){
                                        pushProduct(product);
                                    }else{
                                        initProduceTuWenVOAndSendMsg(product);
                                    }

                                    break;

                                case HXConstant.PUSH_TXT:
                                    ViewUtils.showToast("后台推送开启...");
                                    new IMMultiTxtSendTask().execute(editText.getText().toString());
                                    IMMultiPushActivity.this.finish();

                                    break;
                            }

                        }
                    }, 1000);

                } else {
                    ViewUtils.showToast("没有要发送的内容");
                }
            }
        });
        sendMsgBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //                if(EaseConstant.multiMsgProgress!=0||asyncTaskLock){
//                    return;
//                }
                asyncTaskLock=true;
                if (checkPosition != -1 || editText != null && !"".equals(editText.getText().toString())) {

                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            new SendMsgStatisticsRequest(commission, isRegistMembers, registerTime, multiPushOrSend).start();

                            switch (Math.abs(PUSH_TYPE)) {
                                case HXConstant.PUSH_ARTICLE:
                                    PromotionArticle article = ((PromotionArticle) objectAdapter
                                            .getItem(checkPosition - 1));
//                                    initArticleTuWenVOAndSendMsg(article);
                                    if(multiPushOrSend==HXConstant.MUL_PUSH){
                                        pushArticle(article);
                                    }else {
                                        initArticleTuWenVOAndSendMsg(article);
                                    }
                                    break;

                                case HXConstant.PUSH_CAMPAIGN:
                                    ActivityVO activityVo = ((ActivityVO)
                                            objectAdapter.getItem(checkPosition - 1));
                                    initActivityTuWenVOAndSendMsg(activityVo);
                                    break;

                                case HXConstant.PUSH_SHOP_INFO:
                                    CommissionProduct product = ((CommissionProduct)
                                            objectAdapter.getItem(checkPosition - 1));
//                                    initProduceTuWenVOAndSendMsg(product);
                                    if(multiPushOrSend==HXConstant.MUL_PUSH){
                                        pushProduct(product);
                                    }else{
                                        initProduceTuWenVOAndSendMsg(product);
                                    }

                                    break;

                                case HXConstant.PUSH_TXT:
                                    ViewUtils.showToast("后台推送开启...");
                                    new IMMultiTxtSendTask().execute(editText.getText().toString());
                                    IMMultiPushActivity.this.finish();

                                    break;
                            }

                        }
                    }, 1000);

                } else {
                    ViewUtils.showToast("没有要发送的内容");
                }
            }
        });
//        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if(EaseConstant.multiMsgProgress!=0||asyncTaskLock){
////                    return;
////                }
//                asyncTaskLock=true;
//                if (checkPosition != -1 || editText != null && !"".equals(editText.getText().toString())) {
//
//                    mMainHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            new SendMsgStatisticsRequest(commission, isRegistMembers, registerTime, multiPushOrSend).start();
//
//                            switch (Math.abs(PUSH_TYPE)) {
//                                case HXConstant.PUSH_ARTICLE:
//                                    PromotionArticle article = ((PromotionArticle) objectAdapter
//                                            .getItem(checkPosition - 1));
////                                    initArticleTuWenVOAndSendMsg(article);
//                                    if(multiPushOrSend==HXConstant.MUL_PUSH){
//                                        pushArticle(article);
//                                    }else {
//                                        initArticleTuWenVOAndSendMsg(article);
//                                    }
//                                    break;
//
//                                case HXConstant.PUSH_CAMPAIGN:
//                                    ActivityVO activityVo = ((ActivityVO)
//                                            objectAdapter.getItem(checkPosition - 1));
//                                    initActivityTuWenVOAndSendMsg(activityVo);
//                                    break;
//
//                                case HXConstant.PUSH_SHOP_INFO:
//                                    CommissionProduct product = ((CommissionProduct)
//                                            objectAdapter.getItem(checkPosition - 1));
////                                    initProduceTuWenVOAndSendMsg(product);
//                                    if(multiPushOrSend==HXConstant.MUL_PUSH){
//                                        pushProduct(product);
//                                    }else{
//                                        initProduceTuWenVOAndSendMsg(product);
//                                    }
//
//                                    break;
//
//                                case HXConstant.PUSH_TXT:
//                                    ViewUtils.showToast("后台推送开启...");
//                                    new IMMultiTxtSendTask().execute(editText.getText().toString());
//                                    IMMultiPushActivity.this.finish();
//
//                                    break;
//                            }
//
//                        }
//                    }, 1000);
//
//                } else {
//                    ViewUtils.showToast("没有要发送的内容");
//                }
//            }
//        });

    }

    private void setSearchBtn() {
        //搜索输入框
//        searchBtn = (EditText) findViewById(R.id.query);
        searchBtn = (EditText) findViewById(R.id.edit_search);
        searchBtn.setFocusable(false);
        searchBtn.setHint("搜索");
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (Math.abs(PUSH_TYPE)) {
                    case HXConstant.PUSH_ARTICLE:
                        SearchActicleAct.start(IMMultiPushActivity.this);
                        break;

                    case HXConstant.PUSH_CAMPAIGN:
                        SearchActivityAct.start(IMMultiPushActivity.this);
                        break;

                    case HXConstant.PUSH_SHOP_INFO:
                        SearchProductAct.start(IMMultiPushActivity.this);
                        break;

                }
            }
        });
    }

    /*初始化活动图文对象并发送*/
    private void initActivityTuWenVOAndSendMsg(ActivityVO activityVo) {
        EaseTuWenVO easeTuWenVO = new EaseTuWenVO();
        easeTuWenVO.setId(activityVo.getId());
        easeTuWenVO.setContent(activityVo.getTitle());
        easeTuWenVO.setH5Url(activityVo.getH5Url());
        easeTuWenVO.setUrl(activityVo.getImageUrls().split(",")[0]);
        easeTuWenVO.setTitle(activityVo.getTitle());
        easeTuWenVO.setCreateTime("发布时间: " + EaseConstant.formatOrderLongTime(activityVo.getCreateTime()));

        sendMsg(easeTuWenVO);
    }

    /*初始化文章图文对象并发送*/
    private void initArticleTuWenVOAndSendMsg(PromotionArticle item) {
        EaseTuWenVO easeTuWenVO = new EaseTuWenVO();
        easeTuWenVO.setId(item.getArticleId());
        easeTuWenVO.setContent(item.getTitle());
        easeTuWenVO.setTitle(item.getTitle());
        easeTuWenVO.setUrl(item.getHeadUrl());
        easeTuWenVO.setH5Url(item.getH5Url());

        sendMsg(easeTuWenVO);
    }

    /*初始化商品图文对象并发送*/
    private void initProduceTuWenVOAndSendMsg(CommissionProduct item) {
        EaseTuWenVO easeTuWenVO = new EaseTuWenVO();
        easeTuWenVO.setId(item.getProductId());
        if (item.getActivityPrice() == 0) {
            easeTuWenVO.setContent("￥" + item.getPrice());
        } else {
            easeTuWenVO.setContent("￥" + item.getActivityPrice() + "^ ￥" + item.getPrice());
        }
        easeTuWenVO.setUrl(item.getHeadUrl());
        easeTuWenVO.setH5Url(item.getUrl());
        easeTuWenVO.setTitle(item.getName());

        sendMsg(easeTuWenVO);
    }

    private void pushArticle(PromotionArticle item){
        ViewUtils.showToast("后台推送开启...");
        StringBuilder useIds = new StringBuilder();
        for (int i = 0; i < EaseConstant.getuiUsers.size(); i++) {
            if (i > 0) {
                useIds.append(',');
            }
            useIds.append(EaseConstant.getuiUsers.get(i));
        }
        int sendTarget = EaseConstant.isRegistMembers ? 1 : 0;
        getuiPush(item.getArticleId(), sendTarget, EaseConstant.users.size(), 1, item.getTitle(), useIds.toString(), Account.user.getId());
    }

    private void pushProduct(CommissionProduct item){
        ViewUtils.showToast("后台推送开启...");
        StringBuilder useIds = new StringBuilder();
        for (int i = 0; i < EaseConstant.getuiUsers.size(); i++) {
            if (i > 0) {
                useIds.append(',');
            }
            useIds.append(EaseConstant.getuiUsers.get(i));
        }
        int sendTarget = EaseConstant.isRegistMembers ? 1 : 0;
        getuiPush(item.getProductId(), sendTarget, EaseConstant.users.size(), 0, item.getName(), useIds.toString(), Account.user.getId());
    }

    private void getuiPush(long objectId, int sendTarget, int sendTotalUser, int sendType, String title, String useIds, long userId) {
        GetuiPushRequest getuiPushRequest = new GetuiPushRequest(objectId, sendTarget, sendTotalUser, sendType, Account.user.getShopId(), title, useIds, userId);
        getuiPushRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.showToast("推送成功");
                IMMultiPushActivity.this.finish();
            }

            @Override
            public void onFail(int code) {

            }
        });
        getuiPushRequest.start(this);
    }

    private void sendMsg(EaseTuWenVO easeTuWenVO) {
        ViewUtils.showToast("后台推送开启...");
        if (PUSH_TYPE > 0 && multiPushOrSend == HXConstant.MUL_PUSH && EaseConstant.isRegistMembers) {
             new IMMultiPushTask(PUSH_TYPE).execute(easeTuWenVO);
            //            pushCMDMsg(easeTuWenVO);
        } else {
              new IMMultiSendTask(PUSH_TYPE, multiPushOrSend).execute(easeTuWenVO);
            //            sendTuWenMsg(easeTuWenVO);
        }
        asyncTaskLock=false;
        this.finish();
    }

    /*接口请求回调*/
    BaseRequest.ResponseStateListener responseStateListener = new BaseRequest.ResponseStateListener() {
        @Override
        public void onOK() {
            mListView.onRefreshComplete();
            switch (Math.abs(PUSH_TYPE)) {
                case HXConstant.PUSH_ARTICLE:
                    objects = ((PromotionArticleListRequest) myRequest).getArticles();
                    break;

                case HXConstant.PUSH_CAMPAIGN:
                    objects = ((ActivityListRequest) myRequest).getActivityVOs();
                    break;

                case HXConstant.PUSH_SHOP_INFO:
                    objects = ((PromotionProductListRequest) myRequest).getProducts();
                    break;
            }

            if (objects == null || objects.size() == 0) {
                if (page == 1) {
                    viewContainer.setDisplayedChild(EaseMyFlipperView.NODATA);
                } else {
                    isLastPage = 1;
                }
            } else {
                if (page == 1) {
                    objectAdapter.clear();
                    viewContainer.setDisplayedChild(EaseMyFlipperView.LOADSUCCESSFUL, false);
                }
                objectAdapter.addItem(objects, true);
            }
        }

        @Override
        public void onFail(int code) {
            mListView.onRefreshComplete();
            viewContainer.setDisplayedChild(EaseMyFlipperView.LOADFAIL);

        }
    };

    /*加载活动列表*/
    private void loadActivityData() {
        if (Account.user != null) {
            if (objectAdapter == null) {
                objectAdapter = new IMSelectActivityAdapter(this);
                mListView.setAdapter(objectAdapter);
            }
            myRequest = new ActivityListRequest(page);
            myRequest.setOnResponseStateListener(responseStateListener);
            myRequest.start();
        } else {
            LoginAct.start(this);
        }
    }

    /*加载商品列表*/
    private void loadProduceData() {
        if (objectAdapter == null) {
            objectAdapter = new IMSelectProduceAdapter(this);
            mListView.setAdapter(objectAdapter);
        }
        myRequest = new PromotionProductListRequest(page);
        myRequest.setOnResponseStateListener(responseStateListener);
        myRequest.start();
    }


    /*加载文章列表*/
    private void loadArticleData() {
        if (objectAdapter == null) {
            objectAdapter = new IMSelectArticleAdapter(this);
            mListView.setAdapter(objectAdapter);
        }
        myRequest = new PromotionArticleListRequest(page);
        myRequest.setOnResponseStateListener(responseStateListener);
        myRequest.start();
    }


    /*初始化多推界面*/
    private void initMutilPush() {
        //已经注册的才会显示选了哪些人
        if (EaseConstant.isRegistMembers) {
            initWhos();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isResume && PUSH_TYPE > 0 && EaseConstant.isRegistMembers) {
            initWhos();
        } else {
            isResume = true;
        }
    }

    private TextView whos;

    /*初始化发送给谁控件*/
    private void initWhos() {
        whos = ((TextView) findViewById(R.id.tx_multi_push_who));
        //找到该id的用户
        String name = "";
        long userId = Long.valueOf(EaseConstant.users.get(0).split("_")[1]);
        for (EaseYAMUser easeYAMUser : HXHelper.yamContactList) {
            if (easeYAMUser.getFirendsUserInfo().getId() == userId) {
                name = easeYAMUser.getFirendsUserInfo().getNickname() == null ? "" : easeYAMUser
                        .getFirendsUserInfo().getNickname();
                break;
            }
        }

        whos.setText(Html.fromHtml(String.format(getResources()
                .getString(R.string.multi_push_who_test), name, EaseConstant.users.size()
        )));

        whos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EaseConstant.startActivityNormal(EaseSelectMembersActivity.class,
                        IMMultiPushActivity.this);
            }
        });
        whos.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadData() {

        TextView what = ((TextView) findViewById(R.id.multi_push_type_test));

        switch (PUSH_TYPE) {
            case HXConstant.PUSH_TXT:
                title = "群发文本消息";
                what.setVisibility(View.GONE);
                viewContainer.setDisplayedChild(EaseMyFlipperView.LOADSUCCESSFUL);
                break;

            case HXConstant.PUSH_ARTICLE:
                title = "群发文章消息";
                what.setText("挑选文章");
                loadArticleData();
                break;

            case HXConstant.PUSH_CAMPAIGN:
                title = "群发活动消息";
                what.setText("挑选活动");
                loadActivityData();
                break;

            case HXConstant.PUSH_SHOP_INFO:
                title = "群发商品消息";
                what.setText("挑选商品");
                loadProduceData();
                break;

            case -HXConstant.PUSH_ARTICLE:
                title = "发送文章";
                what.setText("挑选文章");
                loadArticleData();
                break;

            case -HXConstant.PUSH_CAMPAIGN:
                title = "发送活动";
                what.setText("挑选活动");
                loadActivityData();
                break;

            case -HXConstant.PUSH_SHOP_INFO:
                title = "发送商品软文";
                what.setText("挑选商品");
                loadProduceData();
                break;
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageView clickView = ((ImageView) view.findViewById(R.id.check_icon));
        if (position == checkPosition) {
            cleanCheckView(clickView);
        } else {
            setCheckView(clickView, position);
        }
    }

    private void cleanCheckView(ImageView checkView) {
        checkView.setImageResource(
                R.drawable.ease_check_normal);
        this.checkView = null;
        checkPosition = -1;
    }

    private void setCheckView(ImageView clickView, int position) {

        clickView.setImageResource(
                R.drawable.ease_check_press1);
        if (checkView != null && checkPosition != -1) {
            checkView.setImageResource(R.drawable.ease_check_normal);
        }
        checkPosition = position;
        checkView = clickView;
    }
}
