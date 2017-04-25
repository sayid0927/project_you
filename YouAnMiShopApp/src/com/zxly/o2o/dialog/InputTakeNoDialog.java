package com.zxly.o2o.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.zxly.o2o.activity.PersonalChangePasswordAct;
import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.TakeNoInputRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 确认收货
 * Created by kenwu on 2015/9/10.
 */
public class InputTakeNoDialog extends BaseDialog implements View.OnClickListener {

    private EditText editTakeNo;

    private String takeNo,orderNo;

    private TakeNoInputRequest request;

    private View btnSubmit;

    private CallBack callBack;

    @Override
    protected void initView() {

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,-2);
        params.setMargins(20,20,20,20);
        contentView.setLayoutParams(params);

        editTakeNo = (EditText) findViewById(R.id.edit_takeNo);
        editTakeNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });


        btnSubmit=findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    private void submit(String orderNo,String takeNo){
        if ((request==null)){
            request= new TakeNoInputRequest(orderNo,takeNo);
            request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    ViewUtils.showToast("提货成功");
                    if(callBack!=null)
                        callBack.onCall();

                    dismiss();
                }

                @Override
                public void onFail(int code) {
                    ViewUtils.showToast("提货失败");
                    dismiss();
                }

            });
        }

        request.start();
    }



    public void show(String orderNo,CallBack callBack) {
        super.show();
        this.orderNo=orderNo;
        this.callBack=callBack;
    }




    @Override
    public int getLayoutId() {
        return R.layout.dialog_input_take_number;
    }


    @Override
    public void onClick(View v) {
        takeNo= editTakeNo.getText().toString().trim();
        if (!StringUtil.isNull(takeNo)) {
            submit(orderNo,takeNo);
        } else {
            ViewUtils.showToast("提货码不能为空");
        }
    }
}
