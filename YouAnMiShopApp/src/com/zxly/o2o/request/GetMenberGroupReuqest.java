package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.FansGroupModel;
import com.zxly.o2o.model.MenberGroupModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/13.
 */
public class GetMenberGroupReuqest extends BaseRequest{
    private final boolean b;
    private List<MenberGroupModel> menberGroupModelList =new ArrayList<MenberGroupModel>();
    public int memberCount;

    public int getMemberCount() {
        return memberCount;
    }

    public GetMenberGroupReuqest(boolean b){
        this.b=b;
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray=new JSONArray(data);
            int length=jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MenberGroupModel menberGroupModel = new MenberGroupModel();
                menberGroupModel.setId(jsonObject.optInt("id"));
                menberGroupModel.setIsCustomerGroup(jsonObject.optInt("isCustomerGroup"));
                menberGroupModel.setMemberCount(jsonObject.optInt("memberCount"));
                menberGroupModel.setNewMsgMemberCount(jsonObject.optInt("newMsgMemberCount"));
                if(jsonObject.optString("name").equals("我的所有会员")){
                    memberCount = jsonObject.optInt("memberCount");
                }
                menberGroupModel.setName(jsonObject.optString("name"));
                menberGroupModelList.add(menberGroupModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String method() {
        return "/keduoduo/member/memberGroupList";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return b;
    }

    public  List<MenberGroupModel> getMenberGroups() {
        return menberGroupModelList;
    }
}
