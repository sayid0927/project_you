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
import android.util.Log;
import android.widget.ListView;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.adapter.MyOrderAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

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
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-5-27
 * @since      YIBA-O2O
 */
public class MyOrderListFragment extends BaseFragment {

	private PullToRefreshListView mListView;
	private ObjectAdapter adapter;
	private int status=-1;
	private LoadingView loadingView;
	private int pageIndex=1;
	OrderListRequest request;

	public static MyOrderListFragment newInstance(int status){
		MyOrderListFragment f=new MyOrderListFragment();
		Bundle args = new Bundle();
		args.putInt("status", status);
		f.setArguments(args);
		return f;
	}


	@Override
	protected void initView(Bundle bundle) {
		loadingView=(LoadingView) findViewById(R.id.view_loading11);
		loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
			@Override
			public void onLoading() {
				loadingView.startLoading();
				loadData(1);
			}
		});

		mListView = (PullToRefreshListView) findViewById(R.id.win_activity_goods_list);
		mListView.setDivideHeight(DesityUtil.dp2px(getActivity(), 10));
		mListView.setIntercept(true);
		ViewUtils.setRefreshText(mListView);
		mListView.setMode(Mode.DISABLED);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
					// 加载下啦数据
					loadData(1);
				}
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
					// 加载上拉数据
					loadData(pageIndex);
				}

			}
		});

		Bundle args=getArguments();
		status=args.getInt("status");
		if (adapter==null){
			adapter = new MyOrderAdapter(getActivity(),status);
		}

		mListView.setAdapter(adapter);

		if(request==null){
			request=new OrderListRequest(status);
			request.setOnResponseStateListener(new ResponseStateListener() {
				@Override
				public void onOK() {
					mListView.setMode(Mode.BOTH);
					if (!DataUtil.listIsNull(request.getOrderList())) {
						if(pageIndex==1)
							adapter.clear();

						adapter.addItem(request.getOrderList(), true);
						request.setOrderList(null);
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

					if (mListView.isRefreshing())
						mListView.onRefreshComplete();
				}

				@Override
				public void onFail(int code) {
					if(DataUtil.listIsNull(adapter.getContent()))
						loadingView.onLoadingFail();

					if (mListView.isRefreshing())
						mListView.onRefreshComplete();
				}

			});

		}

	}

	@Override
	protected void initView() {
	}

	@Override
	protected void loadInitData() {
		loadData(1);
	}

	public void updateData() {
		if(adapter!=null){
			adapter.clear();
			adapter.notifyDataSetChanged();
			loadData(1);
		}		
	}
	
	public void loadData(final int pageId) {
		pageIndex=pageId;
		if(DataUtil.listIsNull(adapter.getContent()))
		  loadingView.startLoading();

		request.addParams("pageIndex",pageIndex);
		request.start(getActivity());
	}


	@Override
	protected int layoutId() {
		return R.layout.tab_list;
	}


	static class OrderListRequest extends BaseRequest {

		private List<OrderInfo> orderList;

		public OrderListRequest(int status){
			addParams("shopId", Config.shopId);
			addParams("status",status);

		}

		public void setPageIndex(int pageIndex){
			addParams("pageIndex",pageIndex);
		}

		public List<OrderInfo> getOrderList() {
			return orderList;
		}

		public void setOrderList(List<OrderInfo> orderList) {
			this.orderList = orderList;
		}

		@Override
		protected void fire(String data) throws AppException {
			try {
				TypeToken<List<OrderInfo>> types = new TypeToken<List<OrderInfo>>() {};
				orderList= GsonParser.getInstance().fromJson(data,types);
			} catch (AppException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String method() {
			return "/order/list";
		}

	}




}
