package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.zxly.o2o.adapter.PersonalSystemMsgAdapter;
import com.zxly.o2o.adapter.PersonalSystemMsgAdapter.DelCallback;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.Paging;
import com.zxly.o2o.model.SystemMessage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.Mode;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.SystemMsgListRequest;
import com.zxly.o2o.request.SystemMsgRemoveRequest;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.LoadingView.OnAgainListener;

/**
 * @author fengrongjian 2015-1-23
 * @description 系统消息列表
 */
public class PersonalSystemMsgAct extends BasicAct implements
		View.OnClickListener, OnRefreshListener, DelCallback {
	private Context context;
	private PullToRefreshListView listView;
	private PersonalSystemMsgAdapter adapter;
	private int page = 1;
	private boolean isLastPage = false;
	private LoadingView loadingview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_personal_system_msg);
		context = this;
		initViews();
		loadData(page);
	}
	
	public static void start(Activity curAct) {
		Intent intent = new Intent(curAct, PersonalSystemMsgAct.class);
		ViewUtils.startActivity(intent, curAct);
	}
	
	private void initViews(){
		loadingview = (LoadingView) findViewById(R.id.view_loading);
		findViewById(R.id.btn_common_back).setOnClickListener(this);
		((TextView) findViewById(R.id.txt_common_title)).setText("系统消息");
		listView = (PullToRefreshListView) findViewById(R.id.list_view);
		listView.setIntercept(true);
		adapter = new PersonalSystemMsgAdapter(context, this);
		listView.setAdapter(adapter);
		ViewUtils.setRefreshText(listView);
		listView.setOnRefreshListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				SystemMessage systemMessage = (SystemMessage) adapter.getItem(position - 1);
				if(systemMessage.getUrl() != null){
					H5DetailAct.start(PersonalSystemMsgAct.this, systemMessage);
				} else {
					Intent intent = new Intent(PersonalSystemMsgAct.this,
							PersonalSystemMsgDetailAct.class);
					intent.putExtra("msg", systemMessage);
					ViewUtils.startActivity(intent, PersonalSystemMsgAct.this);
				}
			}
		});
	}
	
	private void loadData(final int page) {
		Paging paging = new Paging(page, 20);
		final SystemMsgListRequest request = new SystemMsgListRequest(paging, Config.shopId);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
				AppLog.e("---msg list on ok---");
				if (request.getMsgList().isEmpty()) {
					if(page == 1){
						ViewUtils.setGone(listView);
						loadingview.onDataEmpty("暂无系统消息!");
					} else {
						isLastPage = true;
						listView.onRefreshComplete();
					}
				} else {
					if (page == 1) {
						adapter.clear();
						adapter.addItem2Head(request.getMsgList(), true);
					} else {
						adapter.addItem(request.getMsgList(), true);
					}
					loadingview.onLoadingComplete();
					listView.onRefreshComplete();
				}
			}

			@Override
			public void onFail(int code) {
				AppLog.e("---msg list on fail---");
				ViewUtils.setGone(listView);
				loadingview.onLoadingFail();
				listView.onRefreshComplete();
			}
		});
		loadingview.setOnAgainListener(new OnAgainListener() {
			
			@Override
			public void onLoading() {
				loadingview.startLoading();
				request.start(this);
			}
		});
		loadingview.startLoading();
		request.start(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_common_back:
			finish();
			break;
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
			// 加载下拉数据
			page = 1;
			loadData(page);
		} else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
			// 加载上拉数据
			if (!isLastPage) {
				page ++;
				loadData(page);
			} else {
				mMainHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						listView.onRefreshComplete();
					}
				}, 1000);
				ViewUtils.showToast("亲，没有更多数据");
			}
		}
	}

	@Override
	public void onDelClicked(Object object) {
		// TODO Auto-generated method stub
		final SystemMsgRemoveRequest request = new SystemMsgRemoveRequest(((SystemMessage)object).getId());
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
				AppLog.e("---msg remove on ok---");
				loadData(page);
			}

			@Override
			public void onFail(int code) {
				AppLog.e("---msg remove on fail---");
			}
		});
		request.start(this);
	}
}
