package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CommonShareRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.EncodingHandler;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

import static com.zxly.o2o.shop.R.id.btn_tuiguang;

/**
 * Created by dsnx on 2016/9/13.
 */
public class QrCodePromotionAct extends BasicAct implements View.OnClickListener{
    private TextView txtMyCode,txtHint;
    private ImageView imgQrCode;
    private View btnTuiguang,btnBack;
    private ShareDialog shareDialog;
    private String shareUrl, h5HomeURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_qr_code_promotion);
        txtMyCode= (TextView) findViewById(R.id.txt_myCode);
        imgQrCode= (ImageView) findViewById(R.id.img_qr_code);
        txtHint= (TextView) findViewById(R.id.txt_hint);
        btnTuiguang=findViewById(btn_tuiguang);
        btnBack=findViewById(R.id.btn_back);
        txtHint.setText("  记得提醒用户注册时请输入我的推广码："+ Account.user.getPromotionCode());
        String myInfo="我的推广码："+Account.user.getPromotionCode();
        SpannableString ss1=new SpannableString(myInfo);
        ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#ff5f19")), 6, myInfo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtMyCode.setText(ss1);
//        StringBuilder sb=new StringBuilder(Config.shareUrl);
//        sb.append("sys/dll?shopId=").append(Account.user.getShopId()).append("&appType=2").append("&userId=").append( Account.user.getId());
//        shareUrl=sb.toString();
//        generateQRCode(shareUrl);
        btnBack.setOnClickListener(this);
        btnTuiguang.setOnClickListener(this);
        findViewById(R.id.btn_share_h5).setOnClickListener(this);

        loadShareUrl();
    }

    private void loadShareUrl(){
        final CommonShareRequest commonShareRequest = new CommonShareRequest();
        commonShareRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                shareUrl = commonShareRequest.getShareUrl();
                h5HomeURL = commonShareRequest.getH5HomeURL();
                if(!StringUtil.isNull(shareUrl)) {
                    generateQRCode(shareUrl);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        commonShareRequest.start(this);
    }

    private void generateQRCode(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = DesityUtil.dp2px(QrCodePromotionAct.this, 140);
                final Bitmap bitmap = EncodingHandler.createQRImage(url, size, size);
                imgQrCode.post(new Runnable() {
                    @Override
                    public void run() {
                        imgQrCode.setImageBitmap(bitmap);
                    }
                });

            }
        }).start();
    }
    public static void start(Activity curAct)
    {
        Intent intent=new Intent();
        intent.setClass(curAct, QrCodePromotionAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case btn_tuiguang:
                if (StringUtil.isNull(shareUrl)) {
                    ViewUtils.showToast("推广地址为空");
                    return;
                }
                if (shareDialog==null)
                    shareDialog=new ShareDialog();

                String title = "客官，恭喜您获得本店特权大礼包！点击下载带店回家、立享特权、一键购物、轻松赚钱！";
                shareDialog.show(Account.user.getShopName(), title, shareUrl, "", new ShareListener() {
                    @Override
                    public void onComplete(Object var1) {
                        ViewUtils.showToast("发送成功");


                    }

                    @Override
                    public void onFail(int errorCode) {
                    }
                });

                UmengUtil.onEvent(QrCodePromotionAct.this,new UmengUtil().FIND_SHARE_APP_CLICK,null);
                break;
            case R.id.btn_share_h5:
                if (StringUtil.isNull(h5HomeURL)) {
                    ViewUtils.showToast("H5网店地址为空");
                    return;
                }
                if (shareDialog==null)
                    shareDialog=new ShareDialog();

                shareDialog.show(Account.user.getShopName(), "我发现好多商品的实时优惠，客官快点击我吧！", h5HomeURL, "", new ShareListener() {
                    @Override
                    public void onComplete(Object var1) {
                        ViewUtils.showToast("发送成功");
                    }

                    @Override
                    public void onFail(int errorCode) {
                    }
                });
                UmengUtil.onEvent(QrCodePromotionAct.this,new UmengUtil().FIND_SHARE_H5_CLICK,null);
                    break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
