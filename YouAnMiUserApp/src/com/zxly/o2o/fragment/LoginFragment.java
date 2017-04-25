package com.zxly.o2o.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.BasicAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.LoginRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.EncryptionUtils;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UMengAgent;
import com.zxly.o2o.util.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends BaseFragment implements OnClickListener {
    public static int phone_length = 11;
    private EditText editPhone;
    public EditText editPwd;
    public ImageView btnCleanPhone;
    public ImageView btnCleanPwd;
    private TextView btnRegister;
    private TextView btnLogin;

    private String password;
    private String phoneNumber;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(editPhone.getWindowToken(), 0);
                getActivity().finish();
                break;
            case R.id.btn_title_right:
                ((BasicAct) getActivity()).obtainMessage(LoginAct.REGISTER)
                        .sendToTarget();
                break;
            case R.id.btn_login:
                netWorker();
                UMengAgent.onEvent(getActivity(), UMengAgent.personal_login);
                break;
            case R.id.btn_forget_pwd:
                ((BasicAct) getActivity()).obtainMessage(
                        LoginAct.FORGET_PASSWORD).sendToTarget();
                break;
            case R.id.btn_clean_phone:
                editPhone.setText("");
                break;
            case R.id.btn_clean_pwd:
                editPwd.setText("");
                break;
        }
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "登录");
        btnRegister = (TextView) findViewById(R.id.btn_title_right);
        ViewUtils.setVisible(btnRegister);
        ViewUtils.setText(btnRegister, "注册");
        btnRegister.setOnClickListener(this);

        btnLogin = (TextView) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        findViewById(R.id.btn_forget_pwd).setOnClickListener(this);

        editPhone = (EditText) findViewById(R.id.edit_phone);
        editPhone.requestFocus();
        editPhone.addTextChangedListener(new TextWatcher() {
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
                    btnCleanPhone.setVisibility(View.GONE);
                    btnLogin.setBackgroundResource(R.drawable.btn2_highlight);
                    btnLogin.setEnabled(false);
                } else {
                    if (!s.toString().startsWith("1")) {
                        editPhone.setText("");
                        ViewUtils.showToast("请输入正确的电话号码！");
                        btnCleanPhone.setVisibility(View.GONE);
                        btnLogin.setBackgroundResource(R.drawable.btn2_highlight);
                        btnLogin.setEnabled(false);
                    } else {
                        btnCleanPhone.setVisibility(View.VISIBLE);
                        if (!StringUtil.isNull(editPwd.getText().toString())) {
                            btnLogin.setBackgroundResource(R.drawable.btn_common_selector);
                            btnLogin.setEnabled(true);
                        } else {
                            btnLogin.setBackgroundResource(R.drawable.btn2_highlight);
                            btnLogin.setEnabled(false);
                        }
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
        btnCleanPhone.setOnClickListener(this);
        btnCleanPhone.setVisibility(View.GONE);

        editPwd = (EditText) findViewById(R.id.edit_pwd);
        editPwd.addTextChangedListener(new TextWatcher() {
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
                    btnCleanPwd.setVisibility(View.GONE);
                    btnLogin.setBackgroundResource(R.drawable.btn2_highlight);
                    btnLogin.setEnabled(false);
                } else {
                    btnCleanPwd.setVisibility(View.VISIBLE);
                    if (!StringUtil.isNull(editPhone.getText().toString())) {
                        btnLogin.setBackgroundResource(R.drawable.btn_common_selector);
                        btnLogin.setEnabled(true);
                    } else {
                        btnLogin.setBackgroundResource(R.drawable.btn2_highlight);
                        btnLogin.setEnabled(false);
                    }
                }
                if (temp.length() > Constants.PASSWORD_MAX_LENGTH) {
                    s.delete(Constants.PASSWORD_MAX_LENGTH, s.length());
                    editPwd.setText(s);
                    editPwd.setSelection(s.length());
                    ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
                }
            }
        });

        btnCleanPwd = (ImageView) findViewById(R.id.btn_clean_pwd);
        btnCleanPwd.setOnClickListener(this);
        btnCleanPwd.setVisibility(View.GONE);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_login;
    }

    private void netWorker() {
        phoneNumber = editPhone.getText().toString();
        password = editPwd.getText().toString();

        if ("13888888888".equals(phoneNumber) && "yam2017".equals(password)){
            ViewUtils.showLongToast(StringUtil.getCheckInfo());
            return;
        }
        Pattern pp = Pattern.compile(Constants.PHONE_PATTERN);
        Matcher pm = pp.matcher(phoneNumber);
        if (!pm.matches()) {
            ViewUtils.showToast("请输入正确的电话号码！");
            return;
        }

        Pattern p = Pattern.compile(Constants.PASSWORD_PATTERN);
        Matcher m = p.matcher(password);
        if (!m.matches()) {
            ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
            return;
        }

        final LoginRequest request = new LoginRequest(phoneNumber, EncryptionUtils.md5TransferPwd(password), Config.shopId);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                request.user.setPassword(EncryptionUtils
                        .md5TransferPwd(password));
                request.user.setLoginUserName(phoneNumber);
                //					PreferUtil.getInstance().setLoginUser(
                //							GsonParser.getInstance().toJson(request.user));
                Account.saveLoginUser(getActivity(), request.user);
                Account.user = request.user;
                ((BasicAct) getActivity()).obtainMessage(
                        LoginAct.LOGIN_FINISHED).sendToTarget();

            }

            @Override
            public void onFail(int code) {
            }
        });
        request.start(getActivity());

    }


}
