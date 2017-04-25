package com.easemob.easeui.model;

import android.util.Log;

import java.io.Serializable;

public class IMUserInfoVO implements Serializable {
    private long id;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String userName = "";

    private String password = "";

    private int roleType;
    private int isBoss;

    public int getIsBoss() {
        return isBoss;
    }

    public void setIsBoss(int isBoss) {
        this.isBoss = isBoss;
    }

    private String serialNum="";

    private long roleId;

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    private String nickname = "";

    private byte type;  //1:商户用户 2：用户端用户

    private Byte originType;

    private long shopId;

    private long registShopId;

    private String name = "";

    private byte gender;

    private String description = "";

    private String mobilePhone = "";

    private long birthday;

    private long loginTime;

    private String originHeadUrl = "";

    private String thumHeadUrl = "";

    private String ip = "";

    private Byte status;

    private long passTime;

    private long createTime;

    private long updateTime;

    private String promotionCode;//推广码
    private String areaName = "";
    private String cityName = "";
    private String provinceName = "";
    private String signature = "";
    private String cardNo;
    private String shopName;
    private String appIconUrl;
    private boolean isCurrentShopUser;

    private EaseAddFriendInfo friendInfo;
    private UserRemark userRemark = new UserRemark();

    public class UserRemark implements Serializable{
        String remarkName="";
        String description="";

        public String getRemarkName() {
            return remarkName;
        }

        public void setRemarkName(String remarkName) {
            this.remarkName = remarkName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public UserRemark getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(UserRemark userRemark) {
        this.userRemark = userRemark;
    }

    public boolean isCurrentShopUser() {
        return isCurrentShopUser;
    }

    public void setIsCurrentShopUser(boolean isCurrentShopUser) {
        this.isCurrentShopUser = isCurrentShopUser;
    }

    public EaseAddFriendInfo getFriendInfo() {
        return friendInfo;
    }

    public void setFriendInfo(EaseAddFriendInfo friendInfo) {
        this.friendInfo = friendInfo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Byte getOriginType() {
        return originType;
    }

    public void setOriginType(Byte originType) {
        this.originType = originType;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getRegistShopId() {
        return registShopId;
    }

    public void setRegistShopId(Long registShopId) {
        this.registShopId = registShopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public String getOriginHeadUrl() {
        return originHeadUrl;
    }

    public void setOriginHeadUrl(String originHeadUrl) {
        this.originHeadUrl = originHeadUrl == null ? null : originHeadUrl.trim();
    }

    public String getThumHeadUrl() {
        return thumHeadUrl;
    }

    public void setThumHeadUrl(String thumHeadUrl) {
        this.thumHeadUrl = thumHeadUrl == null ? null : thumHeadUrl.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getPassTime() {
        return passTime;
    }

    public void setPassTime(Long passTime) {
        this.passTime = passTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public class FriendInfo {
        public long createTime;
        public int isBlack;
    }

    public String getAppIconUrl() {
        return appIconUrl;
    }

    public void setAppIconUrl(String appIconUrl) {
        this.appIconUrl = appIconUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "shopId=" + shopId +
                ", userId=" + id +
                ", roleType=" + roleType +
                ", roleId=" + roleType +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                ", loginUserName='" + userName + '\'' +
                '}';
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.e("--user--", "finalize");
    }
}