package com.zxly.o2o.model;

import java.io.Serializable;

public class ShopDiscount implements Serializable {
    /**
     * 优惠券id
     */
    private long id;
    /**
     * 优惠标题
     */
    private String title;
    /**
     * 优惠描述
     */
    private String discountInfo;
    /**
     * 优惠类型 1.现金折扣 2.礼品赠送
     */
    private int discountType;
    /**
     * 截止日期
     */
    private long endTime;
    /**
     * 起始日期
     */
    private long startTime;
    /**
     * 状态    1.正常 2.已使用 3.已过期.4.不可用
     */
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscountInfo() {
        return discountInfo;
    }

    public void setDiscountInfo(String discountInfo) {
        this.discountInfo = discountInfo;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
