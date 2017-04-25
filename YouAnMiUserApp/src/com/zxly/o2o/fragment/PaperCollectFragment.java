package com.zxly.o2o.fragment;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.activity.MyCircleThirdAct;
import com.zxly.o2o.activity.WebViewAct;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.adapter.ShopArticleAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.CollectArticleModel;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.model.ShopArticle;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.ArticlePraiseRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CollectPaperRequest;
import com.zxly.o2o.request.ProductCollectListRequest;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.CallBackWithParam;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/8/16.
 * 我的收藏--文章（由于单词article已被同页面“帖子占用”故用paper 注意区别下）
 */
public class PaperCollectFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener {

    private LoadingView loadingView;
    private PullToRefreshListView listView;
    private int pageIndex = 1;
    private boolean isLastData;
    private boolean hasInit = false;
    private ShopArticleAdapter myAdapter;

    @Override
    protected void initView() {
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        listView = (PullToRefreshListView) findViewById(R.id.list_view);
        listView.setIntercept(true);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        myAdapter = new ShopArticleAdapter(getActivity(),getActivity(),true);
        listView.setAdapter(myAdapter);
        ViewUtils.setRefreshText(listView);
        listView.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!hasInit) {
            loadData(pageIndex);
//        }
//        hasInit = true;
    }

    private void loadData(final int page) {
        final CollectPaperRequest request = new CollectPaperRequest(page);
        request.setTag(this);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {



            @Override
            public void onOK() {
                List<ShopArticle> shopArticleList = request.getPaperList();
                boolean isEmpty = shopArticleList.isEmpty();
                listView.onRefreshComplete();
                if (isEmpty) {
                    isLastData = true;
                    if (page == 1) {
                        myAdapter.clear();
                        myAdapter.notifyDataSetChanged();
                        loadingView.onDataEmpty("没有收藏的文章!");
                    } else {
                            ViewUtils.showToast("亲,没有更多了!");
                    }
                    return;
                }
                if (page == 1) {
                    myAdapter.clear();
                    loadingView.onLoadingComplete();
                    ViewUtils.setGone(loadingView);
                }
                myAdapter.addItem(shopArticleList, true);
            }

            @Override
            public void onFail(int code) {
                if (page == 1) {
                    loadingView.onLoadingFail();
                }
            }
        });
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                if (page == 1) {
                    loadingView.startLoading();
                }
                request.start(getActivity());
            }
        });
        if (page == 1) {
            loadingView.startLoading();
        }
        request.start(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_mycollect_paper;
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;
            loadData(pageIndex);
        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            if (!isLastData) {
                pageIndex++;
                loadData(pageIndex);
            } else {
                mMainHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                }, 1000);
            }
        }
    }


}
