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
    private List<BuyItem> orderItems;
    private String name;
    private String address="";
    private String phone;
    private Long createTime;
    private Integer deliveryType;
    private Long deliveryTime;
    private long residueTime;


    public List<BuyItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<BuyItem> orderItems) {
		this.orderItems = orderItems;
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



    public long getResidueTime() {
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

	public Long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

	public List<BuyItem> getBuyItems() {
		return buyItems;
	}

	public void setBuyItems(List<BuyItem> buyItems) {
		this.buyItems = buyItems;
	}


    
    
    
}
