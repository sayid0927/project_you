package com.zxly.o2o.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.RenewChargeAdapter;
import com.zxly.o2o.adapter.RenewScopeAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.RenewDeadline;
import com.zxly.o2o.model.UserMaintain;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.RenewAddRequest;
import com.zxly.o2o.request.RenewInitRequest;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-3-18
 * @description 续保
 */
public class RenewAddAct extends BasicAct implements View.OnClickListener {
	private ListView chargeListView = null;
	private ListView scopeListView = null;
	private RenewChargeAdapter chargeAdapter;
	private RenewScopeAdapter scopeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_renew_add);
		final UserMaintain maintain = (UserMaintain) getIntent()
				.getSerializableExtra("maintain");
		findViewById(R.id.tag_title_btn_back).setOnClickListener(this);
		((TextView)findViewById(R.id.tag_title_title_name)).setText("续保");
		chargeListView = (ListView) findViewById(R.id.renew_charge_list);
		chargeAdapter = new RenewChargeAdapter(this, new CallBack() {

			@Override
			public void onCall() {
				// TODO Auto-generated method stub
				RenewDeadline deadline = chargeAdapter.getSelectedDeadline();
				addRenew(maintain.getId(), deadline.getId(), deadline.getTypeName());
			}
		});
		chargeListView.setAdapter(chargeAdapter);
		scopeListView = (ListView) findViewById(R.id.renew_scope_list);
		scopeAdapter = new RenewScopeAdapter(this);
		scopeListView.setAdapter(scopeAdapter);
		initRenew(maintain.getPrice());
	}

	private void initRenew(Float price) {
		final RenewInitRequest request = new RenewInitRequest(Config.shopId,
				price);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
				AppLog.e("---renew init on ok---");
				chargeAdapter.clear();
				chargeAdapter.addItem(request.getDeadlineList(), true);
				scopeAdapter.clear();
				scopeAdapter.addItem(request.getScopeList(), true);
			}

			@Override
			public void onFail(int code) {
				AppLog.e("---renew init on fail---");
			}
		});
		request.start(this);
	}
	
	private void addRenew(Long maintainId, Long deadlineId, final String month) {
		final RenewAddRequest request = new RenewAddRequest(Config.shopId,
				maintainId, deadlineId);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
				AppLog.e("---renew add on ok---");
				Intent intent = new Intent(RenewAddAct.this,
						RenewSuccessAct.class);
				intent.putExtra("maintainNo", getIntent().getStringExtra("maintainNo"));
				intent.putExtra("month", month);
				ViewUtils.startActivity(intent, RenewAddAct.this);
			}

			@Override
			public void onFail(int code) {
				AppLog.e("---renew add on fail---");
				if(code != 20107)
				ViewUtils.showToast("新增续保失败");
			}
		});
		request.start(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tag_title_btn_back:
			finish();
			break;
		}
	}

}
