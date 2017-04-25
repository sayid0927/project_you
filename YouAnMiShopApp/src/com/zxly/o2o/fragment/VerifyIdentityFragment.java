package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.VerifyMobileAct;
import com.zxly.o2o.request.AuthCodeCheckRequest;
import com.zxly.o2o.request.AuthCodeGetRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public class VerifyIdentityFragment extends BaseFragment implements OnClickListener {
    private EditText editMsg;
    private TextView btnGetMsgCode;
    private int resendTime;
    private final int DEFAULT_RESEND_INTERVAL = 90;
    private final int TIME_CHANGE = 100;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_CHANGE:
                    resendTime--;
                    if (resendTime > 0) {
                        btnGetMsgCode.setBackgroundResource(R.drawable.bg_msg_press);
                        btnGetMsgCode.setEnabled(false);
                        btnGetMsgCode.setText(String.format("%d秒后重发", resendTime));
                        handler.sendEmptyMessageDelayed(TIME_CHANGE, 1000);
                    } else {
                        btnGetMsgCode.setBackgroundResource(R.drawable.bg_msg_selector);
                        btnGetMsgCode.setEnabled(true);
                        ViewUtils.setText(btnGetMsgCode, "重新发送");
                    }
                    break;
            }
        }
    };

    @Override
    protected void initView(Bundle bundle) {
        initView();
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "设置手机号码");

        if (Account.user != null) {
            String phone = Account.user.getMobilePhone();
            ((TextView) findViewById(R.id.txt_phone)).setText(StringUtil.getHiddenString(phone));
        }
        editMsg = (EditText) findViewById(R.id.edit_msg_code);
        btnGetMsgCode = (TextView) findViewById(R.id.btn_get_msg_code);
        btnGetMsgCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendTime > 0) {
                    ViewUtils.showToast(resendTime + "秒后才可再次发送");
                } else {
                    resendTime = DEFAULT_RESEND_INTERVAL;
                    handler.sendEmptyMessage(TIME_CHANGE);
                    loadData();
                }
            }
        });
        findViewById(R.id.btn_next)
                .setOnClickListener(this);
    }

    private void loadData(){
        final AuthCodeGetRequest authCodeGetRequest = new AuthCodeGetRequest(Account.user.getMobilePhone(), 16);
        authCodeGetRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
            }

            @Override
            public void onFail(int code) {
            }
        });
        authCodeGetRequest.start(getActivity());
    }

    private void checkCode(String code){
        AuthCodeCheckRequest authCodeCheckRequest = new AuthCodeCheckRequest(Account.user.getMobilePhone(), code, 16);
        authCodeCheckRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                ((VerifyMobileAct)getActivity()).setPageShow(2);
            }

            @Override
            public void onFail(int code) {

            }
        });
        authCodeCheckRequest.start(getActivity());
    }

    @Override
    protected int layoutId() {
        return R.layout.win_verify_identity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                ((VerifyMobileAct)getActivity()).setPageShow(0);
                break;
            case R.id.btn_next:
                String msgCode = editMsg.getText().toString().trim();
                if (StringUtil.isNull(msgCode)) {
                    ViewUtils.showToast("验证码不能为空");
                    return;
                }
                checkCode(msgCode);
                break;
        }
    }
}
