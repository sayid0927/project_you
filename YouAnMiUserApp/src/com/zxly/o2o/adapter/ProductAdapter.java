package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-23
 * @since YIBA-O2O
 */
public class ProductAdapter extends ObjectAdapter {

	public ProductAdapter(Context _context) {
		super(_context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflateConvertView();
			holder = new ViewHolder();
			holder.itemIcon = (NetworkImageView) convertView
					.findViewById(R.id.img_item);
			holder.txtPrice = (TextView) convertView
					.findViewById(R.id.txt_price);
			holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
			holder.origPrice = (TextView) convertView
					.findViewById(R.id.txt_low_price);
			holder.txtActType = (TextView) convertView
					.findViewById(R.id.txt_act_type);
            holder.comboFlag=convertView.findViewById(R.id.combo_flag);
			holder.lineBottom = convertView.findViewById(R.id.line_bottom);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NewProduct product = (NewProduct) getItem(position);
		holder.itemIcon.setDefaultImageResId(R.drawable.product_def);
        if(product.getIsPakage()==1)
        {
            ViewUtils.setVisible(holder.comboFlag);
        }else
        {
            ViewUtils.setGone(holder.comboFlag);
        }
		holder.itemIcon.setImageUrl(product.getHeadUrl(),
				AppController.imageLoader);
		float curPrice = product.getPrice()-product.getPreference();
		ViewUtils.setText(holder.txtName, product.getName());
		if(product.getPreference()>0)
		{
			ViewUtils.setVisible(holder.origPrice);
			ViewUtils.strikeThruText(holder.origPrice,  product.getPrice());
			ViewUtils.setTextPrice(holder.txtPrice, curPrice);
		}else
		{
			ViewUtils.setGone(holder.origPrice);
			ViewUtils.setTextPrice(holder.txtPrice,  curPrice);
		}
		switch (product.getTypeCode()) {
		case 1:
			ViewUtils.setVisible(holder.txtActType);
		//	holder.txtActType.setBackgroundResource(R.drawable.qianggou);
			break;
		case 2:
			//holder.txtActType.setBackgroundResource(R.drawable.youhui);
			ViewUtils.setVisible(holder.txtActType);
			break;
		default:
			ViewUtils.setGone(holder.txtActType);
			break;
		}

		if (position == getCount() - 1) {
			ViewUtils.setVisible(holder.lineBottom);
		} else {
			ViewUtils.setInvisible(holder.lineBottom);
		}
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_products;
	}

	class ViewHolder {
		NetworkImageView itemIcon;
		TextView txtPrice, origPrice, txtName, txtActType;
		View lineBottom,comboFlag;
	}
}
