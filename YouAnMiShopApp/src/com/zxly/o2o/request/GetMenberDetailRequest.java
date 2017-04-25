package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.MenberInfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/18.
 * 获取线下粉丝（会员）详情
 */
public class GetMenberDetailRequest extends BaseRequest{
    private MenberInfoModel menberDetail =new MenberInfoModel();
    public GetMenberDetailRequest(long id){
        addParams("id",id);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            menberDetail.setId(jsonObject.optLong("id"));
            menberDetail.setHeadUrl(jsonObject.optString("headUrl"));
            menberDetail.setDeviceName(jsonObject.optString("deviceName"));
            menberDetail.setGender(jsonObject.optInt("gender"));
            menberDetail.setIsNewBehavior(jsonObject.optInt("isNewBehavior"));
            menberDetail.setIsNewConsumption(jsonObject.optInt("isNewConsumption"));
            menberDetail.setMobilePhone(jsonObject.optString("mobilePhone"));
            menberDetail.setNickname(jsonObject.optString("nickname"));
            menberDetail.setRemarkName(jsonObject.optString("remarkName"));
            menberDetail.setUserName(jsonObject.optString("userName"));
            menberDetail.setNewBehaviorCount(jsonObject.optInt("newBehaviorCount"));
            if(jsonObject.has("labels")){
                JSONArray labelsArry = jsonObject.optJSONArray("labels");
                if(labelsArry.length()>0){
                    List<String> labels=new ArrayList<String>();
                    for (int i = 0; i < labelsArry.length(); i++) {
                        labels.add(labelsArry.get(i).toString());
                    }
                    menberDetail.setLabels(labels);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String method() {
        return "/keduoduo/member/memberDetail";
    }

    public MenberInfoModel getMenberDtail(){
        return menberDetail;
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
