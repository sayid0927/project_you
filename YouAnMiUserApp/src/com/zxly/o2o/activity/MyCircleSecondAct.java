/*
 * 文件名：SecondMainAct.java
 * 版权：Copyright 2014 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SecondMainAct.java
 * 修改人：huangbin
 * 修改时间：2014-12-20
 * 修改内容：新增
 */
package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.widget.EaseMyFlipperView;
import com.zxly.o2o.adapter.ForumCommunityAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.LocalTopicRequest;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.request.TopicListRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MyFlipperView;

/**
 * TODO 二级Activity。
 * <p/>
 * TODO 详细描述
 * <p/>
 * TODO 示例代码
 * <p/>
 * <pre>
 * </pre>
 *
 * @author huangbin
 * @version YIBA-O2O 2014-12-20
 * @since YIBA-O2O
 */
public class MyCircleSecondAct extends MyCircleSecondActAssi
        implements OnItemClickListener, OnRefreshListener {

    private View HeaderView;
    private View concernHeaderView;
    private String title;
    private ImageView upToFirst;


    public static void start(long id, String title, int pageType, byte isShop, Activity curAct) {
        Intent intent = new Intent();
        intent.putExtra("mycircle_second_title", title);
        intent.putExtra("circleId", id);
        intent.putExtra("isShop", isShop);
        intent.putExtra("mycircle_second_page", pageType);
        intent.setClass(curAct, MyCircleSecondAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageType = getIntent().getIntExtra("mycircle_second_page", 0);

        if (pageType == Constants.FORUM_COMMUNITY) {
            page++;

            circleId = getIntent().getLongExtra("circleId", 0);
            isShop = getIntent().getByteExtra("isShop", (byte) -1);
            circleType = getIntent().getStringExtra("circleType");

            initView();
            addUpToTopBtn();

            mMainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 300);

        }else{
            initView();
            addUpToTopBtn();

        }


    }

    private void addConcernLayout(final MyCircleRequest request) {
        if (isShop == 1) {
            concernHeaderView =
                    this.getLayoutInflater().inflate(R.layout.mycircle_concern_include, null);
            /*
            concernHeaderView.findViewById(R.id.right_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //关注和取消关注接口
                    viewContainer.setDisplayedChild(MyFlipperView.LOADING);
                    ForumToConcernRequest forumToConcernRequest = new ForumToConcernRequest(circleId,
                            request.isConcern == 1 ? 2 : 1);
                    forumToConcernRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                        @Override
                        public void onOK() {
                            CircleMainPageFragment.isReLoad = true;
                            viewContainer.setDisplayedChild(MyFlipperView.LOADSUCCESSFUL);
                            ViewUtils.showToast("操作成功");

                            TextView concerTV = (TextView) concernHeaderView.findViewById(R.id.right_btn);
                            if (request.isConcern == 1) {
                                setConcernView(concerTV);
                                request.isConcern = 0;
                            } else {
                                setCancelConcernView(concerTV);
                                request.isConcern = 1;
                            }
                        }

                        @Override
                        public void onFail(int code) {
                            ViewUtils.showToast("操作失败");
                            viewContainer.setDisplayedChild(MyFlipperView.LOADSUCCESSFUL);
                        }
                    });
                    forumToConcernRequest.start();
                }
            });

            if (request.isConcern == 1) {
                setCancelConcernView((TextView) concernHeaderView.findViewById(R.id.right_btn));
                ((TextView) concernHeaderView.findViewById(R.id.right_btn)).setText("取消关注");
            } else {
                setConcernView((TextView) concernHeaderView.findViewById(R.id.right_btn));
            }
            */
            if ("brandCircle".equals(circleType)) {
                ((TextView) concernHeaderView.findViewById(R.id.title)).setText(((TopicListRequest) request).circleName);
                ((TextView) concernHeaderView.findViewById(R.id.sub_head)).setText(((TopicListRequest) request).title);
                EaseConstant
                        .setImage((NetworkImageView) concernHeaderView.findViewById(R.id.avatar),
                                ((TopicListRequest) request).imageUrl, R
                                        .drawable.ease_default_avatar, null);
            } else {
                ((TextView) concernHeaderView.findViewById(R.id.title)).setText(((LocalTopicRequest)request).circleName);
                ((TextView) concernHeaderView.findViewById(R.id.sub_head)).setText(((LocalTopicRequest)request).title);
                EaseConstant
                        .setImage((NetworkImageView) concernHeaderView.findViewById(R.id.avatar),
                                ((LocalTopicRequest)request).imageUrl, R
                                        .drawable.ease_default_avatar, null);
            }
            mListView.addH(concernHeaderView);
        }
    }

    private void setCancelConcernView(TextView concerTV) {
        concerTV.setTextColor
                (Color.parseColor("#999999"));
        concerTV.setText("取消关注");
        concerTV.setBackgroundResource(R.drawable.bg_address_grey);
    }

    private void setConcernView(TextView concerTV) {
        concerTV.setText("关注");
        concerTV.setTextColor
                (Color.parseColor("#ff5f19"));
        concerTV.setBackgroundResource(R.drawable.bg_address_normal);

    }

    private void addUpToTopBtn() {
      upToFirst =(ImageView) findViewById(R.id.btn_up_to_first);
        upToFirst.setOnClickListener(new View.OnClickListener
                () {
            @Override
            public void onClick(View v) {
                MyCircleSecondAct.this.mListView.setSelection(0);
                upToFirst.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {
        if (pageType == Constants.FORUM_COMMUNITY)// 论坛
        {
            mInflater.inflate(R.layout.pull_to_refresh_layout, rootLayout);
        }

        setFlipper();

        objectAdapter = new ForumCommunityAdapter(this,isShop,title);
        if("brandCircle".equals(circleType)){
            ((ForumCommunityAdapter)objectAdapter).setCircleType(circleType);
        }
        setListViewAvailable();
        mListView.setAdapter(objectAdapter);
        mListView.setDivideHeight(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, Config.displayMetrics));
        findViewById(R.id.mycircle_publish_forum_btn).setOnClickListener(this);
        UMengAgent.onEvent(this, UMengAgent.forumcommunity_page);
        setTitleLayout();
    }

    private void setTitleLayout() {
        if (pageType == Constants.FORUM_COMMUNITY) {
            title = getIntent().getStringExtra("mycircle_second_title");
            findViewById(R.id.mycircle_second_page_top_tip_layout).setVisibility(View.GONE);
            findViewById(R.id.mycircle_publish_forum_btn).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.mycircle_top_title))
                    .setText(title);
        } else {
            findViewById(R.id.mycircle_publish_forum_btn).setVisibility(View.GONE);
        }
    }

    BaseRequest.ResponseStateListener responseStateListener = new BaseRequest.ResponseStateListener() {
        public void onOK() {

            if (concernHeaderView == null && myRequest != null) {
                addConcernLayout(myRequest);
            }

            if (myRequest.shopTopics == null || myRequest.shopTopics.size() == 0) {
                if (page == 1) {
                    objectAdapter.clear();
                    viewContainer.setDisplayedChild(MyFlipperView.NODATA, false);
                }
                isLastPage = 1;

            } else {
                viewContainer.setDisplayedChild(MyFlipperView.LOADSUCCESSFUL, false);
                if (page == 1) {


                    if (myRequest.shopTopics.size() > 1) {
                        shopTopTopic1 = myRequest.shopTopics.get(0);
                        shopTopTopic2 = myRequest.shopTopics.get(1);
                        if ((shopTopTopic1.getIs_top() == IS_TOP) && (shopTopTopic2.getIs_top() == IS_TOP)) {
                            if (HeaderView == null) {
                                addHead2ListView();
                            }

                            setTopTabText();

                            // 过滤掉置顶的两个
                            myRequest.shopTopics.remove(0);
                            myRequest.shopTopics.remove(0);
                        } else if (HeaderView != null) {
                            mListView.removedH(HeaderView);
                            HeaderView = null;

                        }
                    } else if (HeaderView != null) {
                        mListView.removedH(HeaderView);
                        HeaderView = null;

                    }

                    objectAdapter.clear();
                }
                objectAdapter.addItem(myRequest.shopTopics, true);
            }
            mListView.onRefreshComplete();
        }

        public void onFail(int code) {
            mListView.onRefreshComplete();
            viewContainer.setDisplayedChild(EaseMyFlipperView.LOADFAIL, true);
        }
    };

    private void setTopTabText() {
        if(shopTopTopic1.getIsShopTopic() == 0 && !"brandCircle".equals(circleType)){
            ((TextView) findViewById(R.id.mycircle_second_page_tab1_text))
                    .setText(shopTopTopic1.getContent().length() == 0 ? "图片" : "【本店交流】" + shopTopTopic1.getContent());
        } else {
            ((TextView) findViewById(R.id.mycircle_second_page_tab1_text))
                    .setText(shopTopTopic1.getContent().length() == 0 ? "图片" : shopTopTopic1.getContent());
        }


        //        ((TextView) findViewById(R.id.mycircle_second_page_tab1_text2))
        //                .setText(String.valueOf(shopTopTopic1.getReply_amount()));

        if(shopTopTopic2.getIsShopTopic() == 0 && !"brandCircle".equals(circleType)) {
            ((TextView) findViewById(R.id.mycircle_second_page_tab2_text))
                    .setText(shopTopTopic2.getContent().length() == 0 ? "图片" : "【本店交流】" + shopTopTopic2.getContent());
        } else {
            ((TextView) findViewById(R.id.mycircle_second_page_tab2_text))
                    .setText(shopTopTopic2.getContent().length() == 0 ? "图片" : shopTopTopic2.getContent());
        }

        //        ((TextView) findViewById(R.id.mycircle_second_page_tab2_text2))
        //                .setText(String.valueOf(shopTopTopic2.getReply_amount()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pageType == Constants.FORUM_COMMUNITY && MyCircleRequest.publishTopic != null && MyCircleRequest
                .publishTopic.getIsShown()) {
            page = 1;
            viewContainer.setDisplayedChild(0, true);
            loadData();
        }
    }

    private void setListViewAvailable() {
        mListView = (PullToRefreshListView) findViewById(R.id.pull_to_refreshlistview);
        ViewUtils.setRefreshText(mListView);
        mListView.setOnItemClickListener(this);
        mListView.setIntercept(true);
        mListView.setOnRefreshListener(this);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    upToFirst.setVisibility(view.getFirstVisiblePosition() == 0 ? View.GONE : View.VISIBLE);
                    ((ForumCommunityAdapter) objectAdapter).isFastScrolling = false;
                } else if (scrollState == 1) {
                    ((ForumCommunityAdapter) objectAdapter).isFastScrolling = false;
                } else {
                    ((ForumCommunityAdapter) objectAdapter).isFastScrolling = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        });

    }

    private void addHead2ListView() {
        HeaderView = this.getLayoutInflater().inflate(R.layout.mycircle_topmost_include, null);
        HeaderView.findViewById(R.id.mycircle_second_page_tab1).setOnClickListener(this);
        HeaderView.findViewById(R.id.mycircle_second_page_tab2).setOnClickListener(this);
        mListView.addH(HeaderView);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (concernHeaderView != null && position != 1) {
            position = position - 1;
            startThirdAct(position);
        } else if (concernHeaderView == null) {
            startThirdAct(position);
        }

    }

    private void startThirdAct(int position) {
        if (HeaderView != null && position > 1) {
            MyCircleRequest.shopTopic = (ShopTopic) objectAdapter.getItem(position - 2);
        } else {
            MyCircleRequest.shopTopic = (ShopTopic) objectAdapter.getItem(position - 1);
        }
        if ("brandCircle".equals(circleType)) {
            MyCircleThirdAct.start(this, (byte)1, MyCircleRequest.shopTopic, title);
        } else {
            MyCircleThirdAct.start(this, MyCircleRequest.shopTopic.getIsShopTopic(), MyCircleRequest.shopTopic, title);
        }
    }

    @Override
    protected void loadData() {
        if ("brandCircle".equals(circleType)) {
            myRequest = new TopicListRequest(page, circleId, isShop);
        } else {
            myRequest = new LocalTopicRequest(page, circleId);
        }
        myRequest.setOnResponseStateListener(responseStateListener);
        myRequest.start(this);


    }

}
