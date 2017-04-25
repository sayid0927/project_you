package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.model.AssureScope;
import com.zxly.o2o.o2o_user.R;

import java.util.HashMap;

/**
 * @author fengrongjian 2015-3-16
 * @description 二手担保套餐列表适配器
 */
public class AssureComboAdapter extends ObjectAdapter {
	
	private OnAssureItemClickListener listener = null;
	private HashMap<Integer, Boolean> selectMap = new HashMap<Integer, Boolean>();

	public AssureComboAdapter(Context context, OnAssureItemClickListener listener) {
		super(context);
		this.listener = listener;
	}
	
	public void initMap(){
		if(selectMap != null){
			for(int i=0; i<getCount(); i++){
				selectMap.put(i, false);
			}
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if (convertView == null) {
			convertView = inflateConvertView();
			vh = new ViewHolder();
			vh.head = (ImageView) convertView.findViewById(R.id.head);
			vh.comboName = (TextView) convertView.findViewById(R.id.assure_combo);
			vh.scopes = (TextView) convertView.findViewById(R.id.assure_scopes);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final AssureScope scope = (AssureScope) getItem(position);
		vh.head.setBackgroundResource(selectMap.get(position)? R.drawable.checkbox_assure_pressed: R.drawable.checkbox_assure_normal);
		vh.comboName.setText(scope.getName());
		vh.comboName.setBackgroundColor(context.getResources().getColor(getBgColor(position)));
		vh.scopes.setText(scope.getScopeNames());
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initMap();
				selectMap.put(position, true);
				notifyDataSetChanged();
				listener.onClicked(scope);
			}
		});
		return convertView;
	}
	
	private int getBgColor(int position){
		switch (position % 3) {
		case 0:
			return R.color.assure_combo_one;
		case 1:
			return R.color.assure_combo_two;
		case 2:
			return R.color.assure_combo_three;
		}
		return 0;
	}

	private class ViewHolder {
		ImageView head;
		TextView comboName;
		TextView scopes;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.item_assure_combo;
	}
	
	public interface OnAssureItemClickListener {
		void onClicked(AssureScope assureScope);
	}
	
}
