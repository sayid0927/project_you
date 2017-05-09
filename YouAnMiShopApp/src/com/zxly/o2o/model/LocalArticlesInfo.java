package com.zxly.o2o.model;

/**
 * Created by kenwu on 2016/3/11.
 * 热文列表
 */
public class LocalArticlesInfo {

    private  String headImage ;  //封面图
    private  long id;           //热文id
    private  String label;      //标签，英文逗号分隔
    private  int allReadNum;       //浏览数
    private  int shareNum ;     //分享数
    private String shareUrl;    //分享地址
    private String title;       //热文标题
    private long publishTime;   //发布时间 时间戳
    private  boolean isclick;
    //private String description;   //文章描述

    public boolean isclick() {
        return isclick;
    }

    public void setIsclick(boolean isclick) {
        this.isclick = isclick;
    }


//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getAllReadNum() {
        return allReadNum;
    }

    public void setAllReadNum(int allReadNum) {
        this.allReadNum = allReadNum;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
