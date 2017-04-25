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

public class CommProductAdapter extends ObjectAdapter{
	private boolean isShowPcs;
	private boolean isShowProductInfo;
	public CommProductAdapter(Context _context,boolean isShowPcs)
	{
		super(_context);
		this.isShowPcs=isShowPcs;
	}
	public CommProductAdapter(Context _context)
	{
		super(_context);
		this.isShowPcs=true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder  holder;
		if(convertView==null)
		{
			convertView=inflateConvertView();
			holder=new ViewHolder();
			holder.txtProductName=(TextView) convertView.findViewById(R.id.item_name);
			holder.txtCount=(TextView) convertView.findViewById(R.id.item_sum);
			holder.txtSinglePrice=(TextView) convertView.findViewById(R.id.item_price);
			holder.textComboInfo=(TextView) convertView.findViewById(R.id.text_comboInfo);
			holder.icon=(NetworkImageView) convertView.findViewById(R.id.item_icon);
			holder.lineBottom=convertView.findViewById(R.id.line_bottom);
			convertView.setTag(holder);
		}else
		{
			holder=(ViewHolder) convertView.getTag();
		}
		NewProduct np=(NewProduct) getItem(position);
		ViewUtils.setText(holder.txtProductName, np.getName());
		if(isShowPcs)
		{
			ViewUtils.setText(holder.txtCount, "× "+np.getPcs());
		}

		if(isShowProductInfo) {
			holder.textComboInfo.setText(np.getRemark());
		}

		ViewUtils.setTextPrice(holder.txtSinglePrice, np.getCurPrice());
		holder.icon.setImageUrl(np.getHeadUrl(), AppController.imageLoader);
		if (position == getCount() - 1) {
            ViewUtils.setVisible(holder.lineBottom);

        } else {
            ViewUtils.setInvisible(holder.lineBottom);
        }
		return convertView;
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_refund_product;
	}
	
 	class ViewHolder{
 		View lineBottom;
 		TextView txtProductName, txtCount, txtSinglePrice,textComboInfo;
		NetworkImageView icon;
	}

	public void setIsShowProductInfo(boolean isShowProductInfo) {
		this.isShowProductInfo = isShowProductInfo;
	}
}
