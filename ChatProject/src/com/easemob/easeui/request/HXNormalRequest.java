package com.easemob.easeui.request;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-26
 * @since YIBA-O2O
 */
public abstract class HXNormalRequest extends Request<String> {
    public static final String TAG = "HXNormalRequest";

    private Map<String, Object> params = null;

    protected ResponseStateListener listener;

    short RESULT_SUCCEED = 0;

    private int code;

    final int RESULT_TOKEN_PAST = 20102;// Token过期

    final int RESULT_TOKEN_NULLITY = 20101;// Token无效

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
        try {
            JSONObject jo = new JSONObject(parsed);
            code = jo.getInt("code");
            String data = "";

            if (jo.has("data")) {
                data = jo.getString("data");
            }

            if (code == RESULT_SUCCEED) {
                if (!TextUtils.isEmpty(data)) {
                    fire(data);
                }
            }
            //消息撤回特有返回码（由于前期代码没有判断这种情况 但有多处用到故不作太大修改 仅特别对该code单独处理）
            if(code==20109){
                return Response.error(new VolleyError(new AppException(code,
                        data)));
            }
        } catch (JSONException e) {
            if (isHandlerJSONError()) {
                return Response.error(new VolleyError(new AppException("数据解析异常")));
            }
        } catch (AppException e) {
            e.printStackTrace();
        }

        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));

    }

    protected boolean isHandlerJSONError() {
        return true;
    }

    public void printReqParam(StringBuilder encodedParams) {
        Log.e(TAG, "接口地址:" + this.mUrl);
        Log.e(TAG, "接口入参:" + encodedParams.toString());

    }

    protected void addParams(String key, Object value) {
        if (params == null)
            params = new HashMap<String, Object>();
        params.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        return EaseConstant.dataBaseUrl+method();
    }

    @Override
    public String getBodyContentType() {

        return "application/json; charset=" + getParamsEncoding();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> head = new HashMap<String, String>();

        if (EaseConstant.token.length() != 0) {
            head.put("Authorization", EaseConstant.token);
        }

        head.put("SerialNO", System.currentTimeMillis() + "");
        head.put("DeviceID", EaseConstant.imei);
        head.put("DeviceType", "1");//设备类型 1：android  2：ios
        head.put("TargetID", EaseConstant.shopID + "");
        long userId = EaseConstant.currentUser.getFirendsUserInfo().getId();
        if (userId > 0) {
            head.put("userId", userId + "");
        }
        return head;
    }


    /**
     * 用于异步处理数据
     */
    protected void fire(String data) throws AppException {

    }

    abstract protected String method();

    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, Object> getParams() throws AuthFailureError {
        // Log.d("params", params.toString());
        return params;
    }

    protected boolean isShowLoadingDialog() {
        return false;
    }

    @Override
    protected void deliverResponse(String response) {
        if (listener != null) {
            listener.onOK();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deliverError(VolleyError error) {
        if (error != null) {
            AppException ae;
            if (error.getCause() instanceof AppException) {
                ae = (AppException) error.getCause();
                //个推中单独状态（不属于错误码），所以不需要将此信息通过toast展示出来
                if(!"公告状态正常".equals(ae.getErrorMsg())){
                    Toast.makeText(HXApplication.applicationContext, ae.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            } else if (error instanceof ServerError) {
                Toast.makeText(HXApplication.applicationContext, "服务器异常", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
//                Toast.makeText(HXApplication.applicationContext, "请检查您的网络", Toast.LENGTH_SHORT).show();
            } else if (error instanceof TimeoutError) {
                Toast.makeText(HXApplication.applicationContext, "连接超时", Toast.LENGTH_SHORT).show();
            }
        }

        if (listener != null) {
            listener.onFail(code);
        }

    }

    public boolean listIsEmpty(List list) {

        return list == null || list.isEmpty();
    }

    protected AppException JSONException(JSONException e) {
        return new AppException("数据解析异常", e);
    }

    public interface ResponseStateListener {
        public void onOK();

        public void onFail(int code);
    }

    public void start(Object o) {
        this.setTag(o);
        start();
    }

    public void start() {
        EaseUI.requestQueue.add(this);
    }


    public void setOnResponseStateListener(ResponseStateListener listener) {
        this.listener = listener;
    }
}
