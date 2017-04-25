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
package com.easemob.chatuidemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    /**
     * 保存Preference的name
     */
    public static final String PREFERENCE_NAME = "saveInfo";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceManager mPreferencemManager;
    private static SharedPreferences.Editor editor;

    private String SHARED_KEY_SETTING_NOTIFICATION = "shared_key_setting_notification";
    private String SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound";
    private String SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate";
    private String SHARED_KEY_SETTING_SPEAKER = "shared_key_setting_speaker";

    private static String SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE = "shared_key_setting_chatroom_owner_leave";
    private static String SHARED_KEY_SETTING_GROUPS_SYNCED = "SHARED_KEY_SETTING_GROUPS_SYNCED";
    private static String SHARED_KEY_SETTING_CONTACT_SYNCED = "SHARED_KEY_SETTING_CONTACT_SYNCED";
    private static String SHARED_KEY_SETTING_BALCKLIST_SYNCED = "SHARED_KEY_SETTING_BALCKLIST_SYNCED";

    private static String SHARED_KEY_CURRENTUSER_USERNAME = "SHARED_KEY_CURRENTUSER_USERNAME";
    private static String SHARED_KEY_CURRENTUSER_NICK = "SHARED_KEY_CURRENTUSER_NICK";
    private static String SHARED_KEY_CURRENTUSER_AVATAR = "SHARED_KEY_CURRENTUSER_AVATAR";

    private static final String NOTIFY_LOGOUT = "notification_logout";
    private static final String NOTIFY_SLEEP = "notification_sleep";
    private static final String NOTIFY_ORDER = "notification_order";
    //	private static final String NOTIFY_PRODUCT = "notification_product";
    private static final String NOTIFY_SYSTEM = "notification_system";
    private static final String NOTIFY_FEEDBACK = "notification_feedback";
    private static final String TOP_CONTACTS = "top_contacts";
    private final String MISTIMING = "mistiming";

    private PreferenceManager(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt) {
        if (mPreferencemManager == null) {
            mPreferencemManager = new PreferenceManager(cxt);
        }
    }

    /**
     * 单例模式，获取instance实例
     *
     * @return
     */
    public synchronized static PreferenceManager getInstance() {
        if (mPreferencemManager == null) {
            throw new RuntimeException("please init first!");
        }

        return mPreferencemManager;
    }

    public void setSettingMsgNotification(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean);
        editor.commit();
    }

    public boolean getSettingMsgNotification() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_NOTIFICATION, true);
    }

    public void setSettingMsgSound(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean);
        editor.commit();
    }

    public boolean getSettingMsgSound() {

        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SOUND, true);
    }

    public void setSettingMsgVibrate(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean);
        editor.commit();
    }

    public boolean getSettingMsgVibrate() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_VIBRATE, true);
    }

    public void setSettingMsgSpeaker(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_SPEAKER, paramBoolean);
        editor.commit();
    }

    public boolean getSettingMsgSpeaker() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SPEAKER, true);
    }

    public void setSettingAllowChatroomOwnerLeave(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE, value);
        editor.commit();
    }

    public boolean getSettingAllowChatroomOwnerLeave() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE, true);
    }

    public void setGroupsSynced(boolean synced) {
        editor.putBoolean(SHARED_KEY_SETTING_GROUPS_SYNCED, synced);
        editor.commit();
    }

    public boolean isGroupsSynced() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_GROUPS_SYNCED, false);
    }

    public void setContactSynced(boolean synced) {
        editor.putBoolean(SHARED_KEY_SETTING_CONTACT_SYNCED, synced);
        editor.commit();
    }

    public boolean isContactSynced() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_CONTACT_SYNCED, false);
    }

    public void setBlacklistSynced(boolean synced) {
        editor.putBoolean(SHARED_KEY_SETTING_BALCKLIST_SYNCED, synced);
        editor.commit();
    }

    public boolean isBacklistSynced() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_BALCKLIST_SYNCED, false);
    }

    public void setCurrentUserNick(String nick) {
        editor.putString(SHARED_KEY_CURRENTUSER_NICK, nick);
        editor.commit();
    }

    public void setCurrentUserAvatar(String avatar) {
        editor.putString(SHARED_KEY_CURRENTUSER_AVATAR, avatar);
        editor.commit();
    }

    public String getCurrentUserNick() {
        return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_NICK, null);
    }

    public String getCurrentUserAvatar() {
        return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_AVATAR, null);
    }

    public void setCurrentUserName(String username) {
        editor.putString(SHARED_KEY_CURRENTUSER_USERNAME, username);
        editor.commit();
    }

    public String getCurrentUsername() {
        return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_USERNAME, null);
    }


    public void removeCurrentUserInfo() {
        editor.remove(SHARED_KEY_CURRENTUSER_NICK);
        editor.remove(SHARED_KEY_CURRENTUSER_AVATAR);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public void putString(String key, String value){
        editor.putString(key,value).commit();
    }

    public String getString(String key){
      return   mSharedPreferences.getString(key,"");
    }


    public void setNotifyLogout(boolean flag) {
        putBoolean(NOTIFY_LOGOUT, flag);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 退出后仍接收消息通知
     * @return
     */
    public boolean getNotifyLogout() {
        return getBoolean(NOTIFY_LOGOUT, true);
    }

    public void setNotifySleep(boolean flag) {
        putBoolean(NOTIFY_SLEEP, flag);
    }

    /**
     * 夜间防骚扰模式
     * @return
     */
    public boolean getNotifySleep() {
        return getBoolean(NOTIFY_SLEEP, true);
    }

    public void setNotifyOrder(boolean flag) {
        putBoolean(NOTIFY_ORDER, flag);
    }

    /**
     * 接收订单消息提醒
     * @return
     */
    public boolean getNotifyOrder() {
        return getBoolean(NOTIFY_ORDER, true);
    }

    //	public void setNotifyProduct(boolean flag) {
    //		putBoolean(NOTIFY_PRODUCT, flag);
    //	}

    /**
     * 接收商品消息提醒
     * @return
     */
    //	public boolean getNotifyProduct() {
    //		return getBoolean(NOTIFY_PRODUCT, true);
    //	}
    public void setNotifySystem(boolean flag) {
        putBoolean(NOTIFY_SYSTEM, flag);
    }

    /**
     * 接收系统消息提醒
     * @return
     */
    public boolean getNotifySystem() {
        return getBoolean(NOTIFY_SYSTEM, true);
    }

    public void setNotifyFeedback(boolean flag) {
        putBoolean(NOTIFY_FEEDBACK, flag);
    }

    /**
     * 接收店铺反馈消息提醒
     * @return
     */
    public boolean getNotifyFeedback() {
        return getBoolean(NOTIFY_FEEDBACK, true);
    }

    public void setMistiming(long flag) {
        putLong(NOTIFY_FEEDBACK, flag);
    }

    /**
     * 接收店铺反馈消息提醒
     * @return
     */
    public long getMistiming() {
        return getLong(NOTIFY_FEEDBACK, 0);
    }


    /**
    * 商户置顶联系人
    * */
    public String getTopContacts() {
        return getString(TOP_CONTACTS);
    }

    public void saveTopContacts(String valus){
        putString(TOP_CONTACTS,valus);
    }

    public void cleanTopContacts(){
        putString(TOP_CONTACTS,"");
    }
}
