package com.easemob.easeui.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.model.GetuiMsg;
import com.easemob.easeui.model.GetuiMsgVO;
import com.easemob.easeui.model.GetuiTypeMsg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 * 个推具体类别所有消息请求
 */
public class GetuiTypeMsgRequest extends HXNormalRequest{
    public List<GetuiTypeMsg> getuiTypeMsgList=new ArrayList<GetuiTypeMsg>();
    private GetuiMsg getuiMsg;
    private GetuiMsgVO getuiMsgVO;
    private String expend;
    private int busId;
    private int what;
    private String h5Url;
    private String headUrl;
    private String title;
    private String content;
    private int dataId;
    private long createTime;

    @Override
    protected String method() {
        return "/getui/type/data/list";
    }

    /**
     *
     * @param pageIndex 分页序号
     * @param type 消息类别
     */
    public GetuiTypeMsgRequest(int pageIndex,int type){
        addParams("pageIndex",pageIndex);
        addParams("type",type);
        if(EaseConstant.shopID>0){
            addParams("clientId",EaseConstant.getuiUserClientID);
        }else{
            addParams("clientId",EaseConstant.getuiShopClientID);
        }
        addParams("shopId", Math.abs(EaseConstant.shopID));
        if(EaseConstant.currentUser.getFirendsUserInfo()!=null&&EaseConstant.currentUser.getFirendsUserInfo().getId()!=0){
            addParams("userId",EaseConstant.currentUser.getFirendsUserInfo().getId());
        }
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONArray jsonArray = new JSONArray(data);
            int len=jsonArray.length();
            for(int i=0;i<len;i++){
                JSONObject jb = jsonArray.getJSONObject(i);
                long id = jb.optLong("id");
                String body = jb.optString("body");
                int status = jb.optInt("status");
                if(body!=""){
                    JSONObject bodyJson = new JSONObject(body);
                    expend = bodyJson.optString("expend");
                    busId = bodyJson.optInt("busId");
                    what = bodyJson.optInt("what");
                    createTime = bodyJson.optLong("createTime");
                    if(expend !=""){
                        JSONObject expendJson = new JSONObject(expend);
                        h5Url = expendJson.optString("h5Url");
                        headUrl = expendJson.optString("headUrl");
                        title = expendJson.optString("title");
                        content = expendJson.optString("content");
                        dataId = expendJson.optInt("dataId");
                    }
                }
                GetuiTypeMsg getuiTypeMsg = new GetuiTypeMsg(id, status, "", busId, what, "",
                        createTime, h5Url, dataId, headUrl,title, content);
                getuiTypeMsgList.add(getuiTypeMsg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected boolean isShowLoadingDialog() {
        return super.isShowLoadingDialog();
    }
}
