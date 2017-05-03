/*
 * 文件名：DateUtil.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： DateUtil.java
 * 修改人：wuchenhui
 * 修改时间：2015-6-2
 * 修改内容：新增
 */
package com.zxly.o2o.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

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
 * @version    YIBA-O2O 2015-6-2
 * @since      YIBA-O2O
 */
public class TimeUtil {

	
	/**
	 * 获取剩余确认时间
	 */
	public static String getResideTime(long resideTime) {
		
		String result="";
		if(resideTime<=0)
			return result;
		
		long[] times=getTime(resideTime);
		long day_decade = times[0] / 10;
		long day_unit = times[0] - day_decade * 10;
		
		long hour_decade = times[1] / 10;
		long hour_unit = times[1] - hour_decade * 10;

		long min_decade = times[2] / 10;
		long min_unit = times[2] - min_decade * 10;
		
		if(times[0]!=0){
			result= day_decade+""+day_unit+"天"+hour_decade+""+hour_unit+"小时";
		}else{
			result= hour_decade+""+hour_unit+"小时"+min_decade+""+min_unit+"分钟";
		}

		return result;
	}
	
	/**
	 * 返回 00:00:00 格式的时间数据
	 */
	public static long[] getTime(long resideTime) {
		long[] time=new long[]{0,0,0};
		if(resideTime<=0)
			return time;
		
		long day = resideTime / (24 * 1000 * 60 * 60);
		long hours   = (resideTime - day*(24 * 1000 * 60 * 60)) / (1000 * 60 * 60) ;
		long minutes = (resideTime - day*(24 * 1000 * 60 * 60)  - hours * (1000 * 60 * 60)) / (1000 * 60);
		
        time[0]=day;
        time[1]=hours;
        time[2]=minutes;
        
		return time;
	}
	
	/**
	 * 格式化订单时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatTimeHHMMDD(Long time){
		if(time==null)
			return "-";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time);
		return formatter.format(date);
	}

	/**
	 * 格式化订单时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatTimeMMDD(Long time){
		if(time==null)
			return "-";
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
		Date date = new Date(time);
		return formatter.format(date);
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatOrderTimeHHMM(Long time){
		if(time==null)
			return "-";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM");
		Date date = new Date(time);
		return formatter.format(date);
	}
	
	/**
	 * 格式化订单时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatOrderTime(Long time){
		if(time==null||time==0)
			return "-";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd   HH:mm");
		Date date = new Date(time);
		return formatter.format(date);
	}



	public static String formatTimeYMDHMS(Long time){
		if(time==null||time==0)
			return "-";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return formatter.format(date);
	}

	/**
	 * 格式化订单时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String formatToDate(Long time){
		if(time==null||time==0)
			return "-";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time);
		return formatter.format(date);
	}

	public static int getDayOfMonth(int year,int month){
		if(year==0||month==0)
			return 0;

		boolean isRunNian=false;
		int day=0;

		if((year % 400 == 0)||(year % 4 == 0)&&(year % 100 != 0))
			isRunNian=true;

		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day=31;
				break;

			case 2:
				if(isRunNian){
					day=29;
				}else {
					day=28;
				}

				break;

			case 4:
			case 6:
			case 9:
			case 11:
				day=30;
				break;

			default:
				break;
		}

		return  day;
	}
	
}
