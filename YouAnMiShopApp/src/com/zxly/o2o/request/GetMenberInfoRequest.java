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

        if(data==null||data.length()==0){
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray(data);
            int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    MenberInfoModel menberInfo = new MenberInfoModel();
                    if(jsonObject.has("headUrl"))
                    menberInfo.setHeadUrl(jsonObject.optString("headUrl"));
                    if(jsonObject.has("id"))
                    menberInfo.setId(jsonObject.optLong("id"));
                    if(jsonObject.has("isBuyOnline"))
                    menberInfo.setIsBuyOnline(jsonObject.optInt("isBuyOnline"));
                    if(jsonObject.has("isNewBehavior"))
                    menberInfo.setIsNewBehavior(jsonObject.optInt("isNewBehavior"));
                    if(jsonObject.has("lastBehaviorTime"))
                    menberInfo.setLastBehaviorTime(jsonObject.optLong("lastBehaviorTime"));
                    if(jsonObject.has("lastPhoneTime"))
                    menberInfo.setLastPhoneTime(jsonObject.optLong("lastPhoneTime"));
                    if(jsonObject.has("lastSmsTime"))
                    menberInfo.setLastSmsTime(jsonObject.optLong("lastSmsTime"));
                    if(jsonObject.has("mobilePhone"))
                    menberInfo.setMobilePhone(jsonObject.optString("mobilePhone"));
                    if(jsonObject.has("nickname"))
                    menberInfo.setNickname(jsonObject.optString("nickname"));
                    if(jsonObject.has("remarkName"))
                    menberInfo.setRemarkName(jsonObject.optString("remarkName"));
                    if(jsonObject.has("userName"))
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
