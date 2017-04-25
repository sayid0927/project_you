/*
 * 文件名：MyOrderProductsAdapter.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： MyOrderProductsAdapter.java
 * 修改人：wuchenhui
 * 修改时间：2015-6-8
 * 修改内容：新增
 */
package com.zxly.o2o.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.RefundmentApplyActivity;
import com.zxly.o2o.activity.RefundmentDetailActivity;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.BuyItem;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MListView;

/**
 * TODO 添加类的一句话简单描述。
 * <p>
 * TODO 详细描述
 * <p>
 * TODO 示例代码
 * <pre>
 * </pre>
 * 
 * @author     wuchenhui
 * @version    YIBA-O2O 2015-6-8
 * @since      YIBA-O2O
 */
public class MyOrderProductsAdapter extends ObjectAdapter implements OnClickListener {

	 private final int TYPE_SINGLE_PRODUCT = 0;
	 private final int TYPE_COMBO_PRODUCT = 1;
	 private final int TYPE_COUNT= 2;
     private ParameCallBack callBack;
	 private Activity activity;
	 private OrderInfo orderInfo;
	 private boolean isInOrderList; //如果是在订单列表就不显示退款按钮

	public MyOrderProductsAdapter(Activity activity) {
		super(activity);
		this.activity=activity;
	}

	public void setCallBack(ParameCallBack callBack){
       this.callBack=callBack;
	}

	public void setOrderInfo(OrderInfo orderInfo){
		this.orderInfo=orderInfo;
	}

	public void setInOrderList(boolean isInOrderList){
		this.isInOrderList=isInOrderList;
	}

	// 每个convert view都会调用此方法，获得当前所需要的view样式  
    @Override  
    public int getItemViewType(int position) {  
		Object data = getItem(position);		
		if(data instanceof BuyItem){
			BuyItem item=(BuyItem) data;
			if (item.getType()!= Constants.PRODUCT_TYPE_PACKAGE) {
				return TYPE_SINGLE_PRODUCT;
			}else{
				return TYPE_COMBO_PRODUCT;
			}
		}else{
			return TYPE_SINGLE_PRODUCT;
		}
    }  
  
    @Override  
    public int getViewTypeCount() {  
        return TYPE_COUNT;
    }  
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		    SingleProductHolder singleProductHolder = null;
	        ComboProductHolder comboProductHolder = null;
	        BaseHolder holder=null;

	        int type = getItemViewType(position);  
	        
	        if (convertView == null) {

                if(type== TYPE_SINGLE_PRODUCT){
                    //单个商品
                    convertView=LayoutInflater.from(activity).inflate(R.layout.item_myorder_product,null);
                    singleProductHolder=new SingleProductHolder();

                    singleProductHolder.productName= (TextView) convertView.findViewById(R.id.text_productName);
                    singleProductHolder.productIcon= (NetworkImageView) convertView.findViewById(R.id.img_product_icon);
                    singleProductHolder.productCount= (TextView) convertView.findViewById(R.id.text_product_count);
                    singleProductHolder.productPrice= (TextView) convertView.findViewById(R.id.text_price);
                    singleProductHolder.comboInfo= (TextView) convertView.findViewById(R.id.text_comboInfo);

					singleProductHolder.layoutRefund=convertView.findViewById(R.id.layout_refund);
                    singleProductHolder.txtRefundStatus = (TextView) convertView.findViewById(R.id.text_refund_reside_time);
                    singleProductHolder.btnRefundStatus= (TextView) convertView.findViewById(R.id.btn_refund_status);
                    singleProductHolder.btnRefundStatus.setOnClickListener(this);
                    singleProductHolder.btnRefundStatus.setTag(singleProductHolder);
                    singleProductHolder.botLine=convertView.findViewById(R.id.line_bot);
                    convertView.setTag(singleProductHolder);
                }else{
                    //套餐商品
                    convertView=LayoutInflater.from(activity).inflate(R.layout.item_myorder_combo_product,null);
                    comboProductHolder=new ComboProductHolder();

					//初始化套餐列表
                    comboProductHolder.mListView=(MListView) convertView.findViewById(R.id.list_order_combo_product);
                    MyOrderProductsAdapter adapter=new MyOrderProductsAdapter(activity);
                    comboProductHolder.mListView.setGetTouchEvent(false);
                    comboProductHolder.mListView.setAdapter(adapter);

					comboProductHolder.layoutRefund=convertView.findViewById(R.id.layout_refund);
                    comboProductHolder.txtRefundStatus = (TextView) convertView.findViewById(R.id.text_refund_reside_time);
                    comboProductHolder.btnRefundStatus= (TextView) convertView.findViewById(R.id.btn_refund_status);
                    comboProductHolder.btnRefundStatus.setOnClickListener(this);
                    comboProductHolder.btnRefundStatus.setTag(comboProductHolder);
                    comboProductHolder.botLine=convertView.findViewById(R.id.line_bot);
                    comboProductHolder.lineRefund=convertView.findViewById(R.id.lineRefund);
                    convertView.setTag(comboProductHolder);
                }
	  
	        } else {
                if(type== TYPE_SINGLE_PRODUCT){
                    singleProductHolder = (SingleProductHolder) convertView.getTag();
                }else{
                    comboProductHolder = (ComboProductHolder) convertView.getTag();
                }
	        }


