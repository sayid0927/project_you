/**
* Copyright(C)2012-2013 深圳市壹捌无限科技有限公司版权所有
* 创 建 人:	Gofeel
* 修 改 人:
* 创 建日期:	2013-7-26
* 描	   述:	防止viewpager和listview同时出发
* 版 本 号:	1.0
*/ 
package com.zxly.o2o.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 防止listview和gallery事件冲突
 *
 */
public class FixedListView extends ListView {
	
	public FixedListView(Context context) {
		super(context);
	}
	public FixedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFadingEdgeLength(0);
	}
	
	public FixedListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
}
