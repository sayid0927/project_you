package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.MenberInfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/13.
 */
public class GetMenberInfoRequest extends BaseRequest{

    private List<MenberInfoModel> menberInfoModelList=new ArrayList<MenberInfoModel>();
    private boolean isshowLoading=true;

    public GetMenberInfoRequest(int groupId){
        addParams("groupId",groupId);
        addParams("shopId", Account.user.getShopId());
    }

    public  GetMenberInfoRequest(String groupName){
        addParams("groupName",groupName);
        addParams("shopId", Account.user.getShopId());
    }

    public void setIsshowLoading(boolean isshowLoading){
        this.isshowLoading=isshowLoading;
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray = new JSONArray(data);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MenberInfoModel menberInfo = new MenberInfoModel();
                menberInfo.setHeadUrl(jsonObject.optString("headUrl"));
                menberInfo.setId(jsonObject.optLong("id"));
                menberInfo.setIsBuyOnline(jsonObject.optInt("isBuyOnline"));
                menberInfo.setIsNewBehavior(jsonObject.optInt("isNewBehavior"));
                menberInfo.setLastBehaviorTime(jsonObject.optLong("lastBehaviorTime"));
                menberInfo.setLastPhoneTime(jsonObject.optLong("lastPhoneTime"));
                menberInfo.setLastSmsTime(jsonObject.optLong("lastSmsTime"));
                menberInfo.setMobilePhone(jsonObject.optString("mobilePhone"));
                menberInfo.setNickname(jsonObject.optString("nickname"));
                menberInfo.setRemarkName(jsonObject.optString("remarkName"));
                menberInfo.setUserName(jsonObject.optString("userName"));
                menberInfoModelList.add(menberInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return isshowLoading;
    }

    public List<MenberInfoModel> getMenberInfoList() {
        return menberInfoModelList;
    }

    @Override
    protected String method() {
        return "/keduoduo/member/memberList";
    }
}
