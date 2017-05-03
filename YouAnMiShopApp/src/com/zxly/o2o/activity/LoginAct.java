package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.zxly.o2o.application.Config;
import com.zxly.o2o.fragment.ForgetPasswordChangeFragment;
import com.zxly.o2o.fragment.ForgetPasswordFragment;
import com.zxly.o2o.fragment.ForgetPasswordMessageFragment;
import com.zxly.o2o.fragment.LoginFragment;
import com.zxly.o2o.model.SMSMessage;
import com.zxly.o2o.request.GetuiBindRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

public class LoginAct extends BasicAct {
    public static final int SHOW_DEFAULT = 100;
    public static final int LOGIN_FINISHED = 101;
    public static final int FORGET_PASSWORD = 102;
    public static final int FORGET_PASSWORD_MESSAGE = 103;
    public static final int FORGET_PASSWORD_SETTING = 104;
    private LoginFragment mLoginFragment;
    private ForgetPasswordFragment mForgetPasswordFragment;
    private ForgetPasswordMessageFragment mForgetPasswordMessageFragment;
    private ForgetPasswordChangeFragment mForgetPasswordChangeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_login_main);
        initViews();
    }

    protected void handleMessage(Message msg) {
        boolean b = false;
        switch (msg.what) {
            case LOGIN_FINISHED:
                Intent intent = new Intent(LoginAct.this, MainActivity.class);
                startActivity(intent);
                // 事件埋点
            //    MobclickAgent.onProfileSignIn( Config.getuiClientId);
                UmengUtil.onEvent( LoginAct.this, "login_suc",null);

                new GetuiBindRequest(Config.getuiClientId).start();
                finish();
                break;
            case SHOW_DEFAULT:
                replaceFragment(R.id.login_fragment_main, mLoginFragment);
                break;
            case FORGET_PASSWORD:
                replaceFragment(R.id.login_fragment_main, mForgetPasswordFragment);
                break;
            case FORGET_PASSWORD_MESSAGE:
                b = replaceFragment(R.id.login_fragment_main, mForgetPasswordMessageFragment);
                if (b) {
                    mForgetPasswordMessageFragment.obtainMessage(Constants.REFRESH, msg.obj).sendToTarget();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SMS", (SMSMessage) msg.obj);
                    mForgetPasswordMessageFragment.setArguments(bundle);
                }
                break;
            case FORGET_PASSWORD_SETTING:
                b = replaceFragment(R.id.login_fragment_main, mForgetPasswordChangeFragment);
                if (b) {
                    mForgetPasswordChangeFragment.obtainMessage(Constants.REFRESH, msg.obj).sendToTarget();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SMS", (SMSMessage) msg.obj);
                    mForgetPasswordChangeFragment.setArguments(bundle);
                }
                break;
        }
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, LoginAct.class);
        ViewUtils.startActivity(intent, curAct);

    }

    @Override
    public void finish() {


        super.finish();
    }

    private void initViews() {
        mLoginFragment = new LoginFragment();
        mForgetPasswordChangeFragment = new ForgetPasswordChangeFragment();
        mForgetPasswordMessageFragment = new ForgetPasswordMessageFragment();
        mForgetPasswordFragment = new ForgetPasswordFragment();
        replaceFragment(R.id.login_fragment_main, mLoginFragment);
    }
}
