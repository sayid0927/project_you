package com.zxly.o2o.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chatuidemo.HXConstant;
import com.igexin.sdk.PushManager;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.AppOpenedNoticeRequest;
import com.zxly.o2o.request.AppStartRequest;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.CodeByWireRequest;
import com.zxly.o2o.request.GetSubjectRequest;
import com.zxly.o2o.request.LoginToHXRequest;
import com.zxly.o2o.request.VersionCheckRequest;
import com.zxly.o2o.service.RunHeatbeatService;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.NetworkUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.RunHeatbeatUtils;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 *     @author dsnx  @version 创建时间：2015-1-4 上午10:29:10    类说明: 用于应用启动逻辑处理
 */
public class LaunchAct extends BasicAct {
    private TextView txtUpdatePercent;
    private String delPackageName;
    private Bundle extra_bundle;
    private ImageView imgAdvert;
    private View launchView;
    private int showTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.win_launch);
        extra_bundle = getIntent().getBundleExtra("extra_bundle");
        PushManager.getInstance().initialize(this.getApplicationContext());
        txtUpdatePercent = (TextView) findViewById(R.id.txt_update_percent);
        imgAdvert= (ImageView) findViewById(R.id.img_advert);
        launchView=findViewById(R.id.launchView);
//        MobclickAgent.updateOnlineConfig(this);
//        AnalyticsConfig.enableEncrypt(true);
        initApp();
        settingPromotionCode();
    }

    private void initApp() {
        if (NetworkUtil.isWifi(this) && NetworkUtil.isBleasantWifiNet(this)) {
            AppOpenedNoticeRequest appOpenedNoticeRequest = new AppOpenedNoticeRequest();

            appOpenedNoticeRequest.setOnResponseStateListener(new ResponseStateListener() {
                @Override
                public void onOK() {
                    initHXAccount();
                    checkNewVersion();
                }

                @Override
                public void onFail(int code) {
                   // initHXAccount();
                    checkNewVersion();
                }
            });
            appOpenedNoticeRequest.start();
            RunHeatbeatUtils.startPollingService(this, RunHeatbeatService.class, 1000 * 60 * 5);
        } else {
            initHXAccount();
            checkNewVersion();
        }
    }

    /**
     * 检查环境是否有老版本
     */

    private void checkNewVersion() {
        boolean isUpdate = true;
        long lastUpdateTime = PreferUtil.getInstance().getLastUpdateApkTime();
        if (lastUpdateTime > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd");
            Date date = new Date(lastUpdateTime);
            int upDay = Integer.parseInt(formatter.format(date));
            GregorianCalendar calendar = new GregorianCalendar();
            int curDay = calendar.get(Calendar.DAY_OF_MONTH);
            if (upDay == curDay) {
                isUpdate = false;
            }
        }

        if (isUpdate) {
            txtUpdatePercent.setText("正在检测版本.....");
            VersionCheckRequest versionCheckRequest = new VersionCheckRequest(new ParameCallBack() {
                @Override
                public void onCall(Object object) {

                    txtUpdatePercent.setText("正在为您下载最新版本请稍候(" + object + "%)");
                }
            });
            versionCheckRequest.setOnResponseStateListener(new ResponseStateListener() {

                        @Override
                        public void onOK() {
                            PreferUtil.getInstance().setLastUpdateApkTime(System.currentTimeMillis());
                            toHome();
                        }

                        @Override
                        public void onFail(int code) {
                            if (code == VersionCheckRequest.exit_app_code) {
                                LaunchAct.this.finish();
                                return;
                            }
                            toHome();
                        }
                    });
            versionCheckRequest.start(this);
        } else {
            toHome();
        }

    }

    private void toHome() {
        final AppStartRequest appStartRequest = new AppStartRequest();
        appStartRequest.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                initAdvert();
            }

            @Override
            public void onFail(int code) {
                initAdvert();
            }

        });

        appStartRequest.start();
    }


    private void startHomeAct() {
        if (PreferUtil.getInstance().getIsFirstOpen()) {
            Intent intent = new Intent(LaunchAct.this, UserGuideAct.class);
            ViewUtils.startActivity(intent, LaunchAct.this);
        } else {
            Intent intent = new Intent(LaunchAct.this, HomeAct.class);
            intent.putExtra("extra_bundle",extra_bundle);
            ViewUtils.startActivity(intent, LaunchAct.this);
        }
        finish();
    }
    private void initAdvert()
    {
        final GetSubjectRequest getSubjectRequest = new GetSubjectRequest();
        getSubjectRequest.setCallBack(new ParameCallBack() {
            @Override
            public void onCall(final Object object) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        boolean isExistsImg = (Boolean) object;
                        Log.e("--isExistsImg--",isExistsImg+"-->");
                        if (isExistsImg) {
                            showTime=getSubjectRequest.getShowTime();
                            imgAdvert.setImageBitmap(getSubjectRequest.getBitmap());
                            AlphaAnimation alphaInAnOut = new AlphaAnimation(1.0f, 0.0f);
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
                                    ViewUtils.setGone(launchView);
                                    launchView.clearAnimation();
                                }
                            });
                            launchView.startAnimation(alphaInAnOut);
                            skipCountDown();
                        } else {
                            startHomeAct();
                        }
                    }
                });

            }
        });
        getSubjectRequest.start();
    }
    private void skipCountDown()
    {
        if(showTime>0)
        {
            launchView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showTime--;
                    skipCountDown();
                }
            },1000);
            //ViewUtils.setText(btnSkip,"跳过"+exhibitionTime);
        }else if(showTime==0)//倒计时为零的时候自动跳转
        {
            startHomeAct();

        }

    }
    private void settingPromotionCode() {
        if (StringUtil.isNull(Config.promotionCode)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    File file = new File(Environment.getExternalStorageDirectory(), Constants.PARNET_PATH + "/agent" +
                            ".txt");
                    if (file.exists()) {
                        String res = "";
                        try {
                            FileInputStream fin = new FileInputStream(file);
                            int length = fin.available();
                            byte[] buffer = new byte[length];
                            fin.read(buffer);
                            res = new String(buffer, "utf-8");
                            fin.close();
                            if (!StringUtil.isNull(res)) {
                                String[] info = res.split(",");
                                String sn = info[0];
                                String serialNum = info[1];
                                new CodeByWireRequest(sn, serialNum).start();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            thread.start();

        }
    }


    /**
     * 判断是否有注册：注册返回1和2，没注册返回-1
     */
    private void initHXAccount() {
        Account.user = Account.readLoginUser(this);
        int type = PreferUtil.getInstance().getHXAccount();
        if (type == -1) {
            final LoginToHXRequest loginToHXRequest = new LoginToHXRequest();   //注册游客帐号
            loginToHXRequest.setOnResponseStateListener(new ResponseStateListener() {
                @Override
                public void onOK() {
                    //环信游客帐号
                    PreferUtil.getInstance().setHXAccount(1);
                    AppController.getInstance().loginHxVisitorAccount();
                }

                @Override
                public void onFail(int code) {
                }
            });
            loginToHXRequest.start();
        } else if (type == 1) { //环信游客帐号
            AppController.getInstance().loginHxVisitorAccount();
        } else { //环信正式帐号
            //聊天界面只要有登录我们的后台就可以进去，不用先进行环信的账号登录
            if (Account.user != null) {
                HXConstant.isLoginSuccess = true; //标识登录hx成功
                AppController.getInstance().doLoginHX(Account.user, false);
            } else {
                HXConstant.isLoginSuccess = false; //标识游客登录hx成功
            }
        }

    }


}
