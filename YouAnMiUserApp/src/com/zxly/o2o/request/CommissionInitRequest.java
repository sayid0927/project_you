package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.CommissionRecord;
import com.zxly.o2o.util.DataUtil;

import org.json.JSONObject;

import java.util.List;

public class CommissionInitRequest extends BaseRequest {

    private float totalCommission;
    private float willArrive;
    private List<CommissionRecord> orderComms;


    public float getWillArrive() {
        return willArrive;
    }


    public float getTotalCommission() {
        return totalCommission;
    }


    public List<CommissionRecord> getOrderComms() {
        return orderComms;
    }

    public CommissionInitRequest() {
        addParams("shopId", Config.shopId);
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
        } catch (Exception e) {
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
