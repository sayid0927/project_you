/*
 * 文件名：ActivityProductAct.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ActivityProductAct.java
 * 修改人：wuchenhui
 * 修改时间：2015-6-10
 * 修改内容：新增
 */
package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ActivityProductAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.dialog.SelectChatDialog;
import com.zxly.o2o.model.Filters;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.ProductBrand;
import com.zxly.o2o.model.ProductType;
import com.zxly.o2o.model.SortItem;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshGridView;
import com.zxly.o2o.request.ActivityProductListFilterRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ProductSortInitRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.PullView.OnSelChangeListener;
import com.zxly.o2o.view.SpinnerView;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 *
 * @author wuchenhui
 * @version YIBA-O2O 2015-6-10
 * @since YIBA-O2O
 */
public class ActivityProductAct extends BasicAct implements OnSelChangeListener, OnClickListener, OnRefreshListener {


    private SpinnerView turnType;
    private SpinnerView turnBrand;
    private SpinnerView turnSort;
    private PullToRefreshGridView mGridView;
    private LoadingView loadingview;
    private ActivityProductAdapter adapter;
    private View btnBack;
    private int index;
    private boolean isLastData, isCondition;

    private int typeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_activity_product);
        initTitle("活动商品", this);
        FrameLayout spinnerContent = (FrameLayout) findViewById(R.id.spinner_content);
        turnType = (SpinnerView) findViewById(R.id.turn_type);
        turnBrand = (SpinnerView) findViewById(R.id.turn_brand);
        turnSort = (SpinnerView) findViewById(R.id.turn_sort);
        btnBack = findViewById(R.id.btn_back);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        mGridView = (PullToRefreshGridView) findViewById(R.id.goods_listview);
        adapter = new ActivityProductAdapter(this);
        adapter.setIsInActivityPage(true);
        //	btnBack.setOnClickListener(this);
        ViewUtils.setRefreshText(mGridView);
        mGridView.setAdapter(adapter);
        //mGridView.setDivideHeight(0);
        turnType.setContent(spinnerContent);
        turnBrand.setContent(spinnerContent);
        turnSort.setContent(spinnerContent);
        turnType.setOnSelChangeListener(this);
        turnBrand.setOnSelChangeListener(this);
        turnSort.setOnSelChangeListener(this);
        mGridView.setOnRefreshListener(this);

        Intent it = getIntent();
        if (it != null) {
            typeCode = it.getIntExtra("typeCode", 0);
        }

        if (typeCode == 0) {
            turnType.setDefValue(new ProductType(-1, "全部类型"));
        } else if (typeCode == 1) {
            turnType.setDefValue(new ProductType(Constants.ACTICITY_LIMITTIME, "限时抢"));
        } else {
            turnType.setDefValue(new ProductType(Constants.ACTICITY_CLEARANCE, "清仓"));
        }

        turnBrand.setDefValue(new ProductBrand(-1, "全部品牌"));
        turnSort.setDefValue(new SortItem("默认排序"));
        final ProductSortInitRequest productListInitRequest = new ProductSortInitRequest(Config.shopId);
        productListInitRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.setVisible(mGridView);
                turnType.setData(productListInitRequest.getProductTypeList());
                turnBrand.setData(productListInitRequest.getProductBrandList());
                turnSort.setData(productListInitRequest.getSortItemList());
                loadData(1);
            }

            @Override
            public void onFail(int code) {
                ViewUtils.setGone(mGridView);
                loadingview.onLoadingFail();
            }

        });

        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                loadingview.startLoading();
                productListInitRequest.start(ActivityProductAct.this);
            }
        });

        loadingview.startLoading();
        productListInitRequest.start(ActivityProductAct.this);

    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            finish();
        }
    }


    private void loadData(final int page) {

        List<Filters> filterList = new ArrayList<Filters>();
        ProductType proType = (ProductType) turnType.getSelItem();
        ProductBrand productBrand = (ProductBrand) turnBrand.getSelItem();
        SortItem sortItem = (SortItem) turnSort.getSelItem();

        if (proType.getId() != -1) {
            filterList.add(new Filters(1, proType.getId()));
        }

        if (productBrand.getId() != -1) {
            filterList.add(new Filters(3, productBrand.getId()));
        }

        filterList.add(new Filters(2, Config.shopId));
        Paging paging = new Paging(page, sortItem.getCode());
        final ActivityProductListFilterRequest request = new ActivityProductListFilterRequest(paging, filterList);
        request.setTag(this);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                boolean isEmpty = request.getProductList().isEmpty();
                mGridView.onRefreshComplete();
                if (page == 1) {
                    if (isEmpty) {
                        loadingview.onDataEmpty();
                    } else {
                        loadingview.onLoadingComplete();
                    }

                    //是否有条件
                    if (isCondition) {
                        isCondition = false;
                        adapter.addItem(request.getProductList());
                        adapter.notifyDataSetChanged();
                        return;
                    } else {
                        adapter.clear();
                    }
                }

                if (page > 1) {
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
                mGridView.onRefreshComplete();
            }

        });

        request.start(this);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

        if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
            index = 1;
            loadData(index);
        } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
            // 加载上拉数据
            if (!isLastData) {
                index++;
                loadData(index);
            } else {
                mMainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mGridView.onRefreshComplete();
                    }
                }, 1000);
            }

        }

    }

    @Override
    public void onSelChange() {
        index = 1;
        adapter.clear();
        isCondition = true;
        loadData(index);
    }

    private void initTitle(String titleName, final Activity activity) {


        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_top_message:
                        if(Account.user!=null) {
                            new SelectChatDialog().show();
                        }else{
                            LoginAct.start(ActivityProductAct.this);
                        }
                        break;

                    case R.id.tag_title_btn_back:
                        activity.finish();
                        break;

                    default:
                        break;
                }

            }
        };

        View backBtn = findViewById(R.id.tag_title_btn_back);
        TextView title = (TextView) findViewById(R.id.txt_title);
        title.setText(titleName);

        findViewById(R.id.btn_top_message).setOnClickListener(listener);
        backBtn.setOnClickListener(listener);

    }


    public static void startActivityProductAct(int typeCode, Activity act) {
        Intent it = new Intent();
        it.setClass(act, ActivityProductAct.class);
        it.putExtra("typeCode", typeCode);
        ViewUtils.startActivity(it, act);
    }


}
