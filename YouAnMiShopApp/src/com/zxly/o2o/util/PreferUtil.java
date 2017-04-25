/**
 * Copyright(C)2012-2013 深圳市掌星立意科技有限公司版权所有
 * 创 建 人:Jacky
 * 修 改 人:
 * 创 建日期:2013-7-25
 * 描    述:xml储存数据
 * 版 本 号:
 */
package com.zxly.o2o.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.easemob.easeui.EaseConstant;


public final class PreferUtil {

    public static PreferUtil INSTANCE;
    private static SharedPreferences mPrefer;
    private static final String APP_NAME = "com.zxly.o2o.sharedPreferences";
    private static final String LOGIN_USER = "login_user";
    private static final String LOGIN_TOKEN = "login_token";
    private static final String TOPIC_BV = "Topicbv";
    private static final String ARTICLE_BV = "Articlebv";
    private static final String ARTICLEREPLY_BV = "ArticleReplybv";
    private static final String HOME_MAINTAIN = "homepage_maintain";

    private static final String NOTIFY_ALL = "notification_all";
    private static final String NOTIFY_LOGOUT = "notification_logout";
    private static final String NOTIFY_SLEEP = "notification_sleep";
    private static final String NOTIFY_ORDER = "notification_order";
    private static final String NOTIFY_MONEY = "notification_money";
    private static final String NOTIFY_SYSTEM = "notification_system";
    private static final String NOTIFY_FEEDBACK = "notification_feedback";
    private static final String CONTACT_UPDATE = "contact_update";

    private PreferUtil() {
    }

    public static PreferUtil getInstance() {
        if (INSTANCE == null) {
            return new PreferUtil();
        }
        return INSTANCE;
    }

    public void init(Context ctx) {
        mPrefer = ctx.getSharedPreferences(APP_NAME, Context.MODE_WORLD_READABLE
                | Context.MODE_WORLD_WRITEABLE);
        mPrefer.edit().commit();
    }


    public String getString(String key, String defValue) {
        return mPrefer.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mPrefer.getInt(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPrefer.getBoolean(key, defValue);
    }

    public void putString(String key, String value) {
        mPrefer.edit().putString(key, value).commit();
    }

    public void putInt(String key, int value) {
        mPrefer.edit().putInt(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        mPrefer.edit().putBoolean(key, value).commit();
    }

    public void putLong(String key, long value) {
        mPrefer.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return mPrefer.getLong(key, defValue);
    }

    public void removeKey(String key) {
        mPrefer.edit().remove(key).commit();
    }

    public void setLoginUser(String userStr) {
        putString(LOGIN_USER, userStr);
    }


    public void setLoginToken(String tokenStr) {
        putString(LOGIN_TOKEN, tokenStr);
    }

    public String getLoginToken() {
        return getString(LOGIN_TOKEN, "");
    }


    public String getMyCircleTopicBV() {
        return getString(TOPIC_BV, "");
    }

    public void setMyCircleTopicBV(String tokenStr) {
        putString(TOPIC_BV, tokenStr);
    }

    public String getMyCircleArticleBV() {
        return getString(ARTICLE_BV, "");
    }

    public void setMyCircleArticleBV(String tokenStr) {
        putString(ARTICLE_BV, tokenStr);
    }

    public String getMyCircleArticleReplyBV() {
        return getString(ARTICLEREPLY_BV, "");
    }

    public void setMyCircleArticleReplyBV(String tokenStr) {
        putString(ARTICLEREPLY_BV, tokenStr);
    }

    public String getHomeMaintain() {
        return getString(HOME_MAINTAIN, "");
    }

    public void setHomeMaintain(String homeMaintain) {
        putString(HOME_MAINTAIN, homeMaintain);
    }


    /**
     * 接收消息通知
     *
     * @return
     */
    public boolean getNotifyAll() {
        return getBoolean(NOTIFY_ALL, true);
    }

    public void setNotifyAll(boolean flag) {
        putBoolean(NOTIFY_ALL, flag);
    }


    /**
     * 退出后仍接收消息通知
     *
     * @return
     */
    public boolean getNotifyLogout() {
        return getBoolean(NOTIFY_LOGOUT, true);
    }

    public void setNotifyLogout(boolean flag) {
        putBoolean(NOTIFY_LOGOUT, flag);
    }

    /**
     * 夜间防骚扰模式
     *
     * @return
     */
    public boolean getNotifySleep() {
        return getBoolean(NOTIFY_SLEEP, true);
    }

    public void setNotifySleep(boolean flag) {
        putBoolean(NOTIFY_SLEEP, flag);
    }

    /**
     * 接收订单消息提醒
     *
     * @return
     */
    public boolean getNotifyOrder() {
        return getBoolean(NOTIFY_ORDER, true);
    }

    public void setNotifyOrder(boolean flag) {
        putBoolean(NOTIFY_ORDER, flag);
    }

    /**
     * 接收资金消息提醒
     *
     * @return
     */
    public boolean getNotifyMoney() {
        return getBoolean(NOTIFY_MONEY, true);
    }

    public void setNotifyMoney(boolean flag) {
        putBoolean(NOTIFY_MONEY, flag);
    }

    /**
     * 接收系统消息提醒
     *
     * @return
     */
    public boolean getNotifySystem() {
        return getBoolean(NOTIFY_SYSTEM, true);
    }

    public void setNotifySystem(boolean flag) {
        putBoolean(NOTIFY_SYSTEM, flag);
    }

    /**
     * 接收店铺反馈消息提醒
     *
     * @return
     */
    public boolean getNotifyFeedback() {
        return getBoolean(NOTIFY_FEEDBACK, true);
    }

    public void setNotifyFeedback(boolean flag) {
        putBoolean(NOTIFY_FEEDBACK, flag);
    }


    /**
     * 商户端联系人列表更新日期
     * */
    public long getContactUpdateTime() {
        return getLong(CONTACT_UPDATE, 0);
    }

    public void setContactUpdateTime(long flag) {
        putLong(CONTACT_UPDATE, flag);
    }


    public boolean getIsFirstOpen() {
        boolean isInit = getBoolean("isInit", true);
        putBoolean("isInit", false);
        return isInit;
    }
//第一次打开粉丝页面（首页）
    public boolean getIsFirstOpenFans() {
        boolean isFirstOpenFans = getBoolean("isFirstOpenFans", true);
        putBoolean("isFirstOpenFans", false);
        return isFirstOpenFans;
    }

    public void setIsFirstOpenFans(){
        putBoolean("isFirstOpenFans", false);
    }


//第一次打开粉丝详情页面
    public boolean getIsFirstOpenFansDetail() {
        boolean isFirstOpenFans = getBoolean("isFirstOpenFansdetail", true);
        putBoolean("isFirstOpenFansdetail", false);
        return isFirstOpenFans;
    }

    //第一次打开会员详情页面
    public boolean getIsFirstOpenMenbersDetail() {
        boolean isFirstOpenFans = getBoolean("isFirstOpenMembersdetail", true);
        putBoolean("isFirstOpenMembersdetail", false);
        return isFirstOpenFans;
    }

    public boolean getIsFirstOpenApp() {
        boolean isFirstOpenApp = getBoolean("isFirstOpenApp", true);
        return isFirstOpenApp;
    }

    public void setShopAppHasOpen(){
        putBoolean("isFirstOpenApp",false);
    }
}
