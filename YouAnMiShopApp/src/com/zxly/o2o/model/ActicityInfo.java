package com.zxly.o2o.model;

/**
 * Created by kenwu on 2016/3/11.
 * 推荐活动详情
 */
public class ActicityInfo {

    public static final int TYPE_GAME=1;
    public static final int TYPE_DSD=2;// 剁手党
    public static final int TYPE_DDYH=3;// 到店优惠
    public static final int TYPE_DZP=4;// 大转盘

    private int id;
    private String downloadUrl;
    private String imageUrl;
    private String popuDesc;
    private String popuName;
    private String shareUrl;
    private String appName;
    private int readAmount;
    private int shareAmount;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPopuDesc() {
        return popuDesc;
    }

    public void setPopuDesc(String popuDesc) {
        this.popuDesc = popuDesc;
    }

    public String getPopuName() {
        return popuName;
    }

    public void setPopuName(String popuName) {
        this.popuName = popuName;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getReadAmount() {
        return readAmount;
    }

    public void setReadAmount(int readAmount) {
        this.readAmount = readAmount;
    }

    public int getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(int shareAmount) {
        this.shareAmount = shareAmount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
