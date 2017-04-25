package com.zxly.o2o.model;

public class SortItem {
	private String typeCode;
	private String name;
	private String code;
	private int orderId;//顺序ID

	public SortItem(String name)
	{
		this.name=name;
	}
	public SortItem(String code,String name)
	{
		this.code=code;
		this.name=name;
	}
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return name;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof SortItem)
		{
			if(((SortItem)o).code==code)
			{
				return true;
			}
		}
		return false;
	}

}
