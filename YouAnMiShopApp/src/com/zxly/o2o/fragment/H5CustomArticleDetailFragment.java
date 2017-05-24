package com.zxly.o2o.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.FragmentListAct;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kenwu on 2016/3/23.
 */
public class H5CustomArticleDetailFragment extends BaseFragment implements  View.OnClickListener {


    public static final int WEBVIEW_STATUS_NOMAL=1;
    private int status;

    private LoadingView loadingview = null;
    private WebView webView;
    private View btnSave,layoutSave,btnShare;

    private boolean contentExist=true;

    String pageTitle,shareTitle,desc="",shareUrl;
    String wxUrl;
    String fireUrl;

    ShareDialog shareDialog;
    private ArticlePreViewRequest articlePreViewRequest;
    private SaveArticleRequest saveArticleRequest;


    private ParameCallBack articleRefreshCallBack;

    //文章预览回调...预览界面使用
    private BaseRequest.ResponseStateListener articlePreViewResponseListener=new BaseRequest.ResponseStateListener() {
        @Override
        public void onOK() {
            if (!DataUtil.stringIsNull(articlePreViewRequest.articleReviewUrl)) {
                if(contentExist){
                    layoutSave.setVisibility(View.VISIBLE);
                }

                btnShare.setVisibility(View.VISIBLE);
                layoutSave.setVisibility(View.VISIBLE);

                fireUrl=articlePreViewRequest.articleReviewUrl;
                loadUrl(fireUrl);
            } else {
                loadingview.onDataEmpty("处理失败，稍后再试！");
            }
        }

        @Override
        public void onFail(int code) {
            ViewUtils.setGone(webView);
            loadingview.onDataEmpty("处理失败，稍后再试！");
        }
    };
    private String title;
    private String shareImageUrl;
    private String userAppName;


  /*  @Override
    public void onPause() {
        webView.reload();
        super.onPause();
    }*/

    public static H5CustomArticleDetailFragment newInstance(Bundle bundle){
        H5CustomArticleDetailFragment  f=new H5CustomArticleDetailFragment ();
        f.setArguments(bundle);
        return f;
    }


    @Override
    protected void initView(Bundle bundle) {
        wxUrl=bundle.getString("wxUrl"); //需要保存到服务器才能分享（保存按钮显示）
        fireUrl=bundle.getString("fireUrl");//直接打开网页（保存按钮隐藏）
        title =bundle.getString("title");
        desc=bundle.getString("desc");//修改
        shareImageUrl =bundle.getString("shareImageUrl");
        userAppName =bundle.getString("userAppName");
        pageTitle=bundle.getString("pageTitle");
        if(!TextUtils.isEmpty(userAppName)){
            desc="【"+userAppName+"】"+desc;
        }
        shareUrl=fireUrl;

        if(getActivity() instanceof  FragmentListAct){
            ((FragmentListAct)getActivity()).btnShare.setOnClickListener(H5CustomArticleDetailFragment.this);
        }
        loadingview = (LoadingView) findViewById(R.id.view_loading);

        if (DataUtil.stringIsNull(wxUrl)&&DataUtil.stringIsNull(fireUrl)){
            loadingview.onLoadingFail();
            return;
        }else
        {
            loadingview.startLoading();
        }


        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                if(fireUrl!=null){
                    loadUrl(fireUrl);
                }else {
                  loadingview.onDataEmpty();
                }
            }
        });

        webView = ((WebView) findViewById(R.id.web_view));


        btnShare=((FragmentListAct)getActivity()).btnShare;

//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setLoadWithOverviewMode(true);


        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSaveFormData(false); // 支持保存数据
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.clearCache(true); // 清除缓存
        webView.clearHistory(); // 清除历史记录
        webView.getSettings().setAppCacheEnabled(true);//是否使用缓存
        webView.getSettings().setDomStorageEnabled(true);//DOM Storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //暂时不做处理，在小米系统上back的时候收不到回调
