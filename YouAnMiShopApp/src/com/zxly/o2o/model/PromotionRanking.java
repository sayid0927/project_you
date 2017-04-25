package com.zxly.o2o.model;

import java.io.Serializable;

public class PromotionRanking implements Serializable {
    /**  */
    private static final long serialVersionUID = 1L;
    /**
     * 我的排名
     */
    private int ranking;
    /**
     * 类型 1.商品，2文章，3app
     */
    private int type;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
