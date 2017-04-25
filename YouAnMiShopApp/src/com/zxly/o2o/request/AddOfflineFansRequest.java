package com.zxly.o2o.request;

import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.easemob.easeui.AppException;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fengrongjian on 2016/9/20
 * 新增线下粉丝
 */
public class AddOfflineFansRequest extends BaseRequest {

    private int fansId;

    public AddOfflineFansRequest(int buyIntention, int buyOffline, int gender, ArrayList<Long> labels, String name, String phone, String productName, String productPrice, String remarkContent, String phoneBrand) {
        addParams("buyIntention", buyIntention);
        addParams("buyOffline", buyOffline);
        addParams("gender", gender);
        addParams("labels", labels);
        addParams("name", name);
        addParams("phone", phone);
        addParams("productName", productName);
        addParams("phoneModel",phoneBrand);
        if (!StringUtil.isNull(productPrice)) {
            addParams("productPrice", Double.parseDouble(productPrice));
        }
        addParams("remarkContent", remarkContent);
    }

    @Override
    protected void fire(String data) {
        try {
            JSONObject jb = new JSONObject(data);
            fansId = jb.optInt("fansId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getFansId(){
        return fansId;
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "keduoduo/fans/addOfflineFans";
    }

}
