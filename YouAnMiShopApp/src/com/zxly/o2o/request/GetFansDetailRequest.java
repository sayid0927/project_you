package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.FansInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/28.
 */
public class GetFansDetailRequest extends BaseRequest {
    private FansInfo fansInfo =new FansInfo();
    public GetFansDetailRequest(long fansId){
        addParams("fansId",fansId);
        addParams("shopId", Account.user.getShopId());
    }
    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            fansInfo.setBuyIntention(jsonObject.optInt("buyIntention"));
            fansInfo.setGender(jsonObject.optInt("gender"));
            fansInfo.setHasNewBehavior(jsonObject.optInt("hasNewBehavior"));
            fansInfo.setHasNewShopping(jsonObject.optInt("hasNewShopping"));
            fansInfo.setHasRemark(jsonObject.optInt("hasRemark"));
            fansInfo.setImei(jsonObject.optString("imei"));
            fansInfo.setInstallTime(jsonObject.optLong("installTime"));
            fansInfo.setIsOffline(jsonObject.optInt("isOffline"));
            fansInfo.setName(jsonObject.optString("name"));
            fansInfo.setPhone(jsonObject.optString("phone"));
            fansInfo.setPhoneModel(jsonObject.optString("phoneModel"));
            fansInfo.setNewBehaviorCount(jsonObject.optInt("newBehaviorCount"));
            if(jsonObject.has("labels")){
                JSONArray labelsArry = jsonObject.optJSONArray("labels");
                if(labelsArry.length()>0){
                    List<String> labels=new ArrayList<String>();
                    for (int i = 0; i < labelsArry.length(); i++) {
                        labels.add(labelsArry.get(i).toString());
                    }
                    fansInfo.setLabels(labels);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String method() {
        return "/keduoduo/fans/info";
    }

    public FansInfo getFansDtail(){
        return fansInfo;
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
