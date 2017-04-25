package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.Banners;
import com.zxly.o2o.model.SystemMessage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.SystemMsgReadRequest;
import com.zxly.o2o.request.SystemMsgReadUnloginRequest;
import com.zxly.o2o.service.RemoteInvokeService;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.NetUtils;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * @author fengrongjian 2015-7-2
 * @description H5页面
 */
public class H5DetailAct extends BasicAct implements View.OnClickListener {

	public static final int TYPE_DEFAULT=-1;
	public static final int TYPE_H5_GAME=Banners.TYPE_GAME;
	public static final int TYPE_H5_ARTICLE=2;//平台文章

	private SystemMessage systemMessage;
	private LoadingView loadingview = null;
	private String shareStatisticsUrl;
	private WebView webView;
	private String loadUrl;
	private String bannerUrl;
	private String title;
	private Object shareIcon;
	private int pageType;
    ShareDialog shareDialog;
	private static Banners mBanner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_h5_detail);

		pageType=getIntent().getIntExtra("pageType",TYPE_DEFAULT);
		if(mBanner!=null){
			if(pageType==TYPE_H5_GAME){
				StringBuilder sb=new StringBuilder(mBanner.getShareUrl());
				sb.append("&fromApp=true");
				sb.append("&Authorization=").append(PreferUtil.getInstance().getLoginToken());
				sb.append("&DeviceID=").append(Config.imei);
				sb.append("&TargetID=").append(Config.shopId);
				loadUrl=sb.toString();
			}else {
				loadUrl=mBanner.getUrl();
			}
		}

		bannerUrl = getIntent().getStringExtra("bannerUrl");
		systemMessage = (SystemMessage) getIntent().getSerializableExtra("systemMessage");
		title = getIntent().getStringExtra("title");
		if(bannerUrl != null){
			if(Account.user != null){
				loadUrl = bannerUrl.replace("{userId}", Account.user.getId() + "");
			} else {
				loadUrl = bannerUrl.replace("{userId}", "0");
			}
		} else if(systemMessage != null){
			loadUrl = systemMessage.getUrl().replace("{userId}", Account.user.getId() + "");
			if (systemMessage.getStatus() == 1) {
				markRead(systemMessage.getId());
			} else if (systemMessage.getStatus() == 0) {
				markReadUnlogin(systemMessage.getId());
			}
		}

		initViews();
		loadH5(loadUrl);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		mBanner=null;
	}

	public static void start(Activity curAct, SystemMessage systemMessage) {
		Intent intent = new Intent(curAct, H5DetailAct.class);
		intent.putExtra("systemMessage", systemMessage);
		ViewUtils.startActivity(intent, curAct);
	}
	
	public static void start(Activity curAct, String bannerUrl, String title) {
		Intent intent = new Intent(curAct, H5DetailAct.class);
		intent.putExtra("title", title);
		intent.putExtra("bannerUrl", bannerUrl);
		ViewUtils.startActivity(intent, curAct);
	}

	public static void start(Activity curAct, String bannerUrl, String title,String desc) {
		Intent intent = new Intent(curAct, H5DetailAct.class);
		intent.putExtra("title", title);
		intent.putExtra("desc",desc);
		intent.putExtra("bannerUrl", bannerUrl);
		ViewUtils.startActivity(intent, curAct);
	}

	public static void start(int pageType,Activity curAct,String title,Banners banner) {
		Intent intent = new Intent(curAct, H5DetailAct.class);
		intent.putExtra("title", title);
		intent.putExtra("pageType", pageType);
        mBanner=banner;
		ViewUtils.startActivity(intent, curAct);
	}


	private void initViews() {
		View btnRight=findViewById(R.id.btn_right);
		if(mBanner!=null && mBanner.getType() != Banners.TYPE_EXTERNAL_LINK){
			btnRight.setVisibility(View.VISIBLE);
			btnRight.setOnClickListener(this);
		}

		findViewById(R.id.tag_title_btn_back).setOnClickListener(this);

		if(title != null){
			((TextView) findViewById(R.id.tag_title_title_name)).setText(title);
		} else {
			((TextView) findViewById(R.id.tag_title_title_name)).setText(systemMessage.getTypeName());
		}

		loadingview = (LoadingView) findViewById(R.id.view_loading);
		loadingview.startLoading();
		webView = ((WebView) findViewById(R.id.web_view));
	}

	public void loadH5(String loadUrl) {

		if(NetUtils.isNetworkAvailable(H5DetailAct.this)) {
			webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		} else {
			webView.getSettings().setCacheMode(
					WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
		// webView.getSettings().setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染

		webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setUseWideViewPort(true);
	//	webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setSaveFormData(false); // 支持保存数据
		//webView.clearCache(true); // 清除缓存
		webView.clearHistory(); // 清除历史记录
		webView.getSettings().setAppCacheEnabled(true);//是否使用缓存
		webView.getSettings().setDomStorageEnabled(true);//DOM Storage

		 String cacheDomPath = getApplicationContext().getDir("cacheDomPath", Context.MODE_PRIVATE).getPath();
		 String cacheAppPath = getApplicationContext().getDir("cacheAppPath", Context.MODE_PRIVATE).getPath();
		webView.getSettings().setAppCacheMaxSize(8*1024*1024);
		webView.getSettings().setAppCachePath(cacheAppPath);
		webView.getSettings().setDatabasePath(cacheDomPath);

		webView.setHorizontalScrollBarEnabled(false);//水平不显示
		webView.setVerticalScrollBarEnabled(false); //垂直不显示
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				ViewUtils.setVisible(webView);
				loadingview.onLoadingComplete();
			}
		});


		RemoteInvokeService jsObj = new RemoteInvokeService(this, webView, new ParameCallBack() {
			@Override
			public void onCall(Object object) {

			}
		});

		webView.addJavascriptInterface(jsObj, "js_invoke");
		loadingview.startLoading();
		webView.loadUrl(loadUrl);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			goBack();
			return false;
		}else {
			return super.onKeyDown(keyCode,event);
		}
	}

	protected void goBack(){
		if(webView.canGoBack()){
			webView.goBack();
		}else {
			finish();
		}
	}

	private void markReadUnlogin(long id) {
		final SystemMsgReadUnloginRequest request = new SystemMsgReadUnloginRequest(
				id, 0);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
			}

			@Override
			public void onFail(int code) {
			}
		});
		request.start(this);
	}

	private void markRead(long id) {
		final SystemMsgReadRequest request = new SystemMsgReadRequest(id);
		request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

			@Override
			public void onOK() {
			}

			@Override
			public void onFail(int code) {
			}
		});
		request.start(this);
	}



	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tag_title_btn_back:
			goBack();
			break;

			case R.id.btn_right:
				String imgName="";
				if(shareDialog==null)
					shareDialog=new ShareDialog();
				StringBuilder sb=null;
				if(pageType==Banners.TYPE_GAME){
					shareIcon=BitmapUtil.drawableToBitmap(getResources().getDrawable(R.drawable.icon_dsd_share));
					sb=new StringBuilder(mBanner.getShareUrl());
					imgName="icon_dsd_share.png";
				}else if (pageType==TYPE_H5_ARTICLE){
					shareIcon=BitmapUtil.drawableToBitmap(getResources().getDrawable(R.drawable.icon_youanmi));
					imgName="icon_youanmi.png";
				}else
				{
					sb=new StringBuilder(mBanner.getUrl());
				}
				sb.append("&title=").append(mBanner.getTitle()).append("&desc=").append(mBanner.getDesc());
				shareDialog.show(mBanner.getTitle(), mBanner.getDesc(),sb.toString(),shareIcon,imgName,new ShareListener() {
					@Override
					public void onComplete(Object var1) {

						switch (pageType){

							case TYPE_H5_GAME:
								requestMethod=BaseRequest.Method.GET;
								shareStatisticsUrl= StringUtil.getDomainName(mBanner.getUrl())+"/chop/hand/activity/get/transmit?activityId="+mBanner.getProductId();
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
