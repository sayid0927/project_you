package com.zxly.o2o.dialog;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.UsedRegionAdapter;
import com.zxly.o2o.model.AddressDistrict;
import com.zxly.o2o.model.AddressVillage;
import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.AddressVillageRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.AreaUtil;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;

/**
 * @author fengrongjian 2015-3-17
 * @description 二手选择区域对话框
 */
public class UsedRegionDialog extends BaseDialog implements OnClickListener {
	private ListView mListView;
	private UsedRegionAdapter regionAdapter;
	private TextView selectLable;
	private View back;
	private ParameCallBackById callBack;
	private AddressDistrict district = null;
	private Integer cityId = null;
	private ArrayList<AddressDistrict> districtList = null;
	public static final int AREA_SHOW = 1;
	public static final int VILLAGE_SHOW = 2;

	public UsedRegionDialog() {
		super();
		regionAdapter = new UsedRegionAdapter(this.context, new CallBack() {

			@Override
			public void onCall() {
				if (regionAdapter.getMode() == AREA_SHOW){
					back.setVisibility(View.VISIBLE);
					selectLable.setVisibility(View.GONE);
					district = regionAdapter.getDistrict();
					Integer districtId = Integer.parseInt(district.getDistrictId() + "");
					regionAdapter.setMode(VILLAGE_SHOW);
					getVillage(cityId, districtId);
				} else {
					dismiss();
					AddressVillage village = regionAdapter.getVillage();
					UserAddress userAddress = new UserAddress();
					userAddress.setAreaId(district != null ? Long.parseLong(district.getDistrictId()) : null);
					userAddress.setAreaName(district != null ? district.getDistrictName() : null);
					userAddress.setVillageId(village != null ? village.getId() : null);
					userAddress.setVillageName(village != null ? village.getName() : null);
					AppLog.e("---result---" + userAddress);
					UsedRegionDialog.this.callBack.onCall(getLayoutId(), userAddress);
				}
			}
		});
		mListView.setAdapter(regionAdapter);
	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_used_region;
	}

	@Override
	protected void initView() {
		mListView = (ListView) findViewById(R.id.used_region_list);
		mListView.setLayoutParams(new LinearLayout.LayoutParams(-1, DesityUtil.getScreenSizes(getContext())[1]/2));
		selectLable = (TextView) findViewById(R.id.select_region_label);
		back = (LinearLayout) findViewById(R.id.back);
		back.setOnClickListener(this);
	}

	public void show(ParameCallBackById callBack, Integer prvId, Integer cityId) {
		super.show();
		this.callBack = callBack;
		this.cityId = cityId;
		districtList = AreaUtil.getUsedRegion(prvId, cityId);
		if(districtList.size() == 0){//like dongguan
			back.setVisibility(View.GONE);
			selectLable.setVisibility(View.VISIBLE);
			regionAdapter.setMode(VILLAGE_SHOW);
			getVillage(cityId, null);
		} else {
			back.setVisibility(View.GONE);
			selectLable.setVisibility(View.VISIBLE);
			regionAdapter.setMode(AREA_SHOW);
			regionAdapter.clear();
			regionAdapter.addItem(districtList, true);
		}
		// regionAdapter.notifyDataSetChanged();
	}
	
	private void getVillage(Integer cityId, Integer areaId){
		final AddressVillageRequest request = new AddressVillageRequest(cityId, areaId);
		request.setOnResponseStateListener(new ResponseStateListener() {

			@Override
			public void onOK() {
				// TODO Auto-generated method stub
				AppLog.e("---village on ok---");
				if(request.getVillageList().size() == 0){
					ViewUtils.showToast("没有找到街道");
					dismiss();
				} else {
					regionAdapter.clear();
					regionAdapter.addItem(request.getVillageList(), true);
				}
			}

			@Override
			public void onFail(int code) {
				// TODO Auto-generated method stub
				AppLog.e("---village on fail---");
			}
		});
		request.start(this);
	}

	@Override
	protected boolean isLimitHeight() {
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			back.setVisibility(View.GONE);
			selectLable.setVisibility(View.VISIBLE);
			regionAdapter.setMode(AREA_SHOW);
			regionAdapter.clear();
			regionAdapter.addItem(districtList, true);
			break;
		}

	}

}
