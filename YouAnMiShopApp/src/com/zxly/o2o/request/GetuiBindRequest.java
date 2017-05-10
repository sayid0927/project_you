package com.zxly.o2o.request;

import android.util.Log;

import com.android.volley.VolleyError;
import com.zxly.o2o.account.Account;

/**
 * Created by hejun on 2016/9/18.
 * 新增个推id绑定 收到cid时调用一次  登录成功也会调用一次
 */
public class GetuiBindRequest extends BaseRequest{
    public GetuiBindRequest(String clientId){
        addParams("clientId",clientId);
        if(Account.user!=null&&Account.user.getId()!=0)
        {//仅登录时候传
            addParams("shopId", Account.user.getShopId());
            addParams("userId", Account.user.getId());
            Log.e("TAG","clientId  == "+clientId+"   shopId == "+Account.user.getShopId());
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        if (listener != null) {
            listener.onFail(0000);
        }
    }

    @Override
    protected String method() {
        return "/getui/sis/bind";
    }
}
