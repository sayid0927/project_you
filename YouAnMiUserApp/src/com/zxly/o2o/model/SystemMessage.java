package com.zxly.o2o.model;

import java.io.Serializable;

/**
 * @author fengrongjian 2015-1-23
 * @description 系统消息
 */
public class SystemMessage implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	/** 消息ID */
	private long id;
	/** 消息类型名称 */
	private String typeName;
	/** 标题 */
	private String title;
	/** 消息内容 */
	private String content;
	/** 创建时间 */
	private long createTime;
	/** 已读标志（1：未读，2已读） */
	private byte status;
	/** H5页面URL */
	private String url;

	private Integer clazz;
	private Integer type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getClazz() {
		return clazz;
	}

	public void setClazz(Integer clazz) {
		this.clazz = clazz;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}