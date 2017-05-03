package com.zxly.o2o.model;

/**
 * Created by kenwu on 2015/12/19.
 */
public class PromotionArticle {

    private long articleId;
    private String title = "";
    private String headUrl = "";
    private String url = "";
    public String h5Url="";
    private int scanCount;
    private long createTime;
    private String shareImageUrl="";
    private String userAppName="";
    private String description="";

    public String getShareImageUrl() {
        return shareImageUrl;
    }

    public void setShareImageUrl(String shareImageUrl) {
        this.shareImageUrl = shareImageUrl;
    }

    public String getUserAppName() {
        return userAppName;
    }

    public void setUserAppName(String userAppName) {
        this.userAppName = userAppName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int articleFrom;

    public int getArticleFrom() {
        return articleFrom;
    }

    public void setArticleFrom(int articleFrom) {
        this.articleFrom = articleFrom;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
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

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {

        boolean flag = obj instanceof PromotionArticle;
        if(!flag){
            return false;
        }
        PromotionArticle emp = (PromotionArticle)obj;
        if(this.getArticleId()==emp.getArticleId()){
            return true;
        }else {
            return false;
        }
    }
}
