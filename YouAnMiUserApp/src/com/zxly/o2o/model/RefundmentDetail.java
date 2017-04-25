package com.zxly.o2o.model;

/**
 * Created by Administrator on 2015/5/26.
 */
public class RefundmentDetail {

    private float refundPrice;
    private float realprice;
    private int status;
    private int refundType;
    private int pcs;
    private String headImage = "";
    private String refundNO = "";
    private long refundDate;
    private long id;
    private String orderNo = "";
    private String productName = "";
    private BuyItem buyItem=new BuyItem();
    private String refundReasonName="";
    private String imageUrls="";
    private String refundRemark="";
    private String shopRefund="";
    private long operateTime;


    public String getShopRefund() {
        return shopRefund;
    }

    public void setShopRefund(String shopRefund) {
        this.shopRefund = shopRefund;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(long operateTime) {
        this.operateTime = operateTime;
    }

    public long getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(long reundDate) {
        this.refundDate = reundDate;
    }

    public String getRefundRemark() {
        return refundRemark;
    }

    public void setRefundRemark(String refundRemark) {
        this.refundRemark = refundRemark;
    }

    public String getRefundReasonName() {
        return refundReasonName;
    }

    public void setRefundReasonName(String refundReasonName) {
        this.refundReasonName = refundReasonName;
    }

    public BuyItem getBuyItem() {
        return buyItem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setBuyItem(BuyItem buyItem) {
        this.buyItem = buyItem;
    }

    public float getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(float refundPrice) {
        this.refundPrice = refundPrice;
    }

    public float getRealprice() {
        return realprice;
    }

    public int getStatus() {
        return status;
    }

    public int getRefundType() {
        return refundType;
    }

    public int getPcs() {
        return pcs;
    }

    public String getHeadImage() {
        return headImage;
    }

    public String getRefundNO() {
        return refundNO;
    }

    public String getProductName() {
        return productName;
    }

    public void setRealprice(float realprice) {
        this.realprice = realprice;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setRefundType(int refundType) {
        this.refundType = refundType;
    }

    public void setPcs(int pcs) {
        this.pcs = pcs;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public void setRefundNO(String refundNO) {
        this.refundNO = refundNO;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
