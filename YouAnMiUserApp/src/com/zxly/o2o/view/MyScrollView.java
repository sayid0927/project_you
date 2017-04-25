package com.zxly.o2o.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if(this.onSrollChangeListener!=null)
		{
			
				this.onSrollChangeListener.onSrollCange(scrollY);
		}
	}

	private OnSrollChangeListener onSrollChangeListener;

	public void setOnSrollChangeListener(
			OnSrollChangeListener onSrollChangeListener) {
		this.onSrollChangeListener = onSrollChangeListener;
	}

	public interface OnSrollChangeListener {
		public void onSrollCange(int scrollY);
	}

}
