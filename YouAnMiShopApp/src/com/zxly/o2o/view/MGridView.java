package com.zxly.o2o.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 *     @author dsnx  @version 创建时间：2015-1-14 上午10:44:40    类说明: 
 */
public class MGridView extends GridView{

	public MGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}


}
