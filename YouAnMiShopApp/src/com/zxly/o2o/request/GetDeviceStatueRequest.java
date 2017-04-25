package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.zxly.o2o.model.NetWork;
import com.zxly.o2o.util.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsnx on 2016/6/29.
 */
public class GetDeviceStatueRequest extends BaseRequest {

    private String ip;
    private int deviceType;
    private int deviceStatue;
    public String getIp() {
        return ip;
    }

    public int getDeviceType() {
        return deviceType;
    }


    public int getDeviceStatue() {
        return deviceStatue;
    }

    public GetDeviceStatueRequest(String mac)
    {
        NetWork  netWork= NetworkUtil.getMacAndIp();
        addParams("mac",netWork.mac);
    }
    @Override
    public String getUrl() {
        NetWork  netWork= NetworkUtil.getMacAndIp();
         ip=netWork.ip.substring(0,netWork.ip.lastIndexOf("."));
       if(ip.equals("192.168.88"))//路由
        {
            ip=ip+".254";
        }else if(ip.equals("10.0.0"))//充电宝
        {
            ip=ip+".1";
        }
        return "http://"+ip+"/fwcgi-bin/getDeviceStatue";
    }

    @Override
    protected void fire(String data) throws AppException {
        Log.e("---data--",data);
        try {
            JSONObject jo=new JSONObject(data);
            deviceType=jo.optInt("device_type");
            deviceStatue=jo.optInt("device_statue");
        } catch (JSONException e) {
           throw JSONException(e);
        }

    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }



    @Override
    protected String method() {
        return "/fwcgi-bin/getDeviceStatue";
    }
}
