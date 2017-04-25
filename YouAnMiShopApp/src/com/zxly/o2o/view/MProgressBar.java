package com.zxly.o2o.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DesityUtil;


public class MProgressBar extends View {
	private Paint paint;

	/**中间进度百分比的字符串的颜色 */
	private int textColor;
	/**进度条颜色 */
	private int roundProgressColor;

	private int max; //最大进度
	private int progress; //当前进度


	public MProgressBar(Context context) {
		this(context, null);
	}

	public MProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		paint = new Paint();
		paint.setAntiAlias(true);  //消除锯齿
		
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
		
//		//获取自定义属性和默认值
//		roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
		roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
//		textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_pTextColor, Color.GREEN);
//		textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_pTextSize, 15);
//		roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_pMax, 100);
//		textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
//		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
		
		mTypedArray.recycle();
	}
	

//	private Bitmap headIcon;
	private int height;
	private int width;
	private int initWidth=0;

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//Bitmap headIcon=null;

		/*
		if(headIcon==null||headIcon.isRecycled()){
//			BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_header);
//			headIcon = bitmapDrawable.getBitmap();
			InputStream is = getResources().openRawResource(R.drawable.icon_header);
			headIcon  = BitmapFactory.decodeStream(is);

			headIcon= new BitmapUtil().zoomBitmap(headIcon,DesityUtil.dp2px(getContext(),30),DesityUtil.dp2px(getContext(),30));
			height=headIcon.getHeight();
			width=getWidth();
		}
		*/

		height=getHeight();
		width=getWidth();

		int radius= DesityUtil.dp2px(getContext(),2);
		int centreLeftX=radius;
		int centreLeftY=height/2;

		int centreRightX=getWidth()-radius;
		int centreRightY=centreLeftY;

		//绘制背景进度条
		paint.setColor(getResources().getColor(R.color.gray_e8e8e8));
		canvas.drawRect(initWidth + radius, centreLeftY - radius, getWidth() - radius, centreLeftY + radius, paint);
		canvas.drawCircle(centreRightX, centreRightY, radius, paint);
		canvas.drawCircle(centreLeftX, centreLeftY, radius, paint);

		//绘制当前进度
//		paint.setColor(getResources().getColor(R.color.yellow_fc9c3e));
		paint.setColor(roundProgressColor);
		//绘制左边圆角
//		canvas.drawCircle(centreLeftX, centreLeftY, radius, paint);//

		if(max==0||progress==0){
//			canvas.drawRect(radius, centreLeftY-radius, initWidth+10, centreLeftY+radius, paint);//
//			canvas.drawBitmap(headIcon,initWidth,0, paint);
		}else{
			int currentXPos= (int) ((float)progress/(float)max*(width-initWidth));
			//Log.d("curProgress", "xxx--> max=" + max + "  cur=" + progress + "  res=" + currentXPos);
			canvas.drawRect(radius, centreLeftY - radius, currentXPos - radius, centreLeftY + radius, paint);
			canvas.drawCircle(centreLeftX, centreLeftY, radius, paint);
			if(progress == max){
				canvas.drawCircle(currentXPos - radius, centreRightY, radius, paint);
			}

//			if((currentXPos+initWidth)>=(width-headIcon.getWidth())){
//				canvas.drawBitmap(headIcon,width-headIcon.getWidth(),0, paint);
//			}else{
//				canvas.drawBitmap(headIcon,currentXPos+initWidth,0, paint);
//			}

		}

//		if(headIcon!=null&&!headIcon.isRecycled()){
//			headIcon.recycle();
//			headIcon=null;
//		}

	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.d("onDetachedFromWindow","---->call");
//		if(headIcon!=null&&!headIcon.isRecycled()){
//			headIcon.recycle();
//			headIcon=null;
//		}
	}

	public void setRoundProgressColor(int roundProgressColor){
		this.roundProgressColor =  roundProgressColor;
		invalidate();
	}

	public synchronized int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * 获取进度.需要同步
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
	 * 刷新界面调用postInvalidate()能在非UI线程刷新
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if(progress < 0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(progress > max){
			progress = max;
		}
		if(progress <= max){
			this.progress = progress;
			postInvalidate();
		}
		
	}

	




}
