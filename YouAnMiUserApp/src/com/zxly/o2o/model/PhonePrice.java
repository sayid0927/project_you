package com.zxly.o2o.model;

public class PhonePrice {
    private String level;//价钱档次
    private String price;//价钱

    public PhonePrice(String level, String price) {
        this.level = level;
        this.price = price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return this.price;
    }
}
