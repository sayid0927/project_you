package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/14.
 */
public class CollectArticleModel {
    //1店铺热文 2平台专享
    private int number;
    //文章内容
    private String content;
    private String headUrl;
    private long id;
    private List<String> imageUrls=new ArrayList<String>();
    private int isPraise;
    private int isTop;
    private List<String> labels=new ArrayList<String>();
    private int praiseAmount;
    private int replyAmount;
    private String title;
    private long orgId;
    private int status;

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public int getPraiseAmount() {
        return praiseAmount;
    }

    public void setPraiseAmount(int praiseAmount) {
        this.praiseAmount = praiseAmount;
    }

    public int getReplyAmount() {
        return replyAmount;
    }

    public void setReplyAmount(int replyAmount) {
        this.replyAmount = replyAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CollectArticleModel{" +
                "number=" + number +
                ", content='" + content + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", id=" + id +
                ", imageUrls=" + imageUrls +
                ", isPraise=" + isPraise +
                ", isTop=" + isTop +
                ", labels=" + labels +
                ", praiseAmount=" + praiseAmount +
                ", replyAmount=" + replyAmount +
                ", title='" + title + '\'' +
                '}';
    }
}
