package com.zxly.o2o.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.zxly.o2o.adapter.ProductAdapter;
import com.zxly.o2o.model.ProductBrand;
import com.zxly.o2o.model.ProductType;
import com.zxly.o2o.model.SortItem;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ProductListInitRequest;
import com.zxly.o2o.request.ProductListPageRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.PullView;
import com.zxly.o2o.view.SpinnerView;

@SuppressLint("ValidFragment")
public class ProductGeneralizeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener, PullView.OnSelChangeListener {

    private SpinnerView turnType;
    private SpinnerView turnBrand;
    private SpinnerView turnSort;
    private PullToRefreshListView mListView;
    private LoadingView loadingview;
    private int pageIndex;
    private ProductAdapter adapter;
    private boolean isLastData, isCondition;
    private int type;//1:上架 0:未上架

   /* public ProductGeneralizeFragment( int type) {
        this.type=type;
    }*/
    public static ProductGeneralizeFragment getInstance(int type){
        ProductGeneralizeFragment pgFragment = new ProductGeneralizeFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        pgFragment.setArguments(args);
        return pgFragment;
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initView(Bundle bundle) {
        type=bundle.getInt("type");
        FrameLayout spinnerContent = (FrameLayout) findViewById(R.id.spinner_content);
        turnType = (SpinnerView) findViewById(R.id.turn_type);
        turnBrand = (SpinnerView) findViewById(R.id.turn_brand);
        turnSort = (SpinnerView) findViewById(R.id.turn_sort);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        mListView = (PullToRefreshListView) findViewById(R.id.goods_listview);
        adapter=new ProductAdapter(this.getActivity(),type);
        mListView.setAdapter(adapter);
        mListView.setDivideHeight(0);
        ViewUtils.setRefreshText(mListView);
        turnType.setContent(spinnerContent);
        turnBrand.setContent(spinnerContent);
        turnSort.setContent(spinnerContent);
        turnType.setOnSelChangeListener(this);
        turnBrand.setOnSelChangeListener(this);
        turnSort.setOnSelChangeListener(this);
        turnType.setDefValue(new ProductType(-1, "全部类型"));
        turnBrand.setDefValue(new ProductBrand(-1, "全部品牌"));
        turnSort.setDefValue(new SortItem("默认排序"));
        ViewUtils.setRefreshText(mListView);
        mListView.setOnRefreshListener(this);
        final ProductListInitRequest request = new ProductListInitRequest(type);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.setVisible(mListView);
                turnType.setData(request
                        .getProductTypeList());
                turnBrand.setData(request
                        .getProductBrandList());
                turnSort.setData(request
                        .getSortItemList());
                if (!request.getProductList().isEmpty()) {
                    adapter.addItem(request
                            .getProductList());
                    adapter.notifyDataSetChanged();
                    loadingview.onLoadingComplete();
                } else {
                    loadingview.onDataEmpty();
                }
            }

            @Override
            public void onFail(int code) {
                ViewUtils.setGone(mListView);
                loadingview.onLoadingFail();
            }
        });
        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                loadingview.startLoading();
                request.start(ProductGeneralizeFragment.this
                        .getActivity());
            }
        });
        loadingview.startLoading();
        request.start(ProductGeneralizeFragment.this.getActivity());
    }

    @Override
    protected int layoutId() {
        return R.layout.win_product_generalize;
    }

    private void loadData(final int pageIndex) {


        ProductType proType = (ProductType) turnType.getSelItem();
        ProductBrand productBrand = (ProductBrand) turnBrand.getSelItem();
        SortItem sortItem = (SortItem) turnSort.getSelItem();
        final ProductListPageRequest request = new ProductListPageRequest(type, productBrand.getId(), proType.getId(), sortItem.getCode(), pageIndex);
        request.setTag(this);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                boolean isEmpty = request.getProductList().isEmpty();
                mListView.onRefreshComplete();
                if (pageIndex == 1) {

                    if (isCondition)// 是否有条件
                    {
                        isCondition = false;
                        adapter.addItem(request.getProductList());
                        adapter.notifyDataSetChanged();
                        return;
                    } else {
                        adapter.clear();
                    }
                    if(isEmpty)
                    {
                        loadingview.onDataEmpty();
                    }else
                    {
                        loadingview.onLoadingComplete();
                    }


                }
                if (pageIndex > 1) {
                    if (isEmpty) {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                        return;
                    }

                }
                adapter.addItem(request.getProductList());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFail(int code) {
                mListView.onRefreshComplete();
            }
        });
        request.start(this);

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {


        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;

            loadData(pageIndex);

        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            // 加载上拉数据
            if (!isLastData) {
                pageIndex++;
                loadData(pageIndex);
            } else {
                mMainHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
                    }
                }, 1000);
            }

        }


    }

    @Override
    public void onSelChange() {
        pageIndex = 1;
        adapter.clear();
        isCondition = true;
        loadData(pageIndex);
    }
}
