package com.zxly.o2o.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 文章推荐DTO
 * 
 * @author yuanfei
 * 
 */
public class ShopArticle extends VariousBean implements Serializable {

	/**
	 * TODO 添加字段注释
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 文章来源
	 */
	private String articleFrom = "";

	/**
	 * 文章内容
	 */
	private String content = "";

	/**
	 * 类型id
	 */
	private String typeCode = "";

	/**
	 * 类型名称
	 */
	private String typeName = "";

	/**
	 * 文章标题
	 */
	private String title = "";

	/**
	 * 店铺id
	 */
	private long shopId;

	/**
	 * 平台文章ID
	 * 
	 */
	private long platformArticleId = -1;

	/**
	 * 回复数
	 */
	private int replyAmount=-1;

	/**
	 * 点赞数
	 */
	private long praiseAmount;

	/**
	 * 点踩数
	 */
	private long opposeAmount;

	/**
	 * 阅读数
	 */
	private long readAmount;

	/**
	 * 是否置顶
	 */
	private byte isTop;

	/**
	 * 文章图片保存路径
	 */
	private String articlePicPath = "";

	/**
	 * 模板路径
	 */
	private String templPath = "";

	/**
	 * 文章分享路径
	 */
	private String shareUrl = "";

	/**
	 * 文章路径
	 */
	private String url = "";

	/**
	 * 副标题
	 */
	private String subhead = "";


	/**
	 * 头像Url
	 */
	private String headUrl = "";

	/**
	 * 文章标记
	 */
	private String lableCode = "";

	private int isCollect;

	// 文章是否被点赞
	private int isPraise;

	// 文章是否被点踩
	private int isOppose;


	private List<ArticleReply> articleReplys = new ArrayList<ArticleReply>();

	public void setIsCollected(int isCollected) {
		this.isCollect = isCollected;
	}

	public int getIsCollected() {
		return isCollect;
	}

	public void setIsPraise(int isPraise) {
		this.isPraise = isPraise;
	}

	public int getIsPraise() {
		return isPraise;
	}

	public void setIsOppose(int isOppose) {
		this.isOppose = isOppose;
	}

	public int getIsOppose() {
		return isOppose;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<ArticleReply> getReplys() {
		return articleReplys;
	}

	public void setReplys(List<ArticleReply> articleReplys) {
		this.articleReplys = articleReplys;
	}

	private boolean isShowed;

	public boolean getIsShowed() {
		// TODO Auto-generated method stub
		return isShowed;
	}

	public void setIsShowed(boolean mIsShowed) {
		isShowed = mIsShowed;
	}

	public byte getIsTop() {
		return isTop;
	}

	public void setIsTop(Byte isTop) {
		this.isTop = isTop;
	}

	public String getArticle_pic_path() {
		return articlePicPath;
	}

	public void setArticle_pic_path(String article_pic_path) {
		this.articlePicPath = article_pic_path;
	}

	public String getArticleFrom() {
		return articleFrom;
	}

	public void setArticleFrom(String articleFrom) {
		this.articleFrom = articleFrom;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = Long.parseLong(shopId);
	}

	public Long getPlatformArticleId() {
		return platformArticleId;
	}

	public void setPlatformArticleId(Long platformArticleId) {
		this.platformArticleId = platformArticleId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTempl_path() {
		return templPath;
	}

	public void setTempl_path(String templ_path) {
		this.templPath = templ_path;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getSubhead() {
		return subhead;
	}

	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}

	public String getLableCode() {
		return lableCode;
	}

	public void setLableCode(String lableCode) {
		this.lableCode = lableCode;
	}

	public int getReplyAmount() {
		return replyAmount;
	}

	public void setReplyAmount(int replyAmount) {
		this.replyAmount = replyAmount;
	}

	public Long getPraiseAmount() {
		return praiseAmount;
	}

	public void setPraiseAmount(Long praiseAmount) {
		this.praiseAmount = praiseAmount;
	}

	public Long getOpposeAmount() {
		return opposeAmount;
	}

	public void setOpposeAmount(Long opposeAmount) {
		this.opposeAmount = opposeAmount;
	}

	public Long getRead_amount() {
		return readAmount;
	}

	public void setRead_amount(Long read_amount) {
		this.readAmount = read_amount;
	}

	public String getHead_url() {
		return headUrl;
	}

	public void setHead_url(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getLable_code() {
		return lableCode;
	}

	public void setLable_code(String lable_code) {
		this.lableCode = lable_code;
	}

	public String getType_code() {
		return typeCode;
	}

	public void setType_code(String type_code) {
		this.typeCode = type_code;
	}

}