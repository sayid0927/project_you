package com.zxly.o2o.model;

/**
 * @author 作者huangbin:
 * @version 创建时间：2015-2-9 上午10:01:58 类说明
 */
public class MyCircleTopicBV {
	private long id;
	private boolean isUp;

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setUp(boolean isUp) {
		this.isUp = isUp;
	}

	public boolean getUp() {
		// TODO Auto-generated method stub
		return isUp;
	}
}
