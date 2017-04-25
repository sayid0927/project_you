package com.easemob.chatuidemo.request;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.easemob.chatuidemo.HXApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/9.
 */
public class HXTokenRequest extends HXBaseRequest {

    public static String HXToken = "";

    public HXTokenRequest() {
        addParams("client_id", HXApplication.getInstance().getHXConfigInfo().clientID);
        addParams("client_secret",HXApplication.getInstance().getHXConfigInfo().clientSecret);
        addParams("grant_type", "client_credentials");
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.e(TAG, "接口返回:" + parsed);

        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }

        JSONObject jo;
        try {
            jo = new JSONObject(parsed);
            if (jo.has("access_token")) {
                HXToken = jo.getString("access_token");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("=====HXTOken", HXToken);
        //        new HXUserStatusRequest().start();

        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> head = new HashMap<String, String>();
        head.put("Content-Type", "application/json");
        return head;
    }

    @Override
    protected String method() {
        return null;

    }

    @Override
    public String getUrl() {
        return new StringBuffer("https://a1.easemob.com/")
                .append(HXApplication.getInstance().getHXConfigInfo().orgName)
                .append("/")
                .append(HXApplication.getInstance().getHXConfigInfo().appName )
                .append("/token").toString();
    }


}
