package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.adapter.ActivityProductAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.PhonePrice;
import com.zxly.o2o.model.ProductBrand;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshGridView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PhoneBrandRequest;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.PullView;
import com.zxly.o2o.view.SpinnerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by kenwu on 2015/12/3.
 */
public class RecommendProductFragment extends BaseFragment implements PullView.OnSelChangeListener,View.OnClickListener, AbsListView.OnScrollListener {


    private PullToRefreshGridView mGridView;
    private ObjectAdapter adapter;
    private LoadingView loadingView;
    private int pageIndex;
    private int type;
    private ProductListRequest request;

    private  View btnUpToFirst;
    private PhoneBrandRequest phoneBrandRequest;
    private List<ProductBrand> phoneBrands;
    private List<PhonePrice> phonePrices;

    private SpinnerView turnBrand;
    private SpinnerView turnPrice;
    private long brandId;
    private String priceLevel;
    private long lastExecTime = 0;

    public static RecommendProductFragment newInstance(int type){
        RecommendProductFragment f=new RecommendProductFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        f.setArguments(args);
        return f;
    }


    @Override
    protected void initView(Bundle bundle) {

        btnUpToFirst=findViewById(R.id.btn_upToFirst);
        //   btnUpToFirst.setVisibility(View.VISIBLE);
        btnUpToFirst.setOnClickListener(this);
        loadingView=(LoadingView) findViewById(R.id.view_loading);
        mGridView = (PullToRefreshGridView) findViewById(R.id.gridView);
        mGridView.getRefreshableView().setOnScrollListener(this);

        // mGridView.setDivideHeight(DesityUtil.dp2px(getActivity(), 10));
        // mListView.setIntercept(true);
        // mListView.setMode(Mode.PULL_FROM_END);
        ViewUtils.setRefreshText(mGridView);

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

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                loadData(1);
            }
        });

        Bundle args=getArguments();
        type=args.getInt("type");

        if(DiscoveryFragment.TYPE_MOBILEPHONE == type){
            FrameLayout spinnerContent = (FrameLayout) findViewById(R.id.spinner_content);

            ViewUtils.setVisible(findViewById(R.id.spi_layout));

            turnBrand = (SpinnerView) findViewById(R.id.turn_brand);
            turnBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long curTime = System.currentTimeMillis();
                    if (curTime - lastExecTime > 500) {
                        if (turnBrand.getIsExpan()) {
                            turnBrand.up();
                        } else {
                            phoneBrandRequest = new PhoneBrandRequest();
                            phoneBrandRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                                @Override
                                public void onOK() {
                                    phoneBrands = phoneBrandRequest.getPhoneBrands();
                                    turnBrand.setData(phoneBrands);
                                    turnBrand.pull();
                                }

                                @Override
                                public void onFail(int code) {
                                }
                            });
                            phoneBrandRequest.start(getActivity());

                        }
                        lastExecTime = curTime;
                    }
                }
            });
            turnPrice = (SpinnerView) findViewById(R.id.turn_price);
            turnBrand.setContent(spinnerContent);
            turnPrice.setContent(spinnerContent);
            turnBrand.setOnSelChangeListener(this);
            turnPrice.setOnSelChangeListener(this);
            turnBrand.setDefValue(new ProductBrand(0, "按品牌"));
            turnPrice.setDefValue(new PhonePrice("0", "按价钱"));

            phoneBrandRequest = new PhoneBrandRequest();
            phoneBrandRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    phonePrices = phoneBrandRequest.getPhonePrices();
                    turnPrice.setData(phonePrices);
                }

                @Override
                public void onFail(int code) {
                }
            });
            phoneBrandRequest.start(getActivity());

        }

        if (adapter==null)
            adapter = new ActivityProductAdapter(getActivity());

        mGridView.setAdapter(adapter);

        if (request==null){
            request=new ProductListRequest(type, brandId, priceLevel);
            request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    if (!DataUtil.listIsNull(request.getProducts())) {
                        if(pageIndex==1)
                            adapter.clear();

                        adapter.addItem(request.getProducts(), true);
                        request.setProducts(null);
                        pageIndex++;
                        loadingView.onLoadingComplete();
                    } else {
                        //下拉刷新的时候发现数据为空，清空list
                        if(pageIndex==1){
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                            loadingView.onDataEmpty();
                        }else{
                            //最后一页
                        }
                    }

                    if (mGridView.isRefreshing())
                        mGridView.onRefreshComplete();
                }

                @Override
                public void onFail(int code) {
                    if(DataUtil.listIsNull(adapter.getContent()))
                        loadingView.onLoadingFail();

                    if (mGridView.isRefreshing())
                        mGridView.onRefreshComplete();
                }

            });
        }

    }

    void loadFilterProductList(){
//        if(DataUtil.listIsNull(adapter.getContent()))
            loadingView.startLoading();

        request=new ProductListRequest(type, brandId, priceLevel);
        request.setPageIndex(pageIndex);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (!DataUtil.listIsNull(request.getProducts())) {
                    if(pageIndex==1)
                        adapter.clear();

                    adapter.addItem(request.getProducts(), true);
                    request.setProducts(null);
                    pageIndex++;
                    loadingView.onLoadingComplete();
                } else {
                    //下拉刷新的时候发现数据为空，清空list
                    if(pageIndex==1){
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        loadingView.onDataEmpty();
                    }else{
                        //最后一页
                    }
                }

                if (mGridView.isRefreshing())
                    mGridView.onRefreshComplete();
            }

            @Override
            public void onFail(int code) {
                if(DataUtil.listIsNull(adapter.getContent()))
                    loadingView.onLoadingFail();

                if (mGridView.isRefreshing())
                    mGridView.onRefreshComplete();
            }

        });
        request.start(getActivity());
    }

    @Override
    protected void initView() {
    }


    @Override
    protected void loadInitData() {
        if (DataUtil.listIsNull(adapter.getContent()))
            loadData(1);
    }


    public void loadData(int pageId) {
        if(DataUtil.listIsNull(adapter.getContent()))
            loadingView.startLoading();

        pageIndex=pageId;
        request.setPageIndex(pageId);
        request.start(getActivity());
    }

    @Override
    protected int layoutId() {
        return R.layout.tab_gridview;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upToFirst:
                mGridView.getRefreshableView().smoothScrollToPosition(0);
                break;

            default:
                break;
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem > 3) {
            btnUpToFirst.setVisibility(View.VISIBLE);
        } else {
            if(btnUpToFirst.getVisibility()==View.VISIBLE)
                btnUpToFirst.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSelChange() {
        ProductBrand productBrand = (ProductBrand) turnBrand.getSelItem();
        brandId = productBrand.getId();
        PhonePrice phonePrice = (PhonePrice) turnPrice.getSelItem();
        priceLevel = phonePrice.getLevel();

        pageIndex = 1;
        adapter.clear();
//        isCondition = true;

        loadFilterProductList();

    }


    static class ProductListRequest extends BaseRequest{

        List<NewProduct> products;

        public ProductListRequest(int type, long brandId, String priceLevel){
            addParams("shopId", Config.shopId);
            addParams("type", type);
            if(DiscoveryFragment.TYPE_MOBILEPHONE == type) {
                if (brandId != 0) {
                    addParams("brandId", brandId);
                }
                if (!"0".equals(priceLevel)) {
                    addParams("priceInterval", priceLevel);
                }
            }
        }

        public void setPageIndex(int pageIndex) {
            addParams("pageIndex", pageIndex);
        }

        @Override
        protected void fire(String data) throws AppException {
            try {
                JSONObject  jo = new JSONObject(data);
                String jsonStr=jo.getString("productInfos");
                TypeToken<List<NewProduct>> token = new TypeToken<List<NewProduct>>() {};
                products = GsonParser.getInstance().fromJson(jsonStr, token);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public List<NewProduct> getProducts() {
            return products;
        }

        public void setProducts(List<NewProduct> products) {
            this.products = products;
        }

        @Override
        protected String method() {
            return "/appFound/productList";
        }
    }

}
