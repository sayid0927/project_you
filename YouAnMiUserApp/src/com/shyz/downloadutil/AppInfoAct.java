/*
 * 文件名：AppInfoAct.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： AppInfoAct.java
 * 修改人：wuchenhui
 * 修改时间：2015-5-18
 * 修改内容：新增
 */
package com.shyz.downloadutil;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zxly.o2o.activity.BasicAct;
import com.zxly.o2o.o2o_user.R;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-5-18
 * @since      YIBA-O2O
 */
public class AppInfoAct extends BasicAct implements OnClickListener{

	
	WebView webview;
    DownloadManager downloadManager;
    View btnDownloadTask,btnBck,title;
    private AppBroadcastReceiver mAppBroadcastReceiver; 
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.applist_activity_main);
		
	    Intent it=getIntent();
	    String url=it.getStringExtra("url");
	    
		title=findViewById(R.id.title);
		title.setVisibility(View.GONE);
		downloadManager = DownloadManager.createDownloadManager(this);
		btnDownloadTask = findViewById(R.id.download_task);
		webview = (WebView) findViewById(R.id.myWebivew);
		webview.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		JsObj jsObj = new JsObj(this);
		webview.addJavascriptInterface(jsObj, "roid");
	//	webview.loadUrl("http://ftpagg.18.net/yammarket/index.html");
		webview.loadUrl(url);
		
		downloadManager.setCallbackHandler(myHandler);
		  mAppBroadcastReceiver=new AppBroadcastReceiver(); 
	      IntentFilter intentFilter=new IntentFilter(); 
	      intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); 
	      intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
	      intentFilter.addDataScheme("package"); 
	      this.registerReceiver(mAppBroadcastReceiver, intentFilter); 
	      btnDownloadTask.setOnClickListener(this);
	      btnBck=findViewById(R.id.btn_back);
	      btnBck.setOnClickListener(this);
	}
	
	Handler myHandler = new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DownloadManager.CANCLED:
				Log.d("MainActivity"	, "onCancled-->");
				webview.loadUrl("javascript:AJS.appStop('"+msg.obj.toString()+"')");
				break;
			case DownloadManager.FAILUE:
				Log.d("MainActivity"	, "onFailue-->");
				webview.loadUrl("javascript:AJS.appRetry('"+msg.obj.toString()+"')");
				break;
			case DownloadManager.LOADING:
				Log.d("MainActivity"	, "onDownloading-->");
				webview.loadUrl("javascript:AJS.appDownLoad('"+msg.obj.toString()+"')");
				break;
			case DownloadManager.SUCCESS:
				Log.d("MainActivity"	, "onSuccess-->");
				webview.loadUrl("javascript:AJS.appInstall('"+msg.obj.toString()+"')");
				break;
			case DownloadManager.DELETE:
				Log.d("MainActivity"	, "DELETE-->");
				webview.loadUrl("javascript:AJS.appCancel('"+msg.obj.toString()+"')");
				break;
			}
		};
	};

	
	
	@Override
	protected void onDestroy() {
		if(mAppBroadcastReceiver!=null){
			unregisterReceiver(mAppBroadcastReceiver);
			mAppBroadcastReceiver = null;
		}
		super.onDestroy();
	}
	
	private class AppBroadcastReceiver extends BroadcastReceiver { 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	        String action=intent.getAction(); 
	        String packageName = intent.getDataString();
	        if (Intent.ACTION_PACKAGE_ADDED.equals(action)&&packageName!=null) { 
	        	
	        	webview.loadUrl("javascript:AJS.appOpen('"+packageName.replace("package:", "")+"')");
	            
	        } 
	        if (Intent.ACTION_PACKAGE_REMOVED.equals(action)&&packageName!=null) { 
	        	
	        	webview.loadUrl("javascript:AJS.appCancel('"+packageName.replace("package:", "")+"')");
				
	        } 
	    } 
	   
	}

	
	/**
	 * 显示或隐藏activity标题
	 * @param flag true为显示，false隐藏
	 */
	public void showOrHideTitlebar(boolean flag){
		if(flag){
			title.setVisibility(View.VISIBLE);
		}else{
			
			title.setVisibility(View.GONE);
		}
	}
	
@Override
public void onClick(View v) {
	
	switch (v.getId()) {
		case R.id.download_task:
			Intent intent = new Intent(this,DownLoadTaskActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_back:
			finish();
				break;

		default :
			break;

	}
}
}
