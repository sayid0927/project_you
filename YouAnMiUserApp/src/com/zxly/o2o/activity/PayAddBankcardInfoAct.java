package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.BankcardInfo;
import com.zxly.o2o.model.BranchBank;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayGetPayNoRequest;
import com.zxly.o2o.request.PayOrderRequest;
import com.zxly.o2o.request.PayQueryBankRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PayUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author fengrongjian 2016-3-8
 * @description 新增银行卡界面
 */
public class PayAddBankcardInfoAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private TextView txtCardType, txtAccountCity, txtAccountBank;
    private String bankName;
    private int bankType;
    private String bankNo;
    private UserBankCard userBankCard;
    private static CallBack callBack = null;
    private PayGetPayNoRequest payGetPayNoRequest;
    private LoadingView loadingView;
    private String provinceCode, cityCode;
    private String branchBankName, prcptcd;
    private float orderMoney = 0f;
    private int type;
    private String payNo = null;
    private String payType = null;
    private BankcardInfo bankcardInfo = new BankcardInfo();
    private ArrayList<BranchBank> branchBankList = new ArrayList<BranchBank>();
    private String userPaw;
    private String orderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_pay_add_newcard_info);
        context = this;
        bankName = getIntent().getStringExtra("bankName");
        bankType = getIntent().getIntExtra("bankType", -1);
        bankNo = getIntent().getStringExtra("bankNo");
        userBankCard = (UserBankCard) getIntent().getSerializableExtra("userBankCard");

        payType = getIntent().getStringExtra("payType");
        orderMoney = getIntent().getFloatExtra("orderMoney", 0f);
        type = getIntent().getIntExtra("type", -1);
        payNo = getIntent().getStringExtra("payNo");
        userPaw = getIntent().getStringExtra("userPwd");
        initViews();
    }

    /**
     * @param curAct
     * @param bankName 银行卡名称
     * @param bankType 银行卡类型
     * @param bankNo   银行卡编号
     */
    public static void start(Activity curAct, String bankName,
                             int bankType, String bankNo, UserBankCard userBankCard, String payType,
                             Float orderMoney, int type, String payNo, String userPwd, CallBack _callBack) {
        callBack = _callBack;
        Intent intent = new Intent(curAct, PayAddBankcardInfoAct.class);
        intent.putExtra("bankName", bankName);
        intent.putExtra("bankType", bankType);
        intent.putExtra("bankNo", bankNo);
        intent.putExtra("userBankCard", userBankCard);

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
        ViewUtils.setText(findViewById(R.id.txt_title), "新增银行卡");
        findViewById(R.id.btn_next).setOnClickListener(this);
        txtCardType = (TextView) findViewById(R.id.txt_card_type);
        txtAccountCity = (TextView) findViewById(R.id.txt_account_city);
        txtAccountBank = (TextView) findViewById(R.id.txt_account_bank);
        txtCardType.setText(bankName + (bankType == 2 ? "  储蓄卡" : "  信用卡"));
        findViewById(R.id.btn_account_city).setOnClickListener(this);
        findViewById(R.id.btn_account_bank).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_account_city:
                PayBankInfoAct.start(PayAddBankcardInfoAct.this, new ParameCallBack() {
                    @Override
                    public void onCall(Object object) {
                        Map<String, String> result = (Map<String, String>) object;
                        provinceCode = result.get("provinceCode");
                        cityCode = result.get("cityCode");
                        String provinceName = result.get("provinceName");
                        String cityName = result.get("cityName");
                        txtAccountCity.setText(provinceName + "  " + cityName);

                        branchBankName = null;
                        prcptcd = null;
                        txtAccountBank.setText("");
                    }
                });
                break;
            case R.id.btn_account_bank:
                if(cityCode != null) {
                    loadData();
                } else {
                    ViewUtils.showToast("请先选择开户地");
                }
                break;
            case R.id.btn_next:
                if(branchBankName != null && prcptcd != null && provinceCode!= null && cityCode != null) {
                    bankcardInfo.setBrabankName(branchBankName);
                    bankcardInfo.setPrcptcd(prcptcd);
                    bankcardInfo.setProvinceCode(provinceCode);
                    bankcardInfo.setCityCode(cityCode);
                    saveData(userBankCard);
                } else {
                    ViewUtils.showToast("开户地或开户行不正确");
                }
                break;
        }
    }

    private void loadData() {
        final PayQueryBankRequest payQueryBankRequest = new PayQueryBankRequest(null, cityCode, userBankCard.getBankNumber(), bankNo);
        payQueryBankRequest.setShowLoadingDialog();
        payQueryBankRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                branchBankList = payQueryBankRequest.getBranchBankList();
                if (branchBankList.isEmpty()) {
                    ViewUtils.showToast("该银行在此城市没有对应支行");
                } else {
                    PayBranchBankAct.start(PayAddBankcardInfoAct.this, cityCode, bankNo, userBankCard, new ParameCallBack() {
                        @Override
                        public void onCall(Object object) {
                            Map<String, String> result = (Map<String, String>) object;
                            branchBankName = result.get("branchBankName");
                            prcptcd = result.get("prcptcd");
                            txtAccountBank.setText(branchBankName);
                        }
                    });
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
        payQueryBankRequest.start(this);
    }

    private void saveData(UserBankCard userBankCard) {
        if (Constants.TYPE_TAKEOUT == type) {
            showPayVerifyDialog();
        } else {
            PayUtil.confirmPay(PayAddBankcardInfoAct.this, type, payNo, payType, orderMoney, userBankCard, callBack, bankcardPageInfoCallBack, bankcardInfo, "pay");
        }
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

    private void getTakeoutVerifyPayNo(String orderNo) {
        payGetPayNoRequest = new PayGetPayNoRequest(orderNo, payType, Constants.BUSINESS_OTHER_TYPE, Constants.CHANNEL_TAKEOUT_VERIFY, Constants.PAY_DEVICE_TYPE, Config.shopId);
        payGetPayNoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (payGetPayNoRequest.getPayNo() != null) {
                    payNo = payGetPayNoRequest.getPayNo();
                    pay(payNo);
//                    PayUtil.confirmPay(PayAddBankcardInfoAct.this, Constants.TYPE_PRODUCT_PAY, payNo, payType, 0.01f, userBankCard, callBack, bankcardPageInfoCallBack, bankcardInfo, "takeout_verify", orderMoney);
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
                PayUtil.launchYinTongPay(mHandler, PayAddBankcardInfoAct.this, userBankCard, orderMoney, orderNo, money, payNo, userPaw, createTime, notifyUrl, "");
            }

            @Override
            public void onFail(int code) {
                PayFailAct.start(PayAddBankcardInfoAct.this, orderNo, type);
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

    /**
     * @description 支付成功后回调关闭页面
     */
    private CallBack bankcardPageInfoCallBack = new CallBack() {
        @Override
        public void onCall() {
            finish();
        }
    };

    @Override
    public void finish() {
        super.finish();
        callBack = null;
        if (payGetPayNoRequest != null) {
            payGetPayNoRequest.cancel();
        }
    }

}
