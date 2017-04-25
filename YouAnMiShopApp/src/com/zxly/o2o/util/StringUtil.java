package com.zxly.o2o.util;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-25
 * @since YIBA-O2O
 */
public class StringUtil {
    static String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿"};
    static char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};

    public static boolean isNull(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String getFormatTime(long time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(time);
    }

    public static String getFormatPrice(float price) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(price);

    }

    public static String getPrice(float price){
        return  "￥"+getFormatPrice(price);
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
        return "<font color='" + color + "'>" + text + "</font>";
    }

    public static String getDateByMillis(long millis){
        Date date = new Date(millis);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(date);
    }

    public static String getDateByMillis(long milllis, String format) {
        if (milllis > 0) {
            DateFormat formatter = new SimpleDateFormat(format);
            // long now = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milllis);
            return formatter.format(calendar.getTime());
        } else {
            return "";
        }
    }


    public static String getShortTime(long createTime) {
        if (createTime != 0) {
            createTime =  Config.serverTime() - createTime;

            long year = (createTime / (24 * 3600000 * 365l));
            if (year != 0) {
                return year + "年前";
            }
            long month = (createTime / (24 * 3600000 * 30l));
            if (month != 0) {
                return month + "月前";
            }
            long week = (createTime / (24 * 3600000 * 7l));
            if (week != 0) {
                return week + "星期前";
            }
            long day = (createTime / (24 * 3600000l));
            if (day != 0) {
                return day + "天前";
            }
            long hour = (createTime / 3600000l);
            if (hour != 0) {
                return hour + "小时前";
            }
            long minute = (createTime / 60000l);
            if (minute != 0) {
                return minute + "分钟前";
            }
        }
        return "刚刚";
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
                    continue;
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

    public static String toFromatTime(long time, String fromat_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(fromat_time);
        return dateFormat.format(time);
    }


    public static void setTextSpan(int start, int end, int textSize, int colcor, Spanned text,
                                   TextView textView) {
        SpannableString spS = new SpannableString(text);
        spS.setSpan(new AbsoluteSizeSpan((int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize,
                                AppController.displayMetrics)),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (colcor != 0) {
            spS.setSpan(new ForegroundColorSpan(colcor), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spS);
    }

    public static int getRelativeSize(int size, Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return (dm.widthPixels > 900) ? (2 * size) : ((dm.widthPixels > 720) ? (size + (2 * size) / 3) : (dm.widthPixels > 540) ? (size + size / 3) : (dm.widthPixels > 480) ? size : (size - size / 10));
    }


    public static int getCurrentYear(){
        String currentYear = getFormatTime(Config.currentServerTime, "yyyy");
        return Integer.parseInt(currentYear);
    }

    public static int getCurrentMonth(){
        String currentMonth = getFormatTime(Config.currentServerTime, "MM");
        return Integer.parseInt(currentMonth);
    }

    public static boolean inSleepTime(){
        Calendar calendar = Calendar.getInstance();
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        if(curHour < 8 || curHour > 22){
            return true;
        }
        return false;
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

    public static String trimFromStr(String oldStr,int saveLen){
        if(oldStr!=null&&oldStr.length()>saveLen){
            String end=oldStr.subSequence(saveLen, oldStr.length()).toString();
            return oldStr.replace(end, "...");
        }

        return oldStr;
    }


    public static String getRMBPrice(float price) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return "￥"+df.format(price);

    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    public static String getStyleHtml(String htmlContent) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"format-detection\" content=\"telephone=no\">\n" +
                "    <title></title>\n" +
                "    <meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1\">\n" +
                "    <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
                "    <meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
                "</head>\n");
        stringBuilder.append("<style>  \n")
                .append("body{word-break:break-all!important;} \n")
                .append("img{  \n")
                .append(" max-width:100%!important;  \n")
                .append(" height:auto;  \n")
                .append("}  \n")
                .append("</style> \n");
        stringBuilder.append("<body> \n");
        stringBuilder.append(htmlContent);
        stringBuilder.append("</body>\n" +
                "</html>");
        return stringBuilder.toString();
    }

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
