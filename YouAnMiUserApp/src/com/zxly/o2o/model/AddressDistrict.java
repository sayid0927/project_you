package com.zxly.o2o.model;

import java.io.Serializable;

public class AddressDistrict implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;
	private String districtName;
	private String districtId;

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

}
