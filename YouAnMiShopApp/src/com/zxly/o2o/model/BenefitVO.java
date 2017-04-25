package com.zxly.o2o.model;

/**
 * Created by Administrator on 2016/6/13.
 */
public class BenefitVO {
    private long discountId;
    private byte discountType;
    private String discountInfo;
    private boolean  isCheck;

    public long getDiscountId() {
        return discountId;
    }

    public byte getDiscountType() {
        return discountType;
    }

    public String getDiscountInfo() {
        return discountInfo;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
