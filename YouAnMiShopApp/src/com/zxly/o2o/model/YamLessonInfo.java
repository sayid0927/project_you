package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/10/18.
 */
public class YamLessonInfo {

    private int contentType;
    private long createTime;
    private int id;
    private String image;
    private int learnCount;
    private String title;
    private String typeName;
    private String[] label;

    public String[] getLabel() {
        return label;
    }

    public void setLabel(String[] label) {
        this.label = label;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLearnCount() {
        return learnCount;
    }

    public void setLearnCount(int learnCount) {
        this.learnCount = learnCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
