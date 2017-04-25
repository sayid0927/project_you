/**
 * 
 */
package com.zxly.o2o.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author fengrongjian 2015-3-16
 * @description 二手担保范围信息
 */
public class AssureScope implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String scopeNames;
	private List<AssureCharge> charges;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScopeNames() {
		return scopeNames;
	}

	public void setScopeNames(String scopeNames) {
		this.scopeNames = scopeNames;
	}

	public List<AssureCharge> getCharges() {
		return charges;
	}

	public void setCharges(List<AssureCharge> charges) {
		this.charges = charges;
	}

}
