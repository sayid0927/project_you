package com.zxly.o2o.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.UserMaintain;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-25
 * @since YIBA-O2O
 */
public class StringUtil {
	public final static DateFormat db2TimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	static String[] units = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
			"十亿", "百亿", "千亿", "万亿" };
	static char[] numArray = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };

	public static boolean isNull(String str) {
		if (str == null || str.trim().length() == 0)
			return true;
		else
			return false;
	}

	public static String getBeforeMinutes(long startMillis) {
		long nowMillis = System.currentTimeMillis();
		int seconds = (int) ((nowMillis - startMillis) / 1000);
		int minutes = seconds / 60;
		if(minutes == 0) {
			return "刚刚";
		} else if (minutes < 60) {
			return minutes + "分钟前";
		} else if (minutes > 1440) {
			return minutes / 1440 + "天前";
		} else {
			return minutes / 60 + "小时前";
		}
	}

	/**
	 * 将bean转换成键值对列表
	 *
	 * @param bean
	 * @return
	 */
	public static List<NameValuePair> bean2Parameters(Object bean)
	{
		if (bean == null)
		{
			return null;
		}
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();

		// 取得bean所有public 方法
		Method[] Methods = bean.getClass().getMethods();
		for (Method method : Methods)
		{
			if (method != null && method.getName().startsWith("get")
					&& !method.getName().startsWith("getClass"))
			{
				// 得到属性的类名
				String value = "";
				// 得到属性值
				try
				{
					String className = method.getReturnType().getSimpleName();
					try
					{
						value =  method.invoke(bean)+"";
					} catch (InvocationTargetException e)
					{

					}
					if (value != null && value != "")
					{
						// 添加参数
						// 将方法名称转化为id，去除get，将方法首字母改为小写
						String param = method.getName().replaceFirst("get", "");
						if (param.length() > 0)
						{
							String first = String.valueOf(param.charAt(0))
									.toLowerCase();
							param = first + param.substring(1);
						}
						parameters.add(new BasicNameValuePair(param, value));
					}
				} catch (IllegalArgumentException e)
				{
				} catch (IllegalAccessException e)
				{
				}
			}
		}
		return parameters;
	}
	/**
	 * 对Object进行List<NameValuePair>转换后按key进行升序排序，以key=value&...形式返回
	 *
	 * @param list
	 * @return
	 */
	public static String sortParam(Object order)
	{
		List<NameValuePair> list = bean2Parameters(order);
		return sortParam(list);
	}
	/**
	 * 对List<NameValuePair>按key进行升序排序，以key=value&...形式返回
	 *
	 * @param list
	 * @return
	 */
	public static String sortParam(List<NameValuePair> list)
	{
		if (list == null)
		{
			return null;
		}
		Collections.sort(list, new Comparator<NameValuePair>() {
			@Override
			public int compare(NameValuePair lhs, NameValuePair rhs) {
				return lhs.getName().compareToIgnoreCase(rhs.getName());
			}
		});
		StringBuffer sb = new StringBuffer();
		for (NameValuePair nameVal : list)
		{

			if (null != nameVal.getValue() && !"".equals(nameVal.getValue()))
			{
				sb.append(nameVal.getName());
				sb.append("=");
				sb.append(nameVal.getValue());
				sb.append("&");
			}
		}
		String params = sb.toString();
		if (sb.toString().endsWith("&"))
		{
			params = sb.substring(0, sb.length() - 1);
		}
		return params;
	}
	public static String getFormatTime(long time, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(time);
	}
	public static String getFormatPrice(float price)
	{
		DecimalFormat   df=new   DecimalFormat("#0.00") ;
		return df.format(price);

	}
	public static long getMillisByDate(String date) {
		long millis = 0l;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// String millisString = "2011-09-20 00:00:00";
			millis = sdf.parse(date).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return millis;
	}
	public static String color(String text, String color) {
		return new StringBuilder().append("<font color='" ).append(color)
				.append("'>").append(text).append("</font>").toString();
	}

	public static String getDateByMillis(long millis){
		Date date = new Date(millis);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(date);
	}

	public static String getDateByMillis(long millis, String format) {
		if (millis > 0) {
			DateFormat formatter = new SimpleDateFormat(format);
			// long now = System.currentTimeMillis();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(millis);
			String date = formatter.format(calendar.getTime());
			return date;
		} else {
			return "";
		}
	}

	public static String getDateByMonthAdd(long millis, int monthAdd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(millis);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, monthAdd);
		return sdf.format(calendar.getTime());
	}
	
	public static String getCurrentDateStamp() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(new Date());
	}

	public static String getShortTime(long  createTime)
	{
		if (createTime != 0) {
            createTime = Config.serverTime() - createTime;

            Long year = (createTime / (24 * 3600000 * 365l));
            if (year != 0) {
                return year+"年前";
            }
            Long month = (createTime / (24 * 3600000 * 30l));
            if (month != 0) {
                return month + "月前";
            }
            Long week = (createTime / (24 * 3600000 * 7l));
            if (week != 0) {
                return week + "星期前";
            }
            Long day = (createTime / (24 * 3600000l));
            if (day != 0) {
                return day + "天前";
            }
            Long hour = (createTime / 3600000l);
            if (hour != 0) {
                return hour + "小时前";
            }
            Long minute = (createTime / 60000l);
            if (minute != 0) {
                return minute + "分钟前";
            }
        }
		return "刚刚";
	}

	/**
	 * 字节数组转hex字符串
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des.toUpperCase();
	}

	/**
	 * hex字符串转字节数组
	 */
	public static byte[] hex2Bytes(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
	public static String foematInteger(int num) {
		char[] val = String.valueOf(num).toCharArray();
		int len = val.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			String m = val[i] + "";
			int n = Integer.valueOf(m);
			boolean isZero = n == 0;
			String unit = units[(len - 1) - i];
			if (isZero) {
				if ('0' == val[i - 1]) {
					// not need process if the last digital bits is 0
				} else {
					// no unit for 0
					sb.append(numArray[n]);
				}
			} else {
				sb.append(numArray[n]);
				sb.append(unit);
			}
		}
		return sb.toString();
	}
	public static String toFromatTime(long time,String fromat_time){
		SimpleDateFormat dateFormat = new SimpleDateFormat(fromat_time);
		return dateFormat.format(time);
	}
	
	public static String generateScanInfo(UserMaintain maintain) {
		return "保单信息： "
				+ "\r\n保单名称：" + maintain.getProductName()
				+ "\r\n商品售价：" + "￥" + maintain.getPrice()
				+ "\r\n保单类型：" + (maintain.getType()==1?"首保":"续保")
				+ "\r\n用户账户：" + Account.user.getUserName()
				+ "\r\n生效时间：" + StringUtil.getDateByMillis(maintain.getEffectTime(), "yyyy-MM-dd")
				+ "\r\n剩余天数：" + (maintain.getResidueTime() < 1?"已过期": maintain.getResidueTime() + "天")
				+ "\r\nIMEI号：" + maintain.getIMEI();
	}

	public static String generateScanInfo2(UserMaintain maintain) {
		StringBuffer sb = new StringBuffer();
		sb.append("保单信息： ");
		sb.append("\r\n保单名称：");
		sb.append(maintain.getProductName());
		sb.append("\r\n商品售价： ￥");
		sb.append(maintain.getPrice());
		sb.append("\r\n保单类型：");
		sb.append((maintain.getType()==1?"首保":"续保"));
		sb.append("\r\n用户账户：");
		sb.append(Account.user.getUserName());
		sb.append("\r\n生效时间：");
		sb.append(StringUtil.getDateByMillis(maintain.getEffectTime(), "yyyy-MM-dd"));
		sb.append("\r\n剩余天数：");
		sb.append((maintain.getResidueTime() < 1?"已过期": maintain.getResidueTime() + "天"));
		sb.append("\r\nIMEI号：");
		sb.append(maintain.getIMEI());
		return sb.toString();
	}

	public static void setTextSpan(int start, int end,int textSize,int colcor, String text, TextView textView,DisplayMetrics dm) {
		SpannableString spS = new SpannableString(text);

		spS.setSpan(new AbsoluteSizeSpan((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, dm)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		if(colcor!=0) {
			spS.setSpan(new ForegroundColorSpan(colcor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		textView.setText(spS);
	}

	public static void setTextSpan(int start, int end,int textSize,int colcor, Spanned text, TextView textView,DisplayMetrics dm) {
		SpannableString spS = new SpannableString(text);

		spS.setSpan(new AbsoluteSizeSpan((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, dm)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		if(colcor!=0) {
			spS.setSpan(new ForegroundColorSpan(colcor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		textView.setText(spS);
	}

	public static String getHiddenString(String string) {
		if (!isNull(string) && string.length() > 4) {
			return string.substring(0, 4) + "****" + string.substring(string.length() - 4);
		} else {
			return "";
		}
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile(Constants.PHONE_PATTERN);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isIdNo(String id){
		Pattern p = Pattern.compile(Constants.ID_PATTERN);
		Matcher m = p.matcher(id);
		return m.matches();
	}

	public static String getHideName(String name){
		String hideName;
		int length = name.length();
		switch (length){
			case 0:
			case 1:
				hideName = name;
				break;
			case 2:
				hideName = name.substring(0, 1) + "*";
				break;
			case 3:
				hideName = name.substring(0, 1) + "*" + name.substring(2);
				break;
			case 4:
				hideName = name.substring(0, 1) + "**" + name.substring(3);
				break;
			default:
				hideName = name;
				break;
		}
		return hideName;
	}

	public static String getDomainName(String url){
		String result="";
		if(url.startsWith("http")){
			String newUrl=url.replaceFirst("http://", "");
			int start=newUrl.indexOf("/");
			result=newUrl.subSequence(start, newUrl.length()).toString();
			result=url.replace(result, "");
		}

		return result;
	}

	public static String appendUrlArgs(String url,String[] args){
		if(DataUtil.stringIsNull(url)||DataUtil.arrayIsNull(args))
			return url;

		StringBuffer sb=new StringBuffer();
		sb.append(url);

		if(url.indexOf("?")!=-1){
			for (int i=0;i<args.length;i++) {
				sb.append("&"+args[i]);
			}
		}else {
			for (int i=0;i<args.length;i++) {
				if(i==0){
					sb.append("?"+args[i]);
				}else {
					sb.append("&"+args[i]);
				}
			}
		}

		return sb.toString();
	}


	public static String getRMBPrice(float price) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return "￥"+df.format(price);

	}

//	public static int getRelativeSize(int size,Activity activity) {
//		DisplayMetrics dm = new DisplayMetrics();
//		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//		return (dm.widthPixels > 900) ? (2 * size) : ((dm.widthPixels > 720) ? (size + (2 * size) / 3) : (dm.widthPixels > 540) ? (size + size / 3) : (dm.widthPixels > 480) ? size : (size - size / 10));
//	}

	public static String getCheckInfo(){
		StringBuilder stringBuilder = new StringBuilder("当前版本编号: ");
		stringBuilder.append(Config.appSerialNo);
		stringBuilder.append("\n接口地址: ");
		stringBuilder.append(Config.dataBaseUrl);
		stringBuilder.append("\n环信key: ");
		stringBuilder.append(getHiddenString(Config.hxInfo));
		stringBuilder.append("\n个推AppId: ");
		stringBuilder.append(getHiddenString(Config.id));
		stringBuilder.append("\n个推AppKey: ");
		stringBuilder.append(getHiddenString(Config.key));
		stringBuilder.append("\n个推AppSecret: ");
		stringBuilder.append(getHiddenString(Config.secret));
		return stringBuilder.toString();
	}

}
