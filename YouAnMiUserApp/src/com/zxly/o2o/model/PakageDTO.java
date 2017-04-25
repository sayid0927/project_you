package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

public class PakageDTO {


	private long pakageId;
	private  Float price;
	private int pcs;// 套餐数量
	private List<ProductDTO> products=new ArrayList<ProductDTO>();
	public long getPakageId() {
		return pakageId;
	}
	public void setPakageId(long id) {
		this.pakageId = id;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	
	
	public int getPcs() {
		return pcs;
	}
	public void setPcs(int pcs) {
		this.pcs = pcs;
	}
	
	public List<ProductDTO> getProducts() {
		return products;
	}
	public void addAllProduct(List<ProductDTO> list)
	{
		if(list!=null)
		{
			products.addAll(list);
		}
	}
	public void addProduct(ProductDTO product) {
		if (product != null) {
			products.add(product);
		}
	}
	@Override
	public boolean equals(Object o) {
	
		if(o instanceof Pakage)
		{
			if(((PakageDTO)o).pakageId==this.pakageId)
			{
				return true;
			}
		}
		return false;
	}
	
	

}
