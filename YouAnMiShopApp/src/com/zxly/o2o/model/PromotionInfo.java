package com.zxly.o2o.model;

import java.io.Serializable;

public class PromotionInfo implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 排名的列表
     */
    private PromotionChampion list;
    /**
     * 我的排名
     */
    private PromotionRanking myList;

    public PromotionChampion getList() {
        return list;
    }

    public void setList(PromotionChampion list) {
        this.list = list;
    }

    public PromotionRanking getMyList() {
        return myList;
    }

    public void setMyList(PromotionRanking myList) {
        this.myList = myList;
    }
}
