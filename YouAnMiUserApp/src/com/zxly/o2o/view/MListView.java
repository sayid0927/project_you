package com.zxly.o2o.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 *     @author dsnx  @version 创建时间：2015-1-14 上午10:44:40    类说明: 
 */

public class MListView extends ListView {
	
	private boolean isGetTouchEvent=true;
	
	public MListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	public void setGetTouchEvent(boolean isGetTouchEvent){
		this.isGetTouchEvent=isGetTouchEvent;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!isGetTouchEvent){		
			super.onTouchEvent(event);
			return false;		
		}else{
			return super.onTouchEvent(event);
		}       
	}

}
