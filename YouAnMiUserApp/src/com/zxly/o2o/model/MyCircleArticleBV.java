package com.zxly.o2o.model;

/**
 * @author 作者huangbin:
 * @version 创建时间：2015-2-9 下午2:16:06 类说明
 */
public class MyCircleArticleBV {

	private long id;
	private boolean isUp;
	private boolean isDown;
	private boolean isCollected;

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

	public void setCollected(boolean isCollected) {
		this.isCollected = isCollected;
	}

	public boolean getCollected() {
		return isCollected;
	}

	public void setDown(boolean isDown) {
		this.isDown = isDown;
	}

	public boolean getDown() {
		return isDown;
	}

}
