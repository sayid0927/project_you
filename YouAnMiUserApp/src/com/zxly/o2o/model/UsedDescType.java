/*
 * 文件名：UsedDescType.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： UsedDescType.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-17
 * 修改内容：新增
 */
package com.zxly.o2o.model;

import java.util.List;

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
 * @version YIBA-O2O 2015-3-17
 * @since YIBA-O2O
 */
public class UsedDescType{
	private int id;
	private int type;
	private String name;
	private int orderNo;
	private List<UsedDescParam> params;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public List<UsedDescParam> getParams() {
		return params;
	}
	public void setParams(List<UsedDescParam> params) {
		this.params = params;
	}

	

}
