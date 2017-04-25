package com.zxly.o2o.model;

/**
 *     @author dsnx  @version 创建时间：2015-1-6 下午7:16:34    类说明: 
 */
public class Sort {
	private int field;
	private String order;

	/**
	 * 
	 * @param field
	 *            1:人气排序,2:上新排序,3：价格
	 * 
	 * @param order
	 *            ASC:升序 DESC:降序
	 */
	public Sort(int field, String order) {
		this.field = field;
		this.order = order;
	}

	public int getField() {
		return field;
	}

	public void setField(int field) {
		this.field = field;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
