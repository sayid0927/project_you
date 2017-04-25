package com.easemob.easeui.request;

import android.widget.Toast;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.HXModel;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.model.IMUserInfoVO;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/12/25.
 */
public class EaseAddFriendRequest extends HXNormalRequest {
    public IMUserInfoVO imUserInfoVO;

    public EaseAddFriendRequest(long friendsUserId) {
        addParams("friendsUserId", friendsUserId);
        addParams("shopId", Math.abs(EaseConstant.shopID));
    }

    @Override
    protected String method() {
        return "/friends/user/add";
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


        TypeToken<IMUserInfoVO> token = new TypeToken<IMUserInfoVO>() {
        };
        imUserInfoVO = GsonParser.getInstance().fromJson(data, token);
        EaseYAMUser easeYAMUser = new EaseYAMUser();
        easeYAMUser
                .setIsBlack(imUserInfoVO.getFriendInfo() != null ? imUserInfoVO.getFriendInfo().getIsBlack()
                        : 1);

        easeYAMUser.getFirendsUserInfo().setEid(
                HXApplication.getInstance().parseUserFromID(imUserInfoVO.getId(), HXConstant.TAG_USER));
        easeYAMUser.getFirendsUserInfo().setCreateTime(imUserInfoVO.getCreateTime());
        easeYAMUser.getFirendsUserInfo().setNickname(imUserInfoVO.getNickname());
        easeYAMUser.getFirendsUserInfo().setUsername(imUserInfoVO.getUserName());
        easeYAMUser.getFirendsUserInfo().setThumHeadUrl(imUserInfoVO.getThumHeadUrl());
        easeYAMUser.getFirendsUserInfo().setGender(imUserInfoVO.getGender());
        easeYAMUser.getFirendsUserInfo().setOriginHeadUrl(imUserInfoVO.getOriginHeadUrl());
        easeYAMUser.getFirendsUserInfo().setId(imUserInfoVO.getId());
        easeYAMUser.getFirendsUserInfo().setSignature(imUserInfoVO.getSignature());

        HXHelper.getInstance().cleanYAMContactList();  //清空缓存
        new HXModel(HXApplication.applicationContext).saveYAMContact(easeYAMUser);
        HXHelper.getInstance().getYAMContactList();
    }
}
