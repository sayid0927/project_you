/*
 * 文件名：Product.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： Product.java
 * 修改人：wuchenhui
 * 修改时间：2015-6-24
 * 修改内容：新增
 */
package com.zxly.o2o.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

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
 * @version    YIBA-O2O 2015-6-24
 * @since      YIBA-O2O
 */
public class Product implements Parcelable ,Comparable{

	private long id;
	private String name = "";
	private Float price;
    private Float rate=0f;
    private Float preference=0f;//优惠额度
    private Float comission =0f;    // 佣金
    private int type =0;      //活动类型  1。限时抢购  2。清仓
    private String headUrl = "";// 商品头像缩略图
	private String shareUrl;
	private int typeCode=0;      //活动类型  1。限时抢购  2。清仓
    private int status;//1：待上架，2上架,3下架  （1:正常 2:退款中 3:退款成功   在退款中的状态意义）

    public Float getPreference() {
        return preference;
    }

    public void setPreference(Float preference) {
        this.preference = preference;
    }

	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
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
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Float getComission() {
		return comission;
	}
	public void setComission(Float comission) {
		this.comission = comission;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Product)
        {
           if(((Product)o).id==this.id)
           {
               return true;
           }
        }
        return false;
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeFloat(price);
		dest.writeFloat(rate);
		dest.writeFloat(preference);
		dest.writeFloat(comission);
		dest.writeInt(type);
		dest.writeString(headUrl);
		dest.writeString(shareUrl);
		dest.writeInt(typeCode);
		dest.writeInt(status);
	}
	public static final Creator<Product> CREATOR = new Creator<Product>()
	{
		@Override
		public Product[] newArray(int size)
		{
			return new Product[size];
		}

		@Override
		public Product createFromParcel(Parcel in)
		{
			return new Product(in);
		}
	};

	public Product(){}
	public Product(Parcel in)
	{
		id=in.readLong();
		name=in.readString();
		price=in.readFloat();
		rate=in.readFloat();
		preference=in.readFloat();
		comission=in.readFloat();
		type=in.readInt();
		headUrl=in.readString();
		shareUrl=in.readString();
		typeCode=in.readInt();
		status=in.readInt();
	}

	@Override
	public int compareTo(Object o) {
		Product b = null;
		if(o instanceof Product)
			b = (Product)o;
		if(b.id==this.id)
		{
			b.rate=this.rate;
			b.comission=this.comission;
		}
		return (int) (this.id - b.id);
	}

}
