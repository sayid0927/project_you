package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.ProductParam;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

/**
 *     @author dsnx  @version 创建时间：2015-1-14 下午4:07:40    类说明: 
 */
public class NewProductParamAdapter extends ObjectAdapter {

	public NewProductParamAdapter(Context _context) {
		super(_context);
	}

	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflateConvertView();
			holder = new ViewHolder();
			holder.paramName = (TextView) convertView
					.findViewById(R.id.txt_param_name);
			holder.paramValue = (TextView) convertView
					.findViewById(R.id.txt_param_value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ProductParam param=(ProductParam) getItem(position);
		ViewUtils.setText(holder.paramName,param.getDisplayName());
		ViewUtils.setText(holder.paramValue, param.getParamValue());
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_product_param;
	}

	class ViewHolder {
		TextView paramName;
		TextView paramValue;
	}

	

}