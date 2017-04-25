package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.request.FileUploadRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ViewUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fengrongjian 2015-6-24
 * @description 意见反馈
 */
public class FeedbackAct extends BasicAct implements
        OnClickListener {
    private EditText editFeedback;
    private EditText editContactNumber;
    private TextView txtLengthNo;
    private String lengthNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_feedback);
        initViews();
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case Constants.MSG_SUCCEED:
                finish();
                ViewUtils.showToast("提交成功");
                break;
            case Constants.MSG_FAILED:
                ViewUtils.showToast("提交失败，稍后重试!");
                break;
        }
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, FeedbackAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.txt_title)).setText("意见反馈");
        findViewById(R.id.btn_feedback).setOnClickListener(this);
        editFeedback = (EditText) findViewById(R.id.edit_feedback);
        editFeedback.setFocusable(true);
        editFeedback.setFocusableInTouchMode(true);
        editFeedback.requestFocus();
        editFeedback.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        editFeedback.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable edit) {
                lengthNo = "" + (400 - edit.length());
                txtLengthNo.setText(lengthNo);
            }
        });

        editContactNumber = (EditText) findViewById(R.id.edit_contact_number);
        txtLengthNo = (TextView) findViewById(R.id.txt_length);
    }

    private void submitFeedback() {
        String feedback = editFeedback.getText().toString().trim();
        String contact = editContactNumber.getText().toString().trim();
        if (feedback == null || "".equals(feedback)) {
            ViewUtils.showToast("请填写反馈内容");
            return;
        }
        if(!"".equals(contact)){
            if(!isEmail(contact)&&!isMobileNO(contact)&&!isQQ(contact)){
                ViewUtils.showToast("联系方式输入格式有误");
                return;
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", Account.user.getShopId() + "");
        params.put("content", feedback);
        params.put("contactInfo", contact);
        File file = null;
        new FileUploadRequest(file, params, "shopApp/publish",
                mMainHandler).startUpload();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_feedback:
                submitFeedback();
                break;
            default:
                break;
        }
    }

    public boolean isEmail(String email) {
        Pattern p = Pattern.compile(Constants.EMAIL_PATTERN);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile(Constants.PHONE_PATTERN);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public boolean isQQ(String qq) {
        Pattern p = Pattern.compile(Constants.QQ_PATTERN);
        Matcher m = p.matcher(qq);
        return m.matches();
    }


}
