package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.activity.PayAct;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.StringUtil;

/**
 * Created by kenwu on 2016/5/13.
 */
public class GuaranteePayFragment  extends BaseFragment implements View.OnClickListener  {


    String orderNo;
    double price;

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

        TextView txtPayInfo= (TextView) findViewById(R.id.txt_pay_info);
        Spanned _txtPayInfo= Html.fromHtml("<font color=\"#333333\">用户应支付金额&nbsp;:&nbsp;</font><font color=\"#ff5f19\">"+StringUtil.getRMBPrice((float) price)+"</font>");
        txtPayInfo.setText(_txtPayInfo);

        findViewById(R.id.btn_user_pay).setOnClickListener(this);
        findViewById(R.id.btn_help_pay).setOnClickListener(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_guatantee_pay;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_user_pay:
                getActivity().finish();
                break;

            case R.id.btn_help_pay:
                PayAct.start(getActivity(),orderNo, Constants.TYPE_INSURANCE_PAY);
                getActivity().finish();
                break;

        }

    }
}
