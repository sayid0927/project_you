package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.AddressCity;
import com.zxly.o2o.model.AddressDistrict;
import com.zxly.o2o.model.AddressProvince;
import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.o2o_user.R;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends ObjectAdapter {
	private OnAddressItemClickListener listener = null;
//	private List<AddressCountry> areaList = null;
	private List<AddressProvince> prvList = null;
	private ArrayList<AddressCity> cityList = null;
	private ArrayList<AddressDistrict> districtList = null;
	
	private ArrayList<String> dataList = null;
	
	private int currentState = 0;
	

	public AddressAdapter(Context context, OnAddressItemClickListener listener) {
		super(context);
		this.listener = listener;
	}

	public void setProvinceList(List<AddressProvince> prvList) {
		this.prvList = prvList;
	}
	
	public void setDataList(ArrayList<String> dataList) {
		this.dataList = dataList;
	}
	
	public void setCurrentState(int currentState){
		this.currentState = currentState;
	}
	
	public int getCurrentState(){
		return this.currentState;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		AddressItemHolder holder;
		if (convertView == null) {
			convertView = inflateConvertView();
			holder = new AddressItemHolder();
			holder.name = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (AddressItemHolder) convertView.getTag();
		}
		
//		UserAddress address = (UserAddress) getItem(position);
		holder.name.setText((String)getItem(position));
		return convertView;
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.item_area;
	}

	class AddressItemHolder {
		TextView name;
	}

	public interface OnAddressItemClickListener {
		void onClicked(UserAddress address);
	}

}
