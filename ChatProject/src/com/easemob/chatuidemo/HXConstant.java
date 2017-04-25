/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo;

import com.easemob.easeui.EaseConstant;

public class HXConstant extends EaseConstant{
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String CHAT_ROOM = "item_chatroom";
	public static final String ACCOUNT_REMOVED = "account_removed";
	public static final String ACCOUNT_CONFLICT = "conflict";
	public static final String CHAT_ROBOT = "item_robots";
	public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
	public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
	public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";
	public static boolean isLoginSuccess=false;
	public static final String SYS_MSG="_push";
	public static final String CMD_ACTION_NEWFRIEND ="addNewFriends";
	public static final String CMD_ACTION_TUWEN ="tuWenMsg";
	public static final String CMD_ACTION_SYSTEM ="sysMsg";
	public static final String CUSTOM_MSG="what";
	public static final String TAG_SHOP="_1";  //商户端的聊天id后缀
	public static final String TAG_USER="_2";  //用户端的聊天id后缀
	public static final String SYS_TUWEN_PUSH="msg_tuwen_push";   //业务员广告推送用户id
	public static final String SYS_TUWEN_PUSH_TYPE="msg_tuwen_push_type";   //业务员广告推送类型
	public static final int SYS_TUWEN_PUBLISH=99;   //业务员群发消息带的key==what
	public static final int PUSH_SHOP_INFO=98;   //业务员群发消息带的key==what
	public static final int PUSH_ARTICLE=97;   //业务员群发消息带的key==what
	public static final int PUSH_CAMPAIGN=96;   //业务员群发消息带的key==what
	public static final int PUSH_TXT=95;   //业务员群发消息带的key==what
	public static final int MUL_SEND=1;   //群发消息
	public static final int MUL_PUSH=2;   //推送广告
	public static final String WELCOME="$welcome$";   //推送广告
	public static final String GOTO_DETAIL="go_to_detail";   //是否直接进入系统消息详情

	/***IM_user*/
	public static final String SYS_NOTIC_USER ="user_yannotice_push";  //柚安米公告
	public static final int SYS_SHOPINFO_KEY_USER =100;  //门店快讯
	public static final int SYS_ORDER_KEY_USER =200;  //订单推送消息
	public static final int SYS_MAINTAIN_KEY_USER =300;  //保单推送消息
	public static final int SYS_NOTIC_KEY_USER =400;  //柚安米公告
	public static final int SYS_ACTIVITY_KEY_USER =500;  //门店活动
	public static final int SYS_INSURE_KEY_USER =600;  //延保订单
	public static final int SYS_BENEFIT_KEY_USER =800;  //优惠消息
	public static final int SYS_ARTICLE_KEY_USER =700;//个推文章（由商户推送，归为门店快讯）
	public static final int SYS_YAMARTICLE_KEY_USER =701;//运营文章（运营后台推送，归为门店快讯）个推新增
	public static final int SYS_SHOPPRODUCT_KEY_USER=900;//个推商品（归为门店快讯）
	public static final int SYS_SHOPNOTICE_KEY_USER=1000;//门店公告-个推新增（仅用户app中有）
	public static final int SYS_SHOPDEFINE_KEY_USER=1100;//商户改版--自定义推送内容
	//个推四中消息类型（此四种类型数据都从服务端获取）
	public static final int GETUI_SHOP_INFO=0;//门店快讯
	public static final int GETUI_SHOP_NOTICE=2;//门店公告
	public static final int GETUI_SHOP_ACTIVITY=3;//门店活动
	public static final int GETUI_YAM_NOTICE=4;//柚安米公告

	/***IM_shop*/
	public static final String SYS_REGIST_SHOP ="shop_userregisted_push";  //会员注册
	public static final String SYS_NOTIC_SHOP ="shop_yamnotice_push";  //柚安米公告
	public static final String GROUP_VIP="门店会员";
	public static final String GROUP_UNVIP="未注册会员";
	public static final String GROUP_BOSS="老板";
	public static final int SYS_REGIST_KEY_SHOP =100;  //会员注册
	public static final int SYS_NOTIC_KEY_SHOP =200;  //柚安米公告
	public static final int SYS_FEEDBACK_KEY_SHOP =400;  //反馈
	public static final int SYS_ORDER_KEY_SHOP =500;  //订单推送消息
	public static final int SYS_MAINTAIN_KEY_SHOP =600;  //保单推送消息
	public static final int SYS_FUND_RECORD =700;  //资金记录推送消息
	public static final int SYS_INSURE_KEY_SHOP =900;  //延保推送消息
	public static final int SYS_BENEFIT_KEY_SHOP =1000;  //优惠推送消息
	/*public static final String SYS_REGIST_KEY_SHOP="shop_userregisted_push";  //会员注册
	public static final String SYS_NOTIC_KEY_SHOP="shop_yamnotice_push";  //柚安米公告
	public static final String SYS_FEEDBACK_KEY_SHOP="shop_feedback_push";  //反馈
	public static final String SYS_ORDER_KEY_SHOP="shop_order_push";  //订单推送消息
	public static final String SYS_MAINTAIN_KEY_SHOP="shop_maintain_push";  //保单推送消息*/
}
