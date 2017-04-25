package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.model.AddressDistrict;
import com.zxly.o2o.model.AddressVillage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;

/**
 * @author fengrongjian 2015-3-17
 * @description 二手区域选择列表适配器
 */
public class UsedRegionAdapter extends ObjectAdapter {

	private CallBack callBack = null;
	private AddressDistrict district = null;
	private AddressVillage village = null;
	private int mode;

	public UsedRegionAdapter(Context context, CallBack callBack) {
		super(context);
		this.callBack = callBack;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = inflateConvertView();
			vh = new ViewHolder();
			vh.head = (ImageView) convertView.findViewById(R.id.head);
			vh.name = (TextView) convertView
					.findViewById(R.id.used_region_name);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
//		if (mode == UsedRegionDialog.AREA_SHOW) {
		if(getItem(position) instanceof AddressDistrict){
			final AddressDistrict district = (AddressDistrict) getItem(position);
			vh.name.setText(district.getDistrictName());
			vh.head.setVisibility(View.VISIBLE);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					UsedRegionAdapter.this.district = district;
					callBack.onCall();
				}
			});
		} else {
			final AddressVillage village = (AddressVillage) getItem(position);
			vh.name.setText(village.getName());
			vh.head.setVisibility(View.GONE);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					UsedRegionAdapter.this.village = village;
					callBack.onCall();
				}
			});

		}
		return convertView;
	}

	private class ViewHolder {
		ImageView head;
		TextView name;
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_used_region;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public int getMode(){
		return this.mode;
	}

	public AddressDistrict getDistrict() {
		return district;
	}

	public void setDistrict(AddressDistrict district) {
		this.district = district;
	}

	public AddressVillage getVillage() {
		return village;
	}

	public void setVillage(AddressVillage village) {
		this.village = village;
	}

}
