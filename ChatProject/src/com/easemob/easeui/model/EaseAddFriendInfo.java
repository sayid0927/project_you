package com.easemob.easeui.model;

/**
 * Created by Administrator on 2015/12/29.
 */
public class EaseAddFriendInfo {

    public  long createTime;
    public int isBlack;  //1:否,2:是

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(int isBlack) {
        this.isBlack = isBlack;
    }
}
