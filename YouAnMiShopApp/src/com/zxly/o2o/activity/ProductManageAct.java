package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2015/7/8.
 */
public class ProductManageAct extends  BasicAct implements View.OnClickListener{
    private View btnProductCommission,btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_product_manage);
        this.btnBack=findViewById(R.id.btn_back);
        this.btnProductCommission=findViewById(R.id.btn_product_commission);
        this.btnProductCommission.setOnClickListener(this);
        this.btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(btnProductCommission==v)
        {
           // ProductCommissionSetAct.start(this);
        }else if(btnBack==v)
        {
            finish();
        }
    }
    public static void start(Activity curAct)
    {
        Intent it=new Intent();
        it.setClass(curAct, ProductManageAct.class);
        ViewUtils.startActivity(it, curAct);
    }
}
