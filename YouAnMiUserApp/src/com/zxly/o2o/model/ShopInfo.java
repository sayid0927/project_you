/*
 * 文件名：s.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： s.java
 * 修改人：wuchenhui
 * 修改时间：2015-1-16
 * 修改内容：新增
 */
package com.zxly.o2o.model;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * 
 * <pre>
 * </pre>
 * 
 * @author wuchenhui
 * @version YIBA-O2O 2015-1-16
 * @since YIBA-O2O
 */
public class ShopInfo {
	private Long id;

	private String name="";

	private String mobilePhone;

	private String address="";

	private String headUrl;

	private Long userId;

	private Byte isCollect;

	private String[] imageUrls;
	private short  type;//1:连锁店，2：普通门店

    private String lableNames;

	public ShopInfo(Long id, String name, String mobilePhone, String address,
			String headUrl, Long userId, Byte isCollect, String[] imageUrls,
			ShopDataAmount dataAmount) {
		super();
		this.id = id;
		this.name = name;
		this.mobilePhone = mobilePhone;
		this.address = address;
		this.headUrl = headUrl;
		this.userId = userId;
		this.isCollect = isCollect;
		this.imageUrls = imageUrls;
		this.dataAmount = dataAmount;
	}
	public ShopInfo(Long id, String name, String mobilePhone, String address,
			String headUrl, Long userId, Byte isCollect, String[] imageUrls) {
		super();
		this.id = id;
		this.name = name;
		this.mobilePhone = mobilePhone;
		this.address = address;
		this.headUrl = headUrl;
		this.userId = userId;
		this.isCollect = isCollect;
		this.imageUrls = imageUrls;
	}
	/**
	 * 门店的数据统计
	 */
	private ShopDataAmount dataAmount;

	/**
	 * 构造函数。
	 * 
	 */
	public ShopInfo() {
		super();
	}

	/**
	 * 构造函数。
	 * 
	 * @param id
	 * @param name
	 * @param mobilePhone
	 * @param address
	 * @param headUrl
	 * @param userId
	 * @param isCollect
	 * @param dataAmount
	 */
	public ShopInfo(Long id, String name, String mobilePhone, String address,
			String headUrl, Long userId, Byte isCollect,
			ShopDataAmount dataAmount) {
		super();
		this.id = id;
		this.name = name;
		this.mobilePhone = mobilePhone;
		this.address = address;
		this.headUrl = headUrl;
		this.userId = userId;
		this.isCollect = isCollect;
		this.dataAmount = dataAmount;
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Byte getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(Byte isCollect) {
		this.isCollect = isCollect;
	}

	public ShopDataAmount getDataAmount() {
		return dataAmount;
	}

	public void setDataAmount(ShopDataAmount dataAmount) {
		this.dataAmount = dataAmount;
	}

	public String[] getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String[] imageUrls) {
		this.imageUrls = imageUrls;
	}
	public String getLableNames() {
		return lableNames;
	}
	public void setLableNames(String lableNames) {
		this.lableNames = lableNames;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}
}
