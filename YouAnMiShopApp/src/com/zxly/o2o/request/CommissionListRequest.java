package com.zxly.o2o.request;



import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.CommissionRecord;

import org.json.JSONObject;

import java.util.List;

public class CommissionListRequest extends BaseRequest {


    private List<CommissionRecord> orderComms;

    public List<CommissionRecord> getOrderComms() {
        return orderComms;
    }

    public CommissionListRequest(int pageId) {
        addParams("shopId",Account.user.getShopId());
        addParams("pageIndex", pageId);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject json = new JSONObject(data);
            if(json.has("orderComms")){
                TypeToken<List<CommissionRecord>> types = new TypeToken<List<CommissionRecord>>() {};
                String str = json.getString("orderComms");
                orderComms = GsonParser.getInstance().fromJson(str, types);
            }
        } catch (Exception e) {

        }

    }

    @Override
    protected String method() {
        return "/popu/userCommissionList/list";
    }

}
