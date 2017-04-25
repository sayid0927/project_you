/*
 * 文件名：ArticleBasic.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ArticleBasic.java
 * 修改人：wuchenhui
 * 修改时间：2015-1-16
 * 修改内容：新增
 */
package com.zxly.o2o.model;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * 
 * <pre>
 * </pre>
 * 
 * @author wuchenhui
 * @version YIBA-O2O 2015-1-16
 * @since YIBA-O2O
 */
public class ArticleInfo {
	private Long id;// 文章ID

	private String title="";// 128 标题

	private String subhead="";// 128 副标题

	private String typeName="";// 32 类型名称

	private String lableCode="";// 32 标签

	private Byte isTop;// 文章是否置顶(1：是，2;否)默认1

	private Integer replyAmount;// 回复数

	private String headUrl="";// 头像URL

	private String url="";// URL地址

	private String nikeName="";// 32 创建人昵称

	private Long createTime;// 创建时间

	
	public ArticleInfo() {
	}
	
	public ArticleInfo(String title, String typeName) {
		this.title = title;
		this.typeName = typeName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubhead() {
		return subhead;
	}

	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getLableCode() {
		return lableCode;
	}

	public void setLableCode(String lableCode) {
		this.lableCode = lableCode;
	}

	public Byte getIsTop() {
		return isTop;
	}

	public void setIsTop(Byte isTop) {
		this.isTop = isTop;
	}

	public Integer getReplyAmount() {
		return replyAmount;
	}

	public void setReplyAmount(Integer replyAmount) {
		this.replyAmount = replyAmount;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}
