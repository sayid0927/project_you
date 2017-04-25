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

import com.yintong.pay.utils.BaseHelper;
import com.yintong.pay.utils.Constants;
import com.zxly.o2o.activity.PayFailAct;
import com.zxly.o2o.activity.PayFlowResultAct;
import com.zxly.o2o.activity.PaySuccessAct;
import com.zxly.o2o.activity.PersonalChangePasswordAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.BankcardInfo;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.model.YinTongTradeInfo;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PayCheckPwdRequest;
import com.zxly.o2o.request.PayOrderRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.AppLog;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PayUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import org.json.JSONObject;

public class PayConfirmDialog {

    private Dialog dialog;
    private Context context;
    private View contentView;
    private EditText editPwdInput;
    private int type; // 0：流量充值，1：订单支付，3：提现
    private String payNo;
    private String payType;
    private float orderMoney;
    private Activity curAct;
    private UserBankCard userBankCard;
    private String orderNo;
    private CallBack callBack;//支付方式页面回调
    private CallBack callBack2;//银行卡选择页面回调
    private String description;
    private int ORDER_LIST_REFRESH = 11;
    private int BANKCARD_NUMBER_INCORRECT = 12;
    private int SOME_REASON_PAY_FAIL = 13;
    private float moneyInput;
    private String bankName;
    private BankcardInfo bankcardInfo;

    public PayConfirmDialog(Activity _curAct, int _type, String _payNo, String _payType, float _orderMoney, CallBack _callBack) {
        curAct = _curAct;
        type = _type;
        payNo = _payNo;
        payType = _payType;
        orderMoney = _orderMoney;
        callBack = _callBack;
        initViews();
    }

    public PayConfirmDialog(Activity _curAct, int _type, String _payNo, String _payType, float _orderMoney, UserBankCard _userBankCard, CallBack _callBack, CallBack _callBack2, BankcardInfo _bankcardInfo, String _description) {
        curAct = _curAct;
        type = _type;
        payNo = _payNo;
        payType = _payType;
        orderMoney = _orderMoney;
        userBankCard = _userBankCard;
        callBack = _callBack;
        callBack2 = _callBack2;
        description = _description;
        bankcardInfo = _bankcardInfo;
        initViews();
    }

    public PayConfirmDialog(Activity _curAct, int _type, String _payNo, String _payType, float _orderMoney, UserBankCard _userBankCard, CallBack _callBack, CallBack _callBack2, BankcardInfo _bankcardInfo, String _description, float _moneyInput) {
        curAct = _curAct;
        type = _type;
        payNo = _payNo;
        payType = _payType;
        orderMoney = _orderMoney;
        userBankCard = _userBankCard;
        callBack = _callBack;
        callBack2 = _callBack2;
        description = _description;
        moneyInput = _moneyInput;
        bankcardInfo = _bankcardInfo;
        initViews();
    }

