/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.easeui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.ui.EaseShowBigImageActivity;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshBase;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshListView;
import com.easemob.util.DateUtils;
import com.easemob.util.TimeInfo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class EaseConstant {
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;

    public static long shopID;  //shopId<0时为商户端  >0为用户端
    public static String getuiUserClientID;//用户个推clientId
    public static String getuiShopClientID;
    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";
    public static String token = "";
    public static String imei = "";
    public static EaseYAMUser currentUser = new EaseYAMUser();
    public static String dataBaseUrl = "";
    public static int ADD_FRIEND_SUSSECC = 0; //大于0表示有新增好友
    public static long timeOffset = 0;
    public static  List<String> isWelcome=new ArrayList<String>();   //是否显示过欢迎词
    public static boolean isRegistMembers = false;
    public static List<String> users = new ArrayList<String>(); //要推送的人员
    public static List<String> getuiUsers = new ArrayList<String>(); //要个推推送的人员
    public static int multiMsgProgress;
    //EaseConversationList的滑动状态 默认为禁止状态
    public static int EaseConversationListscrollState=0;
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    public static void startActivity(Intent intent, Activity curAct) {
        if (intent != null) {
            curAct.startActivity(intent);
            curAct.overridePendingTransition(R.anim.ease_slide_left_in, R.anim.ease_slide_left_out);
        }
    }

    public static void startActivityForResult(Intent intent, Activity curAct, int requestCode) {
        if (intent != null) {
            curAct.startActivityForResult(intent, requestCode);
            curAct.overridePendingTransition(R.anim.ease_slide_left_in, R.anim.ease_slide_left_out);
        }
    }

    public static void startActivityNormal(Class toClass, Activity curAct) {

        if (toClass != null) {
            Intent intent = new Intent();
            intent.setClass(curAct, toClass);
            curAct.startActivity(intent);
            curAct.overridePendingTransition(R.anim.ease_slide_left_in, R.anim.ease_slide_left_out);
        }
    }

    public static void startActivityNormalWithString(Class toClass, Activity curAct, String title) {

        if (toClass != null) {
            Intent intent = new Intent();
            intent.setClass(curAct, toClass);
            intent.putExtra("title", title);
            curAct.startActivity(intent);
            curAct.overridePendingTransition(R.anim.ease_slide_left_in, R.anim.ease_slide_left_out);
        }
    }

    public static void startActivityNormalWithString(Class toClass, Activity curAct, String title, String
            key) {

        if (toClass != null) {
            Intent intent = new Intent();
            intent.setClass(curAct, toClass);
            intent.putExtra(key, title);
            curAct.startActivity(intent);
            curAct.overridePendingTransition(R.anim.ease_slide_left_in, R.anim.ease_slide_left_out);
        }
    }

    public static void startActivityNormalWithStringForResult(Class toClass, Activity curAct, String title,
                                                              String key) {

        if (toClass != null) {
            Intent intent = new Intent();
            intent.setClass(curAct, toClass);
            intent.putExtra(key, title);
            curAct.startActivityForResult(intent, 0);
            curAct.overridePendingTransition(R.anim.ease_slide_left_in, R.anim.ease_slide_left_out);
        }
    }


    public static void startActivityNormalWithLong(Class toClass, Activity curAct, long mLong) {

        if (toClass != null) {
            Intent intent = new Intent();
            intent.setClass(curAct, toClass);
            intent.putExtra("mLong", mLong);
            curAct.startActivity(intent);
            curAct.overridePendingTransition(R.anim.ease_slide_left_in, R.anim.ease_slide_left_out);
        }
    }

    public static void startActivityNormalWithLong(Class toClass, Activity curAct, long mLong, String key) {

        if (toClass != null) {
            Intent intent = new Intent();
            intent.setClass(curAct, toClass);
            intent.putExtra(key, mLong);
            curAct.startActivity(intent);
            curAct.overridePendingTransition(R.anim.ease_slide_left_in, R.anim.ease_slide_left_out);
        }
    }

    /**
     * 用户app用户详情界面
     * @param isFromCircle  1:from circle 2:chat
     * */
    public static void startIMUserDetailInfo(long toUserId, boolean isNeedAdd, Activity currentAct,
                                             String title, int isFromCircle, String Tag) {
        Intent intent = new Intent("com.zxly.o2o.activity.IMUserDetailInfoActivity");
        intent.putExtra(EaseConstant.EXTRA_USER_ID, toUserId);
        intent.putExtra("title", title);
        intent.putExtra("isNeedAdd", isNeedAdd);
        intent.putExtra("isFromCircle", isFromCircle);
        if (Tag != null) {
            intent.putExtra("Tag", Tag);
        }
        EaseConstant.startActivity(intent, currentAct);
    }

    public static void startShopIMUserDetailInfo(long toUserId, Activity currentAct) {
        //商户改版一期前 跳转
//        Intent intent = new Intent("com.zxly.o2o.activity.IMUserDetailInfoAct");
//        intent.putExtra(EaseConstant.EXTRA_USER_ID, toUserId);
//        EaseConstant.startActivity(intent, currentAct);
        Intent intent = new Intent("com.zxly.o2o.activity.DetailPersonalAct");
        intent.putExtra("userId", toUserId);
        EaseConstant.startActivity(intent, currentAct);
    }

    public static void startEaseMultiPushActivity(Activity curAct,
                                                  int pushType, int multiPushOrSend) {
        Intent intent = new Intent("com.zxly.o2o.activity.IMMultiPushActivity");
        intent.putExtra("push_type", pushType);  //推送类型
        intent.putExtra("multiPushOrSend", multiPushOrSend);  //推送类型
        EaseConstant.startActivity(intent, curAct);
    }

    public static void startEaseMultiPushActivity(Activity curAct,
                                                  int pushType, int multiPushOrSend, String commission, boolean isRegistMembers, String registerTime) {
        Intent intent = new Intent("com.zxly.o2o.activity.IMMultiPushActivity");
        intent.putExtra("push_type", pushType);  //推送类型
        intent.putExtra("multiPushOrSend", multiPushOrSend);  //推送类型
        intent.putExtra("commission", commission);
        intent.putExtra("isRegistMembers", isRegistMembers);
        intent.putExtra("registerTime", registerTime);
        EaseConstant.startActivity(intent, curAct);
    }

    public static void startShopProduceActivity(Activity curAct, long id) {
        Intent intent = new Intent("com.zxly.o2o.activity.ProductInfoAct");
        intent.putExtra("bannerId", -1);
        intent.putExtra("produceId", id);  //商品id
        EaseConstant.startActivity(intent, curAct);
    }

    public static void startShowDefineGetuiAct(Activity curAct, int id,long dataId) {
        Intent intent = new Intent("com.zxly.o2o.activity.ShowDefineGetuiAct");
        intent.putExtra("id", id);
        intent.putExtra("dataId",dataId);
        EaseConstant.startActivity(intent, curAct);
    }

    public static void startOrderInfoActivity(Activity curAct, String orderNo) {
        Intent intent = new Intent("com.zxly.o2o.activity.MyOrderInfoAct");
        intent.putExtra("orderNumber", orderNo);  //订单号id
        EaseConstant.startActivity(intent, curAct);
    }

    public static void startInsureComlActivity(Activity curAct, long id) {
        Intent intent = new Intent("com.zxly.o2o.activity.InsureInfoComplementAct");
        intent.putExtra("id", id);  //订单号id
        EaseConstant.startActivity(intent, curAct);
    }

    public static void startInsureShopActivity(Activity curAct, long id,String title) {
        Intent intent = new Intent("com.zxly.o2o.shop.action.GUARANTEE_DETAIL");
        intent.putExtra("id",id+"");
        EaseConstant.startActivity(intent, curAct);
    }

    /*打开用户端的延保详情界面*/
    public static void startInsureUserActivity(Activity curAct, long id){
        Intent intent = new Intent("com.zxly.o2o.activity.InsuranceAct");
        intent.putExtra("orderId",id);
        intent.putExtra("type", 1);
        EaseConstant.startActivity(intent, curAct);
    }


    /*打开到商户端店优惠界面*/
    public static void startBenefitsActivity(Activity curAct){
        Intent intent = new Intent("com.zxly.o2o.activity.MyBenefitsAct");
        EaseConstant.startActivity(intent, curAct);
    }
    /*打开到用户端店优惠界面*/
    public static void startDiscountDetailAct(Activity curAct,long id){
        Intent intent = new Intent("com.zxly.o2o.activity.DiscountDetailAct");
        intent.putExtra("id",id);
        EaseConstant.startActivity(intent, curAct);
    }

    /*打开到用户端店优惠界面*/
//    public static void startBenefitsActivity(Activity curAct){
//        Intent intent = new Intent("com.zxly.o2o.activity.MyBenefitsAct");
//        EaseConstant.startActivity(intent, curAct);
//    }

    public static void startShowLocalBigImageViewActivity(Activity curAct, String imageUrl) {
        Intent intent = new Intent(curAct, EaseShowBigImageActivity.class);
        File file = new File(imageUrl);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            intent.putExtra("uri", uri);
        }
        //        else {
        //            // The local full size pic does not exist yet.
        //            // ShowBigImage needs to download it from the server
        //            // first
        //            intent.putExtra("secret", imgBody.getSecret());
        //            intent.putExtra("remotepath", imageUrl);
        //        }
        curAct.startActivity(intent);
    }

    /**
     * 注册提醒消息点击跳转至用户详情页面（自商户改版）
     * @param curAct
     * @param userId
     */
    public static void startOutLineFansDetailActivity(Activity curAct, long userId) {
        Intent intent = new Intent("com.zxly.o2o.shop.action.OUTLINE_FANSDETAIL");
        intent.putExtra("id", userId);
        EaseConstant.startActivity(intent, curAct);
    }

    /**
     * 格式化订单时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatOrderTime(Long time) {
        if (time == null || time == 0) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return formatter.format(date);
    }

    /**
     * 格式化订单时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatOrderNewTime(Long time) {
        if (time == null || time == 0) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        Date date = new Date(time);
        return formatter.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatBehaviorLongTime(Long time) {
        if (time == null || time == 0) {
            return "";
        }
        //        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(time);
        return formatter.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatOrderLongTime(Long time) {
        if (time == null || time == 0) {
            return "";
        }
        //        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return formatter.format(date);
    }

    public static void setImage(NetworkImageView imageView, String url, int defaultDrawable, View
            view) {
        //        imageView.setErrorImageResId(defaultDrawable);
        imageView.setImageUrl(null, null);
        imageView.setDefaultImageResId(defaultDrawable);
        if (url != null && !"".equals(url)) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageUrl(url, EaseUI.imageLoader);
        } else if (view == null) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    public static void setRefreshText(EasePullToRefreshListView mListView) {
        mListView.setMode(EasePullToRefreshBase.Mode.BOTH);
        mListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        mListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        mListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("加载中...");
        mListView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新");

    }


    public static String getShortTime(long createTime) {
        if (createTime != 0) {
            createTime = serverTime() - createTime;
            if (createTime < 0) {
                return "刚刚";
            }

            Long year = (createTime / (24 * 3600000 * 365l));
            if (year != 0) {
                return year + "年前";
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

    public static String getTimestampString(Date var0) {
        String var1;
        long var2 = var0.getTime();
        if (isSameDay(var2)) {
            Calendar var4 = GregorianCalendar.getInstance();
            var4.setTime(var0);
            int var5 = var4.get(11);
            if (var5 > 17) {
                var1 = "晚上 HH:mm";
            } else if (var5 >= 0 && var5 <= 6) {
                var1 = "凌晨 HH:mm";
            } else if (var5 > 11 && var5 <= 17) {
                var1 = "下午 HH:mm";
            } else {
                var1 = "上午 HH:mm";
            }
        } else if (isYesterday(var2)) {
            var1 = "昨天 HH:mm";
        } else {
            var1 = "M月d日 HH:mm";
        }

        return (new SimpleDateFormat(var1, Locale.CHINA)).format(var0);
    }

    private static boolean isSameDay(long var0) {
        TimeInfo var2 = DateUtils.getTodayStartAndEndTime();
        return var0 > var2.getStartTime() && var0 < var2.getEndTime();
    }

    private static boolean isYesterday(long var0) {
        TimeInfo var2 = DateUtils.getYesterdayStartAndEndTime();
        return var0 > var2.getStartTime() && var0 < var2.getEndTime();
    }

    public static long serverTime() {
        return System.currentTimeMillis() + timeOffset;
    }

    /**
     * 格式事件  今天显示今天 昨天显示昨天 一周内(判断标准是指格式化后的时间处于一年中的同一周并不是七天内)显示周几 其余显示年月日
     * @param time
     * @return
     */
    public static String getSimpleTime(long time){
        String timeStamp="";
        if(time!=0){
            if(isSameDay(time)){
                timeStamp="今天";
            }else if(isYesterday(time)){
                timeStamp="昨天";
//            }else if(isSameWeekDates(new Date(time),new Date(serverTime()))){
            }else if(getTwoDay(time)){
                timeStamp = getWeek(time);
            }else{
                timeStamp = formatOrderNewTime(time);
            }
        }
        return timeStamp;
    }

    /**
     * 判断二个时间是否在同一个周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR)==cal2.get(Calendar.WEEK_OF_YEAR)){
                return true;
            }
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    private static String getWeek(long timeStamp) {
        int mydate = 0;
        String week = null;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(Calendar.DAY_OF_WEEK);
        // 获取指定日期转换成星期几
        if (mydate == 1) {
            week = "周日";
        } else if (mydate == 2) {
            week = "周一";
        } else if (mydate == 3) {
            week = "周二";
        } else if (mydate == 4) {
            week = "周三";
        } else if (mydate == 5) {
            week = "周四";
        } else if (mydate == 6) {
            week = "周五";
        } else if (mydate == 7) {
            week = "周六";
        }
        return week;
    }


    /**
     * 得到二个日期间的间隔天数
     */
    public static boolean getTwoDay(long createTime) {
        /*SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
            if(day>0&&day<=7){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }*/

        GregorianCalendar calendar = new GregorianCalendar();
        if (createTime != 0) {
            createTime =  calendar.getTimeInMillis() - createTime;
            Long day = (createTime / (24 * 3600000l));
            if (day>0&&day<=7) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }

    }




}
