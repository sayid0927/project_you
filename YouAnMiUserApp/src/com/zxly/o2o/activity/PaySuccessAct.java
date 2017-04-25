package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayMyAccountRequest;
import com.zxly.o2o.request.PayOrderRequest;
import com.zxly.o2o.request.PayTakeoutRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.PayUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-5-22
 * @description 支付成功界面
 */
public class PaySuccessAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private float orderMoney = 0f;
    private String orderNo;
    private int type;
    private String description;
    private View viewTakeout, viewVerifyCard, viewNormalPay;
    private EditText editMoney;
    private float takeoutMoney;
    private float userBalance;
    private UserBankCard userBankCard;
    private float moneyInput;
    private String userPaw;
    private long insuranceOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_pay_success);
        context = this;
        orderMoney = getIntent().getFloatExtra("orderMoney", 0f);
        orderNo = getIntent().getStringExtra("orderNo");
        type = getIntent().getIntExtra("type", 0);
        description = getIntent().getStringExtra("description");
        userBankCard = (UserBankCard) getIntent().getSerializableExtra("userBankCard");
        moneyInput = getIntent().getFloatExtra("moneyInput", 0f);
        userPaw = getIntent().getStringExtra("userPaw");
        insuranceOrderId = getIntent().getLongExtra("insuranceOrderId", 0);
        initViews();
    }

    /**
     * @param orderMoney   交易金额
     * @param orderNo      订单号
     * @param type        1：商品 11：延保服务
     * @param description      交易描述
     */
    public static void start(Activity curAct, float orderMoney, String orderNo, int type, long insuranceOrderId, String description) {
        Intent intent = new Intent(curAct, PaySuccessAct.class);
        intent.putExtra("orderMoney", orderMoney);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("type", type);
        intent.putExtra("insuranceOrderId", insuranceOrderId);
        intent.putExtra("description", description);
        ViewUtils.startActivity(intent, curAct);
    }

    /**
     * @param orderMoney   交易金额
     * @param orderNo      订单号
     * @param description      交易描述
     * @param userBankCard      银行卡信息
     * @param moneyInput      提现页面输入的金额
     */
    public static void start(Activity curAct, float orderMoney, String orderNo, int type, long insuranceOrderId, String description, UserBankCard userBankCard, float moneyInput, String userPaw) {
        Intent intent = new Intent(curAct, PaySuccessAct.class);
        intent.putExtra("orderMoney", orderMoney);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("type", type);
        intent.putExtra("insuranceOrderId", insuranceOrderId);
        intent.putExtra("description", description);
        intent.putExtra("userBankCard", userBankCard);
        intent.putExtra("moneyInput", moneyInput);
        intent.putExtra("userPaw", userPaw);
        ViewUtils.startActivity(intent, curAct);
    }

    /**
     * @param orderMoney   交易金额
     * @param orderNo      订单号
     * @param description      交易描述
     * @param userBankCard      银行卡信息
     * @param moneyInput      提现页面输入的金额
     */
    public static void start(Activity curAct, float orderMoney, String orderNo, String description, UserBankCard userBankCard, float moneyInput, String userPaw) {
        Intent intent = new Intent(curAct, PaySuccessAct.class);
        intent.putExtra("orderMoney", orderMoney);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("description", description);
        intent.putExtra("userBankCard", userBankCard);
        intent.putExtra("moneyInput", moneyInput);
        intent.putExtra("userPaw", userPaw);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        viewNormalPay = findViewById(R.id.view_normal_pay);
        viewVerifyCard = findViewById(R.id.view_verify_card);
        viewTakeout = findViewById(R.id.view_takeout);

        if ("pay".equals(description)) {
            ViewUtils.setText(findViewById(R.id.txt_title), "支付成功");
            ViewUtils.setVisible(viewNormalPay);
            if (Constants.TYPE_INSURANCE_PAY == type) {
                ViewUtils.setGone(findViewById(R.id.txt_pay_desc));
                ViewUtils.setText(findViewById(R.id.btn_check_order), "查看保障单");
                ViewUtils.setText(findViewById(R.id.btn_continue_shopping), "查看其他保障服务");
            } else if (Constants.TYPE_PRODUCT_PAY == type) {
                ViewUtils.setText(findViewById(R.id.btn_check_order), "查看订单");
                ViewUtils.setText(findViewById(R.id.btn_continue_shopping), "继续购物");
            }
            findViewById(R.id.btn_check_order).setOnClickListener(this);
            findViewById(R.id.btn_continue_shopping).setOnClickListener(this);
        } else if ("takeout".equals(description)) {
            ViewUtils.setText(findViewById(R.id.txt_title), "提现");
            ViewUtils.setVisible(viewTakeout);
            findViewById(R.id.btn_check_account).setOnClickListener(this);
            findViewById(R.id.btn_takeout_detail).setOnClickListener(this);
            ViewUtils.setText(findViewById(R.id.txt_takeout_money), "提现金额：￥" + StringUtil.getFormatPrice(orderMoney));
            String bankNumber = userBankCard.getBankNumber();
            ViewUtils.setText(findViewById(R.id.txt_takeout_bankcard), "提现银行卡：" + userBankCard.getBankName() + " 【" + StringUtil.getHiddenString(bankNumber) + "】");
        } else if ("takeout_verify".equals(description)) {
            ViewUtils.setText(findViewById(R.id.txt_title), "提现");
            ViewUtils.setVisible(viewVerifyCard);
            editMoney = (EditText) findViewById(R.id.edit_money);
            if(moneyInput != 0) {
                ViewUtils.setText(editMoney, moneyInput);
            }
            findViewById(R.id.btn_takeout).setOnClickListener(this);
            loadData();
        }
        findViewById(R.id.btn_check_order).setOnClickListener(this);
        findViewById(R.id.btn_continue_shopping).setOnClickListener(this);
    }

    private void loadData() {
        final PayMyAccountRequest payMyAccountRequest = new PayMyAccountRequest();
        payMyAccountRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                userBalance = payMyAccountRequest.getAccountBalance();
                editMoney.setHint("≤ " + userBalance);
                ViewUtils.setTextPrice((TextView) findViewById(R.id.txt_user_balance), userBalance);
            }

            @Override
            public void onFail(int code) {
            }
        });
        payMyAccountRequest.start(this);
    }

    private void doTakeout(final String moneyInput, final UserBankCard userBankCard, int takeoutType) {
        final PayTakeoutRequest payTakeoutRequest = new PayTakeoutRequest(moneyInput, userBankCard.getUserName(), userBankCard.getIdCard(), userBankCard.getBankNumber(), takeoutType);
        payTakeoutRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                String payNo = payTakeoutRequest.getPayNo();
                PayUtil.confirmTakeout(PaySuccessAct.this, Constants.TYPE_TAKEOUT, payNo, Constants.PAY_TYPE_LIANLIAN, Float.parseFloat(moneyInput), userBankCard, null, null);
//                pay(Float.parseFloat(moneyInput), payNo);
            }

            @Override
            public void onFail(int code) {
            }
        });
        payTakeoutRequest.start(this);
    }

    private void pay(final float moneyInput, String payNo) {
        final PayOrderRequest request = new PayOrderRequest(Constants.TYPE_TAKEOUT, userPaw,
                payNo, Constants.PAY_TYPE_LIANLIAN, userBankCard);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                orderNo = request.getOrderNo();
                String bankName = request.getBankName();
                userBankCard.setBankName(bankName);
                PaySuccessAct.start(PaySuccessAct.this, moneyInput, orderNo, type, insuranceOrderId, "takeout", userBankCard, 0, null);
                finish();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("提现失败" + code);
                PayFailAct.start(PaySuccessAct.this, orderNo, "takeout_fail");
                finish();
            }
        });
        request.start(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_check_order:
                if (Constants.TYPE_INSURANCE_PAY == type) {
                    InsuranceAct.start(PaySuccessAct.this, insuranceOrderId);
                } else if (Constants.TYPE_PRODUCT_PAY == type) {
                    MyOrderInfoAct.start(orderNo, PaySuccessAct.this, null);
                }
                finish();
                break;
            case R.id.btn_continue_shopping:
                finish();
                break;
            case R.id.btn_takeout:
                String moneyInput = editMoney.getText().toString().trim();
                if ("".equals(moneyInput)) {
                    ViewUtils.showToast("请输入提现金额");
                    return;
                } else {
                    takeoutMoney = Float.parseFloat(moneyInput);
                    if (takeoutMoney > userBalance) {
                        ViewUtils.showToast("提现金额不能超过余额");
                        return;
                    } else if (takeoutMoney < 10) {
                        ViewUtils.showToast("提现金额不能小于10元");
                        return;
                    }
                    doTakeout(moneyInput, userBankCard, 2);
                }
                break;
            case R.id.btn_check_account:
                finish();
                break;
            case R.id.btn_takeout_detail:
                PayAccountRecordAct.start(5, PaySuccessAct.this);
                finish();
                break;
        }
    }

}
