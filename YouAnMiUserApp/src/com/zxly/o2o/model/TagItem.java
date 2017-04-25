/*
 * 文件名：TagItem.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： TagItem.java
 * 修改人：wuchenhui
 * 修改时间：2015-4-30
 * 修改内容：新增
 */
package com.zxly.o2o.model;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-4-30
 * @since      YIBA-O2O
 */
public class TagItem {
	private String tagName;
	private Object data;
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public TagItem(String tagName, Object data) {
		super();
		this.tagName = tagName;
		this.data = data;
	}
	public TagItem() {
	}
}
