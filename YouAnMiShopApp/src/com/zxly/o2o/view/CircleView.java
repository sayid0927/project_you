package com.zxly.o2o.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zxly.o2o.shop.R;

/**
 * Created by Administrator on 2016/3/22.
 */
public class CircleView extends View {
    private Paint mPaint = new Paint();
    private int mColor;
    private int mAlpha;
    private float mRadius;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet set){
        TypedArray mTypedArray = getContext().obtainStyledAttributes(set, R.styleable.Circle);
        mColor = mTypedArray.getColor(R.styleable.Circle_color, Color.BLACK);
        mRadius = mTypedArray.getDimension(R.styleable.Circle_radius, 50);
        mAlpha = mTypedArray.getInteger(R.styleable.Circle_alpha, 0);
        mTypedArray.recycle();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setAlpha(mAlpha);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(600, 600, mRadius, mPaint);
        canvas.save();
    }
}
