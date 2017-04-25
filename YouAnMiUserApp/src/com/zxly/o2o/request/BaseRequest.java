package com.zxly.o2o.request;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.util.PerfUtils;
import com.yintong.pay.utils.BaseHelper;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.HmacUtil;
import com.zxly.o2o.util.MD5Util;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @param <T>
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
    protected int code;

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
                Config.timeOffset = jo.getLong("currentTimes") - System.currentTimeMillis();
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
                        ViewUtils.startActivity(intent, AppController
                                .getInstance().getTopAct());
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
        }

        EaseConstant.dataBaseUrl = Config.dataBaseUrl;
        EaseConstant.imei = Config.imei;
        EaseConstant.shopID = Config.shopId;  //用户端shopId标示
        EaseConstant.getuiUserClientID=Config.getuiClientId;
        serialNO=System.currentTimeMillis();
        head.put("SerialNO", serialNO + "");
        head.put("DeviceID", Config.imei);
        head.put("DeviceType", "1");//设备类型 1：android  2：ios
        head.put("brand", Build.BRAND);
        head.put("phoneModel", Build.MODEL);
        head.put("TargetID", Config.shopId + "");
        long userId=PreferUtil.getInstance().getUserId();
        if(userId>0)
        {
            head.put("userId",userId+"");
        }
        if (isSign()) {
            head.put("sign", getSign());
        }
        return head;
    }

    private String getSign() {

        StringBuffer param = new StringBuffer();

        // 将参数拼装key=value形式。
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof List) {
                List<Object> list = (List<Object>) entry.getValue();
                for (Object obj : list) {

                    List<NameValuePair> nvpList = BaseHelper.bean3Parameters(obj);
                    for (NameValuePair nameVal : nvpList) {
                        treeMap.put(nameVal.getName(), nameVal.getValue());
                    }
                }
            } else if (value instanceof String || value instanceof Long || value instanceof Integer ||
                    value instanceof Float) {
                treeMap.put(entry.getKey(), entry.getValue() + "");
            } else {
                List<NameValuePair> nvpList = BaseHelper.bean3Parameters(entry.getValue());
                for (NameValuePair nameVal : nvpList) {
                    treeMap.put(nameVal.getName(), nameVal.getValue());
                }
            }


        }
        int i = 0;
        int last = treeMap.size() - 1;
        for (Map.Entry<String, String> treeEntry : treeMap.entrySet()) {
            param.append(treeEntry.getKey());
            param.append("=");
            param.append(treeEntry.getValue());
            if (i != last) {
                param.append("&");
            }
            i++;
        }
        String secKey = Account.user.getMobilePhone() + Account.user.getId();

        String md5String = secKey + param.toString() + secKey;
        try {
            md5String = URLEncoder.encode(md5String, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return MD5Util.GetMD5Code(md5String);
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
                String errorMsg = ae.getErrorMsg();
                if (isShowErrorMsg()&&!StringUtil.isNull(errorMsg)) {
                    ViewUtils.showToast(ae.getErrorMsg());
                }
            } else if (error instanceof ServerError) {
                ViewUtils.showToast("服务器异常");
            } else if (error instanceof NoConnectionError) {
                ViewUtils.showToast("网络错误，请检查网络后重试");
            } else if (error instanceof TimeoutError) {
                ViewUtils.showToast("连接超时");
            }
        }

        if (listener != null) {
            listener.onFail(code);
        }

    }
    protected boolean isShowErrorMsg()
    {
        return true;
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
