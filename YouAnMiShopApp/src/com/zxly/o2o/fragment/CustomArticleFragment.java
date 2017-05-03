/*
 * 文件名：MyOrderListFragment.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： MyOrderListFragment.java
 * 修改人：wuchenhui
 * 修改时间：2015-5-27
 * 修改内容：新增
 */
package com.zxly.o2o.fragment;


import android.os.Bundle;
import android.widget.ListView;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.CustomArticleAdapter;
import com.zxly.o2o.model.PromotionArticle;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import org.json.JSONObject;

import java.util.List;


/**
 * TODO 添加类的一句话简单描述。
 * <p/>
 * TODO 详细描述
 * <p/>
 * TODO 示例代码
 * <pre>
 * </pre>
 *
 * @author wuchenhui
 * @version YIBA-O2O 2015-5-27
 * @since YIBA-O2O
 */
public class CustomArticleFragment extends BaseFragment implements ResponseStateListener {

    ParameCallBack articleDeleteCallBack;
    private PullToRefreshListView mListView;
    private CustomArticleAdapter adapter;
    private LoadingView loadingView;
    ShopArticleRequest request;
    private int type;
    private int pageIndex = 1;
    private boolean refrensh;

    /**
     * @param type 1：自定义文章  2:行内文章
     * @return
     */
    public static CustomArticleFragment newInstance(int type) {
        CustomArticleFragment f = new CustomArticleFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        f.setArguments(args);
        return f;
    }

    @Override
    protected void initView(Bundle bundle) {
        type = bundle.getInt("type");
        loadingView = (LoadingView) findViewById(R.id.view_loading11);
        //loadingView.startLoading();
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        mListView.setIntercept(true);
        mListView.setDivideHeight(0);

        ViewUtils.setRefreshText(mListView);
//        if(type==2)//行内文章
//        {
//            mListView.setMode(Mode.DISABLED);
//        }
        mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
                    // 加载下啦数据
                    refrensh =true;
                    pageIndex=1;
                    loadData(1);
                }
                if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
                    refrensh =false;
                    // 加载上拉数据
                    loadData(pageIndex);
                }

            }
        });
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                loadData(1);
            }
        });

        if (adapter == null) {
            adapter = new CustomArticleAdapter(getActivity());
            adapter.setDelItem(type==1?true:false);
            adapter.setArticleDeleteCallBack(articleDeleteCallBack);
        }

        mListView.setAdapter(adapter);
        loadData(1);
    }


    public void loadData(final int pageId) {
        if (DataUtil.listIsNull(adapter.getContent()))
            loadingView.startLoading();

        if (request == null) {
            request = new ShopArticleRequest();
            request.setOnResponseStateListener(this);
        }
        request.addParams("pageIndex", pageId);
        request.start(getActivity());
    }

    @Override
    protected int layoutId() {
        return R.layout.win_cus_article;
    }

    @Override
    public void onOK() {
        if (!DataUtil.listIsNull(request.getArticles())) {
            if (pageIndex == 1||refrensh)
                adapter.clear();

            adapter.addItem(request.getArticles(), true);
            request.setArticles(null);
            pageIndex++;
            loadingView.onLoadingComplete();
        } else {
            //下拉刷新的时候发现数据为空，
            if (pageIndex == 1) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                loadingView.onDataEmpty();
            } else {
                //最后一页
                ViewUtils.showToast("没有更多了");
            }
        }

        if (mListView.isRefreshing())
            mListView.onRefreshComplete();
    }


    @Override
    public void onFail(int code) {
        if (DataUtil.listIsNull(adapter.getContent()))
            loadingView.onLoadingFail();

        if (mListView.isRefreshing())
            mListView.onRefreshComplete();
    }


    @Override
    protected void initView() {
    }


    public void setArticleDeleteCallBack(ParameCallBack articleDeleteCallBack) {
        this.articleDeleteCallBack = articleDeleteCallBack;
    }

    class ShopArticleRequest extends BaseRequest {
        private List<PromotionArticle> articles;

        public ShopArticleRequest() {
            addParams("shopId", Account.user.getShopId());
            if (type == 1) {
                addParams("userId", Account.user.getId());
            }
            addParams("pageSize", 20);
        }

        public List<PromotionArticle> getArticles() {
            return articles;
        }

        public void setArticles(List<PromotionArticle> articles) {
            this.articles = articles;
        }

        @Override
        protected void fire(String data) throws AppException {

            try {

                if (DataUtil.stringIsNull(data))
                    return;
                if (type == 1) {
                    JSONObject jsonObject = new JSONObject(data);
                    if (jsonObject.has("customePopuArticleList")) {
                        TypeToken<List<PromotionArticle>> types = new TypeToken<List<PromotionArticle>>() {
                        };
                        this.articles = GsonParser.getInstance().fromJson(jsonObject.optString("customePopuArticleList"), types);
                    }
                } else {

                    TypeToken<List<PromotionArticle>> types = new TypeToken<List<PromotionArticle>>() {
                    };
                    this.articles = GsonParser.getInstance().fromJson(data, types);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String method() {
            if (type == 1)//自定义文章
            {
                return "/makeFans/custome/popuArticle/list";
            } else {
                return "/keduoduo/fans/industriesArticle";
            }

        }

    }


}