    private void initViews() {
        context = AppController.getInstance().getTopAct();
        dialog = new Dialog(context, R.style.dialog);
        contentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_pay_confirm, null);
        TextView txtOrderAmount = ((TextView) contentView
                .findViewById(R.id.txt_pay_money));
        ViewUtils.setTextPrice(txtOrderAmount, orderMoney);
        editPwdInput = (EditText) contentView.findViewById(R.id.edit_pay_input);
        editPwdInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                        String pwd = editPwdInput.getText().toString().trim();
                        if (!StringUtil.isNull(pwd)) {
                            if (com.zxly.o2o.util.Constants.PAY_TYPE_USER_BALANCE.equals(payType)) {
                                checkPayPwd(pwd);
                            } else {
                                pay(pwd);
                            }
                        } else {
                            ViewUtils.showToast("支付密码不能为空");
                        }
                    }
                });
        contentView.findViewById(R.id.btn_pay_cancel)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
        contentView.findViewById(R.id.btn_password_forget)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        PersonalChangePasswordAct.start(curAct, "retrieve");
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
        this.dialog.cancel();
    }

    private void checkPayPwd(final String userPwd) {
        final PayCheckPwdRequest payCheckPwdRequest = new PayCheckPwdRequest(userPwd);
        payCheckPwdRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (payCheckPwdRequest.getWrongTime() != null) {
                    ViewUtils.showToast("您的支付密码已被锁定, " + payCheckPwdRequest.getWrongTime() + "小时后解锁");
                    editPwdInput.setText("");
                    return;
                }
                if (payCheckPwdRequest.getWrongCount() == 1) {
                    ViewUtils.showToast("错误1次, 支付密码输入错误3次将被锁定24小时");
                    editPwdInput.setText("");
                    return;
                } else if (payCheckPwdRequest.getWrongCount() == 2) {
                    ViewUtils.showToast("错误2次, 支付密码输入错误3次将被锁定24小时");
                    editPwdInput.setText("");
                    return;
                } else if (payCheckPwdRequest.getWrongCount() == 3) {
                    dismiss();
                    ViewUtils.showToast("您的支付密码已被锁定, 24小时后解锁");
                    return;
                }
                dismiss();
                PayMsgDialog payMsgDialog = new PayMsgDialog(curAct, type, payNo, payType, orderMoney, callBack, userPwd);
                payMsgDialog.show();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("验证支付密码失败");
            }
        });
        payCheckPwdRequest.start(this);
    }

    private void pay(final String userPaw) {
        final PayOrderRequest request;
        if (com.zxly.o2o.util.Constants.TYPE_FLOW_PAY == type || com.zxly.o2o.util.Constants.TYPE_INSURANCE_PAY == type) {
            request = new PayOrderRequest(1, userPaw,
                    payNo, payType, userBankCard, bankcardInfo);
        } else {
            request = new PayOrderRequest(type, userPaw,
                    payNo, payType, userBankCard, bankcardInfo);
        }
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

                String payNo = request.getPayNo();
                String createTime = request.getCreateTime();
                String notifyUrl = request.getNotifyUrl();
                String money = request.getMoney();
                bankName = request.getBankName();
                userBankCard.setBankName(bankName);
                String mobile = request.getMobile();

                //连连支付
                launchYinTongPay(money, payNo, userPaw, createTime, notifyUrl, mobile);
            }

            @Override
            public void onFail(int code) {
                dismiss();
                if (30115 == code) {
                    ViewUtils.showToast("暂不支持信用卡");
                    return;
                }
                if (com.zxly.o2o.util.Constants.TYPE_FLOW_PAY == type) {
                    PayFlowResultAct.start(curAct, null, com.zxly.o2o.util.Constants.PAY_TYPE_LIANLIAN, "fail");
                } else if (com.zxly.o2o.util.Constants.TYPE_PRODUCT_PAY == type) {
                    PayFailAct.start(curAct, orderNo, type);
                }
                finishTopActivity();
            }
        });
        request.start(this);
    }

    private void launchYinTongPay(String money, final String payNo, final String userPaw, String createTime, String notifyUrl, final String mobile) {
        int ytPayType = 0;
        if (com.zxly.o2o.util.Constants.TYPE_FLOW_PAY == type || com.zxly.o2o.util.Constants.TYPE_INSURANCE_PAY == type) {
            //流量充值：虚拟交易
            ytPayType = 2;
        } else if (com.zxly.o2o.util.Constants.TYPE_PRODUCT_PAY == type) {
            //商品购买等：实物交易
            ytPayType = 1;
        }
        YinTongTradeInfo ytti = new YinTongTradeInfo(Float.parseFloat(money), payNo, createTime, notifyUrl);
        PayUtil.doYinTongPay(userBankCard, ytti, new ParameCallBack() {
            @Override
            public void onCall(Object object) {
                String strRet = (String) object;
                JSONObject objContent = BaseHelper.string2JSON(strRet);
                String retCode = objContent.optString("ret_code");
                String retMsg = objContent.optString("ret_msg");
                AppLog.e("---retMsg---" + retMsg);
                // 成功
                if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
                    // TODO 成功
                    AppLog.e("---success---, retCode is-----" + retCode);
                    if (com.zxly.o2o.util.Constants.TYPE_FLOW_PAY == type) {
                        PayFlowResultAct.start(curAct, payNo, com.zxly.o2o.util.Constants.PAY_TYPE_LIANLIAN, "success");
                    } else if (com.zxly.o2o.util.Constants.TYPE_PRODUCT_PAY == type || com.zxly.o2o.util.Constants.TYPE_INSURANCE_PAY == type) {
                        PaySuccessAct.start(curAct, orderMoney, orderNo, type, description, userBankCard, moneyInput, userPaw);
                    }
                    mHandler.obtainMessage(ORDER_LIST_REFRESH).sendToTarget();
                } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                    // TODO 处理中，掉单的情形
                    String resultPay = objContent.optString("result_pay");
                    if (Constants.RESULT_PAY_PROCESSING
                            .equalsIgnoreCase(resultPay)) {
                    }
                    AppLog.e("---ing---, retCode is---" + retCode);
                } else {
                    // TODO 失败
                    AppLog.e("---fail---, retCode is---" + retCode);
                    if ("1503".equals(retCode) || "1004".equals(retCode) || "1008".equals(retCode)) {
                        mHandler.obtainMessage(BANKCARD_NUMBER_INCORRECT).sendToTarget();
                        return;
                    }
                    if("1005".equals(retCode)){
                        mHandler.obtainMessage(SOME_REASON_PAY_FAIL).sendToTarget();
                        return;
                    }
                    if (!"1006".equals(retCode)) {
                        if (com.zxly.o2o.util.Constants.TYPE_FLOW_PAY == type) {
                            PayFlowResultAct.start(curAct, payNo, com.zxly.o2o.util.Constants.PAY_TYPE_LIANLIAN, "fail");
                        } else if (com.zxly.o2o.util.Constants.TYPE_PRODUCT_PAY == type) {
                            PayFailAct.start(curAct, orderNo, type);
                        }
                        finishTopActivity();
                    }
                }
            }
        }, curAct, ytPayType, mobile);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ORDER_LIST_REFRESH) {
                finishTopActivity();
            } else if (msg.what == BANKCARD_NUMBER_INCORRECT) {
                ViewUtils.showToast("信息输入有误");
            } else if (msg.what == SOME_REASON_PAY_FAIL) {
                ViewUtils.showToast("某些原因导致支付失败");
            }
        }
    };

    private void finishTopActivity() {
        if (callBack != null) {
            callBack.onCall();
        }
        callBack = null;
        if (callBack2 != null) {
            callBack2.onCall();
        }
        callBack2 = null;
        curAct.finish();
    }
}
