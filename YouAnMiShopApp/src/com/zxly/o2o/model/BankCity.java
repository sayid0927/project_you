package com.zxly.o2o.model;

import java.io.Serializable;

public class BankCity implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;
	private String name;
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
