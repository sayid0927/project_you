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
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.MessageAuthRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordFragment extends BaseFragment implements OnClickListener {
    public static int phone_length = 11;
    private EditText editPhoneNumber;
    private ImageView btnClean;
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
                case R.id.btn_clean:
                    editPhoneNumber.setText("");
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
        if (getArguments() != null && "changePw".equals(getArguments().getString("source"))) {
            ViewUtils.setText(findViewById(R.id.txt_title), "修改密码");
        } else {
            ViewUtils.setText(findViewById(R.id.txt_title), "找回密码");
        }
        btnNext = (TextView) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(noDoubleClickListener);

        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        editPhoneNumber.requestFocus();
        editPhoneNumber.addTextChangedListener(new TextWatcher() {
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
                    }
                }
                if (temp.length() > phone_length) {
                    s.delete(phone_length, s.length());
                    editPhoneNumber.setText(s);
                    editPhoneNumber.setSelection(s.length());
                }
            }
        });
        btnClean = (ImageView) findViewById(R.id.btn_clean);
        btnClean.setOnClickListener(noDoubleClickListener);
        btnClean.setVisibility(View.GONE);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_login_forget;
    }

    private void netWorker() {
        final String phoneNumber = editPhoneNumber.getText().toString();
        Pattern p = Pattern.compile(Constants.PHONE_PATTERN);
        Matcher m = p.matcher(phoneNumber);
        if (phoneNumber.length() < phone_length || !m.matches()) {
            ViewUtils.showToast("请输入正确的电话号码！");
            return;
        }
        final MessageAuthRequest request = new MessageAuthRequest(phoneNumber, "2");
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                SMSMessage m = new SMSMessage();
                m.phoneNumber = phoneNumber;
                m.authCode = request.authCode;
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
