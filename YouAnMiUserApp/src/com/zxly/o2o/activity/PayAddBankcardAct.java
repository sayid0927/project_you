package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayGetPayNoRequest;
import com.zxly.o2o.request.PayLoadShortcutRequest;
import com.zxly.o2o.request.PayOrderRequest;
import com.zxly.o2o.request.PayQueryBankInfoRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.PayUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * @author fengrongjian 2015-5-22
 * @description 新增银行卡界面
 */
public class PayAddBankcardAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private EditText editName, editIdNumber, editCardNo;
    private TextView txtName, txtIdNumber;
    private TextView btnTitleRight = null;
    private float orderMoney = 0f;
    private int type;
    private String payNo = null;
    private String payType = null;
    private String userName = null;
    private String userIdNumber = null;
    private String userIdOriginal = null;
    private String userCardNo = null;
    private UserBankCard userBankCard;
    private static CallBack callBack = null;
    private PayLoadShortcutRequest payLoadShortcutRequest;
    private PayGetPayNoRequest payGetPayNoRequest;
    private String moneyInput;
    private boolean isFirst = true;
    private LoadingView loadingView;
    private String userPaw;
    private String orderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_pay_add_newcard);
        context = this;
        payType = getIntent().getStringExtra("payType");
        orderMoney = getIntent().getFloatExtra("orderMoney", 0f);
        type = getIntent().getIntExtra("type", -1);
        payNo = getIntent().getStringExtra("payNo");
        moneyInput = getIntent().getStringExtra("moneyInput");
        userPaw = getIntent().getStringExtra("userPwd");
        initViews();
        loadData();
    }

    /**
     * @param curAct
     * @param payType    交易类型 01：银行卡交易，02：微信，03：余额，04：支付宝
     * @param orderMoney 交易金额
     * @param type       0: 流量充值 1：订单支付，3：提现
     * @param payNo      流水号
     */
    public static void start(Activity curAct, String payType,
                             Float orderMoney, int type, String payNo, String userPwd, CallBack _callBack) {
        callBack = _callBack;
        Intent intent = new Intent(curAct, PayAddBankcardAct.class);
        intent.putExtra("payType", payType);
        intent.putExtra("orderMoney", orderMoney);
        intent.putExtra("type", type);
        intent.putExtra("payNo", payNo);
        intent.putExtra("userPwd", userPwd);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        findViewById(R.id.btn_back).setOnClickListener(this);
        btnTitleRight = (TextView) findViewById(R.id.btn_title_right);
        ViewUtils.setVisible(btnTitleRight);
        ViewUtils.setText(btnTitleRight, "支持银行");
        btnTitleRight.setOnClickListener(this);
        if (Constants.TYPE_PRODUCT_PAY == type || Constants.TYPE_FLOW_PAY == type) {
            ViewUtils.setText(findViewById(R.id.txt_title), "新卡支付");
        } else if (Constants.TYPE_TAKEOUT == type) {
            ViewUtils.setText(findViewById(R.id.txt_title), "新卡提现");
        }
        findViewById(R.id.btn_add).setOnClickListener(this);
        editName = (EditText) findViewById(R.id.edit_name);
        editIdNumber = (EditText) findViewById(R.id.edit_id_number);
        editCardNo = (EditText) findViewById(R.id.edit_bankcard);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtIdNumber = (TextView) findViewById(R.id.txt_id_number);
    }

    private void loadData() {
        payLoadShortcutRequest = new PayLoadShortcutRequest();
        payLoadShortcutRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                loadingView.onLoadingComplete();
                userName = payLoadShortcutRequest.getUserName();
                userIdNumber = payLoadShortcutRequest.getIdCard();
                if (StringUtil.isNull(userName) || StringUtil.isNull(userIdNumber)) {
                    isFirst = true;
                    ViewUtils.setGone(findViewById(R.id.view_info));
                    ViewUtils.setVisible(findViewById(R.id.view_edit_info));
                } else {
                    isFirst = false;
                    ViewUtils.setVisible(findViewById(R.id.view_info));
                    ViewUtils.setGone(findViewById(R.id.view_edit_info));
                }
                ViewUtils.setVisible(findViewById(R.id.view_root));
                userIdOriginal = userIdNumber;
                ViewUtils.setText(txtName, userName);
                ViewUtils.setText(txtIdNumber, StringUtil.getHiddenString(userIdNumber));
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingFail();
            }
        });
        loadingView.startLoading();
        payLoadShortcutRequest.start(this);
    }

    private void queryBankcardInfo(final UserBankCard userBankCard) {
        final PayQueryBankInfoRequest payQueryBankInfoRequest = new PayQueryBankInfoRequest(userBankCard.getBankNumber());
        payQueryBankInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                int bankType = payQueryBankInfoRequest.getBankType();
                String bankNo = payQueryBankInfoRequest.getBankNo();
                int withdrawType = payQueryBankInfoRequest.getWithdrawType();
                if (Constants.TYPE_TAKEOUT == type) {//提现
                    if (Constants.TYPE_BANKCARD_CREDIT == bankType) {//信用卡
                        ViewUtils.showToast("提现不支持信用卡");
                    } else {
                        if (1 == withdrawType) {
                            showPayVerifyDialog();
                        } else {
                            ViewUtils.setVisible(findViewById(R.id.txt_warning));
                        }
                    }
                } else {
                    saveData(userBankCard);
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        payQueryBankInfoRequest.start(this);
    }

    private void saveData(UserBankCard userBankCard) {
        PayUtil.confirmPay(PayAddBankcardAct.this, type, payNo, payType, orderMoney, userBankCard, callBack, bankcardPageCallBack, null, "pay");
    }

    private void showPayVerifyDialog() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_pay_verify);
        dialog.findViewById(R.id.btn_pay_done)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getTakeoutVerifyPayNo("");
                    }
                });
        dialog.findViewById(R.id.btn_pay_cancel)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
    }


    /**
     * @param orderNo 订单号
     * @description 获取新卡提现时0.01元支付验证的流水号
     */
    private void getTakeoutVerifyPayNo(String orderNo) {
        payGetPayNoRequest = new PayGetPayNoRequest(orderNo, payType, Constants.BUSINESS_OTHER_TYPE, Constants.CHANNEL_TAKEOUT_VERIFY, Constants.PAY_DEVICE_TYPE, Config.shopId);
        payGetPayNoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (payGetPayNoRequest.getPayNo() != null) {
                    payNo = payGetPayNoRequest.getPayNo();
                    pay(payNo);
//                    PayUtil.confirmPay(PayAddBankcardAct.this, Constants.TYPE_PRODUCT_PAY, payNo, payType, 0.01f, userBankCard, callBack, bankcardPageCallBack, null, "takeout_verify", orderMoney);
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        payGetPayNoRequest.start(this);
    }

    private void pay(String payNo) {
        final PayOrderRequest request;
        request = new PayOrderRequest(Constants.TYPE_PRODUCT_PAY, userPaw,
                payNo, payType, userBankCard, null);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                orderNo = request.getOrderNo();
                userBankCard.setBankName(request.getBankName());

                String payNo = request.getPayNo();
                String createTime = request.getCreateTime();
                String notifyUrl = request.getNotifyUrl();
                String money = request.getMoney();
                //连连支付
                PayUtil.launchYinTongPay(mHandler, PayAddBankcardAct.this, userBankCard, orderMoney, orderNo, money, payNo, userPaw, createTime, notifyUrl, "");
            }

            @Override
            public void onFail(int code) {
                PayFailAct.start(PayAddBankcardAct.this, orderNo, type);
                finishTopActivity();
            }
        });
        request.start(this);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PayUtil.BANKCARD_NUMBER_INCORRECT:
                    ViewUtils.showToast("信息输入有误");
                    break;
                case PayUtil.SOME_REASON_PAY_FAIL:
                    ViewUtils.showToast("某些原因导致支付失败");
                    break;
                case PayUtil.FINISH_TOP_ACTIVITY:
                    finishTopActivity();
                    break;
                default:
                    break;
            }
        }
    };

    private void finishTopActivity() {
        if (callBack != null) {
            callBack.onCall();
        }
        callBack = null;
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_title_right:
                PaySupportedBanksAct.start(PayAddBankcardAct.this, type);
                break;
            case R.id.btn_add:
                if (isFirst) {
                    userName = editName.getText().toString().trim();
                    userIdNumber = editIdNumber.getText().toString().trim();
                } else {
                    userName = txtName.getText().toString();
                    if (userIdOriginal != null) {
                        userIdNumber = userIdOriginal;
                    } else {
                        userIdNumber = txtIdNumber.getText().toString();
                    }
                }
                userCardNo = editCardNo.getText().toString().trim();
                if (!StringUtil.isNull(userName) && !StringUtil.isNull(userIdNumber) && !StringUtil.isNull(userCardNo) && userCardNo.length() > 10) {
                    userBankCard = new UserBankCard();
                    userBankCard.setUserName(userName);
                    userBankCard.setIdCard(userIdNumber);
                    userBankCard.setBankNumber(userCardNo);
                    queryBankcardInfo(userBankCard);
                } else {
                    ViewUtils.showToast("信息填写不正确");
                }
                break;
        }
    }


    /**
     * @description 支付成功后回调关闭页面
     */
    private CallBack bankcardPageCallBack = new CallBack() {
        @Override
        public void onCall() {
            if (callBack != null) {
                callBack.onCall();
                callBack = null;
            }
            finish();
        }
    };

    @Override
    public void finish() {
        super.finish();
        callBack = null;
        if (payLoadShortcutRequest != null) {
            payLoadShortcutRequest.cancel();
        }
        if (payGetPayNoRequest != null) {
            payGetPayNoRequest.cancel();
        }
    }

}
