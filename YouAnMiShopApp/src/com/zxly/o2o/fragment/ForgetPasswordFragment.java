package com.zxly.o2o.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.activity.BasicAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.model.SMSMessage;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.MessageAuthRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordFragment extends BaseFragment implements OnClickListener {
    public static int phone_length = 11;
    private EditText editPhone;
    private ImageView btnCleanPhone;
    private TextView btnNext;

    NoDoubleClickListener noDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_back:
                    ((BasicAct) getActivity()).obtainMessage(LoginAct.SHOW_DEFAULT)
                            .sendToTarget();
                    break;
                case R.id.btn_next:
                    netWorker();
                    break;
                case R.id.btn_clean_phone:
                    editPhone.setText("");
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
        ViewUtils.setText(findViewById(R.id.txt_title), "找回密码");
        btnNext = (TextView) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(noDoubleClickListener);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        editPhone.requestFocus();
        editPhone.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btnCleanPhone.setVisibility(View.GONE);
                    btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                    btnNext.setEnabled(false);
                } else {
                    if (!s.toString().startsWith("1")) {
                        editPhone.setText("");
                        ViewUtils.showToast("请输入正确的电话号码！");
                        btnCleanPhone.setVisibility(View.GONE);
                        btnNext.setBackgroundResource(R.drawable.btn2_highlight);
                        btnNext.setEnabled(false);
                    } else {
                        btnCleanPhone.setVisibility(View.VISIBLE);
                        btnNext.setBackgroundResource(R.drawable.btn2_selector);
                        btnNext.setEnabled(true);

                    }
                }
                if (temp.length() > phone_length) {
                    s.delete(phone_length, s.length());
                    editPhone.setText(s);
                    editPhone.setSelection(s.length());
                }
            }
        });
        btnCleanPhone = (ImageView) findViewById(R.id.btn_clean_phone);
        btnCleanPhone.setOnClickListener(noDoubleClickListener);
        btnCleanPhone.setVisibility(View.GONE);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_login_forget;
    }

    private void netWorker() {
        final String phoneNumber = editPhone.getText().toString().trim();
        Pattern p = Pattern.compile(Constants.PHONE_PATTERN);
        Matcher m = p.matcher(phoneNumber);
        if (phoneNumber.length() < phone_length || !m.matches()) {
            ViewUtils.showToast("请输入正确的电话号码！");
            return;
        }
        final MessageAuthRequest request = new MessageAuthRequest(7, phoneNumber, phoneNumber);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                SMSMessage m = new SMSMessage();
                m.phoneNumber = phoneNumber;
                m.authCode = request.authCode;
                m.userName = phoneNumber;
                ((BasicAct) getActivity()).obtainMessage(LoginAct.FORGET_PASSWORD_MESSAGE, m)
                        .sendToTarget();
            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(getActivity());
    }
}
