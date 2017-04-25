package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.CommissionProduct;
import com.zxly.o2o.model.YieldDetail;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.YieldDetailSingleRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2015/12/16.
 */
public class ProductCommissionDetailAct extends  BasicAct {
    private ListView goodsListview;
    private ProductCommissionAdapter adapter;
    private String serialNumber;
    private LoadingView loadingview;
    private View btnBack;
    private TextView txtSerialNumber,txtMakeAccount,txtOrderNumbert,txtOrderTime;
    private TextView txtMoney,txtDzTime;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_product_commission);
        btnBack=findViewById(R.id.btn_back);
        setOnBack(btnBack);
        goodsListview= (ListView) findViewById(R.id.goods_listview);
        ViewGroup head= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.head_product_commission, null);
        ViewGroup footer= (ViewGroup) LayoutInflater.from(this).inflate(R.layout.footer_commission, null);
        txtSerialNumber= (TextView) footer.findViewById(R.id.txt_serialNumber);
        txtMakeAccount= (TextView) footer.findViewById(R.id.txt_makeAccount);
        txtOrderNumbert= (TextView) footer.findViewById(R.id.txt_orderNumber);
        txtOrderTime=(TextView)footer.findViewById(R.id.txt_orderTime);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        txtMoney=(TextView)head.findViewById(R.id.txt_money);
        txtDzTime=(TextView)head.findViewById(R.id.txt_dz_time);
        serialNumber=getIntent().getStringExtra("serialNumber");
        id=getIntent().getIntExtra("id",0);
        adapter=new ProductCommissionAdapter(this);
        goodsListview.setDividerHeight(0);
        goodsListview.addHeaderView(head);
        goodsListview.addFooterView(footer);
        goodsListview.setAdapter(adapter);
        loadingview.startLoading();
        final YieldDetailSingleRequest yieldDetailSingleRequest=new YieldDetailSingleRequest(id,serialNumber, YieldDetail.YIEL_TYPE_PROUDCT);
        yieldDetailSingleRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingview.onLoadingComplete();
                ViewUtils.setVisible(goodsListview);
                switch (yieldDetailSingleRequest.getStatus())
                {
                    //1待付款 2待提货3待确认提货4交易成功5关闭 6结束
                    case 2:
                    case 3:
                    case 4:
                        Drawable nav_up=getResources().getDrawable(R.drawable.dongjiezhong_ioc);
                        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                        txtMoney.setCompoundDrawables(null, null, nav_up, null);
                        txtMoney.setTextColor(getResources().getColor(R.color.gray_999999));
                        txtDzTime.setText("预到账时间：" + TimeUtil.formatTimeHHMMDD(yieldDetailSingleRequest.getResidueTime()));
                        break;
                }
                ViewUtils.setText(txtMoney, "+ "+yieldDetailSingleRequest.getCommission()+" ");
                adapter.addItem(yieldDetailSingleRequest.getCommissionProductList());
                txtSerialNumber.setText("交易流水号：" + yieldDetailSingleRequest.getNumberNo());
                txtMakeAccount.setText("下单账号    ："+yieldDetailSingleRequest.getUserAccount());
                txtOrderNumbert.setText ("订单号        ："+yieldDetailSingleRequest.getOrderNo());
                txtOrderTime.setText("订单时间    ："+TimeUtil.formatTimeYMDHMS(yieldDetailSingleRequest.getPayTime()));

            }
            @Override
            public void onFail(int code) {
                loadingview.onDataEmpty("加载失败",true);
            }
        });
        yieldDetailSingleRequest.start(this);
    }
    public static void start(Activity curAct,String serialNumber,int id){
        Intent intent=new Intent();
        intent.putExtra("serialNumber",serialNumber);
        intent.putExtra("id",id);
        intent.setClass(curAct, ProductCommissionDetailAct.class);
        ViewUtils.startActivity(intent, curAct);
    }


    private  class ProductCommissionAdapter extends ObjectAdapter{

        public ProductCommissionAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_product_commission;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null)
            {
                convertView=inflateConvertView();
                holder=new ViewHolder();
                holder.imgProduct= (NetworkImageView) convertView.findViewById(R.id.img_product);
                holder.txtProductName= (TextView) convertView.findViewById(R.id.txt_productName);
                holder.txtRemark = (TextView) convertView.findViewById(R.id.txt_remark);
                holder.txtPrice= (TextView) convertView.findViewById(R.id.txt_price);
                holder.txtCount= (TextView) convertView.findViewById(R.id.txt_count);
                holder.txtCommissionPrice= (TextView) convertView.findViewById(R.id.txt_commission_price);
                holder.txtStatus= (TextView) convertView.findViewById(R.id.txt_status);
                convertView.setTag(holder);
            }else
            {
                holder= (ViewHolder) convertView.getTag();
            }
            CommissionProduct cp= (CommissionProduct) getItem(position);
            holder.imgProduct.setImageUrl(cp.getHeadUrl(), AppController.imageLoader);
            holder.txtProductName.setText(cp.getName());
            holder.txtRemark.setText(cp.getRemark());
            ViewUtils.setTextPrice(holder.txtPrice, cp.getPrice());
            if(Account.user.getRoleType()!=1)
            {
                holder.txtCommissionPrice.setText("佣金：￥" + StringUtil.getFormatPrice(cp.getCommission()));
            }

            switch (cp.getStatus())
            {
                case 1://正常
                    ViewUtils.setGone(holder.txtStatus);
                    break;
                case 2://退款中
                    ViewUtils.setVisible(holder.txtStatus);
                    holder.txtStatus.setText("退款中");
                    break;
                case 3://退款完成
                    ViewUtils.setVisible(holder.txtStatus);
                    holder.txtStatus.setText("已退款");
                    break;
            }
            holder.txtCount.setText("x"+cp.getPcs());
            if(position==(getCount()-1))
            {
                ViewUtils.setGone(convertView,R.id.line);
            }
            return convertView;
        }

        private class ViewHolder{
            NetworkImageView imgProduct;
            TextView txtProductName, txtRemark,txtPrice,txtCount;
            TextView txtCommissionPrice,txtStatus;
        }

    }
}
