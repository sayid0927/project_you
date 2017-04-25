package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.FansGroupModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/9.
 * 获取粉丝分组
 */
public class GetFansGroupRequest extends BaseRequest{
    private final boolean showDialog;
    private List<FansGroupModel> fansGroups=new ArrayList<FansGroupModel>();
    public int fansNum;

    public GetFansGroupRequest(boolean showDialog){
        this.showDialog=showDialog;
        addParams("shopId", Account.user.getShopId());
    }
    @Override
    protected String method() {
        return "/keduoduo/fans/group";
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObj = new JSONObject(data);
            fansNum = jsonObj.optInt("fansNum");
            JSONArray jsonArray = jsonObj.optJSONArray("groups");
            int length=jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FansGroupModel fansGroupModel = new FansGroupModel();
                fansGroupModel.setGroup(jsonObject.optString("group"));
                fansGroupModel.setNum(jsonObject.optInt("num"));
                fansGroupModel.setNewBehaviorNum(jsonObject.optInt("newBehaviorNum"));
                fansGroups.add(fansGroupModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return showDialog;
    }

    public List<FansGroupModel> getFansGroups() {
        return fansGroups;
    }

    public int getFansNum(){
        return fansNum;
    }
}
