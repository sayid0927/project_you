package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.TagModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/18.
 */
public class ShopTagRequest extends BaseRequest{
    private List<TagModel> tagModelList=new ArrayList<TagModel>();

    public ShopTagRequest(){
        addParams("shopId", Account.user.getShopId());
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray = new JSONArray(data);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TagModel tagModel = new TagModel(jsonObject.optString("name"),jsonObject.optLong("id"),false);
                tagModelList.add(tagModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return false;
    }

    @Override
    protected String method() {
        return "/keduoduo/user/labels";
    }

    public List<TagModel> getTagModelList(){
        return tagModelList;
    }
}
