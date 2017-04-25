package com.zxly.o2o.model;

import java.io.Serializable;

public class InsuranceSupplier implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 供应商电话（理赔电话）
     */
    private String contactPhone;
    /**
     * 供应商logo
     */
    private String logoUrl;
    /**
     * 服务时间
     */
    private String serviceTime;

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }
}
