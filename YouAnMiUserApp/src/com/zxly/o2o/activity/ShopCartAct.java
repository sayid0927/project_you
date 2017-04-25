package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ShopCartAdapter;
import com.zxly.o2o.model.BuyItem;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ChartProductListRequest;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/5/18.
 */
public class ShopCartAct extends BasicAct implements View.OnClickListener ,BaseRequest.ResponseStateListener{
    private LoadingView loadingView;
    private View btnBack,btnAllSel;
    private TextView txtPrice,btnEdit;
    private TextView txtTitle,btnToBuy;
    private View contentLayout;
    private PullToRefreshListView productListView;
    private ShopCartAdapter adapter;
    private List<BuyItem> productList=new ArrayList<BuyItem>();
    private float totalPrice=0;
    private boolean isDel=false;
    private boolean isAll=false;
    private int buyCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_shop_chart);
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        btnBack = findViewById(R.id.btn_back);
        txtPrice= (TextView) findViewById(R.id.txt_price);
        txtTitle=(TextView) findViewById(R.id.txt_title);
        btnEdit= (TextView) findViewById(R.id.btn_edit);
        btnAllSel=findViewById(R.id.btn_allSel);
        btnToBuy=(TextView)findViewById(R.id.btn_to_buy);
        ViewUtils.setText(txtTitle, "购物车结算");
        contentLayout=findViewById(R.id.content_layout);
        productListView= (PullToRefreshListView) findViewById(R.id.listview);
        adapter=new ShopCartAdapter(this,productListView.getRefreshableView(),new ParameCallBackById() {
            @Override
            public void onCall(int count,Object object) {
                if(count<=0)
                {
                    btnEdit.setText("");
                    loadingView.onDataEmpty("我闹机荒啦，给我塞点东西吧~",R.drawable.kb_icon_d);
                    ViewUtils.setGone(contentLayout);
                }
                totalPrice= (Float)object;
                if(totalPrice<0)
                {
                    ViewUtils.setText(txtPrice, "￥0.00");
                }else
                {
                    ViewUtils.setTextPrice(txtPrice,totalPrice);
                }
                int tempCount=adapter.getSelProductList().size();
                if(isAll)
                {
                    if(buyCount>tempCount)
                    {
                        isAll=false;
                        btnAllSel.setSelected(isAll);
                    }
                }else
                {
                    if(adapter.getContent().size()==adapter.getSelProductList().size())
                    {
                        buyCount=tempCount;
                        isAll=true;
                        btnAllSel.setSelected(isAll);
                    }
                }
                ViewUtils.setText(btnToBuy,"结算( "+tempCount+" )");

            }
        });
        ViewUtils.setTextPrice(txtPrice, totalPrice);
        productListView.setIntercept(true);
        productListView.setAdapter(adapter);
        
        ViewUtils.setRefreshText(productListView);
        productListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        btnBack.setOnClickListener(this);
        btnToBuy.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnAllSel.setOnClickListener(this);
        getChartList();

    }
    public static void start(Activity curAct)
    {
        Intent intent;
        if(Account.user != null){
            intent = new Intent(curAct, ShopCartAct.class);
            ViewUtils.startActivity(intent, curAct);
        } else {
            intent = new Intent(curAct, LoginAct.class);
            ViewUtils.startActivity(intent, curAct);
        }

    }
    private void getChartList()
    {
       final ChartProductListRequest chartProductListRequest=new ChartProductListRequest(productList);
        chartProductListRequest.setTag(this);
        chartProductListRequest.setOnResponseStateListener(this);
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                loadingView.startLoading();
                chartProductListRequest.start(ShopCartAct.this);

            }
        });
        loadingView.startLoading();
        chartProductListRequest.start(this);

    }

    @Override
    public void onClick(View v) {
        if (btnBack == v) {
            finish();
        }else if(btnToBuy==v)
        {

            AffirmOrderAct.start(this, adapter.getSelProductList(), 1, totalPrice, new ParameCallBack() {
                @Override
                public void onCall(Object object) {
                    finish();
                }
            });
        }else if(btnEdit==v)
        {
            if(isDel)
            {
                isDel=false;
                btnEdit.setText("编辑");
            }else
            {
                isDel=true;
                btnEdit.setText("完成");
            }
            adapter.setIsDel(isDel);
        }else if(btnAllSel==v)
        {
            if(isAll)
            {
                isAll=false;
            }else
            {
                isAll=true;
            }
            adapter.allSel(isAll);
            buyCount=adapter.getSelProductList().size();
            btnAllSel.setSelected(isAll);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onOK() {
        loadingView.onLoadingComplete();
        if(productList.isEmpty())
        {
            loadingView.onDataEmpty("我闹机荒啦，给我塞点东西吧~",R.drawable.kb_icon_d);
            ViewUtils.setGone(btnEdit);
        }else
        {
            ViewUtils.setVisible(btnEdit);
        	ViewUtils.setVisible(contentLayout);
            loadingView.onLoadingComplete();
            Account.orderCount=productList.size();
            adapter.addItem(productList,true);
        }
    }

    @Override
    public void onFail(int code) {
        loadingView.onLoadingFail();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode)
        {
            case  RESULT_OK:
                List<Object> buyItems=adapter.getSelItem();
                int size=buyItems.size();
                for(int i=0;i<size;i++)
                {
                    BuyItem buyItem= (BuyItem) buyItems.get(i);
                    adapter.getContent().remove(buyItem);
                    adapter.getSelProductList().removeAll(buyItem.getProductList());
                }
                adapter.getSelItem().clear();
                adapter.notifyDataSetChanged();
                if(adapter.isEmpty())
                {
                    loadingView.onDataEmpty("我闹机荒啦，给我塞点东西吧~",R.drawable.kb_icon_d);
                    ViewUtils.setGone(contentLayout);
                }else
                {
                    ViewUtils.setTextPrice(txtPrice,0.00f);
                }
                break;
            default:
                break;
        }
    }
}
