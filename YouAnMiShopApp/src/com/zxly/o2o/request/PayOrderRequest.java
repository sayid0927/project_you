package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.model.BankcardInfo;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.util.EncryptionUtils;

import org.json.JSONObject;

/**
 * @author fengrongjian 2015-5-22
 * @description 交易确认网络请求
 */
public class PayOrderRequest extends BaseRequest {
    private int wrongCount = 0;
    private String wrongTime;
    private String notifyUrl;
    private String money;
    private String createTime;
    private String payNo;
    private int deliveryType;
    private String orderNo;
    private String phoneNo;
    private String shopAddress;
    private String bankName;
    private String mobile;

    public PayOrderRequest(int type, String userPaw, String payNo,
                           String payType, UserBankCard userBankCard) {
        addParams("type", type);
        addParams("userPaw", EncryptionUtils.MD5Swap(userPaw));
        addParams("payNo", payNo);
        addParams("payType", payType);
        if (userBankCard != null) {
            addParams("userName", userBankCard.getUserName());
            addParams("idCard", userBankCard.getIdCard());
            addParams("bankNumber", userBankCard.getBankNumber());
        }
    }

    public PayOrderRequest(int type, String userPaw, String payNo,
                           String payType, UserBankCard userBankCard, BankcardInfo bankcardInfo) {
        this(type, userPaw, payNo, payType, userBankCard);
        if(bankcardInfo != null){
            addParams("prcptcd", bankcardInfo.getPrcptcd());
            addParams("brabankName", bankcardInfo.getBrabankName());
            addParams("provinceCode", bankcardInfo.getProvinceCode());
            addParams("cityCode", bankcardInfo.getCityCode());
        }
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if (json.has("notifyUrl")) {
                this.notifyUrl = json.getString("notifyUrl");
            }
            if (json.has("payNo")) {
                this.payNo = json.getString("payNo");
            }
            if (json.has("money")) {
                this.money = json.getString("money");
            }
            if (json.has("createTime")) {
                this.createTime = json.getString("createTime");
            }
            if (json.has("wrongCount")) {
                this.wrongCount = json.getInt("wrongCount");
            }
            if (json.has("wrongTime")) {
                this.wrongTime = json.getString("wrongTime");
            }
            if (json.has("deliveryType")) {
                this.deliveryType = json.getInt("deliveryType");
            }
            if (json.has("orderNo")) {
                this.orderNo = json.getString("orderNo");
            }
            if (json.has("phoneNo")) {
                this.phoneNo = json.getString("phoneNo");
            }
            if (json.has("shopAddress")) {
                this.shopAddress = json.getString("shopAddress");
            }
            if (json.has("bankName")) {
                this.bankName = json.getString("bankName");
            }
            if (json.has("mobile")) {
                this.mobile = json.getString("mobile");
            }
        } catch (Exception e) {
            throw new AppException("数据解析异常");
        }
    }

    @Override
    protected String method() {
        return "pay/payModifiy";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public String getWrongTime() {
        return wrongTime;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getMoney() {
        return money;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getPayNo() {
        return payNo;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public String getMobile() {
        return mobile;
    }
}
