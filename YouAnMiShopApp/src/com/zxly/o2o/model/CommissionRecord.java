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
    private String incomeNo;
    private String timeStr;
    private long reduceTimes;
    private int status; //2.已到账，1待收入，3已退款
    private String numberNo;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberNo() {
        return numberNo;
    }

    public void setNumberNo(String numberNo) {
        this.numberNo = numberNo;
    }

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

    public String getIncomeNo() {
        return incomeNo;
    }

    public void setIncomeNo(String incomeNo) {
        this.incomeNo = incomeNo;
    }

    public long getReduceTimes() {
        return reduceTimes;
    }

    public void setReduceTimes(long reduceTimes) {
        this.reduceTimes = reduceTimes;
    }
}
