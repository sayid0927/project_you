package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/10/8.
 * 根据后台传的id 返回对应的描述信息
 */
public class BehaviorType {
    private String name;

    public BehaviorType(int type) {
        setName(type);
    }

    protected void setName(int type){
        switch (type){
            case 100:
                this.name="购买";
            break;
            case 101:
                this.name="流量充值";
            break;
            case 102:
                this.name="到店购买";
            break;
            case 200:
                this.name="注册";
            break;
            case 201:
                this.name="登录";
            break;
            case 202:
                this.name="浏览";
            break;
            case 203:
                this.name="收藏";
            break;
            case 204:
                this.name="点赞";
            break;
            case 205:
                this.name="评论";
            break;
            case 206:
                this.name="分享";
            break;
            case 207:
                this.name="APP安装";
            break;
            case 208:
                this.name="喜欢";
            break;
            case 209:
                this.name="发布";
            break;
            case 210:
                this.name="回复";
            break;
            case 211:
                this.name="购物车";
            break;
            case 212:
                this.name="参与";
            break;
            default:
                this.name="";
                break;
        }

    }
    public String getName(){
        return name;
    }
}
