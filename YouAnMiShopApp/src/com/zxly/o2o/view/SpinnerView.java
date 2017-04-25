package com.zxly.o2o.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 *     @author dsnx  @version 创建时间：2015-1-9 上午10:58:59    类说明: 
 */
public class SpinnerView extends PullView {



	private ListView listView;
	private SpinnerAdater adapter;

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
		adapter = new SpinnerAdater(getContext());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {
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

	public void setDefValue(Object defValue)
	{
		if (defValue != null) {
			if(!adapter.getContent().contains(defValue))
			{
				adapter.addItem(defValue);
			}

			Object obj = adapter.getItem(0);
			setText(obj.toString());
			selItem = obj;
		}

	}
	public void setData(List list) {
		if (list != null && !list.isEmpty()) {
			adapter.addItem(list);

		}
	}

	class SpinnerAdater extends ObjectAdapter {

		public SpinnerAdater(Context _context) {
			super(_context);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflateConvertView();
				holder.itemName = (TextView) convertView
						.findViewById(R.id.txt_name);
				holder.sel = (ImageView) convertView.findViewById(R.id.img_sel);
				holder.lineBottom = convertView.findViewById(R.id.line_bottom);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Object obj = getItem(position);
			if (obj.equals(selItem)) {
				holder.itemName.setTextColor(getResources().getColor(R.color.red_dd2727));
				convertView.setBackgroundColor(getResources().getColor(R.color.tint_grey));
			} else {
				holder.itemName.setTextColor(getResources().getColor(R.color.gray_999999));
				convertView.setBackgroundColor(getResources().getColor(R.color.white));
			}
			ViewUtils.setText(holder.itemName, obj.toString());
			if (position == getCount() - 1) {
				ViewUtils.setVisible(holder.lineBottom);
			} else {
				ViewUtils.setInvisible(holder.lineBottom);
			}
			return convertView;
		}

		@Override
		public int getLayoutId() {

			return R.layout.item_spinner;
		}

		class ViewHolder {
			TextView itemName;
			ImageView sel;
			View lineBottom;
		}

	}



	@Override
	public int spinnerContentLayoutId() {
		return R.layout.view_spinner;
	}

}

