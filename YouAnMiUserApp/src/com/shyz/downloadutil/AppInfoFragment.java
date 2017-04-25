/*
 * 文件名：AppInfoFragment.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： AppInfoFragment.java
 * 修改人：wuchenhui
 * 修改时间：2015-5-18
 * 修改内容：新增
 */
package com.shyz.downloadutil;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zxly.o2o.fragment.BaseFragment;
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
public class AppInfoFragment extends BaseFragment {
	WebView webview;
    DownloadManager downloadManager;
    View btnDownloadTask,btnBck,title;
	@Override
	protected void initView() {

	    
		title=findViewById(R.id.title);
		title.setVisibility(View.GONE);
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
		JsObj jsObj = new JsObj(getActivity());
		webview.addJavascriptInterface(jsObj, "roid");

	}

	public WebView getWebView(){
		return webview;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		String url=JsObj.url;
		webview.loadUrl(url);
	}
	@Override
	protected int layoutId() {

		return R.layout.applist_activity_main;
	}


}
