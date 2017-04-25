package com.zxly.o2o.model;

import java.io.Serializable;

public class InsurancePrice implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 价格区间开始价格
     */
    private float beginPrice;
    /**
     * 价格区间结束价格
     */
    private float endPrice;
    /**
     * 排序
     */
    private int orderBy;
    /**
     * 统一售价
     */
    private float uniformPrice;

    public float getBeginPrice() {
        return beginPrice;
    }

    public void setBeginPrice(float beginPrice) {
        this.beginPrice = beginPrice;
    }

    public float getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(float endPrice) {
        this.endPrice = endPrice;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public float getUniformPrice() {
        return uniformPrice;
    }

    public void setUniformPrice(float uniformPrice) {
        this.uniformPrice = uniformPrice;
    }
}
