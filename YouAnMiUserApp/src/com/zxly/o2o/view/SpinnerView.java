package com.zxly.o2o.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;

/**
 *     @author dsnx  @version 创建时间：2015-1-9 上午10:58:59    类说明: 
 */
 public class SpinnerView extends PullView {

	private ListView listView;
	private SpinnerAdapter adapter;
	
	public SpinnerView(Context context) {
		super(context);

	}

	public SpinnerView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public SpinnerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void init() {
		super.init();
		listView = (ListView) spinnerContent.findViewById(R.id.listview);
		listView.setDividerHeight(0);
		adapter = new SpinnerAdapter(getContext());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {
				if(position == 0){
					setTopSelect(true);
				} else {
					setTopSelect(false);
				}
				selItem = adapter.getItem(position);
				adapter.notifyDataSetChanged();
				setText(selItem.toString());
				if (onSelChangeListener != null) {
					onSelChangeListener.onSelChange();
				}
				up();
			}
		});
		
	
	}

	public void up() {
		super.up();
		listView.startAnimation(upAnimation);
	}

	public void pull() {
		super.pull();
		listView.startAnimation(pullAnimation);

		
	}

	public void clearData(){
		if(adapter!=null){
			adapter.clear();
			adapter.notifyDataSetChanged();
		}

	}

	public void setDefValue(Object defValue)
	{
		if (defValue != null) {
			if(!adapter.getContent().contains(defValue))
			{
//				adapter.addItem(defValue);
			}

//			Object obj = adapter.getItem(0);
//			setText(obj.toString());
//			selItem = obj;
			selItem = defValue;
			setText(defValue.toString());
		}
		
	}
	public void setData(List list) {
		if (list != null && !list.isEmpty()) {
//			ListUtil.deleteRepeat(list,adapter.getContent());
			adapter.clear();
			adapter.addItem(list);
			
		}
	}

	class SpinnerAdapter extends ObjectAdapter {

		public SpinnerAdapter(Context _context) {
			super(_context);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflateConvertView();
				holder.txtName = (TextView) convertView
						.findViewById(R.id.txt_name);
				holder.imgSelect = (ImageView) convertView.findViewById(R.id.img_sel);
				holder.lineBottom = convertView.findViewById(R.id.line_bottom);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Object obj = getItem(position);
			if (obj.equals(selItem) || obj.toString().equals(selItem.toString())) {
//				holder.txtName.setTextColor(getResources().getColor(R.color.orange_ff5f19));
				ViewUtils.setVisible(holder.imgSelect);
			} else {
//				holder.txtName.setTextColor(getResources().getColor(R.color.black));
				ViewUtils.setGone(holder.imgSelect);
			}
			ViewUtils.setText(holder.txtName, obj.toString());
			if (position == getCount() - 1) {
//				ViewUtils.setVisible(holder.lineBottom);
			} else {
//				ViewUtils.setInvisible(holder.lineBottom);
			}
			return convertView;
		}

		@Override
		public int getLayoutId() {

			return R.layout.item_spinner;
		}

		class ViewHolder {
			TextView txtName;
			ImageView imgSelect;
			View lineBottom;
		}

	}

	

	@Override
	public int spinnerContentLayoutId() {
		return R.layout.view_spinner;
	}

}
