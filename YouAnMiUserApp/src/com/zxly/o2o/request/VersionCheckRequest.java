package com.zxly.o2o.request;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easemob.easeui.AppException;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.FileDownLoader;
import com.zxly.o2o.util.MD5Util;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.util.ZipUtil;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author fengrongjian 2015-1-23
 * @description app新版本检测网络请求
 */
public class VersionCheckRequest extends BaseRequest {

    private Handler handler = new Handler();
    public static final int exit_app_code=-111;
    private long cur = 0;
    private int total = 0;
    private File apkFile = null;
    private RandomAccessFile out = null;
    private ParameCallBack callBack;
    private boolean isUpdateApp;
    public boolean isInstallApp=false;
    private Object lock = new Object();

    public VersionCheckRequest(ParameCallBack callBack) {
        addParams("shopId", Config.shopId);
        this.callBack = callBack;

    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jo = new JSONObject(data);
            Config.serverVersionName = jo.getString("versionName");
            Config.serverShopversionNo = jo.getInt("versionNo");
            Config.AppUpdateUrl = jo.getString("url");
            int versionCodeMin = 0;
            if (jo.has("versionCodeMin")) {
                versionCodeMin = jo.getInt("versionCodeMin");
            }

            int versionCodeCur = jo.getInt("versionCodeCur");
            Config.versionCodeCur = versionCodeCur;

            if (versionCodeMin > AppController.mVersionCode) {
                //开始强制升级
                download(Config.AppUpdateUrl);
            } else {
                // 优先级 强制更新>H5样式自动更新>普通手动更新
                //如果不是强制更新就先检查H5样式，比较小100k，app版本更新一般不会那么频繁
                if(jo.has("appStyleColorTemplate")){
                    final JSONObject h5Jo=new JSONObject(jo.getString("appStyleColorTemplate"));
                    //H5版本号大于本地版本号，以及StyleId变化的时候更新H5文件
                    int defaultH5VersionNo=PreferUtil.getInstance().getH5VersionCode();
                    int newH5VersionCode=h5Jo.optInt("versionNo");
                    int defaultStyleId=PreferUtil.getInstance().getH5StyleId();
                    int newStyleId=h5Jo.optInt("id");
                    Log.d("downLoad", "-->"+ " defVo : "+defaultH5VersionNo +"  defSd : "+defaultStyleId + "  newNo : "+newH5VersionCode + "  newSd : " +newStyleId);
                    if(newH5VersionCode>defaultH5VersionNo){
                        Log.d("downLoad", "update start");
                        downLoadH5Project(h5Jo);
                        Log.d("downLoad", "update Finish-->" + " nowVo : " + PreferUtil.getInstance().getH5VersionCode()+ "  nowSd : " + PreferUtil.getInstance().getH5StyleId());
//                        return; //更新完H5数据后跳出普通版本更新检查
                    }
                }


                if (versionCodeCur > AppController.mVersionCode) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            final Dialog dialog = new Dialog(AppController.getInstance().getTopAct(), R.style.dialog);
                            dialog.setContentView(R.layout.dialog_update);
                            dialog.setCancelable(false);
                            TextView title = (TextView) dialog.findViewById(R.id.txt_title);

                            ViewUtils.setText(title, "检测到最新版本V" + Config.serverVersionName + ", 是否更新?");
                            ((Button) dialog.findViewById(R.id.btn_update))
                                    .setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            isUpdateApp = true;
                                            synchronized (lock) {
                                                lock.notify();
                                            }
                                        }
                                    });
                            ((Button) dialog.findViewById(R.id.btn_cancel))
                                    .setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            isUpdateApp = false;
                                            synchronized (lock) {
                                                lock.notify();
                                            }
                                        }
                                    });
                            dialog.show();
                        }
                    });

                    synchronized (lock) {
                        lock.wait();
                    }
                    if (isUpdateApp) {
                        download(Config.AppUpdateUrl);
                    }

                }

            }



        } catch (Exception e) {
//            throw new AppException("数据解析异常");
        }
    }

    public void download(String updateAppUrl) throws AppException {

        handler.post(new Runnable() {

            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onCall(0);
                }
            }
        });
        int retryCount = 1;

        File dir = new File(Environment.getExternalStorageDirectory(), Constants.PARNET_PATH + "/apks");

        if (!dir.exists()) {
            if (!dir.mkdirs()) {// 无法在sd卡上创建，则在手机闪存上创建
                return;
            }
        }
        // 创建下载文件
        apkFile = new File(dir, "yam_user" + Config.serverShopversionNo + ".apk");
        if(apkFile.exists())
        {
            if(apkFile.isFile())
            {
                apkFile.delete();
            }

        }
        cur = apkFile.length();
        while (retryCount < 4) {
            try {
                HttpURLConnection connn = (HttpURLConnection) new URL(updateAppUrl).openConnection();
                int tempSize = connn.getContentLength();
                connn.disconnect();

                total = tempSize;
                if (total <= 0)
                    throw new IOException("content length error");
                if (cur < total) {
                    HttpURLConnection conn = (HttpURLConnection) new URL(updateAppUrl)
                            .openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("RANGE", "bytes=" + cur + "-");

                    InputStream in = conn.getInputStream();
                    out = new RandomAccessFile(apkFile, "rw");
                    out.seek(cur);
                    byte[] buf = new byte[4096];
                    int count;
                    int time = 0;

                    while ((count = in.read(buf)) != -1) {
                        out.write(buf, 0, count);
                        cur += count;
                        time++;
                        if (time % 25 == 0) {

                            notice(TYPE_APP_UPDATE);
                        }
                    }
                    out.setLength(total);
                    out.close();
                    cur = total;
                    conn.disconnect();
                }
                notice(TYPE_APP_UPDATE);
                handler.post(new Install());
                code=exit_app_code;
                throw  new AppException(null);//故意抛一个异常退出应用只保留安装界面,防止安装界面被覆盖
            } catch (IOException e) {

                // 先保存文件
                try {
                    if (out != null)
                        out.close();
                } catch (IOException e1) {
                }
                if (retryCount == 5) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ViewUtils.showToast("更新版本失败！请检查网络是否正常");
                        }
                    });
                } else {
                    try {
                        Thread.sleep(retryCount * 1000);
                    } catch (InterruptedException ie) {
                    }
                }
                retryCount++;

            }
        }


    }


    private int retryCount=0;
    private void downLoadH5Project(final JSONObject h5Jo){
        notice(TYPE_H5_UPDATE);
        final String fileMd5=h5Jo.optString("md5","");
        String downLoadUrl=h5Jo.optString("url","");
        final File downLoadFile = new File(Environment.getExternalStorageDirectory(), Constants.PARNET_PATH+"/h5_"+ TimeUtil.getDetailTime(System.currentTimeMillis())+".zip");
        new FileDownLoader().downLoadFile(downLoadUrl, downLoadFile, new CallBack() {
            @Override
            public void onCall() {
                if (!fileMd5.equalsIgnoreCase(MD5Util.getFileMD5(downLoadFile))){
                    if(retryCount==0){
                        retryCount=1;
                        downLoadH5Project(h5Jo);
                    }
                }else {
                    try {
                        File file=new File(Constants.H5_PROJECT_PATH);
                        if(file.exists()&&file.canWrite())
                            FileUtils.deleteDirectory(file);

                        try {
                            ZipUtil.UnZipFolder(downLoadFile.getAbsolutePath(),Constants.H5_PROJECT_PATH);
                            PreferUtil.getInstance().setH5StyleId(h5Jo.optInt("id"));
                            PreferUtil.getInstance().setH5VersionCode(h5Jo.optInt("versionNo"));
                        }catch (Exception e){

                        }


                    } catch (Exception e) {
                        Log.d("downLoad","update finish error --->"+e.toString());
                        e.printStackTrace();
                    }finally {
                        //先不做删除，稳定之后优化
//                                if(downLoadFile==null)
//                                    downLoadFile.delete();
                    }
                }

            }
        });



    }


    public static final int TYPE_APP_UPDATE=1;
    public static final int TYPE_H5_UPDATE=2;

    private void notice(final int updateType) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    if(updateType==TYPE_APP_UPDATE)
                    callBack.onCall((cur * 100 / total));
                }else if(updateType==TYPE_H5_UPDATE){
                    callBack.onCall("正在更新数据,请稍候...");
                }
            }
        });
    }



    private class Install implements Runnable {

        @Override
        public void run() {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile),
                    "application/vnd.android.package-archive");
            AppController.getInstance().startActivity(intent);
        }

    }

    @Override
    protected String method() {
        return "app/version";
    }


}
