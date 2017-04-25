package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.easeui.model.IMUserInfoVO;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.VerifyMobileAct;
import com.zxly.o2o.request.AuthCodeGetRequest;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.UpdateMobileRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public class VerifyMobileFragment extends BaseFragment implements OnClickListener {
    private EditText editMobileNew, editMsg;
    private TextView btnGetMsgCode;
    private int resendTime;
    private final int DEFAULT_RESEND_INTERVAL = 90;
    private final int TIME_CHANGE = 100;
    private String mobileNew;

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

        editMobileNew = (EditText) findViewById(R.id.edit_mobile_new);
        editMsg = (EditText) findViewById(R.id.edit_msg_code);
        btnGetMsgCode = (TextView) findViewById(R.id.btn_get_msg_code);
        btnGetMsgCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNew = editMobileNew.getText().toString().trim();
                if (resendTime > 0) {
                    ViewUtils.showToast(resendTime + "秒后才可再次发送");
                } else {
                    if (!StringUtil.isMobileNO(mobileNew)) {
                        ViewUtils.showToast("手机号码有误");
                        return;
                    }
                    resendTime = DEFAULT_RESEND_INTERVAL;
                    handler.sendEmptyMessage(TIME_CHANGE);
                    loadData(mobileNew);
                }
            }
        });
        findViewById(R.id.btn_confirm)
                .setOnClickListener(this);
        findViewById(R.id.btn_confirm).setEnabled(false);
    }

    private void loadData(String mobileNew){
        final AuthCodeGetRequest authCodeGetRequest = new AuthCodeGetRequest(mobileNew, 16);
        authCodeGetRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                findViewById(R.id.btn_confirm).setEnabled(true);
            }

            @Override
            public void onFail(int code) {
            }
        });
        authCodeGetRequest.start(getActivity());
    }

    private void updateMobile(String code){
        UpdateMobileRequest updateMobileRequest = new UpdateMobileRequest(code, mobileNew, 16, Account.user.getId());
        updateMobileRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                IMUserInfoVO user = Account.readLoginUser(getActivity());
                user.setMobilePhone(mobileNew);
                Account.user = user;
                Account.saveLoginUser(getActivity(), user);

                ((VerifyMobileAct)getActivity()).setPageShow(3);
            }

            @Override
            public void onFail(int code) {
            }
        });
        updateMobileRequest.start(getActivity());
    }

    @Override
    protected int layoutId() {
        return R.layout.win_verify_mobile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                ((VerifyMobileAct)getActivity()).setPageShow(0);
                break;
            case R.id.btn_confirm:
                String msgCode = editMsg.getText().toString().trim();
                if (StringUtil.isNull(msgCode)) {
                    ViewUtils.showToast("验证码不能为空");
                    return;
                }
                updateMobile(msgCode);
                break;
        }
    }

}
