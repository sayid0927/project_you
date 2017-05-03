package com.zxly.o2o.request;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * 文件上传
 *     @author benjamin  @version 创建时间：2015-1-22 上午9:48:02    类说明: 
 */
public class FileUploadRequest {
    private static MultiPartStringRequest multiPartRequest;
    private Handler mHandler;
    private LoadingDialog dialog;
    private boolean isShowLoading=true;

    public FileUploadRequest(Map<String, Object> params, String requestUrl, Handler handler) {
        mHandler = handler;
        // 服务器上传地址
        String uri = Config.dataBaseUrl + requestUrl;
        addPutUploadFileRequest(uri, null, params, mResonseListenerString, mErrorListener, null);
    }

    public FileUploadRequest(File file, Map<String, Object> params, String requestUrl, Handler handler) {
        mHandler = handler;
        Map<String, File> files = null;
        if (file != null) {
            files = new HashMap<String, File>();
            files.put("file", file);
            Log.e("imageupload", " on ImageUrl String===" + file.getPath());
        }

        // 服务器上传地址
        String uri = Config.dataBaseUrl + requestUrl;
        addPutUploadFileRequest(uri, files, params, mResonseListenerString, mErrorListener, null);
    }

    public FileUploadRequest(Map<String, File> files, Map<String, Object> params, String requestUrl, Handler handler) {
        mHandler = handler;
        // 服务器上传地址
        String uri = Config.dataBaseUrl + requestUrl;
        addPutUploadFileRequest(uri, files, params, mResonseListenerString, mErrorListener, null);
    }

    public void setShowLoading(boolean showLoading) {
        isShowLoading = showLoading;
    }

    public void startUpload() {
        if (isShowLoading&&dialog == null) {
            dialog = new LoadingDialog();
            dialog.show();
        }
        AppController.getInstance().addImageUploadRequest(multiPartRequest);
    }

    Listener<String> mResonseListenerString = new Listener<String>() {

        @Override
        public void onResponse(String response) {
            if (mHandler != null) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                try {
                    JSONObject jo = new JSONObject(response);
                    if (jo.getString("code").equals("00000") || jo.getString("code").equals("20105")) {
                        if (jo.has("data")) {
                            mHandler.obtainMessage(Constants.MSG_SUCCEED, jo.getString("data")).sendToTarget();
                        } else {
                            mHandler.obtainMessage(Constants.MSG_SUCCEED).sendToTarget();
                        }
                    } else {
                        if (jo.has("data")) {
                            mHandler.obtainMessage(Constants.MSG_FAILED, jo.getString("data")).sendToTarget();
                        } else {
                            mHandler.obtainMessage(Constants.MSG_FAILED).sendToTarget();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.e("imageupload", " on response String" + response);
        }
    };

    ErrorListener mErrorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            if (error != null) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                ViewUtils.showToast(error.networkResponse != null ? "发布失败!" : "网络连接异常!");
                mHandler.sendEmptyMessage(Constants.MSG_FAILED);
            }
        }
    };

    public static void addPutUploadFileRequest(final String url, final Map<String, File> files, final Map<String, Object> params, final Listener<String> responseListener, final ErrorListener errorListener, final Object tag) {
        if (null == url || null == responseListener) {
            return;
        }

        multiPartRequest = new MultiPartStringRequest(Request.Method.POST, url, responseListener, errorListener, PreferUtil.getInstance().getLoginToken()) {
            @Override
            public Map<String, File> getFileUploads() {
                return files;
            }

            @Override
            public Map<String, Object> getStringUploads() {
                return params;
            }
        };
        Log.e("uploadFile", " volley put : uploadFile " + url);
    }
}
