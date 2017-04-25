package com.zxly.o2o.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.activity.ForumGuideAct;
import com.zxly.o2o.activity.MyArticleAct;
import com.zxly.o2o.activity.MyCircleSecondAct;
import com.zxly.o2o.adapter.MyCircleListAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.ArticleListRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyCirclePageRequest;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MyFlipperView;

/**
 *     @author huangbin  @version 创建时间：2015-2-5 上午10:55:38    类说明: 
 */
public class MyCircleFragment extends BaseFragment implements OnItemClickListener, OnClickListener, OnRefreshListener {
    private PullToRefreshListView mListView;
    private MyCircleListAdapter circleAdapter;
    private MyCircleRequest myRequest;
    private Integer listPage = 1;
    private boolean isLastPage;
    private View HeaderView;
    private MyFlipperView viewContainer;
    private ShopArticle shopTopArticle1, shopTopArticle2;

    @Override
    public void onResume() {
        super.onResume();
        UMengAgent.onEvent(getActivity(), UMengAgent.mycircle_page);
    }

    private void loadData() {

        myRequest = new MyCirclePageRequest(Config.shopId);
        myRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                // 置顶文章
                try {
                    viewContainer.setDisplayedChild(3, false);
                    initCirclePageData();
                    mListView.onRefreshComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int code) {
                mListView.onRefreshComplete();
                viewContainer.setDisplayedChild(3, true);
            }
        });
        myRequest.start(this);
    }

    private void loadData2() {
        myRequest = new ArticleListRequest(listPage, null, Config.shopId);
        myRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                if (myRequest.articles == null || myRequest.articles.size() == 0) {
                    isLastPage = true;
                    ViewUtils.showToast("亲，没有新数据");
                } else if (myRequest.articles != null) {
                    if (listPage == 1) {
                        circleAdapter.clear();
                        circleAdapter.addItem2Head(myRequest.articles, true);
                    } else {
                        circleAdapter.addItem(myRequest.articles, true);
                    }
                }
                mListView.onRefreshComplete();
            }

            @Override
            public void onFail(int code) {
                mListView.onRefreshComplete();
            }
        });
        myRequest.start(this);
    }

    private void initCirclePageData() {
        // 论坛交流
        StringUtil.setTextSpan(0, 4, 18, Color.BLACK, String.format(content.getContext().getResources().getString(R.string.mycircle_community), myRequest.myCirclePageData.getUserAmount(), myRequest.myCirclePageData.getTopicAmount()), (TextView) findViewById(R.id.mycircle_community_tab1), content.getContext().getResources().getDisplayMetrics());


        if (myRequest.myCirclePageData.getArticles().size() == 0) {
            isLastPage = true;
        } else {
            if (myRequest.myCirclePageData.getArticles().size() > 1) {
                shopTopArticle1 = myRequest.myCirclePageData.getArticles().get(0);
                shopTopArticle2 = myRequest.myCirclePageData.getArticles().get(1);

                if ((shopTopArticle1.getIsTop() == 1) && (shopTopArticle2.getIsTop() == 1)) {
                    HeaderView.findViewById(R.id.mycircle_topmost_include_layout).setVisibility(View.VISIBLE);

                    setTopTabText();

                    // 过滤掉置顶的两个
                    myRequest.myCirclePageData.getArticles().remove(0);
                    myRequest.myCirclePageData.getArticles().remove(0);
                } else {
                    HeaderView.findViewById(R.id.mycircle_topmost_include_layout).setVisibility(View.GONE);
                }
            } else {
                HeaderView.findViewById(R.id.mycircle_topmost_include_layout).setVisibility(View.GONE);
            }

            // 文章列表
            listPage = 1;
            circleAdapter.clear();
            circleAdapter.addItem2Head(myRequest.myCirclePageData.getArticles(), true);
        }
    }

    private void setTopTabText() {
//        StringUtil.setTextSpan(0, 6, 14, Color.BLUE, new StringBuffer("【").append(shopTopArticle1.getTypeName()).append("】").append(shopTopArticle1.getTitle().length() == 0 ? "无标题" : shopTopArticle1.getTitle()).toString(), (TextView) findViewById(R.id.mycircle_second_page_tab1_text), content.getContext().getResources().getDisplayMetrics());


        ((TextView) findViewById(R.id.mycircle_second_page_tab1_text)).setText(new StringBuffer("【").append(shopTopArticle1.getTypeName()).append("】").append(shopTopArticle1.getTitle().length() == 0 ? "无标题" : shopTopArticle1.getTitle()));
        ((TextView) findViewById(R.id.mycircle_second_page_tab1_text2)).setText(new StringBuffer("").append(shopTopArticle1.getReplyAmount()));

        ((TextView) findViewById(R.id.mycircle_second_page_tab2_text)).setText(new StringBuffer("【").append(shopTopArticle2.getTypeName()).append("】").append(shopTopArticle2.getTitle().length() == 0 ? "无标题" : shopTopArticle2.getTitle()));
        ((TextView) findViewById(R.id.mycircle_second_page_tab2_text2)).setText(new StringBuffer("").append(shopTopArticle2.getReplyAmount()));

        ((TextView) findViewById(R.id.mycircle_second_page_tab1_text)).setTextColor(getResources().getColor(shopTopArticle1.getTypeName().contains("新手") ? R.color.light_yellow : shopTopArticle1.getTypeName().contains("保养") ? R.color.light_blue : R.color.light_green));
        ((TextView) findViewById(R.id.mycircle_second_page_tab2_text)).setTextColor(getResources().getColor(shopTopArticle2.getTypeName().contains("新手") ? R.color.light_yellow : shopTopArticle2.getTypeName().contains("保养") ? R.color.light_blue : R.color.light_green));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 1) {
            MyCircleRequest.shopArticle = (ShopArticle) circleAdapter.getItem(position - 2);
            MyCircleSecondAct.MyCircleLunch(getActivity(), Constants.ARTICLE_DETAIL,"");
    }
    }

    private void setViewOnClickListener(View view) {
        view.findViewById(R.id.mycircle_community_tab1).setOnClickListener(this);
        view.findViewById(R.id.mycircle_second_page_tab1).setOnClickListener(this);
        view.findViewById(R.id.mycircle_second_page_tab2).setOnClickListener(this);
        view.findViewById(R.id.mycircle_top_tab1).setOnClickListener(this);
        view.findViewById(R.id.mycircle_top_tab2).setOnClickListener(this);
        view.findViewById(R.id.mycircle_top_tab3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mycircle_top_tab3:
                MyArticleAct.start(getActivity(), Constants.MYCIRCLE_FUNCTION_DIG);
                break;

            case R.id.mycircle_top_tab2:
                MyArticleAct.start(getActivity(), Constants.MYCIRCLE_PHONE_UPKEEP);
                break;

            case R.id.mycircle_top_tab1:
                MyArticleAct.start(getActivity(), Constants.MYCIRCLE_MUST_LOOK);
                break;

            case R.id.mycircle_community_tab1:
                MyCircleSecondAct.MyCircleLunch(getActivity(), Constants.FORUM_COMMUNITY,"");
                break;

            case R.id.mycircle_second_page_tab1:
                if (shopTopArticle1 != null) {
                    MyCircleRequest.shopArticle = shopTopArticle1;
                    MyCircleSecondAct.MyCircleLunch(getActivity(), Constants.ARTICLE_DETAIL,"");
                }
                break;

            case R.id.mycircle_second_page_tab2:
                if (shopTopArticle2 != null) {
                    MyCircleRequest.shopArticle = shopTopArticle2;
                    MyCircleSecondAct.MyCircleLunch(getActivity(), Constants.ARTICLE_DETAIL,"");
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void initView() {

        setFlipper();

        mListView = (PullToRefreshListView) findViewById(R.id.pull_to_refreshlistview);
        mListView.setIntercept(true);
        ViewUtils.setRefreshText(mListView);
        circleAdapter = new MyCircleListAdapter(content.getContext());
        HeaderView = getActivity().getLayoutInflater().inflate(R.layout.win_mycircle_list_include, null);

        // 添加headView的点击事件
        setViewOnClickListener(HeaderView);
        mListView.addH(HeaderView);
        mListView.setAdapter(circleAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnRefreshListener(this);

        mMainHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                loadData();
            }
        }, 500);
    }

    @Override
    protected int layoutId() {
        return R.layout.mycircle_page_layout;
    }

    protected void setFlipper() {
        viewContainer = (MyFlipperView) findViewById(R.id.list_layout);
        viewContainer.getRetryBtn().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                viewContainer.setDisplayedChild(0, true);
                loadData();
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
            // 加载下拉数据
            loadData();
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            // 加载上拉数据
            if (!isLastPage) {
                listPage += 1;
                loadData2();
            } else {
                mMainHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
                    }
                }, Constants.REFRESH_LAST_TIME);
                ViewUtils.showToast("亲，没有新数据");
            }
        }
    }
}
