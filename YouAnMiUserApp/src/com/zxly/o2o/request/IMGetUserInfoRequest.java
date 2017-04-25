package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.EaseAddFriendInfo;
import com.easemob.easeui.model.IMUserInfoVO;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/24.
 */
public class IMGetUserInfoRequest extends BaseRequest {
    public IMUserInfoVO imUserInfoVO = new IMUserInfoVO();

    public IMGetUserInfoRequest(long userId) {
        addParams("userId", userId);
        addParams("shopId", Math.abs(EaseConstant.shopID));
    }


    @Override
    protected String method() {
        return "/friends/user/detail";
    }

    @Override
    protected void fire(String data) throws AppException {
        JSONObject jo;
        EaseAddFriendInfo easeAddFriendInfo = null;
        boolean isCurrentShopUser=false;
        try {
            jo = new JSONObject(data);
            if (jo.has("friendInfo")) {
                String friendData = jo.getString("friendInfo");

                easeAddFriendInfo = GsonParser.getInstance().fromJson(friendData,
                        new TypeToken<EaseAddFriendInfo>() {
                        });
            }
            if (jo.has("user")) {
                data = jo.getString("user");
            } else {
                return;
            }
            if(jo.has("isCurrentShopUser")){
                isCurrentShopUser = jo.getBoolean("isCurrentShopUser");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imUserInfoVO = GsonParser.getInstance().fromJson(data, new TypeToken<IMUserInfoVO>() {
        });

        imUserInfoVO.setFriendInfo(easeAddFriendInfo);
        imUserInfoVO.setIsCurrentShopUser(isCurrentShopUser);

    }
}
