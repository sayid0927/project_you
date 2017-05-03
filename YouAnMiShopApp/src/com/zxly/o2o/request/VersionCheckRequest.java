package com.zxly.o2o.request;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.easemob.easeui.AppException;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author fengrongjian 2015-7-14
 * @description app新版本检测网络请求
 */
public class VersionCheckRequest extends BaseRequest {


    private Handler handler = new Handler();
    private long cur = 0;
    private int total = 0;
    private File apkFile = null;
    private RandomAccessFile out = null;
    private ParameCallBack callBack;
    private boolean isUpdateApp;
    //是否强制升级
    private boolean isMustUpdate;

    public boolean isInstallApp=false;

    private Object lock = new Object();
    private Object mustLock = new Object();

    public VersionCheckRequest(ParameCallBack callBack) {
        this.callBack = callBack;

    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jo = new JSONObject(data);
            Config.serVerName = jo.getString("serVerName");
            Config.serVerCode = jo.getInt("serVerCode");
            Config.appUpdateUrl = jo.getString("url");
            int versionCodeMin = 0;
            if (jo.has("versionCodeMin")) {
                versionCodeMin = jo.getInt("versionCodeMin");
            }
            if (versionCodeMin > AppController.mVersionCode) {
                //开始强制升级
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        final Dialog updateDialog = new Dialog(AppController.getInstance().getTopAct(), R.style.Transparent);
                        if (updateDialog.isShowing()) {
                            updateDialog.dismiss();
                        }
                        if(!AppController.getInstance().getTopAct().isFinishing()){
                            updateDialog.show();
                        }
                        updateDialog.setContentView(R.layout.activity_findnewapp);
                        updateDialog.setCancelable(false);
                        updateDialog.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO 保存组 返回前一页
                                    updateDialog.dismiss();
                                    isMustUpdate=true;
                                    synchronized (mustLock) {
                                        mustLock.notify();
                                    }

                            }
                        });
                    }
                });
                synchronized (mustLock) {
                    mustLock.wait();
                }
                if(isMustUpdate){
                    download(Config.appUpdateUrl);
                }

//                download(Config.appUpdateUrl);
            } else {
                    if (Config.serVerCode > AppController.mVersionCode) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog dialog =   new Dialog(AppController.getInstance().getTopAct(), R.style.dialog);
//                                dialog.setContentView(R.layout.dialog_update);
                                dialog.setContentView(R.layout.dialog_hasnewversion);
                                dialog.setCancelable(false);
//                                TextView title = (TextView) dialog.findViewById(R.id.txt_title);
//
//                                ViewUtils.setText(title, "检测到最新版本V" + Config.serVerName + ", 是否更新?");
                                ( dialog.findViewById(R.id.btn_update))
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
                                ( dialog.findViewById(R.id.btn_cancel))
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
                            download(Config.appUpdateUrl);
                        }

                    } else if(Config.serVerCode<AppController.mVersionCode) {
                        Log.e("VersionCheckRequest","服务器版本配置错误");
                    }

            }

        } catch (Exception e) {
            throw new AppException("数据解析异常");
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
        File dir = new File(Environment.getExternalStorageDirectory(),Constants.PARNET_PATH+"/apks");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
               return;
            }
        }
        // 创建下载文件
        apkFile = new File(dir, "yam_shop"+ Config.serVerCode+".apk");
        cur=apkFile.length();
        while (retryCount < 6) {
            try {
                HttpURLConnection connn = (HttpURLConnection) new URL(updateAppUrl).openConnection();
                int tempSize = connn.getContentLength();
                connn.disconnect();

                total = tempSize;
                if (total <= 0)
                    throw new IOException("content length error");
                if(cur<total)
                {
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
                            notice();
                        }
                    }
                    out.setLength(total);
                    out.close();
                    cur = total;
                    conn.disconnect();
                }
                notice();
                isInstallApp=true;
                handler.post(new Install());
                break;
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
                }else
                {
                    try {
                        Thread.sleep(retryCount * 1000);
                    } catch (InterruptedException ie) {
                    }
                }

                retryCount++;
            }
        }


    }

    private void notice() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onCall((cur * 100 / total));
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
        return "shopApp/version";
    }


}
