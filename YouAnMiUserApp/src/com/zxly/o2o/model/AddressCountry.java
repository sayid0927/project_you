package com.zxly.o2o.model;

import java.io.Serializable;
import java.util.List;

public class AddressCountry implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;
	private String countryName;
	private String countryId;
	private List<AddressProvince> prvs;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public List<AddressProvince> getPrvs() {
		return prvs;
	}

	public void setPrvs(List<AddressProvince> prvs) {
		this.prvs = prvs;
	}

}
