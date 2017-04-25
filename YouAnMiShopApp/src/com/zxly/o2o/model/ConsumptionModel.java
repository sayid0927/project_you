package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/9/18.
 */
public class ConsumptionModel {
    private String content;
    private long creteTime;
    private int isNew;
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreteTime() {
        return creteTime;
    }

    public void setCreteTime(long creteTime) {
        this.creteTime = creteTime;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
