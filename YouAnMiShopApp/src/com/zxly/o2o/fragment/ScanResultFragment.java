package com.zxly.o2o.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.MyBenefitsAct;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import org.json.JSONObject;

/**
 * Created by kenwu on 2016/6/14.
 */
public class ScanResultFragment extends BaseFragment implements View.OnClickListener {

    public static final int COUPON_TYPE_CASH=1;
    public static final int COUPON_TYPE_GIFT=2;

    public static final int USR_CONDITION_PAY_ANYWAY=1;
    public static final int USR_CONDITION_PAY_SPECIFIED_AMOUNT =2;

    private CouponInfoRequest couponInfoRequest;
    private CouponUseRequest couponUseRequest;

    private LoadingView loadingView;

    private TextView txtTitle,txtDesc,txtType,txtAvailableTime,txtShopName,txtAddress,txtPhoneNo;

    private View btnConfirm,contentView,layoutCouponInfo;

    String id;

    public static ScanResultFragment newInstance(Bundle args){
        ScanResultFragment f=new ScanResultFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initView(Bundle bundle) {
        id=bundle.getString("id");

        loadingView= (LoadingView) findViewById(R.id.view_loading);
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                couponInfoRequest.start(getActivity());
            }
        });
        loadingView.startLoading();

        contentView=findViewById(R.id.layout_content);
        contentView.setVisibility(View.GONE);

        layoutCouponInfo=findViewById(R.id.layout_top);
        txtTitle= (TextView) findViewById(R.id.txt_price);
        txtDesc= (TextView) findViewById(R.id.txt_desc);
        txtType= (TextView) findViewById(R.id.txt_type);
        txtAvailableTime= (TextView) findViewById(R.id.txt_available_time);
        txtShopName= (TextView) findViewById(R.id.txt_shop_name);
        txtPhoneNo= (TextView) findViewById(R.id.txt_phoneNo);
        txtAddress= (TextView) findViewById(R.id.txt_address);

        btnConfirm=findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        if(couponInfoRequest==null){
            couponInfoRequest=new CouponInfoRequest(id);
            couponInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    //不知道服务端有没做处理，这边先判断
                    if(!StringUtil.isNull(couponInfoRequest.getId())&&
                       couponInfoRequest.getShopId()== Account.user.getShopId()){

                        contentView.setVisibility(View.VISIBLE);
                        loadingView.onLoadingComplete();
                        String title="";
                        String desc="";
                        if(couponInfoRequest.getDiscountType()==COUPON_TYPE_CASH){
                            layoutCouponInfo.setBackgroundResource(R.drawable.bg_cash_coupon_2);
                            title=couponInfoRequest.getDiscountInfo();
                            txtTitle.setText(title);
                            txtType.setText("现金抵扣");
                            txtType.setTextColor(0xff06766a);

                            if(couponInfoRequest.getPriceType()==USR_CONDITION_PAY_ANYWAY){
                                desc="到店任意消费立减"+couponInfoRequest.getDiscountInfo();
                            }else if(couponInfoRequest.getPriceType()==USR_CONDITION_PAY_SPECIFIED_AMOUNT){
                                desc="到店消费满"+couponInfoRequest.getPrice()+"元立减"+title;
                            }
                        }else if(couponInfoRequest.getDiscountType()==COUPON_TYPE_GIFT){
                            layoutCouponInfo.setBackgroundResource(R.drawable.bg_cash_coupon_1);
                            txtTitle.setText(couponInfoRequest.getDiscountInfo());
                            txtType.setText("礼品赠送");
                            txtType.setTextColor(0xff9a1355);

                            if(couponInfoRequest.getPriceType()==USR_CONDITION_PAY_ANYWAY){
                                desc="到店任意消费立送"+couponInfoRequest.getDiscountInfo();
                            }else if(couponInfoRequest.getPriceType()==USR_CONDITION_PAY_SPECIFIED_AMOUNT){
                                desc="到店消费满"+couponInfoRequest.getPrice()+"元立送"+couponInfoRequest.getDiscountInfo();
                            }
                        }

                        txtDesc.setText(desc);

                        String availableTime="有效期 :  "+ TimeUtil.formatTimeHHMMDD(couponInfoRequest.getStartTime())+" ~ "+TimeUtil.formatTimeHHMMDD(couponInfoRequest.getEndTime());
                        txtAvailableTime.setText(availableTime);

                        txtShopName.setText(couponInfoRequest.getShopName());
                        txtAddress.setText(couponInfoRequest.getAddress());
                        txtPhoneNo.setText(couponInfoRequest.getPhoneNo());
                    }else {
                        loadingView.onLoadingFail();
                        ViewUtils.showToast("无效的优惠券！");
                        getActivity().finish();
                    }
                }

                @Override
                public void onFail(int code) {

                }


            });
        }

        couponInfoRequest.start(getActivity());
    }

    @Override
    protected int layoutId() {
        return R.layout.win_scan_result;
    }

    @Override
    public void onClick(View v) {
        if(Config.currentServerTime>couponInfoRequest.getStartTime()&&
           Config.currentServerTime<couponInfoRequest.getEndTime()){
            if(couponUseRequest==null){
                couponUseRequest=new CouponUseRequest(id);
                couponUseRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                    @Override
                    public void onOK() {
                        ViewUtils.showToast("优惠券使用成功");
                        Intent it=new Intent();
                        it.putExtra("curTab", 1);
                        it.setClass(getActivity(), MyBenefitsAct.class);
                        ViewUtils.startActivity(it,getActivity());
                        getActivity().finish();
                    }

                    @Override
                    public void onFail(int code) {
                        ViewUtils.showToast("优惠券使用失败");
                    }
                });
            }
            couponUseRequest.start(this);
        }else if(Config.currentServerTime<couponInfoRequest.getStartTime()){
            ViewUtils.showToast("优惠券还没到使用期！");
        }else if (Config.currentServerTime>couponInfoRequest.getEndTime()){
            ViewUtils.showToast("优惠券已过期！");
        }

    }


    static class CouponInfoRequest extends BaseRequest{

        private String id;
        private String discountInfo;
        private int discountType;          //Byte 1.现金折扣 2.礼品赠送
        private int priceType; //1.代表任意消费,2.代表消费指定金额才能享受优惠
        private long startTime;
        private long endTime;

        private String shopName;
        private String address;
        private String phoneNo;
        private long shopId;

        private int price; //消费金额


        public CouponInfoRequest(String id){
            addParams("id",id);
        }


        public String getId() {
            return id;
        }

        public String getDiscountInfo() {
            return discountInfo;
        }


        public int getPrice() {
            return price;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public int getDiscountType() {
            return discountType;
        }


        public long getShopId() {
            return shopId;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public String getAddress() {
            return address;
        }

        public String getShopName() {
            return shopName;
        }

        public int getPriceType() {
            return priceType;
        }

        @Override
        protected void fire(String data) throws AppException {
            try {
                JSONObject jsonRoot=new JSONObject(data);
                id=jsonRoot.optString("id");
                startTime=jsonRoot.optLong("startTime");
                endTime=jsonRoot.optLong("endTime");
                discountInfo=jsonRoot.optString("discountInfo");
                discountType=jsonRoot.optInt("discountType");
                shopId=jsonRoot.optLong("shopId");
                shopName=jsonRoot.optString("shopName");
                address=jsonRoot.optString("shopAddress");
                phoneNo=jsonRoot.optString("mobilePhone");
                priceType=jsonRoot.optInt("priceType");
                price =jsonRoot.optInt("price");
            }catch (Exception e){
                throw new AppException(e.toString());
            }
        }

        @Override
        protected String method() {
            return "/discount/coupon/info";
        }

    }


    static class CouponUseRequest extends BaseRequest{

        public CouponUseRequest(String id){
            addParams("id",id);
        }

        @Override
        protected boolean isShowLoadingDialog() {
            return true;
        }

        @Override
        protected String method() {
            return "/discount/coupon/use";
        }

    }


}
