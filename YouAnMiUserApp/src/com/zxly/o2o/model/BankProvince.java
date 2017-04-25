package com.zxly.o2o.model;

import java.io.Serializable;
import java.util.List;

public class BankProvince implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;
	private String provice;
	private String provinceCode;
	private List<BankCity> area;

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public List<BankCity> getArea() {
		return area;
	}

	public void setArea(List<BankCity> area) {
		this.area = area;
	}
}
