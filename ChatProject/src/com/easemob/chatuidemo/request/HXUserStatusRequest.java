package com.easemob.chatuidemo.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseYAMUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Benjamin on 2015/7/11.
 */
public class HXUserStatusRequest extends HXBaseRequest {
    public static HXUserStatusRequest instance;
    public int isOnline = -1;
    public String toChatUser = "";
    private long userId;
    private int onLinePosition;

    public HXUserStatusRequest(){

    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            android.util.Log.e(TAG, "data:" + parsed);

        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        JSONObject jo;
        try {
            jo = new JSONObject(parsed);
            String data;
            if (jo.has("data")) {
                data = jo.getString("data");
                fire(data);
            } else {
                isOnline = -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (AppException e) {
            return Response.error(new VolleyError(e));
        }

        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }


    @Override
    public String getUrl() {
        userId = HXHelper.yamContactList.get(onLinePosition).getFirendsUserInfo()
                .getId();

        String url = new StringBuffer("https://a1.easemob.com/")
                .append(HXApplication.getInstance().getHXConfigInfo().orgName)
                .append("/")
                .append(HXApplication.getInstance().getHXConfigInfo().appName)
                .append("/users/")
                .append(HXApplication.getInstance().parseUserFromID(userId, HXConstant.TAG_SHOP))
                .append("/status")
                .toString();
        return url;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> head = new HashMap<String, String>();
        head.put("Content-Type", "application/json");

        head.put("Authorization", "Bearer " + HXTokenRequest.HXToken);
        return head;
    }

    @Override
    protected void fire(String data) throws AppException {
        toChatUser = data.substring(2, data.indexOf(":") - 1);
        if (data.contains("online")) {
            //在线
            HXHelper.yamContactList.get(onLinePosition).getFirendsUserInfo().setIsOnline(true);
        }else{
            HXHelper.yamContactList.get(onLinePosition).getFirendsUserInfo().setIsOnline(false);

        }
        if ( HXHelper.yamContactList.size() > onLinePosition + 1&&HXHelper.yamContactList.get
                (onLinePosition+1).getFirendsUserInfo().getIsTop()==1) {
            onLinePosition++;
            this.start();
        }
    }

    @Override
    protected String method() {
        return null;
    }

    @Override
    public int requestMethod() {
        instance = this;
        return Method.GET;
    }
}
