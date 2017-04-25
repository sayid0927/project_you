package com.zxly.o2o.fragment;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.activity.BasicAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.SMSMessage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.GetPromotionCodeRequest;
import com.zxly.o2o.request.MessageAuthRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends BaseFragment implements OnClickListener {
    public static int phone_length = 11;
    private EditText editPhoneNumber, editPromotionCode;
    private ImageView btnClean;
    private TextView btnNext;
    private String phoneNumber;
    private String promotionCode;

    NoDoubleClickListener noDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_back:
                    ((BasicAct) getActivity()).obtainMessage(LoginAct.SHOW_DEFAULT)
                            .sendToTarget();
                    break;
                case R.id.btn_clean:
                    editPhoneNumber.setText("");
                    break;
                case R.id.btn_next:
                    phoneNumber = editPhoneNumber.getText().toString();
                    promotionCode = editPromotionCode.getText().toString();
                    Pattern p = Pattern.compile(Constants.PHONE_PATTERN);
                    Matcher m = p.matcher(phoneNumber);
                    if (phoneNumber.length() < phone_length || !m.matches()) {
                        ViewUtils.showToast("请输入正确的电话号码！");
                        return;
                    }
                    if (promotionCode.length() != 0 && promotionCode.length() != 4) {
                        ViewUtils.showToast("推广码有误");
                        return;
                    }
                    if(!StringUtil.isNull(Config.promotionCode)){
                        promotionCode = Config.promotionCode;
                    }
                    netWorker(phoneNumber, promotionCode);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(noDoubleClickListener);
        ViewUtils.setText(findViewById(R.id.txt_title), "注册");
        btnNext = (TextView) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(noDoubleClickListener);
        findViewById(R.id.btn_clean).setOnClickListener(noDoubleClickListener);

        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        editPhoneNumber.requestFocus();
        editPhoneNumber.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnClean.setVisibility(View.GONE);
                    btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                    btnNext.setEnabled(false);
                } else {
                    if (!s.toString().startsWith("1")) {
                        editPhoneNumber.setText("");
                        ViewUtils.showToast("请输入正确的电话号码！");
                        btnClean.setVisibility(View.GONE);
                        btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                        btnNext.setEnabled(false);
                    } else {
                        btnClean.setVisibility(View.VISIBLE);
                        btnNext.setBackgroundResource(R.drawable.btn_common_selector);
                        btnNext.setEnabled(true);
                        if (s.toString().length() == phone_length) {
                            String phone = s.toString();
                            if (StringUtil.isMobileNO(phone)) {
                                getPromotionCode(phone);
                            } else {
                                ViewUtils.showToast("请输入正确的电话号码！");
                            }
                        }
                    }
                }
                if (temp.length() > phone_length) {
                    s.delete(phone_length, s.length());
                    editPhoneNumber.setText(s);
                    editPhoneNumber.setSelection(s.length());
                }
            }
        });
        editPromotionCode = (EditText) findViewById(R.id.edit_promotion_code);
        btnClean = (ImageView) findViewById(R.id.btn_clean);
        btnClean.setVisibility(View.GONE);
        if (!StringUtil.isNull(Config.promotionCode)){
            ViewUtils.setGone(findViewById(R.id.view_promotion));
        } else {
            ViewUtils.setVisible(findViewById(R.id.view_promotion));
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.win_login_register;
    }

    private void getPromotionCode(String phone) {
        final GetPromotionCodeRequest getPromotionCodeRequest = new GetPromotionCodeRequest(phone);
        getPromotionCodeRequest.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.setVisible(findViewById(R.id.view_promotion));
                ViewUtils.setText(editPromotionCode, getPromotionCodeRequest.getPromotionCode());
            }

            @Override
            public void onFail(int code) {

            }
        });
        getPromotionCodeRequest.start(getActivity());
    }

    private void loadData(){
        final MessageAuthRequest request = new MessageAuthRequest(phoneNumber, "1");
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                SMSMessage smsMessage = new SMSMessage();
                smsMessage.phoneNumber = phoneNumber;
                smsMessage.authCode = request.authCode;
                smsMessage.promotionCode = "";
                ((BasicAct) getActivity()).obtainMessage(LoginAct.REGISTER_MESSAGE, smsMessage)
                        .sendToTarget();
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(getActivity());
    }

    private void netWorker(final String phoneNumber, final String promotionCode) {
        final MessageAuthRequest request = new MessageAuthRequest(phoneNumber, "1", promotionCode);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                SMSMessage smsMessage = new SMSMessage();
                smsMessage.phoneNumber = phoneNumber;
                smsMessage.authCode = request.authCode;
                smsMessage.promotionCode = promotionCode;
                ((BasicAct) getActivity()).obtainMessage(LoginAct.REGISTER_MESSAGE, smsMessage)
                        .sendToTarget();
            }

            @Override
            public void onFail(int code) {
                if (40002 == code) {//输入的推广码没有对应推广人
                    final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
                    dialog.show();
                    dialog.setContentView(R.layout.dialog_register);
                    dialog.findViewById(R.id.btn_return)
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                    dialog.findViewById(R.id.btn_continue)
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    loadData();
                                }
                            });
                }
            }
        });
        request.start(getActivity());
    }
}
