package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.CommissionRecord;

import org.json.JSONObject;

import java.util.List;

public class CommissionInitRequest extends BaseRequest {

    private float totalCommission;
    private float willArrive;
    private List<CommissionRecord> orderComms;
    public boolean hasNextPage;


    public float getTotalCommission() {
        return totalCommission;
    }

    public float getWillArrive() {
        return willArrive;
    }

    public List<CommissionRecord> getOrderComms() {
        return orderComms;
    }

    public CommissionInitRequest() {
        addParams("shopId", Account.user.getShopId());
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
            totalCommission = (float) json.optDouble("totalCommission");
            willArrive= (float) json.optDouble("willArrive");
            if(orderComms.size()<10){
                hasNextPage = false;
            } else {
                hasNextPage = true;
            }
        } catch (Exception e) {
      //      ViewUtils.showToast("-->"+e.toString());
       Log.d("orderLisr","!!errr"+e.toString());
        }

    }


    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }


    @Override
    protected String method() {
        return "/popu/userCommissionList/init";
    }

}
