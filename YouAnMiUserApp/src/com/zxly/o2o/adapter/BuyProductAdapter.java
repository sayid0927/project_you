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

import java.util.List;

/**
 *     @author dsnx  @version 创建时间：2015-1-20 下午4:46:50    类说明: 
 */
public class BuyProductAdapter extends ObjectAdapter {

	public BuyProductAdapter(Context _context) {
		super(_context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflateConvertView();
			holder.addLayout = convertView.findViewById(R.id.add_layout);
			holder.productName = (TextView) convertView.findViewById(R.id.item_name);
			holder.price = (TextView) convertView.findViewById(R.id.item_price);
			holder.count = (TextView) convertView.findViewById(R.id.item_sum);
			holder.icon=(NetworkImageView) convertView.findViewById(R.id.item_icon);
            holder.topSpace=convertView.findViewById(R.id.top_space);
            holder.bottomSpace=convertView.findViewById(R.id.bottom_space);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		NewProduct product=(NewProduct) getItem(position);
		
		ViewUtils.setText(holder.productName,product.getName());
	    if(product.getSelSku()!=null)
	    {
			ViewUtils.setTextPrice(holder.price,(product.getSelSku().getPrice()-product.getPreference()));

	    }else
	    {
	    	ViewUtils.setTextPrice(holder.price,product.getSubPrice());
	    }
		ViewUtils.setText(holder.count,"x"+product.getPcs());
		holder.icon.setImageUrl(product.getHeadUrl(),
				AppController.imageLoader);


        if (position == getCount() - 1) {
            ViewUtils.setGone(holder.addLayout);
            ViewUtils.setVisible(holder.bottomSpace);
            if(position==0)
            {
                ViewUtils.setVisible(holder.topSpace);
            }else
            {
                ViewUtils.setGone(holder.topSpace);
            }

        } else {
            ViewUtils.setVisible(holder.addLayout);
            if(position==0)
            {
                ViewUtils.setVisible(holder.topSpace);

            }else
            {
                ViewUtils.setGone(holder.topSpace);
            }
            ViewUtils.setGone(holder.bottomSpace);

        }

		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_product_order;
	}

	public void refreshListItem(List<NewProduct> productList) {
		if (productList != null && productList.size() > 0) {
			clear();
			addItem(productList);
		}
		notifyDataSetChanged();
	}

	class ViewHolder {
		View addLayout,topSpace,bottomSpace;
		TextView productName;
		TextView price;
		TextView count;
		NetworkImageView icon;
	}

}
