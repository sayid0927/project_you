/*
 * 文件名：ProductDTO.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ProductDTO.java
 * 修改人：Administrator
 * 修改时间：2015年3月12日
 * 修改内容：新增
 */
package com.zxly.o2o.model;

import com.google.gson.annotations.Expose;

import java.util.List;



/**
 * TODO 二手商品列表
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     yuafei 
 * @version    YIBA-O2O 2015年3月12日
 * @since      YIBA-O2O
 */
public class SecondhandProductUploadInfo

{

	@Expose 
	List<UsedDesc> descs;
	
	@Expose                        
    private long shopId;           //门店Id
       
	@Expose 
    private String name;           //商品名称
    
	@Expose 
    private Float price;           //二手低价
     
	@Expose 
    private int assureParty;       //担保方（1：个人担保，2门店担保）
    
	@Expose 
    private String deprType;       //新旧类型
    
	@Expose 
    private String deprName;       //折旧类型中文名称
    
	@Expose 
    private Integer classId;       //类目ID
    
	@Expose 
    private String className;      //类目名称
	
	@Expose 
    private Integer brandId;       //品牌ID   

	@Expose 
    private String brandName;      //品牌名称
    

	@Expose 
    private Integer modelId;       //型号ID
    
	@Expose 
    private String modelName;       //型号名称
    
	@Expose 
    private Long assureTypeId;      //担保类型ID
    
	@Expose 
    private Long assureChargeId;    //担保收费类型ID

	@Expose 
    private String description;
       
	@Expose 
    private Contacts contact;      //联系人 (Address 包含在里面)
    
	
	public SecondhandProductUploadInfo() {
	}



	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public int getAssureParty() {
		return assureParty;
	}

	public void setAssureParty(int assureParty) {
		this.assureParty = assureParty;
	}

	public String getDeprType() {
		return deprType;
	}

	public void setDeprType(String deprType) {
		this.deprType = deprType;
	}

	public String getDeprName() {
		return deprName;
	}

	public void setDeprName(String deprName) {
		this.deprName = deprName;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Long getAssureTypeId() {
		return assureTypeId;
	}

	public void setAssureTypeId(Long assureTypeId) {
		this.assureTypeId = assureTypeId;
	}

	public Long getAssureChargeId() {
		return assureChargeId;
	}

	public void setAssureChargeId(Long assureChargeId) {
		this.assureChargeId = assureChargeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public List<UsedDesc> getDescs() {
		return descs;
	}


	public void setDescs(List<UsedDesc> descs) {
		this.descs = descs;
	}


	public Contacts getContact() {
		return contact;
	}


	public void setContact(Contacts contact) {
		this.contact = contact;
	}


    

    
}
