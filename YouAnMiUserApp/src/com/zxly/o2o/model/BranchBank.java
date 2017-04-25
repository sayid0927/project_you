package com.zxly.o2o.model;

import java.io.Serializable;

public class BranchBank implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;
	private String brabank_name;
	private String prcptcd;

	public String getBrabank_name() {
		return brabank_name;
	}

	public void setBrabank_name(String brabank_name) {
		this.brabank_name = brabank_name;
	}

	public String getPrcptcd() {
		return prcptcd;
	}

	public void setPrcptcd(String prcptcd) {
		this.prcptcd = prcptcd;
	}
}
