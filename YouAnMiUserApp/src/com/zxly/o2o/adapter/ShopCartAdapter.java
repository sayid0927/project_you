package com.zxly.o2o.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.BuyItem;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ModifyShopingCartSumRequest;
import com.zxly.o2o.request.RemoveCartProductRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/5/21.
 */
public class ShopCartAdapter extends ObjectAdapter implements
		View.OnClickListener {
	private ListView listView;
	private List<Object> selItem = new ArrayList<Object>();
	private List<NewProduct> selProductList = new ArrayList<NewProduct>();
	private ParameCallBackById callBack;
	private float totalPrice = 0f;
	private boolean isSingleUpdate = false;
	private int pcs;
	float price;
	private boolean isDel;

	public ShopCartAdapter(Context _context, ListView listView, ParameCallBackById callBack) {
		super(_context);
		this.listView = listView;
		this.callBack = callBack;
	}

	public List<NewProduct> getSelProductList() {
		return selProductList;
	}
	public List<Object> getSelItem()
	{
		return selItem;
	}

	public void addSelItem(BuyItem buyItem) {
		float price;
		int pcs;
		price = buyItem.getPrice();
		pcs = buyItem.getPcs();
		totalPrice = totalPrice + (price * pcs);
		selItem.add(buyItem);
		callBack.onCall(getCount(), totalPrice);
		selProductList.addAll(buyItem.getProductList());
	}

	public void setIsDel(boolean isDel) {
		this.isDel = isDel;
		notifyDataSetChanged();
	}
	public void allSel(boolean isAllSel)
	{
		totalPrice=0;
		if(isAllSel)
		{
			selItem.clear();
			selProductList.clear();
			selItem.addAll(getContent());
			int size=selItem.size();
			for(int i=0;i<size;i++)
			{
				BuyItem buyItem= (BuyItem) selItem.get(i);
				selProductList.addAll(buyItem.getProductList());
				float price = buyItem.getPrice();
				int pcs = buyItem.getPcs();
				totalPrice = totalPrice + (price * pcs);
			}
		}else
		{
			selItem.clear();
			selProductList.clear();
		}
		callBack.onCall(getCount(), totalPrice);
		notifyDataSetChanged();
	}

	@Override
	public int getLayoutId() {
		return R.layout.item_shop_car1t;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		BuyItem buyItem = (BuyItem) getItem(position);
		if (buyItem.getType()==3) {//套餐
			return 1;
		} else {
			return 2;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		final BuyItem shopItem = (BuyItem) getItem(position);
		int type=getItemViewType(position);
		if(convertView==null)
		{
			switch (type)
			{
				case 1://套餐
					PakageProductHolder pph=new PakageProductHolder();
					convertView = inflateConvertView(R.layout.item_shop_car1t);
					pph.txtBuySum = (TextView) convertView
							.findViewById(R.id.txt_buy_sum);
					pph.txtSubtract = (TextView) convertView
							.findViewById(R.id.txt_subtract);
					pph.txtTotalPrice = (TextView) convertView
							.findViewById(R.id.txt_total_price);
					pph.txtAdd = (TextView) convertView.findViewById(R.id.txt_add);
					pph.txtDel = (TextView) convertView.findViewById(R.id.txt_del);
					pph.checkbox = (ImageView) convertView.findViewById(R.id.checkbox);
					pph.listView = (ListView) convertView.findViewById(R.id.listview);
					pph.listView.setDividerHeight(0);
					holder=pph;
					break;
				case 2://单个商品
					ProductHolder ph=new ProductHolder();
					convertView=inflateConvertView(R.layout.item_shop_cart2);
					ph.txtBuySum = (TextView) convertView
							.findViewById(R.id.txt_buy_sum);
					ph.txtSubtract = (TextView) convertView
							.findViewById(R.id.txt_subtract);
					ph.txtAdd = (TextView) convertView.findViewById(R.id.txt_add);
					ph.txtDel = (TextView) convertView.findViewById(R.id.txt_del);
					ph.checkbox = (ImageView) convertView.findViewById(R.id.checkbox);
					ph.txtName=(TextView) convertView.findViewById(R.id.txt_name);
					ph.txtPrice=(TextView) convertView.findViewById(R.id.txt_price);
					ph.txtPrefPrice= (TextView) convertView.findViewById(R.id.txt_pref_price);
					ph.itemIcon=(NetworkImageView) convertView.findViewById(R.id.item_icon);
					ph.txtRemark=(TextView) convertView.findViewById(R.id.txt_remark);
					holder=ph;
					break;
			}
			convertView.setTag(holder);
		}else
		{
			holder= (ViewHolder) convertView.getTag();
		}
		switch (type)
		{
			case 1://套餐
				setItemPakageData(shopItem, (PakageProductHolder) holder);
				break;
			case 2://单个商品
				setItemProductData(shopItem, (ProductHolder) holder);
				break;
		}
		ViewUtils.setText(holder.txtBuySum, shopItem.getPcs());
		if(isDel)
		{
			ViewUtils.setVisible(holder.txtDel);
		}else
		{
			ViewUtils.setGone(holder.txtDel);
		}
		if (selItem.contains(shopItem)) {
			holder.checkbox.setSelected(true);
		} else {
			holder.checkbox.setSelected(false);
		}

		holder.txtAdd.setTag(shopItem);
		holder.txtSubtract.setTag(shopItem);
		holder.txtDel.setTag(shopItem);
		holder.txtAdd.setOnClickListener(this);
		holder.txtSubtract.setOnClickListener(this);
		holder.txtDel.setOnClickListener(this);
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				float price = shopItem.getPrice();
				int pcs = shopItem.getPcs();

				if (selItem.contains(shopItem)) {
					totalPrice = totalPrice - (price * pcs);
					selItem.remove(shopItem);
					selProductList.removeAll(shopItem.getProductList());
				} else {
					totalPrice = totalPrice + (price * pcs);
					selItem.add(shopItem);
					selProductList.addAll(shopItem.getProductList());
				}
				isSingleUpdate = true;
				updateSingleRow(listView, shopItem);
				callBack.onCall(getCount(), totalPrice);
			}
		});

		return convertView;
	}
	private void setItemProductData(final BuyItem shopItem, ProductHolder holder) {

		ViewUtils.setText(holder.txtBuySum, shopItem.getPcs());
		NewProduct product=shopItem.getProductList().get(0);
		holder.itemIcon.setImageUrl(product.getHeadUrl(), AppController.imageLoader);
		ViewUtils.setText(holder.txtName, product.getName());
		ViewUtils.setText(holder.txtRemark,product.getRemark());
		ViewUtils.setTextPrice(holder.txtPrice, product.getCurPrice());
		if(product.getPreference()>0)
		{
			ViewUtils.setTextPrice(holder.txtPrefPrice, product.getPrice());
		}
	}
	private void setItemPakageData(final BuyItem shopItem, PakageProductHolder holder)
	{

		String price="￥"+ StringUtil.getFormatPrice(shopItem.getPrice() * shopItem.getPcs());
		String prefPrice = "￥" + StringUtil.getFormatPrice(shopItem.getPrefPrice());
		StringBuilder builder = new StringBuilder();
		builder.append(price).append("（优惠").append(prefPrice).append("）");

		SpannableString ss1 = new SpannableString(builder.toString());

		ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#fe0000")), 0, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), price.length(), price.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")),  price.length()+3, (price.length()+3+prefPrice.length()) , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),  (price.length()+3+prefPrice.length()), (price.length()+3+prefPrice.length())+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.txtTotalPrice.setText(ss1);

		if (!isSingleUpdate) {
			ShopChartProductAdapter scpAdapter = (ShopChartProductAdapter) holder.listView
					.getAdapter();
			if (scpAdapter == null) {
				scpAdapter = new ShopChartProductAdapter(context);
				holder.listView.setAdapter(scpAdapter);
			}
			scpAdapter.clear();
			scpAdapter.addItem(shopItem.getProductList(), true);

		} else {
			isSingleUpdate = false;
		}



	}
	/**
	 * 
	 * @param actionType 1:加  2:减
	 * @param shopItem
	 */
	private void updateUI(int actionType,BuyItem shopItem)
	{
		shopItem.setPcs(pcs);
		List<NewProduct> list = shopItem.getProductList();
		for (NewProduct np : list) {
			np.setPcs(pcs);
		}
		isSingleUpdate = true;
		updateSingleRow(listView, shopItem);
		if (selItem.contains(shopItem)) {
			if(actionType==1)
			{
				totalPrice = totalPrice + price;
			}else
			{
				totalPrice = totalPrice - price;
			}
			callBack.onCall(getCount(),totalPrice);
		}
		
	}

	@Override
	public void onClick(View v) {
		final BuyItem shopItem = (BuyItem) v.getTag();
		pcs = shopItem.getPcs();
		price = shopItem.getPrice();
		switch (v.getId()) {
		case R.id.txt_add:
			pcs = pcs + 1;
			ModifyShopingCartSumRequest addRequest=new ModifyShopingCartSumRequest(pcs, shopItem.getItemId());
			addRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

				@Override
				public void onOK() {

					updateUI(1,shopItem);
				}

				@Override
				public void onFail(int code) {

				}

			});
			addRequest.start();
			
			
			break;
		case R.id.txt_subtract:
			if (pcs > 1) {
				pcs = pcs - 1;
				ModifyShopingCartSumRequest subRequest=new ModifyShopingCartSumRequest(pcs, shopItem.getItemId());
				subRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

					@Override
					public void onOK() {
						updateUI(2,shopItem);
					}

					@Override
					public void onFail(int code) {

					}

				});
				subRequest.start();
				
				
			}
			break;
		case R.id.txt_del:
			new RemoveCartProductRequest(shopItem.getItemId(), new CallBack() {
				@Override
				public void onCall() {
					content.remove(shopItem);
					notifyDataSetChanged();
					if (selItem.contains(shopItem)) {
						totalPrice = totalPrice - (price * pcs);
						selItem.remove(shopItem);
						selProductList.removeAll(shopItem.getProductList());
					}
					callBack.onCall(getCount(),totalPrice);
				}
			}).start();
			break;
		}

	}

	class ViewHolder {
		ImageView checkbox;
		TextView txtBuySum, txtSubtract, txtAdd,txtDel;
	}



	class PakageProductHolder extends ViewHolder {
		TextView  txtTotalPrice;
		ListView listView;
	}
	class ProductHolder extends ViewHolder{
		TextView txtName;
		TextView txtRemark,txtPrice,txtPrefPrice;
		NetworkImageView itemIcon;
	}

}
