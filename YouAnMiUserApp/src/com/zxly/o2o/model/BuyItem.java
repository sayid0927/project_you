package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

public class BuyItem {
	private String itemId;
	private long id;
	private int type;//1:单个商品限时抢 2 单个商品清仓 3 套餐
	private float price;
	private int pcs;
	private long pakageId;
	private float prefPrice;//优惠总价
	private List<NewProduct> productList=new ArrayList<NewProduct>();
	private int status;
	private long refundId;
	private List<NewProduct> products=new ArrayList<NewProduct>();
    private long residueTime;


	public List<NewProduct> getProducts() {
		return products;
	}
	public void setProducts(List<NewProduct> products) {
		this.products = products;
	}

	public float getPrefPrice() {
		return prefPrice;
	}

	public void setPrefPrice(float prefPrice) {
		this.prefPrice = prefPrice;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setProductList(List<NewProduct> productList) {
		this.productList = productList;
	}
	public long getPakageId() {
		return pakageId;
	}
	public void setPakageId(long pakageId) {
		this.pakageId = pakageId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getPcs() {
		return pcs;
	}
	public void setPcs(int pcs) {
		this.pcs = pcs;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<NewProduct> getProductList() {
		return productList;
	}

	public long getRefundId() {
		return refundId;
	}

	public void setRefundId(long refundId) {
		this.refundId = refundId;
	}

	public long getResidueTime() {
		return residueTime;
	}

	public void setResidueTime(long residueTime) {
		this.residueTime = residueTime;
	}

	public void addProduct(NewProduct np)
	{
		if(np!=null)
		{
			productList.add(np);
		}
	}
	public void addAllProduct(List<NewProduct> list)
	{
		if(list!=null&&!list.isEmpty())
		{
			productList.addAll(list);
		}
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof BuyItem) {
			if (((BuyItem) o).id == this.id) {
				return true;
			}
		}
		return false;
	}
	
	
}
