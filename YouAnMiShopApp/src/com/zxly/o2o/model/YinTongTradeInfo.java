package com.zxly.o2o.model;

/**
 * Created by dsnx on 2015/5/29.
 * 用于连连支付处理的订单信息类
 */
public class YinTongTradeInfo {
    /**
     *
     * @param money_order  订单金额
     * @param dt_order 订单时间
     * @param no_order  订单号
     * @param notify_url  服务器异步通知地址
     */
    public YinTongTradeInfo(float money_order, String no_order, String dt_order, String notify_url) {
        this.money_order = money_order;
        this.no_order = no_order;
        this.dt_order = dt_order;
        this.notify_url = notify_url;
    }

    private float money_order;
    private String no_order;
    private String dt_order;
    private String notify_url;

    public float getMoney_order() {
        return money_order;
    }

    public String getNo_order() {
        return no_order;
    }

    public String getDt_order() {
        return dt_order;
    }

    public String getNotify_url() {
        return notify_url;
    }
}
