package com.zxly.o2o.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/13.
 */
public class MenberInfoModel implements Parcelable {

    /**
     * id : 28287  会员id
     * headUrl : http://yamtest.youanmi.com/yam/M00/00/26/wKhGQ1b4-OKAKzYoAAA0q3rmGMc188.png
     * isBuyOnline : 0否 1是 是否线上购买
     * nickname : 188****0947  会员昵称
     * userName : 18826560947  备注名称
     */

    private long id;
    private String headUrl;
    private int isBuyOnline;
    private String nickname="";
    private String userName="";
    private int isNewBehavior; //是否有新的行为轨迹  0否1是
    private int isNewConsumption;//是否有新的消费轨迹  规则同上
    private long lastBehaviorTime;//上次消费轨迹或者行为轨迹的时间
    private long lastPhoneTime;//上次通话时间
    private long lastSmsTime;//上次短信时间
    private String mobilePhone;//手机号码\
    private String remarkName="";//备注名称
    private String nameAbbr;
    private int loacation;
    private int newBehaviorCount;//后续需求 新增字段---消费轨迹与行为轨迹总数



    public int getNewBehaviorCount() {
        return newBehaviorCount;
    }

    public void setNewBehaviorCount(int newBehaviorCount) {
        this.newBehaviorCount = newBehaviorCount;
    }

    public int getLoacation() {
        return loacation;
    }

    public void setLoacation(int loacation) {
        this.loacation = loacation;
    }

    private List<MenberGroupModel> locationGroupName=new ArrayList<MenberGroupModel>();//所在分组

    public List<MenberGroupModel> getLocationGroupName() {
        return locationGroupName;
    }

    public void setLocationGroupName(List<MenberGroupModel> locationGroupName) {
        this.locationGroupName = locationGroupName;
    }

    public String getNameAbbr() {
        return nameAbbr;
    }

    public void setNameAbbr(String nameAbbr) {
        this.nameAbbr = nameAbbr;
    }

    /**
     * 线下粉丝详情 独有
     * @return
     */
    private String deviceName="";//设备名称
    private int gender;//性别  1男2女
    private List<String> labels =new ArrayList<String>();//标签名数组

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public int getIsNewBehavior() {
        return isNewBehavior;
    }

    public void setIsNewBehavior(int isNewBehavior) {
        this.isNewBehavior = isNewBehavior;
    }

    public int getIsNewConsumption() {
        return isNewConsumption;
    }

    public void setIsNewConsumption(int isNewConsumption) {
        this.isNewConsumption = isNewConsumption;
    }

    public long getLastBehaviorTime() {
        return lastBehaviorTime;
    }

    public void setLastBehaviorTime(long lastBehaviorTime) {
        this.lastBehaviorTime = lastBehaviorTime;
    }

    public long getLastPhoneTime() {
        return lastPhoneTime;
    }

    public void setLastPhoneTime(long lastPhoneTime) {
        this.lastPhoneTime = lastPhoneTime;
    }

    public long getLastSmsTime() {
        return lastSmsTime;
    }

    public void setLastSmsTime(long lastSmsTime) {
        this.lastSmsTime = lastSmsTime;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getIsBuyOnline() {
        return isBuyOnline;
    }

    public void setIsBuyOnline(int isBuyOnline) {
        this.isBuyOnline = isBuyOnline;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object obj) {
        boolean flag = obj instanceof MenberInfoModel;
        if(!flag){
            return false;
        }
        MenberInfoModel object = (MenberInfoModel)obj;
        if(this.getId()==object.getId()){
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
        dest.writeLong(this.id);
        dest.writeString(this.headUrl);
        dest.writeInt(this.isBuyOnline);
        dest.writeString(this.nickname);
        dest.writeString(this.userName);
        dest.writeInt(this.isNewBehavior);
        dest.writeInt(this.isNewConsumption);
        dest.writeLong(this.lastBehaviorTime);
        dest.writeLong(this.lastPhoneTime);
        dest.writeLong(this.lastSmsTime);
        dest.writeString(this.mobilePhone);
        dest.writeString(this.remarkName);
        dest.writeString(this.nameAbbr);
        dest.writeInt(this.loacation);
        dest.writeInt(this.newBehaviorCount);
        dest.writeList(this.locationGroupName);
        dest.writeString(this.deviceName);
        dest.writeInt(this.gender);
        dest.writeStringList(this.labels);
    }

    public MenberInfoModel() {
    }

    protected MenberInfoModel(Parcel in) {
        this.id = in.readLong();
        this.headUrl = in.readString();
        this.isBuyOnline = in.readInt();
        this.nickname = in.readString();
        this.userName = in.readString();
        this.isNewBehavior = in.readInt();
        this.isNewConsumption = in.readInt();
        this.lastBehaviorTime = in.readLong();
        this.lastPhoneTime = in.readLong();
        this.lastSmsTime = in.readLong();
        this.mobilePhone = in.readString();
        this.remarkName = in.readString();
        this.nameAbbr = in.readString();
        this.loacation = in.readInt();
        this.newBehaviorCount = in.readInt();
        this.locationGroupName = new ArrayList<MenberGroupModel>();
        in.readList(this.locationGroupName, MenberGroupModel.class.getClassLoader());
        this.deviceName = in.readString();
        this.gender = in.readInt();
        this.labels = in.createStringArrayList();
    }

    public static final Creator<MenberInfoModel> CREATOR = new Creator<MenberInfoModel>() {
        @Override
        public MenberInfoModel createFromParcel(Parcel source) {
            return new MenberInfoModel(source);
        }

        @Override
        public MenberInfoModel[] newArray(int size) {
            return new MenberInfoModel[size];
        }
    };

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
