package com.zxly.o2o.model;

/**
 * Created by Administrator on 2016/6/1.
 */
public class InsureImage {
    private String originImageUrl = "";
    private String thumImageUrl = "";
    private int isOK;  //0:default  1:ok  2:faile

    public int getIsOK() {
        return isOK;
    }

    public void setIsOK(int isOK) {
        this.isOK = isOK;
    }

    public String getOriginImageUrl() {
        return originImageUrl;
    }

    public void setOriginImageUrl(String originImageUrl) {
        this.originImageUrl = originImageUrl;
    }

    public String getThumImageUrl() {
        return thumImageUrl;
    }

    public void setThumImageUrl(String thumImageUrl) {
        this.thumImageUrl = thumImageUrl;
    }
}
