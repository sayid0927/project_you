package com.zxly.o2o.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by dsnx on 2015/7/20.
 */
public class ShareHelper {

    private final String SHARE_APP_NAME="YouAnMiOpen.apk";
    Context mContext = null;

    public ShareHelper(Context context) {
        this.mContext = context;
    }


    public boolean isMobile_spExist() {
        PackageManager manager = mContext.getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = pkgList.get(i);
            if (pI.packageName.equalsIgnoreCase("com.yam.open"))
                return true;
        }
        return false;
    }

    public boolean detectMobile_sp() {
        boolean isMobile_spExist = isMobile_spExist();
        if (!isMobile_spExist) {

            File cacheDir = mContext.getCacheDir();
            final String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";

            retrieveApkFromAssets(mContext, SHARE_APP_NAME,cachePath);
            chmod("777", cachePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + cachePath),
                    "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }

        return isMobile_spExist;
    }


    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean retrieveApkFromAssets(Context context, String fileName,
                                         String path) {
        boolean bRet = false;

        try {
            InputStream is = context.getAssets().open(fileName);

            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }

            fos.close();
            is.close();

            bRet = true;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bRet;
    }
}
