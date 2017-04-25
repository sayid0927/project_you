package com.zxly.o2o.model;

import com.zxly.o2o.util.StringUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author     dsnx
 * @version    YIBA-O2O 2014-12-29
 * @since      YIBA-O2O
 */
public class NewProduct extends BaseProduct implements Cloneable 
{
    
    private float preference=0;//优惠额度
	private long startTime;
    private long endTime;//活动结束时间
    private long currentTime;
	private int status;
    private int pcs=1; //购买数量
    private int enjoyAmount;//喜欢数量
	private float commission;
	private String url;

	private long residueTime; //活动剩余时间
	private int typeCode=0;      //活动类型  1:限时抢购  2:清仓 3: 套餐
	private int productId; //只有活动才用到此Id 服务器如此设计
	
	private float subPrice;
	private String buyItemId="";//只在下订单的时候有用
    private int isPakage=0;//是否是套餐 1是 0否
    private long pakageId;//所属套餐ID
    private long skuId;
    private String remark="";
    private List<ProductSKUParam> productSKUParamList = new ArrayList<ProductSKUParam>();
	private List<Skus> skuList = new ArrayList<Skus>();
    private float maxPrice;
    private String imageUrl;//广告图片
	private String shareUrl;
	private  float comission;

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public float getComission() {
		return comission;
	}

	public void setComission(float comission) {
		this.comission = comission;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBuyItemId() {
		return buyItemId;
	}

	public void setBuyItemId(String buyItemId) {
		this.buyItemId = buyItemId;
	}


	private Skus selSku;

    private String comboInfo;

    public String getComboInfo() {
        return comboInfo;
    }

    public void setComboInfo(String comboInfo) {
        this.comboInfo = comboInfo;
    }

	public int getEnjoyAmount() {
		return enjoyAmount;
	}

	public void setEnjoyAmount(int enjoyAmount) {
		this.enjoyAmount = enjoyAmount;
	}

	public long getPakageId() {
		return pakageId;
	}

	public void setPakageId(long pakageId) {
		this.pakageId = pakageId;
	}

	public int getIsPakage() {
        return isPakage;
    }

    public void setIsPakage(int isPakage) {
        this.isPakage = isPakage;
    }
    

    public String getRemark() {
    	if(StringUtil.isNull(remark))
    	{
    		if(selSku!=null)
    		{
    			String[] selValue=selSku.getParamComNames().split(",");
        		int size=productSKUParamList.size();
        		StringBuilder sb=new StringBuilder();
        		for(int i=0;i<size;i++)
        		{
        			ProductSKUParam psp=productSKUParamList.get(i);
        			if(i>0)
        			{
        				sb.append("；");
        			}
        			sb.append(psp.getDisplayName()).append(":").append(selValue[i]);
        		}
        		return sb.toString();
    		}
    		
    	}
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Skus getSelSku() {
		return selSku;
	}

	public void setSelSku(Skus sku) {
		this.selSku = sku;
		if(sku!=null)
		{
			this.skuId=sku.getId();
		}
		
	}

	public void addProductSKUParam(ProductSKUParam param)
	{
		if(param!=null)
		{
			productSKUParamList.add(param);
		}
	}
	public void addSku(Skus sku)
	{
		if(sku!=null)
		{
			skuList.add(sku);
		}
	}
    
    public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	
	public float getSubPrice() {
		return subPrice;
	}

	public void setSubPrice(float subPrice) {
		this.subPrice = subPrice;
	}



	public List<ProductSKUParam> getProductSKUParamList() {
		return productSKUParamList;
	}

	public List<Skus> getSkuList() {
		return skuList;
	}

	public float getPreference() {
		return preference;
	}

	

	public float getMaxPrice() {
		return maxPrice;
	}
	
	

	public String getCurPriceStr()
	{
		StringBuilder buil=new StringBuilder();
		if(preference>0)
		{
			if(maxPrice!=price)
			{
				
				buil.append("￥").append(StringUtil.getFormatPrice(price-preference)).append("—").append(StringUtil.getFormatPrice(maxPrice-preference));
			}else
			{
				buil.append("￥").append(StringUtil.getFormatPrice(price-preference));
			}
			
		}else
		{
			if(maxPrice!=price)
			{
				buil.append("￥").append(StringUtil.getFormatPrice(price)).append("—").append(StringUtil.getFormatPrice(maxPrice));
			}else
			{
				buil.append("￥").append(StringUtil.getFormatPrice(price));
			}
			
		}
		return buil.toString();
	}
	public String getOrigPriceStr()
	{
		StringBuilder buil=new StringBuilder();
		if(maxPrice!=price)
		{
			buil.append("￥").append(StringUtil.getFormatPrice(price)).append("—").append(StringUtil.getFormatPrice(maxPrice));
		}else
		{
			buil.append("￥").append(StringUtil.getFormatPrice(price));
		}
		return buil.toString();
	}

	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}

	public void setPreference(float preference) {
		this.preference = preference;
	}

	
    
  
    
    public int getPcs() {
		return pcs;
	}

	public void setPcs(int pcs) {
		this.pcs = pcs;
	}


	
    
    public float getCurPrice() {
		if(selSku!=null)
		{
			return selSku.getPrice()-preference;
		}
    	return this.price-preference;
    	
		
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}

	public long getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(long endTime)
    {
        this.endTime = endTime;
    }

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getResidueTime() {
		return residueTime;
	}

	public void setResidueTime(long residueTime) {
		this.residueTime = residueTime;
	}

	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeId) {
		this.typeCode = typeId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	
	
	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public float getCommission() {
		return commission;
	}

	public void setCommission(float commission) {
		this.commission = commission;
	}


	@Override
	public boolean equals(Object o) {
		if (o instanceof NewProduct) {
			if (((NewProduct) o).id == this.id) {
				Skus sku1=((NewProduct) o).selSku;
				 if(selSku!=null&&sku1!=null)
				 {
					 if(selSku.equals(sku1))
					 {
						 if(this.buyItemId.equals(((NewProduct) o).buyItemId))
						 {
							 return true;
						 }else
						 {
							 return false;
						 }
						
					 }else
					 {
						 return false;
					 }
				 }else
				 {
					 return true;
				 }
				
			}
		}
		return false;
	}

	public NewProduct clone(){ 
		 NewProduct o = null; 
		try{ 
		o = ( NewProduct)super.clone(); 
		}catch(CloneNotSupportedException e){ 
		e.printStackTrace(); 
		} 
		return o; 
		} 
    
	
}
