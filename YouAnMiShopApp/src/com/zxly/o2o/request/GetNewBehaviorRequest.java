package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.MenberBehaviorModel;
import com.zxly.o2o.model.RemarkBaseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/19.
 */
public class GetNewBehaviorRequest extends BaseRequest{
    private List<RemarkBaseInfo> menberBehaviorModelList=new ArrayList<RemarkBaseInfo>();
    public GetNewBehaviorRequest(long menberId,int pageNum){
        addParams("id",menberId);
        addParams("pageIndex",pageNum);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                MenberBehaviorModel menberBehaviorModel = new MenberBehaviorModel();
                menberBehaviorModel.setContent(jsonObject.optString("content"));
                menberBehaviorModel.setType(jsonObject.optInt("type"));
                menberBehaviorModel.setIsNew(jsonObject.optInt("isNew"));
                menberBehaviorModel.setCreateTime(jsonObject.optLong("createTime"));
                menberBehaviorModel.setTargetType(jsonObject.optString("targetType"));
                menberBehaviorModelList.add(menberBehaviorModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    protected String method() {
        return "/keduoduo/member/findBehavior";
    }

    public List<RemarkBaseInfo> getNewBehaviorDataList(){
        return menberBehaviorModelList;
    }
}
