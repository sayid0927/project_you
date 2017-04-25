package com.zxly.o2o.adapter;

/**
 * Created by wuchenhui on 2015/5/29.
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.activity.MyOrderAct;
import com.zxly.o2o.dialog.InputTakeNoDialog;
import com.zxly.o2o.model.BuyItem;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.KeyBoardUtils;
import com.zxly.o2o.util.PhoneUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表
 * */
public class MyOrderAdapter extends ObjectAdapter implements OnClickListener,OnTouchListener {

    private Activity activity;
    private int orderType;
    InputTakeNoDialog inputTakeNoDialog;
    public MyOrderAdapter(Activity activity) {
        super(activity);
        this.activity=activity;
        inputTakeNoDialog=new InputTakeNoDialog();
    }
    
    public void setOrderType(int orderType) {
      //  activity.getCurrentFocus().setOnTouchListener(this);
		this.orderType = orderType;
	}

//
//    @Override
//    public int getCount() {
//        return 6;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

      	OrderInfo orderInfo=(OrderInfo) getItem(position);
        ViewHolder holder=null;
        if(convertView==null){
            convertView=inflateConvertView();
            holder=new ViewHolder();
            convertView.setId(666666);

            holder.viewStub=(ViewStub) convertView.findViewById(R.id.layout_bot);
            if(orderType==MyOrderAct.TYPE_ORDER_SENDING){
                holder.viewStub.setLayoutResource(R.layout.tag_sending);
                holder.viewStub.inflate();

                holder.btnInputNo=convertView.findViewById(R.id.btn_inputTakeNo);
                holder.btnInputNo.setOnClickListener(this);


                holder.btnCall=convertView.findViewById(R.id.btn_call);
                holder.btnCall.setOnClickListener(this);

            }else if(orderType==MyOrderAct.TYPE_ORDER_SENDED){
            	holder.viewStub.setLayoutResource(R.layout.tag_sended);
            	holder.viewStub.inflate();

                holder.btnCall=convertView.findViewById(R.id.btn_call);
                holder.btnCall.setOnClickListener(this);
                holder.txtTakeNo= (TextView) convertView.findViewById(R.id.txt_takeNo);
            }else{
                holder.viewStub.setLayoutResource(R.layout.tag_order_nomal);
                holder.viewStub.inflate();

            }

            holder.txtUserName= (TextView) convertView.findViewById(R.id.txt_userInfo);
            holder.txtAddress= (TextView) convertView.findViewById(R.id.txt_address);

            holder.txtOrderNo=(TextView) convertView.findViewById(R.id.txt_orderNo);
            holder.txtStatus=(TextView) convertView.findViewById(R.id.txt_status);
            holder.txtPrice=(TextView) convertView.findViewById(R.id.txt_price);
            holder.imgType= (ImageView) convertView.findViewById(R.id.img_type);

            holder.mList=(MListView) convertView.findViewById(R.id.listView);
            holder.mList.setGetTouchEvent(false);

            MyOrderProductsAdapter adapter=new MyOrderProductsAdapter(activity);
            holder.mList.setAdapter(adapter);
            convertView.setTag(holder);

        }else{
        	holder=(ViewHolder) convertView.getTag();
        }

        if(orderType==MyOrderAct.TYPE_ORDER_SENDING){
        	fillSendingData(holder, orderInfo,position);
        }else if(orderType==MyOrderAct.TYPE_ORDER_SENDED){
        	fillSendedData(holder, orderInfo);
        }else{
        	fillOrderData(holder, orderInfo);
        }

