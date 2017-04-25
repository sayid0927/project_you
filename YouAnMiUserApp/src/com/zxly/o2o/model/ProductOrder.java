/*
 * 文件名：aa.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： aa.java
 * 修改人：wuchenhui
 * 修改时间：2015-1-29
 * 修改内容：新增
 */
package com.zxly.o2o.model;

import java.util.List;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-1-29
 * @since      YIBA-O2O
 */
public class ProductOrder
{
    private Long id=-1l;//      订单ID
    
    private String subCode="";//  32  预约单号
    
    private Float price=0f;//    (10,2)  付款
    
    private Float freight=0f;//    (10,2)  运费
    
    private Long createTime=0l;//        订单交易时间
    
    private String csName="";
    
    private String csAddress="";
    
    private String csPhone="";
    private int status;//1:预约中 ,2:预约成功, 3:预约被拒绝, 4:无效预约
    private int shopId;
    private String shopName;
    private ShopInfo shopInfo;
    
    private List<NewProduct> products;//     商品列表
    
    
    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getCsName()
    {
        return csName;
    }
    
    public void setCsName(String csName)
    {
        this.csName = csName;
    }
    
    public String getCsAddress()
    {
        return csAddress;
    }
    
    public void setCsAddress(String csAddress)
    {
        this.csAddress = csAddress;
    }
    
    public String getCsPhone()
    {
        return csPhone;
    }
    
    public void setCsPhone(String csPhone)
    {
        this.csPhone = csPhone;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getSubCode()
    {
        return subCode;
    }
    
    public void setSubCode(String subCode)
    {
        this.subCode = subCode;
    }
    
    public Float getPrice()
    {
        return price;
    }
    
    public void setPrice(Float price)
    {
        this.price = price;
    }
    
    public Float getFreight()
    {
        return freight;
    }
    
    public void setFreight(Float freight)
    {
        this.freight = freight;
    }
    
    public Long getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Long createTime)
    {
        this.createTime = createTime;
    }
    
    public List<NewProduct> getProducts()
    {
        return products;
    }
    
    public void setProducts(List<NewProduct> products)
    {
        this.products = products;
    }
    

	public ShopInfo getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(ShopInfo shopInfo) {
		this.shopInfo = shopInfo;
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof ProductOrder)
		{
			if(((ProductOrder)o).id==this.id)
			{
				return true;
			}
		}
		return false;
	}
    
    
}
