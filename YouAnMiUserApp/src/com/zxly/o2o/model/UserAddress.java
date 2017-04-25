/**
 * 
 */
package com.zxly.o2o.model;

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * @author fengrongjian 2015-1-22
 * @description 用户地址信息
 */
public class UserAddress implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@Expose 
	private String name;
	
	@Expose 
	private Long countryId;
	
	@Expose 
	private Long provinceId;
	
	@Expose 
	private Long cityId;
	
	@Expose 
	private Long areaId;
	
	@Expose 
	private Long villageId;
	
	@Expose 
	private String villageName;
	
	@Expose 
	private String detailedAddress;
	
	@Expose 
	private Integer isDefault;// 是否默认 1：是 2：否
	
	@Expose 
	private String mobilePhone;
	
	@Expose 
	private String address;
	
	@Expose 
	private String provinceName;
	
	@Expose 
	private String cityName;
	
	@Expose 
	private String areaName;
	
	@Expose 
	private String pcaName;

	public String getPcaName() {
		return pcaName;
	}

	public void setPcaName(String pcaName) {
		this.pcaName = pcaName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getVillageId() {
		return villageId;
	}

	public void setVillageId(Long villageId) {
		this.villageId = villageId;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getDetailedAddress() {
		return detailedAddress;
	}

	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}



	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof  UserAddress)
		{
			if(((UserAddress)o).id.equals(id))
			{
				return true;
			}

		}
		return false;
	}

	@Override
	public String toString() {
		return "UserAddress [id=" + id + ", name=" + name + ", countryId="
				+ countryId + ", provinceId=" + provinceId + ", cityId="
				+ cityId + ", areaId=" + areaId + ", villageId=" + villageId
				+ ", villageName=" + villageName + ", detailedAddress="
				+ detailedAddress + ", isDefault=" + isDefault
				+ ", mobilePhone=" + mobilePhone + ", address=" + address
				+ ", provinceName=" + provinceName + ", cityName=" + cityName
				+ ", areaName=" + areaName + ", pcaName=" + pcaName + "]";
	}

}
