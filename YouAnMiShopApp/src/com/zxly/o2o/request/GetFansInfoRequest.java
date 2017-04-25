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
 * Created by hejun on 2016/9/9.
 * 获取粉丝列表
 */
public class GetFansInfoRequest extends BaseRequest{

    private final boolean showDialog;
    private List<FansInfo> fansInfoList=new ArrayList<FansInfo>();

    /**
     *
     * @param groupName 粉丝组名
     */
    public  GetFansInfoRequest(String groupName,boolean showDialog){
        this.showDialog=showDialog;
        addParams("group",groupName);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray = new JSONArray(data);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FansInfo fansInfo = new FansInfo();
                fansInfo.setBuyIntention(jsonObject.optInt("buyIntention"));
                fansInfo.setFansId(jsonObject.optInt("fansId"));
                fansInfo.setHasNew(jsonObject.optInt("hasNew"));
                fansInfo.setHasRemark(jsonObject.optInt("hasRemark"));
                fansInfo.setImei(jsonObject.optString("imei"));
                fansInfo.setName(jsonObject.optString("name"));
                fansInfo.setPhoneModel(jsonObject.optString("phoneModel"));
                fansInfo.setIsFocus(jsonObject.optInt("isFocus"));
                fansInfo.setPhone(jsonObject.optString("phone"));
                fansInfoList.add(fansInfo);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected boolean isShowLoadingDialog() {
        return showDialog;
    }

    public List<FansInfo> getFansInfoList() {
        return fansInfoList;
    }

    @Override
    protected String method() {
        return "/keduoduo/fans/list";
    }
}
