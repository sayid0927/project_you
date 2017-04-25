package com.easemob.easeui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


/**
 *     @author huangbin  @version 创建时间：2015-2-3 下午2:20:05    类说明: 
 */
public class VolleyImageView extends NetworkImageView {
	private Bitmap mLocalBitmap;

	private boolean mShowLocal;

	public void setLocalImageBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			mShowLocal = true;
		}
		this.mLocalBitmap = bitmap;
		requestLayout();
	}

	@Override
	public void setImageUrl(String url, ImageLoader imageLoader) {
		mShowLocal = false;
		super.setImageUrl(url, imageLoader);
	}

	public VolleyImageView(Context context) {
		this(context, null);
	}

	public VolleyImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VolleyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

		super.onLayout(changed, left, top, right, bottom);
		if (mShowLocal) {
			setImageBitmap(mLocalBitmap);
		}
	}

}
