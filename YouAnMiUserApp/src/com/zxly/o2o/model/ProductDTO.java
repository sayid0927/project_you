package com.zxly.o2o.model;

/**
 * Created by dsnx on 2015/4/21.
 */
public class ProductDTO {
    private long id;
    private long skuId;
    private Float preprice;//原价
    private Float price;
    private Long pakageId;
    private int type;
    private int pcs;
    public void setId(long id) {
        this.id = id;
    }
    public void setSkuId(long skuId) {
        this.skuId = skuId;
    }
    public void setPcs(int pcs) {
        this.pcs = pcs;
    }

    public void setPreprice(float preprice) {
        this.preprice = preprice;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setPakageId(Long pakageId) {
        this.pakageId = pakageId;
    }

    public long getId() {
        return id;
    }

    public long getSkuId() {
        return skuId;
    }

    public float getPreprice() {
        return preprice;
    }

    public float getPrice() {
        return price;
    }

    public long getPakageId() {
        return pakageId;
    }

    public int getPcs() {
        return pcs;
    }
}
