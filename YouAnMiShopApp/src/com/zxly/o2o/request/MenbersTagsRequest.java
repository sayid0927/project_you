package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class MenbersTagsRequest extends BaseRequest{

    private List<String> groupIds;
    private List<String> labelsIds;

    public MenbersTagsRequest(long id){
        addParams("id",id);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if(jsonObject.has("groupIds")){
                JSONArray jsonArray = jsonObject.optJSONArray("groupIds");
                groupIds = new ArrayList<String>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    groupIds.add( jsonArray.get(i)+"");
                }
            }

            if(jsonObject.has("labelIds")){
                JSONArray jsonArray = jsonObject.optJSONArray("labelIds");
                labelsIds = new ArrayList<String>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    labelsIds.add( jsonArray.get(i)+"");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String method() {
        return "/keduoduo/member/memberExt";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    public List<String> getGroupIds(){
        return groupIds;
    }

    public List<String> getLabelsIds(){
        return labelsIds;
    }
}
