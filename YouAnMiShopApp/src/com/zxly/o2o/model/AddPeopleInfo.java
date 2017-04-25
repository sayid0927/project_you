package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/8/30.
 */
public class AddPeopleInfo {
    private String headUrl="";
    private String userName="";
    private String phoneNum="";
    private boolean isCheck;
    private String firstLetter="";

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public AddPeopleInfo(String headUrl, String userName, String phoneNum, boolean isCheck) {
        this.headUrl = headUrl;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.isCheck = isCheck;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
