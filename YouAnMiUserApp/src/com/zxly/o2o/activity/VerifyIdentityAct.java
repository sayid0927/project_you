package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.IdentityQueryRequest;
import com.zxly.o2o.request.IdentityVerifyRequest;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2016-5-25
 * @description 验证身份界面
 */
public class VerifyIdentityAct extends BasicAct implements OnClickListener {
    private EditText editName, editIdNo;
    private TextView txtName, txtIdNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_personal_identity);
        initViews();
        loadData();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, VerifyIdentityAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("认证身份信息");
        editName = (EditText) findViewById(R.id.edit_name);
        editIdNo = (EditText) findViewById(R.id.edit_id_number);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtIdNo = (TextView) findViewById(R.id.txt_id_number);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    private void loadData() {
        final IdentityQueryRequest identityQueryRequest = new IdentityQueryRequest();
        identityQueryRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                String certName = identityQueryRequest.getCertName();
                String certNo = identityQueryRequest.getCertNo();
                if (StringUtil.isNull(certName) || StringUtil.isNull(certNo)) {
                    ViewUtils.setVisible(findViewById(R.id.view_verify));
                } else {
                    ViewUtils.setVisible(findViewById(R.id.view_query));
                    if (!StringUtil.isNull(certName)) {
                        ViewUtils.setText(txtName, StringUtil.getHideName(certName));
                    }
                    if (!StringUtil.isNull(certNo)) {
                        ViewUtils.setText(txtIdNo, certNo.substring(0, 14) + "****");
                    }
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        identityQueryRequest.start(this);
    }

    private void setData(String certName, String certNo, int certType) {
        IdentityVerifyRequest identityVerifyRequest = new IdentityVerifyRequest(certName, certNo, certType);
        identityVerifyRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.showToast("设置成功");
                finish();
            }

            @Override
            public void onFail(int code) {
            }
        });
        identityVerifyRequest.start(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_confirm:
                String certName = editName.getText().toString().trim();
                String certNo = editIdNo.getText().toString().trim();
                int certType = 1;//1:身份证
                if (StringUtil.isNull(certName)){
                    ViewUtils.showToast("姓名不能为空");
                    return;
                }
                if (certName.length() < 2 || certName.length() > 4) {
                    ViewUtils.showToast("姓名有误");
                    return;
                }
                if (StringUtil.isNull(certNo)){
                    ViewUtils.showToast("身份证号码不能为空");
                    return;
                }
                if (!StringUtil.isIdNo(certNo)) {
                    ViewUtils.showToast("身份证号码有误");
                    return;
                }
                setData(certName, certNo, certType);
                break;
        }
    }


}
