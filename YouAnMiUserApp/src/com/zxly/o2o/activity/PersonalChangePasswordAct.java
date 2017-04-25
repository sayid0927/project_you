package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.SMSMessage;
import com.zxly.o2o.model.User;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ChangePasswordRequest;
import com.zxly.o2o.request.PayChangePwdRequest;
import com.zxly.o2o.request.PayGetSecurityCodeRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.EncryptionUtils;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fengrongjian 2015-3-24
 * @description 修改密码
 */
public class PersonalChangePasswordAct extends BasicAct implements
        View.OnClickListener {
    private Context context = null;
    private EditText editOriginalPwd;
    private EditText editNewPwd;
    private EditText editNewPwdConfirm;
    private String type;
    private TextView btnResend;
    private EditText editVerifyCode;
    private int resendTime;
    private final int DEFAULT_RESEND_INTERVAL = 90;
    private final int TIME_CHANGE = 100;
    private PayGetSecurityCodeRequest payGetSecurityRequest;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.REFRESH:
                    SMSMessage m = (SMSMessage) msg.obj;
                    resendTime = DEFAULT_RESEND_INTERVAL;
                    initViews();
                    break;
                case TIME_CHANGE:
                    resendTime--;
                    if (resendTime > 0) {
                        btnResend.setText(String.format("%d秒后重发", resendTime));
                        handler.sendEmptyMessageDelayed(TIME_CHANGE, 1000);
                    } else {
                        ViewUtils.setText(btnResend, "重新发送");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_change_password);
        context = this;
        type = getIntent().getStringExtra("type");
        resendTime = DEFAULT_RESEND_INTERVAL;
        initViews();
        if (!"login".equals(type)) {
            getCode();
        }
    }

    /**
     * @param curAct
     * @param type   来源 登录：login, 交易：pay, 找回：retrieve
     */
    public static void start(Activity curAct, String type) {
        Intent intent = new Intent(curAct, PersonalChangePasswordAct.class);
        intent.putExtra("type", type);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(noDoubleClickListener);
        if ("login".equals(type)) {
            ((TextView) findViewById(R.id.txt_title)).setText("登录密码修改");
            ViewUtils.setGone(findViewById(R.id.view_phone_no));
            ViewUtils.setVisible(findViewById(R.id.view_original_pwd));
        } else {
            handler.removeMessages(TIME_CHANGE);
            if ("pay".equals(type)) {
                ((TextView) findViewById(R.id.txt_title)).setText("交易密码修改");
            } else if ("retrieve".equals(type)) {
                ((TextView) findViewById(R.id.txt_title)).setText("找回交易密码");
            }
            if (Account.user != null) {
                String phone = Account.user.getMobilePhone();
                ((TextView) findViewById(R.id.txt_phone)).setText(phone
                        .substring(0, 3) + "****" + phone.substring(7));
            }
            editVerifyCode = (EditText) findViewById(R.id.edit_verify_code);
            btnResend = (TextView) findViewById(R.id.btn_resend);
            btnResend.setOnClickListener(noDoubleClickListener);
            btnResend.setText(String.format("%d秒后重发", resendTime));
            if (resendTime > 0) {
                handler.sendEmptyMessageDelayed(TIME_CHANGE, 1000);
            }
        }
        findViewById(R.id.btn_confirm).setOnClickListener(noDoubleClickListener);
        editOriginalPwd = (EditText) findViewById(R.id.original_password);
        editNewPwd = (EditText) findViewById(R.id.new_password);
        editNewPwdConfirm = (EditText) findViewById(R.id.new_password_confirm);
        editOriginalPwd.addTextChangedListener(mOriginalPassTextWatcher);
        editNewPwd.addTextChangedListener(mNewPassTextWatcher);
        editNewPwdConfirm.addTextChangedListener(mNewPassConfirmTextWatcher);
    }

    private void changePw() {
        final String newPassword = editNewPwd.getText().toString().trim();
        final String confirmPassword = editNewPwdConfirm.getText().toString()
                .trim();
        Pattern p = Pattern.compile(Constants.PASSWORD_PATTERN);
        Matcher newM = p.matcher(newPassword);
        Matcher confirmM = p.matcher(confirmPassword);
        if (!newM.matches() || !confirmM.matches()) {
            ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            ViewUtils.showToast("两次密码输入必须相同!");
            return;
        }
        if ("login".equals(type)) {
            final String originalPassword = editOriginalPwd.getText().toString()
                    .trim();
            if (!Account.user.getPassword().equals(
                    EncryptionUtils.md5TransferPwd(originalPassword))) {
                ViewUtils.showToast("原密码输入有误");
                return;
            }
            changeLoginPwd(confirmPassword);
        } else {
            if (!"".equals(editVerifyCode.getText().toString().trim())) {
                String msgCode = editVerifyCode.getText().toString().trim();
                String newPwdConfirm = editNewPwdConfirm.getText().toString().trim();
                changePayPwd(msgCode, newPwdConfirm);
            } else {
                ViewUtils.showToast("请输入验证码");
            }
        }

    }

    private void changePayPwd(String code, final String confirmPassword) {
        PayChangePwdRequest payChangePwdRequest = new PayChangePwdRequest(code, confirmPassword);
        payChangePwdRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if ("pay".equals(type)) {
                    ViewUtils.showToast("修改支付密码成功!");
                } else if ("retrieve".equals(type)) {
                    ViewUtils.showToast("找回支付密码成功!");
                }
                finish();
            }

            @Override
            public void onFail(int code) {
            }
        });
        payChangePwdRequest.start(this);
    }

    private void changeLoginPwd(final String confirmPassword) {
        final ChangePasswordRequest request = new ChangePasswordRequest(
                Account.user.getUserName(),
                EncryptionUtils.md5TransferPwd(confirmPassword),
                Account.user.getPassword());
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.showToast("修改密码成功!");
                User user = Account.readLoginUser(context);
                user.setPassword(EncryptionUtils
                        .md5TransferPwd(confirmPassword));
                Account.user = user;
                Account.saveLoginUser(context, user);

                // Account.user.setPassword(EncryptionUtils
                // .md5TransferPwd(confirmPassword));
                // Account.saveLoginUser(PersonalChangePasswordAct.this,
                // Account.user);
                finish();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("网络不佳,修改失败!");
            }
        });
        request.start(this);
    }

    NoDoubleClickListener noDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_back:
                    finish();
                    break;
                case R.id.btn_confirm:
                    changePw();
                    break;
                case R.id.btn_resend:
                    if (resendTime > 0) {
                        ViewUtils.showToast(resendTime + "秒后才可再次发送");
                    } else {
                        getCode();
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {

    }

    @Override
    public void finish() {
        super.finish();
        handler.removeMessages(TIME_CHANGE);
        if (payGetSecurityRequest != null) {
            payGetSecurityRequest.cancel();
        }
    }

    private void getCode() {
        payGetSecurityRequest = new PayGetSecurityCodeRequest(3);
        payGetSecurityRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                handler.obtainMessage(Constants.REFRESH).sendToTarget();
            }

            @Override
            public void onFail(int code) {
                handler.obtainMessage(Constants.REFRESH).sendToTarget();
            }
        });
        payGetSecurityRequest.start(this);
    }

    TextWatcher mOriginalPassTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > Constants.PASSWORD_MAX_LENGTH) {
                s.delete(Constants.PASSWORD_MAX_LENGTH, s.length());
                editOriginalPwd.setText(s);
                editOriginalPwd.setSelection(s.length());
                ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
            }
        }
    };
    TextWatcher mNewPassTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > Constants.PASSWORD_MAX_LENGTH) {
                s.delete(Constants.PASSWORD_MAX_LENGTH, s.length());
                editNewPwd.setText(s);
                editNewPwd.setSelection(s.length());
                ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
            }
        }
    };
    TextWatcher mNewPassConfirmTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > Constants.PASSWORD_MAX_LENGTH) {
                s.delete(Constants.PASSWORD_MAX_LENGTH, s.length());
                editNewPwdConfirm.setText(s);
                editNewPwdConfirm.setSelection(s.length());
                ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
            }
        }
    };

}
