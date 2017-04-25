package com.zxly.o2o.adapter;

/**
 * Created by wuchenhui on 2015/5/29.
 */

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.activity.MyOrderInfoAct;
import com.zxly.o2o.activity.PayAct;
import com.zxly.o2o.dialog.ConfirmReceiveDialog;
import com.zxly.o2o.dialog.OrderOperateDialog;
import com.zxly.o2o.model.BuyItem;
import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyOrderInfoRequest;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MListView;

import java.util.List;
import java.util.Map;

/**
 * 订单列表
 * */
public class MyOrderAdapter extends ObjectAdapter implements OnClickListener,ParameCallBack {

    private OrderOperateDialog orderOperateDialog;

    private ConfirmReceiveDialog confirmReceiveDialog;

    private Activity activity;

    private int curStatus;

    public MyOrderAdapter(Activity activity,int curStatus) {
        super(activity);
        this.activity=activity;
        this.curStatus=curStatus;
       // MyOrderAct.callBack=this;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //	OrderInfo orderInfo=(OrderInfo) getItem(position);
        ViewHolder holder=null;

        if(convertView==null){
            convertView=inflateConvertView();
            convertView.setId(R.id.view_container);
            
            holder=new ViewHolder();
            convertView.setTag(holder);

            holder.imageSendType=(ImageView) convertView.findViewById(R.id.image_send_type);
            holder.txtOrderNumber=(TextView) convertView.findViewById(R.id.text_order_number);
            holder.txtOrderStatus=(TextView) convertView.findViewById(R.id.text_order_status);
            holder.mList=(MListView) convertView.findViewById(R.id.list_order_product);
            holder.mList.setGetTouchEvent(false);


            MyOrderProductsAdapter adapter=new MyOrderProductsAdapter(activity);
            adapter.setCallBack(this);
            adapter.setInOrderList(true);
            holder.mList.setAdapter(adapter);

            holder.txtOrderPrice=(TextView) convertView.findViewById(R.id.text_order_ture_price);
            holder.btnCancleOrder=(TextView) convertView.findViewById(R.id.btn_cancle_order);
            holder.btnCancleOrder.setTag(holder);
            holder.btnPayOrder=(TextView) convertView.findViewById(R.id.btn_pay_order);
            holder.btnPayOrder.setTag(holder);
            holder.container=convertView.findViewById(R.id.view_container);
            
        }else{
        	holder=(ViewHolder) convertView.getTag();
        }

        fillData(convertView, holder, (OrderInfo) getItem(position));

        return convertView;
    }


    private void fillData(View convertView, ViewHolder holder,OrderInfo orderInfo){
    	holder.orderInfo=orderInfo;
    	
        if (orderInfo.getDeliveryType() == Constants.ORDER_DELIVERY_SEND){
            holder.imageSendType.setImageResource(R.drawable.icon_song);
        }else{
            holder.imageSendType.setImageResource(R.drawable.icon_ti);
        }

        setOrderbtns(orderInfo.getStatus(),holder);

        ViewUtils.setText(holder.txtOrderNumber, "订单号 : "+orderInfo.getOrderNo());
        ViewUtils.setText(holder.txtOrderStatus, getOrderStatus(orderInfo));
        ViewUtils.setTextPrice(holder.txtOrderPrice, orderInfo.getPrices());
    //    MyOrderBtnClickListener listener=new MyOrderBtnClickListener(orderInfo);
        convertView.setOnClickListener(this);
        holder.btnPayOrder.setOnClickListener(this);
        holder.btnCancleOrder.setOnClickListener(this);
 
        ((MyOrderProductsAdapter)holder.mList.getAdapter()).setOrderInfo(orderInfo);
        ((ObjectAdapter)holder.mList.getAdapter()).clear();

        if(!DataUtil.listIsNull(orderInfo.getBuyItems()))
        ((ObjectAdapter)holder.mList.getAdapter()).addItem(orderInfo.getBuyItems(), true);
     
    }


