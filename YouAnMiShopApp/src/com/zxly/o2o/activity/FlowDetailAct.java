package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.FlowZjAdapter;
import com.zxly.o2o.model.YieldDetail;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.YieldDetailSingleRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2015/12/16.
 */
public class FlowDetailAct extends BasicAct{

    private ListView listview;
    private FlowZjAdapter adapter;
    private String serialNumber;
    private LoadingView loadingview;
    private View btnBack;
    private TextView txtSerialNumber,txtMakeAccount,txtOrderTime,txtMoney;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_flow_zj_commision);
        btnBack=findViewById(R.id.btn_back);
        setOnBack(btnBack);
        listview= (ListView) findViewById(R.id.listview);
        ViewGroup head= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.head_flow_zj_commission,null);
        ViewGroup footer=(ViewGroup)LayoutInflater.from(this).inflate(R.layout.footer_commission,null);
        ViewUtils.setGone(footer,R.id.txt_orderNumber);
        txtSerialNumber= (TextView) footer.findViewById(R.id.txt_serialNumber);
        txtMakeAccount= (TextView) footer.findViewById(R.id.txt_makeAccount);
        txtOrderTime=(TextView)footer.findViewById(R.id.txt_orderTime);
        txtMoney= (TextView) head.findViewById(R.id.txt_money);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        serialNumber=getIntent().getStringExtra("serialNumber");
        id=getIntent().getIntExtra("id",0);
        adapter=new FlowZjAdapter(this);
        listview.setDividerHeight(0);
        listview.addHeaderView(head);
        listview.addFooterView(footer);
        listview.setAdapter(adapter);
        loadingview.startLoading();
        final YieldDetailSingleRequest yieldDetailSingleRequest=new YieldDetailSingleRequest(id,serialNumber, YieldDetail.YIEL_TYPE_FLOW);
        yieldDetailSingleRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingview.onLoadingComplete();
                ViewUtils.setVisible(listview);
                adapter.addItem(yieldDetailSingleRequest.getKeyValueLis(), true);
                ViewUtils.setText(txtMoney, "+ " + yieldDetailSingleRequest.getCommission() + " ");
                adapter.addItem(yieldDetailSingleRequest.getCommissionProductList());
                txtSerialNumber.setText("交易流水号：" + yieldDetailSingleRequest.getNumberNo());
                txtMakeAccount.setText("下单账号    ：" + yieldDetailSingleRequest.getUserAccount());
                txtOrderTime.setText("订单时间    ：" + TimeUtil.formatTimeYMDHMS(yieldDetailSingleRequest.getPayTime()));
            }

            @Override
            public void onFail(int code) {
                loadingview.onDataEmpty("加载失败",true);
            }
        });
        yieldDetailSingleRequest.start(this);
    }
    public static void start(Activity curAct,String serialNumber,int id)
    {
        Intent it=new Intent();
        it.setClass(curAct,FlowDetailAct.class);
        it.putExtra("serialNumber", serialNumber);
        it.putExtra("id",id);
        ViewUtils.startActivity(it,curAct);
    }

}
