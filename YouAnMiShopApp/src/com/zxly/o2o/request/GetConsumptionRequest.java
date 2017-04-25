package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.ConsumptionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/18.
 * 获取消费轨迹
 */
public class GetConsumptionRequest extends BaseRequest{

    private List<ConsumptionModel> consumptionModelList=new ArrayList<ConsumptionModel>();
    public GetConsumptionRequest(long id,int pageIndex){
        addParams("id",id);
        addParams("pageIndex",pageIndex);
        addParams("shopId", Account.user.getShopId());
    }


    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray=new JSONArray(data);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ConsumptionModel consumptionModel = new ConsumptionModel();
                consumptionModel.setContent(jsonObject.getString("content"));
                consumptionModel.setCreteTime(jsonObject.optLong("createTime"));
                consumptionModel.setIsNew(jsonObject.optInt("isNew"));
                consumptionModel.setType(jsonObject.optInt("type"));
                consumptionModelList.add(consumptionModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String method() {
        return "/keduoduo/member/findConsumption";
    }

    public List<ConsumptionModel> getConsumptionModelList(){
        return consumptionModelList;
    }
}
