package com.zxly.o2o.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/9.
 * 粉丝信息
 */
public class FansInfo implements Parcelable {
    //购买意向 0颗星,1颗星，2颗星，3颗星,没有默认值  没选就不传
    private int buyIntention;
    //粉丝id
    private int fansId;
    //有新动态 0 否，1 是
    private int hasNew;
    //	有备注  	0 否，1 是
    private int hasRemark;
    //手机IMEI
    private String imei="";
    //	是否关注 	0 否，1 是
    private int isFocus;
    //	粉丝姓名
    private String name="";
    //	手机型号
    private String phoneModel="";

    /**
     * 以下几个字段仅在选择群推中需要
     */
    private String headUrl="";
    private String nickName="";
    private String remarkName="";
    private String userName="";
    private String remarkContent="";


    public String getRemarkContent() {
        return remarkContent;
    }

    public void setRemarkContent(String remarkContent) {
        this.remarkContent = remarkContent;
    }

    //手机号码
    private String phone="";
    //粉丝标签列表
    private List<String> labels=new ArrayList<String>();
    //是否线下录入  1 线下录入 0安装app
    private int isOffline;
    //线下安装或者安装时间
    private long installTime;
    private int hasNewBehavior;
    private int hasNewShopping;
    private int gender;
    private int newBehaviorCount;
    //标签名称
    private List<String> labelStr=new ArrayList<String>();




    public List<String> getLabelStr() {
        return labelStr;
    }

    public void setLabelStr(List<String> labelStr) {
        this.labelStr = labelStr;
    }

    public int getNewBehaviorCount() {
        return newBehaviorCount;
    }

    public void setNewBehaviorCount(int newBehaviorCount) {
        this.newBehaviorCount = newBehaviorCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public int getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(int isOffline) {
        this.isOffline = isOffline;
    }

    public long getInstallTime() {
        return installTime;
    }

    public void setInstallTime(long installTime) {
        this.installTime = installTime;
    }

    public int getHasNewBehavior() {
        return hasNewBehavior;
    }

    public void setHasNewBehavior(int hasNewBehavior) {
        this.hasNewBehavior = hasNewBehavior;
    }

    public int getHasNewShopping() {
        return hasNewShopping;
    }

    public void setHasNewShopping(int hasNewShopping) {
        this.hasNewShopping = hasNewShopping;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getBuyIntention() {
        return buyIntention;
    }

    public void setBuyIntention(int buyIntention) {
        this.buyIntention = buyIntention;
    }

    public int getFansId() {
        return fansId;
    }

    public void setFansId(int fansId) {
        this.fansId = fansId;
    }

    public int getHasNew() {
        return hasNew;
    }

    public void setHasNew(int hasNew) {
        this.hasNew = hasNew;
    }

    public int getHasRemark() {
        return hasRemark;
    }

    public void setHasRemark(int hasRemark) {
        this.hasRemark = hasRemark;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    @Override
    public boolean equals(Object obj) {

        boolean flag = obj instanceof FansInfo;
        if(!flag){
            return false;
        }
        FansInfo object = (FansInfo)obj;
        if(this.getFansId()==object.getFansId()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.buyIntention);
        dest.writeInt(this.fansId);
        dest.writeInt(this.hasNew);
        dest.writeInt(this.hasRemark);
        dest.writeString(this.imei);
        dest.writeInt(this.isFocus);
        dest.writeString(this.name);
        dest.writeString(this.phoneModel);
        dest.writeString(this.headUrl);
        dest.writeString(this.nickName);
        dest.writeString(this.remarkName);
        dest.writeString(this.userName);
        dest.writeString(this.phone);
        dest.writeStringList(this.labels);
        dest.writeInt(this.isOffline);
        dest.writeLong(this.installTime);
        dest.writeInt(this.hasNewBehavior);
        dest.writeInt(this.hasNewShopping);
        dest.writeInt(this.gender);
        dest.writeInt(this.newBehaviorCount);
        dest.writeStringList(this.labelStr);
    }

    public FansInfo() {
    }

    protected FansInfo(Parcel in) {
        this.buyIntention = in.readInt();
        this.fansId = in.readInt();
        this.hasNew = in.readInt();
        this.hasRemark = in.readInt();
        this.imei = in.readString();
        this.isFocus = in.readInt();
        this.name = in.readString();
        this.phoneModel = in.readString();
        this.headUrl = in.readString();
        this.nickName = in.readString();
        this.remarkName = in.readString();
        this.userName = in.readString();
        this.phone = in.readString();
        this.labels = in.createStringArrayList();
        this.isOffline = in.readInt();
        this.installTime = in.readLong();
        this.hasNewBehavior = in.readInt();
        this.hasNewShopping = in.readInt();
        this.gender = in.readInt();
        this.newBehaviorCount = in.readInt();
        this.labelStr = in.createStringArrayList();
    }

    public static final Creator<FansInfo> CREATOR = new Creator<FansInfo>() {
        @Override
        public FansInfo createFromParcel(Parcel source) {
            return new FansInfo(source);
        }

        @Override
        public FansInfo[] newArray(int size) {
            return new FansInfo[size];
        }
    };
}
