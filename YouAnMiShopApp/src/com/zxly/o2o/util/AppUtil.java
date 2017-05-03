package com.zxly.o2o.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.zxly.o2o.application.AppController;

import java.io.File;
import java.util.List;

public class AppUtil {
	private static final String TAG = "AppUtil";

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
					AppController.getInstance().startActivity(intent);
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

	/**
	 * 获取appname
	 * @param context
	 * @return
     */
	public static String getAppName(Context context){
		PackageManager packageManager = context.getPackageManager();
		return (String) packageManager.getApplicationLabel(context.getApplicationInfo());
	}

	/**
	 * 判断应用是否已经启动
	 * @param context 一个context
	 * @param packageName 要判断应用的包名
	 * @return boolean
	 */
	public static boolean isAppAlive(Context context, String packageName){
		ActivityManager activityManager =
				(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processInfos
				= activityManager.getRunningAppProcesses();
		for(int i = 0; i < processInfos.size(); i++){
			if(processInfos.get(i).processName.equals(packageName)){

				return true;
			}
		}
		return false;
	}
	public static boolean isDebuggable(Context ctx) {
		boolean debuggable = false;
		PackageManager pm = ctx.getPackageManager();
		try {
			ApplicationInfo appinfo = pm.getApplicationInfo(ctx.getPackageName(), 0);
			debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
		}
		catch(NameNotFoundException e) {
		}

		return debuggable;
	}
	
}
