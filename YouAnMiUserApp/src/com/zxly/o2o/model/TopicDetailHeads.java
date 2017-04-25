package com.zxly.o2o.model;

/**
 * Created by Administrator on 2015/12/28.
 */
public class TopicDetailHeads {
    private TopicDetailHeadsUser user=new TopicDetailHeadsUser();
    private byte isPraise;
    private long praiseTime;

    public byte getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(byte isPraise) {
        this.isPraise = isPraise;
    }

    public long getPraiseTime() {
        return praiseTime;
    }

    public void setPraiseTime(long praiseTime) {
        this.praiseTime = praiseTime;
    }

    public TopicDetailHeadsUser getUser() {
        return user;
    }

    public void setUser(TopicDetailHeadsUser user) {
        this.user = user;
    }
}
