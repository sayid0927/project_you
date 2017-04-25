package com.zxly.o2o.model;

import java.io.Serializable;

public class BankcardInfo implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;
	private String prcptcd;
	private String brabankName;
	private String provinceCode;
	private String cityCode;

	public String getPrcptcd() {
		return prcptcd;
	}

	public void setPrcptcd(String prcptcd) {
		this.prcptcd = prcptcd;
	}

	public String getBrabankName() {
		return brabankName;
	}

	public void setBrabankName(String brabankName) {
		this.brabankName = brabankName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
}
