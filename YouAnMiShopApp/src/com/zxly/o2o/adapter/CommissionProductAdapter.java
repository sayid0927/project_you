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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.H5DetailAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.CommissionProduct;
import com.zxly.o2o.model.ShareInfo;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

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
public class CommissionProductAdapter extends ObjectAdapter implements OnClickListener {
    ShareDialog dialog;
	private int imgSize;
	public CommissionProductAdapter(Activity activity) {
		super(activity);
	    imgSize=((DesityUtil.getScreenSizes(activity)[0]-DesityUtil.dp2px(activity,30))/2);;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=inflateConvertView();
			convertView.setId(R.id.convertView);
			convertView.setOnClickListener(this);
			holder=new ViewHolder();
			convertView.setTag(holder);
			holder.imgIcon=(NetworkImageView) convertView.findViewById(R.id.img_head_icon);
			RelativeLayout.LayoutParams  params= new RelativeLayout.LayoutParams(imgSize, imgSize);
			params.addRule(RelativeLayout.BELOW, R.id.bg_top);
			holder.imgIcon.setLayoutParams(params);
			holder.txtName=(TextView) convertView.findViewById(R.id.txt_product_name);
			holder.txtPrice=(TextView) convertView.findViewById(R.id.txt_product_price);
			holder.txtReturnCommisson=(TextView) convertView.findViewById(R.id.txt_returnCommission);
			holder.btnPromotion= (TextView) convertView.findViewById(R.id.btn_promotion);
			holder.botBg=convertView.findViewById(R.id.bg_bot);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		if(position==(getCount()-1)){
			holder.botBg.setVisibility(View.VISIBLE);
		}else {
			holder.botBg.setVisibility(View.GONE);
		}

		fillData(holder,(CommissionProduct)getItem(position));

		return convertView;
	}
	
	public void fillData(final ViewHolder holder, final CommissionProduct product) {
		holder.product=product;
		holder.imgIcon.setDefaultImageResId(R.drawable.icon_default_330x330);
		holder.imgIcon.setImageUrl(product.getHeadUrl(), AppController.imageLoader);
		ViewUtils.setText(holder.txtName, product.getName());
		ViewUtils.setTextPrice(holder.txtPrice, product.getPrice());

		holder.txtReturnCommisson.setText("佣金: ￥" + product.getCommission());

		holder.btnPromotion.setOnClickListener(this);
		holder.btnPromotion.setTag(product);

		}


	
		class ViewHolder{
			NetworkImageView imgIcon;
			TextView txtName;
			TextView txtPrice;
			TextView txtReturnCommisson;
			TextView btnPromotion;
			CommissionProduct product;
			View botBg;
		}

	@Override
	public int getLayoutId() {
		return R.layout.item_commission_product;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()){
			case R.id.convertView:
				ViewHolder holder= (ViewHolder) v.getTag();
				//jin ru  h5  jie mian  dandu dai fen xiang  xinxi
				ShareInfo shareInfo=new ShareInfo();
				shareInfo.setTitle(holder.product.getName());
				shareInfo.setDesc("");
				shareInfo.setUrl(holder.product.getUrl()+"&promotionUserId=" + Account.user.getId());
                shareInfo.setIconUrl(holder.product.getHeadUrl());
				H5DetailAct.start(H5DetailAct.TYPE_DEFAULT,
						          AppController.getInstance().getTopAct(),
						          holder.product.getUrl()+"&isHide=true"+"&promotionUserId=" + Account.user.getId(),
						          "商品详情",shareInfo);

				UmengUtil.onEvent(context,new UmengUtil().PROMOTION_GOODS_CLICK,null);
				break;

			case R.id.btn_promotion:
				if(dialog==null)
					dialog=new ShareDialog();

				final CommissionProduct product= (CommissionProduct) v.getTag();
				dialog.show(product.getName(), product.getUrl() + "&promotionUserId=" + Account.user.getId(), product.getHeadUrl(), new ShareListener() {
					@Override
					public void onComplete(Object var1) {
						new PromoteCallbackConfirmRequest(product.getProductId(),1,product.getName()).start();
					}

					@Override
					public void onFail(int errorCode) {

					}
				});

				UmengUtil.onEvent(context,new UmengUtil().PROMOTION_SHARE_CLICK,null);
				break;

			default:
				break;

		}


	}

}
