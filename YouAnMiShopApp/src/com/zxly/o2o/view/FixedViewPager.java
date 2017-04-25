/**
 * Copyright(C)2012-2013 深圳市掌星立意科技有限公司版权所有
 * 创 建 人:	Gofeel
 * 修 改 人:
 * 创 建日期:	2013-7-26
 * 描	   述:   	拦截点击事件
 * 版 本 号:	1.0
 */
package com.zxly.o2o.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 拦截点击事件，滑动方向判断
 */
public class FixedViewPager extends ViewPager {
	private ViewPagerCallback mViewPagerCallback = null;
	private boolean mWillIntercept = true;
	private boolean mIsScrolling = false;
	private boolean mIsCanScroll = true;

	public FixedViewPager(Context context) {
		super(context);
		init();
	}

	public FixedViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setOnPageChangeListener(listener);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (mWillIntercept) {
			return super.onInterceptTouchEvent(arg0);
		} else {
			return false;
		}
	}

	/**
	 * 设置ViewPager是否拦截点击事件
	 */
	public void setTouchIntercept(boolean value) {
		mWillIntercept = value;
	}

	/**
	 * 设置ViewPager是否可滑动
	 */
	public void setScanScroll(boolean mIsCanScroll) {
		this.mIsCanScroll = mIsCanScroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		if (mIsCanScroll) {
			super.scrollTo(x, y);
		}
	}

	/**
	 * listener ,to get move direction .
	 */
	public OnPageChangeListener listener = new OnPageChangeListener() {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (mIsScrolling) {
				if (mViewPagerCallback != null && arg2 != 0) {
					mViewPagerCallback.onPageScrolled(arg0, arg1, arg2);
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 == 1) {
				mIsScrolling = true;
			} else {
				mIsScrolling = false;
			}
		}

		@Override
		public void onPageSelected(int arg0) {
			if (mViewPagerCallback != null) {
				mViewPagerCallback.onPageSelected(arg0);
			}
		}
	};

	/**
	 * 滑动状态改变回调
	 */
	public interface ViewPagerCallback {
		public void onPageScrolled(int arg0, float arg1, int arg2);
		public void onPageSelected(int arg0);
	}

	/**
	 * 设置接口
	 * 
	 * @param callback
	 */
	public void setViewPagerCallback(ViewPagerCallback callback) {
		mViewPagerCallback = callback;
	}

}
