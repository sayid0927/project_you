package com.zxly.o2o.model;

public class BaseProduct {

	protected long id;
	protected String name = "";
	protected String headUrl = "";// 商品头像缩略图
	protected int clickAmount;// 人气量/点击量
	protected int isParamType;// 参数是否含有属性（1：是，2：否）
	protected int type;// 1:新手 ,2:二手
	protected int status;//1：待上架，2上架,3下架  （1:正常 2:退款中 3:退款成功   在退款中的状态意义）
	protected String desc;
	protected int collect;// 商品收藏，1：是，2：否
    protected long shopId;

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    private int amount;
	protected float price;
	protected Contacts contact;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getParamType() {
		return isParamType;
	}

	public void setIsParamType(int isParamType) {
		this.isParamType = isParamType;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public int getClickAmount() {
		return clickAmount;
	}

	public void setClickAmount(int clickAmount) {
		this.clickAmount = clickAmount;
	}

	public int getCollect() {
		return collect;
	}

	public void setCollect(int collect) {
		this.collect = collect;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	

	public Contacts getContact() {
		return contact;
	}

	public void setContact(Contacts contact) {
		this.contact = contact;
	}

	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
