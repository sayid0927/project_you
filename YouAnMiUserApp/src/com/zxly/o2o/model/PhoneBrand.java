package com.zxly.o2o.model;

public class PhoneBrand {
    private long id;//品牌ID
    private String name;//品牌名称

    public PhoneBrand(int id, String name){
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
