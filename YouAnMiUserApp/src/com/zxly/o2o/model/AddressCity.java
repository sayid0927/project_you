package com.zxly.o2o.model;

import java.io.Serializable;
import java.util.List;

public class AddressCity implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;
	private String cityName;
	private String cityId;
	private List<AddressDistrict> districts;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<AddressDistrict> getDistricts() {
		return districts;
	}

	public void setDistricts(List<AddressDistrict> districts) {
		this.districts = districts;
	}

}
