package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.activity.BasicAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.model.SMSMessage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.MessageAuthRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ViewUtils;

public class RegisterMessageFragment extends BaseFragment implements View.OnClickListener {
    private TextView txtMsgDesc;
    private EditText editMsg;
    private TextView btnNext;
    private TextView btnResend;

    private int resendTime;
    private String phoneNumber = "";
    private String authCode = "";
    private String promotionCode = "";
    private final int TIME_CHANGE = 100;
    private final int DEFAULT_RESEND_INTERVAL = 90;

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initView(Bundle bundle) {
        super.initView(bundle);
        Bundle b = getArguments();
        SMSMessage smsMessage = (SMSMessage) b.getSerializable("SMS");
        phoneNumber = smsMessage.phoneNumber;
        authCode = smsMessage.authCode;
        promotionCode = smsMessage.promotionCode;
        resendTime = DEFAULT_RESEND_INTERVAL;
        initView();
    }

    NoDoubleClickListener noDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_back:
                    ((BasicAct) getActivity()).obtainMessage(LoginAct.REGISTER)
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
    protected void initView() {
        mMainHandler.removeMessages(TIME_CHANGE);
        findViewById(R.id.btn_back).setOnClickListener(noDoubleClickListener);
        ViewUtils.setText(findViewById(R.id.txt_title), "填写验证码");
        btnNext = (TextView) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(noDoubleClickListener);
        btnResend = (TextView) findViewById(R.id.btn_resend);
        btnResend.setOnClickListener(noDoubleClickListener);
        txtMsgDesc = (TextView) findViewById(R.id.txt_msg_desc);
        txtMsgDesc.setText(String.format("验证码已发往%s，请等待", phoneNumber));
        editMsg = (EditText) findViewById(R.id.edit_msg_code);
        editMsg.requestFocus();
        editMsg.addTextChangedListener(new TextWatcher() {

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
                    btnNext.setBackgroundResource(R.drawable.btn_common_selector);
                    btnNext.setEnabled(true);
                }
            }
        });
        btnResend.setText(String.format("重新发送(%d)", resendTime));
        if (resendTime > 0) {
            mMainHandler.sendEmptyMessageDelayed(TIME_CHANGE, 1000);
        }

    }

    protected void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.REFRESH:
                resendTime = DEFAULT_RESEND_INTERVAL;
                SMSMessage smsMessage = (SMSMessage) msg.obj;
                phoneNumber = smsMessage.phoneNumber;
                authCode = smsMessage.authCode;
                promotionCode = smsMessage.promotionCode;
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

    @Override
    protected int layoutId() {
        return R.layout.win_login_message;
    }

    private void netWorkerResend() {
        final MessageAuthRequest request = new MessageAuthRequest(phoneNumber, "1");
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                SMSMessage smsMessage = new SMSMessage();
                smsMessage.phoneNumber = phoneNumber;
                smsMessage.authCode = request.authCode;
                smsMessage.promotionCode = promotionCode;
                obtainMessage(Constants.REFRESH, smsMessage).sendToTarget();
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(getActivity());
    }

    private void equalsAuthCode() {
        if (editMsg.getText() != null && editMsg.getText().toString().equals(authCode)) {
            SMSMessage smsMessage = new SMSMessage();
            smsMessage.phoneNumber = phoneNumber;
            smsMessage.authCode = authCode;
            smsMessage.promotionCode = promotionCode;
            ((BasicAct) getActivity()).obtainMessage(LoginAct.REGISTER_SETTING, smsMessage)
                    .sendToTarget();
        } else {
            ViewUtils.showToast("验证码不正确");
        }
    }

}
