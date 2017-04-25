package com.zxly.o2o.model;

import java.io.Serializable;

/**
 * @author fengrongjian 2015-12-7
 * @description 我的推广会员
 */
public class MyPromotionMember implements Serializable{

	private static final long serialVersionUID = 1L;
	private float commission;//佣金贡献
	private String headUrl;//头像
	private String letter;//首字母
	private String name;//名称
	private String userName;//手机号码
	private String thumHeadUrl;
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public float getCommission() {
		return commission;
	}

	public void setCommission(float commission) {
		this.commission = commission;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getThumHeadUrl() {
		return thumHeadUrl;
	}

	public void setThumHeadUrl(String thumHeadUrl) {
		this.thumHeadUrl = thumHeadUrl;
	}
}
