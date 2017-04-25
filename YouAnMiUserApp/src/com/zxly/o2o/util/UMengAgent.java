package com.zxly.o2o.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public class UMengAgent {

	/**门店首页icon**/
	public static  final  String tag_store_header_icon="tag_store_header_icon";
	/**门店首页限时抢**/
	public static  final  String tag_store_limit_sale="tag_store_limit_sale";
	/**门店首页限时抢商品**/
	public static  final  String tag_store_limit_sale_item="tag_store_limit_sale_item";
	/**门店首页清仓**/
	public static  final  String tag_store_clearance_sale="tag_store_clearance_sale";
	/**门店首页清仓商品**/
	public static  final  String tag_store_clearance_sale_item="tag_store_clearance_sale_item";
	/**门店首页新品**/
	public static  final  String tag_store_new_product="tag_store_new_product";
	/**门店首页最新商品**/
	public static  final  String tag_store_new_product_item="tag_store_new_product_item";
	
	/**门店首页（新手必看）**/
	public static  final  String tag_article_must_look="tag_article_must_look";
	/**门店首页（手机保养）**/
	public static  final  String tag_article_phone_takecare="tag_article_phone_takecare";
	/**门店首页（功能深挖）**/
	public static  final  String tag_article_function="tag_article_function";
	/**门店首页（论坛交流）**/
	public static  final  String tag_article_communicate="tag_article_communicate";	
	/**门店首页（文章item）**/
	public static  final  String tag_article_item="tag_article_item";
	
	
	/**店长直销**/
	public static  final  String product_home_dzzx="product_home_dzzx";
	/**二手寄卖**/
	public static  final  String product_home_esjm="product_home_esjm";
	/**系统消息**/
	public static final String personal_home_msg="personal_home_msg";
	/**我的登录**/
	public static final String personal_home_login="personal_home_login";
	/**预约单**/
	public static final String personal_home_order="personal_home_order";
	/**我的收藏**/
	public static final String personal_home_collect="personal_home_collect";
	/**我的帖子**/
	public static final String personal_home_topic="personal_home_topic";
	/**我的二手**/
	public static final String personal_home_second_hand="personal_home_second_hand";
	/**保修单**/
	public static final String personal_home_renew="personal_home_renew";
	/**店铺反馈**/
	public static final String personal_home_feedback="personal_home_feedback";
	/**我的头像**/
	public static final String personal_home_head="personal_home_head";
	/**设置**/
	public static final String personal_setting="personal_setting";
	/**设置收货地址**/
	public static final String personal_setting_address="personal_setting_address";
	/**设置检查更新**/
	public static final String personal_setting_update="personal_setting_update";
	/**设置退出**/
	public static final String personal_setting_logout="personal_setting_logout";
	/**修改头像**/
	public static final String personal_change_head="personal_change_head";
	/**修改昵称**/
	public static final String personal_change_nick="personal_change_nick";
	/**修改生日**/
	public static final String personal_change_birthday="personal_change_birthday";
	/**修改性别**/
	public static final String personal_change_sex="personal_change_sex";
	/**注册**/
	public static final String personal_register="personal_register";
	/**登录**/
	public static final String personal_login="personal_login";
	/**忘记密码**/
	public static final String personal_forget_password="personal_forget_password";
	/**修改密码**/
	public static final String personal_change_password="personal_change_password";
	/**圈子首页**/
	public static  final  String mycircle_page="mycircle_page";
	/**本店交流列表**/
	public static  final  String forumcommunity_page="forumcommunity_page";
	/**新手必看列表**/
	public static  final  String mustlook_page="mustlook_page";
	/**手机保养列表**/
	public static  final  String phone_upkeep="phone_upkeep";
	/**本店热文列表**/
	public static  final  String shop_hot_article="shop_hot_article";
	/**文章详情页面**/
	public static  final  String article_detail="article_detail";
	/**帖子详情页面**/
	public static  final  String topic_detail="topic_detail";
	/**帖子发布页面**/
	public static  final  String forumcommunity_publish_page="forumcommunity_publish_page";
	/**文章收藏列表**/
	public static  final  String collected_article_page="collected_article_page";
	/**我的发帖列表**/
	public static  final  String my_publish_topics_list_page="my_publish_topics_list_page";
	/**我的回帖列表**/
	public static  final  String my_rely_topics_list_page="my_rely_topics_list_page";
	/**我的二手发布列表**/
	public static  final  String my_publish_secondhand_list_page="my_publish_secondhand_list_page";
	
	
	/**collected_article_page
     *  上报友盟统计事件
     * @param context   Context
     * @param key       对应友盟中的事件ID(一一对应)
     */
    public static void onEvent(Context context, String key) {
        MobclickAgent.onEvent(context, key);
    }
	
}
