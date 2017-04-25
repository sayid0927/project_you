/*
 * 文件名：ShopTopicDTO.java
 * 版权：Copyright 2014 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ShopPostDTO.java
 * 修改人：Administrator
 * 修改时间：2014年12月27日
 * 修改内容：新增
 */
package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 论坛帖子
 * 
 * 
 * forum_topic
 * 
 * @author Administrator
 * @version YIBA-O2O 2014年12月27日
 * @since YIBA-O2O
 */
public class ShopTopic extends VariousBean  {
	/**
	 * 是不是我的回帖
	 */
	private boolean isMyReply;
	
	private List<TopicReply> replyList =new ArrayList<TopicReply>();

	/**
	 * 帖子内容
	 */
	private String content = "";

	/**
	 * 帖子类型
	 */
	private int typeId;

	/**
	 * 门店id
	 */
	private long shopId;

	/**
	 * 是否置顶
	 */
	private int isTop;

	/**
	 * 回复数
	 */
	private long replyAmout;

	/**
	 * 点赞数
	 */
	private long praiseAmout;

	/**
	 * 分享数
	 */
	private long shareAmount;

	/**
	 * 阅读数
	 */
	private long readAmount;

	/**
	 * 帖子来源 id
	 */

	private int topicFromId;

	/**
	 * 帖子来源name
	 */

	private String topicFromName = "";

	/**
	 * 是不是平台帖子
	 * */
	private byte isShopTopic;

	/**
	 * 分享地址
	 * */
	private String shareUrl="";


	/**
	 * 圈子头像
	 * */
	private String circleHeadUrl="";

	/**
	 * 帖子原图
	 */
	private List<String> originImageList =new ArrayList<String>();

	/**
	 * 缩略图
	 */
	private List<String> thumImageList = new ArrayList<String>();

	private byte isPraise;  //1.已点赞，2.未点赞

	private byte publishMan; //1.匿名，2.柚安米

	private byte isCollect;  //1.已收藏 2.未收藏

	private List<String> headUrlList=new ArrayList<String>();

	private PublishUser publishUser=new PublishUser();



	public List<String> getHeadUrlList() {
		return headUrlList;
	}

	public void setHeadUrlList(List<String> headUrlList) {
		this.headUrlList = headUrlList;
	}

	public String getCircleHeadUrl() {
		return circleHeadUrl;
	}

	public void setCircleHeadUrl(String circleHeadUrl) {
		this.circleHeadUrl = circleHeadUrl;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public byte getIsShopTopic() {
		return isShopTopic;
	}

	public void setIsShopTopic(byte isShopTopic) {
		this.isShopTopic = isShopTopic;
	}

	public byte getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(byte isCollect) {
		this.isCollect = isCollect;
	}

	public byte getPublishMan() {
		return publishMan;
	}

	public PublishUser getPublishUser() {
		return publishUser;
	}

	public void setPublishUser(PublishUser publishUser) {
		this.publishUser = publishUser;
	}

	public void setPublishMan(byte publishMan) {
		this.publishMan = publishMan;
	}

	private UserInfoVO publisher = new UserInfoVO();

	public boolean getIsMyReply() {
		return isMyReply;
	}

	public void setIsMyReply(boolean isMyReply) {
		this.isMyReply = isMyReply;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType_id() {
		return typeId;
	}

	public void setType_id(Integer type_id) {
		this.typeId = type_id;
	}

	public long getShop_id() {
		return shopId;
	}

	public void setShop_id(Long shop_id) {
		this.shopId = shop_id;
	}

	public int getIs_top() {
		return isTop;
	}

	public void setIs_top(Integer is_top) {
		this.isTop = is_top;
	}

	public int getTopic_from_id() {
		return topicFromId;
	}

	public void setTopic_from_id(Integer topic_from_id) {
		this.topicFromId = topic_from_id;
	}

	public String getTopic_from_name() {
		return topicFromName;
	}

	public void setTopic_from_name(String topic_from_name) {
		this.topicFromName = topic_from_name;
	}

	public long getReplyAmout() {
		return replyAmout;
	}

	public void setReplyAmout(long replyAmout) {
		this.replyAmout = replyAmout;
	}

	public long getPraiseAmout() {
		return praiseAmout;
	}

	public void setPraiseAmout(long praiseAmout) {
		this.praiseAmout = praiseAmout;
	}

	public long getRead_amount() {
		return readAmount;
	}

	public void setRead_amount(long read_amount) {
		this.readAmount = read_amount;
	}

	public UserInfoVO getUserVO() {
		return publisher;
	}

	public void setUserVO(UserInfoVO userVO) {
		this.publisher = userVO;
	}

	public void setIsPraise(byte isPraise) {
		this.isPraise = isPraise;
	}

	public byte getIsPraise() {
		return isPraise;
	}

	public List<TopicReply> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<TopicReply> replyList) {
		this.replyList = replyList;
	}

	public List<String> getThumImageList() {
		return thumImageList;
	}

	public void setThumImageList(List<String> thumImageList) {
		this.thumImageList = thumImageList;
	}

	public List<String> getOriginImageList() {
		return originImageList;
	}

	public void setOriginImageList(List<String> originImageList) {
		this.originImageList = originImageList;
	}


	public long getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(long shareAmount) {
		this.shareAmount = shareAmount;
	}

	public class PublishUser{
		public long id;
		public String nickname;
		public String thumHeadUrl;
		private int type;//1老板 2业务员 3普通用户

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getThumHeadUrl() {
			return thumHeadUrl;
		}

		public void setThumHeadUrl(String thumHeadUrl) {
			this.thumHeadUrl = thumHeadUrl;
		}
	}

}
