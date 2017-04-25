package com.zxly.o2o.util;

import android.os.Environment;
import android.util.Log;

import com.easemob.easeui.AppException;

import org.apache.commons.io.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kenwu on 2016/1/29.
 */
public class FileDownLoader {


    /**
     * 用于下载小文件，暂时不做重连，后续优化
     */
    public void downLoadFile(String downLoadUrl,File destFile, CallBack callBack) {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream=null;
        try {
            Log.d("downLoad","start");

         httpURLConnection = (HttpURLConnection) new URL(downLoadUrl).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            Log.d("downLoad", "start-->" + responseCode);
            if (responseCode < 200 || responseCode >= 300) {
                throw new AppException("response error !!!");
            }

            if(destFile==null){
                throw new AppException("h5 file null !!!");
            }

            File parentFile = destFile.getParentFile();
            if(parentFile != null && !parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination \'" + parentFile + "\' directory cannot be created");
            } else if(destFile.exists() && !destFile.canWrite()) {
                throw new IOException("Destination \'" + destFile + "\' exists but is read-only");
            }

            Log.d("downLoad","start-->"+destFile.getAbsolutePath());
            fileOutputStream=new FileOutputStream(destFile);
            inputStream = httpURLConnection.getInputStream();
            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                Log.d("downLoad", "start ...");
            }

            if(callBack!=null)
                callBack.onCall();
        } catch (Exception e) {
            Log.d("downLoad", "start error"+e.toString());
           // throw new AppException("downLoad h5 error !!!");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (Exception e) {

            }
        }
    }


}
