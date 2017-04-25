package com.zxly.o2o.model;

/**
 * Created by Administrator on 2015/12/15.
 */
public class CircleForumVO extends MyCirCleObject{
    private int isTop;

    private long concernAmount;
    private long id;
    private String imageUrl="";
    private String name="";
    private String content="";
    private boolean isCheck=true;

    private String initialLetter="";

    private long concernTime;

    public long getConcernTime() {
        return concernTime;
    }

    public void setConcernTime(long concernTime) {
        this.concernTime = concernTime;
    }

    public String getInitialLetter() {
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public long getConcernAmount() {
        return concernAmount;
    }

    public void setConcernAmount(long concernAmount) {
        this.concernAmount = concernAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
