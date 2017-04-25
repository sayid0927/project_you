package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

public class Pakage {

	private long pakageId;
	private  float price;
	private String name;
	private float maxPrefPrice;
	private int nameId;
	private int pcs;// 套餐数量
	private List<NewProduct> productList=new ArrayList<NewProduct>();

	public float getMaxPrefPrice() {
		return maxPrefPrice;
	}

	public void setMaxPrefPrice(float maxPrefPrice) {
		this.maxPrefPrice = maxPrefPrice;
	}

	public long getPakageId() {
		return pakageId;
	}
	public void setPakageId(long id) {
		this.pakageId = id;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNameId() {
		return nameId;
	}
	public void setNameId(int nameId) {
		this.nameId = nameId;
	}
	
	public int getPcs() {
		return pcs;
	}
	public void setPcs(int pcs) {
		this.pcs = pcs;
	}
	public List<NewProduct> getProductList() {
		return productList;
	}
	public void addAllProduct(List<NewProduct> list)
	{
		if(list!=null)
		{
			productList.addAll(list);
		}
	}
	public void addProduct(NewProduct product) {
		if (product != null) {
			productList.add(product);
		}
	}
	@Override
	public boolean equals(Object o) {
	
		if(o instanceof Pakage)
		{
			if(((Pakage)o).pakageId==this.pakageId)
			{
				return true;
			}
		}
		return false;
	}
	
	
}
