package com.zxly.o2o.model;

/**
 * Created by kenwu on 2016/3/22.
 */
public class ArticleType {

    private String id;
    private String codeName;

    public ArticleType(String id, String codeName) {
        this.id = id;
        this.codeName = codeName;
    }

    public ArticleType() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String name) {
        this.codeName = name;
    }
}
