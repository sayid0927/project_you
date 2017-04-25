/*
 * 文件名：TimeCutDownLayout.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： TimeCutDownLayout.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-25
 * 修改内容：新增
 */
package com.zxly.o2o.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.model.TimeCutDown;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.TimeCutdownUtil;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-3-25
 * @since      YIBA-O2O
 */
public class TimeCutDownLayout extends LinearLayout {
	private CallBack callback;
	private TimeCutdownUtil timeCutdownUtil;
	private TimeCutDown item;
	/**private TextView hour,min,seconds,dot01,dot02;//目前暂时不用（用于设置时 分 秒分开的情况） */
	private TextView timeText;
	private int   textColor,textDefColor;
	private float textSize;
	private boolean isInit;
	public TimeCutDownLayout(Context context) {
		super(context);
		setVisibility(View.GONE);
	}
	public TimeCutDownLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimeCutDownLayout);
		 textSize=a.getDimension(R.styleable.TimeCutDownLayout_timeTextSize, getResources().getDimension(R.dimen.small_text));
		 textDefColor=getResources().getColor(R.color.white);
		 textColor=a.getColor(R.styleable.TimeCutDownLayout_timeTextColor,textDefColor);
		 a.recycle();
		 setVisibility(View.GONE);
	}
	
	public void setTextStyle(){
		 textColor=getResources().getColor(R.color.white);
		 textSize=getResources().getDimension(R.dimen.small_text);
	}
	
	

	
	private void initView(){
		timeCutdownUtil=TimeCutdownUtil.getInstance();
		timeText=new TextView(getContext());
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-2,-2);
		timeText.setLayoutParams(params);
		timeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		timeText.setTextColor(textColor);
		addView(timeText);

		/*
		View timeView=LayoutInflater.from(getContext()).inflate(R.layout.time_layout_style3,null);
		hour = (TextView) timeView.findViewById(R.id.hour);
		min = (TextView) timeView.findViewById(R.id.min);
		seconds = (TextView)timeView.findViewById(R.id.sec);
		dot01= (TextView)timeView.findViewById(R.id.dot01);
		dot02= (TextView)timeView.findViewById(R.id.dot02);
		hour.setTextColor(textColor);
		hour.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		min.setTextColor(textColor);
		min.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		seconds.setTextColor(textColor);
		seconds.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		dot01.setTextColor(textColor);
		dot01.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		dot02.setTextColor(textColor);
		dot02.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		addView(timeView);
		*/
	
	}
	
	
	public void setOnTimeFinishCallBack(CallBack callback){
		this.callback=callback;
	}
	
	
	public TimeCutDown getItem() {
		return this.item;
	}
	public void setItem(TimeCutDown item) {
		this.item = item;
	}


	/**
	 * 只传入剩余时间
	 * @param resideTime
	 */
	public void setResideTime(long resideTime){
		
//		if(resideTime<=1000)
//			return; // 限时抢时间小于1秒不给加到队列

		TimeCutDown timeItem=new TimeCutDown();
		timeItem.setTimeStyle(0);
		timeItem.setLeavingTime(resideTime);
		addTimeCutDownItemToList(timeItem);
		
	}
	
	public void addTimeCutDownItemToList(TimeCutDown timeItem){

		//应为不是所有的都需要time布局，如果在构造方法执行，清仓的时候也会被加载initView
		if(!isInit)
			initView();

		isInit=true;
        setVisibility(View.VISIBLE);	
		timeItem.setTimeStyle(0);

		if(timeItem.getEndTime()<=0)
			timeItem.setEndTime(timeCutdownUtil.getServerTime()+timeItem.getLeavingTime());

		long[] time =timeCutdownUtil.getHMSTimes(timeItem);
		setTime(timeText, time);
		
		setItem(timeItem);
		timeCutdownUtil.addOnTimeChangeLayout(this);
	}



    /**
     * timecutdownUtil里面调用，用于设置当前显示的时间
     */
    public void changeTime(long[] times) {

        if(item.getLeavingTime()<=0&&callback!=null){
            timeCutdownUtil.removeLayout(this);
            post(new Runnable() {
                @Override
                public void run() {
                    callback.onCall();
                }
            });
        }

        /**
         setTimeStr(hour, times[0]);
         setTimeStr(min, times[1]);
         setTimeStr(seconds, times[2]);
         */
        setTime(timeText, times);
    }



    /**
	 * 用于设置 时 分 秒 分开的情况
	 * 
	 * @param text
	 * @param time
	 */
	private void setTime(final TextView text, final long time) {
		post(new Runnable() {			
			@Override
			public void run() {
				text.setText(getTime(time));			
			}
		});
	}
	
	/**
	 * 用于设置 时 分 秒 分开的情况
	 * 
	 * @param text
	 */
	private void setTime(final TextView text, final long[] times) {
		post(new Runnable() {			
			@Override
			public void run() {
//				if(times[0]==0&&times[1]==0&&times[2]==0){
//					 text.setText("");
//					 return;
//				}

				if((times[0]/24)>3){
					text.setText((times[0]/24)+"天");
					return;
				}

			    text.setText(getTime(times[0])+":"+getTime(times[1])+":"+getTime(times[2]));
			}
		});
	}
	
	private String getTime(long time) {
		if (time<= 0) {
			return "00";
		} else if(time>0&&time<10){
			return "0"+time;
		}else{
			return time+"";
		}
	}
	
//	public void changeTimeByStyle1(long times[], View viewHolder) {			
//		setTimeStr(hour, times[0]);
//		setTimeStr(min, times[1]);
//		setTimeStr(seconds, times[2]);
//		Log.d("refreshTime", "剩余时间 ：" + times[0] + " : " + times[1] + " : " + times[2]);
//	}
	

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
//		if(getVisibility()==View.VISIBLE)
//		Log.d("onScreen", "onAttachedToWindow ---> size=="+timeCutdownUtil.layouts.size());

	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(timeCutdownUtil!=null&&getVisibility()==View.VISIBLE)
			timeCutdownUtil.removeLayout(this);
		//if(getVisibility()==View.VISIBLE)
	//	Log.d("onScreen", "onDetachedFromWindow  ->" +getVisibility()  +" size=  "+timeCutdownUtil.layouts.size());
	}




	
}
