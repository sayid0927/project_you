package com.zxly.o2o.request;

import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *     @author huangbin  @version 创建时间：2015-1-22 上午9:48:02    类说明: 
 */
public class FileUploadRequest {
    private static MultiPartStringRequest multiPartRequest;
    private Handler mHandler;
    private LoadingDialog dialog;

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

    public void startUpload() {
        if (dialog == null) {
            dialog = new LoadingDialog();
            dialog.setCancelable(true, new CallBack() {
                @Override
                public void onCall() {
                    if(multiPartRequest!=null)
                    multiPartRequest.cancel();
                }
            });
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
                    String data=jo.has("data")?jo.getString("data"):"请求失败";
                    if (jo.getString("code").equals("00000") || jo.getString("code").equals("20105")) {
                        mHandler.obtainMessage(Constants.MSG_SUCCEED, data).sendToTarget();
                    } else {
                        mHandler.obtainMessage(Constants.MSG_FAILED, data).sendToTarget();
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
                ViewUtils.showToast(error.networkResponse != null?"发布失败!":"网络连接异常!");
                mHandler.sendEmptyMessage(Constants.MSG_FAILED);
            }
        }
    };

    public static void addPutUploadFileRequest(final String url, final Map<String, File> files, final Map<String, Object> params, final Listener<String> responseListener, final ErrorListener errorListener, final Object tag) {
        if (null == url || null == responseListener) {
            return;
        }

        multiPartRequest = new MultiPartStringRequest(Request.Method.POST, url, responseListener, errorListener) {
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
