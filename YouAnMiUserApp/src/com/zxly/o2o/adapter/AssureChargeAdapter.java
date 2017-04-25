package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.model.AssureCharge;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;

/**
 * @author fengrongjian 2015-3-16
 * @description 二手担保套餐收费列表适配器
 */
public class AssureChargeAdapter extends ObjectAdapter {
	
	private CallBack callBack = null;
	private AssureCharge mCharge = null;

	public AssureChargeAdapter(Context context, CallBack callBack) {
		super(context);
		this.callBack = callBack;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if (convertView == null) {
			convertView = inflateConvertView();
			vh = new ViewHolder();
			vh.head = (ImageView) convertView.findViewById(R.id.head);
			vh.deadlineName = (TextView) convertView.findViewById(R.id.assure_charge_deadline);
			vh.assureRate = (TextView) convertView.findViewById(R.id.assure_charge_rate1);
			vh.chargeRate = (TextView) convertView.findViewById(R.id.assure_charge_rate2);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final AssureCharge charge = (AssureCharge) getItem(position);
		vh.deadlineName.setText(charge.getDeadlineName());
		vh.assureRate.setText(charge.getAssureRate() + "%");
		vh.chargeRate.setText(charge.getChargeRate() + "%");
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				vh.head.setImageDrawable(context.getResources().getDrawable(R.drawable.checkbox_press));
				mCharge = charge;
				callBack.onCall();
			}
		});
		return convertView;
	}
	
	private class ViewHolder {
		ImageView head;
		TextView deadlineName;
		TextView assureRate;
		TextView chargeRate;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.item_assure_charge;
	}
	
	public Long getAssureCharge() {
		// TODO Auto-generated method stub
		return mCharge.getId();
	}
	
}
