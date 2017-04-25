package com.zxly.o2o.model;

/**
 * Created by wuchenhui on 2015/7/7.
 */
public class TaskInfo {
	private String name;
	private int targetValue;
	private int finishValue;
    private String unitName;
    private int type;
	private int curProgress;
    private int target;

	public int getCurProgress() {
		return curProgress;
	}

	public void setCurProgress(int curProgress) {
		this.curProgress = curProgress;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(int targetValue) {
		this.targetValue = targetValue;
	}
	public int getFinishValue() {
		return finishValue;
	}
	public void setFinishValue(int finishValue) {
		this.finishValue = finishValue;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
    

}
