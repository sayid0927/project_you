package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayCheckPwdRequest;
import com.zxly.o2o.request.PayDelBankcardRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-12-8
 * @description 验证身份界面
 */
public class PayIdentityCheckAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private EditText editPwd = null;
    private PayCheckPwdRequest payCheckPwdRequest;
    private String payNo = null;
    private String payType = null;
    private float orderMoney = 0f;
    private int type;
    private long bankcardId;
    private static CallBack callBack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_identity_check);
        context = this;
        payType = getIntent().getStringExtra("payType");
        orderMoney = getIntent().getFloatExtra("orderMoney", 0f);
        type = getIntent().getIntExtra("type", -1);
        payNo = getIntent().getStringExtra("payNo");
        bankcardId = getIntent().getLongExtra("bankcardId", 0);
        initViews();
    }

    /**
     * @description 解绑银行卡校验
     * @param curAct
     * @param bankcardId      银行卡id
     */
    public static void start(Activity curAct, long bankcardId) {
        Intent intent = new Intent(curAct, PayIdentityCheckAct.class);
        intent.putExtra("bankcardId", bankcardId);
        ViewUtils.startActivity(intent, curAct);
    }

    /**
     * @description 新增银行卡校验
     * @param curAct
     * @param payType    交易类型 01：银行卡交易，02：微信，03：余额，04：支付宝
     * @param orderMoney 交易金额
     * @param type       0: 流量充值 1：订单支付，3：提现
     * @param payNo      流水号
     * @param _callBack     前一页面
     */
    public static void start(Activity curAct, String payType,
                             Float orderMoney, int type, String payNo, CallBack _callBack) {
        callBack = _callBack;
        Intent intent = new Intent(curAct, PayIdentityCheckAct.class);
        intent.putExtra("payType", payType);
        intent.putExtra("orderMoney", orderMoney);
        intent.putExtra("type", type);
        intent.putExtra("payNo", payNo);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("验证身份");
        findViewById(R.id.btn_check).setOnClickListener(this);
        editPwd = (EditText) findViewById(R.id.edit_pwd);
    }

    private void verifyPwd(final String tradingPwd) {
        payCheckPwdRequest = new PayCheckPwdRequest(tradingPwd);
        payCheckPwdRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                String wrongTime = payCheckPwdRequest.getWrongTime();
                int wrongCount = payCheckPwdRequest.getWrongCount();
                if (wrongTime != null) {
                    ViewUtils.showToast("您的支付密码已被锁定, " + wrongTime + "小时后解锁");
                    editPwd.setText("");
                    return;
                }
                if (wrongCount == 1) {
                    ViewUtils.showToast("错误1次, 支付密码输入错误3次将被锁定24小时");
                    editPwd.setText("");
                } else if (wrongCount == 2) {
                    ViewUtils.showToast("错误2次, 支付密码输入错误3次将被锁定24小时");
                    editPwd.setText("");
                } else if (wrongCount == 3) {
                    ViewUtils.showToast("您的支付密码已被锁定, 24小时后解锁");
                } else {
                    if (bankcardId != 0) {
                        delBankCard(bankcardId);
                    } else {
                        ViewUtils.showToast("验证成功");
                        PayAddBankcardAct.start(PayIdentityCheckAct.this, payType,
                                orderMoney, type, payNo, tradingPwd, callBack);
                        finish();
                    }
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        payCheckPwdRequest.start(this);
    }

    private void delBankCard(long id) {
        PayDelBankcardRequest payDelBankcardRequest = new PayDelBankcardRequest(id);
        payDelBankcardRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.showToast("解绑成功");
                finish();
            }

            @Override
            public void onFail(int code) {
            }
        });
        payDelBankcardRequest.start(this);
    }

    @Override
    public void finish() {
        super.finish();
        if (payCheckPwdRequest != null) {
            payCheckPwdRequest.cancel();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_check:
                String tradingPwd = editPwd.getText().toString().trim();
                if ("".equals(tradingPwd)) {
                    ViewUtils.showToast("请输入支付密码");
                }  else if (tradingPwd.length() < 6) {
                    ViewUtils.showToast("密码不能少于6位");
                } else {
                    verifyPwd(tradingPwd);
                }
                break;
        }
    }

}
