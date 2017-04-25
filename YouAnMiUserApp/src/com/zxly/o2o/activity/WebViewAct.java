package com.zxly.o2o.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.service.RemoteInvokeService;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * todo:
 * 这个界面类型复杂之后分类难处理，要重新梳理
 *
 * @author wuchenhui 2015-7-2
 * @description H5页面
 */
public class WebViewAct extends BasicAct implements View.OnClickListener {

	private  final  static  String TAG=WebViewAct.class.getName();


	public static final int TYPE_DEFAULT=-1;
	public static final int TYPE_H5_GAME=5;

	public static final int TYPE_GAME=1; //H5游戏
	public static final int TYPE_DSD=2; //剁手党活动

	private int pageType;
	private String shareStatisticsUrl;

	private LoadingView loadingview = null;
	private WebView webView;
	private String loadUrl;
	private String title;
	static ShareInfo shareInfo;
	private ShareDialog shareDialog;

	RelativeLayout layoutContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());



		title = getIntent().getStringExtra("title");
		loadUrl=getIntent().getStringExtra("loadUrl");
		pageType=getIntent().getIntExtra("pageType", TYPE_DEFAULT);

		initViews();
		loadH5(loadUrl);
	}



	@Override
	public void onPause() {
		//webView.reload();
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		shareInfo=null;
		layoutContent.removeView(webView);
		webView.destroy();
	}

	public int getLayoutId(){
		return R.layout.win_webview;
	}


	private void initViews() {
		shareDialog=new ShareDialog();
		View btnShare=findViewById(R.id.btn_right);
		btnShare.setOnClickListener(this);
		if (shareInfo==null){
			btnShare.setVisibility(View.GONE);
		}else {
			btnShare.setVisibility(View.VISIBLE);
		}

		findViewById(R.id.tag_title_btn_back).setOnClickListener(this);
		if(title != null){
			((TextView) findViewById(R.id.tag_title_title_name)).setText(title);
		}

		loadingview = (LoadingView) findViewById(R.id.view_loading);
		loadingview.startLoading();

		layoutContent= (RelativeLayout) findViewById(R.id.layout_container);
		webView=new WebView(this);
		webView.setBackgroundColor(Color.WHITE);
		RelativeLayout.LayoutParams webParams=new RelativeLayout.LayoutParams(-1,-1);
		webParams.addRule(RelativeLayout.BELOW,R.id.layout_title);
		webView.setLayoutParams(webParams);

		layoutContent.addView(webView);

		webView.getSettings().setPluginState(WebSettings.PluginState.ON);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setSaveFormData(false); // 支持保存数据
		webView.clearCache(true); // 清除缓存
		webView.clearHistory(); // 清除历史记录
		webView.getSettings().setAppCacheEnabled(true);//是否使用缓存
		webView.getSettings().setDomStorageEnabled(true);//DOM Storage
		webView.setHorizontalScrollBarEnabled(false);//水平不显示
		webView.setVerticalScrollBarEnabled(false); //垂直不显示

		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式

		String cacheDirPath = getFilesDir().getAbsolutePath()+ Config.getH5CachePath(this);
//      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
		Log.e(TAG, "H5缓存路径cacheDirPath======   "+cacheDirPath);
		//设置数据库缓存路径
		webView.getSettings().setDatabasePath(cacheDirPath);
		//设置  Application Caches 缓存目录
		webView.getSettings().setAppCachePath(cacheDirPath);
		//开启 Application Caches 功能
		webView.getSettings().setAppCacheEnabled(true);

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				loadingview.startLoading();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				loadingview.onLoadingComplete();
			}
		});

		RemoteInvokeService jsObj = new RemoteInvokeService(this, webView, new CallBack() {
			@Override
			public void onCall() {

			}
		});

		webView.addJavascriptInterface(jsObj, "js_invoke");

	}

	private void loadH5(String loadUrl) {
		webView.loadUrl(loadUrl);
	}

	public static void start(String title, String loadUrl) {
		Intent intent = new Intent(AppController.getInstance().getTopAct(), H5DetailAct.class);
		intent.putExtra("title", title);
		intent.putExtra("bannerUrl",loadUrl);
		ViewUtils.startActivity(intent,AppController.getInstance().getTopAct());

	}

	public static void start(int pageType, String title, String loadUrl,ShareInfo shareInfo) {
		Intent intent = new Intent(AppController.getInstance().getTopAct(), WebViewAct.class);
		intent.putExtra("title", title);
		intent.putExtra("loadUrl",loadUrl);
		intent.putExtra("pageType",pageType);
		WebViewAct.shareInfo=shareInfo;
		ViewUtils.startActivity(intent, AppController.getInstance().getTopAct());
	}

	protected void goBack(){
		if(webView.canGoBack()){
			webView.goBack();
		}else {
			finish();
		}
	}



	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.tag_title_btn_back:
				goBack();
				break;

			case R.id.btn_right:
				if(shareDialog==null)
					shareDialog=new ShareDialog();


				//门店文章的话要船bitmap
				shareDialog.show(shareInfo.getTitle(), shareInfo.getDesc(),shareInfo.getUrl(),shareInfo.getShareIcon(), new ShareListener() {
					@Override
					public void onComplete(Object var1) {

						switch (pageType){

							case TYPE_H5_GAME:
								requestMethod=BaseRequest.Method.GET;
								shareStatisticsUrl= StringUtil.getDomainName(shareInfo.getUrl())+"/chop/hand/activity/get/transmit?activityId="+shareInfo.getId();
								new ShareStatisticsRequest().start();
								break;

							default:
								break;

						}

					}

					@Override
					public void onFail(int errorCode) {

					}
				});
				break;
		}
	}


	private int requestMethod=BaseRequest.Method.POST;
	class ShareStatisticsRequest extends BaseRequest{


		@Override
		public String getUrl() {

			return shareStatisticsUrl;
		}

		@Override
		public int requestMethod() {
			return requestMethod;
		}

		@Override

		protected String method() {
			return shareStatisticsUrl+"";
		}
	}

}
