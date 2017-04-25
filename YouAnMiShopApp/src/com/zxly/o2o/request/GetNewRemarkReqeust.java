package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.RemarkBaseInfo;
import com.zxly.o2o.model.RemarkInfoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/20.
 * 获取备注信息--线下粉丝详情页面
 */
public class GetNewRemarkReqeust extends BaseRequest{
    private final boolean showDialog;
    private List<RemarkBaseInfo>  remarkInfoModelList=new ArrayList<RemarkBaseInfo>();
    public GetNewRemarkReqeust(long id,int pageIndex,boolean showDialog){
        this.showDialog=showDialog;
        addParams("id",id);
        addParams("pageIndex",pageIndex);
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                RemarkInfoModel remarkInfoModel = new RemarkInfoModel();
                remarkInfoModel.setContent(jsonObject.optString("content"));
                remarkInfoModel.setCreateTime(jsonObject.optLong("createTime"));
                remarkInfoModel.setCreatorName(jsonObject.optString("creatorName"));
                remarkInfoModel.setRemarkType(jsonObject.optInt("remarkType"));
                remarkInfoModelList.add(remarkInfoModel);
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
        return "/keduoduo/member/findRemark";
    }

    public List<RemarkBaseInfo> getRemarkInfoModelList(){
        return remarkInfoModelList;
    }
}
