package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.widget.GridView;

import com.zxly.o2o.adapter.CommissionProductAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.pullrefresh.PullToRefreshAdapterViewBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshGridView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PromotionProductListRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by kenwu on 2015/12/3.
 */
public class PromotionProductFragment extends BaseFragment {

    private PullToRefreshGridView mGridView;
    private ObjectAdapter adapter;
    private LoadingView loadingView;
    private int pageIndex=1;
    private PromotionProductListRequest request;

    public static PromotionProductFragment newInstance(){
        PromotionProductFragment f=new PromotionProductFragment();
        Bundle args = new Bundle();
        args.putInt("type", 1);
        f.setArguments(args);
        return f;
    }

    public static void setRefreshText(PullToRefreshAdapterViewBase mPullToRefrehView) {
        mPullToRefrehView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefrehView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mPullToRefrehView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mPullToRefrehView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        mPullToRefrehView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        mPullToRefrehView.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                AppController.getInstance().getString(R.string.pull_to_refresh_down));
        mPullToRefrehView.getLoadingLayoutProxy(true, false).setReleaseLabel(
                AppController.getInstance().getString(R.string.pull_to_releaseL_down));

    }


    @Override
    protected void initView(Bundle bundle) {
        loadingView=(LoadingView) findViewById(R.id.view_loading);
        loadingView.startLoading();
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                loadData(1);
            }
        });


        mGridView = (PullToRefreshGridView) findViewById(R.id.gridView);
        ViewUtils.setRefreshBaseText(mGridView);
        mGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    // 加载下啦数据
                    loadData(1);
                }
                if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    // 加载上拉数据
                    loadData(pageIndex);
                }
            }
        });


        if (adapter==null){
            adapter = new CommissionProductAdapter(getActivity());
        }

        mGridView.setAdapter(adapter);
        loadData(1);
    }

    @Override
    protected void initView() {
    }


    @Override
    protected int layoutId() {
        return R.layout.tab_gridview;
    }


    public void loadData(final int pageId) {
        if(DataUtil.listIsNull(adapter.getContent()))
            loadingView.startLoading();

        if(request==null){
            request=new PromotionProductListRequest();
            request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    if (!DataUtil.listIsNull(request.getProducts())) {
                        if (pageIndex == 1)
                            adapter.clear();

                        adapter.addItem(request.getProducts(), true);
                        request.setProducts(null);
                        pageIndex++;
                        loadingView.onLoadingComplete();
                    } else {
                        //下拉刷新的时候发现数据为空，清空list
                        if (pageIndex == 1) {
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                            loadingView.onDataEmpty("暂无内容",R.drawable.img_default_tired);
                        } else {
                            //最后一页
                        }
                    }

                    if (mGridView.isRefreshing())
                        mGridView.onRefreshComplete();

                    if(request.hasNextPage){
                        mGridView.setMode(PullToRefreshBase.Mode.BOTH);
                    } else {
                        mGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                }

                @Override
                public void onFail(int code) {
                    if (DataUtil.listIsNull(adapter.getContent()))
                        loadingView.onLoadingFail();

                    if (mGridView.isRefreshing())
                        mGridView.onRefreshComplete();
                }

            });
        }

        pageIndex=pageId;
        request.addParams("pageIndex",pageIndex);
        request.start(getActivity());
    }


}
