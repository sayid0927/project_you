package com.zxly.o2o.model;

/**
 * Created by Administrator on 2016/6/12.
 */
public class BenefitListVO {
    private String discountInfo="";
    private String discountNo="";
    private String imageUrl="";
    private String userName="";

    private long id;
    private long takeTime;
    private long useTime;
    private long shopId;
    private long userId;

    private byte userType;
    private byte discountType;

    public String getDiscountInfo() {
        return discountInfo;
    }

    public String getDiscountNo() {
        return discountNo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public long getId() {
        return id;
    }

    public long getTakeTime() {
        return takeTime;
    }

    public long getUseTime() {
        return useTime;
    }

    public byte getUserType() {
        return userType;
    }

    public byte getDiscountType() {
        return discountType;
    }

    public long getShopId() {
        return shopId;
    }

    public long getUserId() {
        return userId;
    }
}
