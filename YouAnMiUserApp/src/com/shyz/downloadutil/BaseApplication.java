package com.shyz.downloadutil;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class BaseApplication {

	private static BaseApplication instance;
	public static Application app;
	public static float mHeightPixels = -1f;//屏幕的高，像素值
	public static float mWidthPixels = -1f;//屏幕的宽，像素值
	public static float mDensity = 1f;//屏幕密度

	public static BaseApplication getInstance(Application app){
		
		if(instance==null)
			instance=new BaseApplication();
		BaseApplication.app=app;
		return instance;
	}


	public void init() {
		readParams();
		initImageLoader();
	}

	
	
	/**
	 * 获取屏幕参数
	 */
	protected void readParams() {
	;
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) app.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		mWidthPixels = displayMetrics.widthPixels;
		mHeightPixels = displayMetrics.heightPixels;
		mDensity = displayMetrics.density;
		
	}

	/**
	 * 图片缓存初始化,图片缓存大小 目录等设置
	 */
	protected void initImageLoader() {
		//File cacheDir = StorageUtils.getCacheDirectory(instance);// 缓存目录放置在应用包下
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
			    .Builder(app)  
			    .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
			    .threadPoolSize(3)//线程池内加载的数量  
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
			    .memoryCacheSize(2 * 1024 * 1024)    
			    .tasksProcessingOrder(QueueProcessingType.LIFO) 
			    .imageDownloader(new BaseImageDownloader(app, 5*1000, 30*1000))// connectTimeout (5 s), readTimeout (30 s)超时时间  
			    .writeDebugLogs() // Remove for release app  
			    .build();//开始构建  
		ImageLoader.getInstance().init(config);// 全局初始化此配置

	}
	
}
