package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.PayAct;
import com.zxly.o2o.activity.PaySuccessAct;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.model.GuaranteeInfo;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetMonthOrderPayRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by kenwu on 2017/4/13.
 */
public class GuaranteePayFragment  extends BaseFragment implements View.OnClickListener  {


    String orderNo;
    double price;
    byte PayType;
     Button btn_help_pay;
    private LoadingDialog loadingDialog;
    private GuaranteeInfo guaranteeInfo;
    private GetMonthOrderPayRequest monthrderPayRequest;
    private  LinearLayout layoutGon;
    private  int id;


    public static GuaranteePayFragment newInstance(Bundle bundle){
        GuaranteePayFragment f=new GuaranteePayFragment();
        f.setArguments(bundle);
        return f;
    }



    @Override
    protected void initView() {

    }

    @Override
    protected void initView(Bundle bundle) {
        orderNo=bundle.getString("orderNo");
        price=bundle.getDouble("price");
        PayType=bundle.getByte("PayType");
        id=bundle.getInt("Id");
        if(PayType==0x02){
                  // 月结支付

            findViewById(R.id.txt_pay_info).setVisibility(View.GONE);
            findViewById(R.id.btn_user_pay).setVisibility(View.GONE);
            findViewById(R.id.layout_visible).setVisibility(View.GONE);
            findViewById(R.id.line2).setVisibility(View.GONE);
            layoutGon =(LinearLayout)findViewById(R.id.layout_gon);
            layoutGon.setVisibility(View.VISIBLE);
            layoutGon.setOnClickListener(this);

        }else {
                   //  线下支付
            TextView txtPayInfo= (TextView) findViewById(R.id.txt_pay_info);
            Spanned _txtPayInfo = Html.fromHtml("<font color=\"#333333\">用户应支付金额&nbsp;:&nbsp;</font><font color=\"#ff5f19\">" + StringUtil.getRMBPrice((float) price) + "</font>");
            txtPayInfo.setText(_txtPayInfo);
            findViewById(R.id.btn_user_pay).setOnClickListener(this);
            findViewById(R.id.btn_help_pay).setOnClickListener(this);
            findViewById(R.id.layout_gon).setOnClickListener(this);

        }
    }

    public void setData(GuaranteeInfo guaranteeInfo)
    {
        this.guaranteeInfo=guaranteeInfo;
    }

    @Override
    protected int layoutId() {
        return R.layout.win_guatantee_pay;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

//            case R.id.btn_user_pay:
//                getActivity().finish();
//                break;

            case R.id.btn_help_pay:
                PayAct.start(getActivity(),orderNo, Constants.TYPE_INSURANCE_PAY);
                getActivity().finish();
                break;



            case R.id.layout_gon:


                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog();
                }
                loadingDialog.show();
                monthrderPayRequest =new GetMonthOrderPayRequest(id, Account.user.getShopId());
                monthrderPayRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                    @Override
                    public void onOK() {
                        ViewUtils.showToast("提交成功!");
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        getActivity().finish();
                        PaySuccessAct.start(getActivity(), 0, "payType", 0, "pay");
                    }

                    @Override
                    public void onFail(int code) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        ViewUtils.showToast("提交失败!");
                    }
                });
                monthrderPayRequest.start(this);
                break;

        }

    }
}
