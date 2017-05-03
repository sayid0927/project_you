package com.zxly.o2o.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by zl on 2017/4/10.
 */


/// 用户行为分析

public class UmengUtil {

    private  static  UmengUtil umengUtil;

    public String FIND_BACK_CLICK="find_back_click";                                 //主页 - 找客 / “找客”  点击“返回”
    public String FIND_REFRESH="find_refresh";                                       //主页 - 找客 / “找客”  下拉刷新
    public String FIND_UPLOAD="find_upload";                                         //主页 - 找客 / “找客”     上划加载
    public String FIND_HOTTAB_CLICK="find_hottab_click";                             //主页 - 找客 / “找客”  点击网络热文
    public String FIND_CUSTOM_PASTE_CLICK="find_custom_paste_click";                 //主页 - 找客 / “找客”  自定义文章点击“粘贴链接”
    public String FIND_ARTICLE_LISTSHARE_CLICK="find_article_listshare_click";       //  主页 - 找客 / “找客”  点击列表文章/活动“推广”
    public String FIND_ARTICLE_CLICK="find_article_click";                           //主页 - 找客 / “找客”  点击列表文章/活动进详情页
    public String FIND_ARTICLE_ENTER="find_article_enter";                           //主主页 - 找客 - 文章/活动详情页 进入详情页
    public String FIND_ARTICLE_DETAILSHARE_CLICK="find_article_detailshare_click";   //  主页 - 找客 / “找客”  详情页点击“分享”

    public String FIND_SHAREBT_CLICK="find_sharebt_click";                           //主页 - 找客 - 文章/活动详情页 分享弹框点击
    public String FIND_SHARE_APP_CLICK="find_share_app_click";                       //主页 - 找客 - 扫码链接推广  点击“推广门店APP链接”
    public String FIND_SHAREBT_APP_CLICK="find_sharebt_app_click";                   //主页 - 找客 - 扫码链接推广  分享弹框点击
    public String FIND_SHARE_H5_CLICK="find_share_h5_click";                         //主页 - 找客 - 扫码链接推广  点击“分享H5网店链接”
    public String FIND_SHAREBT_H5_CLICK="find_sharebt_h5_click";                     //主页 - 找客 - 扫码链接推广  分享弹框点击


    public String SEARCH_ENTER="search_enter";                     //主页 - 搜索  进入“搜索”页面
    public String SEARCH_BACK_CLICK="search_back_click";           //主页 - 搜索  点击“返回”
    public String SEARCH_BT_CLICK="search_bt_click";               //主页 - 搜索  点击“搜索”


    public String FANS_OFFLINEFANS_CLICK="fans_offlinefans_click"; //主页 - 店铺粉丝  点击"线下录入粉丝"
    public String FANS_ADDFANS_CLICK="fans_addfans_click";         //主页 - 店铺粉丝  点击线下录入粉丝 - “新增”


    public String FANS_FANS_SAVE_CLICK="fans_fans_save_click";  //主页 - 店铺粉丝 - 新增线下粉丝 点击“保存”
    public String FANS_FANS_SAVE_SUC="fans_fans_save_suc";      //主页 - 店铺粉丝 - 新增线下粉丝 保存成功
    public String FANS_FANS_SAVE_FAIL="fans_fans_save_fail";      //主页 - 店铺粉丝 - 新增线下粉丝 保存失败

