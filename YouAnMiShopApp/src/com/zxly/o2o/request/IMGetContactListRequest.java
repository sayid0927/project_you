package com.zxly.o2o.request;

import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.HXModel;
import com.easemob.easeui.AppException;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Benjamin on 2015/7/4.
 */
public class IMGetContactListRequest extends BaseRequest {
    private int pageIndex = 1;
    private static List<EaseUser> contactList = new ArrayList<EaseUser>();
    public static boolean isLoadingContact;

    public IMGetContactListRequest(long shopId) {
        addParams("shopId", shopId);
        addParams("pageIndex", pageIndex);
        addParams("pageSize",3000);
        isLoadingContact=true;
    }

    @Override
    protected String method() {
        return "/shop/shopUser/registerUser/list";
    }

    @Override
    protected void fire(String data) throws AppException {

        JSONObject jo;
        try {
            jo = new JSONObject(data);
            if(jo.has("users")) {
                data = jo.getString("users");


                TypeToken<List<EaseUser>> token = new TypeToken<List<EaseUser>>() {
                };
                List<EaseUser> mContactList = GsonParser.getInstance().fromJson(data, token);
                contactList.addAll(mContactList);

                if (jo.getInt("allPage") > pageIndex) {
                    pageIndex++;
                    new IMGetContactListRequest(Account.user.getShopId()).start();
                } else {
                    HXHelper.getInstance().cleanYAMContactList();  //清空缓存
                    new HXModel(AppController.getInstance())  //重新保存数据
                            .saveContactList(contactList);
                    new IMGetUnRegistListRequest(Account.user.getShopId()).start(); //获取非注册列表
                    contactList.clear();  //保存完后清除
                }
            }else{
                new IMGetUnRegistListRequest(Account.user.getShopId()).start(); //获取非注册列表
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
