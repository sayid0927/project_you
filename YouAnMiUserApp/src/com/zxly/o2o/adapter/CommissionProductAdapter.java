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
import com.zxly.o2o.activity.LoginAct;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.StringUtil;
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

	private int imgSize;
	private ShareDialog dialog;
    private Activity curAct;
	public CommissionProductAdapter(Activity activity) {
		super(activity);
		curAct=activity;
	    imgSize=((DesityUtil.getScreenSizes(activity)[0]- DesityUtil.dp2px(activity,30))/2);
		dialog=new ShareDialog();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=inflateConvertView();
			convertView.setId(R.id.convertView);
			holder=new ViewHolder();
			convertView.setTag(holder);
			holder.imgIcon=(NetworkImageView) convertView.findViewById(R.id.img_product_icon);
			RelativeLayout.LayoutParams  params= new RelativeLayout.LayoutParams(imgSize, imgSize);
			params.addRule(RelativeLayout.BELOW, R.id.bg_top);
			holder.imgIcon.setLayoutParams(params);
			holder.txtName=(TextView) convertView.findViewById(R.id.txt_product_name);
			holder.txtPrice=(TextView) convertView.findViewById(R.id.txt_product_price);
			holder.txtReturnCommisson=(TextView) convertView.findViewById(R.id.txt_returnCommission);
			holder.btnPromotion= (TextView) convertView.findViewById(R.id.btn_promotion);
			holder.btnPromotion.setOnClickListener(this);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		fillData(holder,(NewProduct)getItem(position));
	    convertView.setOnClickListener(this);
		return convertView;
	}
	
	public void fillData(final ViewHolder holder,NewProduct product) {
		holder.product=product;
		holder.imgIcon.setDefaultImageResId(R.drawable.icon_default_330x330);
		holder.imgIcon.setImageUrl(product.getHeadUrl(), AppController.imageLoader);
		ViewUtils.setText(holder.txtName, product.getName());
		ViewUtils.setTextPrice(holder.txtPrice, product.getPrice());
	//	holder.txtReturnCommisson.setText("佣金: ￥" + product.getCommission());
		holder.btnPromotion.setOnClickListener(this);
		holder.btnPromotion.setTag(product);



	}

	class ViewHolder{
		NetworkImageView imgIcon;
		TextView txtName;
		TextView txtPrice;
		TextView txtReturnCommisson;
		TextView btnPromotion;
		NewProduct product;
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
				ProductInfoAct.start(AppController.getInstance().getTopAct(),holder.product.getProductId());
				break;

			case R.id.btn_promotion:
				NewProduct product= (NewProduct) v.getTag();
				if(Account.user==null){
					LoginAct.start(curAct);
					return;
				}

				StringBuilder desc=new StringBuilder("售价：");
				desc.append(StringUtil.getRMBPrice(product.getPrice())).append("\n\n").append(Account.shopInfo.getName());

				dialog.show(product.getName(),desc.toString(), product.getUrl()+"&promotionUserId="+ Account.user.getId(), product.getHeadUrl(),null);
				break;

			default:
				break;

		}



	}

}
