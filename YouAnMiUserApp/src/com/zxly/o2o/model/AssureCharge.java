/**
 * 
 */
package com.zxly.o2o.model;

import java.io.Serializable;

/**
 * @author fengrongjian 2015-3-16
 * @description 二手担保收费标准信息
 */
public class AssureCharge implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deadlineName;
	private Float assureRate;
	private Float chargeRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeadlineName() {
		return deadlineName;
	}

	public void setDeadlineName(String deadlineName) {
		this.deadlineName = deadlineName;
	}

	public Float getAssureRate() {
		return assureRate;
	}

	public void setAssureRate(Float assureRate) {
		this.assureRate = assureRate;
	}

	public Float getChargeRate() {
		return chargeRate;
	}

	public void setChargeRate(Float chargeRate) {
		this.chargeRate = chargeRate;
	}

}
