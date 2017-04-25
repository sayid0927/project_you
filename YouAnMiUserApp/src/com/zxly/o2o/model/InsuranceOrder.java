package com.zxly.o2o.model;

import java.io.Serializable;

public class InsuranceOrder implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 合同号
     */
    private String contractNum;

    /**
     * 支付类型(1:线上支付,2:月结支付)
     */

    private byte payType;

    /**
     * 合同生效时间

     */
    private long effectTime;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户手机
     */
    private String userPhone;
    /**
     * 手机Imei号
     */
    private String phoneImei;
    /**
     * 手机品牌
     */
    private String phoneBrand;
    /**
     * 手机型号
     */
    private String phoneModel;
    /**
     * 支付金额
     */
    private float price;
    /**
     * 保单状态
     */
    private int orderStatus;
    /**
     * 订单号
     */
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public byte getPayType() {
        return payType;
    }

    public void setPayType(byte payType) {
        this.payType = payType;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public long getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(long effectTime) {
        this.effectTime = effectTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPhoneImei() {
        return phoneImei;
    }

    public void setPhoneImei(String phoneImei) {
        this.phoneImei = phoneImei;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
