package com.zxly.o2o.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MyViewPager extends ViewPager{
    private boolean mIsCanScroll;
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
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
}
