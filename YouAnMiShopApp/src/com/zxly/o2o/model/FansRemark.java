package com.zxly.o2o.model;

/**
 * Created by fengrongjian on 2016/9/20.
 * 粉丝备注
 */
public class FansRemark {
    //行为类型
    private int type;
    //	写备注的业务员
    private String salesman;
    //	时间
    private long time;
    //	内容
    private String content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
