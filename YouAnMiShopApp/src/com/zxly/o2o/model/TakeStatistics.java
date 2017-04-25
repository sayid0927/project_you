package com.zxly.o2o.model;

/**
 * Created by dsnx on 2016/6/16.
 */
public class TakeStatistics {
    private String discountInfo;
    private int discountType;
    private int id;
    private int takePersons;
    private int usePersons;

    public String getDiscountInfo() {
        return discountInfo;
    }

    public void setDiscountInfo(String discountInfo) {
        this.discountInfo = discountInfo;
    }

    public int getDiscountType() {
        return discountType;
    }

    public void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTakePersons() {
        return takePersons;
    }

    public void setTakePersons(int takePersons) {
        this.takePersons = takePersons;
    }

    public int getUsePersons() {
        return usePersons;
    }

    public void setUsePersons(int usePersons) {
        this.usePersons = usePersons;
    }
}
