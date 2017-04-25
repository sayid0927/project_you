package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.InsuranceProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.InsuranceListRequest;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * @author fengrongjian 2016-4-26
 * @description 保修服务
 */
public class InsuranceListAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private ListView listView;
    private InsuranceAdapter insuranceAdapter;
    private LoadingView loadingview;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_insurance_list);
        context = this;
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("保障服务");
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        LayoutInflater layoutInflater =
                (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        headerView = layoutInflater.inflate(R.layout.layout_insurance_list_header, null);
        headerView.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.list_view);
        insuranceAdapter = new InsuranceAdapter(context);
        listView.addHeaderView(headerView);
        listView.setAdapter(insuranceAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, InsuranceListAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void loadData() {
        final InsuranceListRequest request = new InsuranceListRequest(Config.shopId);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.setVisible(listView);
                insuranceAdapter.clear();
                insuranceAdapter.addItem(request.getInsuranceProductList(), true);
                loadingview.onLoadingComplete();
            }

            @Override
            public void onFail(int code) {
                ViewUtils.setGone(listView);
                loadingview.onLoadingFail();
            }
        });
        loadingview.startLoading();
        request.start(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();

                break;
            case R.id.header_view:
                RenewListAct.start(InsuranceListAct.this);
                break;

        }
    }

    class InsuranceAdapter extends ObjectAdapter {

        public InsuranceAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new ViewHolder();
                holder.imgInsurance = (NetworkImageView) convertView.findViewById(R.id.img_insurance_local);
                holder.imgType = (ImageView) convertView.findViewById(R.id.img_type);
                holder.txtTitle = (TextView) convertView
                        .findViewById(R.id.txt_insurance_title);
                holder.txtContent = (TextView) convertView
                        .findViewById(R.id.txt_insurance_content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final InsuranceProduct insuranceProduct = (InsuranceProduct) getItem(position);
//            holder.imgInsurance.setDefaultImageResId(R.drawable.bg_insurance_youbao);
            holder.imgInsurance.setImageUrl(insuranceProduct.getImage(),
                    AppController.imageLoader);

            ViewUtils.setText(holder.txtTitle, insuranceProduct.getName());
//            holder.txtTitle.setText(insuranceProduct.getName() + "(" + insuranceProduct.getOrderStatus() + ")");
            ViewUtils.setText(holder.txtContent, insuranceProduct.getIntroduction());
            final int orderStatus = insuranceProduct.getOrderStatus();
            if (orderStatus == 7 || orderStatus == 6) {
                holder.imgType.setImageResource(R.drawable.icon_has_insurance);
            } else {
                holder.imgType.setImageResource(R.drawable.icon_limit_new);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InsuranceAct.start(InsuranceListAct.this, insuranceProduct);
                }
            });
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_insurance;
        }

        class ViewHolder {
            NetworkImageView imgInsurance;
            ImageView imgType;
            TextView txtTitle, txtContent;
        }
    }
}
