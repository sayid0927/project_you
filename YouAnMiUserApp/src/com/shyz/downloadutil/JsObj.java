package com.shyz.downloadutil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.lidroid.xutils.exception.DbException;

import java.io.File;


/**
 * JsObj
 * 
 * @description:被JS调用的
 * @author:huangbqian
 * @date:2015-1-9 下午15:50:01
 */
public class JsObj {
	private DownloadManager downloadManager;
	private Context context;
	private static final int ADD_TASK = 22;
	private static final int STOP_TASK = 23;
	private static final int RESUME_TASK = 24;
	private static final int SHOW_TITLEBAR = 25;
	private static final int HIDE_TITLEBAR = 26;
	public static String url;
	public JsObj(Context context){
		this.context = context;
		downloadManager = DownloadManager.createDownloadManager(context);
	}	
	
	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			DownLoadTaskInfo info;
			switch (msg.what) {
			case ADD_TASK:
				//Log.e("", "ADD_TASK--->");
				info = (DownLoadTaskInfo) msg.obj;
				try {
					downloadManager.addNewDownload(info.getDownloadUrl(), info.getFileName(), info.getPackageName(),info.getIconUrl(),info.getVersionCode(),info.getFileSizeOfMb(),null);
				} catch (DbException e) {
					Log.e("", "DbException--->"+e.toString());
					e.printStackTrace();
				}
				break;
			case STOP_TASK:
				//Log.e("", "STOP_TASK--->");
				 info = (DownLoadTaskInfo) msg.obj;
				 DownLoadTaskInfo taskInfo1 = downloadManager.getTask(info.getPackageName());
					try {
						downloadManager.stopDownload(taskInfo1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				break;
			case RESUME_TASK:
				//Log.e("", "RESUME_TASK--->");
			    info = (DownLoadTaskInfo) msg.obj;
				DownLoadTaskInfo taskInfo = downloadManager.getTask(info.getPackageName());
				try {
					downloadManager.resumeDownload(taskInfo,null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case SHOW_TITLEBAR:
			//	((DownLoadListAct)context).showOrHideTitlebar(true);
				((DownLoadListAct)context).fragmentContorler.showTab(0);
				break;
			case HIDE_TITLEBAR:
				((DownLoadListAct)context).fragmentContorler.showTab(1);
			//	((DownLoadListAct)context).showOrHideTitlebar(false);
				break;
			}
		};
	};
	
	
	/**
	 * 获取当前应用在列表中的状态
	 * @param packagename 包名
	 * @param versionCode 版本号
	 * @return 
	 */
	@JavascriptInterface
	public String getState(String packagename,int versionCode){
		DownLoadTaskInfo taskInfo = downloadManager.getTask(packagename);
		if(taskInfo !=null){
            switch (taskInfo.getState()) {
                case WAITING:
                	return "waiting";//等待下载
                case STARTED:
                	 
                case LOADING:
                	return "downloading";//下载中
                case CANCELLED:
                	return "resume";//下载已取消,状态显示继续
                case SUCCESS:
                	int installedCode = AppUtil.getInstalledAPkVersion(context, taskInfo.getPackageName());
                	//Log.e("", "已经安装的版本名-->"+installedCode);
                	//Log.e("", "taskInfo.getVersionName()--->"+taskInfo.getVersionName()+"列表版本-->"+versionCode);
                	if(installedCode!=-1&&taskInfo.getVersionCode()!=versionCode){//有安装版本，并且下载版本和列表中显示的版本不同时
                		if(taskInfo.getVersionCode()<versionCode||installedCode<versionCode){//下载的版本比列表中显示的版本要低，则下载的无效，提示更新，重新下载
                			//Log.e("", "可以升级-->");
                			return "upgrade";//升级
                		}else{
                			//Log.e("", "可以打开-->");
                			return "open";//打开
                		}
                		
                	}else{
	                	File file = new File(taskInfo.getFileSavePath());
	                	if(file.exists()){
	                		return "install";//安装
	                	}else{//下载的文件不存在，则重新下载
	                		try {
								downloadManager.removeDownload(taskInfo);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                		return "download";//下载
	                	}
                	}
                 
                case FAILURE:
                	return "failue"; //失败  
            }
		}else{
			int installedVercode = AppUtil.getInstalledAPkVersion(context,packagename);
    		if(installedVercode!=-1){//表示已经安装
    			if(installedVercode<versionCode){//有更新的版本
    				return "upgrade";//升级
    			}else{
    				return "open";//打开
    			}
    		}else{//表示未安装也未在下载任务中
    			return "download";
    		}
		}
		return null;
		
	}
	
	/**
	 * 
	 * @param downlaodUrl 下载地址
	 * @param fileName 应用名称
	 * @param packageName 包名
	 * @param versionCode 版本号
	 * @param sizeMB 文件大小 ，必须是MB单位
	 */
	@JavascriptInterface
	public void toDownLoad(String downlaodUrl, String fileName,String packageName,String iconUrl,int versionCode,String classCode,String source,float size){
		DownLoadTaskInfo taskInfo = new DownLoadTaskInfo();
		taskInfo.setDownloadUrl(downlaodUrl);
		taskInfo.setFileName(fileName);
		taskInfo.setPackageName(packageName);
		taskInfo.setIconUrl(iconUrl);
		taskInfo.setVersionCode(versionCode);
		taskInfo.setFileSizeOfMb(size);
		Message msgMessage = myHandler.obtainMessage();
		msgMessage.what = ADD_TASK;
		msgMessage.obj = taskInfo;
		myHandler.sendMessage(msgMessage);
//		Log.e("", "toDownLoad--->"+downlaodUrl);
//		try {
//			downloadManager.addNewDownload(downlaodUrl, fileName, packageName,iconUrl,versionCode,size,null);
//		} catch (DbException e) {
//			Log.e("", "DbException--->"+e.toString());
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 暂停下载
	 * @param packageName 应用包名
	 */
	@JavascriptInterface
	public void toStop(String packageName){
		DownLoadTaskInfo taskInfo = new DownLoadTaskInfo();
		taskInfo.setPackageName(packageName);
		Message msgMessage = myHandler.obtainMessage();
		msgMessage.what = STOP_TASK;
		msgMessage.obj = taskInfo;
		myHandler.sendMessage(msgMessage);
//		DownLoadTaskInfo taskInfo = downloadManager.getTask(packageName);
//		try {
//			downloadManager.stopDownload(taskInfo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 恢复下载
	 * @param packageName 应用包名
	 */
	@JavascriptInterface
	public void toResume(String packageName){
		DownLoadTaskInfo taskInfo = new DownLoadTaskInfo();
		taskInfo.setPackageName(packageName);
		Message msgMessage = myHandler.obtainMessage();
		msgMessage.what = RESUME_TASK;
		msgMessage.obj = taskInfo;
		myHandler.sendMessage(msgMessage);
//		DownLoadTaskInfo taskInfo = downloadManager.getTask(packageName);
//		try {
//			downloadManager.resumeDownload(taskInfo,null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	/**
	 * 安装应用 
	 * @param packageName 应用包名
	 */
	@JavascriptInterface
	public void toInstall(String packageName){
		DownLoadTaskInfo taskInfo = downloadManager.getTask(packageName);
		AppUtil.installApk(context,taskInfo);
	}
	/**
	 * 打开应用 
	 * @param packageName 应用包名
	 * @param context
	 */
	@JavascriptInterface
	public void toOpen(String packageName){
		AppUtil.startApk2(context,packageName);
	}
	
	/**
	 * 弹出新的页面
	 * @param url 跳转地址
	 * @param title 标题
	 */
	@JavascriptInterface
	public void buildNewWebPage(String url,String title){
//		Log.e("", "buildNewPage--->");
		Message msgMessage = myHandler.obtainMessage();
		msgMessage.what = HIDE_TITLEBAR;
		myHandler.sendMessage(msgMessage);
//		//((MainActivity)context).showOrHideTitlebar(false);
		Log.d("fragmentTest", "calll");
		JsObj.url=url;
	//	((DownLoadListAct)context).fragmentContorler.showTab(1);
	}
	/**
	 * webview从新页返回首页
	 */
	@JavascriptInterface
	public void finishActivity(){
//		Log.e ("", "finishActivity--->");
		Message msgMessage = myHandler.obtainMessage();
		msgMessage.what = SHOW_TITLEBAR;
		myHandler.sendMessage(msgMessage);
//		//((MainActivity)context).showOrHideTitlebar(true);
//		((DownLoadListAct)context).fragmentContorler.showTab(0);
		Log.d("fragmentTest", "finsh");
	}
	
}