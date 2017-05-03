package com.zxly.o2o.model;

/**
 * Created by hejun on 2016/9/13.
 */
public class MenberGroupModel {

    /**
     * id : 分组id
     * isCustomerGroup : 是否为自定义分组  0否1是
     * memberCount :  组内人数 1
     * name : 延保会员  分组名称
     * newMsgMemberCount  新轨迹人数
     */


    private int id;
    private int isCustomerGroup;
    private int memberCount;
    private String name;
    private int newMsgMemberCount;
    private boolean showTuisong=true;
    //标识该组是否是被点击
    private boolean hasClick;
    //自定义的属性---是否展示 非自定义且会员数为0的组规定为不需要显示  默认需要显示
    private boolean shouldShow=true;

    public boolean isShouldShow() {
        return shouldShow;
    }

    public void setShouldShow(boolean shouldShow) {
        this.shouldShow = shouldShow;
    }

    public boolean isHasClick() {
        return hasClick;
    }

    public void setHasClick(boolean hasClick) {
        this.hasClick = hasClick;
    }

    public boolean isShowTuisong() {
        return showTuisong;
    }

    public void setShowTuisong(boolean showTuisong) {
        this.showTuisong = showTuisong;
    }

    public int getNewMsgMemberCount() {
        return newMsgMemberCount;
    }

    public void setNewMsgMemberCount(int newMsgMemberCount) {
        this.newMsgMemberCount = newMsgMemberCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsCustomerGroup() {
        return isCustomerGroup;
    }

    public void setIsCustomerGroup(int isCustomerGroup) {
        this.isCustomerGroup = isCustomerGroup;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
