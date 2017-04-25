package com.zxly.o2o.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;

import com.zxly.o2o.application.AppController;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PaySetPwdRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;

public class PaySetDialog {
    private Dialog dialog;
    private Context context;
    private View contentView;
    private EditText editPwdInput;
    private EditText editPwdConfirm;
    private Activity curAct;
    private ParameCallBack setPwdCallBack;

    public PaySetDialog(Activity _curAct, ParameCallBack _setPwdCallBack) {
        setPwdCallBack = _setPwdCallBack;
        curAct = _curAct;
        context = AppController.getInstance().getTopAct();
        dialog = new Dialog(context, R.style.dialog);
        contentView = LayoutInflater.from(context).inflate(
                R.layout.dialog_pay_set, null);
        editPwdInput = (EditText) contentView.findViewById(R.id.edit_pw_input);
        editPwdConfirm = (EditText) contentView
                .findViewById(R.id.edit_pw_confirm);
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
                        String pwdInput = editPwdInput.getText().toString()
                                .trim();
                        String pwdConfirm = editPwdConfirm.getText().toString()
                                .trim();
                        if ("".equals(pwdInput) || "".equals(pwdConfirm)) {
                            ViewUtils.showToast("密码不能为空");
                        } else if (pwdInput.length() < 6 || pwdConfirm.length() < 6) {
                            ViewUtils.showToast("密码不能少于6位");
                        } else if (!pwdInput.equals(pwdConfirm)) {
                            ViewUtils.showToast("两次密码不一致");
                        } else {
                            setPwd(pwdInput);
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
    }

    public void setPwd(final String pwd) {
        final PaySetPwdRequest request = new PaySetPwdRequest(pwd);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                dismiss();
                Constants.isUserPaw = 2;
                setPwdCallBack.onCall(pwd);
            }

			@Override
			public void onFail(int code) {
				dismiss();
			}
		});
		request.start(this);
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

}
