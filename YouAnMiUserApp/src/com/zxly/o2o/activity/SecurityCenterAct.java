package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2016-5-24
 * @description 安全中心界面
 */
public class SecurityCenterAct extends BasicAct implements OnClickListener {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_security_center);
        context = this;
        initViews();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, SecurityCenterAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("安全中心");
        findViewById(R.id.btn_change_login_pwd).setOnClickListener(this);
        findViewById(R.id.btn_change_pay_pwd).setOnClickListener(this);
        findViewById(R.id.btn_set_mobile).setOnClickListener(this);
        findViewById(R.id.btn_id_auth).setOnClickListener(this);
        findViewById(R.id.btn_bankcard_manage).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_change_login_pwd:
                PersonalChangePasswordAct.start(SecurityCenterAct.this, "login");
                UMengAgent.onEvent(this, UMengAgent.personal_change_password);
                break;
            case R.id.btn_change_pay_pwd:
                PersonalChangePasswordAct.start(SecurityCenterAct.this, "pay");
                UMengAgent.onEvent(this, UMengAgent.personal_change_password);
                break;
            case R.id.btn_set_mobile:
                VerifyMobileAct.start(SecurityCenterAct.this);
                break;
            case R.id.btn_id_auth:
                VerifyIdentityAct.start(SecurityCenterAct.this);
                break;
            case R.id.btn_bankcard_manage:
                BankcardManageAct.start(SecurityCenterAct.this);
                break;
        }
    }

}
