package com.zxly.o2o.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.BasicAct;
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.LoginRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.EncryptionUtils;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends BaseFragment implements OnClickListener {
    public static int phone_length = 11;
    private EditText editName;
    private EditText editPassword;
    private ImageView btnCleanName;
    private ImageView btnCleanPassword;
    private TextView btnLogin;

    private String password;
    private String phoneNumber;
    private   HashMap<String,String> map=new HashMap<String,String>();
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                // 事件埋点
                UmengUtil.onEvent( getActivity(), "login_login_press",null);
                doLogin();


                break;
            case R.id.btn_clean_name:
                editName.setText("");
                break;
            case R.id.btn_clean_password:
                editPassword.setText("");
                break;
            case R.id.btn_forget:
                // 事件埋点
                UmengUtil.onEvent( getActivity(), "login_forgot_press",null);

                ((BasicAct) getActivity()).obtainMessage(
                        LoginAct.FORGET_PASSWORD).sendToTarget();

                break;
        }
    }

    @Override
    protected void initView() {
        ViewUtils.setText(findViewById(R.id.txt_title), "登录");
        ViewUtils.setGone(findViewById(R.id.btn_back));
        btnLogin = (TextView) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        findViewById(R.id.btn_forget).setOnClickListener(this);

        editName = (EditText) findViewById(R.id.edit_name);
        editName.requestFocus();
        editName.addTextChangedListener(new TextWatcher() {
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
                    btnCleanName.setVisibility(View.GONE);
                    btnLogin.setBackgroundResource(R.drawable.btn2_highlight);
                    btnLogin.setEnabled(false);
                } else {
                    if (!s.toString().startsWith("1")) {
                        editName.setText("");
                        ViewUtils.showToast("请输入正确的电话号码！");
                        btnCleanName.setVisibility(View.GONE);
                        btnLogin.setBackgroundResource(R.drawable.btn2_highlight);
                        btnLogin.setEnabled(false);
                    } else {
                        btnCleanName.setVisibility(View.VISIBLE);
                        if (!StringUtil.isNull(editPassword.getText().toString())) {
                            btnLogin.setBackgroundResource(R.drawable.btn2_selector);
                            btnLogin.setEnabled(true);
                        } else {
                            btnLogin.setBackgroundResource(R.drawable.btn2_highlight);
                            btnLogin.setEnabled(false);
                        }
                    }
                }
                if (temp.length() > phone_length) {
                    s.delete(phone_length, s.length());
                    editName.setText(s);
                    editName.setSelection(s.length());
                }
            }
        });
        btnCleanName = (ImageView) findViewById(R.id.btn_clean_name);
        btnCleanName.setOnClickListener(this);
        btnCleanName.setVisibility(View.GONE);

        editPassword = (EditText) findViewById(R.id.edit_password);
        editPassword.addTextChangedListener(new TextWatcher() {
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
                    btnCleanPassword.setVisibility(View.GONE);
                    btnLogin.setBackgroundResource(R.drawable.btn2_highlight);
                    btnLogin.setEnabled(false);
                } else {
                    btnCleanPassword.setVisibility(View.VISIBLE);
                    if (!StringUtil.isNull(editName.getText().toString())) {
                        btnLogin.setBackgroundResource(R.drawable.btn2_selector);
                        btnLogin.setEnabled(true);
                    } else {
                        btnLogin.setBackgroundResource(R.drawable.btn2_highlight);
                        btnLogin.setEnabled(false);
                    }
                }
                if (temp.length() > Constants.PASSWORD_MAX_LENGTH) {
                    s.delete(Constants.PASSWORD_MAX_LENGTH, s.length());
                    editPassword.setText(s);
                    editPassword.setSelection(s.length());
                    ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
                }
            }
        });
        btnCleanPassword = (ImageView) findViewById(R.id.btn_clean_password);
        btnCleanPassword.setOnClickListener(this);
        btnCleanPassword.setVisibility(View.GONE);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_login;
    }

    private void doLogin() {
        phoneNumber = editName.getText().toString();
        password = editPassword.getText().toString();
        if ("13888888888".equals(phoneNumber) && "yam2017".equals(password)){
            ViewUtils.showLongToast(StringUtil.getCheckInfo());
            return;
        }

        Pattern pp = Pattern.compile(Constants.PHONE_PATTERN);
        Matcher pm = pp.matcher(phoneNumber);
        if (!pm.matches()) {
            map.put("登录失败","请输入正确的电话号码");
            ViewUtils.showToast("请输入正确的电话号码！");
            UmengUtil.onEvent( getActivity(), "overall_open",map);
            return;
        }

        Pattern p = Pattern.compile(Constants.PASSWORD_PATTERN);
        Matcher m = p.matcher(password);
        if (!m.matches()) {
            map.put("登录失败","请输入正确的电话号码");
            ViewUtils.showToast("密码只能为6-16位的数字或字母组成");
            UmengUtil.onEvent( getActivity(), "overall_open",map);
            return;
        }
//        final LoginRequest request = new LoginRequest(phoneNumber, EncryptionUtils.encryptionPwd(password));
        final LoginRequest request = new LoginRequest(phoneNumber, EncryptionUtils.md5TransferPwd(password));
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                request.user.setPassword(EncryptionUtils
                        .md5TransferPwd(password));
                request.user.setUserName(phoneNumber);
                //					PreferUtil.getInstance().setLoginUser(
                //							GsonParser.getInstance().toJson(dataInitrequest.user));
                Account.saveLoginUser(getActivity(), request.user);
                PreferUtil.getInstance().setLoginToken(request.user.getToken());
                Account.user = request.user;
                ((BasicAct) getActivity()).obtainMessage(
                        LoginAct.LOGIN_FINISHED).sendToTarget();
            }

            @Override
            public void onFail(int code) {
                if (20009 == code) {
//                    ViewUtils.showToast("用户不存在");
                    map.put("登录失败","用户不存在");
                    UmengUtil.onEvent( getActivity(), "overall_open",map);
                } else if (20010 == code) {
//                    ViewUtils.showToast("密码错误");
                    map.put("登录失败","密码错误");
                    UmengUtil.onEvent( getActivity(), "overall_open",map);
                } else if (30013 == code) {
                    ViewUtils.showToast("非商户平台用户");
                    map.put("登录失败","非商户平台用户");
                    UmengUtil.onEvent( getActivity(), "overall_open",map);
                } else if (30012 == code) {
                    ViewUtils.showToast("非法参数");
                    map.put("登录失败","非法参数");
                    UmengUtil.onEvent( getActivity(), "overall_open",map);
                }
            }
        });
        request.start(getActivity());

    }
}
