package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
 * Created by dsnx on 2016/5/19.
 */
public class YaoBaoDetailAct extends BasicAct {

    private ListView listview;
    private FlowZjAdapter adapter;
    private String serialNumber;
    private LoadingView loadingview;
    private View btnBack;
    private TextView txtMoney,txtTitle,txtTabName,txtDzTime;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_flow_zj_commision);
        btnBack=findViewById(R.id.btn_back);
        setOnBack(btnBack);
        listview= (ListView) findViewById(R.id.listview);
        ViewGroup head= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.head_product_commission,null);
        txtMoney= (TextView) head.findViewById(R.id.txt_money);
        txtTabName=(TextView)head.findViewById(R.id.txt_tabName);
        txtDzTime=(TextView)head.findViewById(R.id.txt_dz_time);
        txtTitle=(TextView)findViewById(R.id.txt_title);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        serialNumber=getIntent().getStringExtra("serialNumber");
        id=getIntent().getIntExtra("id",0);
        adapter=new FlowZjAdapter(this);
        listview.setDividerHeight(0);
        listview.addHeaderView(head);
        listview.setAdapter(adapter);
        loadingview.startLoading();
        txtTitle.setText("延保收益");
        txtTabName.setText(" 延保详细");
        final YieldDetailSingleRequest yieldDetailSingleRequest=new YieldDetailSingleRequest(id,serialNumber, YieldDetail.YIEL_TYPE_YB);
        yieldDetailSingleRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingview.onLoadingComplete();
                ViewUtils.setVisible(listview);
                adapter.addItem(yieldDetailSingleRequest.getKeyValueLis(), true);
                ViewUtils.setText(txtMoney, "+ " + yieldDetailSingleRequest.getCommission() + " ");

                switch (yieldDetailSingleRequest.getStatus())
                {
                    case 1://冻结中
                        Drawable nav_up=getResources().getDrawable(R.drawable.dongjiezhong_ioc);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        txtMoney.setCompoundDrawables(null, null, nav_up, null);
                        txtMoney.setTextColor(getResources().getColor(R.color.gray_999999));
                        txtDzTime.setText("预到账时间：" + TimeUtil.formatTimeHHMMDD(yieldDetailSingleRequest.getResidueTime()));
                        break;
                }
                adapter.addItem(yieldDetailSingleRequest.getCommissionProductList());

            }

            @Override
            public void onFail(int code) {
                loadingview.onDataEmpty("加载失败",true);
            }
        });
        yieldDetailSingleRequest.start(this);
    }
    public static void start(Activity curAct, String serialNumber, int id)
    {
        Intent it=new Intent();
        it.setClass(curAct,YaoBaoDetailAct.class);
        it.putExtra("serialNumber", serialNumber);
        it.putExtra("id",id);
        ViewUtils.startActivity(it,curAct);
    }
}
