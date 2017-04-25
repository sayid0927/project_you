package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.FansInfo;
import com.zxly.o2o.model.FilterPeopleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/8/29.
 */
public class SearchFilterUserRequest extends BaseRequest{

    private List<FilterPeopleModel> filterPeopleModelList=new ArrayList<FilterPeopleModel>();

    public SearchFilterUserRequest(int isReachStore,List<String> operateType,String paramInfo,int userType,int pageIndex){
        addParams("isReachStore",isReachStore);
        if(operateType.size()!=0){
            addParams("operateType",operateType);
        }
        addParams("paramInfo",paramInfo);
        if(userType!=-1){
            addParams("userType",userType);
        }
        addParams("pageIndex",pageIndex);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected String method() {
        return "keduoduo/searchUserInfo";
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray = new JSONArray(data);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                FilterPeopleModel filterPeopleModel = new FilterPeopleModel();
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                filterPeopleModel.setHeadUrl(jsonObject.optString("headUrl"));
                filterPeopleModel.setId(jsonObject.optLong("id"));
                filterPeopleModel.setImei(jsonObject.optString("imei"));
                filterPeopleModel.setMobilePhone(jsonObject.optString("mobilePhone"));
                filterPeopleModel.setUserName(jsonObject.optString("userName"));
                filterPeopleModel.setUserType(jsonObject.optInt("userType"));
                filterPeopleModel.setInputFans(jsonObject.optBoolean("isInputFans"));

                filterPeopleModelList.add(filterPeopleModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public List<FilterPeopleModel> getFilterPeopleModelList(){
        return filterPeopleModelList;
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
