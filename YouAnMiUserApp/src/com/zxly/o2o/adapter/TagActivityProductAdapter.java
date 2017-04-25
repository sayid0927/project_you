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
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.ActivityProductAct;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.UMengAgent;
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
public class TagActivityProductAdapter extends ObjectAdapter {

	private Activity activity;
	public TagActivityProductAdapter(Activity activity) {
		super(activity);
	    this.activity=activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		NewProduct[] products=(NewProduct[]) getItem(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=getConvertView(holder);
			convertView.setTag(holder);			
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.itemName.setOnClickListener(new ItemClickListener(products[0]));
		
		if(products[0].getTypeCode()== Constants.ACTICITY_LIMITTIME){
			holder.itemName.setText("限时抢");
			holder.itemName.setBackgroundColor(0xffff5151);
		}else if(products[0].getTypeCode()== Constants.ACTICITY_CLEARANCE){
			holder.itemName.setText("清仓");
			holder.itemName.setBackgroundColor(0xffffb72b);			
		}else{
			holder.itemName.setText("新品");
			holder.itemName.setBackgroundColor(0xff4e9ae0);					
		}
		
		fillData(holder.item1,products[0]);
		if(products[1]==null){
			holder.item2.itemlayout.setVisibility(View.GONE);
			holder.noContentItem.setVisibility(View.VISIBLE);
		}else{
			holder.noContentItem.setVisibility(View.GONE);
			holder.item2.itemlayout.setVisibility(View.VISIBLE);
			fillData(holder.item2,products[1]);		
		}

		return convertView;
	}
	
	public View getConvertView(ViewHolder holder){
		LinearLayout itemLayout=new LinearLayout(activity);
		itemLayout.setPadding(DesityUtil.dp2px(activity, 8),0, DesityUtil.dp2px(activity, 8), DesityUtil.dp2px(activity, 10));
		itemLayout.setOrientation(LinearLayout.HORIZONTAL);
		itemLayout.setBackgroundColor(Color.WHITE);
		AbsListView.LayoutParams itemParamas=new AbsListView.LayoutParams(-1, -2);
	//	itemParamas.setMargins(DesityUtil.dp2px(activity, 8),0, DesityUtil.dp2px(activity, 8), DesityUtil.dp2px(activity,10));
		itemLayout.setLayoutParams(itemParamas);
		
		holder.itemName=new TextView(activity);
		holder.itemName.setGravity(Gravity.CENTER);
		holder.itemName.setTextColor(Color.WHITE);
		holder.itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		holder.itemName.setPadding(DesityUtil.dp2px(activity,10),0, DesityUtil.dp2px(activity,10),0);
		LinearLayout.LayoutParams btnParamas=new LinearLayout.LayoutParams(DesityUtil.dp2px(activity, 44), DesityUtil.dp2px(activity,159));
		holder.itemName.setLayoutParams(btnParamas);

		LinearLayout contentLayout=new LinearLayout(activity);
		contentLayout.setOrientation(LinearLayout.HORIZONTAL);
		contentLayout.setBackgroundResource(R.drawable.nice_choice_bg);
		LinearLayout.LayoutParams contentParamas=new LinearLayout.LayoutParams(-1, -1);
	//	contentParamas.setMargins(DesityUtil.dp2px(activity, 8),0, DesityUtil.dp2px(activity, 8), DesityUtil.dp2px(activity,10));
		contentLayout.setLayoutParams(contentParamas);
		
		holder.item1=new Item();
		holder.item2=new Item();
		initItem(holder.item1);
		initItem(holder.item2);
		initNoContentItem(holder);
		contentLayout.addView(holder.item1.itemlayout);
	    contentLayout.addView(getMiddleLine());
		contentLayout.addView(holder.item2.itemlayout);	
		holder.noContentItem.setVisibility(View.GONE);
		contentLayout.addView(holder.noContentItem);
		
		itemLayout.addView(holder.itemName);
		itemLayout.addView(contentLayout);
		
		return itemLayout;
	}
	

	
	public void fillData(final Item item, NewProduct product) {
		
		if (product != null && item != null) {	
			//item.setId(product.getProductId());//暂时不用活动Id 吧活动商品当非活动商品处理		
			item.itemlayout.setOnClickListener(new OnProductItemClikIistener(product));

			item.icon.setDefaultImageResId(R.drawable.icon_default);
			item.icon.setImageUrl(product.getHeadUrl(), AppController.imageLoader);

			ViewUtils.setText(item.name,product.getName());
			ViewUtils.setTextPrice(item.price, product.getPrice()-product.getPreference());
			ViewUtils.strikeThruText(item.oldPrice,product.getPrice());

			item.txtStatus.setText("距结束 ");
			item.timeLayout.setResideTime(product.getResidueTime());
			if (product.getTypeCode() == Constants.ACTICITY_LIMITTIME) {
				item.timeLayoutBg.setVisibility(View.VISIBLE);

				item.timeLayout.setOnTimeFinishCallBack(new CallBack() {
					@Override
					public void onCall() {
						item.txtStatus.setText("已结束");
						item.timeLayout.setVisibility(View.GONE);
					}
				});

			} else {
				item.timeLayoutBg.setVisibility(View.GONE);
			}
		}

	}
	
	
	public void initItem(Item item) {
		item.itemlayout=new RelativeLayout(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1);
		params.gravity=Gravity.CENTER;
		params.weight=1;
		item.itemlayout.setLayoutParams(params);
		int height=LinearLayout.LayoutParams.MATCH_PARENT;
		
		item.icon = new NetworkImageView(activity);
		item.icon.setId(R.id.image_product);
		RelativeLayout.LayoutParams imageParams=new RelativeLayout.LayoutParams(-2, DesityUtil.dp2px(activity, 106));
		imageParams.setMargins(DesityUtil.dp2px(activity, 5), DesityUtil.dp2px(activity, 5), DesityUtil.dp2px(activity, 5), 0);
		imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		item.icon.setLayoutParams(imageParams);
		
		item.name  =new TextView(activity);
		item.name.setId(R.id.text_productName);
		item.name.setTextAppearance(activity, R.style.text_13_black);
		item.name.setSingleLine(true);
		item.name.setEllipsize(TruncateAt.END);
		RelativeLayout.LayoutParams nameParams=new RelativeLayout.LayoutParams(-2,-2);
		nameParams.setMargins(DesityUtil.dp2px(activity, 6), DesityUtil.dp2px(activity, 4), DesityUtil.dp2px(activity, 6), 0);
		nameParams.addRule(RelativeLayout.BELOW, R.id.image_product);
		item.name.setLayoutParams(nameParams);
	
		item.price = new TextView(activity);
		item.price.setTextAppearance(activity, R.style.text_14_red_dd2727);
		item.price.setId(R.id.text_price);
		RelativeLayout.LayoutParams priceParams=new RelativeLayout.LayoutParams(-2,-2);
		priceParams.setMargins(DesityUtil.dp2px(activity, 6), 0, DesityUtil.dp2px(activity, 0), 0);
		priceParams.addRule(RelativeLayout.BELOW, R.id.text_productName);
		item.price.setLayoutParams(priceParams);
		
		item.oldPrice = new TextView(activity);
		item.oldPrice.setId(R.id.text_old_price);
		item.oldPrice.setTextAppearance(activity, R.style.text_11_light_grey);
	
		RelativeLayout.LayoutParams oldPriceParams=new RelativeLayout.LayoutParams(-2,-2);
		oldPriceParams.setMargins(0, 0, DesityUtil.dp2px(activity, 6), 0);
		oldPriceParams.addRule(RelativeLayout.BELOW, R.id.text_productName);
		oldPriceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		item.oldPrice.setLayoutParams(oldPriceParams);
		
		
		item.timeLayoutBg=new LinearLayout(activity);
		item.timeLayoutBg.setGravity(Gravity.CENTER);
		item.timeLayoutBg.setBackgroundResource(R.drawable.daojishi);
		item.timeLayoutBg.setVisibility(View.GONE);
		RelativeLayout.LayoutParams timeBgParams=new RelativeLayout.LayoutParams(-1, DesityUtil.dp2px(activity,20));
		timeBgParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.image_product);
		item.timeLayoutBg.setLayoutParams(timeBgParams);
		
		
		item.timeLayout=new TimeCutDownLayout(activity);
		item.timeLayout.setTextStyle();

		item.txtStatus=new TextView(activity);
		LinearLayout.LayoutParams txtStatusParams=new LinearLayout.LayoutParams(-2,-2);
		item.txtStatus.setLayoutParams(txtStatusParams);
		item.txtStatus.setTextAppearance(activity, R.style.text_12_white);


		LinearLayout.LayoutParams timeLayoutParams=new LinearLayout.LayoutParams(-2,-2);
		item.timeLayout.setLayoutParams(timeLayoutParams);
		item.timeLayoutBg.setOrientation(LinearLayout.HORIZONTAL);
		item.timeLayoutBg.setGravity(Gravity.CENTER);
		item.timeLayoutBg.addView(item.txtStatus);
		item.timeLayoutBg.addView(item.timeLayout);
		
		item.itemlayout.addView(item.icon);
		item.itemlayout.addView(item.name);
		item.itemlayout.addView(item.price);
		item.itemlayout.addView(item.oldPrice);
		item.itemlayout.addView(item.timeLayoutBg);	
	}	
	
