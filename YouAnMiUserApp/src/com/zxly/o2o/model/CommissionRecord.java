package com.zxly.o2o.model;

/**
 * Created by kenwu on 2015/12/11.
 */
public class CommissionRecord {

    public static final int COMMISSION_STATUS_WILLARRIVE=1;
    public static final int COMMISSION_STATUS_ARRIVED=2;
    public static final int COMMISSION_STATUS_CANCLE =3;

    private String orderNo;
    private float commission;
    private String timeStr;
    private int status; //2.已到账，1待收入，3已退款


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
