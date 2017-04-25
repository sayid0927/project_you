/**
 * Copyright(C)2012-2013 深圳市掌星立意科技有限公司版权所有
 * 创 建 人:    Gofeel
 * 修 改 人:
 * 创 建日期:    2013-7-22
 * 描    述:   顶部展示框适配器
 * 版 本 号:    1.0
 */
package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.Banners;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ParameCallBack;

import java.util.List;

/**
 * 顶部推荐广告位适配器
 */
public class RecommendAdapter extends BaseAdapter implements OnClickListener {
	private List<Banners> mDataSource;
	private LayoutInflater mInflater;
    private Context context;
	public RecommendAdapter(Context context, List<Banners> dataSource) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDataSource = dataSource;
		this.context=context;
	}

	public RecommendAdapter(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (mDataSource == null || mDataSource.size() == 0) {
			return 0;
		} else if (mDataSource.size() == 1) {
			return 1;
		}
		return mDataSource.size() * 10000;
	}

	@Override
	public Object getItem(int position) {
		int length = mDataSource.size();
		position = position % length;
		if (position < 0)
			position = position + length;
		return mDataSource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NetworkImageView v = null;
		if (convertView == null) {
			v = (NetworkImageView) mInflater.inflate(R.layout.view_item_galley,
					parent, false);
		} else {
			v = (NetworkImageView) convertView;
		}
		if (null == mDataSource)
			return v;

		
		final int length = mDataSource.size();
		if (length > 0) {
			position = position % length;
			if (position < 0)
				position = position + length;
		}
		if (position < 0)
			return v;

		final Banners promotion = mDataSource.get(position);
        v.setDefaultImageResId(R.drawable.icon_default);
		v.setImageUrl(promotion.getImageUrl(),
				AppController.imageLoader);
		v.setOnClickListener(this);
		v.setTag(promotion);
		return v;
	}

	@Override
	public void onClick(View v) {		
		final Banners banner=(Banners) v.getTag();
		ProductInfoAct.start((Activity) context, banner.getProductId(), banner.getBannerId(), new ParameCallBack() {

			@Override
			public void onCall(Object object) {
				mDataSource.remove(banner);
				notifyDataSetChanged();
			}
		});
		
	}
}
