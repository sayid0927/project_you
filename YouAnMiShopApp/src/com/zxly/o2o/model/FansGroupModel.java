package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/9/9.
 * 粉丝分组实体类
 */
public class FansGroupModel {
    //粉丝数
    private int num;
    //	新行为粉丝数
    private int newBehaviorNum;
    //粉丝分组名称
    private String group;
    //标识该组是否是被点击
    private boolean hasClick;
    //点击分组是否显示推送  默认显示
    private boolean showTuisong=true;

    public boolean isShowTuisong() {
        return showTuisong;
    }

    public void setShowTuisong(boolean showTuisong) {
        this.showTuisong = showTuisong;
    }

    public boolean isHasClick() {
        return hasClick;
    }

    public void setHasClick(boolean hasClick) {
        this.hasClick = hasClick;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNewBehaviorNum() {
        return newBehaviorNum;
    }

    public void setNewBehaviorNum(int newBehaviorNum) {
        this.newBehaviorNum = newBehaviorNum;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
