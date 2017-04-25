package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.RenewDeadline;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;

/**
 * @author fengrongjian 2015-3-18
 * @description 续保价格列表适配器
 */
public class RenewChargeAdapter extends ObjectAdapter {

	private CallBack callBack = null;
	protected RenewDeadline deadline;

	public RenewChargeAdapter(Context context, CallBack callBack) {
		super(context);
		this.callBack = callBack;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = inflateConvertView();
			vh = new ViewHolder();
			vh.name = (TextView) convertView.findViewById(R.id.renew_type_name);
			vh.price = (TextView) convertView.findViewById(R.id.renew_price);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final RenewDeadline deadline = (RenewDeadline) getItem(position);
		vh.name.setText(deadline.getTypeName() + "个月:");
		vh.price.setText("￥" + deadline.getPrice() + "元");
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RenewChargeAdapter.this.deadline = deadline;
				callBack.onCall();
			}
		});

		return convertView;
	}

	private class ViewHolder {
		TextView name;
		TextView price;
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_renew_charge;
	}

	public RenewDeadline getSelectedDeadline() {
		return this.deadline;
	}
}
