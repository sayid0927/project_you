package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

public class CollectTopic {
    private long topicId;
    private long collectTime;
    private String circleName;
    private String content;
    private int praiseAmout;
    private int replyAmout;
    private long createTime;
    private String uName;
    private String userThumHeadUrl;
    private String tablename;
    private boolean priseFlag;
    private int isShopTopic;
    private String originImageUrls;
    private String thumImageUrls;
    private int publishMan;

    private List<String> originImageList = new ArrayList<String>();

    private List<String> thumImageList = new ArrayList<String>();

    public int getPublishMan() {
        return publishMan;
    }

    public void setPublishMan(int publishMan) {
        this.publishMan = publishMan;
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

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getUserThumHeadUrl() {
        return userThumHeadUrl;
    }

    public void setUserThumHeadUrl(String userThumHeadUrl) {
        this.userThumHeadUrl = userThumHeadUrl;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public boolean isPriseFlag() {
        return priseFlag;
    }

    public void setPriseFlag(boolean priseFlag) {
        this.priseFlag = priseFlag;
    }

    public String getOriginImageUrls() {
        return originImageUrls;
    }

    public void setOriginImageUrls(String originImageUrls) {
        this.originImageUrls = originImageUrls;
    }

    public String getThumImageUrls() {
        return thumImageUrls;
    }

    public void setThumImageUrls(String thumImageUrls) {
        this.thumImageUrls = thumImageUrls;
    }

}
