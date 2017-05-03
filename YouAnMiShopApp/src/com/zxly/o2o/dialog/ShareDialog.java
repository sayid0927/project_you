package com.zxly.o2o.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.shop.AccessTokenKeeper;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.shop.WBAuthActivity;
import com.zxly.o2o.shop.WBShareActivity;
import com.zxly.o2o.shop.wxapi.Util;
import com.zxly.o2o.shop.wxapi.WXEntryActivity;
import com.zxly.o2o.util.AppUtil;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.MyImageManager;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by dsnx on 2015/7/14.
 */
public class ShareDialog extends BaseDialog implements View.OnClickListener{
    private static final int  SHARE_CANCEL=-1;
    private static final int  SHARE_TO_QQ=0;
    private static final int  SHARE_TO_QQ_ZOOM=1;
    private static final int  SHARE_TO_WX=2;
    private static final int  SHARE_TO_WX_ZOOM=3;
    private static final int  SHARE_TO_WB=4;
    private GridView gridView;
    private View btnCancel;
    private String[] shareIcon;
    private String[] shareName;
    private String url ,title;
    private Tencent mTencent;
    private ShareListener shareListener;
    private boolean isPublic;//是否分享到朋友圈, 或者QQ空间
    private Object imgInfo;
    private MyImageManager myImageManager;
    private String desc=" ";

    @Override
    protected void initView() {
        shareIcon=new String[]{"share_weixin_f","share_weixin","share_qq","share_qq_qz"};
        shareName=new String[]{"朋友圈","微信好友","QQ好友","QQ空间"};
        gridView= (GridView) findViewById(R.id.grid_view);
        ShareAdapter shareAdapter= new ShareAdapter(context);
        gridView.setAdapter(shareAdapter);
    }


    /**
     *
     * @param desc 如果只有标题就把标题放到内容的地方
     * @param targetUrl
     * @param image image 可以是一个地址 或者bitmap
     * @param shareListener
     */
    public void show(String desc,String targetUrl,Object image,ShareListener shareListener) {
        super.show();
        this.title=" ";
        this.desc=desc;
        this.url=targetUrl;
        this.imgInfo=image;
        this.shareListener=shareListener;
    }

    public void show(String title,String desc,String targetUrl,Object image,ShareListener shareListener) {
        super.show();
        this.title=title;
        this.desc=desc;
        this.url=targetUrl;
        this.imgInfo=image;
        this.shareListener=shareListener;
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_share;
    }

    @Override
    public void onClick(View v) {
        if(v==btnCancel)
        {
            dismiss();
        }
    }


