package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.BasicAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.SMSMessage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.LoginRequest;
import com.zxly.o2o.request.RegisterRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.EncryptionUtils;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterSettingFragment extends BaseFragment implements OnClickListener {
    private EditText editPwd;
    private ImageView btnClean;
    private EditText editPwdConfirm;
    private ImageView btnCleanConfirm;
    private TextView btnNext;
    private String phoneNumber = "";
    //暂时没用到预留
    private String authCode = "";
    private String promotionCode = "";
    private String pwd = "";
    private String pwdConfirm = "";

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                ((BasicAct) getActivity()).obtainMessage(LoginAct.REGISTER)
                        .sendToTarget();
                break;
            case R.id.btn_next:
                netWorkerRegister();
                UMengAgent.onEvent(getActivity(), UMengAgent.personal_register);
                break;
            case R.id.btn_clean:
                editPwd.setText("");
                break;
            case R.id.btn_clean_confirm:
                editPwdConfirm.setText("");
                break;
        }
    }

    @Override
    protected void initView(Bundle bundle) {
        super.initView(bundle);
        Bundle b = getArguments();
        SMSMessage smsMessage = (SMSMessage) b.getSerializable("SMS");
        phoneNumber = smsMessage.phoneNumber;
        authCode = smsMessage.authCode;
        promotionCode = smsMessage.promotionCode;
        initView();
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "完成注册");
        btnNext = (TextView) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        editPwd = (EditText) findViewById(R.id.edit_pwd);
        editPwd.requestFocus();
        editPwd.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnClean.setVisibility(View.GONE);
                    btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                    btnNext.setEnabled(false);
                } else {
                    btnClean.setVisibility(View.VISIBLE);
                    if (!StringUtil.isNull(editPwdConfirm.getText().toString())) {
                        btnNext.setBackgroundResource(R.drawable.btn_common_selector);
                        btnNext.setEnabled(true);
                    } else {
                        btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                        btnNext.setEnabled(false);
                    }
                }
                if (temp.length() > Constants.PASSWORD_MAX_LENGTH) {
                    s.delete(Constants.PASSWORD_MAX_LENGTH, s.length());
                    editPwd.setText(s);
                    editPwd.setSelection(s.length());
                    ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
                }
            }
        });
        editPwdConfirm = (EditText) findViewById(R.id.edit_pwd_confirm);
        editPwdConfirm.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnCleanConfirm.setVisibility(View.GONE);
                    btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                    btnNext.setEnabled(false);
                } else {
                    btnCleanConfirm.setVisibility(View.VISIBLE);
                    if (!StringUtil.isNull(editPwd.getText().toString())) {
                        btnNext.setBackgroundResource(R.drawable.btn_common_selector);
                        btnNext.setEnabled(true);
                    } else {
                        btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                        btnNext.setEnabled(false);
                    }
                }
                if (temp.length() > Constants.PASSWORD_MAX_LENGTH) {
                    s.delete(Constants.PASSWORD_MAX_LENGTH, s.length());
                    editPwdConfirm.setText(s);
                    editPwdConfirm.setSelection(s.length());
                    ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
                }
            }
        });
        btnClean = (ImageView) findViewById(R.id.btn_clean);
        btnCleanConfirm = (ImageView) findViewById(R.id.btn_clean_confirm);
        btnClean.setOnClickListener(this);
        btnCleanConfirm.setOnClickListener(this);
        ViewUtils.setGone(btnClean);
        ViewUtils.setGone(btnCleanConfirm);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_login_set_password;
    }

    private void netWorkerRegister() {
        pwd = editPwd.getText().toString();
        pwdConfirm = editPwdConfirm.getText().toString();
        Pattern p = Pattern.compile(Constants.PASSWORD_PATTERN);
        Matcher m = p.matcher(pwd);
        Matcher mConfirm = p.matcher(pwdConfirm);
        if (!m.matches() || !mConfirm.matches()) {
            ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
            return;
        }
        if (!pwd.equals(pwdConfirm)) {
            ViewUtils.showToast("两次密码输入必须相同!");
            return;
        }
        final RegisterRequest request = new RegisterRequest(phoneNumber, EncryptionUtils.md5TransferPwd(pwd), Config.shopId, authCode, Constants.APP_TYPE, promotionCode);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.showToast("注册成功!");
//				((BasicFragmentAct) getActivity()).obtainMessage(LoginAct.SHOW_DEFAULT)
//				.sendToTarget();
                netWorkerLogin();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("网络不佳,注册失败!");
            }
        });
        request.start(getActivity());
    }

    private void netWorkerLogin() {
        final LoginRequest request = new LoginRequest(phoneNumber, EncryptionUtils.md5TransferPwd(pwd), Config.shopId);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                request.user.setPassword(EncryptionUtils
                        .md5TransferPwd(pwd));
                request.user.setLoginUserName(phoneNumber);
//				PreferUtil.getInstance().setLoginUser(
//						GsonParser.getInstance().toJson(request.user));
                Account.saveLoginUser(getActivity(), request.user);
                Account.user = request.user;
                ((BasicAct) getActivity()).obtainMessage(
                        LoginAct.LOGIN_FINISHED).sendToTarget();
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(getActivity());
    }

}
