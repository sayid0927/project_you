package com.zxly.o2o.model;

import com.google.gson.annotations.Expose;

/**
 * 
 * @author dsnx 详细描述见接口文档UsedDescDTO
 */
public class UsedDesc {
	
	@Expose 
	private String typeName;
	
	@Expose 
	private String descName;
	
	@Expose 
	private int typeId;
	
	@Expose 
	private String descId;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDescName() {
		return descName;
	}

	public void setDescName(String descName) {
		this.descName = descName;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getDescId() {
		return descId;
	}

	@Override
	public String toString() {
		return descName+",";
	}

	public void setDescId(String descId) {
		this.descId = descId;
	}

}
