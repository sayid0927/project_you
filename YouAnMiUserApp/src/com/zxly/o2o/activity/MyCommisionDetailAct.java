package com.zxly.o2o.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.CommisionDetailVO;
import com.zxly.o2o.model.OrderCommVO;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.CommisionDetailRequest;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Benjamin on 2015/6/5.
 */
public class MyCommisionDetailAct extends BasicAct {
    private CommisionDetailRequest commisionDetailRequest;
    private String orderCommision = "";
    private String commissionArriveTime = "";
    private static OrderCommVO orderCommVO;
    private ListView productListView;
    private ComisionProductAdapter comisionProductAdapter;
    private TextView txtTotalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mycommision_detail_layout);
        orderCommision = orderCommVO.getOrderCommission() + "";
        commissionArriveTime = orderCommVO.getTimeStr();
        productListView = (ListView) findViewById(R.id.listview);
        txtTotalPrice = (TextView) findViewById(R.id.txt_total_price);
        comisionProductAdapter = new ComisionProductAdapter(this);
        productListView.setAdapter(comisionProductAdapter);

        setUpActionBar(getActionBar());
        loadData();
    }

    private void loadData() {
        commisionDetailRequest = new CommisionDetailRequest(orderCommVO.getOrderId());
        commisionDetailRequest.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                initView();
            }

            @Override
            public void onFail(int code) {

            }
        });
        commisionDetailRequest.start();
    }

    private void initView() {

        StringBuilder sb = new StringBuilder("到账佣金");
        sb.append("     +").append(StringUtil.getFormatPrice(Float.valueOf(orderCommision)));
        SpannableString ss2 = new SpannableString(sb.toString());
        ss2.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new AbsoluteSizeSpan(DesityUtil.sp2px(this, 14)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new AbsoluteSizeSpan(DesityUtil.sp2px(this, 20)), 5, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        switch (orderCommVO.getStatus()) {

            case 1://已到账
                ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c_green_449900)), 6, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case 2://预到账
                ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 6, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case 3://订单取消
                ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.grey_aaaaaa)), 6, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss2.setSpan(new StrikethroughSpan(), 7, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                break;
        }
        StringBuilder sbTime=new StringBuilder("到账时间      ");
        sbTime.append(orderCommVO.getTimeStr());
        SpannableString ss3=new SpannableString(sbTime);
        ss3.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss3.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 5, sbTime.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss3.setSpan(new AbsoluteSizeSpan(DesityUtil.sp2px(this, 14)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss3.setSpan(new AbsoluteSizeSpan(DesityUtil.sp2px(this, 15)), 5, sbTime.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        ((TextView) findViewById(R.id.txt_commison)).setText(ss2);
        ((TextView) findViewById(R.id.txt_daozhang_time)).setText(ss3);
        ((TextView) findViewById(R.id.bottom_text)).setText(Html.fromHtml(
                String.format(getResources().getString(R.string.commision_detail_below_string),
                        orderCommVO.getOrderNo(),
                        commisionDetailRequest.commisionDetailVO.getStatusName(),
                        commisionDetailRequest.commisionDetailVO.getUserName(),
                        commisionDetailRequest.commisionDetailVO.getCreateDate())));
        comisionProductAdapter.addItem(commisionDetailRequest.commisionDetailVO.getOrderProductVO(), true);
        ViewUtils.setTextPrice(txtTotalPrice, commisionDetailRequest.commisionDetailVO.getTotalPrice());


    }

    public static void start(Activity curAct, OrderCommVO _orderCommVO) {
        orderCommVO = _orderCommVO;
        Intent intent = new Intent(curAct, MyCommisionDetailAct.class);
        ViewUtils.startActivity(intent, curAct);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderCommVO = null;
    }

    private void setUpActionBar(final ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.tag_title);
            ((TextView) actionBar.getCustomView().findViewById(R.id.tag_title_title_name))
                    .setText("佣金详情");
            findViewById(R.id.tag_title_btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private class ComisionProductAdapter extends ObjectAdapter {

        public ComisionProductAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_comision_product;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflateConvertView();
                holder.txtProductName = (TextView) convertView.findViewById(R.id.item_name);
                holder.txtPrice = (TextView) convertView.findViewById(R.id.item_price);
                holder.txtCount = (TextView) convertView.findViewById(R.id.item_sum);
                holder.icon = (NetworkImageView) convertView.findViewById(R.id.item_icon);
                holder.txtRemark = (TextView) convertView.findViewById(R.id.txt_remark);
                holder.lineBottom = convertView.findViewById(R.id.line_bottom);
                holder.txtState = (TextView) convertView.findViewById(R.id.txt_state);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final CommisionDetailVO.OrderProductVO product = (CommisionDetailVO.OrderProductVO) getItem(position);
            holder.icon.setDefaultImageResId(R.drawable.product_def);
            holder.icon.setImageUrl(product.getHeadUrl(),
                    AppController.imageLoader);
            ViewUtils.setText(holder.txtProductName, product.getProductName());
            ViewUtils.setText(holder.txtCount, "x" + product.getProductNumber());
            ViewUtils.setText(holder.txtRemark, product.getRemark());
            ViewUtils.setTextPrice(holder.txtPrice, product.getPrice());
            if (product.getStatus() == 3) {
                ViewUtils.setVisible(holder.txtState);
            } else {
                ViewUtils.setGone(holder.txtState);
            }
            if (position == getCount() - 1) {
                ViewUtils.setVisible(holder.lineBottom);

            } else {
                ViewUtils.setInvisible(holder.lineBottom);
            }
            return convertView;
        }

        class ViewHolder {
            TextView txtProductName;
            TextView txtPrice;
            TextView txtCount;
            TextView txtRemark;
            TextView txtState;
            View lineBottom;
            NetworkImageView icon;
        }
    }
}
