package com.zxly.o2o.model;

/**
 *     @author dsnx  @version 创建时间：2015-1-10 上午11:52:37    类说明: 
 */
public class ProductBrand {

	private Integer id;
	private String name;
	private Integer typeId;
	public ProductBrand(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public ProductBrand() {
	}
	
	public Integer getId() {
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

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ProductBrand) {
			if (((ProductBrand) o).id == this.id) {
				return true;
			}
		}
		return false;
	}

}
