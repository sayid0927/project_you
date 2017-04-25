package com.zxly.o2o.model;

/**
 * Created by fengrongjian on 2016/9/20.
 * 粉丝消费/行为轨迹
 */
public class FansBehavior {
    //行为类型
    private int type;
    //	是否新增
    private int isNew;
    //	添加时间
    private long createTime;
    //	内容
    private String content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
