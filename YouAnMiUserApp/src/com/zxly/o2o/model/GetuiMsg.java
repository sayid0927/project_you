package com.zxly.o2o.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/12.
 */
public class GetuiMsg implements Serializable{
    /**消息详情地址*/
    private String h5Url="";
    /**数据id*/
    private long dataId;
    /**消息类型*/
    private int what;
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

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
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

}
