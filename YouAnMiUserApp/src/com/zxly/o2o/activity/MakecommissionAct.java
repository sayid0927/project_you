package com.zxly.o2o.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.adapter.CommissionProductAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshGridView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CommissionProductRequest;
import com.zxly.o2o.request.ProductListRequest;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.List;

public class MakecommissionAct extends BasicAct implements View.OnClickListener,AbsListView.OnScrollListener {

	PullToRefreshGridView mGridView;
	private  View btnUpToFirst;
	ObjectAdapter adapter;
    ProductListRequest request;
	LoadingView  loadingView;
	View imgTips;
	private int pageIndex=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_make_commission);
		btnUpToFirst=findViewById(R.id.btn_upToFirst);
		btnUpToFirst.setOnClickListener(this);
		mGridView= (PullToRefreshGridView) findViewById(R.id.gridView);
		mGridView.getRefreshableView().setOnScrollListener(this);
		ViewUtils.setRefreshText(mGridView);
		adapter=new CommissionProductAdapter(this);
		mGridView.setAdapter(adapter);
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

		loadingView= (LoadingView) findViewById(R.id.view_loading);
		loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
			@Override
			public void onLoading() {
				loadData(1);
			}
		});


		View btnBack=findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);

		View btnCommisssionRecord=findViewById(R.id.btn_commissionRecord);
		btnCommisssionRecord.setOnClickListener(this);

		imgTips=findViewById(R.id.view_promotionInit);
        findViewById(R.id.btn_make_money).setOnClickListener(this);
		if(PreferUtil.getInstance().getIsFirstOpenMakeCommission()){
			imgTips.setVisibility(View.VISIBLE);
		}


		request=new ProductListRequest();
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
						loadingView.onDataEmpty();
					} else {
						//最后一页
					}
				}

				if (mGridView.isRefreshing())
					mGridView.onRefreshComplete();
			}

			@Override
			public void onFail(int code) {
				if (DataUtil.listIsNull(adapter.getContent()))
					loadingView.onLoadingFail();

				if (mGridView.isRefreshing())
					mGridView.onRefreshComplete();
			}

		});


		loadData(1);
	}

	public void loadData(final int pageId) {
		if(DataUtil.listIsNull(adapter.getContent()))
			loadingView.startLoading();

		pageIndex=pageId;
		request.setPageIndex(pageId);
		request.start(this);
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem >3) {
			btnUpToFirst.setVisibility(View.VISIBLE);
		} else {
			if(btnUpToFirst.getVisibility()==View.VISIBLE)
				btnUpToFirst.setVisibility(View.GONE);
		}
	}



	@Override
	public void onClick(View v) {

		switch (v.getId()){

			case R.id.btn_back:
				finish();
				break;

			case R.id.btn_upToFirst:
				mGridView.getRefreshableView().smoothScrollToPosition(0);
				break;

			case R.id.btn_commissionRecord:
				Intent it=new Intent();
				it.setClass(this, CommissionRecordAct.class);
				ViewUtils.startActivity(it,this);
				break;

			case R.id.btn_make_money:
				imgTips.setVisibility(View.GONE);
				PreferUtil.getInstance().setMakeCommissionInit();
				break;

		}

	}

	static class ProductListRequest extends BaseRequest{

		private List<NewProduct> products;

		public List<NewProduct> getProducts() {
			return products;
		}


		public ProductListRequest(){
			addParams("shopId", Config.shopId);
		}


		public void setPageIndex(int pageIndex){
			addParams("pageIndex",pageIndex);
		}

		public void setProducts(List<NewProduct> products) {
			this.products = products;
		}

		@Override
		protected void fire(String data) throws AppException {
			TypeToken<List<NewProduct>> token = new TypeToken<List<NewProduct>>() {};
			products = GsonParser.getInstance().fromJson(data, token);
		}


		@Override
		protected String method() {
			return "/appFound/popuProduct";
		}
	}

}
