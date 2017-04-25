package com.zxly.o2o.model;

public class BaseProduct {

	protected long id;
	protected String name = "";
	protected String headUrl = "";// 商品头像缩略图
	protected int status;//1：待上架，2上架,3下架  （1:正常 2:退款中 3:退款成功   在退款中的状态意义）
	protected float price;
	protected String remark="";
	protected float preference;//优惠额度;
	private int pcs=1; //购买数量


	public float getPreference() {
		return preference;
	}

	public void setPreference(float preference) {
		this.preference = preference;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPcs() {
		return pcs;
	}

	public void setPcs(int pcs) {
		this.pcs = pcs;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof BaseProduct) {
			if (((BaseProduct) o).id == this.id) {
				return true;
			}
		}
		return false;
	}
}
