package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.HXModel;
import com.easemob.easeui.AppException;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 2015/7/10.
 */
public class IMGetContactsRequest extends BaseRequest {
    private List<EaseYAMUser> shopRoleUsers = new ArrayList<EaseYAMUser>();

    public IMGetContactsRequest(List<EaseYAMUser> shopRoleUsers) {
        addParams("shopId", Config.shopId);
        this.shopRoleUsers = shopRoleUsers;
    }

    @Override
    protected String method() {
        return "/friends/user/list";
    }

    @Override
    protected void fire(String data) throws AppException {
        JSONObject jo;
        List<EaseYAMUser> contactList=null;
        try {
            jo = new JSONObject(data);
            if (jo.has("users")) {
                data = jo.getString("users");
                Log.e("=======", "before save conatct=====:::::" + shopRoleUsers.size());
                TypeToken<List<EaseYAMUser>> token = new TypeToken<List<EaseYAMUser>>() {
                };
                contactList = GsonParser.getInstance().fromJson(data, token);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (shopRoleUsers != null && contactList != null) {
            shopRoleUsers.addAll(contactList);
        }
        HXHelper.getInstance().cleanYAMContactList();  //清空缓存
        new HXModel(AppController.getInstance())
                .saveYAMContactList(shopRoleUsers);
    }
}
