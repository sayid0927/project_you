package com.zxly.o2o.model;

import java.io.Serializable;

public class UserOrder implements Serializable {
    private int refund;//退款
    private int waitAccept;//待收货
    private int waitAffirm;//待确认
    private int waitPay;//待付款

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public int getWaitAccept() {
        return waitAccept;
    }

    public void setWaitAccept(int waitAccept) {
        this.waitAccept = waitAccept;
    }

    public int getWaitAffirm() {
        return waitAffirm;
    }

    public void setWaitAffirm(int waitAffirm) {
        this.waitAffirm = waitAffirm;
    }

    public int getWaitPay() {
        return waitPay;
    }

    public void setWaitPay(int waitPay) {
        this.waitPay = waitPay;
    }
}