		if(type== TYPE_SINGLE_PRODUCT){
			holder=singleProductHolder;
		}else{
			holder=comboProductHolder;
		}

	    setBotLine(holder, position);

	    fillData(holder, getItem(position));
		
		return convertView;
	}


	private void setBotLine(BaseHolder holder,int position){
        holder.botLine.setVisibility(View.VISIBLE);
        if(position==(getCount()-1))
            holder.botLine.setVisibility(View.GONE);
	}

    private void fillData(BaseHolder holder,Object data){

		//设置售后状态
        setAfterSalesServiceStatus(holder, data);

        if(holder instanceof SingleProductHolder){
        	SingleProductHolder singleProductHolder=((SingleProductHolder)holder);
        	NewProduct product=null;
        	if(data instanceof BuyItem){
				//订单里面的item
        		product=((BuyItem)data).getProducts().get(0);
        		ViewUtils.setText(singleProductHolder.productCount,"x"+((BuyItem)data).getPcs());
        	}else{
				//套餐里面的item
        		product=(NewProduct) data;
        	    ViewUtils.setText(singleProductHolder.productCount,"x"+product.getPcs());
        	}

            ViewUtils.setText(singleProductHolder.productName, product.getName());
            ViewUtils.setTextPrice(singleProductHolder.productPrice, product.getPrice());

			if(!StringUtil.isNull(product.getRemark())){
				String remark=product.getRemark().replace("\n",",");
				ViewUtils.setText(((SingleProductHolder) holder).comboInfo,remark);
			}else{

			}

            singleProductHolder.productIcon.setDefaultImageResId(R.drawable.icon_default);
            singleProductHolder.productIcon.setImageUrl(product.getHeadUrl(), AppController.imageLoader);

        }else{
        	ComboProductHolder comboProductHolder=((ComboProductHolder)holder);
        	comboProductHolder.btnRefundStatus.setOnClickListener(this);

        	((MyOrderProductsAdapter)comboProductHolder.mListView.getAdapter()).setOrderInfo(orderInfo);
        	((ObjectAdapter)comboProductHolder.mListView.getAdapter()).clear();
        	for (NewProduct product : ((BuyItem)data).getProducts()) {
				product.setPcs(((BuyItem)data).getPcs());
			}

        	((ObjectAdapter)comboProductHolder.mListView.getAdapter()).addItem(((BuyItem) data).getProducts(), true);
        }
    	
	
    }

	
    /**
     * 设置售后状态(退款，查看退款)
	 *
     */
    private void setAfterSalesServiceStatus(BaseHolder holder,Object data){

		//属于购物车buyitem才显示售后,套餐里面item是product
    	if(!(data instanceof BuyItem)){
			ViewUtils.setGone(holder.layoutRefund);
    		return;
    	}

		BuyItem item= (BuyItem) data;
  	    holder.btnRefundStatus.setTag(data);
    	int refundStatus=((BuyItem)data).getStatus();

    	switch (orderInfo.getStatus()) {
    		//售后状态从付完款才开始
			case Constants.ORDER_WAIT_ROR_PAY :
				ViewUtils.setGone(holder.layoutRefund);
				break;
				
			//交易进行中（售后7天还没过的都算）
			case Constants.ORDER_SENDING:
			case Constants.ORDER_WAIT_ROR_TAKE :
			case Constants.ORDER_WAIT_CONFIRM:
				 if(refundStatus== Constants.PRODUCT_STATE_NOMAL){
					 //如果在订单列表里面就不让申请退款
					 if(isInOrderList){
						 holder.layoutRefund.setVisibility(View.GONE);
						 return;
					 }
					 ViewUtils.setVisible(holder.btnRefundStatus);
					 ViewUtils.setText(holder.txtRefundStatus, "");
					 holder.btnRefundStatus.setText("申请退款");
				 }else if(refundStatus== Constants.PRODUCT_STATE_FERUNDING){
					 ViewUtils.setVisible(holder.layoutRefund);
					 ViewUtils.setText(holder.txtRefundStatus, "退款中，待店家确认：" + TimeUtil.getResideTime(item.getResidueTime()));
					 holder.btnRefundStatus.setText("查看退款");
				 }else if(refundStatus== Constants.PRODUCT_STATE_FERUND_SUCCESS){
					 ViewUtils.setVisible(holder.layoutRefund);
					 ViewUtils.setText(holder.txtRefundStatus, "商家已退款");
					 holder.btnRefundStatus.setText("查看退款");
				 }else{
					 //暂时不做考虑
				 }

				break;
				
			//交易结束	
			case Constants.ORDER_CLOSE:
			case Constants.ORDER_FINISH:
				if(refundStatus== Constants.PRODUCT_STATE_FERUND_SUCCESS){
					ViewUtils.setVisible(holder.layoutRefund);
					ViewUtils.setText(holder.txtRefundStatus, "商家已退款");
					holder.btnRefundStatus.setText("查看退款");
				}else{
					ViewUtils.setGone(holder.layoutRefund);
				}

				break;
				
			default :
				ViewUtils.setGone(holder.txtRefundStatus);
				ViewUtils.setGone(holder.btnRefundStatus);
				break;
		}
    
    }


		
	    class SingleProductHolder extends BaseHolder {
           TextView productName;
           NetworkImageView productIcon;
           TextView productCount;
           TextView productPrice;
           TextView comboInfo;
	    }  
	  
	    class ComboProductHolder extends BaseHolder {
            MListView mListView;
            View lineRefund;
	    }
	    
	    class BaseHolder {
			/** 退款状态说明**/
            TextView txtRefundStatus;
			/** 退款状按钮**/
            TextView btnRefundStatus;

            View botLine;
			View layoutRefund;
            BuyItem item;
	    }

		@Override
		public int getLayoutId() {
			return 0;
		}

		@Override
		public void onClick(View v) {

	      //带上
			switch (v.getId()) {
				case R.id.btn_refund_status :
					BuyItem item=(BuyItem) v.getTag();
					if(item.getStatus()== Constants.PRODUCT_STATE_NOMAL){
						//传进去的是buyItem的总价格
						RefundmentApplyActivity.start(activity, item.getItemId(),
								orderInfo.getStatus(),item.getType(), 
								orderInfo.getOrderNo(), item.getPrice(), item.getPcs(),callBack);
					}else{
						RefundmentDetailActivity.start(activity,item.getRefundId(),orderInfo.getOrderNo(),callBack);
					}

					break;
					
				default :
					break;
			}
        			
		}  

}
