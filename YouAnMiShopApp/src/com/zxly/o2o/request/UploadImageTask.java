/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zxly.o2o.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;

import com.easemob.util.EMLog;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.fragment.UserDefinedSendFragment;
import com.zxly.o2o.util.PicTools;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadImageTask extends AsyncTask<List<Object>, Integer, Map<String, File>> {
    public boolean downloadThumbnail = false;
    String requestUrl;
    Handler handler;
    LoadingDialog loadingDialog;

    Map<String, Object> sendParams;

    public UploadImageTask(String requestUrl,
                           Handler handler, Map<String, Object> sendParams) {
        loadingDialog=new LoadingDialog();
        loadingDialog.show();
        this.sendParams = sendParams;
        this.requestUrl = requestUrl;
        this.handler = handler;
    }

    @Override
    protected Map<String, File> doInBackground(List<Object>... params) {
        Map<String, File> files = new HashMap<String, File>();
        for (int i = 0; i < params[0].size() - 1; i++) {
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            String path = (String) params[0].get(i);
            UserDefinedSendFragment.photoBitmap = BitmapFactory.decodeFile(path, bmpFactoryOptions);
            bmpFactoryOptions.inJustDecodeBounds = false;
            long outHeight = bmpFactoryOptions.outHeight;
            while (outHeight > 4000) {
                bmpFactoryOptions.inSampleSize += 2;  //图片大小为原来的1/2倍
                outHeight = outHeight / 2;
            }
            UserDefinedSendFragment.photoBitmap = BitmapFactory.decodeFile(path, bmpFactoryOptions);
            File file = PicTools.savePicAndReturn(false, String.valueOf(i));
            files.put("file" + i, file);
        }


        //用完后回收
        if (UserDefinedSendFragment.photoBitmap != null) {
            UserDefinedSendFragment.photoBitmap.recycle();
            UserDefinedSendFragment.photoBitmap = null;
        }


        return files;
    }

    @Override
    protected void onPostExecute(Map<String, File> result) {
        loadingDialog.dismiss();
        new FileUploadRequest(result, sendParams,
                requestUrl, handler).startUpload();
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }


    public interface DownloadFileCallback {
        void beforeDownload();

        void downloadProgress(int progress);

        void afterDownload(Bitmap bitmap);
    }


    public static String getThumbnailImagePath(String imagePath) {
        String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
        path += "th" + imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.length());
        EMLog.d("msg", "original image path:" + imagePath);
        EMLog.d("msg", "thum image path:" + path);
        return path;
    }
}
