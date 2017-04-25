package com.easemob.chatuidemo.request;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.easeui.AppException;
import com.easemob.easeui.controller.EaseUI;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author benjamin
 * @version YIBA-O2O 2014-12-26
 * @since YIBA-O2O
 */
public abstract class HXBaseRequest extends Request<String> {
    public static final String TAG = "HXNormalRequest";

    private Map<String, Object> params = null;

    protected ResponseStateListener listener;

    short RESULT_SUCCEED = 0;
    private int code;

    final int RESULT_TOKEN_PAST = 20102;// Token过期

    final int RESULT_TOKEN_NULLITY = 20101;// Token无效


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

    @Override
    public String getBodyContentType() {

        return "application/json; charset=" + getParamsEncoding();
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
                Toast.makeText(HXApplication.applicationContext, ae.getErrorMsg(), Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
//                Toast.makeText(HXApplication.applicationContext, "服务器异常", Toast.LENGTH_SHORT).show();
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
        EaseUI.getInstance().requestQueue.add(this);
    }


    public void setOnResponseStateListener(ResponseStateListener listener) {
        this.listener = listener;
    }
}
