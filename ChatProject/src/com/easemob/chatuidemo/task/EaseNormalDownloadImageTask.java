package com.easemob.chatuidemo.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/1/12.
 */
public class EaseNormalDownloadImageTask extends AsyncTask<String, Integer, Bitmap> {

    private DownloadFileCallback callback;

    public EaseNormalDownloadImageTask(DownloadFileCallback callback){
        this.callback = callback;
    }

    //onPreExecute方法用于在执行后台任务前做一些UI操作
    @Override
    protected void onPreExecute() {
    }

    //doInBackground方法内部执行后台任务,不可在此方法内修改UI
    @Override
    protected Bitmap doInBackground(String... params) {
        URL myFileUrl;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //onProgressUpdate方法用于更新进度信息
    @Override
    protected void onProgressUpdate(Integer... progresses) {
    }

    //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
    @Override
    protected void onPostExecute(Bitmap result) {
        callback.afterDownload(result);
    }

    //onCancelled方法用于在取消执行中的任务时更改UI
    @Override
    protected void onCancelled() {
    }

    public interface DownloadFileCallback{
        void afterDownload(Bitmap bitmap);
    }
}
