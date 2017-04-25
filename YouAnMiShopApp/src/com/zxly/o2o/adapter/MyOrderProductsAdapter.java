/*
 * 文件名：MyOrderProductsAdapter.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： MyOrderProductsAdapter.java
 * 修改人：wuchenhui
 * 修改时间：2015-6-8
 * 修改内容：新增
 */
package com.zxly.o2o.adapter;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.BuyItem;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.OrderInfo;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MListView;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;


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

	 private final int TYPE_1 = 0;  
	 private final int TYPE_2 = 1;  
	 private Activity activity;
	 private OrderInfo orderInfo;
	public MyOrderProductsAdapter(Activity activity) {
		super(activity);
		this.activity=activity;
	}

//
//	@Override
//	public int getCount() {
//		return 3;
//	}

	public void setOrderInfo(OrderInfo orderInfo){
		this.orderInfo=orderInfo;
	}
	
	// 每个convert view都会调用此方法，获得当前所需要的view样式  
    @Override  
    public int getItemViewType(int position) {  
		Object data = getItem(position);		
		if(data instanceof BuyItem){
			BuyItem item=(BuyItem) data;
			if (item.getType()!=Constants.PRODUCT_TYPE_PACKAGE) {
				return TYPE_1;
			}else{
				return TYPE_2;
			}
		}else{
			return TYPE_1;
		}
    }  

    @Override
    public int getViewTypeCount() {
        return 2;
    }


	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		    ViewHolder1 holder1 = null;  
	        ViewHolder2 holder2 = null; 
	        ViewHolder holder=null;
	        int type = getItemViewType(position);
		Log.d("listType","-->"+type  +"   getcount"+getCount());
		if (convertView == null) {

                if(type==TYPE_1){
                    //单个商品
                    convertView=LayoutInflater.from(activity).inflate(R.layout.item_myorder_product,null);
                    holder1=new ViewHolder1();

                    holder1.productName= (TextView) convertView.findViewById(R.id.txt_productName);
                    holder1.productIcon= (NetworkImageView) convertView.findViewById(R.id.img_productIcon);
                    holder1.productCount= (TextView) convertView.findViewById(R.id.txt_productCount);
                    holder1.productPrice= (TextView) convertView.findViewById(R.id.txt_price);
                    holder1.productOldPrice=(TextView) convertView.findViewById(R.id.txt_oldPrice);
                  //  holder1.botLine=convertView.findViewById(R.id.line_bot);
                    convertView.setTag(holder1);
                }else{
                    //套餐商品
                    convertView=LayoutInflater.from(activity).inflate(R.layout.item_myorder_combo_product,null);
                    holder2=new ViewHolder2();

                    holder2.mListView=(MListView) convertView.findViewById(R.id.listView);
                    MyOrderProductsAdapter adapter=new MyOrderProductsAdapter(activity);
                    holder2.mListView.setGetTouchEvent(false);
                    holder2.mListView.setAdapter(adapter);
					Log.d("listType", "in coccc   : " + ((BuyItem) getItem(position)).getProducts().size());
					((ObjectAdapter) holder2.mListView.getAdapter()).addItem(((BuyItem)getItem(position)).getProducts(), true);
            //        holder2.botLine=convertView.findViewById(R.id.line_bot);
                    convertView.setTag(holder2);
                }
	  
	        } else {

                if(type==TYPE_1){
                    holder1 = (ViewHolder1) convertView.getTag();
                }else{
                    holder2 = (ViewHolder2) convertView.getTag();
                }

	        }  
	        
	        if(type==TYPE_1){
	        	holder=holder1;
	        }else{
	        	holder=holder2;
	        }

	    //    setBotLine(holder, position);
	       fillData(convertView, holder, getItem(position));
		
		return convertView;
	}

	
	private void setBotLine(ViewHolder holder,int position){
    //    holder.botLine.setVisibility(View.VISIBLE);
//        if(position==(getCount()-1))
//            holder.botLine.setVisibility(View.GONE);
	}

    private void fillData(View convertView,ViewHolder holder,Object data){

        if(holder instanceof ViewHolder1){
        	//BuyItem item=(BuyItem) data;
        	ViewHolder1 holder1=((ViewHolder1)holder);
        	//holder1.item=item;
        	NewProduct product=null;
        	if(data instanceof BuyItem){
        		product=((BuyItem)data).getProducts().get(0);
        		ViewUtils.setText(holder1.productCount, "x"+((BuyItem)data).getPcs());
        	}else{
        		product=(NewProduct) data;
        		ViewUtils.setText(holder1.productCount, "x"+product.getPcs());
        	}
        	
//            if(!DataUtil.stringIsNull(product.get)){
//                holder1.comboInfo.setVisibility(View.VISIBLE);
//                ViewUtils.setText(holder1.comboInfo,product.getComboInfo());
//            }else{
//                holder1.comboInfo.setVisibility(View.GONE);
//            }
            ViewUtils.setText(holder1.productName,product.getName());
			ViewUtils.setTextPrice(holder1.productPrice, product.getPrice());
             
            holder1.productIcon.setDefaultImageResId(R.drawable.product_def);
            holder1.productIcon.setImageUrl(product.getHeadUrl(),AppController.imageLoader);
        }else{
        	
        	ViewHolder2 holder2=((ViewHolder2)holder);
        	//holder2.item=item;

        	((MyOrderProductsAdapter)holder2.mListView.getAdapter()).setOrderInfo(orderInfo);
        //	((MyOrderProductsAdapter)holder2.mListView.getAdapter()).setIsComboList(true);
        	((ObjectAdapter)holder2.mListView.getAdapter()).clear();
        	for (NewProduct product : ((BuyItem)data).getProducts()) {
				product.setPcs(((BuyItem)data).getPcs());
			}

		//	Log.d("listType","in coccc   : "+((BuyItem) data).getProducts().size() );
        	/**要改为produts**/
					((ObjectAdapter) holder2.mListView.getAdapter()).addItem(((BuyItem) data).getProducts(), true);
        }
    	
	
    }

	
		
	    class ViewHolder1 extends ViewHolder {  
           TextView productName;
           NetworkImageView productIcon;
           TextView productCount;
           TextView productPrice;
           TextView productOldPrice;
           TextView txtTakeNo;
	    }  
	  
	    class ViewHolder2 extends ViewHolder {  
            MListView mListView;
	    }
	    
	    class ViewHolder{


            BuyItem item;
	    }

		@Override
		public int getLayoutId() {
			return 0;
		}

		@Override
		public void onClick(View v) {

        			
		}  

}
