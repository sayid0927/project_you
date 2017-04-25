/*
 * 文件名：ProductAppearanceDialog.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ProductAppearanceDialog.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-19
 * 修改内容：新增
 */
package com.zxly.o2o.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.adapter.UsedRegionAdapter;
import com.zxly.o2o.model.DataDictionary;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.ParameCallBackById;

import java.util.List;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-3-19
 * @since      YIBA-O2O
 */
public class ProductAppearanceDialog extends BaseDialog implements OnClickListener{
	private ParameCallBackById callBack;
	private ListView appearanceListView;
	private RelativeLayout layout;
	private UsedRegionAdapter regionAdapter;
	ProductAppearanceAdaper adapter;

	public ProductAppearanceDialog() {
		super();

	}

	@Override
	public int getLayoutId() {
		return R.layout.dialog_product_appearance;
	}

	@Override
	protected void initView() {
		appearanceListView = (ListView) findViewById(R.id.product_appearance_list);
		layout=(RelativeLayout) findViewById(R.id.layout_dialog_appearance);
		layout.setLayoutParams(new LinearLayout.LayoutParams(-1, DesityUtil.getScreenSizes(getContext())[1]/2+50));
	}

	
	public void show(ParameCallBackById callBack, List<DataDictionary>  deprs) {
		super.show();
		this.callBack = callBack;
		adapter=new ProductAppearanceAdaper(getContext());
		appearanceListView.setAdapter(adapter);
        adapter.addItem(deprs, true);
	}
	
	
	@Override
	protected boolean isLimitHeight() {
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			
			break;
		}

	}
	
	
	class ProductAppearanceAdaper extends ObjectAdapter{

		public ProductAppearanceAdaper(Context _context) {
			super(_context);
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null){
				convertView = inflateConvertView();
				holder=new ViewHolder();
				holder.appearanceName=(TextView) convertView.findViewById(R.id.appearance_name);
				holder.botLine= convertView.findViewById(R.id.view_bot_line);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			final DataDictionary item=(DataDictionary) getItem(position);
			holder.appearanceName.setText(item.getName());
			convertView.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View arg0) {
			        ProductAppearanceDialog.this.callBack.onCall(ProductAppearanceDialog.this.getLayoutId(), item);      		
					ProductAppearanceDialog.this.dismiss();
				}
			});
			
			if(position==getCount()-1){
				holder.botLine.setVisibility(View.GONE);
			}else{
				holder.botLine.setVisibility(View.VISIBLE);
			}
			
			return convertView;
		}

		@Override
		public int getLayoutId() {			
			return R.layout.dialog_product_appearance_list_item;
		}
		
		
		class ViewHolder {
			TextView appearanceName;
			View botLine;
		}
	}
	
}
