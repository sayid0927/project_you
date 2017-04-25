package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.activity.BasicAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.model.SMSMessage;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.ResettingPasswordRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.EncryptionUtils;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordChangeFragment extends BaseFragment implements OnClickListener {
    private EditText editNewPwd;
    private EditText editConfirmPwd;
    private String authCode = "";
    private String userName = "";
    private TextView btnNext;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                ((BasicAct) getActivity()).obtainMessage(LoginAct.FORGET_PASSWORD)
                        .sendToTarget();
                break;
            case R.id.btn_next:
                netWorker();
                break;
//		case R.id.password_clean:
//			editConfirmPwd.setText("");
//			break;
        }
    }

    @Override
    protected void initView(Bundle bundle) {
        super.initView(bundle);
        Bundle b = getArguments();
        SMSMessage sm = (SMSMessage) b.getSerializable("SMS");
        authCode = sm.authCode;
        userName = sm.userName;
        initView();
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "找回密码");
        btnNext = (TextView) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        editNewPwd = (EditText) findViewById(R.id.edit_new_pwd);
        editNewPwd.requestFocus();
        editConfirmPwd = (EditText) findViewById(R.id.edit_new_confirm);
        editNewPwd.addTextChangedListener(mNewPassTextWatcher);
        editConfirmPwd.addTextChangedListener(mConfirmPassTextWatcher);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_login_change_password;
    }

    protected void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.REFRESH:
                SMSMessage m = (SMSMessage) msg.obj;
                authCode = m.authCode;
                userName = m.userName;
                break;
        }
    }

    TextWatcher mNewPassTextWatcher = new TextWatcher() {
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
                btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                btnNext.setEnabled(false);
            } else {
                if (!StringUtil.isNull(editConfirmPwd.getText().toString())) {
                    btnNext.setBackgroundResource(R.drawable.btn2_selector);
                    btnNext.setEnabled(true);
                } else {
                    btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                    btnNext.setEnabled(false);
                }
            }
            if (temp.length() > Constants.PASSWORD_MAX_LENGTH) {
                s.delete(Constants.PASSWORD_MAX_LENGTH, s.length());
                editNewPwd.setText(s);
                editNewPwd.setSelection(s.length());
                ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
            }
        }
    };
    TextWatcher mConfirmPassTextWatcher = new TextWatcher() {
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
                btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                btnNext.setEnabled(false);
            } else {
                if (!StringUtil.isNull(editNewPwd.getText().toString())) {
                    btnNext.setBackgroundResource(R.drawable.btn2_selector);
                    btnNext.setEnabled(true);
                } else {
                    btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                    btnNext.setEnabled(false);
                }
            }
            if (temp.length() > Constants.PASSWORD_MAX_LENGTH) {
                s.delete(Constants.PASSWORD_MAX_LENGTH, s.length());
                editConfirmPwd.setText(s);
                editConfirmPwd.setSelection(s.length());
                ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
            }
        }
    };

    private void netWorker() {
        final String newPassword = editNewPwd.getText().toString();
        final String confirmPassword = editConfirmPwd.getText().toString();
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
        final ResettingPasswordRequest request = new ResettingPasswordRequest(userName, EncryptionUtils.md5TransferPwd(confirmPassword), authCode);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.showToast("找回密码成功!");
                ((BasicAct) getActivity()).obtainMessage(LoginAct.SHOW_DEFAULT)
                        .sendToTarget();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("网络不佳,修改失败!");
            }
        });
        request.start(getActivity());
    }

}
