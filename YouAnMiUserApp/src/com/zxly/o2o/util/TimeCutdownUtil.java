/*
 * 文件名：TimeCutdownTools.java
 * 版权：Copyright 2014 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： TimeCutdownTools.java
 * 修改人：wuchenhui
 * 修改时间：2014-12-23
 * 修改内容：新增
 */
package com.zxly.o2o.util;

import android.util.Log;

import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.TimeCutDown;
import com.zxly.o2o.request.ServerTimeRequest;
import com.zxly.o2o.view.TimeCutDownLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * 倒计时
 * 
 * @author wuchenhui
 * @version YIBA-O2O 2014-12-23
 * @since YIBA-O2O
 */
public class TimeCutdownUtil {
	public static long serverTime;
	public static final int REFRESH_RATE = 1000;
	public static final int TIME_TYPE_HMS = 0;
	public static final int TIME_TYPE_HHMMSS = 1;
	public static final List<TimeCutDownLayout> layouts=new ArrayList<TimeCutDownLayout>();
	public boolean isStart = false;
	private long lastSystemTime; //用于记录上次运算系统时间（防止逗B们恶意串改系统时间）
	public static long changeTime; //用于记录被修改过的时间差
    private static TimeCutdownUtil timeCutdownUtil;
    public static final int CHECK_TIME_RATE=60*60*1000;//(每隔一个小时主动强求校验时间)
    private static final long[] FINISH_TIME=new long[]{0,0,0};
	private TimeCutdownUtil() {
	}
	public static TimeCutdownUtil getInstance(){	

		if(timeCutdownUtil==null){
			timeCutdownUtil=new TimeCutdownUtil();	
		}
		return timeCutdownUtil;
	}


	public void startTimeCutDown() {
		if (!isStart){
			startTimeThread();	
		}
			
	}

	public void stopTimeCutDown() {
		if (isStart)
			stopTimeThread();
	}

	private void stopTimeThread() {
		isStart = false;
	}

	private void startTimeThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int runTime=0;
					isStart = true;

					while (isStart) {

						for (int i = 0; i < layouts.size(); i++) {
							if(AppController.getInstance().getTopAct()==layouts.get(i).getContext())
							cutdownTime(layouts.get(i));
							Log.d("current","size :"+layouts.size()  +"   pos : "+i);
						}

                        //第一次开启队列的时候检查系统时间，加入心跳机制后可去除
						if(runTime==0||runTime==CHECK_TIME_RATE){
							new ServerTimeRequest().start();
							runTime=0;
						}

						runTime+=REFRESH_RATE;
						Thread.sleep(REFRESH_RATE);
					}

				} catch (Exception e) {
					Log.d("errorBerak", "-->"+e.toString());
					e.printStackTrace();
				} finally {
					isStart = false;
				}

			}

		}).start();
	}

	
	public void cutdownTime(final TimeCutDownLayout layout) {
		final long[] times = getTimes(layout.getItem());
		layout.changeTime(times);
    }

	public long[] getTimes(TimeCutDown timeItem) {
		long[] times = null;

        //用于防止时间被恶意篡改在上一秒的时间 减去当前的绝对值大于5s 系统时间就是被修改了
		if(lastSystemTime!=0&&Math.abs(System.currentTimeMillis()-lastSystemTime)>5000){
			changeTime=lastSystemTime+changeTime-System.currentTimeMillis()+REFRESH_RATE; //如果获取不到服务器时间就暂时通过算法纠错
			new ServerTimeRequest().start();
		//	Log.d("changeTime", "-->"+"本地时间："+getCurrentTime(System.currentTimeMillis())+"  服务器时间 ："+getCurrentTime(getServerTime())  +" 时间差 ："+changeTime);
		}else{
		//	Log.d("nochange", "-->"+"本地时间："+getCurrentTime(System.currentTimeMillis())+"  服务器时间 ："+getCurrentTime(getServerTime()) +" 时间差:"+changeTime);
		}

		lastSystemTime=System.currentTimeMillis();

		//服务器时间=当前系统时间+时间差（changeTime）;
		timeItem.setLeavingTime(timeItem.getEndTime()-getServerTime());			

		switch (timeItem.getTimeStyle()) {
			case TIME_TYPE_HMS :
				if(timeItem.getLeavingTime()>=0){
					times = getHMSTimes(timeItem);	
				}else{
					times=FINISH_TIME;
				}

				break;

			case TIME_TYPE_HHMMSS :
				if(timeItem.getLeavingTime()>=0){
					times = getHHMMSSTimes(timeItem);	
				}else{
					times=new long[]{0,0,0,0,0,0};
				}

				break;

			default :
				times=FINISH_TIME;
				break;			
		}
		
		return times;
	}
	
	
	
	public static long getServerTime(){
		return System.currentTimeMillis()+changeTime;
	}


    /**
     * 返回 00:00:00 格式的时间数据
     *
     * @param bean
     * @return times[0]：时 ，times[1]：分 ，times[2]：秒
     */
    public long[] getHMSTimes(TimeCutDown bean) {
        if(bean.getLeavingTime()<=0)
            return new long[]{0, 0, 0};
        long hours = bean.getLeavingTime() / (1000 * 60 * 60);
        long minutes = (bean.getLeavingTime() - hours * (1000 * 60 * 60))/ (1000 * 60);
        long seconds = (bean.getLeavingTime() - hours * (1000 * 60 * 60) - minutes* (1000 * 60)) / (1000);
        long[] times = new long[]{hours, minutes, seconds};
        return times;
    }

    /**
     * 返回 0 0:0 0:0 0格式 的时间数据，时分秒的十位个位数字都是拆开的
     *
     * @param bean
     * @return
     */
    public long[] getHHMMSSTimes(TimeCutDown bean) {
        long[] times = getHMSTimes(bean);
        Log.d("currentTimeHms", "-->"+ times[0]+" : "+times[1]+" : "+times[2]);
        long hour_decade = times[0] / 10;
        long hour_unit = times[0] - hour_decade * 10;

        long min_decade = times[1] / 10;
        long min_unit = times[1] - min_decade * 10;

        long sec_decade = times[2] / 10;
        long sec_unit = times[2] - sec_decade * 10;

        Log.d("currentTime", "-->"+ hour_decade+" "+hour_unit+" : "+min_decade+" "+min_unit+" : "+sec_decade+" "+sec_unit);
        return new long[]{hour_decade, hour_unit, min_decade, min_unit,sec_decade, sec_unit};
    }


    /**
	 * 新加布局入队列
	 */
	public void addOnTimeChangeLayout(TimeCutDownLayout layout) {
		int position=indexOfLayout(layout);
		if(position!=-1)
			return;
		
		layouts.add(layout);
		Log.d("addItemTolIst", " in  size=="+layouts.size());
		startTimeCutDown();
	}

    /**
     * 删除布局
     */
	public void removeLayout(TimeCutDownLayout layout){
		int position=indexOfLayout(layout);
		Log.d("onCallRemove", "-->"+position);
		if(position!=-1){
			layouts.remove(position);			
		}else{
			
		}		
	}


    /**
     * 查找布局
     */
    public int indexOfLayout(TimeCutDownLayout layout){
    	if(layouts!=null&&layout!=null){
			for (int i = 0; i <layouts.size(); i++) {
                if(layouts.get(i)==layout){
                	return i;
                }
			}
		}
    	return -1;
    }




    public static String getCurrentTime(long currentTime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
        Date date = new Date(currentTime);
        return formatter.format(date);

    }



}
