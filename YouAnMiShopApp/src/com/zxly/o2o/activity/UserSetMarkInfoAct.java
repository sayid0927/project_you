package com.zxly.o2o.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PersonalRemarkRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2016/4/11.
 */
public class UserSetMarkInfoAct extends BasicAct implements View.OnClickListener{
    Long userId;
    public static boolean isRemarkOK;
    private EditText descET,markET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_mark_layout);
        userId=getIntent().getLongExtra("userId",0);
        initView();
    }

    private void initView() {
        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.btn_save_mark).setOnClickListener(this);
        descET=(EditText) findViewById(R.id.desc_edit);
        markET=(EditText) findViewById(R.id.mark_edit);

        descET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>400){
                    descET.setText(descET.getText().toString().substring(0,400));
                    descET.setSelection(descET.getText().length());
                    ViewUtils.showToast("字数超出限制");
                }
            }
        });
        markET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>12){
                    markET.setText(markET.getText().toString().substring(0,12));
                    markET.setSelection(markET.getText().length());
                    ViewUtils.showToast("字数超出限制");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_save_mark:
                if(Account.user!=null) {
                    String desc = descET.getText().toString();
                    String remarkName = markET.getText().toString();
                    PersonalRemarkRequest personalRemarkRequest = new PersonalRemarkRequest(desc,
                            remarkName,userId);
                    personalRemarkRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                        @Override
                        public void onOK() {
                            ViewUtils.showToast("备注成功");
                            isRemarkOK=true;
                            finish();
                        }

                        @Override
                        public void onFail(int code) {
                            ViewUtils.showToast("备注失败");
                        }
                    });
                    personalRemarkRequest.start();
                }
                break;
        }
    }
}
