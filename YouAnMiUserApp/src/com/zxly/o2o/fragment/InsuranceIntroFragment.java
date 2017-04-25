package com.zxly.o2o.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.InsuranceAct;
import com.zxly.o2o.activity.InsuranceClauseAct;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.InsurancePrice;
import com.zxly.o2o.model.InsuranceProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.InsuranceProductRequest;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

public class InsuranceIntroFragment extends BaseFragment implements OnClickListener {
    private TextView txtInsuranceName, txtInsuranceIntro, txtInsurancePeriod, txtInsuranceTime;
    private long id;
    private String name;
    private String imgUrl;
    private ListView listView;
    private LoadingView loadingView;
    private View headerView, footerView;
    private InsurancePriceAdapter insurancePriceAdapter;
    private NetworkImageView imgInsurance;

    @Override
    protected void initView(Bundle bundle) {
        initView();
    }

    @Override
    protected void initView() {
        id = ((InsuranceAct)getActivity()).id;
        name = ((InsuranceAct) getActivity()).name;
        imgUrl = ((InsuranceAct) getActivity()).imgUrl;

        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), name);
        findViewById(R.id.btn_service).setOnClickListener(this);

        loadingView = (LoadingView) findViewById(R.id.view_loading);
        LayoutInflater layoutInflater =
                (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        headerView = layoutInflater.inflate(R.layout.win_insurance_introduce_header, null);
        footerView = layoutInflater.inflate(R.layout.win_insurance_introduce_footer, null);
        headerView.findViewById(R.id.btn_intro_detail).setOnClickListener(this);
        footerView.findViewById(R.id.btn_buy).setOnClickListener(this);
        imgInsurance = (NetworkImageView) headerView.findViewById(R.id.img_insurance);
//        imgInsurance.setDefaultImageResId(R.drawable.bg_insurance_youbao);
        imgInsurance.setImageUrl(imgUrl, AppController.imageLoader);
        txtInsuranceName = (TextView) headerView.findViewById(R.id.txt_insurance_name);
        txtInsuranceIntro = (TextView) headerView.findViewById(R.id.txt_insurance_introduction);
        txtInsurancePeriod= (TextView) headerView.findViewById(R.id.txt_insurance_period);
        txtInsuranceTime= (TextView) headerView.findViewById(R.id.txt_insurance_time);
        listView = (ListView) findViewById(R.id.list_view);
        insurancePriceAdapter = new InsurancePriceAdapter(getActivity());
        listView.addHeaderView(headerView);
        listView.addFooterView(footerView);
        listView.setAdapter(insurancePriceAdapter);
        loadData();
    }

    @Override
    protected int layoutId() {
        return R.layout.win_insurance_introduce;
    }

    private void loadData() {
        final InsuranceProductRequest request = new InsuranceProductRequest(id);
        request.setOnResponseStateListener(new ResponseStateListener() {

            @Override
            public void onOK() {
                loadingView.onLoadingComplete();
                InsuranceProduct insuranceProduct = request.getInsuranceProduct();
                int productStatus = insuranceProduct.getStatus();
                if (1 == productStatus) {
                    ViewUtils.showToast("该产品已下架");
                } else {
                    ViewUtils.setText(txtInsuranceName, insuranceProduct.getName());
                    ViewUtils.setText(txtInsuranceIntro, insuranceProduct.getIntroduction());

                    txtInsurancePeriod.setText(insuranceProduct.getServicePeriod() + "个月");
                    int compensateNum = insuranceProduct.getCompensateNum();
                    if (compensateNum == -1) {
                        txtInsuranceTime.setText("不限次数");
                    } else {
                        txtInsuranceTime.setText(compensateNum + "次");
                    }
                    insurancePriceAdapter.clear();
                    insurancePriceAdapter.addItem(insuranceProduct.getPrices(), true);
                    ViewUtils.setVisible(listView);
                }
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingFail();
            }
        });
        loadingView.startLoading();
        request.start(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getActivity().finish();
                break;
            case R.id.btn_service:
                ((InsuranceAct) getActivity()).insuranceService();
                break;
            case R.id.btn_buy:
                ((InsuranceAct) getActivity()).setPageShow(10, null);
                break;
            case R.id.btn_intro_detail:
                InsuranceClauseAct.start(getActivity(), id);
                break;
        }
    }

    class InsurancePriceAdapter extends ObjectAdapter {

        public InsurancePriceAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new ViewHolder();
                holder.viewDivider = convertView
                        .findViewById(R.id.view_divider);
                holder.txtPriceHead = (TextView) convertView
                        .findViewById(R.id.txt_insurance_price_head);
                holder.txtPrice = (TextView) convertView
                        .findViewById(R.id.txt_insurance_price);
                holder.txtRange = (TextView) convertView
                        .findViewById(R.id.txt_insurance_range);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final InsurancePrice insurancePrice = (InsurancePrice) getItem(position);
            if(position == 0){
                ViewUtils.setGone(holder.viewDivider);
                ViewUtils.setVisible(holder.txtPriceHead);
            } else {
                ViewUtils.setVisible(holder.viewDivider);
                ViewUtils.setGone(holder.txtPriceHead);
            }
            ViewUtils.setText(holder.txtPrice, insurancePrice.getUniformPrice());
            holder.txtRange.setText("(手机价格" + insurancePrice.getBeginPrice() + "~" + insurancePrice.getEndPrice() + ")");
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_insurance_price;
        }

        class ViewHolder {
            View viewDivider;
            TextView txtPriceHead, txtPrice, txtRange;
        }

    }

}
