package com.zxly.o2o.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.GuaranteeDetailAct;
import com.zxly.o2o.activity.PayAct;
import com.zxly.o2o.activity.PaySuccessAct;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.dialog.LoadingDialog;
import com.zxly.o2o.model.GuaranteeInfo;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetMonthOrderPayRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenwu on 2016/4/27.
 */
public class GuaranteeOrderDetailFragment extends BaseFragment {

    private View contentView;

    private TextView txtStatus, btnModify;
    private ListView mListView;
    private View btnPay;
    private  LinearLayout layoutPay;
    private  byte PayType;
    private LoadingDialog loadingDialog;

    private GuaranteeInfo guaranteeInfo;
    private GetMonthOrderPayRequest monthrderPayRequest;

    public static GuaranteeOrderDetailFragment newInstance(Bundle bundle){
        GuaranteeOrderDetailFragment f=new GuaranteeOrderDetailFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected void initView() {

    }

    public void setData(GuaranteeInfo guaranteeInfo)
    {
        this.guaranteeInfo=guaranteeInfo;
    }


    @Override
    protected void initView(Bundle bundle) {

        contentView=findViewById(R.id.layout_content);
        contentView.setVisibility(View.VISIBLE);

        txtStatus= (TextView) findViewById(R.id.txt_status);
        btnModify = (TextView) findViewById(R.id.btn_modify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuaranteeDetailAct)getActivity()).loadPage(GuaranteeInfo.STATUS_MODIFY);
            }
        });
        mListView= (ListView) findViewById(R.id.mListView);
        btnPay = findViewById(R.id.btn_pay);
        layoutPay= (LinearLayout)findViewById(R.id.layout_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        PayAct.start(getActivity(), guaranteeInfo.getOrderNo(), Constants.TYPE_INSURANCE_PAY);
            }
        });
         layoutPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog();
                }
               long aa = Account.user.getShopId();
                loadingDialog.show();
                monthrderPayRequest =
                                new GetMonthOrderPayRequest(guaranteeInfo.getId(),Account.user.getShopId());
                monthrderPayRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                    @Override
                    public void onOK() {
                        ViewUtils.showToast("提交成功!");
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        getActivity().finish();
                        PaySuccessAct.start(getActivity(), 0, "payType", 0, "pay");
                    }
                    @Override
                    public void onFail(int code) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        ViewUtils.showToast("提交失败!");
                    }
                });
                monthrderPayRequest.start(this);
            }
        });

        layoutPay.setVisibility(View.GONE);
        btnPay.setVisibility(View.GONE);
        btnModify.setVisibility(View.GONE);

        refreshUI();
