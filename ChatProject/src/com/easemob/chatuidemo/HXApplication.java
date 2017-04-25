/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
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
package com.easemob.chatuidemo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.GeTuiConversation;
import com.easemob.exceptions.EaseMobException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class HXApplication {

    public static Context applicationContext;
    private static HXApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    public int unReadMsgCount;  //统计未读消息数量
    public int registMsg, notic;  //未读：注册、资讯、推荐
    public String mMessageId = "";
    public List<GeTuiConversation> getuiMsgList=new ArrayList<GeTuiConversation>();
    public int getuiUndreadcount;//个推未读消息数量

    public int getGetuiUndreadcount() {
        return getuiUndreadcount;
    }

    public void setGetuiUndreadcount(int getuiUndreadcount) {
        this.getuiUndreadcount = getuiUndreadcount;
    }

    public List<GeTuiConversation> getGetuiMsgList() {
        return getuiMsgList;
    }

    public void setGetuiMsgList(List<GeTuiConversation> getuiMsgList) {
        this.getuiMsgList = getuiMsgList;
    }

    private HXConfigInfo hxConfigInfo;

    public void onCreate(Context context,int sslRawId) {
        applicationContext = context;
        instance = this;
        //个推
//        GetuiApplication.getInstance().onCreate(applicationContext);

        //init demo helper
        HXHelper.getInstance().init(applicationContext,sslRawId);
        unReadMsgCount = getUnreadMsgCountTotal();
        initHXInfo();
    }

    public static HXApplication getInstance() {
        if (instance == null) {
            instance = new HXApplication();
        }
        return instance;
    }

    public String parseUserFromID(long id, String type) {
        return new StringBuffer(String.valueOf(Math.abs(EaseConstant.shopID))).append("_")
                .append(id).append(type).toString();
    }

    public long parseIdFromName(String name) {
        if(name.contains("_")) {
            return Long.valueOf(name.substring(name.indexOf("_") + 1,
                    name.lastIndexOf("_")));
        }
        return 0;
    }

    public String getUnRegistUserName(String imei) {
        return new StringBuffer(String.valueOf(Math.abs(EaseConstant.shopID))).append("_")
                .append(imei).toString();
    }


    /**
     * 设置柚安米消息未读数
     */
    private void setShopMsgUnReadSize(EMConversation conversation) {
        try {
            if (conversation.getMessage(0).getIntAttribute("what") ==
                    HXConstant.SYS_REGIST_KEY_SHOP) {  //注册消息
                registMsg = conversation.getUnreadMsgCount();
            } else if (conversation.getMessage(0).getIntAttribute("what") ==
                    HXConstant.SYS_NOTIC_KEY_SHOP) {  //柚安米公告
                notic = conversation.getUnreadMsgCount();
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
    }

    /**
     * 累加柚安米消息未读数
     */
    public void addShopMsgUnReadSize(EMMessage message) {
        try {
            if (message != null) {
                if (message.getIntAttribute("what") == HXConstant.SYS_REGIST_KEY_SHOP) {  //注册消息
                    registMsg++;
                } else if (message.getIntAttribute("what") == HXConstant.SYS_NOTIC_KEY_SHOP) {  //柚安米公告
                    notic++;
                }
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;

        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance()
                .getAllConversations();

        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
            } else if (conversation.getType() == EMConversation.EMConversationType.Chat) {
                setShopMsgUnReadSize(conversation);
            }
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    /**
     * 获取未读会员注册消息数
     *
     * @return
     */
    public int getUnreadRegistMsgCount(Hashtable<String, EMConversation> conversations) {
        int registMsg = 0;
        for (EMConversation conversation : conversations
                .values()) {
            if (conversation.getType() == EMConversation.EMConversationType.Chat) {
                try {
                    if (conversation.getMessage(0).getIntAttribute("what") ==
                            HXConstant.SYS_REGIST_KEY_SHOP) {  //注册消息
                        registMsg = conversation.getUnreadMsgCount();
                        return registMsg;
                    }
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }
        return registMsg;
    }

    public void deleteAllConversations(boolean isDeleteMsg) {
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance()
                .getAllConversations();

        if (isDeleteMsg) {
            EMChatManager.getInstance().deleteAllConversation();
            //            synchronized (conversations) {
            //                Enumeration key = conversations.keys();
            //
            //                while (key.hasMoreElements()) {
            //                    EMChatManager.getInstance().clearConversation(key.nextElement().toString());
            //                }
            //
            //            }
        } else {
            synchronized (conversations) {
                Enumeration var2 = conversations.keys();

                while (var2.hasMoreElements()) {
                    String var3 = (String) var2.nextElement();
                    //                    InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
                    //                    inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());

                    EMChatManager.getInstance().deleteConversation(var3, false, false);
                }

            }

        }
    }


    public HXConfigInfo getHXConfigInfo(){
        return hxConfigInfo;
    }

    private void initHXInfo(){
        try {
            hxConfigInfo=new HXConfigInfo();
            Context context= HXApplication.applicationContext;
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String HXkey=appInfo.metaData.getString("EASEMOB_APPKEY");
            if("youanmitest02#o2odev".equals(HXkey)){
                /*开发环境*/
                hxConfigInfo.clientID = "YXA69CQNgDaCEeW4lOsVr5X_9A";
                hxConfigInfo.clientSecret = "YXA6GWNTC__hBZRmYFuYLCv5f0hG8AY";
                hxConfigInfo.orgName = "youanmitest02";
                hxConfigInfo.appName = "o2odev";
            }else if("youanmi-test#hxtestdemo".equals(HXkey)){
                /*测试环境*/
                hxConfigInfo.clientID = "YXA6TvShcCaxEeW2IE9_eiO8CA";
                hxConfigInfo.clientSecret = "YXA6wzp2aBzvN2IiA7-Sc7av2pSXLS4";
                hxConfigInfo.orgName = "youanmi-test";
                hxConfigInfo.appName = "hxtestdemo";
            }else if ("youanmi#youanmio2ouser".equals(HXkey)){
                /*正式环境*/
                hxConfigInfo.clientID = "YXA610ICgBDpEeWRfMdDiQgnIA";
                hxConfigInfo.clientSecret= "YXA6_X4unpjUJVSVQlPK1GcALo44IiY";
                hxConfigInfo.orgName = "youanmi";
                hxConfigInfo.appName = "youanmio2ouser";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

}
