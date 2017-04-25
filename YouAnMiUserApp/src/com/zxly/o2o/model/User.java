package com.zxly.o2o.model;

import java.io.Serializable;

/**
 *     @author dsnx  
 *     @version 创建时间：2015-1-4 下午2:04:08    类说明: 
 */
public class User implements Serializable{

	public static final int ACTION_TYPE_SHARE=1;
	public static final int ACTION_TYPE_PRISE=2;
	public static final int ACTION_TYPE_COLLECT=3;
	public static final int ACTION_TYPE_LIKE=4;

	/**  */
	private static final long serialVersionUID = 1L;
	private long id;
	private String userName;//用户昵称 默认为手机号
	private String loginUserName="";//登录用户名 目前为手机号
	private String password;//登录密码
	private byte type;//用户类型
	private long shopId;//用户注册的店铺ID
	private String nickname="";
	private byte gender;
	private String mobilePhone="";
	private String thumHeadUrl="";//头像缩略图URL
	private String originHeadUrl;//头像原图地址
	private long takeoutDeliveryId;//收货地址id
	private String takeDeliveryAddress;//收货地址
	private long birthday;
	private String cityName;//城市名称
	private String promotionCode;//推广码
	private String provinceName;//省份名称
	private String signature;//个性签名
	private String name;
	private long  oprateTime;
	private long belongId;//专属业务员id
	private String signKey;

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public long getBelongId() {
		return belongId;
	}

	public void setBelongId(long belongId) {
		this.belongId = belongId;
	}

	public long getTakeoutDeliveryId() {
		return takeoutDeliveryId;
	}

	public void setTakeoutDeliveryId(long takeoutDeliveryId) {
		this.takeoutDeliveryId = takeoutDeliveryId;
	}

	public String getTakeDeliveryAddress() {
		return takeDeliveryAddress;
	}

	public void setTakeDeliveryAddress(String takeDeliveryAddress) {
		this.takeDeliveryAddress = takeDeliveryAddress;
	}

	public long getOprateTime() {
		return oprateTime;
	}

	public void setOprateTime(long oprateTime) {
		this.oprateTime = oprateTime;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getLoginUserName() {
		return loginUserName;
	}
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public long getShopId() {
		return shopId;
	}
	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	public String getNickName() {
		return nickname;
	}
	public void setNickName(String nickName) {
		this.nickname = nickName;
	}
	public byte getGender() {
		return gender;
	}
	public void setGender(byte gender) {
		this.gender = gender;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getThumHeadUrl() {
		return thumHeadUrl;
	}
	public void setThumHeadUrl(String thumHeadUrl) {
		this.thumHeadUrl = thumHeadUrl;
	}
	
	public String getOriginHeadUrl() {
		return originHeadUrl;
	}
	public void setOriginHeadUrl(String originHeadUrl) {
		this.originHeadUrl = originHeadUrl;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			if (((User) o).id == this.id) {
				return true;
			}
		}
		return false;
	}
}
