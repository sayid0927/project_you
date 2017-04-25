/**
 * 
 */
package com.zxly.o2o.model;

import java.io.Serializable;

/**
 * @author fengrongjian 2015-3-17
 * @description 续保期限信息
 */
public class RenewDeadline implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String typeName;
	private Float price;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

}
