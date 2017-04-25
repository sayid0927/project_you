package com.zxly.o2o.model;

import java.io.Serializable;
//活动类
public class Event implements Serializable {
	private static final long serialVersionUID = -8483044063088948856L;

	private int action;
	private String url;
	private String text;
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
