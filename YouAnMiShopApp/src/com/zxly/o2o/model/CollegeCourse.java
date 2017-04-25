package com.zxly.o2o.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CollegeCourse implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    private long articleId;
    private long articleType;
    private String title;
    private String content;
    private long createTime;
    private int readAmount;//正在学习人数
    private String iconUrl;
    private ArrayList<String> lable = new ArrayList<String>();
    private String videoUrl;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public long getArticleType() {
        return articleType;
    }

    public void setArticleType(long articleType) {
        this.articleType = articleType;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getReadAmount() {
        return readAmount;
    }

    public void setReadAmount(int readAmount) {
        this.readAmount = readAmount;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public ArrayList<String> getLable() {
        return lable;
    }

    public void setLable(ArrayList<String> lable) {
        this.lable = lable;
    }
}
