package com.zxly.o2o.model;

import java.math.BigDecimal;

/**
 * Created by Benjamin on 2015/7/20.
 */
public  class OrderCommVO{
    private String orderId="";  //订单编号
    private String timeStr="";  //到帐时间
    private Integer status; //状态 1.已到账 2.预到账 3.已失效
    private String statusName=""; //状态的名字
    private String orderNo="";
    private BigDecimal orderCommission; //订单总佣金


    public String getOrderId() {
        return orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public BigDecimal getOrderCommission() {
        return orderCommission;
    }

    public void setOrderCommission(BigDecimal orderCommission) {
        this.orderCommission = orderCommission;
    }
}