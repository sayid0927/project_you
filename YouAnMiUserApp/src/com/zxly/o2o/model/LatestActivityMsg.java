package com.zxly.o2o.model;

/**
 * Created by Administrator on 2016/7/11.
 * 最新活动
 */
public class LatestActivityMsg {
    /** 图片地址*/
    private String headUrl="";
    /** 活动id*/
    private Integer id;
    /** 价格/价格区间*/
    private String price;
    /** 活动标题*/
    private String title;

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
