/*
 * 文件名：TimeCutDownBean.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： TimeCutDownBean.java
 * 修改人：wuchenhui
 * 修改时间：2015-1-19
 * 修改内容：新增
 */
package com.zxly.o2o.model;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * 
 * <pre>
 * </pre>
 * 
 * @author wuchenhui
 * @version YIBA-O2O 2015-1-19
 * @since YIBA-O2O
 */
public class TimeCutDown extends NewProduct {

	private int timeStyle;  //设置样式
	private Long leavingTime;
	private long endTime;


	public int getTimeStyle() {
		return timeStyle;
	}
	
	public void setTimeStyle(int timeStyle) {
		this.timeStyle = timeStyle;
	}
	
	public Long getLeavingTime() {
		return leavingTime;
	}

	public void setLeavingTime(Long leavingTime) {
		this.leavingTime = leavingTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}



	
	
}
