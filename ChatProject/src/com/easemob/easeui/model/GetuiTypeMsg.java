package com.easemob.easeui.model;

import com.easemob.chat.EMMessage;

/**
 * Created by Administrator on 2016/7/18.
 */
public class GetuiTypeMsg {
    //消息id
    private long id;
    //消息已读状态 1 已读 0 未读
    private int status;
    //消息体字符串
    private String body="";
    //每条消息的字段
    private int busId;
    private int what;
    private String expend="";
    private long createTime;
    /**消息详情地址*/
    private String h5Url="";
    /**数据id*/
    private long dataId;
    /**消息简略图*/
    private String headUrl="";
    /**消息标题*/
    private String title="";
    /**消息内容*/
    private String content="";

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public GetuiTypeMsg(long id, int status, String body, int busId, int what, String expend, long createTime, String h5Url, long dataId, String headUrl, String title, String content) {
        this.id = id;
        this.status = status;
        this.body = body;
        this.busId = busId;
        this.what = what;
        this.expend = expend;
        this.createTime = createTime;
        this.h5Url = h5Url;
        this.dataId = dataId;
        this.headUrl = headUrl;
        this.title = title;
        this.content = content;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public String getExpend() {
        return expend;
    }

    public void setExpend(String expend) {
        this.expend = expend;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public GetuiTypeMsg(int status, long id, String body, int busId, int what, String expend, long createTime) {
        this.status = status;
        this.id = id;
        this.body = body;
        this.busId = busId;
        this.what = what;
        this.expend = expend;
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
