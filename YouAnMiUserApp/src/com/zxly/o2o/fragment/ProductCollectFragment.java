package com.zxly.o2o.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.adapter.ActivityProductAdapter;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshGridView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ProductCollectListRequest;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

public class ProductCollectFragment extends BaseFragment implements OnRefreshListener {
    private PullToRefreshGridView gridView;
    private ActivityProductAdapter adapter;
    private LoadingView loadingview;
    private int page = 1;
    private boolean isLastData;
    private boolean isInit = false;
    private List<NewProduct> productList = new ArrayList<NewProduct>();

    @Override
    protected void initView() {
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        gridView = (PullToRefreshGridView) findViewById(R.id.grid_view);
        gridView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new ActivityProductAdapter(this.getActivity());
        adapter.hideLikeCount();
        gridView.setAdapter(adapter);
        ViewUtils.setRefreshText(gridView);
        adapter.addItem(productList, true);
        gridView.setOnRefreshListener(this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                final NewProduct nProduct = (NewProduct) adapter.getItem(arg2 - 1);
                ProductInfoAct.start(ProductCollectFragment.this.getActivity(), nProduct, new ParameCallBack() {

                    @Override
                    public void onCall(Object object) {
                        adapter.getContent().remove(nProduct);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isInit) {
            loadData(page);
        }
        isInit = true;
    }

    private void loadData(final int page) {
        final ProductCollectListRequest request = new ProductCollectListRequest(page);
        request.setTag(this);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                productList = request.getProductList();
                boolean isEmpty = productList.isEmpty();
                gridView.onRefreshComplete();
                if (isEmpty) {
                    isLastData = true;
                    if (page == 1) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        loadingview.onDataEmpty();
                    } else {
                        if (isLastData)
                            ViewUtils.showToast("亲,没有更多了!");
                    }
                    return;
                }
                if (page == 1) {
                    adapter.clear();
                    loadingview.onLoadingComplete();
                    ViewUtils.setGone(loadingview);
                }
                adapter.addItem(productList, true);
            }

            @Override
            public void onFail(int code) {
                if (page == 1) {
                    loadingview.onLoadingFail();
                }
            }
        });
        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                if (page == 1) {
                    loadingview.startLoading();
                }
                request.start(ProductCollectFragment.this);
            }
        });
        if (page == 1) {
            loadingview.startLoading();
        }
        request.start(this);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
            page = 1;
            loadData(page);
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            if (!isLastData) {
                page++;
                loadData(page);
            } else {
                mMainHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        gridView.onRefreshComplete();
                    }
                }, 1000);
            }
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.product_collect;
    }

}
