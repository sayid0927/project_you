/*
 * 文件名：ProductModel.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ProductModel.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-20
 * 修改内容：新增
 */
package com.zxly.o2o.model;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-3-20
 * @since      YIBA-O2O
 */
public class ProductModel {
	private Integer id;
	private String name;
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
	@Override
	public String toString() {
		return "ProductModel [id=" + id + ", name=" + name + "]";
	}
	

}
