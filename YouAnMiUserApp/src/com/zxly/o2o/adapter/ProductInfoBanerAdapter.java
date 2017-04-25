package com.zxly.o2o.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ImageUtil;

/**
 *     @author dsnx  @version 创建时间：2015-1-13 上午10:40:43    类说明: 
 */
public class ProductInfoBanerAdapter extends ObjectAdapter {

	public ProductInfoBanerAdapter(Context _context) {
		super(_context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView v = null;
		if (convertView == null) {
			v = (ImageView) inflateConvertView();
		} else {
			v = (ImageView) convertView;
		}
		Bitmap bitmap = ImageUtil
				.LoadResourceBitmap(context, R.drawable.icon_default);
		ImageUtil.zoomBitmap(bitmap, Config.screenWidth);
		v.setImageDrawable(ImageUtil.zoomBitmap(bitmap, Config.screenWidth));
		return v;
	}

	@Override
	public int getLayoutId() {
		return R.layout.view_item_galley;
	}

}
