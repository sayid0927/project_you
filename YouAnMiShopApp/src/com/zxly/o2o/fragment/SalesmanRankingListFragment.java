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
import android.view.LayoutInflater;
import android.widget.ListView;

import com.zxly.o2o.activity.SalesmanRankingAct;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.adapter.SalesmanRankingAdapter;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.SalesmanRankingRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.Map;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-5-27
 * @since      YIBA-O2O
 */
public class SalesmanRankingListFragment extends DateListFragment implements ResponseStateListener {
	
	private PullToRefreshListView mListView;
	private ObjectAdapter adapter;
	private LoadingView loadingView;
	private int type;
	int year;
	int month;
	SalesmanRankingRequest request;
	public static SalesmanRankingListFragment newInstance(int type){
		SalesmanRankingListFragment f=new SalesmanRankingListFragment();
		Bundle args = new Bundle();
		args.putInt("type", type);
		f.setArguments(args);
		return f;
	}

	
	@Override
	protected void initView(Bundle bundle) {
		super.initView(bundle);
		year=monthAdapter.getSelectYear();
		month=monthAdapter.getSelectMonth();
		loadingView=(LoadingView) findViewById(R.id.view_loading);
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
		mListView.setIntercept(true);


		ViewUtils.setRefreshText(mListView);
		mListView.setMode(Mode.PULL_FROM_START);

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
					// 加载下啦数据
					loadData(year, month);
				}


			}
		});

		loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
			@Override
			public void onLoading() {
				loadingView.startLoading();
				loadData(year, month);
			}
		});

		if (adapter==null){
			Bundle args=getArguments();
			type=args.getInt("type");
			adapter = new SalesmanRankingAdapter(getActivity());
			((SalesmanRankingAdapter)adapter).setType(type);
		}

		if(((SalesmanRankingAct)getActivity()).curType==type)
			loadData(year,month);

		if(type==SalesmanRankingAct.PROMOTION_TYPE_PRODUCT|type==SalesmanRankingAct.PROMOTION_TYPE_ARTICLE){
			mListView.addH(LayoutInflater.from(getActivity()).inflate(R.layout.item_ranking_header,null));
		}

		mListView.setAdapter(adapter);

	}


	@Override
	public void onResume() {
		super.onResume();
	}


	public void refreshData(){
		loadData(year,month);
	}

	public void loadData(int year,int month) {
		if(!mListView.isRefreshing()) {
			loadingView.startLoading();
		}
		request = new SalesmanRankingRequest(type,year,month){

			protected String method() {
				if(type== SalesmanRankingAct.PROMOTION_TYPE_PRODUCT){
					return "/shopRank/productRank";
				}else if(type== SalesmanRankingAct.PROMOTION_TYPE_ARTICLE){
					return "/shopRank/articleRank";
				}else {
					return "/shopRank/staffRank";
				}

			}
		};
		request.setOnResponseStateListener(this);
		request.start(getActivity());
	}


	@Override
	public void onOK() {
		if (!DataUtil.listIsNull(request.getList())) {
			adapter.clear();
			adapter.addItem(request.getList(), true);
			loadingView.onLoadingComplete();
		} else {
			//下拉刷新的时候发现数据为空，
			adapter.clear();
			adapter.notifyDataSetChanged();
			loadingView.onDataEmpty("暂无数据",R.drawable.img_default_tired);
		}

		mListView.onRefreshComplete();
	}



	@Override
	public void onFail(int code) {
		if(DataUtil.listIsNull(adapter.getContent()))
			loadingView.onLoadingFail();
		
		if (mListView.isRefreshing())
			mListView.onRefreshComplete();		
	}



	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCall(Object data) {
		super.onCall(data);
		Map<String,Integer> result= (Map<String, Integer>) data;
		year=result.get("year");
		month=result.get("month");
		loadingView.startLoading();
		loadData(year, month);

	}

}
