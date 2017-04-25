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
import android.widget.ListView;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.adapter.RecommendPromotionAdapter;
import com.zxly.o2o.model.ActicityInfo;
import com.zxly.o2o.model.PromotionArticle;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class PromotionRecommendFragment extends BaseFragment implements ResponseStateListener {
	
	private PullToRefreshListView mListView;
	private ObjectAdapter adapter;
	private LoadingView loadingView;
	DataListRequest request;

	public static PromotionRecommendFragment newInstance(){
		PromotionRecommendFragment f=new PromotionRecommendFragment();
		Bundle args = new Bundle();
		args.putInt("status", 1);
		args.putInt("type", 1);
		f.setArguments(args);
		return f;
	}

	
	@Override
	protected void initView(Bundle bundle) {
		loadingView=(LoadingView) findViewById(R.id.view_loading11);
		mListView = (PullToRefreshListView) findViewById(R.id.listview);
        mListView.setIntercept(true);
		ViewUtils.setRefreshText(mListView);
		mListView.setMode(Mode.PULL_FROM_START);

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
					loadData();
				}
            }
        });
		 
		loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                loadData();
            }
        });

		if (adapter==null){
			adapter = new RecommendPromotionAdapter(getActivity());
		}
		
		mListView.setAdapter(adapter);
	}


	@Override
	protected void loadInitData() {
		if(DataUtil.listIsNull(adapter.getContent())){
			loadData();
		}
	}

	public void loadData() {
		if(DataUtil.listIsNull(adapter.getContent()))
		  loadingView.startLoading();

		if (request==null){
			request = new DataListRequest();
			request.setOnResponseStateListener(this);
		}

		request.start(getActivity());
	}

	@Override
	protected int layoutId() {
		return R.layout.tag_listview;
	}


	List<Map<String,Object>> dataList=new ArrayList<Map<String, Object>>();

	@Override
	public void onOK() {
		adapter.clear();
		if(DataUtil.listIsNull(request.getActivityList())&&
		   DataUtil.listIsNull(request.getArticleList())){
			loadingView.onDataEmpty();
		}else {
			dataList.clear();
			if(!DataUtil.listIsNull(request.getActivityList())){
				Map<String,Object> item_activity=new HashMap<String, Object>();
				item_activity.put("data", request.getActivityList());
				dataList.add(item_activity);
			}

			if(!DataUtil.listIsNull(request.getArticleList())){
				Map<String,Object> item_article=new HashMap<String, Object>();
				item_article.put("data",request.getArticleList());
				dataList.add(item_article);
			}

			adapter.addItem(dataList);
			request.setActivityList(null);
			request.setArticleList(null);
			loadingView.onLoadingComplete();
		}

		adapter.notifyDataSetChanged();
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



	class DataListRequest extends BaseRequest {
		private List<ActicityInfo> activityList;
		private List<PromotionArticle> articleList;
		public DataListRequest(){
			addParams("shopId", Account.user==null?0:Account.user.getShopId());
			addParams("userId",Account.user==null?0:Account.user.getId());
		}

		public List<ActicityInfo> getActivityList() {
			return activityList;
		}


		public List<PromotionArticle> getArticleList() {
			return articleList;
		}

		public void setArticleList(List<PromotionArticle> articleList) {
			this.articleList = articleList;
		}

		public void setActivityList(List<ActicityInfo> activityList) {
			this.activityList = activityList;
		}

		@Override
		protected void fire(String data) throws AppException {
			try {
				JSONObject jsonRoot = new JSONObject(data);
				if(jsonRoot.has("activityList")){
					TypeToken<List<ActicityInfo>> types = new TypeToken<List<ActicityInfo>>() {};
					activityList= GsonParser.getInstance().fromJson(jsonRoot.getString("activityList"), types);
				}

				if(jsonRoot.has("articleList")){
					TypeToken<List<PromotionArticle>> types = new TypeToken<List<PromotionArticle>>() {};
					articleList= GsonParser.getInstance().fromJson(jsonRoot.getString("articleList"), types);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


		}

		@Override
		protected String method() {
			return "/makeFans/recommendActivityList";
		}

	}



}
