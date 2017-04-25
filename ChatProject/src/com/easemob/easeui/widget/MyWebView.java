package com.easemob.easeui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 *     @author huangbin  @version 创建时间：2015-1-20 下午8:10:50    类说明: 
 */
public class MyWebView extends WebView {
	private boolean isIntercept;

	public MyWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

//	public void setIsIntercept(boolean isIntercept){
//		this.isIntercept=isIntercept;
//	}
//
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		return isIntercept;
//	}
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		// TODO Auto-generated method stub
//		return isIntercept;
//	}

}
