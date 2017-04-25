/*
 * 文件名：ActivityProductAdapter.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： ActivityProductAdapter.java
 * 修改人：wuchenhui
 * 修改时间：2015-5-5
 * 修改内容：新增
 */
package com.zxly.o2o.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.TimeCutdownUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.TimeCutDownLayout;

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
 * @version    YIBA-O2O 2015-5-5
 * @since      YIBA-O2O
 */
public class ActivityProductAdapter extends ObjectAdapter implements OnClickListener {

	private int imgSize;
	private Activity activity;
	private boolean isInActivityPage;
	private boolean isHideLikeCount;

	public ActivityProductAdapter(Activity activity) {
		super(activity);
	    this.activity=activity;
	    imgSize=((DesityUtil.getScreenSizes(activity)[0]- DesityUtil.dp2px(activity,30))/2);
	}


	public void setIsInActivityPage(boolean isInActivityPage){
		this.isInActivityPage=isInActivityPage;
	}

	public void hideLikeCount(){
		this.isHideLikeCount = true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=inflateConvertView();
			holder=new ViewHolder();
			convertView.setTag(holder);
			holder.txtActivityType= (TextView) convertView.findViewById(R.id.txt_act_type);
			holder.txtLikeCount= (TextView) convertView.findViewById(R.id.txt_likeCount);
			holder.imgIcon=(NetworkImageView) convertView.findViewById(R.id.img_product_icon);
			RelativeLayout.LayoutParams  params= new RelativeLayout.LayoutParams(imgSize, imgSize);
			params.addRule(RelativeLayout.BELOW, R.id.bg_top);
			holder.imgIcon.setLayoutParams(params);
			holder.txtName=(TextView) convertView.findViewById(R.id.txt_product_name);
			holder.txtPrice=(TextView) convertView.findViewById(R.id.txt_product_price);
			holder.txtOldPrice=(TextView) convertView.findViewById(R.id.txt_product_old_price);
			holder.layoutTime= (LinearLayout) convertView.findViewById(R.id.layout_time);
			holder.viewTime= (TimeCutDownLayout) convertView.findViewById(R.id.view_time);
			holder.txtStutus= (TextView) convertView.findViewById(R.id.txt_status);
			holder.botLine=convertView.findViewById(R.id.bg_bot);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		if(isLastRow(position)){
			holder.botLine.setVisibility(View.VISIBLE);
		}else {
			holder.botLine.setVisibility(View.GONE);
		}

		fillData(holder,(NewProduct)getItem(position));
	    convertView.setOnClickListener(this);
		return convertView;
	}
	
	public void fillData(final ViewHolder holder, final NewProduct product) {
		if (product != null && holder != null) {				
			holder.product=product;
			if(isInActivityPage)
			product.setId(product.getProductId());//暂时不用活动Id 吧活动商品当非活动商品处理
            holder.imgIcon.setDefaultImageResId(R.drawable.icon_default);
            holder.imgIcon.setImageUrl(product.getHeadUrl(), AppController.imageLoader);
			ViewUtils.setText(holder.txtName, product.getName());
			if(isHideLikeCount)
				ViewUtils.setGone(holder.txtLikeCount);
			ViewUtils.setText(holder.txtLikeCount, product.getEnjoyAmount());
			ViewUtils.setTextPrice(holder.txtPrice, product.getPrice() - product.getPreference());

			if (product.getTypeCode() == Constants.ACTICITY_LIMITTIME) {
				ViewUtils.strikeThruText(holder.txtOldPrice,product.getPrice());
				ViewUtils.setVisible(holder.layoutTime);
				ViewUtils.setVisible(holder.txtActivityType);
				holder.txtActivityType.setText("抢购");

				if((product.getStartTime()-product.getCurrentTime())>0){
					holder.txtStutus.setText("距开始  ");
					holder.viewTime.setResideTime(product.getStartTime()- Config.serverTime());
				}else{
					holder.txtStutus.setText("距结束  ");
					holder.viewTime.setResideTime(product.getEndTime() - Config.serverTime());
				}

				holder.viewTime.setOnTimeFinishCallBack(new CallBack() {
					@Override
					public void onCall() {
						if(product.getStatus()== Constants.ACTICITY_STOP){
							product.setStatus(Constants.ACTICITY_SART);
							holder.txtStutus.setText("距结束  ");
							holder.viewTime.setResideTime(product.getEndTime() - TimeCutdownUtil.getServerTime());
						}else{
							holder.txtStutus.setText("已结束");
							holder.viewTime.setVisibility(View.GONE);
						}
					}
				});

			} else if(product.getTypeCode() == Constants.ACTICITY_CLEARANCE){
				ViewUtils.strikeThruText(holder.txtOldPrice,product.getPrice());
				ViewUtils.setGone(holder.layoutTime);
				ViewUtils.setVisible(holder.txtActivityType);
				holder.txtActivityType.setText("优惠");
			}else{
				ViewUtils.setText(holder.txtOldPrice,"");
				ViewUtils.setGone(holder.layoutTime);
				ViewUtils.setGone(holder.txtActivityType);
			}
		}

	}
	
		class ViewHolder{
			TextView txtActivityType;
			TextView txtLikeCount;
			NetworkImageView imgIcon;
			LinearLayout layoutTime;
			TimeCutDownLayout viewTime;
			TextView txtStutus;
			TextView txtName;
			TextView txtPrice;
			TextView txtOldPrice;
			NewProduct product;
			View botLine;
		}

	@Override
	public int getLayoutId() {
		return R.layout.item_activity_product;
	}

	@Override
	public void onClick(View v) {
		ViewHolder holder=(ViewHolder) v.getTag();
		NewProduct product=holder.product;
		ProductInfoAct.start(activity, product, new ParameCallBack() {
			@Override
			public void onCall(Object object) {
				notifyDataSetChanged();
			}
		});
	}


	private boolean isLastRow(int pos) {
		if (getCount() % 2 == 0) {
			if (pos == (getCount() - 1) || pos == (getCount() - 2))
				return true;
		} else {
			if (pos == (getCount() - 1)) {
				return true;
			}
		}

		return false;
	}

}
