package com.zxly.o2o.model;

import java.util.List;

/**     /promote/localArticle/list
 *    本地热文列表1.0
 */
public class LocalArticle {
    private long areaId;      //区县id  可能为空
    private String areaName;  //区县名称  可能为空
    private  long cityId;     // 城市id
    private  String cityName;  //城市名称
    private List<LocalArticlesInfo> articlesInfoList;  //热文列表


    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<LocalArticlesInfo> getArticlesInfoList() {
        return articlesInfoList;
    }

    public void setArticlesInfoList(List<LocalArticlesInfo> articlesInfoList) {
        this.articlesInfoList = articlesInfoList;
    }
}
