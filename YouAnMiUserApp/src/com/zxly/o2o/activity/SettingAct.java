package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PushUnbindRequest;
import com.zxly.o2o.request.VersionDetectRequest;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PhoneUtil;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-1-23
 * @description 设置界面
 */
public class SettingAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private Dialog dialog;
    private CheckBox cbxPushMsg;
    private TextView txtVersionTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_setting);
        context = this;
        initViews();
        loadData();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, SettingAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        dialog = new Dialog(this, R.style.dialog);
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("设置");
        findViewById(R.id.btn_delivery_address)
                .setOnClickListener(this);
        findViewById(R.id.btn_security_center)
                .setOnClickListener(this);
        findViewById(R.id.btn_change_login_pwd).setOnClickListener(this);
        findViewById(R.id.btn_change_pay_pwd).setOnClickListener(this);

        cbxPushMsg = (CheckBox) findViewById(R.id.cbx_push_msg);
        cbxPushMsg.setChecked(PreferUtil.getInstance().getMsgPush());
        cbxPushMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferUtil.getInstance().setMsgPush(isChecked);
            }
        });
        findViewById(R.id.btn_shop_desc)
                .setOnClickListener(this);
        findViewById(R.id.btn_about_us)
                .setOnClickListener(this);
        findViewById(R.id.btn_feedback)
                .setOnClickListener(this);
        txtVersionTip = (TextView) findViewById(R.id.txt_version_tip);
        findViewById(R.id.btn_logout).setOnClickListener(this);

//        if (Config.serverShopversionNo > Config.shopVersionCode || Config.versionCodeCur > AppController.mVersionCode) {
//            ViewUtils.setVisible(txtVersionTip);
//            ViewUtils.setText(txtVersionTip, "发现新版本 V" + Config.serverVersionName);
//        }
        findViewById(R.id.btn_service_phone)
                .setOnClickListener(this);
    }
    
    private void loadData(){
        VersionDetectRequest versionCheckRequest = new VersionDetectRequest(new ParameCallBack() {
            @Override
            public void onCall(Object object) {

//                txtUpdatePercent.setText("正在为您下载最新版本请稍候(" + object + "%)");
            }
        });
        versionCheckRequest
                .setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

                    @Override
                    public void onOK() {
                        PreferUtil.getInstance().setLastUpdateApkTime(System.currentTimeMillis());

                        if (Config.serverShopversionNo > Config.shopVersionCode || Config.versionCodeCur > AppController.mVersionCode) {
                            ViewUtils.setVisible(txtVersionTip);
                            ViewUtils.setText(txtVersionTip, "发现新版本 V" + Config.serverVersionName);
                        }

                    }

                    @Override
                    public void onFail(int code) {
                    }
                });
        versionCheckRequest.start(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_delivery_address:
                SettingAddressAct.start(SettingAct.this, "manage");
                UMengAgent.onEvent(this, UMengAgent.personal_setting_address);
                break;
            case R.id.btn_security_center:
                SecurityCenterAct.start(SettingAct.this);
                break;
            case R.id.btn_change_login_pwd:
                PersonalChangePasswordAct.start(SettingAct.this, "login");
                UMengAgent.onEvent(this, UMengAgent.personal_change_password);
                break;
            case R.id.btn_change_pay_pwd:
                PersonalChangePasswordAct.start(SettingAct.this, "pay");
                UMengAgent.onEvent(this, UMengAgent.personal_change_password);
                break;
            case R.id.btn_shop_desc:
                PersonalShopDescAct.start(SettingAct.this);
                break;
            case R.id.btn_about_us:
                PersonalAboutUsAct.start(SettingAct.this);
                UMengAgent.onEvent(this, UMengAgent.personal_setting_update);
                break;
            case R.id.btn_feedback:
                FeedbackAct.start(SettingAct.this);
                UMengAgent.onEvent(SettingAct.this, UMengAgent.personal_home_feedback);
                break;
            case R.id.btn_logout:
                createLogoutDialog();
                break;
            case R.id.btn_service_phone:
                PhoneUtil.openPhoneKeyBord("4008077067", context);
                break;
        }
    }

    private void createLogoutDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_logout);
        dialog.findViewById(R.id.logout_done)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        pushUnBind();

                        UMengAgent.onEvent(SettingAct.this, UMengAgent.personal_setting_logout);
                    }
                });
        dialog.findViewById(R.id.logout_cancel)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void pushUnBind(){
        PushUnbindRequest pushUnbindRequest = new PushUnbindRequest();
        pushUnbindRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                Account.orderCount = 0;
                SettingAct.this.deleteFile("user");
                Account.user = null;
                PreferUtil.getInstance().setLoginToken("");

                HXConstant.isLoginSuccess = false;
                HXHelper.getInstance().logout(true, null);//退出环信
                HXHelper.getInstance().cleanYAMContactList();  //清空本地缓存
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HXHelper.getInstance().deleteDBContactCache();  //清空本地联系人
                        LoginAct.start(SettingAct.this);
                        PreferUtil.getInstance().setHXAccount(1); //设置为游客状态
                        AppController.getInstance().loginHxVisitorAccount();//登录游客账号
                        SettingAct.this.finish();
                    }
                }).start();
            }

            @Override
            public void onFail(int code) {

            }
        });
        pushUnbindRequest.start(this);
    }


}
