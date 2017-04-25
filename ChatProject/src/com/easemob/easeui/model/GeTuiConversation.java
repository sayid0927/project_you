package com.easemob.easeui.model;

import com.easemob.chat.EMConversation;

/**
 * Created by Administrator on 2016/7/15.
 */
public class GeTuiConversation extends EMConversation {

    private String title="";
    private long updateTime;
    private int number;
    private int conversationType;
    private int what;
    private String userName="";
    private String nickName="";
    //消息类型
    private int typeMsg;

    public int getTypeMsg() {
        return typeMsg;
    }

    public void setTypeMsg(int typeMsg) {
        this.typeMsg = typeMsg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public GeTuiConversation(String s) {
        super(s);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(int type) {
        this.conversationType = type;
    }
}
