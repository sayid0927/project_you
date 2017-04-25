package com.zxly.o2o.request;

import com.easemob.easeui.AppException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hejun on 2016/9/29.
 */
public class GetDefineMsgRequest extends BaseRequest{


    private String content;
    private String imgUrls;

    public GetDefineMsgRequest(int id){
        addParams("id",id);
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if(jsonObject.has("content")){
                content = jsonObject.optString("content");
            }
            if(jsonObject.has("imgUrls")){
                imgUrls = jsonObject.optString("imgUrls");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String method() {
        return "/keduoduo/push/getCustomContent";
    }

    public String getContent(){
        return content;
    }

    public String getImgUrls(){
        return imgUrls;
    }
}
