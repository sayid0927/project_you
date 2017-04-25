package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.UsedProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public class UsedProductAdapter extends ObjectAdapter {

	public UsedProductAdapter(Context _context) {
		super(_context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null)
		{
			convertView = inflateConvertView();
			holder = new ViewHolder();
			holder.itemIcon=(NetworkImageView) convertView.findViewById(R.id.img_item);
			holder.txtAddress=(TextView) convertView.findViewById(R.id.txt_address);
			holder.txtName=(TextView) convertView.findViewById(R.id.txt_name);
			holder.txtTime=(TextView) convertView.findViewById(R.id.txt_time);
			holder.txtPrice=(TextView) convertView.findViewById(R.id.txt_price);
			holder.productType= (TextView) convertView.findViewById(R.id.txt_product_type);
			holder.lineBottom=convertView.findViewById(R.id.line_bottom);
			convertView.setTag(holder);
		}else
		{
			
			holder=(ViewHolder) convertView.getTag();
		}
	
		UsedProduct product=(UsedProduct) getItem(position);
		holder.itemIcon.setDefaultImageResId(R.drawable.used_product_def);
		holder.itemIcon.setImageUrl(product.getHeadUrl(),
				AppController.imageLoader);
		ViewUtils.setText(holder.txtName, product.getName());
		ViewUtils.setTextPrice(holder.txtPrice, product.getPrice());
		ViewUtils.setText(holder.txtTime, StringUtil.getShortTime(product.getPutawayTime())+"发布");
		ViewUtils.setText(holder.txtAddress, product.getContact().getAreaName()+"-"+product.getContact().getVillageName());
		switch (product.getAssureParty()) {
		case 2:
		case 3:
			ViewUtils.setVisible(holder.productType);
			holder.productType.setText("门店担保");
			holder.productType.getBackground().setAlpha(73);
			break;
		default:
			ViewUtils.setGone(holder.productType);
			break;
		}
		if (position == getCount() - 1) {
			ViewUtils.setVisible(holder.lineBottom);
		} else {
			ViewUtils.setGone(holder.lineBottom);
		}
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_used_products;
	}
	class ViewHolder {
		NetworkImageView itemIcon;
		TextView txtPrice, txtAddress, txtName, txtTime,productType;
		View lineBottom;
	}

}
