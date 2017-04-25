package com.zxly.o2o.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.TouchImageViewAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.MyCirCleObject;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.Constants;

public abstract class MyCircleSwipeAdapter extends SwipeBaseAdapter {

	public MyCircleSwipeAdapter(Context _context) {
		super(_context);
		// TODO Auto-generated constructor stub
	}


	public void setItemAnim(MyCirCleObject mObject, View mView, int position) {
		if (!mObject.getIsShown()) {
			if (position >= Constants.PER_PAGE_SIZE - 1) {
				mView.startAnimation(AnimationUtils.loadAnimation(context,
						R.anim.listview_item_anim));
			}
			mObject.setIsShown(true);
		}
	}
	
	public void showPic(int position, String url, long time,
			boolean isUserPhone) {
		Intent intent = new Intent(context, TouchImageViewAct.class);

		intent.putExtra("file_path", url);
		if (time == 0) {
				intent.putExtra("file_is_local", true);
		} else {
			intent.putExtra("file_is_local", false);
		}

		context.startActivity(intent);
	}
	
	public  void setImage(NetworkImageView imageView, String url,
			int defaultDrawable, boolean isShowDefaul) {
		imageView.setErrorImageResId(defaultDrawable);
		imageView.setDefaultImageResId(defaultDrawable);
		if (url != null && url.contains("http://")) {
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageUrl(url, AppController.imageLoader);
		} else if (isShowDefaul) {
			imageView.setVisibility(View.VISIBLE);
		} else {
			imageView.setVisibility(View.GONE);
		}
	}

}
