package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/9/19.
 * 会员行为数据类  包括消费行为（轨迹）及行为轨迹
 */
public class MenberBehaviorModel extends RemarkBaseInfo{
    private String content="";
    private long createTime;
    private int isNew;//是否新增
    private int type;//类型--现在还未用到



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
