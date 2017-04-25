package com.zxly.o2o.model;

/**
 *     @author dsnx  @version 创建时间：2015-1-10 上午11:51:47    类说明: 
 */
public class ProductType {

	private int id;
	private String name;

	public ProductType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		return this.name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ProductType) {
			if (((ProductType) o).id == this.id) {
				return true;
			}
		}
		return false;
	}
}
