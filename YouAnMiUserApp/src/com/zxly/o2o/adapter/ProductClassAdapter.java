/*
 * 文件名：ProductClassAdapter.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ProductClassAdapter.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-18
 * 修改内容：新增
 */
package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;

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
 * @version    YIBA-O2O 2015-3-18
 * @since      YIBA-O2O
 */
public class ProductClassAdapter extends ObjectAdapter {

	public ProductClassAdapter(Context _context) {
		super(_context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflateConvertView();
			holder.itemName = (TextView) convertView.findViewById(R.id.txt_name);
			holder.sel = (ImageView) convertView.findViewById(R.id.img_sel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Object obj = getItem(position);
//		if (obj.equals(selItem)) {
//			holder.sel.setVisibility(View.VISIBLE);
//		} else {
//			holder.sel.setVisibility(View.GONE);
//		}
//		ViewUtils.setText(holder.itemName, obj.toString());
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
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
