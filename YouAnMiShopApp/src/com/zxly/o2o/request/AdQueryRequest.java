package com.zxly.o2o.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.easemob.easeui.AppException;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dsnx on 2016/8/22.
 */
public class AdQueryRequest extends BaseRequest{
    private int exhibitionTime;//展示时长(秒)
    private String image;//图片地址
    private String linkUrl;//链接地址
    private OutputStream output;
    private Bitmap bitmap=null;
    public int getExhibitionTime() {
        return exhibitionTime;
    }

    public String getImage() {
        return image;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    protected String method() {
        return "/app/ad/query";
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject=new JSONObject(data);
            if(jsonObject.has("ad"))
            {
                JSONObject jsonAd=jsonObject.getJSONObject("ad");
                exhibitionTime=jsonAd.optInt("exhibitionTime",3);
                image=jsonAd.optString("image");
                linkUrl=jsonAd.optString("linkUrl");
                File dir=new File(Constants.STORE_IMG_PATH);
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        return;
                    }
                }
                File imgFileapkFile = new File(dir, image.substring(image.lastIndexOf("/")+1));
                if(!imgFileapkFile.exists()){
                    HttpURLConnection connn = (HttpURLConnection) new URL(image).openConnection();
                    InputStream input = connn.getInputStream();
                    byte[] buf = new byte[1024];
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    int count = -1;
                    while ((count = input.read(buf)) != -1) {
                        out.write(buf, 0, count);
                    }
                    byte[] rs = out.toByteArray();
                    connn.disconnect();
                    FileOutputStream fos = new FileOutputStream(imgFileapkFile);
                    fos.write(rs);
                    fos.close();
                }
                FileInputStream fis = new FileInputStream(imgFileapkFile);
                bitmap=BitmapFactory.decodeStream(fis);
                fis.close();

            }
        } catch (JSONException e) {
            throw JSONException(e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(output!=null)
                {
                    output.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
