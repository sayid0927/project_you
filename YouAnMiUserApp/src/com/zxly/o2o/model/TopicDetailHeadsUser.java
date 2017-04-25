package com.zxly.o2o.model;

/**
 * Created by Administrator on 2015/12/28.
 */
public class TopicDetailHeadsUser {
    private  long id;
    private  String nickname="";
    private  String thumHeadUrl="";
    private String signature="";

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getThumHeadUrl() {
        return thumHeadUrl;
    }

    public void setThumHeadUrl(String thumHeadUrl) {
        this.thumHeadUrl = thumHeadUrl;
    }
}
