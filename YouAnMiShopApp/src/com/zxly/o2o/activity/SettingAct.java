package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.utils.PreferenceManager;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PushUnbindRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

public class SettingAct extends BasicAct implements
        View.OnClickListener {
    private Dialog dialog;
    private TextView txtVersionNew;
    private static CallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_setting);
        initViews();
    }

    public static void start(Activity curAct, CallBack _callBack) {
        callBack = _callBack;
        Intent intent = new Intent(curAct, SettingAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "设置");
        findViewById(R.id.btn_security_center).setOnClickListener(this);
        findViewById(R.id.btn_notification_settings).setOnClickListener(this);
        findViewById(R.id.btn_clear_data).setOnClickListener(this);

        findViewById(R.id.btn_about_us).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        txtVersionNew = (TextView) findViewById(R.id.txt_version_new);
        if (Config.serVerCode > AppController.mVersionCode) {
            ViewUtils.setText(txtVersionNew, "新版本: V" + Config.serVerName);
            ViewUtils.setVisible(txtVersionNew);
        } else {
            ViewUtils.setGone(txtVersionNew);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void finish() {
        super.finish();
        callBack = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_security_center:
                SecurityCenterAct.start(SettingAct.this);

                break;
            case R.id.btn_notification_settings:
                NotificationSettingsAct.start(SettingAct.this);
                break;
            case R.id.btn_clear_data:
                ClearDataAct.start(SettingAct.this);

                UmengUtil.onEvent(SettingAct.this, new UmengUtil().SETTING_CLEARDATA_CLICK, null);
                break;
            case R.id.btn_about_us:
                AboutUsAct.start(SettingAct.this);
                UmengUtil.onEvent(SettingAct.this, new UmengUtil().SETTING_ABOUT_CLICK, null);
                break;
            case R.id.btn_logout:
                createLogoutDialog();
                UmengUtil.onEvent(SettingAct.this, new UmengUtil().SETTING_LOGOUT_CLICK, null);
                break;

        }
    }

    private void createLogoutDialog() {
        if (dialog == null) {
            dialog = new Dialog(SettingAct.this, R.style.dialog);
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_logout);
        dialog.findViewById(R.id.btn_done)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        pushUnBind();
                        UmengUtil.onEvent(SettingAct.this, new UmengUtil().SETTING_LOGOUT_CONFIRM_CLICK, null);

                    }
                });
        dialog.findViewById(R.id.btn_cancel)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            UmengUtil.onEvent(SettingAct.this, new UmengUtil().SETTING_LOGOUT_CANCEL_CLICK, null);
                        }
                    }
                });
    }

    private void pushUnBind() {
        PushUnbindRequest pushUnbindRequest = new PushUnbindRequest();
        pushUnbindRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                SettingAct.this.deleteFile("user");
                Account.user = null;
                PreferUtil.getInstance().setLoginToken("");
                LoginAct.start(SettingAct.this);
                PreferenceManager.getInstance().cleanTopContacts();
                callBack.onCall();
                SettingAct.this.finish();
                HXHelper.getInstance().logout(true, null); //退出环信
                HXHelper.getInstance().deleteDBContactCache();  //清空本地联系人
                HXHelper.getInstance().cleanYAMContactList();  //清空本地缓存
            }

            @Override
            public void onFail(int code) {

            }
        });
        pushUnbindRequest.start(this);
    }
}
