package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

/**
 *     @author dsnx  @version 创建时间：2015-1-14 上午10:11:05    类说明: 
 */
public class DeliveryModeAdapter extends ObjectAdapter {

	private int parentWidth;


	public DeliveryModeAdapter(Context _context) {
		super(_context);
	}

	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflateConvertView();
			holder = new ViewHolder();
			holder.itemName = (TextView) convertView
					.findViewById(R.id.item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
        ViewUtils.setText(holder.itemName, getItem(position));
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_delivery_mode;
	}

	

	class ViewHolder {
		TextView itemName;
	}

}
