package com.zxly.o2o.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.zxly.o2o.application.AppController;
import com.zxly.o2o.fragment.UserDefinedSendFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PicTools {

    /**
     * 压缩图片质量
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 95;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对图片的比例按2的倍数进行压缩
     */
    public static Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        float ww = 400f;
        int be = (int) (newOpts.outWidth / ww);
        if (be <= 0)
            be = 2;
        newOpts.inSampleSize = be;// 设置采样率
        newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    public static void convertByBitmapOptions(String srcPath, String decPath) {
        Bitmap bmp = compressImageFromFile(srcPath);
        compressBmpToFile(bmp, new File(decPath));
    }

    /**
     * 通过系统提供的 ThumbnailUtils经行图片压缩
     */
    public static void coverByThumbnailUtils(String srcPath, String decPath) {

        try {
            Bitmap bmp = BitmapFactory.decodeFile(srcPath);
            int orgWith = bmp.getWidth();
            int height = bmp.getHeight();
            int newHeight = height * 720 / orgWith;
            Bitmap newBmp = ThumbnailUtils
                    .extractThumbnail(bmp, 720, newHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

            File myCaptureFile = new File(decPath);
            FileOutputStream out = new FileOutputStream(myCaptureFile);
            if (newBmp.compress(Bitmap.CompressFormat.JPEG, 95, out)) {
                out.flush();
                out.close();
            }
            if (!bmp.isRecycled()) {
                bmp.recycle();
            }
            if (!newBmp.isRecycled()) {
                newBmp.recycle();
            }
        } catch (Exception e) {
            Log.d("fileInfo", " coverByTh 转换出错");
            e.printStackTrace();
        }
    }

    /**
     * 通过Matrix scale 来调整图片大小
     */
    public static void convertImageByBitmapMatrix(String fromFile, String toFile, int width, int quality) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            // 缩放图片的尺寸
            float scale = (float) width / bitmapWidth;

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            // 产生缩放后的Bitmap对象
            Bitmap resizeBitmap = Bitmap
                    .createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            // save file
            File myCaptureFile = new File(toFile);
            FileOutputStream out = new FileOutputStream(myCaptureFile);
            if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (!resizeBitmap.isRecycled()) {
                resizeBitmap.recycle();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 把一个目录下的所有 图片压缩到指定的目录
     */
    public static void convertImageFromDirToDir(String org, String dsc) {

        try {
            File orgFile = new File(org);
            File dscFile = new File(dsc);
            File[] fileList = orgFile.listFiles();

            long startConvert = System.currentTimeMillis();
            for (int i = 0; i < fileList.length; i++) {
                // Log.d("fileInfo", "file :"+fileList[i].getAbsolutePath()
                // +"new file  path=="+dscFile.getAbsolutePath()+File.separator+fileList[i].getName());
                // transImage(fileList[i].getAbsolutePath(),
                // dscFile.getAbsolutePath()+File.separator+fileList[i].getName(),720,
                // 90);
                // coverByTh(fileList[i].getAbsolutePath(),
                // dscFile.getAbsolutePath()+File.separator+fileList[i].getName());
                convertByBitmapOptions(fileList[i].getAbsolutePath(), dscFile.getAbsolutePath() + File.separator + fileList[i].getName());
                Log.d("fileInfo", "file ：" + i + "转换完成");
            }
            Log.d("fileInfo", "耗时：" + (System.currentTimeMillis() - startConvert));
        } catch (Exception e) {
            Log.d("fileInfo", "file error");
            e.printStackTrace();
        }
    }

    public static File getFile(String packageName, String fileName) {
        File f = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + File.separator + packageName + File.separator + fileName.substring((2 * fileName.length()) / 3));
        if (f.exists()) {
            return f;
        } else {
            return null;
        }
    }

    // by-benjamin 保存路径
    public static File getOutputPhotoFile(boolean isNeedTimeStamp, String fileName) {

        File directory = getOutputDirectory();

        if (isNeedTimeStamp) {
            String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());

            return new File(directory.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return new File(directory.getPath() + File.separator + fileName.substring((2 * fileName.length()) / 3));
        }
    }

    public static File getOutputPhotoFile() {
        File directory = getOutputDirectory();
        return new File(directory.getPath() + File.separator + "img_temp" + ".jpg");
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File getOutputDirectory() {
        File directory = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppController
                .getInstance().getPackageName());

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e("保存失败", "创建保存路径失败");
                return null;
            }
        }
        return directory;
    }

    public static void cleanPic(File file) {
        if (file != null) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                // if (childFiles == null || childFiles.length == 0) {
                // file.delete();
                // return;
                // }
                if (childFiles.length > 100) {
                    for (File childFile : childFiles) {
                        childFile.delete();
                    }
                }
                // file.delete();
            }
        }
    }

    //	Images.Media.BUCKET_ID是相册的ID，我是用sql语句进行一些分组处理，然后获取相册列表信息的，包括相册封面路径、相册名称等信息
    //	代码如下：
    public static ArrayList<String> queryGallery(Activity context) {
        ArrayList<String> galleryList = new ArrayList<String>();
        Cursor mCursor = getCursorfromQuery(context);
        if (mCursor.moveToLast()) {
            do {
                //获取图片的路径
                String path = mCursor.getString(0);
                galleryList.add(path);
            } while (mCursor.moveToPrevious());
        }
        mCursor.close();

        return galleryList;
    }

    public static Cursor getCursorfromQuery(Activity context) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        String[] columns = new String[]{MediaStore.Images.Media.DATA};

        //只查询jpeg的图片
        return mContentResolver.query(mImageUri, columns, MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg"}, MediaStore.Images.Media.DATE_TAKEN);

    }

    // 保存到sdcard
    public static File savePicAndReturn(boolean isNeedTimeStamp, String fileName) {
        File file = PicTools.getOutputPhotoFile(isNeedTimeStamp, fileName);
        if (file != null) {
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file.getPath());
                if (UserDefinedSendFragment.photoBitmap != null) {
                    UserDefinedSendFragment.photoBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
                    fos.flush();
                    fos.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return file;
        }
        return null;
    }

}
