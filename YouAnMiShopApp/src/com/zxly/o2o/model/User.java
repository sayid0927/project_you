package com.zxly.o2o.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private long shopId;//用户注册的店铺ID
    private long userId;//用户ID
    private int roleType;//1：商户app，2：业务员app，3：送货员app
    private long roleId;
    private String password;//登录密码
    private String userName;//手机号
    private String token;//

    private String loginUserName;

    private long id;
    private String name;
    private String originHeadUrl;
    private String thumHeadUrl;
    private String appIconUrl;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
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

    public String getOriginHeadUrl() {
        return originHeadUrl;
    }

    public void setOriginHeadUrl(String originHeadUrl) {
        this.originHeadUrl = originHeadUrl;
    }

    public String getThumHeadUrl() {
        return thumHeadUrl;
    }

    public void setThumHeadUrl(String thumHeadUrl) {
        this.thumHeadUrl = thumHeadUrl;
    }

    public String getAppIconUrl() {
        return appIconUrl;
    }

    public void setAppIconUrl(String appIconUrl) {
        this.appIconUrl = appIconUrl;
    }
}
