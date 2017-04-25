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
 * 获取消费轨迹信息   线下粉丝详情页面
 */
public class GetNewConsumptionDataRequest extends BaseRequest{

    private final boolean showDialog;
    private List<RemarkBaseInfo> menberConsumptionDataList=new ArrayList<RemarkBaseInfo>();
    /**
     *
     * @param menberId 会员id
     * @param pageIndex 页数
     */
    public GetNewConsumptionDataRequest(long menberId,int pageIndex,boolean showDialog){
        this.showDialog=showDialog;
        addParams("id",menberId);
        addParams("pageIndex",pageIndex);
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
                menberConsumptionDataList.add(menberBehaviorModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return showDialog;
    }

    @Override
    protected String method() {
        return "/keduoduo/member/findConsumption";
    }

    public List<RemarkBaseInfo> getMenberConsumptionDataList(){
        return menberConsumptionDataList;
    }
}
