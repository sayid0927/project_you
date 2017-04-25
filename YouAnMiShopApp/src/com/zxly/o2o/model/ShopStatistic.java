/*
 * 文件名：ShopStatistic.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ShopStatistic.java
 * 修改人：wuchenhui
 * 修改时间：2015-6-23
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
 * @version    YIBA-O2O 2015-6-23
 * @since      YIBA-O2O
 */
public class ShopStatistic {

	private String name;
	private float count;
	
	public ShopStatistic() {;
	}	
	
	public ShopStatistic(String name, float count) {
		super();
		this.name = name;
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getCount() {
		return count;
	}
	public void setCount(float count) {
		this.count = count;
	}
	
	
}
