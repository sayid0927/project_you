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
import com.zxly.o2o.model.GeogArea;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;

 public class AreaSelView extends PullView {
	private ListView listView1,listView2;
	private SpinnerAdater adapter1,adapter2;
	private View linLayout;
	private GeogArea selGA;
	public AreaSelView(Context context) {
		super(context);

	}

	public AreaSelView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public AreaSelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void init() {
		super.init();
		listView1 = (ListView) spinnerContent.findViewById(R.id.listview1);
		listView2 = (ListView) spinnerContent.findViewById(R.id.listview2);
		listView1.setDividerHeight(0);
		listView2.setDividerHeight(0);
		linLayout=spinnerContent.findViewById(R.id.lin_layout);
		ViewUtils.setText(txtView, "区域");
		adapter1 = new SpinnerAdater(getContext(),1);
		adapter2=new SpinnerAdater(getContext(),2);
		listView1.setAdapter(adapter1);
		listView2.setAdapter(adapter2);
		listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				GeogArea ag = (GeogArea) adapter1.getItem(position);
				selGA=ag;
				adapter1.notifyDataSetChanged();
				adapter2.clear();
				adapter2.addItem(ag.getVillageList(), true);
			}
		});
		
		listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selItem = adapter2.getItem(position);
				adapter2.notifyDataSetChanged();
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
		linLayout.startAnimation(upAnimation);
	}

	public void pull() {
		super.pull();
		linLayout.startAnimation(pullAnimation);

		
	}
	public void setData(List<GeogArea> list) {
		adapter1.clear();adapter2.clear();
		if (list != null && !list.isEmpty()) {
			selGA=list.get(0);
			adapter1.addItem(list);
			adapter2.addItem(selGA.getVillageList());
			
		}
	}

	@Override
	public int spinnerContentLayoutId() {
		return R.layout.view_area_sel;
	}
	class SpinnerAdater extends ObjectAdapter {
		private int type;
		public SpinnerAdater(Context _context,int type) {
			super(_context);
			this.type=type;

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
				if(type==1)
				{
                    convertView.setBackgroundColor(getResources().getColor(R.color.white));
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Object obj = getItem(position);
			if(type==2)
			{
				if (obj.equals(selItem)) {
					holder.itemName.setTextColor(getResources().getColor(R.color.red_dd2727));
					holder.sel.setVisibility(View.VISIBLE);
				} else {
					holder.itemName.setTextColor(getResources().getColor(R.color.gray_999999));
					holder.sel.setVisibility(View.INVISIBLE);
				}
			}else
			{
				holder.sel.setVisibility(View.GONE);
				
				if(selGA.equals(obj))
				{
					holder.itemName.setTextColor(getResources().getColor(R.color.red_dd2727));
					convertView.setBackgroundColor(getResources().getColor(R.color.tint_grey));
				}else
				{
					holder.itemName.setTextColor(getResources().getColor(R.color.gray_999999));
					convertView.setBackgroundColor(getResources().getColor(R.color.white));
				}
			}
			
			ViewUtils.setText(holder.itemName, obj.toString());
			return convertView;
		}

		@Override
		public int getLayoutId() {

			return R.layout.item_spinner;
		}

		class ViewHolder {
			TextView itemName;
			ImageView sel;
		}

	}

}
