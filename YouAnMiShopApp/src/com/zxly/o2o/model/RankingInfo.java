package com.zxly.o2o.model;

/**
 * Created by kenwu on 2015/12/22.
 */
public class RankingInfo {
    private int id;
    private int amount;
    private String name;
    private String roleName;
    private String thumHeadUrl;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getThumHeadUrl() {
        return thumHeadUrl;
    }

    public void setThumHeadUrl(String thumHeadUrl) {
        this.thumHeadUrl = thumHeadUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
