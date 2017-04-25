package com.zxly.o2o.model;

/**
 * Author fengrongjian
 * Date 2015-07-30
 * Description TODO
 */
public class SpreadCommission {
    private int commissionCount;
    private float commission;
    private int refundCount;
    private float refund;
    private int paymentsCount;

    private float payments;

    public int getCommissionCount() {
        return commissionCount;
    }

    public void setCommissionCount(int commissionCount) {
        this.commissionCount = commissionCount;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public int getRefundCount() {
        return refundCount;
    }

    public void setRefundCount(int refundCount) {
        this.refundCount = refundCount;
    }

    public float getRefund() {
        return refund;
    }

    public void setRefund(float refund) {
        this.refund = refund;
    }

    public int getPaymentsCount() {
        return paymentsCount;
    }

    public void setPaymentsCount(int paymentsCount) {
        this.paymentsCount = paymentsCount;
    }

    public float getPayments() {
        return payments;
    }

    public void setPayments(float payments) {
        this.payments = payments;
    }
}
