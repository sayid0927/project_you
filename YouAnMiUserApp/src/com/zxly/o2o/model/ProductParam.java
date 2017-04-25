package com.zxly.o2o.model;

/**
 *     @author dsnx  @version 创建时间：2015-1-20 上午10:49:24    类说明: 
 */
public class ProductParam {
	private String displayName;
	private String paramValue;
	private int paramType;
	private int isMust;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public int getParamType() {
		return paramType;
	}

	public void setParamType(int paramType) {
		this.paramType = paramType;
	}

	public int getIsMust() {
		return isMust;
	}

	public void setIsMust(int isMust) {
		this.isMust = isMust;
	}

}
