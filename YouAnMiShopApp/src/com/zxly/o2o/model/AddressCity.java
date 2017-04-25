package com.zxly.o2o.model;

import java.io.Serializable;

public class AddressCity implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    private String cityName;
    private String cityId;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

}
