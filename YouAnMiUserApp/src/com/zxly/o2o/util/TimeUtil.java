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
	 * 获取剩余确认时间
	 */
	public static String getConfirmReceiveResideTime(long resideTime) {

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
			result= day_unit+"天"+hour_decade+""+hour_unit+"小时"+min_decade+""+min_unit+"分钟";
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
	public static String formatOrderTime(Long time){
		if(time==null||time==0)
			return "-";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date(time);	
		return formatter.format(date);
	}



	/**
	 * 格式化订单时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getHHMMDDTime(Long time){
		if(time==null||time==0)
			return "-";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time);
		return formatter.format(date);
	}

	/**
	 * 格式化订单时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getDetailTime(Long time){
		if(time==null||time==0)
			return "-";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = new Date(time);
		return formatter.format(date);
	}

	public static String getBeforeTime(long resideTime) {
		//小于5分钟的视为刚刚

		if (resideTime<=0)
			return "--";

		if((resideTime/(1000*60))<5){
			return "刚刚";
		}else if((resideTime/(1000*60))<=60){
			return (resideTime/(1000*60)) + "分钟前";
		}else if((resideTime/(1000*60*60))<=24){
			return (resideTime/(1000*60*60)) + "小时前";
		}else if((resideTime/(1000*60*60*24))>=1){
			return (resideTime/(1000*60*60*24)) + "天前";
		}else {
			return "--";
		}

	}

}
