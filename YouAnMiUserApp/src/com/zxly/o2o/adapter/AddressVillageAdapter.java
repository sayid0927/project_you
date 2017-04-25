package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.AddressVillage;
import com.zxly.o2o.o2o_user.R;

public class AddressVillageAdapter extends ObjectAdapter {

	public AddressVillageAdapter(Context _context) {
		super(_context);
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
		AddressVillage village = (AddressVillage) getItem(position);
		holder.name.setText(village.getName());
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_area;
	}

	class AddressItemHolder {
		TextView name;
	}

}
