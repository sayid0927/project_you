package com.shyz.downloadutil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.List;

public class AppUtil {
	private static final String TAG = "AppUtil";
	
	/**
	 * 根据一个完成的下载任务来安装apk
	 * @param context
	 * @param taskinfo
	 */
	public static void installApk(Context context,DownLoadTaskInfo taskinfo){
		File file = new File(taskinfo.getFileSavePath());
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
	}
	
	/**
	 * 根据包名启动一个apk应用 
	 * @param context
	 * @param packageName
	 */
	public static void startApk(Context context,String packageName){
		Log.e("", "打开apk---》");
		if(TextUtils.isEmpty(packageName)){
			Log.d("TAG", "包名不存在");
			return;
		}
		PackageManager pm =context.getPackageManager();
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageInfo pi;
		List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
		if (apps.size() == 0) {
			try {
				pi = pm.getPackageInfo(packageName, 0);
				resolveIntent.setPackage(pi.packageName);
				apps = pm.queryIntentActivities(resolveIntent, 0);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}
		if (apps.size() != 0) {
			ResolveInfo ri = apps.iterator().next();
			if (ri != null) {
				try {
					String className = ri.activityInfo.name;
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					ComponentName cn = new ComponentName(packageName, className);
					intent.setComponent(cn);
					BaseApplication.app.startActivity(intent);
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
			}
		}
	}
	
	 /**
		 * 启动应用
		 * 
		 * @param object  ApkInfo AppDetailInfo DownLoadTaskInfo 三种类型
		 * @return 
		 */
		public static boolean startApk2(Context context,String packageName) {
			
		
		
			//Context context = BaseApplication.getInstance();
			PackageManager pm =context.getPackageManager();
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			PackageInfo pi;
			try {
				pi = pm.getPackageInfo(packageName, 0);
				resolveIntent.setPackage(pi.packageName);
			} catch (Exception e) {
			
			}
			List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
			if (apps.size() == 0) {
				try {
					pi = pm.getPackageInfo(packageName, 0);
					resolveIntent.setPackage(pi.packageName);
					apps = pm.queryIntentActivities(resolveIntent, 0);
				} catch (Exception e1) {
					
				}
			}
			if (apps.size() != 0) {
				ResolveInfo ri = apps.iterator().next();
				if (ri != null) {
					try {
						
						String className = ri.activityInfo.name;
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.addCategory(Intent.CATEGORY_LAUNCHER);
						ComponentName cn = new ComponentName(packageName, className);
						intent.setComponent(cn);
						context.startActivity(intent);

						
						return true;
					} catch (Exception e) {

					}
				}
			}
			return false;
		}
	
	/**
	 * 根据包名取出安装的apk版本名称versionname
	 * @param context
	 * @param packageName
	 * @return 如果存在安装，则返回版本号，否则返回-1
	 */
	public static int getInstalledAPkVersion(Context context,String packageName){
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		}catch (NameNotFoundException e) {
			packageInfo = null;
			Log.e(TAG, "未安装此应用");
		}
		if(packageInfo !=null){
			return packageInfo.versionCode;
		}else{
			return -1;
		}
	}
	
}
