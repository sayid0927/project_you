package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/9/18.
 */
public class RemarkInfoModel extends RemarkBaseInfo{
    private String content;
    private long createTime;
    private String creatorName;
    private int remarkType;//1 普通 2，电话回访 3，短信回访

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
}
