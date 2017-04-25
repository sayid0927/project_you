package com.easemob.chatuidemo;

/**
 * Created by kenwu on 2016/2/21.
 */
public class HXConfigInfo {
    public String HXKey;
    public String clientID;
    public String clientSecret;
    public String orgName;
    public  String appName;

    @Override
    public String toString() {
        return "HXConfigInfo{" +
                "HXKey='" + HXKey + '\'' +
                ", clientID='" + clientID + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", orgName='" + orgName + '\'' +
                ", appName='" + appName + '\'' +
                '}';
    }
}
