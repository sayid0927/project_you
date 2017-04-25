package com.zxly.o2o.model;

/**
 * Created by dsnx on 2015/12/23.
 */
public class CommissionProduct extends BaseProduct {
     float commission;//佣金
    private String url;//推广链接（后台如此命名）

    private boolean isCheck;
    private long productId;

    private float activityPrice;  //活动价格

    public float getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(float activityPrice) {
        this.activityPrice = activityPrice;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public float getCommission() {
        return commission;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object obj) {

        boolean flag = obj instanceof CommissionProduct;
        if(!flag){
            return false;
        }
        CommissionProduct object = (CommissionProduct)obj;
        if(this.getProductId()==object.getProductId()){
            return true;
        }else {
            return false;
        }
    }
}
