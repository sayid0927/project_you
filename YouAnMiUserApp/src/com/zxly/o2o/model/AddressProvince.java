package com.zxly.o2o.model;

import java.io.Serializable;
import java.util.List;

public class AddressProvince implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;
	private String provinceName;
	private String provinceId;
	private List<AddressCity> citys;

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public List<AddressCity> getCitys() {
		return citys;
	}

	public void setCitys(List<AddressCity> citys) {
		this.citys = citys;
	}

}
