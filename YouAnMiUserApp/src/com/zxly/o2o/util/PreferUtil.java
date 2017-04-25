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

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.model.User;

public final class PreferUtil {

    public static PreferUtil INSTANCE;
    private static SharedPreferences mPrefer;
    private static final String APP_NAME = "com.zxly.o2o.sharedPreferences";
    private static final String LOGIN_USER = "login_user";
    private static final String LOGIN_TOKEN = "login_token";

    private static final String TOPIC_BV = "Topicbv";
    private static final String ARTICLE_BV = "Articlebv";
    private static final String ARTICLEREPLY_BV = "ArticleReplybv";
    private static final String MESSAGE_ID = "message_id";
    private static final String MESSAGE_TIME = "message_time";
    private static final String DATE_STAMP = "date_stamp";
    private static final String HX_ISDEFAULT = "hx_isdefault";    //0:default 1:正式
    private static final String RENEW_GUIDE = "renew_guide"; //是否在我的保修单后面显示红点提醒
    private static final String MSG_PUSH = "msg_push"; //接受系统推送消息
    private  final String FORUM_TOP_LIST="forum_top_list"; //圈子置顶列表
    private  final String FORUM_GUIDE_ISSHOW="forum_guide_isshow"; //圈子置顶列表
    private static final String DISCOUNT_GUIDE = "discount_share";//我的优惠分享引导

    private PreferUtil() {
    }

    public static PreferUtil getInstance() {
        if (INSTANCE == null) {
            return new PreferUtil();
        }
        return INSTANCE;
    }

    public void init(Context ctx) {
        mPrefer = ctx.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
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
        mPrefer.edit().putString(key, value).apply();
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

    public User getLoginUser() {
        String userStr = getString(LOGIN_USER, null);
        if (userStr == null)
            return null;
        try {
            return GsonParser.getInstance().getBean(userStr, User.class);
        } catch (AppException e) {
            return null;
        }
    }

    public void setLoginToken(String tokenStr) {
        putString(LOGIN_TOKEN, tokenStr);
    }

    public int getHXAccount() {
        return getInt(HX_ISDEFAULT, -1);
    }

    public void setHXAccount(int hx_isdefault) {
        putInt(HX_ISDEFAULT, hx_isdefault);
    }


    public void setLastUpdateApkTime(long time )
    {
        putLong("lastUpdateApkTime", time);
    }
    public long getLastUpdateApkTime()
    {
        return  getLong("lastUpdateApkTime",0);
    }
    public void setFlagOldAppDel(boolean isDel)
    {
        putBoolean("delOldApp", isDel);
    }
    public boolean isDelOldApp()
    {
        return  getBoolean("delOldApp", false);
    }
    public void setDeviceAdminActive(int isAdminActive)
    {
        putInt("adminActive", isAdminActive);
    }

    /***
     *
     * @return 0:未弹出激活界面 1:已经强制弹出激活  2:已经激活
     */
    public int getDeviceAdminActive()
    {
        return  getInt("adminActive", 0);
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

    public long getMsgId() {
        return getLong(MESSAGE_ID, 0);
    }

    public void setMsgId(long msgId) {
        putLong(MESSAGE_ID, msgId);
    }

    public long getMsgCreateTime() {
        return getLong(MESSAGE_TIME, 0);
    }

    public void setMsgCreateTime(long createTime) {
        putLong(MESSAGE_TIME, createTime);
    }

    public String getDateStamp() {
        return getString(DATE_STAMP, "");
    }

    public void setDateStamp(String date) {
        putString(DATE_STAMP, date);
    }

    public boolean getIsFirstOpen(){
        boolean isInit=getBoolean("isInit",true);
        return isInit;
    }

    public boolean getIsFirstOpenMakeCommission(){
        boolean isInit=getBoolean("isInitCommission",true);
        return isInit;
    }

    public void setH5VersionCode(int h5VersionCode){
        putInt("H5_VERSION_CODE",h5VersionCode);
    }

    public long getShopId() {
        return getLong("SHOP_ID", 0);
    }

    public void setShopId(long shopId) {
        putLong("SHOP_ID", shopId);
    }
    public void setUserId(long userId)
    {
        putLong("userId",userId);
    }
    public long getUserId()
    {
        return getLong("userId",0);
    }


    public int getH5VersionCode(){
        return getInt("H5_VERSION_CODE",0);
    }

    public void setH5StyleId(int H5StyleId){
        putInt("H5_STYLE_ID",H5StyleId);
    }


    public int getH5StyleId(){
        return  getInt("H5_STYLE_ID",0);
    }

    public void setMakeCommissionInit(){
        putBoolean("isInitCommission",false);
    }

    public void setAppHasOpen(){
        putBoolean("isInit",false);
    }

    public boolean getIsShowRenewGuide(){
        return getBoolean(RENEW_GUIDE, true);
    }

    public void setRenewGuideHasOpen(){
        putBoolean(RENEW_GUIDE, false);
    }

    public boolean getIsShowDiscountGuide() {
        return getBoolean(DISCOUNT_GUIDE, true);
    }

    public void setDiscountGuideHasOpen() {
        putBoolean(DISCOUNT_GUIDE, false);
    }

    /**
     * 接收系统推送消息
     *
     * @return
     */
    public boolean getMsgPush() {
        return getBoolean(MSG_PUSH, true);
    }

    public void setMsgPush(boolean isPushMsg) {
        putBoolean(MSG_PUSH, isPushMsg);
    }

    public void setForumTopList(String list){
        putString(FORUM_TOP_LIST, list);
    }

    public String getForumTopList(){
        return getString(FORUM_TOP_LIST,"");
    }

    public boolean getForumGuideIsShow() {
        return  getBoolean(FORUM_GUIDE_ISSHOW,false);
    }

    public void setForumGuideIsShow(boolean isShow){
        putBoolean(FORUM_GUIDE_ISSHOW, isShow);
    }
}
