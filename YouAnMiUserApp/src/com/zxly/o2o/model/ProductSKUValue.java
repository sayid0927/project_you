package com.zxly.o2o.model;

public class ProductSKUValue {
	private int id;
	private String paramValue;
	private String displyName;
	private int type=3;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getDisplyName() {
		return displyName;
	}

	public void setDisplyName(String displyName) {
		this.displyName = displyName;
	}

	/**
	 * 
	 * @return 1:已选择 ,2:不可选择,3:可选择
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {

		if (o instanceof ProductSKUValue) {
			if (((ProductSKUValue) o).id == this.id) {
				return true;
			}
		}
		return false;
	}

}
