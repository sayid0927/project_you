package com.zxly.o2o.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zxly.o2o.adapter.AddressVillageAdapter;
import com.zxly.o2o.model.AddressVillage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.AddressVillageRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * @author fengrongjian 2015-3-6
 * @description 收货地址街道村落选择界面
 */
public class SettingAddressVillageAct extends BasicAct implements
		View.OnClickListener {
	private Context context;
	private ListView mListView = null;
	private AddressVillageAdapter adapter = null;
	private String source = null;
	private LoadingView loadingview = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_personal_address_area_new);
		context = this;
		source = getIntent().getStringExtra("source");
		findViewById(R.id.btn_back).setOnClickListener(this);
		ViewUtils.setText(findViewById(R.id.txt_title), "街道选择");
		loadingview = (LoadingView) findViewById(R.id.view_loading);
		findViewById(R.id.prv_name).setVisibility(View.GONE);
		findViewById(R.id.city_name).setVisibility(View.GONE);
		mListView = (ListView) findViewById(R.id.list_view);
		adapter = new AddressVillageAdapter(context);
		if ("edit".equals(source)) {
		} else {
		}
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent();
				intent.putExtra("village",
						(AddressVillage) adapter.getItem(position));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		getVillages();
	}

	private void getVillages() {
		Integer cityId = null;
		Integer areaId = null;
		if (getIntent().getStringExtra("cityId") != null) {
			cityId = Integer.parseInt(getIntent().getStringExtra("cityId"));
		}
		if (getIntent().getStringExtra("areaId") != null) {
			areaId = Integer.parseInt(getIntent().getStringExtra("areaId"));
		}
		final AddressVillageRequest request = new AddressVillageRequest(cityId,
				areaId);
		request.setOnResponseStateListener(new ResponseStateListener() {

			@Override
			public void onOK() {
				if (request.getVillageList() == null
						|| request.getVillageList().size() == 0) {
					ViewUtils.setGone(mListView);
					loadingview.onDataEmpty("没有找到对应街道");
				} else {
					ViewUtils.setVisible(mListView);
					adapter.clear();
					adapter.addItem(request.getVillageList(), true);
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
