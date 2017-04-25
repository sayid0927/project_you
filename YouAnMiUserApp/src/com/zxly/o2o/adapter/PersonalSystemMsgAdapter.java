package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.SystemMessage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.StringUtil;

/**
 * @author fengrongjian 2015-1-23
 * @description 系统消息列表适配器
 */
public class PersonalSystemMsgAdapter extends ObjectAdapter {
	private DelCallback delCallback;

	public PersonalSystemMsgAdapter(Context context, DelCallback delCallback) {
		super(context);
		this.delCallback = delCallback;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflateConvertView();
			holder = new ViewHolder();
			holder.labelType = (TextView) convertView
					.findViewById(R.id.label_type);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.details = (TextView) convertView.findViewById(R.id.details);
			holder.btnDel = convertView.findViewById(R.id.btn_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final SystemMessage msg = (SystemMessage) getItem(position);
		holder.labelType.setText(msg.getTypeName());
		holder.time.setText(StringUtil.getShortTime(msg.getCreateTime()));
		holder.title.setText(msg.getTitle());
		holder.details.setText(msg.getContent());
		if(msg.getStatus() == 2){
			holder.title.setTextColor(context.getResources().getColor(R.color.light_grey));
		} else {
			holder.title.setTextColor(context.getResources().getColor(android.R.color.black));
		}
		holder.btnDel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				delCallback.onDelClicked(msg);
			}
		});
		return convertView;
	}

	private class ViewHolder {
		TextView labelType;
		TextView time;
		TextView title;
		TextView details;
		View btnDel;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.item_system_msg;
	}
	
	public interface DelCallback{
		void onDelClicked(Object object);
	}
}
