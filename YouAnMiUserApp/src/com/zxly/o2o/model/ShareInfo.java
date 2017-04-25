package com.zxly.o2o.model;

/**
 * Created by kenwu on 2016/3/18.
 */
public class ShareInfo {
    int id;
    int type;
    String title;
    String desc;
    Object shareIcon;
    String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public Object getShareIcon() {
        return shareIcon;
    }

    public void setShareIcon(Object shareIcon) {
        this.shareIcon = shareIcon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