        convertView.setOnTouchListener(this);
        return convertView;
    }


    private void fillOrderData(ViewHolder holder, OrderInfo orderInfo){
    	holder.orderInfo=orderInfo;

        ViewUtils.setText(holder.txtOrderNo, "订单号 : " + orderInfo.getOrderNo());
        ViewUtils.setText(holder.txtStatus, getOrderStatus(orderInfo));
        ViewUtils.setTextPrice(holder.txtPrice, orderInfo.getPrices());
        if(orderInfo.getDeliveryType()==Constants.ORDER_DELIVERY_SEND){
            holder.imgType.setImageResource(R.drawable.icon_song);
            holder.txtUserName.setText(orderInfo.getName()+" ("+orderInfo.getPhone()+")");
            holder.txtAddress.setText(orderInfo.getAddress());
        }else {
            holder.imgType.setImageResource(R.drawable.icon_ti);
            ViewUtils.setTextPrice(holder.txtPrice, orderInfo.getPrices());
            holder.txtUserName.setText("客户自提");
            holder.txtAddress.setText("");
        }

        ((MyOrderProductsAdapter) holder.mList.getAdapter()).setOrderInfo(orderInfo);
        ((ObjectAdapter)holder.mList.getAdapter()).clear();
        if(!DataUtil.listIsNull(orderInfo.getBuyItems()))
        ((ObjectAdapter)holder.mList.getAdapter()).addItem(orderInfo.getBuyItems(), true);

    }

    private void fillSendingData(final ViewHolder holder, OrderInfo orderInfo, final int pos){
    	fillOrderData(holder, orderInfo);
        holder.btnCall.setTag(orderInfo.getPhone());
        holder.btnInputNo.setTag(orderInfo);
        holder.btnCall.setTag(orderInfo.getPhone());
        //	Spanned str=Html.fromHtml("<font color=\"#999999\">请输入收件人 </font><font color=\"#198dfb\">"+orderInfo.getPhone()+"</font><font color=\"#999999\"> 的取货码 </font>");
    //	holder.txtOrderInput.setHint("请输入取货码");
    }

    private void fillSendedData(ViewHolder holder, OrderInfo orderInfo){
    	fillOrderData(holder, orderInfo);
        holder.btnCall.setTag(orderInfo.getPhone());
    	ViewUtils.setText(holder.txtTakeNo, "提货码 ："+orderInfo.getDeliveryCode());
    }

    /**
     * 根据statucode 返回当前状态
     */
    private String getOrderStatus(OrderInfo orderInfo){
        String statusName=null;
        int status=orderInfo.getStatus();
        switch (status){
            case Constants.ORDER_WAIT_ROR_PAY:
                statusName= "待付款";
                break;
            case Constants.ORDER_WAIT_ROR_TAKE:
                if(orderInfo.getDeliveryType()==Constants.ORDER_DELIVERY_TAKE){
                    statusName= "待提货";
                }else{
                    statusName= "待发货";
                }
                break;

            case Constants.ORDER_SENDING:
                statusName= "送货中";
                break;
            case Constants.ORDER_WAIT_CONFIRM:
                statusName= "已收货";
                break;
            case Constants.ORDER_FINISH:
                statusName= "已结束";
                break;
            case Constants.ORDER_CLOSE:
                statusName= "已关闭";
                break;
            case Constants.ORDER_REFUNDING:
                statusName= "退款中";
                break;
            case Constants.ORDER_REFUND_SUCCESS:
                statusName= "已退款";
                break;
            default:
                break;
        }

        return  statusName;
    }

    
    @Override
    public int getLayoutId() {
        return R.layout.item_myorder;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        KeyBoardUtils.hideKeyBord(activity);
        return false;
    }


    /**订单viewHolder**/
    class ViewHolder{
    	TextView txtOrderNo;
        ImageView imgType;
        TextView txtStatus;
        MListView mList;
        TextView txtPrice;
        TextView txtUserName;
        TextView txtAddress;
        TextView txtTakeNo;
        View btnCall,btnInputNo;
        ViewStub  viewStub;
        OrderInfo orderInfo;
    }

    
	@Override
	public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_call:
                String phone= (String) v.getTag();
                if(!DataUtil.stringIsNull(phone)){
                    PhoneUtil.openPhoneKeyBord(phone,context);
                }else {
                    ViewUtils.showToast("电话号码不能为空!");
                }

                break;

            case R.id.btn_inputTakeNo:
                final OrderInfo orderInfo= (OrderInfo) v.getTag();
                inputTakeNoDialog.show(orderInfo.getOrderNo(), new CallBack() {
                    @Override
                    public void onCall() {
                        getContent().remove(orderInfo);
                        notifyDataSetChanged();
                    }
                });
                break;

            default:
                break;
        }

	}


}
