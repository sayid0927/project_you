package com.zxly.o2o.request;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.easemob.easeui.AppException;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dsnx on 2016/8/31.
 */
public class GetSubjectRequest extends BaseRequest {
    private FileOutputStream fos;
    private FileInputStream fis;
    private ByteArrayOutputStream out;
    private  Bitmap bitmap=null;
    private ParameCallBack callBack;
    private int showTime;
    private  boolean isCall;

    public GetSubjectRequest()
    {
        setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                if(!isCall)
                {
                    callBack.onCall(false);
                }

            }

            @Override
            public void onFail(int code) {
                if(!isCall)
                {
                    callBack.onCall(false);
                }
            }
        });
    }


    public void setCallBack(ParameCallBack callBack) {
        this.callBack = callBack;
    }

    public int getShowTime() {
        return showTime;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    protected String method() {
        return "/get/subject";
    }

    @Override
    protected void fire(String data) throws AppException {
                String imgUrl;
            try {
                JSONObject  jsonObject=new JSONObject(data);
                imgUrl=jsonObject.optString("imageUrl");
                showTime=jsonObject.optInt("showTime",3);
                if(StringUtil.isNull(imgUrl))
                {
                    return;
                }
                File dir=new File(Constants.STORE_IMG_PATH);
                    if (!dir.exists()) {
                        if (!dir.mkdirs()) {
                            return;
                        }
                    }
                    File imgFileapkFile = new File(dir, imgUrl.substring(imgUrl.lastIndexOf("/")+1));
                    if(!imgFileapkFile.exists()){
                        callBack.onCall(false);
                        HttpURLConnection connn = (HttpURLConnection) new URL(imgUrl).openConnection();
                        InputStream input = connn.getInputStream();
                        byte[] buf = new byte[1024];
                        out = new ByteArrayOutputStream();
                        int count = -1;
                        while ((count = input.read(buf)) != -1) {
                            out.write(buf, 0, count);
                        }
                        byte[] rs = out.toByteArray();
                        connn.disconnect();
                        fos = new FileOutputStream(imgFileapkFile);
                        fos.write(rs);
                        fos.close();
                        out.close();
                    }else
                    {
                        FileInputStream fis = new FileInputStream(imgFileapkFile);
                        bitmap=BitmapFactory.decodeStream(fis);
                        fis.close();
                        isCall=true;
                        callBack.onCall(true);
                    }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                try {
                    if(fos!=null)
                    {
                        fos.close();
                    }
                    if(out!=null)
                    {
                        out.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
