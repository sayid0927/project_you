package com.zxly.o2o.model;

import java.util.List;

/**
 * Created by wuchenhui on 2015/5/25.
 */
public class OrderInfo {

    private String orderNo;
    private Integer status;
    private Float prices;
    private List<BuyItem> buyItems;
    private String name;
    private String address;
    private String phone;
    private Long createTime;
    private Integer deliveryType;
    private Long confirmPickupDate;
    private Long residueTime;
    private String deliveryCode;

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getPrices() {
        return prices;
    }

    public void setPrices(Float prices) {
        this.prices = prices;
    }



    public Long getResidueTime() {
        return residueTime;
    }

    public void setResidueTime(Long residueTime) {
        this.residueTime = residueTime;
    }



    public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

  

    public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getConfirmPickupDate() {
        return confirmPickupDate;
    }

    public void setConfirmPickupDate(Long confirmPickupDate) {
        this.confirmPickupDate = confirmPickupDate;
    }

	public List<BuyItem> getBuyItems() {
		return buyItems;
	}

	public void setBuyItems(List<BuyItem> buyItems) {
		this.buyItems = buyItems;
	}


    
    
    
}
