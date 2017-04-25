package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.dialog.SelectChatDialog;
import com.zxly.o2o.fragment.FavourableComboFragment;
import com.zxly.o2o.model.BuyItem;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Pakage;
import com.zxly.o2o.model.Skus;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.AddPakageToCartRequest;
import com.zxly.o2o.util.DesityUtil;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;

public class ComboSelAct extends BaseViewPageAct implements View.OnClickListener,ParameCallBackById {
	private static List<Pakage> productComboList;
	private static Skus skus;
	private int index=0;
	private long productId;
	private View btnJoinCart,btnToBuy;
	private String[] tabName;
	private TextView txtPrice,txtPrePrice;
	private View btnChat;
	float totalPrice=0;//合计
	private List<NewProduct> selProductList;
	public static void start(List<Pakage> _productComboList,
			NewProduct product, Activity curAct) {
		productComboList = _productComboList;
		Intent intent = new Intent(curAct, ComboSelAct.class);
		intent.putExtra("productId", product.getId());
		skus = product.getSelSku();
		ViewUtils.startActivity(intent, curAct);
		
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				index=position;
				FavourableComboFragment fcf=(FavourableComboFragment) fragments.get(position);
				onCall(index, fcf.selProductList);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
	
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		productComboList = null;
		skus = null;
	}
	
	@Override
	protected int layoutId() {

		return R.layout.win_combo_sel;
	}

	@Override
	protected void initView() {

		Intent intent = this.getIntent();
		this.productId = intent.getLongExtra("productId", -1);
		setUpActionBar("套餐");
		btnJoinCart=findViewById(R.id.btn_join_cart);
		btnToBuy=findViewById(R.id.btn_to_buy);
		btnChat=findViewById(R.id.btn_chat);
		int size = productComboList.size();
		tabName = new String[size];
		for (int i = 0; i < size; i++) {

			Pakage pCombo = (Pakage) productComboList.get(i);
			tabName[i] = pCombo.getName();
			FavourableComboFragment fragment = new FavourableComboFragment(
					pCombo, skus, productId,this,i);
			fragments.add(fragment);

		}
		txtPrice=(TextView) findViewById(R.id.txt_price);
		txtPrePrice=(TextView) findViewById(R.id.txt_pre_price);
		btnJoinCart.setOnClickListener(this);
		btnToBuy.setOnClickListener(this);
		btnChat.setOnClickListener(this);
	}



	@Override
	protected String[] tabName() {
		return tabName;
	}

	private boolean verifyProduct(List<NewProduct> list)
	{
		if(list==null)
		{
			return false;
		}
		if(list.size()==1)
		{
			ViewUtils.showToast("请至少选择一个搭配商品");
			return false;
		}else
		{
			for(NewProduct product:list)
			{
				if(!product.getSkuList().isEmpty())
				{
					if(product.getSelSku()==null)
					{
						ViewUtils.showToast("请选择商品属性");
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if(verifyProduct(selProductList))
		{
			if(v==btnToBuy)
			{
				/*Pakage src=(Pakage)productComboList.get(index);
				BuyItem buyItem=new BuyItem();
				buyItem.setId(System.currentTimeMillis());
				buyItem.setItemId(System.currentTimeMillis()+"");
				buyItem.setPakageId(src.getPakageId());
				buyItem.setPcs(1);
				buyItem.setPrice(totalPrice);
				buyItem.addAllProduct(selProductList);
				buyItem.setType(3);
				VerifyBuyAct.start(this,buyItem);*/
				AffirmOrderAct.start(this,selProductList,0,totalPrice);
			}else if(v==btnJoinCart)
			{
				Pakage src=(Pakage)productComboList.get(index);
				Pakage pkage2=new Pakage();
				pkage2.setPakageId(src.getPakageId());
				pkage2.setName(src.getName());
				pkage2.setPcs(1);
				pkage2.setPrice(totalPrice);
				pkage2.addAllProduct(selProductList);
				pkage2.setPrice(totalPrice);
				new AddPakageToCartRequest(pkage2).start();
			}
			if(v==btnChat)
			{
				if(Account.user!=null) {
					new SelectChatDialog().show();
				}else{
					LoginAct.start(this);
				}
			}
		}

	}

	@Override
	public void onCall(int id, Object object) {
		if(id!=index)
			return ;
		totalPrice=0;
		selProductList=(List<NewProduct>) object;
		float favorablePrice=0;//优惠
		for(NewProduct product:selProductList)
		{
			float origPrice=0,price=0;
			if(product.getSelSku()!=null)
			{
				origPrice=product.getSelSku().getPrice();
				price=origPrice-product.getPreference();
			}else
			{
				origPrice=product.getPrice();
				price=origPrice-product.getPreference();
			}
			totalPrice=totalPrice+price;
			favorablePrice=favorablePrice+origPrice;
		}
		String strtotalPrice="套餐价格：￥"+StringUtil.getFormatPrice(totalPrice);
	    txtPrice.setText(strtotalPrice);
		ViewUtils.setText(txtPrePrice, "优惠：￥"+ StringUtil.getFormatPrice(favorablePrice-totalPrice));
	}
}
