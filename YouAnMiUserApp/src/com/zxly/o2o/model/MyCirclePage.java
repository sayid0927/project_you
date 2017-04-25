/*
 * 文件名：Article.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： Article.java
 * 修改人：Administrator
 * 修改时间：2015-1-4
 * 修改内容：新增
 */
package com.zxly.o2o.model;

public class MyCirclePage extends MyArticleList {

	private int userAmount;
	private int topicAmount;

	public int getUserAmount() {
		return userAmount;
	}

	public void setUserAmount(int userAmount) {
		this.userAmount = userAmount;
	}

	public int getTopicAmount() {
		return topicAmount;
	}

	public void setTopicAmount(int topicAmount) {
		this.topicAmount = topicAmount;
	}

	@Override
	public String toString() {
		return "MyCirclePageBean [articles=" + getArticles() + ", userAmount=" + userAmount
				+ ", topicAmount=" + topicAmount + ", getArticles()=" + getArticles()
				+ ", getUserAmount()=" + getUserAmount() + ", getTopicAmount()=" + getTopicAmount()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
