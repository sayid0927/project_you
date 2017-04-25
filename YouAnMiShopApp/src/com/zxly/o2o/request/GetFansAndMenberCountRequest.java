package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hejun on 2016/9/14.
 * 初始化客多多界面  顶部 粉丝数量和会员数量
 */
public class GetFansAndMenberCountRequest extends BaseRequest{

    private int fansCount;
    private int menberCount;

    public GetFansAndMenberCountRequest(){
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("fansCount")){
                fansCount=jsonObject.optInt("fansCount");
            }
            if(jsonObject.has("memberCount")){
                menberCount=jsonObject.optInt("memberCount");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String method() {
        return "/keduoduo/init";
    }

    public int getFansCount(){
        return fansCount;
    }

    public int getMenberCount(){
        return menberCount;
    }
}
