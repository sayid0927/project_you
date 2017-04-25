package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.model.EaseAddFriendInfo;
import com.easemob.easeui.model.IMUserInfoVO;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/1/5.
 */
public class IMGetUserDetailInfoRequest extends BaseRequest{
    public IMUserInfoVO imUserInfoVO = new IMUserInfoVO();

    public IMGetUserDetailInfoRequest(long userId){
        addParams("userId",userId);
        addParams("shopId", Account.user.getShopId());
    }


    @Override
    protected String method() {
        return "/shop/shopUser/detail";
    }

    @Override
    protected void fire(String data) throws AppException {
        JSONObject jo;
        try {
            jo = new JSONObject(data);
            if (jo.has("user")) {
                data = jo.getString("user");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imUserInfoVO = GsonParser.getInstance().fromJson(data, new TypeToken<IMUserInfoVO>() {
        });

    }
}