    public String FANS_FANS_PUSH_CLICK="fans_fans_push_click";   //主页 - 店铺粉丝 点击“推送”
    public String FANS_FANS_FOLLOW_CLICK="fans_fans_follow_click";   //主页 - 店铺粉丝 点击“关注”
    public String FANS_FANS_UNFOLLOW_CLICK="fans_fans_unfollow_click";   //主页 - 店铺粉丝 点击“取消关注”
    public String FANS_NEWFANS_CLICK="fans_newfans_click";   //主页 - 店铺粉丝 点击"新粉丝"
    public String FANS_STARFANS_CLICK="fans_starfans_click";   //主页 - 店铺粉丝 点击"我关注的粉丝"
    public String HOME_NEWGROUP_CLICK="home_newgroup_click";   //主页 - 我的会员 点击“新建组”
    public String HOME_NEWGROUP_SAVE_CLICK="home_newgroup_save_click";   //主页 - 我的会员 - 新增会员分组 点击“保存”
    public String HOME_NEWGROUP_SAVE_SUC="home_newgroup_save_suc";   //主页 - 我的会员 - 新增会员分组 点击“保存成功”
    public String HOME_NEWGROUP_SAVE_FAIL="home_newgroup_save_fail";   //主页 - 我的会员 - 新增会员分组 点击“保存失败”
    public String MEMBER_PHONE_CLICK="member_phone_click";   //主页 - 我的会员 - 新增会员分组 点击“按机型分组”
    public String MEMBER_ACTION_CLICK="member_action_click";   //主页 - 我的会员 - 新增会员分组 点击“按机型分组”
    public String MEMBER_GROUP_CLICK="member_group_click";   //主页 - 我的会员 - 新增会员分组 点击“按机型分组”
    public String MANAGE_BALANCE_CLICK="manage_balance_click";   //主页 - 我的会员 - 新增会员分组 点击“账户余额”
    public String MANAGE_WITHDRAW_CLICK="manage_withdraw_click";   //主页 - 我的会员 - 新增会员分组 点击“提现”
    public String MANAGE_BILL_CLICK="manage_bill_click";   //主页 - 我的会员 - 新增会员分组 点击“账单明细”
    public String MANAGE_MONTH_CLICK="manage_month_click";   //主页 - 我的会员 - 新增会员分组 点击“月份”
    public String MANAGE_LASTMONTH_CLICK="manage_lastmonth_click";   //主页 - 我的会员 - 新增会员分组 点击“上个月”
    public String MANAGE_NEXTMONTH_CLICK="manage_nextmonth_click";   //主页 - 我的会员 - 新增会员分组 点击“上个月”
    public String MANAGE_ALLORDER_CLICK="manage_allorder_click";   //主页 - 我的会员 - 新增会员分组 点击“全部订单”
    public String MANAGE_CHECKING_CLICK="manage_checking_click";   //主页 - 我的会员 - 新增会员分组 点击“待发货”
    public String MANAGE_DELIVERING_CLICK="manage_delivering_click";   //主页 - 我的会员 - 新增会员分组 点击“待收货”
    public String MANAGE_REFUND_CLICK="manage_refund_click";   //主页 - 我的会员 - 新增会员分组 点击“退款”
    public String MANAGE_SALESRANKING_CLICK="manage_salesranking_click";   //主页 - 我的会员 - 新增会员分组 点击“店员榜单”
    public String MANAGE_DISCOUNTRECEIVE_CLICK="manage_discountreceive_click";   //主页 - 我的会员 - 新增会员分组 点击“退款”
    public String BILL_ENTER="bill_enter";   //账单明细 进入“账单明细”页面
    public String BILL_BACK_CLICK="bill_back_click";   //账单明细 点击“返回”
    public String BILL_REFRESH="bill_refresh";   //账单明细 下拉刷新
    public String BILL_UPLOAD="bill_upload";   //账单明细 上划加载
    public String WITHDRAW_ADDCARD_CLICK="withdraw_addcard_click";   //账单明细 点击“提现到新银行卡”
    public String ORDER_ENTER="order_enter";   //订单管理 进入“订单管理”页面
    public String ORDER_BACK_CLICK="order_back_click";   //订单管理 点击“返回”
    public String ORDER_REFRESH="order_refresh";   //订单管理 下拉刷新
    public String ORDER_UPLOAD="order_upload";   //订单管理 上划加载
    public String MONEY_REFRESH="money_refresh";   //赚钱  下拉刷新
    public String MONEY_QRCODE_CLICK="money_qrcode_click";   //赚钱  下拉刷新
    public String MONEY_REVENUE_CLICK="money_revenue_click";   //赚钱  下拉刷新
    public String MONEY_ADDDATA_CLICK="money_adddata_click";   //赚钱  下拉刷新
    public String MONEY_INSURANCE_CLICK="money_insurance_click";   //赚钱  下拉刷新
    public String MONEY_COMMISSION_CLICK="money_commission_click";
    public String MONEY_GUIDE_CLICK="money_guide_click";
    public String INCOME_ENTER="income_enter";
    public String INCOME_BACK_CLICK="income_back_click";
    public String INCOME_REFRESH="income_refresh";
    public String INCOME_UPLOAD="income_upload";
    public String INCOME_MAKEMONEY_CLICK="income_makemoney_click";
    public String ADDDATA_ENTER="adddata_enter";
    public String ADDDATA_BACK_CLICK="adddata_back_click";
    public String ADDDATA_RECORDS_CLICK="adddata_records_click";
    public String INSURANCE_ENTER="insurance_enter";
    public String INSURANCE_BACK_CLICK="insurance_back_click";
    public String PROMOTION_ENTER="promotion_enter";
    public String PROMOTION_BACK_CLICK="promotion_back_click";
    public String PROMOTION_RECORDS_CLICK="promotion_records_click";
    public String PROMOTION_GOODS_CLICK="promotion_goods_click";
    public String PROMOTION_SHARE_CLICK="promotion_share_click";
    public String MONEY_GUIDE_ARTICLE_ENTER="money_guide_article_enter";
    public String MONEY_GUIDE_SHARE_CLICK="money_guide_share_click";
    public String MY_SETTING_CLICK="my_setting_click";
    public String SAFE_CHANGELOGINCODE_CLICK="safe_changelogincode_click";
    public String SAFE_CHANGEPAYCODE_CLICK="safe_changepaycode_click";
    public String SAFE_SETPHONENUMBER_CLICK="safe_setphonenumber_click";
    public String SAFE_VERIFYIDENTITY_CLICK="safe_verifyidentity_click";
    public String SAFE_CARDMANAGEMENT_CLICK="safe_cardmanagement_click";
    public String SETTING_CLEARDATA_CLICK="setting_cleardata_click";
    public String SETTING_ABOUT_CLICK="setting_about_click";
    public String SETTING_LOGOUT_CLICK="setting_logout_click";
    public String SETTING_LOGOUT_CONFIRM_CLICK="setting_logout_confirm_click";
    public String SETTING_LOGOUT_CANCEL_CLICK="setting_logout_cancel_click";
    public String MY_BALANCE_CLICK="my_balance_click";
    public String MY_MESSAGE_CLICK="my_message_click";
    public String MY_TARGET_CLICK="my_target_click";
    public String MY_COLLEGE_CLICK="my_college_click";
    public String MY_DEVICE_CLICK="my_device_click";

