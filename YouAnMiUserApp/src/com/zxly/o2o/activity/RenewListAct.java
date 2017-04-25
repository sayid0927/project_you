package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.RenewAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.dialog.VerifyDialog;
import com.zxly.o2o.model.UserMaintain;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.RenewListRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.MyDeviceAdmin;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * @author fengrongjian 2015-3-17
 * @description 保修单列表
 */
public class RenewListAct extends BasicAct implements View.OnClickListener {
	private Context context;
	private ListView mListView;
	private RenewAdapter renewAdapter;
	private LoadingView loadingview = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_renew);
		context = this;
		PreferUtil.getInstance().setRenewGuideHasOpen();
		findViewById(R.id.btn_back).setOnClickListener(this);
		((TextView)findViewById(R.id.txt_title)).setText("保修单");
		loadingview = (LoadingView) findViewById(R.id.view_loading);
		mListView = (ListView) findViewById(R.id.renew_list);
		renewAdapter = new RenewAdapter(context);
		mListView.setAdapter(renewAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(RenewListAct.this,
						RenewDetailAct.class);
				intent.putExtra("guarantee",
						(UserMaintain) renewAdapter.getItem(position));
				ViewUtils.startActivity(intent, RenewListAct.this);
			}
		});
		if(Account.user != null){
			loadData();
		}
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName cm = new ComponentName(this, MyDeviceAdmin.class);
		if (!devicePolicyManager.isAdminActive(cm))
		{
			VerifyDialog verDialog = new VerifyDialog();
			ViewUtils.setGone(verDialog.getCancleButton());
			verDialog.show(new CallBack() {
				@Override
				public void onCall() {
					ViewUtils.startActivityDeviceManager(RenewListAct.this);
				}
			}, getResources().getString(R.string.device_manager_activate_msg));

		}
	}
	
	public static void start(Activity curAct) {
		Intent intent = new Intent(curAct, RenewListAct.class);
		ViewUtils.startActivity(intent, curAct);
	}

	private void loadData() {
		final RenewListRequest request = new RenewListRequest(
				Account.user.getUserName(), Config.shopId);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
				if (request.getRenewList() == null
						|| request.getRenewList().size() == 0) {
					ViewUtils.setGone(mListView);
					loadingview.onDataEmpty("暂无保修单!");
				} else {
					renewAdapter.clear();
					renewAdapter.addItem(request.getRenewList(), true);
					loadingview.onLoadingComplete();
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
				request.start(this);
			}
		});
		loadingview.startLoading();
		request.start(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}

}
