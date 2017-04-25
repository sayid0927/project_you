/*
 * 文件名：TopicReply.java
 * 版权：Copyright 2014 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： TopicReply.java
 * 修改人：Administrator
 * 修改时间：2014年12月29日
 * 修改内容：新增
 */
package com.zxly.o2o.model;

/**
 * 帖子回复
 * <p/>
 * <pre>
 * </pre>
 *
 * @author Administrator
 * @version YIBA-O2O 2014年12月29日
 * @since YIBA-O2O
 */
public class TopicReply extends VariousBean {

    /**
     * 回复内容
     */
    private String content = "";


    /**
     * 楼层
     */
    private int floor;

    /**
     * 回复 -->别人的回复，被回复的用户的id
     */
    private long parentUserId;

    private int praiseAmount;

    private byte isPraise;

    /**
     * 回复 -->别人的回复，被回复的用户的name
     */
    private String parentNickname = "";

private TopicDetailHeadsUser replyer=new TopicDetailHeadsUser();

    /**
     * 父回复id
     */
    private long parentReplyId;

    private String parentContent;

    private ShopTopic topicVO = new ShopTopic();


    public byte getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(byte isPraise) {
        this.isPraise = isPraise;
    }

    public int getPraiseAmount() {
        return praiseAmount;
    }

    public void setPraiseAmount(int praiseAmount) {
        this.praiseAmount = praiseAmount;
    }

    public String getParentContent() {
        return parentContent;
    }

    public void setParentContent(String parentContent) {
        this.parentContent = parentContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setParentReplyId(long parent_reply_id) {
        this.parentReplyId = parent_reply_id;
    }

    public long getParentReplyId() {
        return parentReplyId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }


    public Long getParent_user_id() {
        return parentUserId;
    }

    public void setParent_user_id(Long parent_user_id) {
        this.parentUserId = parent_user_id;
    }

    public String getParentNickname() {
        return parentNickname;
    }

    public void setParentNickname(String parentNickname) {
        this.parentNickname = parentNickname;
    }

    public TopicDetailHeadsUser getReplyer() {
        return replyer;
    }

    public void setReplyer(TopicDetailHeadsUser replyer) {
        this.replyer = replyer;
    }

    public ShopTopic getTopicVO() {
        return topicVO;

    }

    public void setTopicVO(ShopTopic topicVO) {
        this.topicVO = topicVO;
    }


}
