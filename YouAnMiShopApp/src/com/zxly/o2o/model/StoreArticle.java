package com.zxly.o2o.model;

/**
 * Created by dsnx on 2016/9/13.
 */
public class StoreArticle {
    private int articleId;
    private String headUrl;
    private int isProductArticle;
    private boolean isRecomend;
    private String[] labels;
    private int scanCount;
    private String title;
    private String url;
    private int hasNewLabel=-1;

    public int getHasNewLabel() {
        return hasNewLabel;
    }

    public void setHasNewLabel(int hasNewLabel) {
        this.hasNewLabel = hasNewLabel;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public boolean isProductArticle() {
        return isProductArticle==1?true:false;
    }

    public void setProductArticle(int productArticle) {
        isProductArticle = productArticle;
    }

    public boolean isRecomend() {
        return isRecomend;
    }

    public void setRecomend(boolean recomend) {
        isRecomend = recomend;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
