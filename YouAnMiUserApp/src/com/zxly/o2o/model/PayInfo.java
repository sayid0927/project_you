package com.zxly.o2o.model;

import java.io.Serializable;

/**
 * @author fengrongjian 2015-12-21
 * @description 支付信息
 */
public class PayInfo implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;

    private int type;
    private String payNo;
    private String payType;
    private float orderMoney;
    private UserBankCard userBankCard;
    private String desc;
    private String moneyInput;

    public PayInfo(int type, String payNo, String payType, float orderMoney, UserBankCard userBankCard) {
        this.type = type;
        this.payNo = payNo;
        this.payType = payType;
        this.orderMoney = orderMoney;
        this.userBankCard = userBankCard;
    }

    public PayInfo(int type, String payNo, String payType, float orderMoney, UserBankCard userBankCard, String desc, String moneyInput) {
        this(type, payNo, payType, orderMoney, userBankCard);
        this.desc = desc;
        this.moneyInput = moneyInput;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public float getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(float orderMoney) {
        this.orderMoney = orderMoney;
    }

    public UserBankCard getUserBankCard() {
        return userBankCard;
    }

    public void setUserBankCard(UserBankCard userBankCard) {
        this.userBankCard = userBankCard;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMoneyInput() {
        return moneyInput;
    }

    public void setMoneyInput(String moneyInput) {
        this.moneyInput = moneyInput;
    }
}
