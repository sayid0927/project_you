/**
 * Copyright(C)2012-2013 深圳市掌星立意科技有限公司版权所有
 * 创 建 人:	Gofeel
 * 修 改 人:
 * 创 建日期:	2013-7-22
 * 描	   述:	常量类
 * 版 本 号:   1.0
 */
package com.zxly.o2o.util;

import android.os.Environment;

import java.io.File;

/** 通用常量类 */
public class Constants {

	/**新浪微博**/
	public static final String XLWB_APP_KEY = "2023646846";
	/**应用的回调页**/
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
	/**应用申请的高级权限**/
	public static final String SCOPE =
			"email,direct_messages_read,direct_messages_write,"
					+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
					+ "follow_app_official_microblog," + "invitation_write";

	public static final String WX_APP_ID="wx3ef3bfdf7a1b11e1";
	public static final String WX_MCH_ID = "1291642701";
	public static final  String WX_API_KEY ="qismd2om1oxfn382mxoacopj1mxp2xlm";
	public static final String QQ_SHARE_APP_ID="1104793023";



	/** 每页大小*/
	public static final int PER_PAGE_SIZE = 7;
	/** 登录 */
	public static final int REFRESH = 1000;
	/** 圈子 */
	public static final int FORUM_COMMUNITY = 0;
	public static final int MYCIRCLE_MUST_LOOK = 1;
	public static final int MYCIRCLE_PHONE_UPKEEP = 2;
	public static final int MYCIRCLE_FUNCTION_DIG = 3;
	public static final int MYCIRCLE_COLLECTED = 4;
	public static final int MY_TOPICOS= 5;
	public static final int MY_REPLYS= 6;

	public static final int FORUM_COMMUNITY_DETAIL = 10;
	public static final int ARTICLE_DETAIL = 11;
	public static final int FORUM_PUBLISH = 12;
	public static final int GET_PIC_FROM_CELLPHONE = 13;
	public static final int GET_PIC_FROM_CAMERA = 14;
	public static final int GET_PIC_FROM_CANCEL = 15;
	public static final int RESULT_PICTURE = 16;
	public static final int DELETE_PIC = 17;
	public static final int CHANGE_PIC = 18;

	public static final int ACTION_TYPE_COLLECT= 1001;
	public static final int ACTION_TYPE_UNCOLLECT= 1002;
	public static final int ACTION_TYPE_DOWN= 1003;
	public static final int ACTION_TYPE_UP= 1004;
	public static final int ACTION_TYPE_CANCEL_DOWN= 1005;
	public static final int ACTION_TYPE_CANCEL_UP= 1006;
	public static final int ACTION_TYPE_PUBLISH= 1007;
	public static final int ACTION_TYPE_COMMENT= 1008;

	public static final int REFRESH_LAST_TIME = 300;
	
	public static final int RESULT_TOKEN_PAST = 20102;// Token过期

	public static final int RESULT_TOKEN_NULLITY = 20101;// Token无效

	/**首页*/
	public static final int COLLECT_SHOP = 1;
	public static final int CANCLE_COLLECT_SHOP = 2;
	
	//handler消息标志，0X0000表示成功，0X0001表示失败
	public static final int MSG_SUCCEED = 0X0000;
	public static final int MSG_FAILED = 0X0001;
	public static final int RESULT_SUCCEED = 0X0003;

	public static  final int APP_USER_TYPE=2;

	/** 支付类型 */
	public static final int TYPE_FLOW_PAY = 0;//流量充值订单支付
	public static final int TYPE_PRODUCT_PAY = 1;//确认交易类型是商品支付
	public static final int TYPE_TAKEOUT = 3;//提现
	public static final int TYPE_INSURANCE_PAY = 11;//延保
	public static final int TYPE_JUST_ADD = 99;//仅仅是新增银行卡
	/** 支付方式 */
	public static final String PAY_TYPE_UNKNOWN = "0";//未选中任何支付方式
	public static final String PAY_TYPE_LIANLIAN = "01";//连连支付
	public static final String PAY_TYPE_WEIXIN = "02";//微信支付
	public static final String PAY_TYPE_USER_BALANCE = "03";//用户余额支付
	public static final String PAY_TYPE_ALI = "04";//支付宝支付
	/** 是否设置交易密码 */
	public static int isUserPaw = 0;//1：未设置 2：已设置
	/** 支付业务类型 */
	public static final String BUSINESS_TYPE_PRODUCT = "001";//商品
	public static final String BUSINESS_TYPE_FLOW = "002";//流量
	public static final String BUSINESS_OTHER_TYPE = "003";//其他（提现验证支付时用到）
	public static final String BUSINESS_TYPE_INSURANCE = "011";//延保
	/** 支付渠道类型 */
	public static final String CHANNEL_SHOP_APP= "002";//商户app
	public static final String CHANNEL_TAKEOUT_VERIFY= "006";//提现认证支付
	/** 支付设备类型 */
	public static final String PAY_DEVICE_TYPE = "1";//1：android 2：ios
	/** 银行卡类型 */
	public static final int TYPE_BANKCARD_SAVINGS = 2;//储蓄卡
	public static final int TYPE_BANKCARD_CREDIT = 3;//信用卡
	/** 短信验证码类型 */
	public static final int TYPE_MSG_PAY = 6;//支付相关短信类型
	public static final int TYPE_MSG_MODIFY_PWD = -1;//修改密码短信类型


