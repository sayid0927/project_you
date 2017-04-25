package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.UserAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2016/10/11.
 */
public class OrderBranchsRequest extends BaseRequest {
    public List<UserAddress> addressList=new ArrayList<UserAddress>();
    public OrderBranchsRequest(int pageIndex)
    {
        addParams("pageIndex",pageIndex);
        addParams("pageSize",20);
        addParams("shopId", Config.shopId);
    }
    @Override
    protected String method() {
        return "/order/branchs";
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray=new JSONArray(data);
            int lenght=jsonArray.length();
            for(int i=0;i<lenght;i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                UserAddress userAddress=new UserAddress();
                userAddress.setAddress(jsonObject.optString("address"));
                userAddress.setId(jsonObject.optLong("id"));
                userAddress.setName(jsonObject.optString("name"));
                userAddress.setMobilePhone(jsonObject.optString("telephone"));
                addressList.add(userAddress);
            }
        } catch (JSONException e) {
            throw  JSONException(e);
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
