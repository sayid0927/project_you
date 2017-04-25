package com.zxly.o2o.dialog;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyOrderOperateRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kenhuiwu on 2015/9/16.
 */
public class OrderOperateDialog extends BaseDialog implements View.OnClickListener {

    private Button btnCancel,btnOk;
    private ParameCallBack callBack;
    private TextView txtMsg,txtTitle;
    private boolean isShowAnimation,isShowTitle=false;
    private View layoutTitle;
    private OrderInfo orderInfo;
    private int operateType;
    @Override
    protected void initView() {
        btnCancel=(Button) findViewById(R.id.btn_cancel);
        btnOk=(Button) findViewById(R.id.btn_ok);
        txtMsg= (TextView) findViewById(R.id.txt_msg);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        layoutTitle=findViewById(R.id.layout_title);

        txtTitle=(TextView) findViewById(R.id.txt_title);
    }

    public Button getOkbutton(){
        return btnOk;
    }

    public Button getCancleButton(){
        return btnCancel;
    }

    public TextView getContextText(){
        return txtMsg;
    }

    public TextView getTitle(){
        return txtTitle;
    }

    public void setIsShowTitle(boolean isShowTitle){
        this.isShowTitle=isShowTitle;
    }

    @Override
    protected boolean isShowAnimation() {
        return isShowAnimation;
    }

    public void setIsShowAnimation(boolean isShowAnimation){
        this.isShowAnimation=isShowAnimation;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_verify;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    /**
     * 目前主要是取消和删除订单
     */
    public void show(int operateType,OrderInfo orderInfo,ParameCallBack callBack) {
        super.show();
        String msg=null;
        this.callBack=callBack;
        this.orderInfo=orderInfo;
        this.operateType=operateType;

        if (operateType == Constants.ORDER_WAIT_ROR_PAY) {
            msg = "确定取消订单？";
        } else {
            msg = "确认删除订单？";
        }

        ViewUtils.setText(txtMsg, msg);

    }


    /**
     *  订单操作（取消或者删除订单）
     * */
    private void executeOperateOrder(final OrderInfo orderInfo, final int operateType) {

        MyOrderOperateRequest request=new MyOrderOperateRequest(orderInfo.getOrderNo(),operateType);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                Map<String,Object> result=new HashMap<String, Object>();
                result.put(Constants.ORDER_OPERATE_TYPE,operateType);
                result.put(Constants.ORDER_NO,orderInfo.getOrderNo());
                if(callBack!=null)
                callBack.onCall(result);
            }

            @Override
            public void onFail(int code) {

            }

        });

        request.start(getContext());
    }


    @Override
    public void onClick(View v) {
        if(v==btnOk) {
            executeOperateOrder(orderInfo, operateType);
        }

        dismiss();
    }
}