    public String MY_CALL_CLICK="my_call_click";
    public String MY_FEEDBACK_CLICK="my_feedback_click";
    public String INFO_ENTER="info_enter";
    public String INFO_BACK_CLICK="info_back_click";
    public String INFO_AVATAR_CLICK="info_avatar_click";
    public String INFO_NICKNAME_CLICK="info_nickname_click";
    public String INFO_PHONENUMBER_CLICK="info_phonenumber_click";
    public String INFO_SEX_CLICK="info_sex_click";
    public String INFO_BIRTHDAY_CLICK="info_birthday_click";
    public String INFO_REGION_CLICK="info_region_click";
    public String INFO_SIGN_CLICK="info_sign_click";
    public String CLEARDATA_INFOLIST_CLICK="cleardata_infolist_click";
    public String CLEARDATA_CACHEDATA_CLICK="cleardata_cachedata_click";
    public String ABOUT_UPDATE_CLICK="about_update_click";
    public String ACCOUNT_ENTER="account_enter";
    public String ACOCUNT_BACK_CLICK="acocunt_back_click";
    public String ACOCUNT_WITHDRAW_CLICK="acocunt_withdraw_click";
    public String ACOCUNT_BILL_CLICK="acocunt_bill_click";
    public String ACOCUNT_ADDCARD_CLICK="acocunt_addcard_click"; //管理/设置 - 我的账户  点击“去添加”(银行卡)






    public static   String getVersion(Context context) {
        String version="";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static   void   onEvent(Context context , String EventsName, HashMap<String,String> map){
        if(map!=null){
            map.put("Android版本",getVersion(context));
            MobclickAgent.onEvent(context,EventsName,map);
        }else {
            HashMap<String, String> maps = new HashMap<String, String>();
            maps.put("Android版本", getVersion(context));
            MobclickAgent.onEvent(context, EventsName, maps);
        }
    }
}
