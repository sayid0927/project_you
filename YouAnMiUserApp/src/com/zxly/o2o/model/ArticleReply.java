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

	private String nickname;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public byte getIsPraise() {
		return isPraise;
	}

	public void setIsPraise(byte isPraise) {
		this.isPraise = isPraise;
	}

	private boolean isNeedAnim;
	private String headUrl="";
	private int status;//1--未屏蔽  2--已屏蔽
	private long replayerId;

	public boolean isNeedAnim() {
		return isNeedAnim;
	}

	public void setNeedAnim(boolean needAnim) {
		isNeedAnim = needAnim;
	}



	public long getReplayerId() {
		return replayerId;
	}

	public void setReplayerId(long replayerId) {
		this.replayerId = replayerId;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

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
