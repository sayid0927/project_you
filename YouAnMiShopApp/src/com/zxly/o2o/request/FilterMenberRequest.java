package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.MenberInfoModel;
import com.zxly.o2o.util.DataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/19.
 */
public class FilterMenberRequest extends BaseRequest{
    private List<MenberInfoModel> menberInfoModelList=new ArrayList<MenberInfoModel>();
    /**
     *
     * @param gender 性别  可为空
     * @param groupIds 组ids 可为空
     * @param operatorIds 运营商集合 可为空
     */
    public FilterMenberRequest(int gender, List<Long> groupIds, List<String> operatorIds){
        if(gender!=0){
            addParams("gender",gender);
        }
        if(!DataUtil.listIsNull(groupIds)){
            addParams("groupIds",groupIds);
        }

        if(!DataUtil.listIsNull(operatorIds)){
            addParams("operatorIds",operatorIds);

        }
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray = new JSONArray(data);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MenberInfoModel menberInfoModel = new MenberInfoModel();
                menberInfoModel.setHeadUrl(jsonObject.optString("headUrl"));
                menberInfoModel.setId(jsonObject.optInt("id"));
                menberInfoModel.setNickname(jsonObject.optString("nickName"));
//                menberInfoModel.setPhoneModel(jsonObject.optString("productName"));
                menberInfoModel.setRemarkName(jsonObject.optString("remarkName"));
                menberInfoModel.setUserName(jsonObject.optString("userName"));
                menberInfoModelList.add(menberInfoModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String method() {
        return "/keduoduo/push/selectUser";
    }

    public List<MenberInfoModel> getMenberInfoModelList() {
        return menberInfoModelList;
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