//        GuaranteeInfo guaranteeInfo=new GuaranteeInfo();
//        guaranteeInfo.setOrderStatus(bundle.getInt("status"));
//        guaranteeInfo.setCreateTime(System.currentTimeMillis());
//        guaranteeInfo.setOrderNo(bundle.getString("orderNo"));
//        guaranteeInfo.setPrice(55.55);
//        guaranteeInfo.setContractNum("a10000000000");
//        guaranteeInfo.setPayTime(System.currentTimeMillis());
//
//        GuaranteeInfo.InsuranceProduct insuranceProduct=new GuaranteeInfo.InsuranceProduct();
//        insuranceProduct.setName("XX保");
//        guaranteeInfo.setProduct(insuranceProduct);
//
//        GuaranteeInfo.MOrderInfo orderInfo=new GuaranteeInfo.MOrderInfo();
//        orderInfo.setUserName("小明");
//        orderInfo.setUserPhone("13246602006");
//        orderInfo.setPhoneModel("垃圾小米");
//        orderInfo.setPhoneImei("8888888888888888");
//orderInfo.setPhonePrice(666.01);
//        guaranteeInfo.setOrderInfo(orderInfo);
//
//        refreshUI(guaranteeInfo);


    }


    public void refreshUI(){
        btnPay.setVisibility(View.GONE);
        btnModify.setVisibility(View.GONE);

        List<Item> itemList=new ArrayList<Item>();


        switch (guaranteeInfo.getOrderStatus()){

            case GuaranteeInfo.STATUS_WAIT_FOR_PAY:
                txtStatus.setText("待付款");
                Drawable drawable= getActivity().getResources().getDrawable(R.drawable.daifukuan);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                txtStatus.setCompoundDrawables(drawable, null, null, null);
                txtStatus.setTextColor(0xffff5f19);

                PayType= guaranteeInfo.getPayType();

                if(PayType==0x02){
                    layoutPay.setVisibility(View.VISIBLE);
                    btnPay.setVisibility(View.GONE);
                }else
                    btnPay.setVisibility(View.VISIBLE);
                    btnModify.setVisibility(View.VISIBLE);

                itemList.add(new Item("名称", guaranteeInfo.getProduct().getName()));
                itemList.add(new Item("下单时间", TimeUtil.formatOrderTime(guaranteeInfo.getCreateTime())));
                itemList.add(new Item("订单号",guaranteeInfo.getOrderNo()));
    //            itemList.add(new Item("支付时间:",TimeUtil.formatOrderTime(guaranteeInfo.getPayTime())));
    //            itemList.add(new Item("合同号  :",guaranteeInfo.getContractNum()));
                itemList.add(new Item("购买人",guaranteeInfo.getOrderInfo().getUserName()));
                itemList.add(new Item("手机号",guaranteeInfo.getOrderInfo().getUserPhone()));
                itemList.add(new Item("品牌型号",guaranteeInfo.getOrderInfo().getPhoneModel()));
                itemList.add(new Item("IMEI",guaranteeInfo.getOrderInfo().getPhoneImei()));
                itemList.add(new Item("手机价格",StringUtil.getPrice((float) guaranteeInfo.getOrderInfo().getPhonePrice())));
                itemList.add(new Item("延保价格",StringUtil.getPrice((float) guaranteeInfo.getPrice())));
                break;

            case GuaranteeInfo.STATUS_IN_REVIEW:

                drawable= getActivity().getResources().getDrawable(R.drawable.shenhezhong);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                txtStatus.setCompoundDrawables(drawable, null, null, null);
                txtStatus.setTextColor(0xff26abd0);
                txtStatus.setText("审核中");

                itemList.add(new Item("名称", guaranteeInfo.getProduct().getName()));
                itemList.add(new Item("下单时间", TimeUtil.formatOrderTime(guaranteeInfo.getCreateTime())));
                itemList.add(new Item("订单号", guaranteeInfo.getOrderNo()));
                itemList.add(new Item("支付时间",TimeUtil.formatOrderTime(guaranteeInfo.getPayTime())));
                itemList.add(new Item("合同号",guaranteeInfo.getContractNum()));
                itemList.add(new Item("购买人",guaranteeInfo.getOrderInfo().getUserName()));
                itemList.add(new Item("手机号",guaranteeInfo.getOrderInfo().getUserPhone()));
                itemList.add(new Item("品牌型号",guaranteeInfo.getOrderInfo().getPhoneModel()));
                itemList.add(new Item("IMEI",guaranteeInfo.getOrderInfo().getPhoneImei()));
                itemList.add(new Item("手机价格",StringUtil.getPrice((float) guaranteeInfo.getOrderInfo().getPhonePrice())));
                if(PayType==0x02)
                    itemList.add(new Item("延保价格",StringUtil.getPrice((float) guaranteeInfo.getPrice())+"   (月结)"));
                if(guaranteeInfo.getAnotherPayId()==null){
                    itemList.add(new Item("延保价格",StringUtil.getPrice((float) guaranteeInfo.getPrice())));
                }else {
                    itemList.add(new Item("延保价格",StringUtil.getPrice((float) guaranteeInfo.getPrice())+"   (代付)"));
                }
                break;

            case GuaranteeInfo.STATUS_IN_GUARANTEE:
                txtStatus.setText("保障中");
                drawable= getActivity().getResources().getDrawable(R.drawable.baozhangzhong);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                txtStatus.setCompoundDrawables(drawable, null, null, null);
                txtStatus.setTextColor(0xff3fb837);

                itemList.add(new Item("名称", guaranteeInfo.getProduct().getName()));
                itemList.add(new Item("下单时间", TimeUtil.formatOrderTime(guaranteeInfo.getCreateTime())));
                itemList.add(new Item("订单号",guaranteeInfo.getOrderNo()));
                itemList.add(new Item("支付时间",TimeUtil.formatOrderTime(guaranteeInfo.getPayTime())));
                itemList.add(new Item("合同号",guaranteeInfo.getContractNum()));
                itemList.add(new Item("购买人",guaranteeInfo.getOrderInfo().getUserName()));
                itemList.add(new Item("手机号",guaranteeInfo.getOrderInfo().getUserPhone()));
                itemList.add(new Item("品牌型号",guaranteeInfo.getOrderInfo().getPhoneModel()));
                itemList.add(new Item("IMEI",guaranteeInfo.getOrderInfo().getPhoneImei()));
                itemList.add(new Item("手机价格",StringUtil.getPrice((float) guaranteeInfo.getOrderInfo().getPhonePrice())));
                itemList.add(new Item("延保价格", StringUtil.getPrice((float) guaranteeInfo.getPrice())));

                break;

            case GuaranteeInfo.STATUS_REFUNDED:

                drawable= getActivity().getResources().getDrawable(R.drawable.shixiaodan);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                txtStatus.setCompoundDrawables(drawable, null, null, null);
                txtStatus.setTextColor(0xffaaaaaa);
                txtStatus.setText("已退单");

                itemList.add(new Item("名称", guaranteeInfo.getProduct().getName()));
                itemList.add(new Item("下单时间", TimeUtil.formatOrderTime(guaranteeInfo.getCreateTime())));
                itemList.add(new Item("订单号",guaranteeInfo.getOrderNo()));
                itemList.add(new Item("支付时间",TimeUtil.formatOrderTime(guaranteeInfo.getPayTime())));
                itemList.add(new Item("合同号",guaranteeInfo.getContractNum()));
                itemList.add(new Item("购买人",guaranteeInfo.getOrderInfo().getUserName()));
                itemList.add(new Item("手机号",guaranteeInfo.getOrderInfo().getUserPhone()));
                itemList.add(new Item("品牌型号",guaranteeInfo.getOrderInfo().getPhoneModel()));
                itemList.add(new Item("IMEI",guaranteeInfo.getOrderInfo().getPhoneImei()));
                itemList.add(new Item("手机价格",StringUtil.getPrice((float) guaranteeInfo.getOrderInfo().getPhonePrice())));
                itemList.add(new Item("延保价格",StringUtil.getPrice((float) guaranteeInfo.getPrice())));
                itemList.add(new Item("退单时间", TimeUtil.formatOrderTime(guaranteeInfo.getCreateTime())));

                break;

            case GuaranteeInfo.STATUS_PAY_TIMEOUT:

                drawable= getActivity().getResources().getDrawable(R.drawable.shixiaodan);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                txtStatus.setCompoundDrawables(drawable, null, null, null);
                txtStatus.setTextColor(0xffaaaaaa);
                txtStatus.setText("支付超时");

                itemList.add(new Item("名称", guaranteeInfo.getProduct().getName()));
                itemList.add(new Item("下单时间", TimeUtil.formatOrderTime(guaranteeInfo.getCreateTime())));
                itemList.add(new Item("订单号",guaranteeInfo.getOrderNo()));
     //           itemList.add(new Item("支付时间:",TimeUtil.formatOrderTime(guaranteeInfo.getPayTime())));
    //            itemList.add(new Item("合同号  :",guaranteeInfo.getContractNum()));
                itemList.add(new Item("购买人",guaranteeInfo.getOrderInfo().getUserName()));
                itemList.add(new Item("手机号",guaranteeInfo.getOrderInfo().getUserPhone()));
                itemList.add(new Item("品牌型号",guaranteeInfo.getOrderInfo().getPhoneModel()));
                itemList.add(new Item("IMEI",guaranteeInfo.getOrderInfo().getPhoneImei()));
                itemList.add(new Item("手机价格",StringUtil.getPrice((float) guaranteeInfo.getOrderInfo().getPhonePrice())));
                itemList.add(new Item("延保价格", StringUtil.getPrice((float) guaranteeInfo.getPrice())));
                break;

            case GuaranteeInfo.STATUS_OVERDUE:
                txtStatus.setText("已过期");
                drawable= getActivity().getResources().getDrawable(R.drawable.shixiaodan);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                txtStatus.setCompoundDrawables(drawable, null, null, null);
                txtStatus.setTextColor(0xffaaaaaa);

                itemList.add(new Item("名称", guaranteeInfo.getProduct().getName()));
                itemList.add(new Item("下单时间", TimeUtil.formatOrderTime(guaranteeInfo.getCreateTime())));
                itemList.add(new Item("订单号",guaranteeInfo.getOrderNo()));
                itemList.add(new Item("支付时间",TimeUtil.formatOrderTime(guaranteeInfo.getPayTime())));
                itemList.add(new Item("合同号",guaranteeInfo.getContractNum()));
                itemList.add(new Item("购买人",guaranteeInfo.getOrderInfo().getUserName()));
                itemList.add(new Item("手机号",guaranteeInfo.getOrderInfo().getUserPhone()));
                itemList.add(new Item("品牌型号",guaranteeInfo.getOrderInfo().getPhoneModel()));
                itemList.add(new Item("IMEI",guaranteeInfo.getOrderInfo().getPhoneImei()));
                itemList.add(new Item("手机价格",StringUtil.getPrice((float) guaranteeInfo.getOrderInfo().getPhonePrice())));
                itemList.add(new Item("延保价格", StringUtil.getPrice((float) guaranteeInfo.getPrice())));
                break;

            default:
                break;

        }

        ItemListAdapter adapter=new ItemListAdapter(getActivity());
        mListView.setAdapter(adapter);
        adapter.addItem(itemList,true);

    }

    @Override
    protected int layoutId() {
        return R.layout.win_guarantee_detail;
    }


    static class ItemListAdapter extends ObjectAdapter{

        public ItemListAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_guarantee_info;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView=inflateConvertView();
                holder=new ViewHolder();
                holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
                holder.txtValue= (TextView) convertView.findViewById(R.id.txt_value);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }

            Item item= (Item) getItem(position);
            holder.txtName.setText(item.getName());
            holder.txtValue.setText(item.getValue());


            return convertView;
        }

        static class ViewHolder{
            TextView txtName,txtValue;

        }
    }


    static class Item {
        private String name;
        private String value;

        public Item(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