	public void initNoContentItem(ViewHolder holder){
		holder.noContentItem=new LinearLayout(activity);
		holder.noContentItem.setGravity(Gravity.CENTER);
	//	item.setBackgroundColor(Color.RED);
		LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(-1, -1);
		p1.weight=1;
		holder.noContentItem.setLayoutParams(p1);
		ImageView bg=new ImageView(activity);
		bg.setBackgroundResource(R.drawable.zanshimeiyoushangpin);
		LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(-2, -2);
		p2.gravity=Gravity.CENTER;
		bg.setLayoutParams(p2);
		holder.noContentItem.addView(bg);
	}

	public View getMiddleLine() {
		View item = new View(activity);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DesityUtil.dp2px(activity, 0.7f), -2);
	//	params.setMargins(0, y, 0, y);
		item.setBackgroundColor(activity.getResources().getColor(R.color.grey));
		item.setLayoutParams(params);
		return item;
	}
	
	
	class ViewHolder {
		  TextView itemName;
		  Item item1;
		  Item item2;
		  LinearLayout noContentItem;
		}
	
		class Item{
			RelativeLayout itemlayout;
			ImageView typeIcon;
			NetworkImageView icon;
			TextView txtStatus;
			TimeCutDownLayout timeLayout;
			LinearLayout timeLayoutBg;
			TextView name;
			TextView price;
			TextView oldPrice;
		}
	
	
	

	@Override
	public int getLayoutId() {
		return 0;
	}
	
	
	class OnProductItemClikIistener implements OnClickListener {
		private NewProduct product;

		OnProductItemClikIistener(NewProduct product) {
			this.product = product;
		}

		@Override
		public void onClick(View arg0) {		
			ProductInfoAct.start(activity, product.clone());
		}

	}
	
	class ItemClickListener implements OnClickListener {
		private NewProduct product;

		ItemClickListener(NewProduct product) {
			this.product = product;
		}

		@Override
		public void onClick(View arg0) {		
			Intent it=null;
			switch (product.getTypeCode()) {
				case 1 :
					ActivityProductAct.startActivityProductAct(1,activity);
//					it = new Intent(activity, ActivityGoodsAct.class);
//					it.putExtra("currentIndex", AllGoodsFragment.LIMIT_TIME_GOODS);
//					ViewUtils.startActivity(it,activity);
					UMengAgent.onEvent(activity, UMengAgent.tag_store_limit_sale);
					break;
				case 2 :
					ActivityProductAct.startActivityProductAct(2,activity);
//					it = new Intent(activity, ActivityGoodsAct.class);
//					it.putExtra("currentIndex", AllGoodsFragment.CLEARANCE_GOODS);
//					ViewUtils.startActivity(it,activity);
					UMengAgent.onEvent(activity, UMengAgent.tag_store_clearance_sale);
					break;
				default :
	//				RadioGroup rgs=((HomeAct)activity).rgs;
	//				((RadioButton)rgs.findViewById(R.id.main_act_shangpin_btn)).setChecked(true);
//					it = new Intent(activity, AllProductAct.class);
//					ViewUtils.startActivity(it, activity);
					UMengAgent.onEvent(activity, UMengAgent.tag_store_new_product);
					break;
			}
		}

	}


}
