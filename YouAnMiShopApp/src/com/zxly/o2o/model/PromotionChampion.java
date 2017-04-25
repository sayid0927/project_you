package com.zxly.o2o.model;

import java.io.Serializable;

public class PromotionChampion implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 完成的次数
     */
    private int count;
    /**
     * 业务员昵称
     */
    private String nickName;
    /**
     * 总次数
     */
    private int totCount;
    /**
     * 类型 1.商品，2文章，3app
     */
    private int type;
    /**
     * 我的排名
     */
    private int ranking;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getTotCount() {
        return totCount;
    }

    public void setTotCount(int totCount) {
        this.totCount = totCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
