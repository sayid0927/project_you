package com.zxly.o2o.request;

import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.HXModel;
import com.easemob.easeui.AppException;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class IMGetUnRegistListRequest extends BaseRequest {
    public static int pageIndex = 1;
    private List<EaseUser> unRegistList = new ArrayList<EaseUser>();

    public IMGetUnRegistListRequest(long shopId) {
        addParams("shopId", shopId);
        addParams("pageIndex", pageIndex);
        addParams("pageSize",3000);
    }

    @Override
    protected String method() {
        return "/shop/shopUser/unRegisterUser/list";
    }

    @Override
    protected void fire(String data) throws AppException {
        JSONObject jo;
        try {
            jo = new JSONObject(data);
            if (jo.has("users")) {
                data = jo.getString("users");


                TypeToken<List<EaseUser>> token = new TypeToken<List<EaseUser>>() {
                };
                unRegistList.addAll(GsonParser.getInstance().fromJson(data, token));

                if (jo.getInt("allPage") > pageIndex) {
                    pageIndex++;
                    new IMGetUnRegistListRequest(Account.user.getShopId()).start();
                } else {
                    EaseContactAdapter.unRegistList.clear();  //清空缓存
                    new HXModel(AppController.getInstance())  //重新保存数据
                            .saveUnRegistList(unRegistList);
                    unRegistList.clear();  //保存完后清除
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
