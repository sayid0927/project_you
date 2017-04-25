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
import com.zxly.o2o.activity.PayFailAct;
import com.zxly.o2o.activity.PaySuccessAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.UserBankCard;
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

public class PayTakeoutDialog {
    private Dialog dialog;
    private Context context;
    private View contentView;
    private EditText editMsgCode, editPwdInput;
    private TextView btnGetMsgCode;
    private int type; //1：订单支付，2：充值：3：提现 0：流量充值
    private String payNo;
    private String payType;//交易类型 1：余额交易，2银行卡交易
    private float orderMoney;
    private Activity curAct;
    private UserBankCard userBankCard;
    private String orderNo;
    private CallBack callBack;//支付方式页面回调
    private CallBack callBack2;//银行卡选择页面回调
    private PayGetSecurityCodeRequest payGetSecurityRequest;
    private PayVerifySecurityCodeRequest payVerifyCodeRequest;
    private int resendTime;
    private final int DEFAULT_RESEND_INTERVAL = 90;
    private final int TIME_CHANGE = 100;
    private String bankName;

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

    public PayTakeoutDialog(Activity _curAct, int _type, String _payNo, String _payType, float _orderMoney, UserBankCard _userBankCard, CallBack _callBack, CallBack _callBack2) {
        curAct = _curAct;
        type = _type;
        payNo = _payNo;
        payType = _payType;
        orderMoney = _orderMoney;
        userBankCard = _userBankCard;
        callBack = _callBack;
        callBack2 = _callBack2;
        context = AppController.getInstance().getTopAct();
        dialog = new Dialog(context, R.style.dialog);
        contentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_takeout_confirm, null);
        initViews();
    }

    private void initViews() {
        ViewUtils.setTextPrice((TextView) contentView
                .findViewById(R.id.txt_takeout_money), orderMoney);
        String bankNumber = userBankCard.getBankNumber();
        String bankName = userBankCard.getBankName();
        if(StringUtil.isNull(bankName)){
           bankName = "";
        }
        ViewUtils.setText(contentView
                .findViewById(R.id.txt_takeout_bankcard), bankName + "(" + StringUtil.getHiddenString(bankNumber) + ")");
        if (Account.user != null) {
            String phone = Account.user.getMobilePhone();
            phone = "验证码已发送至手机号：" + phone.substring(0, 3) + "****" + phone.substring(7);
            ViewUtils.setText(contentView.findViewById(R.id.txt_msg_code), phone);
        }
        editMsgCode = (EditText) contentView.findViewById(R.id.edit_msg_code);
        editPwdInput = (EditText) contentView.findViewById(R.id.edit_pay_input);
        btnGetMsgCode = (TextView) contentView.findViewById(R.id.btn_resend);
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
        editMsgCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        contentView.findViewById(R.id.btn_pay_done).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String msgCode = editMsgCode.getText().toString().trim();
                String pwd = editPwdInput.getText().toString().trim();
                if (StringUtil.isNull(msgCode)) {
                    ViewUtils.showToast("验证码不能为空");
                    return;
                }
                if (StringUtil.isNull(pwd)) {
                    ViewUtils.showToast("支付密码不能为空");
                    return;
                }
                verifySecurity(msgCode, pwd);
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

    private void verifySecurity(String msgCode, final String pwd) {
        payVerifyCodeRequest = new PayVerifySecurityCodeRequest(Constants.TYPE_MSG_PAY, msgCode, payNo);
        payVerifyCodeRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                pay(pwd);
            }

            @Override
            public void onFail(int code) {
            }
        });
        payVerifyCodeRequest.start(this);
    }

    private void pay(String userPaw) {
        final PayOrderRequest request = new PayOrderRequest(type, userPaw,
                payNo, payType, userBankCard);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                if (request.getWrongTime() != null) {
                    ViewUtils.showToast("您的支付密码已被锁定, " + request.getWrongTime() + "小时后解锁");
                    editPwdInput.setText("");
                    return;
                }
                if (request.getWrongCount() == 1) {
                    ViewUtils.showToast("错误1次, 支付密码输入错误3次将被锁定24小时");
                    editPwdInput.setText("");
                    return;
                } else if (request.getWrongCount() == 2) {
                    ViewUtils.showToast("错误2次, 支付密码输入错误3次将被锁定24小时");
                    editPwdInput.setText("");
                    return;
                } else if (request.getWrongCount() == 3) {
                    dismiss();
                    ViewUtils.showToast("您的支付密码已被锁定, 24小时后解锁");
                    return;
                }
                dismiss();
                orderNo = request.getOrderNo();
                bankName = request.getBankName();
                userBankCard.setBankName(bankName);
                PaySuccessAct.start(curAct, orderMoney, orderNo, "takeout", userBankCard, 0, null);
                finishTopActivity();
            }

            @Override
            public void onFail(int code) {
                dismiss();
                ViewUtils.showToast("提现失败" + code);
                PayFailAct.start(curAct, orderNo, "takeout_fail");
                finishTopActivity();
            }
        });
        request.start(this);
    }

    private void finishTopActivity() {
        if (callBack != null) {
            callBack.onCall();
        }
        callBack = null;
        if (callBack2 != null) {
            callBack2.onCall();
        }
        callBack = null;
        callBack2 = null;
        curAct.finish();
    }
}
