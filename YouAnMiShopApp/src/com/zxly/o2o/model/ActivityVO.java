package com.zxly.o2o.model;

/**
 * Created by Administrator on 2016/1/11.
 */
public class ActivityVO {
    private String title;
    private long id;
    private String imageUrls="";
    private String h5Url="";
    private long createTime;

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object obj) {

        boolean flag = obj instanceof ActivityVO;
        if(!flag){
            return false;
        }
        ActivityVO object = (ActivityVO)obj;
        if(this.getId()==object.getId()){
            return true;
        }else {
            return false;
        }
    }
}