	/**1:商户app 2:用户app**/
	public static final int APP_TYPE = 1;


	/** cache路径 */
	public static final String STORE_CACHE_PATH ="yam_user/cache" ;

	/** 图片路径 */
	public static final String STORE_IMG_PATH = Environment.getExternalStorageDirectory().getPath()
			+ File.separator + "yam_shop/image" + File.separator;
	
	public static final String PHONE_PATTERN = "^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
	public static final String ID_PATTERN = "^\\d{15}|^\\d{17}([0-9]|X|x)$";
	public static final String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	// 5-10位QQ号码
	public static final String QQ_PATTERN = "^[1-9]\\d{4,9}$";
	public static final String PASSWORD_PATTERN = "^[A-Za-z0-9]{6,16}$";
	public static final int PASSWORD_MAX_LENGTH = 16;
	public static final String PARNET_PATH =  "yam_shop";
	public static final int DOWNLOAD_DONE = 31;
	public static final int DOWNLOAD_ERROR = 32;

    /**订单**/

    /**订单**/
	//订单状态:
	/**待付款**/
	public static final int ORDER_WAIT_ROR_PAY = 1;
	/**待发货**/
	public static final int ORDER_WAIT_ROR_TAKE= 2;
	/**卖家送货中**/
	public static final int ORDER_SENDING= 3;
	/**待确认**/
	public static final int ORDER_WAIT_CONFIRM = 4;
	/**交易关闭**/
	public static final int ORDER_CLOSE= 5;
	/**交易结束**/
	public static final int ORDER_FINISH= 6;
	/**交易关闭**/
	public static final int ORDER_REFUNDING= 8;
	/**交易关闭**/
	public static final int ORDER_REFUND_SUCCESS= 9;

	/**退款单状态**/
	public static final int REFUND_ORDER_APPLY= 1;  //商家确认中
	public static final int REFUND_ORDER_CONFIRMING= 2;  //商家确认退款
	public static final int REFUND_ORDER_CONFIRMED= 3;   //商家确认退货
	public static final int REFUND_ORDER_REFUND_DONE= 4;  //退款成功
	public static final int REFUND_ORDER_REJECT= 5;  //退款驳回

    /**商品类型**/
    public static final int PRODUCT_TYPE_PACKAGE= 3;
  //  public static final int PRODUCT_TYPE_NOMAL= 3;
    
	/**订单操作：付款，取消,删除**/
	public static final int ORDER_OPERATE_PAY= 3;
	public static final int ORDER_OPERATE_CANCLE= 1;
	public static final int ORDER_OPERATE_DELETE= 2;
	
	/**订单送货类型**/
	public static final int ORDER_DELIVERY_TAKE=2;
	public static final int ORDER_DELIVERY_SEND=1;
	
	/**商品套餐状态**/
	public static final int PRODUCT_STATE_NOMAL=1;
	public static final int PRODUCT_STATE_FERUNDING=2 ;
	public static final int PRODUCT_STATE_FERUND_SUCCESS=3;
	
	/**角色类型 1:店长 2：业务员 3：送货员**/
	public static final int USER_TYPE_ADMIN=1;
	public static final int USER_TYPE_SALESMAN=2;
	public static final int USER_TYPE_DELIVERYMAN=3;

	/**活动页面打开方式 1:从本应用打开  2:从分享地址打开*/
	public static final int OPEN_FROM_APP=1;
	public static final int OPEN_FROM_SHARE=2;

	/**柚安米商学院 1.全部课程；2.最新课程； 3.搜索课程*/
	public static final int YAM_COURSE_ALL=1;
	public static final int YAM_COURSE_NEW=2;
	public static final int YAM_COURSE_SEARCH=3;


}
