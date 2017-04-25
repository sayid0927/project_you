package com.shyz.downloadutil;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.zxly.o2o.activity.BasicAct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.FragmentTabHandler;

import java.util.ArrayList;
import java.util.List;

public class DownLoadListAct extends BasicAct implements OnClickListener {
	List<Fragment> fragments=new ArrayList<Fragment>();
	public FragmentTabHandler fragmentContorler;
    DownloadManager downloadManager;
    private AppBroadcastReceiver mAppBroadcastReceiver;
    private AppInfoFragment appInfoFragment;
    private AppListFragment appListFragment;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_download);

		  downloadManager = DownloadManager.createDownloadManager(this);
		  downloadManager.setCallbackHandler(myHandler);
		  mAppBroadcastReceiver=new AppBroadcastReceiver(); 
	      IntentFilter intentFilter=new IntentFilter(); 
	      intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED); 
	      intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED); 
	      intentFilter.addDataScheme("package"); 
	      this.registerReceiver(mAppBroadcastReceiver, intentFilter);
	      appListFragment=new AppListFragment();
	      appInfoFragment=new AppInfoFragment();
			fragments.add(appListFragment);
			fragments.add(appInfoFragment);
			fragmentContorler = new FragmentTabHandler(this, fragments,R.id.layout_second_issue);
			fragmentContorler.showTab(0);
	}
	
	Handler myHandler = new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DownloadManager.CANCLED:
				Log.d("MainActivity"	, "onCancled-->");
				if(appListFragment.getWebView()!=null)
				appListFragment.getWebView().loadUrl("javascript:AJS.appStop('"+msg.obj.toString()+"')");
				
				if(appInfoFragment.getWebView()!=null)
				appInfoFragment.getWebView().loadUrl("javascript:AJS.appStop('"+msg.obj.toString()+"')");
				break;
			case DownloadManager.FAILUE:
				Log.d("MainActivity"	, "onFailue-->");
				if(appInfoFragment.getWebView()!=null)
					appInfoFragment.getWebView().loadUrl("javascript:AJS.appRetry('"+msg.obj.toString()+"')");
				
				if(appListFragment.getWebView()!=null)
					appListFragment.getWebView().loadUrl("javascript:AJS.appRetry('"+msg.obj.toString()+"')");
				break;
			case DownloadManager.LOADING:
				Log.d("MainActivity"	, "onDownloading-->");
				if(appListFragment.getWebView()!=null)
					appListFragment.getWebView().loadUrl("javascript:AJS.appDownLoad('"+msg.obj.toString()+"')");
				if(appInfoFragment.getWebView()!=null)
					appInfoFragment.getWebView().loadUrl("javascript:AJS.appDownLoad('"+msg.obj.toString()+"')");
				break;
			case DownloadManager.SUCCESS:
				Log.d("MainActivity"	, "onSuccess-->");
				if(appInfoFragment.getWebView()!=null)
					appInfoFragment.getWebView().loadUrl("javascript:AJS.appInstall('"+msg.obj.toString()+"')");
				if(appListFragment.getWebView()!=null)
					appListFragment.getWebView().loadUrl("javascript:AJS.appInstall('"+msg.obj.toString()+"')");
				break;
			case DownloadManager.DELETE:
				Log.d("MainActivity"	, "DELETE-->");
				
				if(appListFragment.getWebView()!=null)
					appListFragment.getWebView().loadUrl("javascript:AJS.appCancel('"+msg.obj.toString()+"')");
				
				if(appInfoFragment.getWebView()!=null)
					appInfoFragment.getWebView().loadUrl("javascript:AJS.appCancel('"+msg.obj.toString()+"')");
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
	        
				if(appListFragment.getWebView()!=null)
					appListFragment.getWebView().loadUrl("javascript:AJS.appOpen('"+packageName.replace("package:", "")+"')");
				
				if(appInfoFragment.getWebView()!=null)
					appInfoFragment.getWebView().loadUrl("javascript:AJS.appOpen('"+packageName.replace("package:", "")+"')");
	        } 
	        if (Intent.ACTION_PACKAGE_REMOVED.equals(action)&&packageName!=null) { 
	        
				if(appListFragment.getWebView()!=null)
					appListFragment.getWebView().loadUrl("javascript:AJS.appCancel('"+packageName.replace("package:", "")+"')");
				
				if(appInfoFragment.getWebView()!=null)
					appInfoFragment.getWebView().loadUrl("javascript:AJS.appCancel('"+packageName.replace("package:", "")+"')");		
	        } 
	    } 
	   
	}

	
//	/**
//	 * 显示或隐藏activity标题
//	 * @param flag true为显示，false隐藏
//	 */
//	public void showOrHideTitlebar(boolean flag){
//		if(flag){
//			title.setVisibility(View.VISIBLE);
//		}else{
//			
//			title.setVisibility(View.GONE);
//		}
//	}
	
@Override
public void onClick(View v) {
	
	switch (v.getId()) {
		case R.id.download_task:
			Intent intent = new Intent(DownLoadListAct.this,DownLoadTaskActivity.class);
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
