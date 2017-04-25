package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.FansInfo;
import com.zxly.o2o.util.DataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/19.
 */
public class FilterFansRequest extends BaseRequest{
    private List<FansInfo> fansInfoList=new ArrayList<FansInfo>();
    /**
     *
     * @param gender 性别  可为空
     * @param groupIds 组ids 可为空
     * @param intentionLevels 购机意向 可为空
     * @param operatorIds 运营商集合 可为空
     */
    public FilterFansRequest(int gender, List<String> groupIds,List<String> intentionLevels,List<String> operatorIds){
        if(gender!=0){
            addParams("gender",gender);
        }
        if(!DataUtil.listIsNull(groupIds)){
            addParams("groupIds",groupIds);
        }
        if(!DataUtil.listIsNull(intentionLevels)){
            addParams("intentionLevels",intentionLevels);

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
                FansInfo fansInfo = new FansInfo();
                fansInfo.setBuyIntention(jsonObject.optInt("intentionLevel"));
                fansInfo.setFansId(jsonObject.optInt("id"));
                fansInfo.setImei(jsonObject.optString("imei"));
                fansInfo.setPhoneModel(jsonObject.optString("productName"));
                fansInfo.setName(jsonObject.optString("remarkName"));
                fansInfoList.add(fansInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    public List<FansInfo> getFansInfoList() {
        return fansInfoList;
    }
    @Override
    protected String method() {
        return "/keduoduo/push/selectFans";
    }
}
