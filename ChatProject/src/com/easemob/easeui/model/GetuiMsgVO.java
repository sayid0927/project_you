package com.easemob.easeui.model;

/**
 * Created by Administrator on 2016/7/18.
 */
public class GetuiMsgVO {
    private int busId;
    private int what;
    private String expend="";
    private long createTime;

    public GetuiMsgVO(int busId, int what, String expend, long createTime) {
        this.busId = busId;
        this.what = what;
        this.expend = expend;
        this.createTime = createTime;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public String getExpend() {
        return expend;
    }

    public void setExpend(String expend) {
        this.expend = expend;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
