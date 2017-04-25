package com.zxly.o2o.model;

public class ShopPrompt {

    private String name;
    private int count;
    private int type;//0:订单1：商品2：消息
    private int tag;

    public ShopPrompt() {
    }

    public ShopPrompt(String name, int count, int type, int tag) {
        super();
        this.name = name;
        this.count = count;
        this.type = type;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
