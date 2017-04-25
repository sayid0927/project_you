package com.zxly.o2o.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 2015/7/20.
 */
public class MyCommissionVO {
    private BigDecimal totalCommission;

    private List<OrderCommVO> orderComms = new ArrayList<OrderCommVO>();

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    public List<OrderCommVO> getOrderComms() {
        return orderComms;
    }

    public void setOrderComms(
            List<OrderCommVO> orderComms) {
        this.orderComms = orderComms;
    }

}
