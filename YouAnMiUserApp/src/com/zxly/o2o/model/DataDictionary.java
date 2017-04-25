/*
 * 文件名：DataDictionary.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： DataDictionary.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-17
 * 修改内容：新增
 */
package com.zxly.o2o.model;

/**
 * TODO 新旧度类型
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
public class DataDictionary {
	private String code;
	private String typeCode;
	private String name;
	private int orderId;
	
	public DataDictionary(String code, String typeCode, String name, int orderId) {
		super();
		this.code = code;
		this.typeCode = typeCode;
		this.name = name;
		this.orderId = orderId;
	}
	public DataDictionary() {	
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	@Override
	public String toString() {
		return "DataDictionary [code=" + code + ", typeCode=" + typeCode
				+ ", name=" + name + ", orderId=" + orderId + "]";
	}
	
	

}
