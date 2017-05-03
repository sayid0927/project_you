package com.zxly.o2o.model;

import java.util.List;

/**
 * Created by kenwu on 2016/4/26.
 */
public class GuaranteeInfo {

    public static final int STATUS_WAIT_FOR_CONFIRN=1; //待审核(已在用户app下单)
    public static final int STATUS_CANCELED=2;
    public static final int STATUS_REFUSED=3;
    public static final int STATUS_WAIT_FOR_PAY=4;
    public static final int STATUS_PAY_TIMEOUT=5;
    public static final int STATUS_IN_REVIEW=6;
    public static final int STATUS_IN_GUARANTEE=7;
    public static final int STATUS_REFUNDED=8;
    public static final int STATUS_OVERDUE=9;
    public static final int STATUS_MODIFY=10;//修改订单资料


    private int id; //延保单id

    private String orderNo;

    private double price;

    private Long anotherPayId; //代付人ID

    //延保订单状态,1:已申购,2:已取消,3:已拒单,4:待支付,5:支付超时,6:审核中,7:保障中,8:已退单,9:已过期
    private int orderStatus;

    private long createTime; //订单创建时间

    private long payTime;

    private byte payType;

    private long  updateTime;

    private String contractNum;//合同号

    private long  effectTime; //合同生效时间

    private InsuranceProduct product;

    private MOrderInfo orderInfo;


    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getAnotherPayId() {
        return anotherPayId;
    }

    public void setAnotherPayId(Long anotherPayId) {
        this.anotherPayId = anotherPayId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public long getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(long effectTime) {
        this.effectTime = effectTime;
    }

    public InsuranceProduct getProduct() {
        return product;
    }

    public void setProduct(InsuranceProduct product) {
        this.product = product;
    }

    public MOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(MOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public byte getPayType() {
        return payType;
    }

    public void setPayType(byte payType) {
        this.payType = payType;
    }

    public static class MOrderInfo{
        private String  phoneModel;
        private double  phonePrice;
        private String phoneImei;
        private String userName;
        private String userPhone;
        private String thumImageUrl;
        private String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getThumImageUrl() {
            return thumImageUrl;
        }

        public void setThumImageUrl(String thumImageUrl) {
            this.thumImageUrl = thumImageUrl;
        }

        public String getPhoneModel() {
            return phoneModel;
        }

        public void setPhoneModel(String phoneModel) {
            this.phoneModel = phoneModel;
        }

        public double getPhonePrice() {
            return phonePrice;
        }

        public void setPhonePrice(double phonePrice) {
            this.phonePrice = phonePrice;
        }

        public String getPhoneImei() {
            return phoneImei;
        }

        public void setPhoneImei(String phoneImei) {
            this.phoneImei = phoneImei;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }
    }

    public static class GuaranteePackage{
        private String packageName;
        private double price;
        private double  beginPrice;
        private double  endPrice;
        private int orderBy;

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getBeginPrice() {
            return beginPrice;
        }

        public void setBeginPrice(double beginPrice) {
            this.beginPrice = beginPrice;
        }

        public double getEndPrice() {
            return endPrice;
        }

        public void setEndPrice(double endPrice) {
            this.endPrice = endPrice;
        }

        public int getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(int orderBy) {
            this.orderBy = orderBy;
        }
    }

    public static class InsuranceProduct{
        private String name;
        private int  compensateNum; //赔付次数
        private GuaranteePackage packages;
        private String sampleImage;


        //by huangbin  ↓↓↓↓↓↓↓↓↓↓↓↓↓
        private List<SectionPrice> prices;

        public String getSampleImage() {
            return sampleImage;
        }

        public void setSampleImage(String sampleImage) {
            this.sampleImage = sampleImage;
        }

        public List<SectionPrice> getPrices() {
            return prices;
        }

        public void setPrices(List<SectionPrice> prices) {
            this.prices = prices;
        }

        //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ by huangbin




        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCompensateNum() {
            return compensateNum;
        }

        public void setCompensateNum(int compensateNum) {
            this.compensateNum = compensateNum;
        }

        public GuaranteePackage getPackages() {
            return packages;
        }

        public void setPackages(GuaranteePackage packages) {
            this.packages = packages;
        }
    }

}
