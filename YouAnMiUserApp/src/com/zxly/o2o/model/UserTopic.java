package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

public class UserTopic {
    private long id;
    private String content;
    private int praiseAmout;
    private int replyAmout;
    private long createTime;
    private byte isPraise;

    private int isShopTopic;
    private String circleName;

    private List<String> originImageList = new ArrayList<String>();

    private List<String> thumImageList = new ArrayList<String>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPraiseAmout() {
        return praiseAmout;
    }

    public void setPraiseAmout(int praiseAmout) {
        this.praiseAmout = praiseAmout;
    }

    public int getReplyAmout() {
        return replyAmout;
    }

    public byte getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(byte isPraise) {
        this.isPraise = isPraise;
    }

    public void setReplyAmout(int replyAmout) {
        this.replyAmout = replyAmout;
    }

    public int getIsShopTopic() {
        return isShopTopic;
    }

    public void setIsShopTopic(int isShopTopic) {
        this.isShopTopic = isShopTopic;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public List<String> getOriginImageList() {
        return originImageList;
    }

    public void setOriginImageList(List<String> originImageList) {
        this.originImageList = originImageList;
    }

    public List<String> getThumImageList() {
        return thumImageList;
    }

    public void setThumImageList(List<String> thumImageList) {
        this.thumImageList = thumImageList;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
