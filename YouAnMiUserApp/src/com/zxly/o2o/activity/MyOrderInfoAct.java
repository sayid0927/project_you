package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.MyOrderAdapter;
import com.zxly.o2o.adapter.MyOrderProductsAdapter;
import com.zxly.o2o.dialog.ConfirmReceiveDialog;
import com.zxly.o2o.dialog.OrderOperateDialog;
import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyOrderInfoRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.MListView;

import java.util.Map;

/**
 * Created by wuchenhui on 2015/5/25.
 */
public class MyOrderInfoAct extends BasicAct implements View.OnClickListener{


	private View contentView;

    private TextView txtAddrType,txtNameType;

    private TextView txtOrderStatus;
    private TextView txtTakeType;

    private TextView txtPayName;
    private TextView  orderPrice,orderTruePrice;
    private TextView  userName,detailAddress,phoneNumber;
    private MListView mListView;
    private TextView orderNumber,orderMakeTime,orderFinishTime;
    private TextView btnCancleOrder,btnPayOrder;


    private View botLine,layoutOrderOperate;

    private MyOrderInfoRequest request;

    private LoadingView loadingView;
    private OrderOperateDialog orderOperateDialog;
    private OrderInfo orderInfo;
    private static ParameCallBack callBack;

    private ConfirmReceiveDialog confirmReceiveDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wim_order_info);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次进入详情都要刷新数据

        loading();
    }

    public void loading(){
        contentView.setVisibility(View.GONE);
        loadingView.startLoading();
        request.start(this);
    }

    private void init(){

        initTitle("订单详情", this);

        contentView=findViewById(R.id.view_content);
        
        loadingView= (LoadingView) findViewById(R.id.view_loading);

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                request.start(this);
            }
        });

        txtTakeType= (TextView) findViewById(R.id.txt_take_type);
        txtOrderStatus= (TextView) findViewById(R.id.txt_order_status);

        txtAddrType= (TextView) findViewById(R.id.address);
        txtNameType= (TextView) findViewById(R.id.username);

        txtPayName= (TextView) findViewById(R.id.txt_payName);
        orderPrice= (TextView) findViewById(R.id.text_order_price);
        orderTruePrice= (TextView) findViewById(R.id.text_order_ture_price);

        userName= (TextView) findViewById(R.id.text_username);
        detailAddress = (TextView) findViewById(R.id.text_detail_address);
        phoneNumber= (TextView) findViewById(R.id.text_phone_number);

        mListView= (MListView) findViewById(R.id.list_order_product);
        
        orderNumber= (TextView) findViewById(R.id.text_order_number);
        orderMakeTime= (TextView) findViewById(R.id.text_order_make_time);
        orderFinishTime= (TextView) findViewById(R.id.text_order_finish_time);

        btnCancleOrder= (TextView) findViewById(R.id.btn_cancle_order);
        btnCancleOrder.setOnClickListener(this);

        btnPayOrder= (TextView) findViewById(R.id.btn_pay_order);
        btnPayOrder.setOnClickListener(this);

        layoutOrderOperate=findViewById(R.id.layout_order_operate);
        botLine=findViewById(R.id.line_bot);
        
        Intent it=getIntent();
        String orderNumber=it.getStringExtra("orderNumber");
        request=new MyOrderInfoRequest(orderNumber);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                try {
                    if (request.getOrderInfo() == null)
                        return;

                    orderInfo = request.getOrderInfo();
                    updateData(orderInfo);
                    loadingView.onLoadingComplete();
                }catch (Exception e){
                    contentView.setVisibility(View.GONE);
                    loadingView.onLoadingFail();
                }
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingFail();
            }
        });

        confirmReceiveDialog=new ConfirmReceiveDialog();

        
       // orderBtnsLayout.setVisibility(View.VISIBLE);
       // botLine.setVisibility(View.GONE);
        //botLine.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
    }


    private void updateData(OrderInfo orderInfo){

    	contentView.setVisibility(View.VISIBLE);

        if(orderInfo.getDeliveryType()==Constants.ORDER_DELIVERY_TAKE){
            ViewUtils.setText(txtTakeType," - 到店自提");
            ViewUtils.setText(txtNameType,"提货手机  :");
            ViewUtils.setText(txtAddrType,"提货地址  :");
            ViewUtils.setText(userName,Account.user.getMobilePhone());
            ViewUtils.setText(detailAddress,orderInfo.getAddress());
            ViewUtils.setText(phoneNumber,"");
        }else{
            ViewUtils.setText(txtTakeType," - 送货上门");
            ViewUtils.setText(txtNameType,"收货人    :");
            ViewUtils.setText(txtAddrType,"收货地址  :");
            ViewUtils.setText(userName,orderInfo.getName());
            ViewUtils.setText(detailAddress,orderInfo.getAddress());
            ViewUtils.setText(phoneNumber,orderInfo.getPhone());
        }

        //设置订单当前状态
        setOrderCurrentStatus(orderInfo.getStatus());


        if(orderInfo.getStatus()==Constants.ORDER_WAIT_ROR_PAY){
            ViewUtils.setText(txtPayName,"应付款 :");
        }else{
            ViewUtils.setText(txtPayName,"实付款 :");
        }

     	/**  设置订单金额,收货信息  **/
        ViewUtils.setTextPrice(orderPrice,orderInfo.getPrices());
        ViewUtils.setTextPrice(orderTruePrice, orderInfo.getPrices());



    	/**  设置订单商品列表信息  **/
        MyOrderProductsAdapter adapter= new MyOrderProductsAdapter(this);
        adapter.setCallBack(callBack); //设置订单操作回调

        adapter.setOrderInfo(orderInfo);
        mListView.setAdapter(adapter);
        adapter.addItem(request.getOrderInfo().getBuyItems(),true);
        
        /**  设置订单单号 以及 创建,完成时间  **/
        ViewUtils.setText(orderNumber,"订单号     :   "+orderInfo.getOrderNo());
        ViewUtils.setText(orderMakeTime,"下单时间 :   "+TimeUtil.formatOrderTime(orderInfo.getCreateTime()));
      //  ViewUtils.setText(order,orderInfo.getCteateTime());
        if(orderInfo.getStatus()==Constants.ORDER_FINISH||orderInfo.getStatus()==Constants.ORDER_WAIT_CONFIRM){
            orderFinishTime.setVisibility(View.VISIBLE);
            if(orderInfo.getDeliveryType()==Constants.ORDER_DELIVERY_TAKE){
                ViewUtils.setText(orderFinishTime,"提货时间 :   "+TimeUtil.formatOrderTime(orderInfo.getDeliveryTime()));
            }else{
                ViewUtils.setText(orderFinishTime,"收货时间 :   "+TimeUtil.formatOrderTime(orderInfo.getDeliveryTime()));
            }
        }

        /**  设置订单底部可操作的按钮 目前只有 付款，删除，取消  **/
        setOrderOperateStuatus(request.getOrderInfo().getStatus());

    }


    /**
     * 设置订单可操作的状态
     *
     * **/
    private void setOrderOperateStuatus(int status){

         switch (status){

             case Constants.ORDER_WAIT_ROR_PAY:
                 ViewUtils.setVisible(layoutOrderOperate);
                 ViewUtils.setVisible(botLine);

                 ViewUtils.setVisible(btnCancleOrder);
                 ViewUtils.setVisible(btnPayOrder);
                 ViewUtils.setText(btnPayOrder,"付款");
                 break;

             case Constants.ORDER_WAIT_ROR_TAKE:
             case Constants.ORDER_SENDING:
             case Constants.ORDER_WAIT_CONFIRM:
                 if(status==Constants.ORDER_WAIT_CONFIRM&&!MyOrderAdapter.isLock(request.getOrderInfo().getBuyItems())){
                     ViewUtils.setVisible(layoutOrderOperate);
                     ViewUtils.setVisible(botLine);

                     ViewUtils.setVisible(btnPayOrder);
                     ViewUtils.setGone(btnCancleOrder);
                     ViewUtils.setText(btnPayOrder, "确认收货");
                 }else{
                     //付款后 不能取消,删除订单,只能选择在BuyItem选择退款
                     ViewUtils.setGone(botLine);
                     ViewUtils.setGone(layoutOrderOperate);
                 }
                
                 break;

             //过了售后只能删除订单
             case Constants.ORDER_CLOSE:
             case Constants.ORDER_FINISH:
                 ViewUtils.setVisible(layoutOrderOperate);
                 ViewUtils.setVisible(botLine);

                 ViewUtils.setGone(btnPayOrder);
                 ViewUtils.setVisible(btnCancleOrder);
                 ViewUtils.setText(btnCancleOrder, "删除订单");
                 break;

             default:
                 //后台返回的没经过和前端对应的状态视为异常的订单，不让用户进行操作
                 ViewUtils.setGone(botLine);
                 ViewUtils.setGone(layoutOrderOperate);
                 break;

         }

    }


    /**
     * 设置订单当前状态
     *
     * **/
    private void setOrderCurrentStatus(int orderStatus){

        switch (orderStatus){

            case Constants.ORDER_WAIT_ROR_PAY:
                ViewUtils.setText(txtOrderStatus,"待付款");
                break;

            case Constants.ORDER_WAIT_ROR_TAKE:
                if(orderInfo.getDeliveryType()==Constants.ORDER_DELIVERY_SEND){
                    ViewUtils.setText(txtOrderStatus,"等待店家送货");
                }else{
                    ViewUtils.setText(txtOrderStatus,"待提货");
                }
                break;

            case Constants.ORDER_SENDING:
                ViewUtils.setText(txtOrderStatus,"送货中");
                break;

            case Constants.ORDER_WAIT_CONFIRM:
                if(MyOrderAdapter.isLock(orderInfo.getBuyItems())){
                    ViewUtils.setText(txtOrderStatus,"待确认，剩余时间已冻结");
                }else{
                    ViewUtils.setText(txtOrderStatus,"待确认，剩余时间"+TimeUtil.getConfirmReceiveResideTime(orderInfo.getResidueTime()));
                }
                break;

            case Constants.ORDER_FINISH:
                ViewUtils.setText(txtOrderStatus,"已结束");
                break;

            default:
                //后台返回的没经过和前端对应的状态视为异常的订单，把状态设为结束
                ViewUtils.setText(txtOrderStatus,"已结束");
                break;
        }
    }


    private void initTitle(String titleName, final Activity activity){
        View backBtn=findViewById(R.id.tag_title_btn_back);
        TextView title= (TextView) findViewById(R.id.tag_title_title_name);
        title.setText(titleName);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


    public static void start(String orderNumber, Activity activity, ParameCallBack resultCallBack){
        Intent it=new Intent();
        it.setClass(activity,MyOrderInfoAct.class);
        it.putExtra("orderNumber", orderNumber);
        callBack=resultCallBack;
        ViewUtils.startActivity(it, activity);
    }

    @Override
    protected void onDestroy() {
        callBack=null;
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_cancle_order:
                final int operateType;
                if (orderInfo.getStatus() == Constants.ORDER_WAIT_ROR_PAY) {
                    operateType = Constants.ORDER_OPERATE_CANCLE;
                } else {
                    operateType = Constants.ORDER_OPERATE_DELETE;
                }

                if (orderOperateDialog == null) {
                    orderOperateDialog = new OrderOperateDialog();
                }

                orderOperateDialog.show(operateType, request.getOrderInfo(), new ParameCallBack() {
                    @Override
                    public void onCall(Object object) {
                        Map<String,Object> result= (Map<String, Object>) object;
                        Integer operaType= (Integer) result.get(Constants.ORDER_OPERATE_TYPE);
                        if(operateType==Constants.ORDER_OPERATE_CANCLE){
                            loading();
                        }else{
                            if(callBack!=null)
                                callBack.onCall(object);

                            finish();
                        }



                    }
                });
                break;

            case  R.id.btn_pay_order:
                //打开支付面（传入订单号即可）
                if(request.getOrderInfo().getStatus()==Constants.ORDER_WAIT_CONFIRM){
                    confirmReceiveDialog.show(MyOrderInfoAct.this,request.getOrderInfo(),callBack);
                }else{
                    PayAct.start(this, orderInfo.getOrderNo(),callBack);
                }
                break;


        }
    }


}