//                if (webView.getUrl().equals(fireUrl)) {
//                    txtTitle.setText(pageTitle);
//                } else {
//                    txtTitle.setText(webView.getTitle());
//                }
            }

        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, final int errorCode, String description, String failingUrl) {
              //  Log.e("webMeoth", " onReceivedError -->" + errorCode + "  desc -->" + description + " faurl ->" + failingUrl);

              //  if(errorCode == WebViewClient.ERROR_CONNECT || errorCode == WebViewClient.ERROR_TIMEOUT || errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
//                    webView.loadData("", "text/html", "utf_8");
                    ViewUtils.setGone(webView);
                    loadingview.onLoadingFail();
                    status = errorCode;




            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
          //      Log.e("webMeoth", " shouldOverrideUrlLoading -->" + url);
                if(url!=fireUrl){
                    return  true;
                }
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Log.e("webMeoth", " onPageFinished -->" + url);
                if(status==WEBVIEW_STATUS_NOMAL){
                    ViewUtils.setVisible(webView);
                    loadingview.onLoadingComplete();
                }
                if(StringUtil.isNull(fireUrl))
                {
                    view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML);");
                }

            }
        });

        layoutSave =findViewById(R.id.layout_save);
        btnSave=findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);


        //如果传入的是微信URL 就请求预览，如果是fireUrl直接打开
        if(!DataUtil.stringIsNull(wxUrl)){
            loadUrl(wxUrl);
        }else if(!DataUtil.stringIsNull(fireUrl)){
            //fireUrl="http://t7.youanmi.com:7123/style/H5Game/game.html?id=2124&shopId=1&userId=1&bisUrl=aHR0cDovLzE5Mi4xNjguMS4yNDoyODAwOC8=";
            btnShare.setVisibility(View.VISIBLE);
            layoutSave.setVisibility(View.GONE);

            loadUrl(fireUrl);
        }

    }

    String text="";
    boolean isFirstAdd = true;
    final class InJavaScriptLocalObj {
        @android.webkit.JavascriptInterface
        public void showSource(String html) {
            Log.e("-----","--showSource--");
            text=html;
            if(isFirstAdd)
            {
                isFirstAdd=false;
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        //text = text.replace("data-src", "src");//处理微信图片不显示
                        //webView.loadData(text, "text/html; charset=UTF-8", null);//这种写法可以正确解码
                        articlePreViewRequest=new ArticlePreViewRequest(text);
                        articlePreViewRequest.setOnResponseStateListener(articlePreViewResponseListener);
                        articlePreViewRequest.start(getActivity());
                    }
                });
            }

        }}

    private void loadUrl(String loadUrl){
        status=WEBVIEW_STATUS_NOMAL;
        loadingview.startLoading();
        webView.loadUrl(loadUrl);
    }

    public void goBack(){
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            getActivity().finish();
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_save:
                saveContent(null);
                break;

            case R.id.btn_share:
                if(shareDialog==null)
                    shareDialog=new ShareDialog();

                //分享地址为空保存并且分享，不为空直接分享
                if(shareUrl==null){


                    saveContent(new CallBack() {
                        @Override
                        public void onCall() {
                            shareUrl=saveArticleRequest.getShareUrl();
                            String shareImageUrl ="";
                            String shareTitle="";
                            String userAppName="";
                            if(!TextUtils.isEmpty(shareUrl)&&shareUrl.contains("shareImage=")){
                                String substring = saveArticleRequest.getShareUrl().replaceAll("(?is).*?shareImage=(.*?)&.*", "$1");
                                shareImageUrl=new String(android.util.Base64.decode(substring.getBytes(), android.util.Base64.DEFAULT));
                            }
                            if(!TextUtils.isEmpty(shareUrl)&&shareUrl.contains("title=")){
                                shareTitle = saveArticleRequest.getShareUrl().replaceAll("(?is).*?title=(.*?)&.*", "$1");
                            }
                            if(!TextUtils.isEmpty(shareUrl)&&shareUrl.contains("userAppName=")){
                                userAppName=saveArticleRequest.getShareUrl().substring(saveArticleRequest.getShareUrl().indexOf("userAppName="),saveArticleRequest.getShareUrl().length()-1);
                                userAppName=userAppName.replace("userAppName=","");
                            }

                            desc=articlePreViewRequest.title;
                            if(!TextUtils.isEmpty(userAppName)){
                                desc = ("【"+userAppName+"】"+desc);
                            }

                            int index= isChinese(shareUrl);
                            if(index!=000){
                                shareUrl= shareUrl.substring(0,index);
                            }



                            shareDialog.show(shareTitle,desc, shareUrl.replace("isShare=0","isShare=1"), shareImageUrl, new ShareListener() {
                                @Override
                                public void onComplete(Object var1) {
                                    ViewUtils.showToast("分享成功!");
                                    getActivity().finish();
                                }

                                @Override
                                public void onFail(int errorCode) {
                                    ViewUtils.showToast("分享失败!");
                                }
                            });
                        }
                    });
                }else {
                    int index= isChinese(shareUrl);
                    if(index!=000){
                        shareUrl= shareUrl.substring(0,index);
                    }
                    if(TextUtils.isEmpty(desc)){
                        shareDialog.show(desc,shareUrl.replace("isShare=0","isShare=1"),shareImageUrl,null);
                    }else {
                        shareDialog.show(title,desc,shareUrl.replace("isShare=0","isShare=1"),shareImageUrl,null);
                    }
                }
                break;

            default:
                break;

        }

    }


    private void connectUrl(final String url, final MResponseListener listener){
      new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  HttpURLConnection httpURLConnection = null;
                  httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                  httpURLConnection.setRequestMethod("GET");
                  httpURLConnection.setConnectTimeout(3000);
                  httpURLConnection.setReadTimeout(3000);
                  httpURLConnection.connect();

                  int responseCode = httpURLConnection.getResponseCode();
                  if (responseCode < 200 || responseCode >= 300) {
                      if(listener!=null)
                          listener.onFail();
                      throw new AppException("response error !!!");
                  }else {
                      if(listener!=null)
                          listener.onOK();
                  }
              }catch (Exception e){
                  e.printStackTrace();
                  listener.onFail();
              }
          }
      }).start();
    }


    /**保存成功并且分享就传入callBack**/
    private void saveContent(final CallBack callBack){
        if(saveArticleRequest==null){
            saveArticleRequest=new SaveArticleRequest(articlePreViewRequest.title,articlePreViewRequest.fireUrl);
            saveArticleRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    shareUrl=saveArticleRequest.getShareUrl();
                    if(articleRefreshCallBack !=null)
                        articleRefreshCallBack.onCall(null);

                    if(callBack!=null){
                        callBack.onCall();
                        layoutSave.setVisibility(View.GONE);
                    }else {
                        ViewUtils.showToast("文章保存成功！");
                        getActivity().finish();
                    }

                    //清空剪贴板
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData newData=ClipData.newPlainText("","");
                    cm.setPrimaryClip(newData);
                }

                @Override
                public void onFail(int code) {

                    ViewUtils.showToast("文章保存失败！");
                }
            });

        }


        saveArticleRequest.start();
    }

    private void doShare(){

    }


    @Override
    protected void initView() {
    }

    public void setArticleRefreshCallBack(ParameCallBack articleRefreshCallBack) {
        this.articleRefreshCallBack = articleRefreshCallBack;
    }

    @Override
    protected int layoutId() {
        return R.layout.view_h5;
    }

     class ArticlePreViewRequest extends  BaseRequest{
        String articleReviewUrl;
        String fireUrl;
        String title;
        String h5Host;
         public ArticlePreViewRequest(String content)
         {

             addParams("content",content);
             addParams("shopId",Account.user.getShopId());
         }

         @Override
         protected boolean isShowLoadingDialog() {
             return true;
         }

         @Override
        protected void fire(String data) throws AppException {
            try {
                if(!DataUtil.stringIsNull(data)){
                    JSONObject jsonObject=new JSONObject(data);
                    articleReviewUrl=jsonObject.optString("articleReviewUrl");
                    fireUrl=jsonObject.optString("fileUrl");
                    title=jsonObject.optString("title");
                    h5Host=jsonObject.optString("h5Host");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        protected String method() {
            return "makeFans/custome/article/review";
        }
    }


    class SaveArticleRequest extends  BaseRequest{
        private  String shareUrl;

        public SaveArticleRequest(String title,String fireUrl){
            addParams("articleUrl",wxUrl);
            addParams("shopId",Account.user.getShopId());
            addParams("userId",Account.user.getId());
            addParams("title",title);
            addParams("fileUrl",fireUrl);
        }

        @Override
        protected void fire(String data) throws AppException {
            setShareUrl(data);
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        @Override
        protected boolean isShowLoadingDialog() {
            return true;
        }

        @Override
        protected String method() {
            return "makeFans/custome/article/save";
        }
    }


    public interface MResponseListener {
        public void onOK();

        public void onFail();
    }

    public int isChinese(String str) {
        if (str == null)
            return 000;
        int i=0;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                return i;// 有一个中文字符就返回
            }else {
                i++;
                continue;
            }
        }
        return 000;
    }

    // 判断一个字符是否是中文
    public boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }



}
