/*
 * 文件名：SMSMessage.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SMSMessage.java
 * 修改人：Administrator
 * 修改时间：2015-1-8
 * 修改内容：新增
 */
package com.zxly.o2o.model;

import java.io.Serializable;

public class SMSMessage implements Serializable{
	public String phoneNumber;
	public String authCode;
	public String promotionCode;
}
