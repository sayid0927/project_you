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
	private int replyAmount;

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

	private byte status;// status=3的表示已删除或禁用，其它状态表示正常
	private List<ArticleReply> articleReplys = new ArrayList<ArticleReply>();


	//新增
	private int indexArticleType;//1.帖子 2.文章 3.软文
	private String originImageUrl;//原图（文章类型的没有）
	private String publishName;//发布人（帖子则找user_info的名字，软文和文章：看是商户后台发布,显示为门店简称或者柚安米）
	private String thumImageUrl;//缩略图（文章类型的没有）
	private List<String> thumImageUrlAry = new ArrayList<String>();
	private String tableName;

	//文章优化新增
	private List<String> labels = new ArrayList<String>();
	//文章优化新增
	private List<String> imageUrls = new ArrayList<String>();

	//收藏列表新增
	private long orgId;
	private int articleType;

	private boolean isNeedPraiseAnim;

	public boolean isNeedPraiseAnim() {
		return isNeedPraiseAnim;
	}

	public void setNeedPraiseAnim(boolean needPraiseAnim) {
		isNeedPraiseAnim = needPraiseAnim;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public int getArticleType() {
		return articleType;
	}

	public void setArticleType(int articleType) {
		this.articleType = articleType;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getThumImageUrlAry() {
		return thumImageUrlAry;
	}

	public void setThumImageUrlAry(List<String> thumImageUrlAry) {
		this.thumImageUrlAry = thumImageUrlAry;
	}

	public int getIndexArticleType() {
		return indexArticleType;
	}

	public void setIndexArticleType(int indexArticleType) {
		this.indexArticleType = indexArticleType;
	}

	public String getOriginImageUrl() {
		return originImageUrl;
	}

	public void setOriginImageUrl(String originImageUrl) {
		this.originImageUrl = originImageUrl;
	}

	public String getPublishName() {
		return publishName;
	}

	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}

	public String getThumImageUrl() {
		return thumImageUrl;
	}

	public void setThumImageUrl(String thumImageUrl) {
		this.thumImageUrl = thumImageUrl;
	}

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

	public long getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = Long.parseLong(shopId);
	}

	public long getPlatformArticleId() {
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public long getPraiseAmount() {
		return praiseAmount;
	}

	public void setPraiseAmount(long praiseAmount) {
		this.praiseAmount = praiseAmount;
	}

	public long getOpposeAmount() {
		return opposeAmount;
	}

	public void setOpposeAmount(long opposeAmount) {
		this.opposeAmount = opposeAmount;
	}

	public long getRead_amount() {
		return readAmount;
	}

	public void setRead_amount(long read_amount) {
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

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
}