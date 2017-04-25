/**
 * 
 */
package com.zxly.o2o.model;

import java.io.Serializable;

/**
 * @author fengrongjian 2015-3-16
 * @description 二手担保返回信息
 */
public class AssureResult implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;
	private Long assureTypeId;
	private Long assureChargeId;
	private String assureName;

	public Long getAssureTypeId() {
		return assureTypeId;
	}

	public void setAssureTypeId(Long assureTypeId) {
		this.assureTypeId = assureTypeId;
	}

	public Long getAssureChargeId() {
		return assureChargeId;
	}

	public void setAssureChargeId(Long assureChargeId) {
		this.assureChargeId = assureChargeId;
	}

	public String getAssureName() {
		return assureName;
	}

	public void setAssureName(String assureName) {
		this.assureName = assureName;
	}

	@Override
	public String toString() {
		return "AssureResult [assureTypeId=" + assureTypeId
				+ ", assureChargeId=" + assureChargeId + ", assureName="
				+ assureName + "]";
	}

}
