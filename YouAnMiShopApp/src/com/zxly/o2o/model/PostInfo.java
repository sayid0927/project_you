package com.zxly.o2o.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/7/6.
 */
public class PostInfo {
    private int postId;
    private String postName;
    private Boolean isSave;

    public  List<PostTask> tasks=new ArrayList<PostTask>();

    public Boolean isSave() {
        return isSave;
    }

    public void setSave(boolean isSave) {
        this.isSave = isSave;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof PostInfo) {
            if (((PostInfo) o).postId == this.postId) {
                return true;
            }
        }

        return false;
    }

}
