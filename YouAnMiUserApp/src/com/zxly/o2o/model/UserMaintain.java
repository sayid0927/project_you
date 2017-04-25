/**
 * 
 */
package com.zxly.o2o.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * @author fengrongjian 2015-3-16
 * @description 保修单信息
 */
public class UserMaintain implements Serializable {
	/**  */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String maintainNo;
	private String productName;
	private Float price;
	private Long residueTime;
	private String imei;
	private ShopInfo shop;
	private Long effectTime;
	private int type;// 1首保，2续保
	private String desc;
	private byte isHomeView;
	private Bitmap bitmap;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMaintainNo() {
		return maintainNo;
	}

	public void setMaintainNo(String maintainNo) {
		this.maintainNo = maintainNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Long getResidueTime() {
		return residueTime;
	}

	public void setResidueTime(Long residueTime) {
		this.residueTime = residueTime;
	}

	public String getIMEI() {
		return imei;
	}

	public void setIMEI(String imei) {
		this.imei = imei;
	}

	public ShopInfo getShop() {
		return shop;
	}

	public void setShop(ShopInfo shop) {
		this.shop = shop;
	}

	public Long getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(Long effectTime) {
		this.effectTime = effectTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public byte getIsHomeView() {
		return isHomeView;
	}

	public void setIsHomeView(byte isHomeView) {
		this.isHomeView = isHomeView;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public String toString() {
		return "UserMaintain [maintainNo=" + maintainNo + ", productName="
				+ productName + ", residueTime=" + residueTime + ", imei="
				+ imei + ", shop=" + shop + ", desc=" + desc + "]";
	}

	
}
