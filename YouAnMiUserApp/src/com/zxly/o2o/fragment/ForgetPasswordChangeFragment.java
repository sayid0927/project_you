package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.activity.BasicAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.model.SMSMessage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.ResettingPasswordRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.EncryptionUtils;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordChangeFragment extends BaseFragment implements OnClickListener {

    private EditText editNewPwd;
    private ImageView btnClean;
    private EditText editConfirmPwd;
    private ImageView btnCleanConfirm;
    private TextView btnNext;
    private String phoneNumber = "";
    private String authCode = "";

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
                UMengAgent.onEvent(getActivity(), UMengAgent.personal_forget_password);
                break;
            case R.id.btn_clean:
                editNewPwd.setText("");
                break;
            case R.id.btn_clean_confirm:
                editConfirmPwd.setText("");
                break;
        }
    }

    @Override
    protected void initView(Bundle bundle) {
        super.initView(bundle);
        Bundle b = getArguments();
        SMSMessage sm = (SMSMessage) b.getSerializable("SMS");
        phoneNumber = sm.phoneNumber;
        authCode = sm.authCode;
        initView();
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "修改密码");

        btnNext = (TextView) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        editNewPwd = (EditText) findViewById(R.id.edit_new_pwd);
        editNewPwd.requestFocus();
        editConfirmPwd = (EditText) findViewById(R.id.edit_confirm_pwd);
        editNewPwd.addTextChangedListener(mNewPassTextWatcher);
        editConfirmPwd.addTextChangedListener(mConfirmPassTextWatcher);

        btnClean = (ImageView) findViewById(R.id.btn_clean);
        btnCleanConfirm = (ImageView) findViewById(R.id.btn_clean_confirm);
        btnClean.setOnClickListener(this);
        btnCleanConfirm.setOnClickListener(this);
        ViewUtils.setGone(btnClean);
        ViewUtils.setGone(btnCleanConfirm);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_login_change_password;
    }

    protected void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.REFRESH:
                SMSMessage m = (SMSMessage) msg.obj;
                phoneNumber = m.phoneNumber;
                authCode = m.authCode;
                // initView();
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
                btnClean.setVisibility(View.GONE);
                btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                btnNext.setEnabled(false);
            } else {
                btnClean.setVisibility(View.VISIBLE);
                if (!StringUtil.isNull(editConfirmPwd.getText().toString())) {
                    btnNext.setBackgroundResource(R.drawable.btn_common_selector);
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
                btnCleanConfirm.setVisibility(View.GONE);
                btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                btnNext.setEnabled(false);
            } else {
                btnCleanConfirm.setVisibility(View.VISIBLE);
                if (!StringUtil.isNull(editNewPwd.getText().toString())) {
                    btnNext.setBackgroundResource(R.drawable.btn_common_selector);
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
        final ResettingPasswordRequest request = new ResettingPasswordRequest(phoneNumber, EncryptionUtils.md5TransferPwd(confirmPassword), authCode);
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
