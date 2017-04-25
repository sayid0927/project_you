package com.zxly.o2o.model;

public class ProductSKUParam {
	private int id;
	private String displayName;
	private ProductSKUValue[] productSKUValue;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public ProductSKUValue[] getProductSKUValue() {
		return productSKUValue;
	}


	public void setProductSKUValue(ProductSKUValue[] productSKUValue) {
		this.productSKUValue = productSKUValue;
	}


	@Override
	public boolean equals(Object o) {
	
		if(o instanceof ProductSKUParam)
		{
			if(((ProductSKUParam)o).id==this.id)
			{
				return true;
			}
		}
		return false;
	}
	
	
	
}
