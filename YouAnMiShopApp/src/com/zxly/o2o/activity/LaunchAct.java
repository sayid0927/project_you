package com.zxly.o2o.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.request.AdQueryRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.VersionCheckRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 *     @author dsnx  @version 创建时间：2015-1-4 上午10:29:10    类说明: 用于应用启动逻辑处理
 */
public class LaunchAct extends BasicAct {
    private Bundle extra_bundle;
    private TextView txtUpdatePercent,btnSkip;
    private View llAdView,llLaunchView;
    private ImageView imgAd;
    private  int exhibitionTime;//倒计时
    private String linkUrl;//广告连接地址;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_launch);

//        PushManager.getInstance().initialize(LaunchAct.this.getApplicationContext());
        txtUpdatePercent = (TextView) findViewById(R.id.txt_update_percent);
        extra_bundle = getIntent().getBundleExtra("extra_bundle");
        llAdView=findViewById(R.id.ll_ad);
        llLaunchView=findViewById(R.id.ll_launch);
        btnSkip= (TextView) llAdView.findViewById(R.id.btn_skip);
        imgAd= (ImageView) llAdView.findViewById(R.id.img_ad);
        Account.user = Account.readLoginUser(this);

//        MobclickAgent.updateOnlineConfig(this);
//        AnalyticsConfig.enableEncrypt(true);

        ViewUtils.setImmerseLayout(getWindow());
        checkNewVersion();

    }

    private void checkNewVersion() {
//        txtUpdatePercent.setText("正在检测版本.....");
        txtUpdatePercent.setText("");
        final VersionCheckRequest versionCheckRequest = new VersionCheckRequest(new ParameCallBack() {
            @Override
            public void onCall(Object object) {

                txtUpdatePercent.setText("正在为您下载最新版本请稍候(" + object + "%)");
            }
        });
        versionCheckRequest
                .setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

                    @Override
                    public void onOK() {

//                        toLoginOrHome();
                        if(versionCheckRequest.isInstallApp)
                        {
                            finish();
                        }else
                        {
                            initAdInfo();
                        }


                    }

                    @Override
                    public void onFail(int code) {

//                        toLoginOrHome();

                        initAdInfo();

                    }
                });
        versionCheckRequest.start(this);
//        AppController.getInstance().initHXAccount(Account.user,false);
//        Intent intent = new Intent(LaunchAct.this, MainActivity.class);
//        startActivity(intent);
    }
    private void initAdInfo()
    {
        if(!PreferUtil.getInstance().getIsFirstOpen())
        {
            final AdQueryRequest adQueryRequest=new AdQueryRequest();
            adQueryRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {

                    if(TextUtils.isEmpty(adQueryRequest.getImage()))
                    {
                        toLoginOrHome();
                    }else
                    {
                        if(adQueryRequest.getBitmap()!=null)
                        {
                            imgAd.setImageBitmap(adQueryRequest.getBitmap());
                            AlphaAnimation alphaInAnOut= new AlphaAnimation(1.0f,0.0f);
                            alphaInAnOut.setDuration(900);
                            alphaInAnOut.setAnimationListener(new Animation.AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    ViewUtils.setGone(llLaunchView);
                                    llLaunchView.clearAnimation();
                                }
                            });

                            llLaunchView.startAnimation(alphaInAnOut);
                            exhibitionTime=adQueryRequest.getExhibitionTime();
                            linkUrl=adQueryRequest.getLinkUrl();
                            skipCountDown();
                            llAdView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!StringUtil.isNull(linkUrl))
                                    {
                                        exhibitionTime=-1;
                                        H5CommonAct.start(LaunchAct.this,linkUrl,"柚安米",1);

                                    }


                                }
                            });
                            btnSkip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    exhibitionTime=-1;
                                    toLoginOrHome();
                                }
                            });

                        }else
                        {
                            toLoginOrHome();
                        }

                    }
                }

                @Override
                public void onFail(int code) {
                    toLoginOrHome();
                }
            });
            adQueryRequest.start();
        }else
        {
            toLoginOrHome();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
    }

    private void skipCountDown()
    {
        if(exhibitionTime>0)
        {
            btnSkip.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exhibitionTime--;
                    skipCountDown();
                }
            },1000);
            ViewUtils.setText(btnSkip,"跳过"+exhibitionTime);
        }else if(exhibitionTime==0)//倒计时为零的时候自动跳转
        {
            toLoginOrHome();
        //    Toast.makeText(this,"。。。。",Toast.LENGTH_SHORT);
        }

    }
    private void toLoginOrHome() {

        if(PreferUtil.getInstance().getIsFirstOpenApp()){
            Intent intent = new Intent(LaunchAct.this, ShopGuideAct.class);
            ViewUtils.startActivity(intent, LaunchAct.this);
            finish();
        }else {
            if (Account.user != null) {
                AppController.getInstance().initHXAccount(Account.user,false);
                Intent intent = new Intent(LaunchAct.this, MainActivity.class);
                intent.putExtra("extra_bundle",extra_bundle);
                ViewUtils.startActivity(intent, this);
                finish();
            } else {
                LoginAct.start(LaunchAct.this);
                finish();
            }
        }
        // 事件埋点
        UmengUtil.onEvent(LaunchAct.this, "overall_open",null);
    }

    @Override
    public void onBackPressed() {
        exhibitionTime=-1;
        finish();
    }
}
