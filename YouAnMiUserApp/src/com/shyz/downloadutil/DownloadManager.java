package com.shyz.downloadutil;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager {
	private  static final String TAG = "DownloadManager";
	private String filePath;//文件保存路径
    private List<DownLoadTaskInfo> DownLoadTaskInfoList;//下载任务列表
    private int maxDownloadThread = 3;//下载线程
    private Context mContext;
    private DbUtils db;
    private static final long MIN_STORAGE = 10;//10M
    private static DownloadManager downloadManager;
    public static final int LOADING = 11;
    public static final int CANCLED = 12;
    public static final int SUCCESS = 13;
    public static final int FAILUE = 14;
    public static final int DELETE = 15;
    private Handler callbackHandler;
	/**
     * 单例取得下载器
     * @param appContext
     * @return
     */
    public static DownloadManager createDownloadManager(Context context){
    	if(downloadManager==null){
    		downloadManager = new DownloadManager(context);
    	}
		return downloadManager;
    }
    private DownloadManager(Context appContext) {
    	if(hasSdcard()){
    		filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/900_market/";
    	}else{
    		filePath = appContext.getFilesDir().getAbsolutePath()+"/900_market/";
    	}
        ColumnConverterFactory.registerColumnConverter(HttpHandler.State.class, new HttpHandlerStateConverter());
        mContext = appContext;
        db = DbUtils.create(mContext);
        try {
            DownLoadTaskInfoList = db.findAll(Selector.from(DownLoadTaskInfo.class));
           
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
        if (DownLoadTaskInfoList == null) {
            DownLoadTaskInfoList = new ArrayList<DownLoadTaskInfo>();
        }else{
        	 for(int i = 0,length=DownLoadTaskInfoList.size();i<length;i++){//手动杀进程时，正在下载的任务状态loading不能转为取消状态，下次启动是要手动设置，保证状态正确
             	if(DownLoadTaskInfoList.get(i).getState() == State.LOADING){
             		DownLoadTaskInfoList.get(i).setState(State.CANCELLED);
             	}
             }
        }
    }


    public int getDownLoadTaskInfoListCount() {
        return DownLoadTaskInfoList.size();
    }
 
    
    /**
     * 从下载列表中获取未下载完成的任务
     * @return
     */
    public List<DownLoadTaskInfo> getDoingTask(){
    	List<DownLoadTaskInfo> list = new ArrayList<DownLoadTaskInfo>();
    	for(DownLoadTaskInfo task : DownLoadTaskInfoList){
    		if(task.getState()!=State.SUCCESS){
    			list.add(task);
    		}
    	}
    	return list;
    }
    /**
     * 获取未下载完成任务数
     * @return
     */
    public int getDoingTaskCount(){
    	int count = 0;
    	for(DownLoadTaskInfo task : DownLoadTaskInfoList){
    		if(task.getState()!=State.SUCCESS){
    			count++;
    		}
    	}
    	return count;
    }
    /**
     * 从下载列表中获取已经下载完成的任务
     * @return
     */
    public List<DownLoadTaskInfo> getDoneTask(){
    	List<DownLoadTaskInfo> list = new ArrayList<DownLoadTaskInfo>();
    	for(DownLoadTaskInfo task : DownLoadTaskInfoList){
    		if(task.getState()==State.SUCCESS){
    			list.add(task);
    		}
    	}
    	return list;
    }
    /**
     * 根据包名取得下载任务里的对象，没有返回null
     * 
     * @param PackageName
     * @return
     */
    public DownLoadTaskInfo getTask(String PackageName){
    	DownLoadTaskInfo info;
    	for (int i = 0,length = DownLoadTaskInfoList.size(); i < length; i++) {
    		info = DownLoadTaskInfoList.get(i);
			if(info.getPackageName().equals(PackageName)){
				return info;
			}
		}
    	return null;
    }
 
    /**
     * 添加一个下载应用
     * @param downlaodUrl 下载地址
     * @param fileName 应用名称
     * @param packageName 包名
     * @param iconUrl 应用图标地址
     * @param versionCode 版本号
     * @param sizeMb 应用大小，必须转化成MB
     * @throws DbException
     */
    public void addNewDownload(String downlaodUrl, String fileName,String packageName,String iconUrl,int versionCode,float sizeMb,RequestCallBack<File> callback) throws DbException {
    	if (!readSDCard(sizeMb))return;
     	removeDownload(getTask(packageName));//如果以前已经有存在的下载任务，则先删除
    	String file_save_path = filePath+System.currentTimeMillis()+"_zxly.apk";
        DownLoadTaskInfo downLoadTaskInfo = new DownLoadTaskInfo();
        downLoadTaskInfo.setDownloadUrl(downlaodUrl);
        downLoadTaskInfo.setPackageName(packageName);
        downLoadTaskInfo.setAutoResume(true);
        downLoadTaskInfo.setFileName(fileName);
        downLoadTaskInfo.setVersionCode(versionCode);
        downLoadTaskInfo.setIconUrl(iconUrl);
        downLoadTaskInfo.setFileSavePath(file_save_path);
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(downlaodUrl, file_save_path, true, false, new ManagerCallBack(downLoadTaskInfo,callback));
        downLoadTaskInfo.setHandler(handler);
        downLoadTaskInfo.setState(handler.getState());
        DownLoadTaskInfoList.add(downLoadTaskInfo);
        db.saveOrUpdate(downLoadTaskInfo);
    }
    /**
     * 恢复下载
     * @param DownLoadTaskInfo
     * @param callback
     * @throws DbException
     */
    public void resumeDownload(DownLoadTaskInfo DownLoadTaskInfo,RequestCallBack<File> callback) throws DbException {
    	if (!readSDCard(DownLoadTaskInfo.getFileSizeOfMb()))return;	
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(
                DownLoadTaskInfo.getDownloadUrl(),
                DownLoadTaskInfo.getFileSavePath(),
                true,
                false,
                new ManagerCallBack(DownLoadTaskInfo,callback));
        DownLoadTaskInfo.setHandler(handler);
        DownLoadTaskInfo.setState(handler.getState());
        db.saveOrUpdate(DownLoadTaskInfo);
    }
    
    public void removeDownload(DownLoadTaskInfo DownLoadTaskInfo) throws DbException {
    	if(DownLoadTaskInfo==null)return;
    	if(DownLoadTaskInfo.getState()!=State.SUCCESS){//删除任务时，下载完成的任务不需要调用此方法
	        HttpHandler<File> handler = DownLoadTaskInfo.getHandler();
	        if (handler != null && !handler.isCancelled()) {
	            handler.cancel();
	        }
    	}
        DownLoadTaskInfoList.remove(DownLoadTaskInfo);
        db.delete(DownLoadTaskInfo);
        File file = new File(DownLoadTaskInfo.getFileSavePath());
        if(file.exists()){
        	file.delete();
        }
        if(callbackHandler!=null&&AppUtil.getInstalledAPkVersion(mContext, DownLoadTaskInfo.getPackageName())==-1){//
    		Message msg = callbackHandler.obtainMessage();
    		msg.what = DELETE;
    		msg.obj = DownLoadTaskInfo.getPackageName();
    		callbackHandler.sendMessage(msg);
    	} 
    }
    /**
     * 删除多个下载任务
     * @param taskList
     * @throws DbException
     */
    public void removeDownload(List<DownLoadTaskInfo> taskList) throws DbException{
    	 for (DownLoadTaskInfo downLoadTaskInfo : taskList) {
				removeDownload(downLoadTaskInfo);
		}	
    }
    /***
     * 暂停下载任务
     * @param DownLoadTaskInfo
     * @throws DbException
     */
    public void stopDownload(DownLoadTaskInfo downLoadTaskInfo) throws DbException {
        if(downLoadTaskInfo==null)return;
        HttpHandler<File> handler = downLoadTaskInfo.getHandler();
        if (handler != null && !handler.isCancelled()) {
            handler.cancel();
            if(downLoadTaskInfo.getState()!=State.SUCCESS){
        		downLoadTaskInfo.setState(handler.getState());
        	}
        } 
        db.saveOrUpdate(downLoadTaskInfo);
    }
    /**
     * 暂停全部下载任务，退出程序最好执行些方法不让在后台下载
     * @throws DbException
     */
    public void stopAllDownload() throws DbException {
        for (DownLoadTaskInfo DownLoadTaskInfo : DownLoadTaskInfoList) {
            HttpHandler<File> handler = DownLoadTaskInfo.getHandler();
            if (handler != null && !handler.isCancelled()) {
                handler.cancel();
                if(DownLoadTaskInfo.getState()!=State.SUCCESS){
            		DownLoadTaskInfo.setState(handler.getState());
            	}
            }
           
        }
        db.saveOrUpdateAll(DownLoadTaskInfoList);
    }
    /**
     * 退出应用，后台下载
     * @throws DbException
     */
    public void backupDownLoadTaskInfoList() throws DbException {
        for (DownLoadTaskInfo DownLoadTaskInfo : DownLoadTaskInfoList) {
            HttpHandler<File> handler = DownLoadTaskInfo.getHandler();
            if (handler != null) {
                DownLoadTaskInfo.setState(handler.getState());
            }
        }
        db.saveOrUpdateAll(DownLoadTaskInfoList);
    }

    public int getMaxDownloadThread() {
        return maxDownloadThread;
    }

    public void setMaxDownloadThread(int maxDownloadThread) {
        this.maxDownloadThread = maxDownloadThread;
    }
	public class ManagerCallBack extends RequestCallBack<File> {
	    private DownLoadTaskInfo downLoadTaskInfo;
        private RequestCallBack<File> baseCallBack;

        public RequestCallBack<File> getBaseCallBack() {
            return baseCallBack;
        }

        public void setBaseCallBack(RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
        }

        private ManagerCallBack(DownLoadTaskInfo downLoadTaskInfo, RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
            this.downLoadTaskInfo = downLoadTaskInfo;
        }
        @Override
        public Object getUserTag() {
            if (baseCallBack == null) return null;
            return baseCallBack.getUserTag();
        }

        @Override
        public void setUserTag(Object userTag) {
            if (baseCallBack == null) return;
            baseCallBack.setUserTag(userTag);
        }

        @Override
        public void onStart() {
        	//Log.d("fengruyi", "开始下载-->"+downLoadTaskInfo.getFileName());
        	if(callbackHandler!=null){
        		Message msg = callbackHandler.obtainMessage();
        		msg.what = LOADING;
        		msg.obj = downLoadTaskInfo.getPackageName();
        		callbackHandler.sendMessage(msg);
        	}
            HttpHandler<File> handler = downLoadTaskInfo.getHandler();
            if (handler != null) {
            	downLoadTaskInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downLoadTaskInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onStart();
            }
        }

        @Override
        public void onCancelled() {
        	//Log.d("fengruyi", "取消下载-->"+downLoadTaskInfo.getFileName());
        	if(callbackHandler!=null){
        		Message msg = callbackHandler.obtainMessage();
        		msg.what = CANCLED;
        		msg.obj = downLoadTaskInfo.getPackageName();
        		callbackHandler.sendMessage(msg);
        	}
        	 HttpHandler<File> handler = downLoadTaskInfo.getHandler();
             if (handler != null) {	
             	
             	downLoadTaskInfo.setState(handler.getState());
             }
             try {
             	
                 db.saveOrUpdate(downLoadTaskInfo);
             } catch (DbException e) {
             	
                 LogUtils.e(e.getMessage(), e);
             }
             if (baseCallBack != null) {
             	
                 baseCallBack.onCancelled();
             }
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
        	//Log.d("fengruyi", "正在下载-->"+downLoadTaskInfo.getFileName());
        	HttpHandler<File> handler = downLoadTaskInfo.getHandler();
            if (handler != null) {	
            	downLoadTaskInfo.setState(handler.getState());
            }
            downLoadTaskInfo.setRate((int)(current-downLoadTaskInfo.getProgress()));
            downLoadTaskInfo.setFileLength(total);
            downLoadTaskInfo.setProgress(current);
           
            try {
                db.saveOrUpdate(downLoadTaskInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onLoading(total, current, isUploading);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
        	//Log.d("fengruyi", "下载成功-->"+downLoadTaskInfo.getFileName());
        	if(callbackHandler!=null){
        		Message msg = callbackHandler.obtainMessage();
        		msg.what = SUCCESS;
        		msg.obj = downLoadTaskInfo.getPackageName();
        		callbackHandler.sendMessage(msg);
        	}
            HttpHandler<File> handler = downLoadTaskInfo.getHandler();
            if (handler != null) {
            	downLoadTaskInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downLoadTaskInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onSuccess(responseInfo);
            }
            //下载完成弹出安装界面
            AppUtil.installApk(mContext,downLoadTaskInfo);
        }

        @Override
        public void onFailure(HttpException error, String msg) {
        	//Log.d("fengruyi", "下载失败-->"+downLoadTaskInfo.getFileName());
        	if(callbackHandler!=null){
        		Message msg2 = callbackHandler.obtainMessage();
        		msg2.what = FAILUE;
        		msg2.obj = downLoadTaskInfo.getPackageName();
        		callbackHandler.sendMessage(msg2);
        	}
            HttpHandler<File> handler = downLoadTaskInfo.getHandler();
            if (handler != null) {
            	downLoadTaskInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downLoadTaskInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onFailure(error, msg);
            }
        }
    }

    private class HttpHandlerStateConverter implements ColumnConverter<HttpHandler.State> {

        @Override
        public HttpHandler.State getFieldValue(Cursor cursor, int index) {
            return HttpHandler.State.valueOf(cursor.getInt(index));
        }

        @Override
        public HttpHandler.State getFieldValue(String fieldStringValue) {
            if (fieldStringValue == null) return null;
            return HttpHandler.State.valueOf(fieldStringValue);
        }

        @Override
        public Object fieldValue2ColumnValue(HttpHandler.State fieldValue) {
            return fieldValue.value();
        }

        @Override
        public ColumnDbType getColumnDbType() {
            return ColumnDbType.INTEGER;
        }
    }
    
    
    public interface DownloadStateListener{
    	public void onStart(DownLoadTaskInfo info);
    	public void onLoading(DownLoadTaskInfo info);
    	public void onCancle(DownLoadTaskInfo info);
    	public void onSuccess(DownLoadTaskInfo info);
    	public void onFailure(DownLoadTaskInfo info);
    }
	 /**
	  * 是否有Sd卡
	  * @return
	  */
	 public  boolean hasSdcard() {
	     String status = Environment.getExternalStorageState();
	     if (status.equals(Environment.MEDIA_MOUNTED)) {
	         return true;
	     } else {
	         return false;
	     }
	 }
	public boolean  readSDCard(float size) {
    	 String state = Environment.getExternalStorageState();
    	 if (Environment.MEDIA_MOUNTED.equals(state)) {
	    	  StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    	  long blockSize = sf.getBlockSize();   
			  long blocks = sf.getAvailableBlocks();   
			  long availableSpare = (blocks*blockSize)/1024/1024;  
			  if(availableSpare<=MIN_STORAGE){
				 // Log.e(TAG, "手机存储空间不足，请释放内存");
				  Toast.makeText(mContext, "手机存储空间不足，请释放内存", Toast.LENGTH_SHORT).show();
				  return false;
			  }
			  else if(availableSpare<size){
				 // Log.e(TAG, "手机存储空间不足下载此应用，请释放内存");
				  Toast.makeText(mContext, "手机存储空间不足下载此应用，请释放内存", Toast.LENGTH_SHORT).show();
				  return false;
			  }
    	 }
    	 return true;
    	}
	
	public Handler getCallbackHandler() {
		return callbackHandler;
	}
	public void setCallbackHandler(Handler callbackHandler) {
		this.callbackHandler = callbackHandler;
	}
	
	
	/**
	 * 下载器监听
	 * @author Administrator
	 *
	 */
	public interface DownloadListener{
		/**
		 * 任务正在下载
		 * @param taskInfo
		 */
		public void onDownloading(DownLoadTaskInfo taskInfo);
		/**
		 * 任务已经暂停
		 * @param taskInfo
		 */
		public void onCancled(DownLoadTaskInfo taskInfo);
		/**
		 * 任务下载成功
		 * @param taskInfo
		 */
		public void onSuccess(DownLoadTaskInfo taskInfo);
		/**
		 * 任务下载失败
		 * @param taskInfo
		 */
		public void onFailue(DownLoadTaskInfo taskInfo);
	}
	

	
}