    /**
     * 分享到QQ
    * */
    private  void doShareToQQ() {
        // QQ分享要在主线程做
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (mTencent == null) {
                    mTencent = Tencent.createInstance(Constants.QQ_SHARE_APP_ID, AppController.getInstance().getApplicationContext());
                }

                IUiListener qqShareListener = new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        if (shareListener != null) {
                            shareListener.onComplete(o);
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                        if (shareListener != null) {
                            shareListener.onFail(uiError.errorCode);
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (shareListener != null) {
                            shareListener.onFail(SHARE_CANCEL);
                        }
                    }
                };

                if (null != mTencent) {
                    Bundle params = new Bundle();
                    if (isPublic) {
                        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
                        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, desc);//选填
                        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
                        ArrayList<String> imageUrls = new ArrayList<String>();
                        if (!StringUtil.isNull((String) imgInfo)) {
                            imageUrls.add((String) imgInfo);
                        }else{
                            imageUrls.add("http://test.jpg"); // imageUrls  为空也会提示分享失败，构造一个无效的地址
                        }

                        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);

                        mTencent.shareToQzone(AppController.getInstance().getTopAct(), params, qqShareListener);
                    } else {
                        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);//分享到好友
                        if (!StringUtil.isNull((String) imgInfo)) {
                            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, (String) imgInfo);
                        }
                        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
                        params.putString(QQShare.SHARE_TO_QQ_TITLE,title);
                        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, desc);
                        mTencent.shareToQQ(AppController.getInstance().getTopAct(), params, qqShareListener);
                    }
                }
            }

        });

    }


    /**
     * 分享到微信
     * */
    private void doShareToWX(){

        WXEntryActivity.shareListener=shareListener;

        IWXAPI api = WXAPIFactory.createWXAPI(getContext(), Constants.WX_APP_ID, false);
        api.registerApp(Constants.WX_APP_ID);//注册到微信

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);

        if(isPublic){
            if(StringUtil.isNull(title)){
                msg.title =  desc;
                msg.description =title;
            }else {
                msg.title =  title;
                msg.description =desc;
            }
        }else{
            msg.description =  desc;
            msg.title =title;
        }

        if(imgInfo!=null){
            Bitmap thumb = (Bitmap) imgInfo;
            //Bitmap thumb = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher);
            msg.thumbData = Util.bmpToByteArray(thumb, true);
        }


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("wx_no");

        req.message = msg;
        req.scene = isPublic ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    /**
    * 分享到微博
    * */
    private void doShareToWB(){
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = desc;

        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        if(imgInfo!=null&&imgInfo instanceof Bitmap){
            mediaObject.setThumbImage((Bitmap) imgInfo);
        }else {
            Bitmap  bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher);
            mediaObject.setThumbImage(bitmap);
        }


        mediaObject.actionUrl = url;
        mediaObject.defaultText = "default";
        weiboMessage.mediaObject = mediaObject;

        WBShareActivity.start(getContext(), shareListener, weiboMessage);
    }


    /**
     * 控制share事件分发
     * 放线程里面应为微信图片是传二进制的，有些要从网络或者问IO读取...
     * **/
    private void doShare(final int shareType){
        myImageManager=MyImageManager.from(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp=null;
                switch (shareType){
                    case SHARE_TO_QQ:
                    case SHARE_TO_QQ_ZOOM:
                        if (shareType==SHARE_TO_QQ_ZOOM){
                            isPublic=true;
                        }else {
                            isPublic=false;
                        }

                        if(imgInfo instanceof Bitmap){
                            //bitmap 类型的先放到cache里面转换成本地可见地址再分享
                            File shareImg =new BitmapUtil().saveBitmapToFile((Bitmap) imgInfo,"test.jpg");
                            if(shareImg==null){
                                return;
                            }
                            imgInfo=shareImg.getPath();
                        }
                        doShareToQQ();//网络地址形式的可以直接分享
                        break;

                    case SHARE_TO_WX:
                    case SHARE_TO_WX_ZOOM:
                        if (shareType==SHARE_TO_WX_ZOOM){
                            isPublic=true;
                        }else {
                            isPublic=false;
                        }

                        if(imgInfo instanceof String){
                            //网络加载图片转换成bmp
                          //  bmp=myImageManager.getBirmapFromCache((String) imgInfo);//还没找到vollery缓存位置 暂时无效的
                            bmp=new BitmapUtil().getHttpBitmap((String) imgInfo); //如果是QQ分享可以直接用url 微信就要拉下来
                            imgInfo=bmp;
                        }

                        imgInfo=new BitmapUtil().getWxShareIcon((Bitmap) imgInfo);
                        doShareToWX();
                        break;


                    case SHARE_TO_WB:
                        if(imgInfo instanceof String){
                            //网络加载图片转换成bmp
                            //  bmp=myImageManager.getBirmapFromCache((String) imgInfo);//还没找到vollery缓存位置 暂时无效的
                            bmp=new BitmapUtil().getHttpBitmap((String) imgInfo); //如果是QQ分享可以直接用url 微信就要拉下来
                            imgInfo=bmp;
                        }

                     //   Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
                        Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(getContext());
                        if(mAccessToken.isSessionValid()){
                            doShareToWB();
                        }else{
                            WBAuthActivity.start(getContext(), new ShareListener() {
                                @Override
                                public void onComplete(Object var1) {
                                    doShareToWB();
                                }

                                @Override
                                public void onFail(int errorCode) {

                                }
                            });
                        }

                        break;

                    default:
                        break;

                }

            }
        }).start();

    }

    private  class  ShareAdapter extends ObjectAdapter{

        public ShareAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_share;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null)
            {
                convertView=inflateConvertView();
                holder=new ViewHolder();
                holder.itemIcon= (ImageView) convertView.findViewById(R.id.img_icon);
                holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }

            holder.itemIcon.setImageDrawable(AppController.getInstance().getDrawable(shareIcon[position]));
            holder.txtName.setText(shareName[position]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imgInfo instanceof String &&StringUtil.isNull((String) imgInfo))
                    {
                        imgInfo= Account.user.getAppIconUrl();
                    }else if(imgInfo==null)
                    {
                        imgInfo= Account.user.getAppIconUrl();
                    }
                    switch (position){
                        case 2:
                            if(AppUtil.getInstalledAPkVersion(context,"com.tencent.mobileqq")!=-1){
                                doShare(SHARE_TO_QQ);
                            }else {
                                ViewUtils.showToast("本地未安装QQ !");
                            }
                            break;

                        case 3:
                            if(AppUtil.getInstalledAPkVersion(context, "com.tencent.mobileqq")!=-1) {
                                doShare(SHARE_TO_QQ_ZOOM);
                            }else {
                                ViewUtils.showToast("本地未安装QQ !");
                            }
                            break;

                        case 1:
                            if(AppUtil.getInstalledAPkVersion(context,"com.tencent.mm")!=-1) {
                                doShare(SHARE_TO_WX);
                            }else {
                                ViewUtils.showToast("本地未安装微信 !");
                            }
                            break;

                        case 0:
                            if(AppUtil.getInstalledAPkVersion(context,"com.tencent.mm")!=-1) {
                                doShare(SHARE_TO_WX_ZOOM);
                            }else {
                                ViewUtils.showToast("本地未安装微信 !");
                            }
                            break;
                    }

                    dismiss();
                }
            });

            return convertView;
        }


        @Override
        public int getCount() {
            return shareIcon.length;
        }

        private class ViewHolder{
            ImageView itemIcon;
            TextView txtName;
        }
    }

}
