package com.zxly.o2o.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.PayAct;
import com.zxly.o2o.activity.PayFailAct;
import com.zxly.o2o.activity.PayFlowResultAct;
import com.zxly.o2o.activity.PaySuccessAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayGetSecurityCodeRequest;
import com.zxly.o2o.request.PayOrderRequest;
import com.zxly.o2o.request.PayVerifySecurityCodeRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;

public class PayMsgDialog {
    private Dialog dialog;
    private Context context;
    private View contentView;
    private EditText editMsg;
    private TextView btnGetMsgCode;
    private int type; //1：订单支付，2：充值：3：提现 0：流量充值
    private String payNo;
    private String payType;//交易类型 1：余额交易，2银行卡交易
    private float orderMoney;
    private Activity curAct;
    private PayGetSecurityCodeRequest payGetSecurityRequest;
    private PayVerifySecurityCodeRequest payVerifyCodeRequest;
    private int resendTime;
    private final int DEFAULT_RESEND_INTERVAL = 90;
    private final int TIME_CHANGE = 100;
    private CallBack callBack;
    private String pwd;
    private String orderNo;

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

    public PayMsgDialog(Activity _curAct, int _type, String _payNo, String _payType, float _orderMoney, CallBack _callBack, String _pwd) {
        curAct = _curAct;
        type = _type;
        payNo = _payNo;
        payType = _payType;
        orderMoney = _orderMoney;
        callBack = _callBack;
        pwd = _pwd;
        context = AppController.getInstance().getTopAct();
        dialog = new Dialog(context, R.style.dialog);
        contentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_pay_msg, null);
        initViews();
    }

    private void initViews() {
        if (Account.user != null) {
            String phone = Account.user.getMobilePhone();
            ((TextView) contentView.findViewById(R.id.txt_phone)).setText(phone.substring(0, 3)
                    + "****" + phone.substring(7));
        }
        TextView txtOrderAmount = ((TextView) contentView
                .findViewById(R.id.txt_pay_money));
        ViewUtils.setTextPrice(txtOrderAmount, orderMoney);
        editMsg = (EditText) contentView.findViewById(R.id.edit_msg_code);
        btnGetMsgCode = (TextView) contentView.findViewById(R.id.btn_get_msg_code);
        btnGetMsgCode.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (resendTime > 0) {
                    ViewUtils.showToast(resendTime + "秒后才可再次发送");
                } else {
                    resendTime = DEFAULT_RESEND_INTERVAL;
                    handler.sendEmptyMessage(TIME_CHANGE);
                    loadData();
                }
            }
        });
        editMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        contentView.findViewById(R.id.btn_pay_done)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String msgCode = editMsg.getText().toString().trim();
                        if (StringUtil.isNull(msgCode)) {
                            ViewUtils.showToast("验证码不能为空");
                            return;
                        }
                        verifySecurity(msgCode);
                    }
                });
        contentView.findViewById(R.id.btn_pay_cancel)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
    }

    public void show() {
        if (dialog.isShowing())
            return;
        dialog.show();
        dialog.getWindow().setContentView(contentView);
    }

    public void dismiss() {
        handler.removeMessages(TIME_CHANGE);
        this.dialog.cancel();
    }

    private void loadData() {
        payGetSecurityRequest = new PayGetSecurityCodeRequest(Constants.TYPE_MSG_PAY);
        payGetSecurityRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
            }

            @Override
            public void onFail(int code) {
            }
        });
        payGetSecurityRequest.start(this);
    }

    private void verifySecurity(String msgCode) {
        payVerifyCodeRequest = new PayVerifySecurityCodeRequest(Constants.TYPE_MSG_PAY, msgCode, payNo);
        payVerifyCodeRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                dismiss();
                pay(pwd);
            }

            @Override
            public void onFail(int code) {
            }
        });
        payVerifyCodeRequest.start(this);
    }

    private void pay(String userPaw) {
        final PayOrderRequest request;
        if (Constants.TYPE_FLOW_PAY == type || Constants.TYPE_INSURANCE_PAY == type) {
            request = new PayOrderRequest(1, userPaw,
                    payNo, payType, null);
        } else {
            request = new PayOrderRequest(type, userPaw,
                    payNo, payType, null);
        }
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                dismiss();
                orderNo = request.getOrderNo();
                if (Constants.PAY_TYPE_USER_BALANCE.equals(payType)) {
                    //余额方式业务
                    if (Constants.TYPE_FLOW_PAY == type) {
                        //流量充值
                        PayFlowResultAct.start(curAct, null, Constants.PAY_TYPE_USER_BALANCE, "success");
                    } else {
                        //商品购买和延保购买
                        PaySuccessAct.start(curAct, orderMoney, orderNo, type, PayAct.insuranceOrderId, "pay");
                    }
                    launchOrderCallback();
                    finishTopActivity();
                }
            }

            @Override
            public void onFail(int code) {
                dismiss();
                if (Constants.TYPE_FLOW_PAY == type) {
                    PayFlowResultAct.start(curAct, null, Constants.PAY_TYPE_USER_BALANCE, "fail");
                } else {
                    PayFailAct.start(curAct, orderNo, type);
                }
                finishTopActivity();
            }
        });
        request.start(this);
    }

    private void launchOrderCallback() {
        if (PayAct.parameCallBack != null) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put(com.zxly.o2o.util.Constants.ORDER_OPERATE_TYPE, com.zxly.o2o.util.Constants.ORDER_OPERATE_PAY);
            result.put(com.zxly.o2o.util.Constants.ORDER_NO, orderNo);
            ///   result.put(Constants.OPERATE_RESULT,Constants.OPERATE_SUCCESS);
            PayAct.parameCallBack.onCall(result);
        }
    }

    private void finishTopActivity() {
        if (callBack != null) {
            callBack.onCall();
        }
        callBack = null;
        curAct.finish();
    }
}
