package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.dialog.BaseDialog;
import com.zxly.o2o.model.FansInfo;
import com.zxly.o2o.model.FilterPeopleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/10/18.
 * 用于得到所有粉丝 线下粉丝除外
 */
public class GetAllFansInfoRequest extends BaseRequest{
    private List<FansInfo> allFanslList=new ArrayList<FansInfo>();

    public GetAllFansInfoRequest(){
        //只搜粉丝
        addParams("userType",1);
        addParams("shopId", Account.user.getShopId());
        //不是线下粉丝
        addParams("isOffLineFans",1);
        //分页大小  可以往死里写
        addParams("pageSize",8000);
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
                FansInfo fansInfo = new FansInfo();
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                fansInfo.setFansId(jsonObject.optInt("id"));
                fansInfo.setImei(jsonObject.optString("imei"));
                fansInfo.setPhone(jsonObject.optString("mobilePhone"));
                fansInfo.setName(jsonObject.optString("userName"));
                fansInfo.setBuyIntention(jsonObject.optInt("intentionLevel"));
                allFanslList.add(fansInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public List<FansInfo> getAllFansList(){
        return allFanslList;
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }
}
