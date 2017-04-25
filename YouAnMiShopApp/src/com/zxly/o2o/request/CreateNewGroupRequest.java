package com.zxly.o2o.request;

import android.text.TextUtils;
import android.util.Log;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by hejun on 2016/9/21.
 * 会员--新增分组
 */
public class CreateNewGroupRequest extends BaseRequest{

    private int id;

    public CreateNewGroupRequest(List<Long> menberIds, String name){
        addParams("memberIds",menberIds);
        addParams("name",name);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        //后台传这个id就是这么chubao~~~
        if(!TextUtils.isEmpty(data)){
            id = Integer.parseInt(data);
        }
    }

    public int getId() {
        return id;
    }


    @Override
    protected String method() {
        return "/keduoduo/member/addMemberGroup";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
