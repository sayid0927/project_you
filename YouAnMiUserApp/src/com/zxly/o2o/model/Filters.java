package com.zxly.o2o.model;

/**
 *     @author dsnx  @version 创建时间：2015-1-6 下午7:17:29    类说明: 
 */
public class Filters {
	private int key;
	private Object value;

	/**
	 * 
	 * @param key
	 *            1：商品类目 2: 店铺ID 3: 品牌  4:活动类型,5: 文章类型,6  寄卖类型
	 * 
	 * @param value
	 */
	public Filters(int key, Object value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void sort(int sortType) {

	}

}
