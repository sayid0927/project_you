package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/9/19.
 */
public class ReplyArticleModel {
    //评论内容
    private String content="";

    private String headUrl;
    private long id;
    //是否点赞  0--是 1--否
    private int isPraise;

    private String nickName;
    //点赞数
    private int praiseAmount;
    //评论人id
    private long replyerId;
    //是否屏蔽  1--否  2--是
    private int status;
    private long updateTime;

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

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPraiseAmount() {
        return praiseAmount;
    }

    public void setPraiseAmount(int praiseAmount) {
        this.praiseAmount = praiseAmount;
    }

    public long getReplyerId() {
        return replyerId;
    }

    public void setReplyerId(long replyerId) {
        this.replyerId = replyerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
