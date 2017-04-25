package com.zxly.o2o.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.activity.MyOrderInfoAct;
import com.zxly.o2o.activity.PersonalChangePasswordAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ConfirmReceiveRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 确认收货
 * Created by kenwu on 2015/9/10.
 */
public class ConfirmReceiveDialog extends BaseDialog {

    private EditText editPwdInput;

    private ParameCallBack callBack;

    private OrderInfo orderInfo;

    private  ConfirmReceiveRequest request;

    private Activity curAct;

    @Override
    protected void initView() {

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,-2);
        params.setMargins(20,20,20,20);
        content.setLayoutParams(params);

        editPwdInput = (EditText) findViewById(R.id.edit_pay_input);
        editPwdInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });


       findViewById(R.id.btn_pay_done).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String pwd = editPwdInput.getText().toString().trim();
                        if (!StringUtil.isNull(pwd)) {
                            submit(pwd);
                        } else {
                            ViewUtils.showToast("支付密码不能为空");
                        }
                    }
                });

        findViewById(R.id.btn_pay_cancel).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }

                });

        findViewById(R.id.btn_password_forget).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        PersonalChangePasswordAct.start(curAct, "retrieve");
                    }

                });

    }


    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    private void submit(String pwd){
        request= new ConfirmReceiveRequest(orderInfo.getOrderNo(),pwd);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (!DataUtil.stringIsNull(request.getWrongTime())) {
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

                Map<String,Object> result=new HashMap<String, Object>();
                result.put(Constants.ORDER_OPERATE_TYPE, Constants.ORDER_OPERATE_CONFIRM);
                result.put(Constants.ORDER_NO, orderInfo.getOrderNo());
                if(callBack!=null)
                callBack.onCall(result);
                if(curAct instanceof  MyOrderInfoAct) {
                    ((MyOrderInfoAct) curAct).loading();
                }
                ViewUtils.showToast("确认成功，货款已打入商家账户 !");
                dismiss();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.showToast("确认收货失败");
                dismiss();
            }

        });

        request.start();
    }



    public void show(Activity curAct,OrderInfo orderInfo,ParameCallBack callBack) {
        this.callBack=callBack;
        this.orderInfo=orderInfo;
        this.curAct=curAct;
        super.show();
    }


    @Override
    protected boolean isLimitHeight() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_receive_confirm;
    }



}
