package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.chat.EMConversation;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.GeTuiConversation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/7/15.
 */
public class GetuiTypeDataRequest extends BaseRequest {

    public List<GeTuiConversation> emConversationList=new ArrayList<GeTuiConversation>();
    public GetuiTypeDataRequest()
    {
            addParams("clientId",EaseConstant.getuiShopClientID);
        if(EaseConstant.currentUser.getFirendsUserInfo()!=null&&EaseConstant.currentUser.getFirendsUserInfo().getId()!=0){
            addParams("userId",EaseConstant.currentUser.getFirendsUserInfo().getId());
        }
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray=new JSONArray(data);
            int len=jsonArray.length();
            for(int i=0;i<len;i++)
            {
                JSONObject jb=jsonArray.getJSONObject(i);
                GeTuiConversation shopinfo = new GeTuiConversation("user_shopinfo_push");
                        shopinfo.setUpdateTime(jb.optLong("updateTime"));
                        shopinfo.setTitle(jb.optString("title"));
                        shopinfo.setNumber(jb.optInt("num"));
                        shopinfo.setConversationType(jb.optInt("type"));
                emConversationList.add(shopinfo);
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
        return "/getui/type/data";
    }
}
