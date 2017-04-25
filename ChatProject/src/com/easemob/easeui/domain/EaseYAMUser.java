package com.easemob.easeui.domain;

/**
 * Created by Administrator on 2015/12/9.
 */
public class EaseYAMUser {

    private EaseUser firendsUserInfo = new EaseUser("");

    private String remarkName = "";

    private long shopId;

    //    private long  friendUserId;

    private long createTime;


    private boolean isCheck;

    private int isBlack;   //1:false 2:true

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public EaseUser getFirendsUserInfo() {
        return firendsUserInfo;
    }

    public void setFirendsUserInfo(EaseUser firendsUserInfo) {
        this.firendsUserInfo = firendsUserInfo;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public int getIsBlack() {
        return isBlack;
    }

    public void setIsBlack(int isBlack) {
        this.isBlack = isBlack;
    }


    //    public long getFriendUserId() {
    //        return friendUserId;
    //    }
    //
    //    public void setFriendUserId(long friendUserId) {
    //        this.friendUserId = friendUserId;
    //    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


}
