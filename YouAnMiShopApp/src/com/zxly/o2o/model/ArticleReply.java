package com.zxly.o2o.model;

/**
 * 文章回复dto
 * 
 * <pre>
 * </pre>
 * 
 * @author Administrator
 * @version YIBA-O2O 2015年1月4日
 * @since YIBA-O2O
 */
public class ArticleReply extends ArticleReplyBase {

	/**
	 * 顶数
	 */
	private long praiseAmount;

	private String thumbUrl = "";

	private byte isPraise;
	private boolean isNeedAnim;

	public boolean getIsNeedAnim() {
		return isNeedAnim;
	}

	public void setIsNeedAnim(boolean isNeedAnim) {
		this.isNeedAnim = isNeedAnim;
	}

	public long getPraiseAmount() {
		return praiseAmount;
	}

	public void setPraiseAmount(long praiseAmount) {
		this.praiseAmount = praiseAmount;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public byte getIsPraiseAmount() {
		return isPraise;
	}

	public void setIsPraiseAmount(byte isPraiseAmount) {
		this.isPraise = isPraiseAmount;
	}

}
