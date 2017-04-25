/*
 * 文件名：SecondMainAct.java
 * 版权：Copyright 2014 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SecondMainAct.java
 * 修改人：huangbin
 * 修改时间：2014-12-20
 * 修改内容：新增
 */
package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.zxly.o2o.fragment.ForgetPasswordChangeFragment;
import com.zxly.o2o.fragment.ForgetPasswordFragment;
import com.zxly.o2o.fragment.ForgetPasswordMessageFragment;
import com.zxly.o2o.fragment.LoginFragment;
import com.zxly.o2o.fragment.RegisterFragment;
import com.zxly.o2o.fragment.RegisterMessageFragment;
import com.zxly.o2o.fragment.RegisterSettingFragment;
import com.zxly.o2o.model.SMSMessage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ViewUtils;

public class LoginAct extends BasicAct {
    public static final int SHOW_DEFAULT = 100;
    public static final int FORGET_PASSWORD = 101;

    public static final int FORGET_PASSWORD_MESSAGE = 102;
    public static final int FORGET_PASSWORD_SETTING = 103;
    public static final int LOGIN_FINISHED = 104;
    public static final int REGISTER = 201;
    public static final int REGISTER_MESSAGE = 202;
    public static final int REGISTER_SETTING = 203;
    private LoginFragment mLoginFragment;
    private ForgetPasswordFragment mForgetPasswordFragment;
    private ForgetPasswordMessageFragment mForgetPasswordMessageFragment;
    private ForgetPasswordChangeFragment mForgetPasswordChangeFragment;
    private RegisterFragment mRegisterFragment;
    private RegisterMessageFragment mRegisterMessageFragment;
    private RegisterSettingFragment mRegisterSettingFragment;
    private static CallBack callBack;
    public String promotionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_fragment_activity_main);

        initView();
    }

    protected void handleMessage(Message msg) {
        boolean b;
        switch (msg.what) {
            case LOGIN_FINISHED:
                setResult(RESULT_OK);

                /*从推送栏调到登录界面并登录成功后跳到聊天界面*/
                if (getIntent().getBooleanExtra("goToSysMsg", false)) {
                    String userId = getIntent().getStringExtra("userId");
                    Intent intent = new Intent(this, ChatActivity.class);

                    intent.putExtra("chatType", HXConstant.CHATTYPE_SINGLE);
                    intent.putExtra("userId", userId);

                    startActivity(intent);
                }

                if (callBack != null) {
                    callBack.onCall();
                }
                finish();
                break;
            case SHOW_DEFAULT:
                if ("changePw".equals(getIntent().getStringExtra("source"))) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    replaceFragment(R.id.fragment_main, mLoginFragment);
                }
                break;
            case FORGET_PASSWORD:
                replaceFragment(R.id.fragment_main, mForgetPasswordFragment);
                break;
            case FORGET_PASSWORD_MESSAGE:
                b = replaceFragment(R.id.fragment_main, mForgetPasswordMessageFragment);
                if (b) {
                    mForgetPasswordMessageFragment.obtainMessage(Constants.REFRESH, msg.obj).sendToTarget();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SMS", (SMSMessage) msg.obj);
                    mForgetPasswordMessageFragment.setArguments(bundle);
                }
                break;
            case FORGET_PASSWORD_SETTING:
                b = replaceFragment(R.id.fragment_main, mForgetPasswordChangeFragment);
                if (b) {
                    mForgetPasswordChangeFragment.obtainMessage(Constants.REFRESH, msg.obj).sendToTarget();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SMS", (SMSMessage) msg.obj);
                    mForgetPasswordChangeFragment.setArguments(bundle);
                }
                break;
            case REGISTER:
                replaceFragment(R.id.fragment_main, mRegisterFragment);
                break;
            case REGISTER_MESSAGE:
                b = replaceFragment(R.id.fragment_main, mRegisterMessageFragment);
                if (b) {
                    mRegisterMessageFragment.obtainMessage(Constants.REFRESH, msg.obj).sendToTarget();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SMS", (SMSMessage) msg.obj);
                    mRegisterMessageFragment.setArguments(bundle);
                }
                break;
            case REGISTER_SETTING:
                b = replaceFragment(R.id.fragment_main, mRegisterSettingFragment);
                if (b) {
                    mRegisterSettingFragment.obtainMessage(Constants.REFRESH, msg.obj).sendToTarget();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("SMS", (SMSMessage) msg.obj);
                    mRegisterSettingFragment.setArguments(bundle);
                }
                break;
        }
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, LoginAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, CallBack _callBack) {
        callBack = _callBack;
        Intent intent = new Intent(curAct, LoginAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    public void finish() {
        super.finish();
        callBack = null;
    }

    private void initView() {
        mRegisterFragment = new RegisterFragment();
        mRegisterMessageFragment = new RegisterMessageFragment();
        mRegisterSettingFragment = new RegisterSettingFragment();
        mForgetPasswordChangeFragment = new ForgetPasswordChangeFragment();
        mForgetPasswordMessageFragment = new ForgetPasswordMessageFragment();
        mForgetPasswordFragment = new ForgetPasswordFragment();
        mLoginFragment = new LoginFragment();
        if ("changePw".equals(getIntent().getStringExtra("source"))) {
            Bundle bundle = new Bundle();
            bundle.putString("source", "changePw");
            mForgetPasswordFragment.setArguments(bundle);
            replaceFragment(R.id.fragment_main, mForgetPasswordFragment);
        } else {
            // 先记录下各组件的目前状态,登录成功后才保存

            replaceFragment(R.id.fragment_main, mLoginFragment);
        }
    }

}
