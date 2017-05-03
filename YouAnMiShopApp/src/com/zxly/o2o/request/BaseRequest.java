package com.zxly.o2o.request;

import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.HmacUtil;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

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
public abstract class BaseRequest extends Request<String> {
    public static final String TAG = "BaseRequest";
    private Map<String, Object> params = null;

    private LoadingDialog dialog;
    protected ResponseStateListener listener;

    short RESULT_SUCCEED = 0;
    private int code = -1;

    final int RESULT_TOKEN_PAST = 20102;// Token过期

    final int RESULT_TOKEN_NULLITY = 20101;// Token无效
    private long serialNO=0;
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {

            parsed = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            AppLog.e(TAG, "接口返回:" + parsed);

        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        try {
            JSONObject jo = new JSONObject(parsed);
            code = jo.getInt("code");
            String data = "";
            if (jo.has("currentTimes")) {
                Config.currentServerTime = jo.getLong("currentTimes");
                Config.timeOffset = Config.currentServerTime - System.currentTimeMillis();
                EaseConstant.timeOffset = Config.timeOffset;
            }
            if (jo.has("data")) {
                data = jo.getString("data");
            }
            if (code == RESULT_SUCCEED) {
                if (!StringUtil.isNull(data)) {
                    fire(data);
                }
            } else {
                switch (code) {
                    case RESULT_TOKEN_PAST:
                    case RESULT_TOKEN_NULLITY:
                        AppController.getInstance().deleteFile("user");
                        Account.user = null;
                        PreferUtil.getInstance().setLoginToken("");
                        Intent intent = new Intent(AppController.getInstance().getTopAct(), LoginAct.class);
                        ViewUtils.startActivity(intent, AppController.getInstance().getTopAct());
                        return Response.error(new VolleyError(new AppException(code, "")));
                }
                return Response.error(new VolleyError(new AppException(code,
                        data)));
            }

        } catch (JSONException e) {
            if (isHandlerJSONError()) {
                return Response.error(new VolleyError(new AppException("数据解析异常")));
            }


        } catch (AppException e) {
            return Response.error(new VolleyError(e));
        }

        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));

    }

    protected boolean isHandlerJSONError() {
        return true;
    }

    @Override
    public void printReqParam(String encodedParams) {
        AppLog.e(TAG, "接口地址:" + this.mUrl);
        AppLog.e(TAG, "接口流水号:" + serialNO);
        AppLog.e(TAG, "接口入参:" + encodedParams);
    }

    public void addParams(String key, Object value) {
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        params.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        return Config.dataBaseUrl + method();
    }

    @Override
    public String getBodyContentType() {

        return "application/json; charset=" + getParamsEncoding();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> head = new HashMap<String, String>();
        String token = PreferUtil.getInstance().getLoginToken();

        if (!StringUtil.isNull(token)) {
            head.put("Authorization", token);
            EaseConstant.token = token;
            EaseConstant.dataBaseUrl = Config.dataBaseUrl;
            EaseConstant.getuiShopClientID=Config.getuiClientId;
            EaseConstant.imei = Config.imei;
            if (Account.user != null) {
                EaseConstant.shopID = -Account.user.getShopId(); //商户端shopId标示为负数，方便跟用户端区分
            }
        }
        serialNO=System.currentTimeMillis();
        head.put("SerialNO", serialNO + "");
        head.put("DeviceID", AppController.imei);
        head.put("DeviceType", "1");//设备类型 1：android  2：ios

        return head;
    }

    protected boolean isSign() {
        return false;
    }

    /**
     * 是否校验接口签名
     */
    protected boolean isCheckSign() {
        return false;
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
        if (isCheckSign()) {
            try {
                JSONObject jsonObject = new JSONObject(params);
                jsonObject.put("sign", "");
                String signedString = HmacUtil.HmacSHA1Encrypt(jsonObject.toString(), Config.accessKey);
                params.put("sign", signedString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    protected boolean isShowLoadingDialog() {
        return false;
    }

    @Override
    protected void deliverResponse(String response) {
        if (isShowLoadingDialog()) {
            dialog.dismiss();
        }
        if (listener != null) {
            listener.onOK();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deliverError(VolleyError error) {
        if (isShowLoadingDialog()) {
            dialog.dismiss();
        }
        if (error != null) {
            AppException ae;
            if (error.getCause() instanceof AppException) {
                ae = (AppException) error.getCause();
              //  ViewUtils.showToast(ae.getErrorMsg());
            } else if (error instanceof ServerError) {
                ViewUtils.showToast("服务器异常");
            } else if (error instanceof NoConnectionError) {
//                ViewUtils.showToast("请检查您的网络");
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
        if (isShowLoadingDialog()) {
            dialog = new LoadingDialog();
            dialog.show();
        }
        AppController.getInstance().addRequest(this);
    }


    public void setOnResponseStateListener(ResponseStateListener listener) {
        this.listener = listener;
    }

}
