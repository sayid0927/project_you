/**
 * Copyright(C)2012-2013 深圳市掌星立意科技有限公司版权所有
 * 创 建 人:Jacky
 * 修 改 人:
 * 创 建日期:2013-7-25
 * 描    述:SD卡工具类
 * 版 本 号:
 */

package com.zxly.o2o.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class FileUtils {

	/** 获取图片路径
	 * 
	 * @param context
	 * @return */
	public static String getImageDownloadDir(Context context) {
		String filePath = "";
		if (FileUtils.hasSDCard()) {
			// 有SD卡
			filePath = Constants.STORE_IMG_PATH;
		} else {
			// 没有SD卡
			String path = context.getFilesDir().getPath();
			filePath = path + File.separator + "zxly_appstore/image" + File.separator;
		}
		return filePath;
	}
	public static String getCacheDownloadDir(){
		if(FileUtils.hasSDCard()){
			return Constants.STORE_CACHE_PATH;
		}
		return null;
	}

	/** 并存入本地SD卡
	 * 
	 * @param url
	 *            bitmap
	 * @return */
	public static void saveToSDCard(Bitmap bm, String url) {

		String downFilePath = Constants.STORE_IMG_PATH;
		saveBitamp(bm, url, downFilePath);

	}

	/** 并存入本地手机内存
	 * 
	 * @param url
	 *            bitmap
	 * @return */
	public static void saveToMobile(Bitmap bm, String url, Context context) {

		String path = context.getFilesDir().getPath();
		String filePath = path + File.separator + "zxly_appstore/image" + File.separator;
		saveBitamp(bm, url, filePath);

	}

	/** 保存Bitamp图片
	 * 
	 * @param url
	 *            bitmap
	 * @return */
	private static void saveBitamp(Bitmap bm, String url, String downFilePath) {
		try {

			File dirFile = new File(downFilePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File file = new File(downFilePath + url.substring(url.lastIndexOf("/") + 1));
			if (file.exists()) {
				return;
			}
			FileOutputStream fos = new FileOutputStream(file);
			if (url.endsWith(".png")) {
				bm.compress(CompressFormat.PNG, 100, fos);
			} else {
				bm.compress(CompressFormat.JPEG, 100, fos);
			}
			// fos.write(out.toByteArray(), 0, out.toByteArray().length);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			AppLog.p(e);
		}
	}

	/** 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException */
	public static File createSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

	/** 从服务器上获取图片
	 * 
	 * @param path
	 * @return
	 * @throws Exception */
	public static byte[] downloadImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	/** 是否有sd卡
	 * 
	 * @param context
	 * @return */
	public static boolean hasSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	
	/** 把InputStream转成byte
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception */
	private static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/** 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return */
	public static File createSDDir(String dirName) {
		File dir = new File(dirName);
		if(!dir.exists()){
		    dir.mkdirs();
		}
		return dir;
	}

	/** 判断SD卡上的文件夹是否存在
	 * 
	 * @param fileName
	 * @return boolean */
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/** 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @param len
	 * @return */
	public static File write2SDFromInput(String path, String fileName, InputStream input, int len) {
		File file = null;
		OutputStream outputStream = null;
		createSDDir(path);
		try {
			file = createSDFile(path + fileName);
			outputStream = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			while ((temp = input.read(buffer)) != -1) {
				outputStream.write(buffer, 0, temp);
			}
			outputStream.flush();
		} catch (IOException e) {
			AppLog.p(e);
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) {
				AppLog.p(e);
			}
		}
		return file;
	}

	public static String getimei(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String ret = tm.getDeviceId();
		AppLog.i("info", "imei = " + ret);
		return ret;
	}

	public static String getImsi(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String ret = tm.getSubscriberId();
		AppLog.i("info", "imsi = " + ret);
		if (ret == null) {
			return "(null)";
		}
		return ret;
	}

	public static boolean deleteDir(String path) {
		boolean success = true;
		File file = new File(path);
		if (file.exists()) {
			File[] list = file.listFiles();
			if (list != null) {
				int len = list.length;
				for (int i = 0; i < len; ++i) {
					if (list[i].isDirectory()) {
						deleteDir(list[i].getPath());
					} else {
						boolean ret = list[i].delete();
						if (!ret) {
							success = false;
						}
					}
				}
			}
		} else {
			success = false;
		}
		if (success) {
			file.delete();
		}
		return success;
	}
	
	/**
	 * @return
	 */
//	public static String getOperator(Activity act) {
//		TelephonyManager telManager = (TelephonyManager) act
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		String imsi = telManager.getSubscriberId();
//		if (imsi != null) {
//			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
//				return "中国移动";
//			} else if (imsi.startsWith("46001")) {
//				return "中国联通";
//
//			} else if (imsi.startsWith("46003")) {
//				return "中国电信";
//			}
//		}
//		return "未插手机卡";
//
//	}
//	public static String getLocalIpAddress() {
//	    try {
//	        for (Enumeration<NetworkInterface> en = NetworkInterface
//	                .getNetworkInterfaces(); en.hasMoreElements();) {
//	            NetworkInterface intf = en.nextElement();
//	            for (Enumeration<InetAddress> enumIpAddr = intf
//	                    .getInetAddresses(); enumIpAddr.hasMoreElements();) {
//	                InetAddress inetAddress = enumIpAddr.nextElement();
//	                if (!inetAddress.isLoopbackAddress()) {
//	                    return inetAddress.getHostAddress().toString();
//	                }
//	            }
//	        }
//	    } catch (SocketException ex) {
//	        Log.e("ifo", ex.toString());
//	    }
//	    return "";
//	 }
    
    public static String getCurrentTime() {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt = new Date(System.currentTimeMillis());  
        String sDateTime = sdf.format(dt);  
        return sDateTime;
    }
    
    public static String getCurrentDate() {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date(System.currentTimeMillis());  
        String sDateTime = sdf.format(dt);  
        return sDateTime;
    }
    
    /**
     * 从SD卡获取缓存
     */
    public static String restoreCache(File file) {
    	String result = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    	int len = 0;
	    	byte[] data = new byte[1024];
	    	while((len = fileInputStream.read(data)) != -1) {        
	    		byteArrayOutputStream.write(data, 0, len);
	    	}
	    	if(fileInputStream != null) {                        
	    		fileInputStream.close();
	        }         
	    	result =  new String(byteArrayOutputStream.toByteArray());
		} catch (Exception e) {
			AppLog.p(e);
		}
    	return result;
    }
    
    /**
     * 缓存至SD卡
     */
    public static void saveCache(File file, String result) {
    	try {     
    		OutputStream os = new FileOutputStream(file);     
    		os.write(result.getBytes());     
    		os.close();     
    	} catch (Exception e) {     
    		AppLog.p(e);
    	}
    }
    
}
