package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.RenewScope;
import com.zxly.o2o.o2o_user.R;

/**
 * @author fengrongjian 2015-3-18
 * @description 保修范围列表适配器
 */
public class RenewScopeAdapter extends ObjectAdapter {

	public RenewScopeAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = inflateConvertView();
			vh = new ViewHolder();
			vh.typeName = (TextView) convertView
					.findViewById(R.id.renew_type_name);
			vh.paramName = (TextView) convertView
					.findViewById(R.id.renew_param_name);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final RenewScope scope = (RenewScope) getItem(position);
		vh.typeName.setText(scope.getTypeName() + ":");
		vh.paramName.setText(scope.getParamName());
		return convertView;
	}

	private class ViewHolder {
		TextView typeName;
		TextView paramName;
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_renew_scope;
	}

}
