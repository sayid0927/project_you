package com.zxly.o2o.model;

import java.io.Serializable;

/**
 * Author fengrongjian
 * Date 2015-12-22
 * Description TODO
 */
public class OperationInfo implements Serializable {
    /**
     * APP安装量
     */
    private int installAmount;
    /**
     * APP注册量
     */
    private int registerAmount;
    /**
     * 流量成交额
     */
    private float flowTurnover;
    /**
     * 商品成交额
     */
    private float productTurnover;
    /**
     * 店铺访客数
     */
    private int visitAmount;
    /**
     * 商品浏览量
     */
    private int clickAmount;

    public int getInstallAmount() {
        return installAmount;
    }

    public void setInstallAmount(int installAmount) {
        this.installAmount = installAmount;
    }

    public int getRegisterAmount() {
        return registerAmount;
    }

    public void setRegisterAmount(int registerAmount) {
        this.registerAmount = registerAmount;
    }

    public float getFlowTurnover() {
        return flowTurnover;
    }

    public void setFlowTurnover(float flowTurnover) {
        this.flowTurnover = flowTurnover;
    }

    public float getProductTurnover() {
        return productTurnover;
    }

    public void setProductTurnover(float productTurnover) {
        this.productTurnover = productTurnover;
    }

    public int getVisitAmount() {
        return visitAmount;
    }

    public void setVisitAmount(int visitAmount) {
        this.visitAmount = visitAmount;
    }

    public int getClickAmount() {
        return clickAmount;
    }

    public void setClickAmount(int clickAmount) {
        this.clickAmount = clickAmount;
    }
}
