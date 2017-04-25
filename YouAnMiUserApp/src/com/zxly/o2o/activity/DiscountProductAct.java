package com.zxly.o2o.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.zxly.o2o.adapter.ActivityProductAdapter;
import com.zxly.o2o.adapter.CommissionProductAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.Filters;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshGridView;
import com.zxly.o2o.request.ActivityProductListFilterRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CommissionProductRequest;
import com.zxly.o2o.request.DiscountProductRequest;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

public class DiscountProductAct extends BasicAct implements View.OnClickListener {

	PullToRefreshGridView mGridView;
	ActivityProductAdapter adapter;
	LoadingView  loadingView;

	private int pageIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_discount_product);



		View btnBack=findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);

		mGridView= (PullToRefreshGridView) findViewById(R.id.gridView);
		ViewUtils.setRefreshText(mGridView);
		adapter=new ActivityProductAdapter(this);
		adapter.setIsInActivityPage(true);
		mGridView.setAdapter(adapter);
		mGridView.setOnRefreshListener(new OnRefreshListener<GridView>() {
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

        loadData(1);
	}

	public void loadData(final int pageId) {
		if(DataUtil.listIsNull(adapter.getContent()))
			loadingView.startLoading();

		List<Filters> filterList = new ArrayList<Filters>();
		filterList.add(new Filters(2, Config.shopId));
		Paging paging = new Paging(pageId,null);

		final ActivityProductListFilterRequest request = new ActivityProductListFilterRequest(paging, filterList);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
			@Override
			public void onOK() {
				if (!DataUtil.listIsNull(request.getProductList())) {
					if(pageId==1)
						adapter.clear();

					adapter.addItem(request.getProductList(), true);
					pageIndex++;
					loadingView.onLoadingComplete();
				} else {
					//下拉刷新的时候发现数据为空，清空list
					if(pageId==1){
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

		request.start(this);
	}



	@Override
	public void onClick(View v) {

		switch (v.getId()){
			case R.id.btn_back:
				finish();
				break;

		}

	}
}
