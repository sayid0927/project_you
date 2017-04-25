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
import com.zxly.o2o.request.MessageAuthRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ViewUtils;

public class ForgetPasswordMessageFragment extends BaseFragment implements OnClickListener {
    private EditText editMessage;
    private TextView btnResend;
    private TextView txtMessageDesc;
    private int resendTime;
    private String phoneNumber = "";
    private String authCode = "";
    private String userName = "";
    private final int TIME_CHANGE = 100;
    private final int DEFAULT_RESEND_INTERVAL = 90;
    private TextView btnNext;

    NoDoubleClickListener noDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_back:
                    ((BasicAct) getActivity()).obtainMessage(LoginAct.FORGET_PASSWORD)
                            .sendToTarget();
                    break;
                case R.id.btn_next:
                    equalsAuthCode();
                    break;
                case R.id.btn_resend:
                    if (resendTime > 0) {
                        ViewUtils.showToast(resendTime + "秒后才可再次发送");
                    } else {
                        netWorkerResend();
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initView(Bundle bundle) {
        super.initView(bundle);
        Bundle b = getArguments();
        SMSMessage sm = (SMSMessage) b.getSerializable("SMS");
        phoneNumber = sm.phoneNumber;
        authCode = sm.authCode;
        userName = sm.userName;
        resendTime = DEFAULT_RESEND_INTERVAL;
        initView();
    }

    @Override
    protected void initView() {
        mMainHandler.removeMessages(TIME_CHANGE);
        findViewById(R.id.btn_back).setOnClickListener(noDoubleClickListener);
        ViewUtils.setText(findViewById(R.id.txt_title), "找回密码");
        btnNext = (TextView) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(noDoubleClickListener);
        btnResend = (TextView) findViewById(R.id.btn_resend);
        btnResend.setOnClickListener(noDoubleClickListener);
        txtMessageDesc = (TextView) findViewById(R.id.txt_message_desc);
        txtMessageDesc.setText(String.format("验证码已发往%s，请等待", phoneNumber));
        editMessage = (EditText) findViewById(R.id.edit_message);
        editMessage.requestFocus();
        editMessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                    btnNext.setBackgroundResource(R.drawable.btn2_selector);
                    btnNext.setEnabled(true);
                }
            }
        });
        btnResend.setText(String.format("重新发送(%d)", resendTime));
        if (resendTime > 0) {
            mMainHandler.sendEmptyMessageDelayed(TIME_CHANGE, 1000);
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.win_login_message;
    }

    protected void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.REFRESH:
                SMSMessage m = (SMSMessage) msg.obj;
                resendTime = DEFAULT_RESEND_INTERVAL;
                phoneNumber = m.phoneNumber;
                authCode = m.authCode;
                userName = m.userName;
                initView();
                break;
            case TIME_CHANGE:
                resendTime--;
                if (resendTime > 0) {
                    btnResend.setText(String.format("重新发送(%d)", resendTime));
                    mMainHandler.sendEmptyMessageDelayed(TIME_CHANGE, 1000);
                } else {
                    btnResend.setText("重新发送");
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMainHandler.removeMessages(TIME_CHANGE);
    }

    private void netWorkerResend() {
        final MessageAuthRequest request = new MessageAuthRequest(7, userName, phoneNumber);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                SMSMessage m = new SMSMessage();
                m.phoneNumber = phoneNumber;
                m.authCode = request.authCode;
                m.userName = userName;
                obtainMessage(Constants.REFRESH, m).sendToTarget();
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(getActivity());
    }

    private void equalsAuthCode() {
        if (editMessage.getText() != null && editMessage.getText().toString().equals(authCode)) {
            SMSMessage m = new SMSMessage();
            m.phoneNumber = phoneNumber;
            m.authCode = authCode;
            m.userName = userName;
            ((BasicAct) getActivity()).obtainMessage(LoginAct.FORGET_PASSWORD_SETTING, m)
                    .sendToTarget();
        } else {
            ViewUtils.showToast("验证码不正确");
        }
    }

}
