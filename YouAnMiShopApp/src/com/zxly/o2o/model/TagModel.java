package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/9/9.
 */
public class TagModel {
    private String name;
    private long id;
    private boolean ischoose;

    public TagModel(String name, long id, boolean ischoose) {
        this.name = name;
        this.id = id;
        this.ischoose = ischoose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean ischoose() {
        return ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }

    @Override
    public String toString() {
        return "TagModel{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", ischoose=" + ischoose +
                '}';
    }
}
