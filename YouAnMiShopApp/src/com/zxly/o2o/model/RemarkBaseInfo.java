package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/9/20.
 * 备注信息  消费轨迹  行为轨迹 实体类
 */
public class RemarkBaseInfo {
    private String content="";
    private long createTime;
    private String creatorName="";
    private int remarkType;//1 普通 2，电话回访 3，短信回访
    private int isNew;//是否新增
    private int type;//类型--现在还未用到
    private String salesman="";
    private String targetName="";
    private String targetType="";
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getRemarkType() {
        return remarkType;
    }

    public void setRemarkType(int remarkType) {
        this.remarkType = remarkType;
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
}
