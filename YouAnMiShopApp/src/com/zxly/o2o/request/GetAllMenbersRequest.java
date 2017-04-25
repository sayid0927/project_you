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
 * Created by hejun on 2016/9/21.
 */
public class GetAllMenbersRequest extends BaseRequest{

    private List<MenberInfoModel> menberInfoModelList=new ArrayList<MenberInfoModel>();

    public  GetAllMenbersRequest(){
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                MenberInfoModel menberInfoModel = new MenberInfoModel();
                menberInfoModel.setHeadUrl(jsonObject.optString("headUrl"));
                menberInfoModel.setId(jsonObject.optLong("id"));
                menberInfoModel.setMobilePhone(jsonObject.optString("mobilePhone"));
                menberInfoModel.setNameAbbr(jsonObject.optString("nameAbbr"));
                menberInfoModel.setRemarkName(jsonObject.optString("remarkName"));
                menberInfoModel.setNickname(jsonObject.optString("nickname"));
                menberInfoModel.setUserName(jsonObject.optString("userName"));
                menberInfoModelList.add(menberInfoModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String method() {
        return "/keduoduo/member/findAllMember";
    }

    public List<MenberInfoModel>  getMenberInfoModelList(){
        return menberInfoModelList;
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
