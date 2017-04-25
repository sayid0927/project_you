package com.zxly.o2o.model;

/**
 * Created by dsnx on 2015/12/21.
 * 收益明细
 */
public class YieldDetail {

    public static final int YIEL_TYPE_PROUDCT=5;//商品
    public static final int YIEL_TYPE_FLOW=7;//流量
    public static final int YIEL_TYPE_YYFF=10;//应用分发
    public static final int YIEL_TYPE_DZD=11;//对账收入
    public static final int YIEL_TYPE_YB=12;//延保
    private int commissionStatus;//佣金状态
    private float money;
    private String serialNumber;//流水号
    private int payType;
    private long time;
    private int type;//2：商品，7：流量，10：应用分发
    private int id;
    private int status;//1,待到帐；2已到帐

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCommissionStatus() {
        return commissionStatus;
    }

    public void setCommissionStatus(int commissionStatus) {
        this.commissionStatus = commissionStatus;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