    /**
     * 根据statucode 返回当前状态
     *
     * **/
    private String getOrderStatus(OrderInfo orderInfo){
        String statusName=null;
        int status=orderInfo.getStatus();
        switch (status){
            case Constants.ORDER_WAIT_ROR_PAY:
                statusName= "待付款";
                break;
            case Constants.ORDER_WAIT_ROR_TAKE:
                if(orderInfo.getDeliveryType()== Constants.ORDER_DELIVERY_TAKE){
                    statusName= "待提货";
                }else{
                    statusName= "等待卖家送货";
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
            default:
                break;
        }

        return  statusName;
    }


    /**
     * 如果订单里面其中一个buyItem是退款状态，那么这个订单就是锁定状态
     *
     * **/
    public static boolean isLock(List<BuyItem> itemList){
        if(!DataUtil.listIsNull(itemList)){
            for (BuyItem item :itemList) {
                if(item.getStatus()== Constants.PRODUCT_STATE_FERUNDING)
                    return true;
            }
        }

        return false;
    }



    private void updateOrderList(String orderNo,int operaType){
        if(DataUtil.listIsNull(getContent()))
            return;

   //     Log.d("onCall"," type:"+operaType+" --orderNo :"+orderNo);
        for (int i=0;i<getContent().size();i++){
            final OrderInfo orderInfo= (OrderInfo) getContent().get(i);
            if(orderInfo.getOrderNo().equals(orderNo)){
                switch (operaType){
                    case Constants.ORDER_OPERATE_PAY:
                        if(curStatus!= Constants.ORDER_REQUEST_ALL){
                            getContent().remove(orderInfo);
                            notifyDataSetChanged();
                            return;
                        }
                        orderInfo.setStatus(Constants.ORDER_WAIT_ROR_TAKE);
                        break;

                    case Constants.ORDER_OPERATE_CANCLE:
                        if(curStatus!= Constants.ORDER_REQUEST_ALL){
                            getContent().remove(orderInfo);
                            notifyDataSetChanged();
                            return;
                        }
                       orderInfo.setStatus(Constants.ORDER_CLOSE);
                        break;

                    case Constants.ORDER_OPERATE_CONFIRM:
                        if(curStatus!= Constants.ORDER_REQUEST_ALL){
                            getContent().remove(orderInfo);
                            notifyDataSetChanged();
                            return;
                        }
                        orderInfo.setStatus(Constants.ORDER_FINISH);
                        break;

                    case Constants.ORDER_OPERATE_DELETE:
                        getContent().remove(orderInfo);
                        break;

                    // 退款，取消退款
                    case Constants.ORDER_OPERATE_REFUND_APPLY:
                    case Constants.ORDER_OPERATE_REFUND_CANCEL:
                        final MyOrderInfoRequest orderInfoRequest=new MyOrderInfoRequest(orderNo);
                        orderInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                            @Override
                            public void onOK() {
                                if(orderInfoRequest.getOrderInfo()!=null){
                                    orderInfo.setStatus(orderInfoRequest.getOrderInfo().getStatus());
                                    orderInfo.setBuyItems(orderInfoRequest.getOrderInfo().getBuyItems());
                                    notifyDataSetChanged();
                                }

                            }

                            @Override
                            public void onFail(int code) {

                            }

                        });
                        orderInfoRequest.start(activity);
                        break;

                    default:
                        break;
                }

            }
        }

        notifyDataSetChanged();
    }



    /**
     * 根据statucode 设置button样式 
     *
     * **/
    private void setOrderbtns(int status,ViewHolder holder){
        String statusName=null;
        switch (status){
            case Constants.ORDER_WAIT_ROR_PAY:
                holder.btnPayOrder.setVisibility(View.VISIBLE);
                holder.btnCancleOrder.setVisibility(View.VISIBLE);
                holder.btnPayOrder.setText("付款");
                holder.btnCancleOrder.setText("取消订单");
                break;

            case Constants.ORDER_SENDING:
            case Constants.ORDER_WAIT_ROR_TAKE:
            case Constants.ORDER_WAIT_CONFIRM:
                if(status== Constants.ORDER_WAIT_CONFIRM&&!MyOrderAdapter.isLock(holder.orderInfo.getBuyItems())){
                    holder.btnPayOrder.setVisibility(View.VISIBLE);
                    holder.btnPayOrder.setText("确认收货");
                }else{
                    holder.btnPayOrder.setVisibility(View.GONE);
                }

                holder.btnCancleOrder.setVisibility(View.GONE);
                break;

            case Constants.ORDER_FINISH:
            case Constants.ORDER_CLOSE:
                holder.btnPayOrder.setVisibility(View.GONE);
                holder.btnCancleOrder.setVisibility(View.VISIBLE);
                holder.btnCancleOrder.setText("删除订单");
                break;

            default:
                break;

        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_myorder;
    }


    /**
     * 订单操作回调（付款，取消订单，删除订单，退款，确认收货。。。。）
     */
    @Override
    public void onCall(Object object) {
        Map<String,Object> result= (Map<String, Object>) object;
        Integer operaType= (Integer) result.get(Constants.ORDER_OPERATE_TYPE);
        String orderNo= (String) result.get(Constants.ORDER_NO);
        //Log.d("oncall"," operaType:"+operaType  +"  orNo:"+orderNo);
        updateOrderList(orderNo,operaType);
    }


    class ViewHolder{
        ImageView imageSendType;
        TextView txtOrderNumber;
        TextView txtOrderStatus;
        MListView mList;
        TextView txtOrderPrice;
        TextView btnCancleOrder;
        TextView btnPayOrder;
        View container;
        OrderInfo orderInfo;
    }


    

	@Override
	public void onClick(View v) {
	
		final ViewHolder holder=(ViewHolder) v.getTag();		
	     switch (v.getId()) {
	        	
	        	case R.id.view_container:
                    /**增加付款成功回调 ,退款申请成功回调 ,取消退款成功回调**/
	        		MyOrderInfoAct.start(holder.orderInfo.getOrderNo(), activity, this);
	        		break;
	        	
	            case R.id.btn_pay_order:
                    /**付款或者确认收货**/
                    if(holder.orderInfo.getStatus()== Constants.ORDER_WAIT_ROR_PAY){
                        PayAct.start(activity, holder.orderInfo.getOrderNo(),this); //跳转付款
                    }else{
                        if(confirmReceiveDialog==null)
                            confirmReceiveDialog= new ConfirmReceiveDialog();

                        confirmReceiveDialog.show(activity,holder.orderInfo, this);
                    }
	                break;

	            case R.id.btn_cancle_order:
                    /**付取消或者删除订单**/
	                if (orderOperateDialog == null) {
	                    orderOperateDialog = new OrderOperateDialog();
	                }

	                final int operateType;
	                if (holder.orderInfo.getStatus() == Constants.ORDER_WAIT_ROR_PAY) {
	                    operateType = Constants.ORDER_OPERATE_CANCLE;
	                } else {
	                    operateType = Constants.ORDER_OPERATE_DELETE;
	                }

                    orderOperateDialog.show(operateType, holder.orderInfo,this);
	                break;

	            default:
	                break;
	        }		
		
	}


}
