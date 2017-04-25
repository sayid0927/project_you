package com.zxly.o2o.model;

import java.io.Serializable;

public class UserBankCard implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    private long id;
    private String bankNumber;//卡号
    private int isDefault;// 1:默认
    private String bankName;//银行名称
    private String bankNo;//银行编号
    private int bankType;// 1:储蓄卡 2:信用卡
    private String idCard;//身份证号
    private String userName;//身份证名
    private String imageBanner;
    private int withdrawType;//1：允许提现的银行，2：不允许提现的银行

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public int getBankType() {
        return bankType;
    }

    public void setBankType(int bankType) {
        this.bankType = bankType;
    }

    public String getImageBanner() {
        return imageBanner;
    }

    public void setImageBanner(String imageBanner) {
        this.imageBanner = imageBanner;
    }

    public int getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(int withdrawType) {
        this.withdrawType = withdrawType;
    }

    @Override
    public String toString() {
        return "UserBankCard{" +
                "id=" + id +
                ", bankNumber='" + bankNumber + '\'' +
                ", isDefault=" + isDefault +
                ", bankName='" + bankName + '\'' +
                ", bankNo='" + bankNo + '\'' +
                ", bankType=" + bankType +
                ", idCard='" + idCard + '\'' +
                ", userName='" + userName + '\'' +
                ", imageBanner='" + imageBanner + '\'' +
                '}';
    }
}
