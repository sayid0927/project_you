/**
 * 
 */
package com.zxly.o2o.model;

import java.io.Serializable;

/**
 * @author fengrongjian 2015-3-17
 * @description 续保范围信息
 */
public class RenewScope implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;
	private String typeName;
	private String paramName;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

}
