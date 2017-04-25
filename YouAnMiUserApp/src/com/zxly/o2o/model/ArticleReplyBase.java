/*
 * 文件名：ArticleReplyBaseDTO.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ArticleReplyBaseDTO.java
 * 修改人：Administrator
 * 修改时间：2015年1月20日
 * 修改内容：新增
 */
package com.zxly.o2o.model;

/**
 * @author yuanfei
 * @version YIBA-O2O 2015年1月20日
 * @since YIBA-O2O
 */
public class ArticleReplyBase extends VariousBean {
	/**
	 * 文章回复 内容
	 */
	private String content = "";

	/**
	 * 文章id
	 */
	private long articleId;

	/**
	 * 回复人Id
	 */
	private long replyerId;

	private UserInfoVO replyer = new UserInfoVO();;

	public UserInfoVO getReplyer() {
		return replyer;
	}

	public void setReplyer(UserInfoVO replyer) {
		this.replyer = replyer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public long getReplyerId() {
		return replyerId;
	}

	public void setReplyerId(Long replyerId) {
		this.replyerId = replyerId;
	}
}
