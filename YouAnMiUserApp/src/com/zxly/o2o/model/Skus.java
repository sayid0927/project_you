package com.zxly.o2o.model;

public class Skus {
	private long id;
	private float price;
	private String paramComNames="";
	private String paramComValues="";

	
	public String getParamComNames() {
		return paramComNames;
	}

	public void setParamComNames(String paramComNames) {
		this.paramComNames = paramComNames;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

	public String getParamComValues() {
		return paramComValues;
	}

	public void setParamComValues(String paramComValues) {
		this.paramComValues = paramComValues;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if(((Skus)o).id==this.id)
		{
			return true;
		}
		return false;
	}
	
	

}
