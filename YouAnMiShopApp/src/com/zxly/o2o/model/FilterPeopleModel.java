package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/9/18.
 * 搜索实体类
 */
public class FilterPeopleModel {
    private String headUrl="";
    private long id;
    private String imei="";
    private String mobilePhone="";
    private String userName="";
    private int userType;
    private boolean isInputFans;
    private int intentionLevel;//购机意向

    public int getIntentionLevel() {
        return intentionLevel;
    }

    public void setIntentionLevel(int intentionLevel) {
        this.intentionLevel = intentionLevel;
    }

    public boolean isInputFans() {
        return isInputFans;
    }

    public void setInputFans(boolean inputFans) {
        isInputFans = inputFans;
    }

    /**
     * 1 粉丝  2 会员
     * @return
     */
    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
