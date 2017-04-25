package com.zxly.o2o.model;

import java.io.Serializable;
import java.util.ArrayList;

public class InsuranceProduct implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 延保产品id
     */
    private long id;
    /**
     * 产品图片
     */
    private String image;
    /**
     * 产品名称
     */
    private String name="";
    /**
     * 价格
     */
    private float price;
    /**
     * 延保成品订单id
     */
    private long orderId;
    /**
     * 订单状态
     */
    private int orderStatus;
    /**
     * 订单状态
     */
    private String orderNo;
    /**
     * 理赔次数
     */
    private int compensateNum;
    /**
     * 简介
     */
    private String introduction;
    /**
     * 价格售价结束区间
     */
    private float priceEndInterval;
    /**
     * 价格售价开始区间
     */
    private float priceStartInterval;
    /**
     * 服务期, 单位月数
     */
    private int servicePeriod;
    /**
     * 价格列表
     */
    private ArrayList<InsurancePrice> prices;
    /**
     * 产品状态 1：下架，2：上架
     */
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getCompensateNum() {
        return compensateNum;
    }

    public void setCompensateNum(int compensateNum) {
        this.compensateNum = compensateNum;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public float getPriceEndInterval() {
        return priceEndInterval;
    }

    public void setPriceEndInterval(float priceEndInterval) {
        this.priceEndInterval = priceEndInterval;
    }

    public float getPriceStartInterval() {
        return priceStartInterval;
    }

    public void setPriceStartInterval(float priceStartInterval) {
        this.priceStartInterval = priceStartInterval;
    }

    public int getServicePeriod() {
        return servicePeriod;
    }

    public void setServicePeriod(int servicePeriod) {
        this.servicePeriod = servicePeriod;
    }

    public ArrayList<InsurancePrice> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<InsurancePrice> prices) {
        this.prices = prices;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
