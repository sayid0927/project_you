package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.AssureComboAdapter;
import com.zxly.o2o.adapter.AssureComboAdapter.OnAssureItemClickListener;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.dialog.AssureComboDialog;
import com.zxly.o2o.model.AssureResult;
import com.zxly.o2o.model.AssureScope;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.AssureListRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-3-16
 * @description 二手担保套餐列表
 */
public class AssureComboAct extends BasicAct implements View.OnClickListener,
		OnAssureItemClickListener {
	private static ParameCallBackById callBack;
	private Button btnBack;
	private Context context;
	private ListView mListView;
	private AssureComboAdapter assureAdapter;
	private TextView textBlank;
	private int classId;
	private AssureComboDialog assureComboDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_assure_combo);
		context = this;
		initViews();
		initParams();
	}

	private void initParams() {
		classId = getIntent().getIntExtra("classId", -1);
		if (classId != -1) {
			loadData(Config.shopId, Long.parseLong(classId + ""));
		} else {
			ViewUtils.showToast("---classId error---");
		}
	}

	private void initViews() {
		textBlank = (TextView) findViewById(R.id.text_blank);
		textBlank.setText("暂无担保套餐!");
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.assure_list);
		assureAdapter = new AssureComboAdapter(context, this);
		mListView.setAdapter(assureAdapter);
	}
	
	private void loadData(Long id, Long classId) {
		final AssureListRequest request = new AssureListRequest(id, classId);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
				AppLog.e("---assure list on ok---");
				if (request.getAssureList().size() == 0) {
					mListView.setVisibility(View.GONE);
					findViewById(R.id.layout_blank).setVisibility(View.VISIBLE);
				} else {
					mListView.setVisibility(View.VISIBLE);
					findViewById(R.id.layout_blank).setVisibility(View.GONE);
					assureAdapter.clear();
					assureAdapter.addItem(request.getAssureList());
					assureAdapter.initMap();
					assureAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFail(int code) {
				AppLog.e("---assure list on fail---");
			}
		});
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
	
	public static void start(Activity curAct, int classId, ParameCallBackById _callBack) {
		callBack = _callBack;
		Intent intent = new Intent(curAct, AssureComboAct.class);
		intent.putExtra("classId", classId);
		ViewUtils.startActivity(intent, curAct);
	}

	@Override
	public void onClicked(final AssureScope assureScope) {
		// TODO Auto-generated method stub
		if (assureComboDialog == null) {
			assureComboDialog = new AssureComboDialog();
		}
		assureComboDialog.show(new ParameCallBack() {

			@Override
			public void onCall(Object object) {
				if (object != null) {
					AssureResult assureResult = new AssureResult();
					assureResult.setAssureTypeId(assureScope.getId());
					assureResult.setAssureChargeId((Long) object);
					assureResult.setAssureName(assureScope.getName());
					callBack.onCall(R.layout.win_assure_combo, assureResult);
					finish();
				} else {
					
				}

			}
		}, assureScope);

	}
}
