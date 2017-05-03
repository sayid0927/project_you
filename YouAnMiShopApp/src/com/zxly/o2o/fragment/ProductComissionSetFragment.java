package com.zxly.o2o.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.model.ProductBrand;
import com.zxly.o2o.model.ProductType;
import com.zxly.o2o.model.SortItem;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ComInitRequest;
import com.zxly.o2o.request.CommissionRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.PullView;
import com.zxly.o2o.view.SpinnerView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class ProductComissionSetFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener, PullView.OnSelChangeListener {

    private SpinnerView turnType;
    private SpinnerView turnBrand;
    private SpinnerView turnSort;
    private PullToRefreshListView mListView;
    public List<Product> selProductList=new ArrayList<Product>();
    public List<Product> allProductList=new ArrayList<Product>();
    private LoadingView loadingview;
    private int pageIndex;
    private CommissionProductSetAdapter adapter;
    private boolean isLastData, isCondition;
    private int type;//1，已设置佣金的商品 0,未设置佣金的商品
    private boolean isAllSel;
    private boolean isInit;
    private boolean isVisibleToUser;

    public ProductComissionSetFragment(int type) {
        this.type = type;
    }

    @Override
    protected void initView() {
        FrameLayout spinnerContent = (FrameLayout) findViewById(R.id.spinner_content);
        turnType = (SpinnerView) findViewById(R.id.turn_type);
        turnBrand = (SpinnerView) findViewById(R.id.turn_brand);
        turnSort = (SpinnerView) findViewById(R.id.turn_sort);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        mListView = (PullToRefreshListView) findViewById(R.id.goods_listview);
        mListView.setIntercept(true);
        adapter=new CommissionProductSetAdapter(this.getActivity());
        mListView.setAdapter(adapter);
        mListView.setDivideHeight(0);
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

    }


    @Override
    public void onResume() {
        super.onResume();
        if(isVisibleToUser&&isInit)
        {
            adapter.clear();
            adapter.addItem(allProductList, true);
        }
    }

    public void notifyDataSetChanged()
    {
        adapter.clear();
        adapter.addItem(allProductList, true);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            if(!isInit)
            {
                init();
            }else
            {
                adapter.clear();
                adapter.addItem(allProductList, true);

                if(allProductList.isEmpty())
                {
                    if(type==0)
                    {
                        loadingview.onDataEmpty("无未设置佣金的商品");
                    }else {
                        loadingview.onDataEmpty("无已设置佣金的商品");
                    }

                }else
                {
                    loadingview.onLoadingComplete();
                }

            }

        }/* else {
            //相当于Fragment的onPause
        }*/
    }
    private void init()
    {
        final ComInitRequest request = new ComInitRequest(type);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                isInit=true;
                ViewUtils.setVisible(mListView);

                turnType.setData(request
                        .getProductTypeList());
                turnBrand.setData(request
                        .getProductBrandList());
                turnSort.setData(request
                        .getSortItemList());
                if (!request.getProductList().isEmpty()) {
                    allProductList.addAll(request.getProductList());
                    adapter.addItem(allProductList, true);
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
                request.start(ProductComissionSetFragment.this
                        .getActivity());
            }
        });
        loadingview.startLoading();
        request.start(ProductComissionSetFragment.this.getActivity());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public boolean isAllSel() {
        return isAllSel;
    }

    public int getType() {
        return type;
    }

    public void allSel()
    {
        isAllSel=true;
        selProductList.clear();
        selProductList.addAll(allProductList);
        adapter.notifyDataSetChanged();
    }
    public void cancelSel()
    {
        isAllSel=false;
        selProductList.clear();
        adapter.notifyDataSetChanged();
    }


    @Override
    protected int layoutId() {
        return R.layout.win_product_generalize;
    }

    private void loadData(final int pageIndex) {


        ProductType proType = (ProductType) turnType.getSelItem();
        ProductBrand productBrand = (ProductBrand) turnBrand.getSelItem();
        SortItem sortItem = (SortItem) turnSort.getSelItem();
        final CommissionRequest request = new CommissionRequest(type, productBrand.getId(), proType.getId(), sortItem.getCode(), pageIndex);
        request.setTag(this);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                boolean isEmpty = request.getProductList().isEmpty();
                mListView.onRefreshComplete();
                if (pageIndex == 1) {

                    allProductList.clear();
                    if (isCondition)// 是否有条件
                    {
                        isCondition = false;
                        allProductList.addAll(request.getProductList());
                        adapter.addItem(allProductList, true);
                        return;
                    } else {
                        adapter.clear();
                    }
                    if(isEmpty)
                    {
                        loadingview.onDataEmpty("无未设置佣金的商品");
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

                allProductList.addAll(request.getProductList());
                adapter.addItem(allProductList, true);
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
        isCondition = true;
        adapter.clear();
        loadData(pageIndex);
    }

    private class CommissionProductSetAdapter extends ObjectAdapter {

        public CommissionProductSetAdapter(Context _context) {
            super(_context);

        }

        @Override
        public int getLayoutId() {
            return R.layout.item_comission_product;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null)
            {
                convertView=inflateConvertView();
                holder=new ViewHolder();
                holder.itemIcon= (NetworkImageView) convertView.findViewById(R.id.img_item_icon);
                holder.imgPlotFlag= (ImageView) convertView.findViewById(R.id.img_plot_flag);
                holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
                holder.txtPrice= (TextView) convertView.findViewById(R.id.txt_price);
                holder.txtComission= (TextView) convertView.findViewById(R.id.txt_comission);
                holder.txtOldPrice= (TextView) convertView.findViewById(R.id.txt_old_price);
                holder.txtScale= (TextView) convertView.findViewById(R.id.txt_scale);
                holder.checkSel=convertView.findViewById(R.id.check_sel);
                holder.lineBottom=convertView.findViewById(R.id.line_bottom);
                convertView.setTag(holder);
            }else
            {
                holder= (ViewHolder) convertView.getTag();
            }
            final Product product= (Product) getItem(position);
            if(selProductList.contains(product))
            {
                holder.checkSel.setSelected(true);
            }else
            {
                holder.checkSel.setSelected(false);
            }
            switch (product.getTypeCode()) {
                case 1:
                    ViewUtils.setVisible(holder.imgPlotFlag);
                    holder.imgPlotFlag.setBackgroundResource(R.drawable.qianggou);
                    break;
                case 2:
                    holder.imgPlotFlag.setBackgroundResource(R.drawable.youhui);
                    ViewUtils.setVisible(holder.imgPlotFlag);
                    break;
                default:
                    ViewUtils.setGone(holder.imgPlotFlag);
                    break;
            }
            holder.itemIcon.setDefaultImageResId(R.drawable.product_def);
            holder.itemIcon.setImageUrl(product.getHeadUrl(),
                    AppController.imageLoader);
            float curPrice = product.getPrice()-product.getPreference();
            ViewUtils.setText(holder.txtName, product.getName());
            if(product.getPreference()>0)
            {
                ViewUtils.strikeThruText(holder.txtOldPrice,  product.getPrice());
            }
            String commission;
            SpannableString ss1;
            if(product.getComission()>0)
            {
                commission="佣金：￥"+product.getComission();
                ViewUtils.setText(holder.txtScale,product.getRate()+"%");
                ss1=new SpannableString(commission);
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#EB3434")), 3, commission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else
            {
                commission="无佣金";
                ss1=new SpannableString(commission);
                ViewUtils.setText(holder.txtScale,"");
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0,  commission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.txtComission.setText(ss1);
            ViewUtils.setTextPrice(holder.txtPrice, curPrice);
            if (position == getCount() - 1) {
                ViewUtils.setVisible(holder.lineBottom);

            } else {
                ViewUtils.setInvisible(holder.lineBottom);
            }
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(selProductList.contains(product))
                    {
                        selProductList.remove(product);
                    }else
                    {
                        selProductList.add(product);
                    }
                    updateSingleRow(mListView.getRefreshableView(),product);

                }
            });
            return convertView;
        }
        class ViewHolder{
            NetworkImageView itemIcon;
            ImageView imgPlotFlag;
            TextView txtName,txtPrice,txtComission,txtOldPrice,txtScale;
            View checkSel,lineBottom;
        }
    }
}
