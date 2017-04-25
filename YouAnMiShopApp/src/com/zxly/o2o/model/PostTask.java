package com.zxly.o2o.model;

/**
 * Created by dsnx on 2015/7/13.
 */
public class PostTask {

    public int type;
    public String target;
    Integer id;

    public PostTask(int type, String target, Integer id) {
        this.type = type;
        this.target = target;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof  PostTask)
        {
            PostTask p= (PostTask) o;
            if(p.type==this.type&&p.target.equals(this.target))
            {
                return true;
            }
        }
        return false;
    }
}
