package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.config.Config;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.PromotionBindRequest;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-12-7
 * @description 绑定推广人界面
 */
public class PromotionBindAct extends BasicAct implements
        OnClickListener {
    private EditText editPromotionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_promotion_bind);
        initViews();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, PromotionBindAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("推广人");
        findViewById(R.id.btn_submit).setOnClickListener(this);
        editPromotionCode = (EditText) findViewById(R.id.edit_promotion_code);
        editPromotionCode.setFocusable(true);
        editPromotionCode.setFocusableInTouchMode(true);
        editPromotionCode.requestFocus();
        editPromotionCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        editPromotionCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable edit) {
            }
        });
    }

    private void submitPromotionCode(String promotionCode) {
        PromotionBindRequest promotionBindRequest = new PromotionBindRequest(promotionCode, Config.shopId);
        promotionBindRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.showToast("绑定成功");
                PromotionDetailAct.start(PromotionBindAct.this);
                finish();
            }

            @Override
            public void onFail(int code) {

            }
        });
        promotionBindRequest.start(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_submit:
                String promotionCode = editPromotionCode.getText().toString().trim();
                if (promotionCode.length() != 4) {
                    ViewUtils.showToast("请填写推广码");
                    return;
                }
                submitPromotionCode(promotionCode);
                break;
            default:
                break;
        }
    }

}
