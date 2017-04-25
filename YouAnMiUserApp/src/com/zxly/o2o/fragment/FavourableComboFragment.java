package com.zxly.o2o.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.ProductPropertySelDialog;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Pakage;
import com.zxly.o2o.model.Skus;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.ComboInfoRequest;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

public class FavourableComboFragment extends BaseFragment implements View.OnClickListener {

	private  Pakage productCombo;
	private  Skus skus;
	private  long productId;
	private ListView productListView;
	private NetworkImageView headProductIcon;
	private FavourableComAdapter fcAdapter;
	private TextView txtTitle,txtHeadProductName,txtHeadPrice,txtHeadPrefPrice,txtHeadPropSel;
	private ProductPropertySelDialog proPropSelDialog;
	private LoadingView loadingview;
	private NewProduct product;
	private ParameCallBackById parameCallback;
	public List<NewProduct> selProductList=new ArrayList<NewProduct>();
	private int index;
	
	public FavourableComboFragment() {
	}
	
	@SuppressLint("ValidFragment")
	public FavourableComboFragment(Pakage productCombo,Skus skus,long productId,ParameCallBackById callBack,int index)
	{
		this.productCombo=productCombo;
		this.skus=skus;
		this.productId=productId;
		this.parameCallback=callBack;
		this.index=index;
	}
	@Override
	protected void initView() {
		
		productListView=(ListView) findViewById(R.id.lv_product_sel);
		txtTitle=(TextView) findViewById(R.id.txt_title);
		ViewUtils.setText(txtTitle, productCombo.getName());
		View headerView = inflater.inflate(R.layout.head_favourable_combo, null);
		headProductIcon=(NetworkImageView) headerView.findViewById(R.id.img_product_icon);
		txtHeadProductName=(TextView) headerView.findViewById(R.id.txt_name);
		txtHeadPrice=(TextView) headerView.findViewById(R.id.txt_price);
		txtHeadPrefPrice=(TextView) headerView.findViewById(R.id.txt_pref_price);
		txtHeadPropSel=(TextView) headerView.findViewById(R.id.txt_prop_sel);
		loadingview=(LoadingView) findViewById(R.id.view_loading);
		productListView.addHeaderView(headerView);
		txtHeadPropSel.setOnClickListener(this);
		fcAdapter=new FavourableComAdapter(this.getActivity());
		productListView.setAdapter(fcAdapter);
		if(skus!=null)
		{
			txtHeadPropSel.setTextColor(this.getResources().getColor(R.color.darkgray));
			ViewUtils.setText(txtHeadPropSel, skus.getParamComNames());
		}
		initData();
		
	}

	@Override
	protected int layoutId() {
		return R.layout.fragment_combo;
	}

	private void initData()
	{
		final ComboInfoRequest request=new ComboInfoRequest(productCombo.getPakageId(),productId);
		request.setOnResponseStateListener(new ResponseStateListener() {
			
			@Override
			public void onOK() {
				ViewUtils.setVisible(productListView);
				loadingview.onLoadingComplete();
				if (!request.getProductList().isEmpty()) {
					fcAdapter.addItem(request.getProductList(),true);
					productCombo.addAllProduct(request.getProductList());
					product=request.getProduct();
					if(skus!=null)
					{
						product.setSelSku(skus);
						ViewUtils.setText(txtHeadPrice, StringUtil.getFormatPrice(skus.getPrice()-product.getPreference()));
					}else
					{
						ViewUtils.setText(txtHeadPrice,product.getCurPriceStr());
					}
					selProductList.add(product);
					selProductList.addAll(request.getProductList());
					parameCallback.onCall(index,selProductList);
					headProductIcon.setImageUrl(product.getHeadUrl(),AppController.imageLoader);
					ViewUtils.setText(txtHeadProductName,product.getName());
					if(product.getPreference()>0)
					{
						ViewUtils.strikeThruText(txtHeadPrefPrice,product.getPrice());

					}
				
				}
			}
			
			@Override
			public void onFail(int code) {
				ViewUtils.setGone(productListView);
				loadingview.onLoadingFail("该套餐已经下架!",false);
			}
		});
		loadingview.startLoading();
		request.start(this);
	}
	@Override
	public void onClick(View v) {
		if(v==txtHeadPropSel)
		{

			if (proPropSelDialog == null) {
				proPropSelDialog = new ProductPropertySelDialog();
			}
			proPropSelDialog.show(new ParameCallBack() {
				
				@Override
				public void onCall(Object object) {
					if(object!=null)
					{
						skus=(Skus) object;
						txtHeadPropSel.setTextColor(FavourableComboFragment.this.getResources().getColor(R.color.light_grey));
						product.setSelSku(skus);
						ViewUtils.setText(txtHeadPrice, "￥" + StringUtil.getFormatPrice(skus.getPrice() - product.getPreference()));
						ViewUtils.strikeThruText(txtHeadPrefPrice, skus.getPrice());
						ViewUtils.setText(txtHeadPropSel, skus.getParamComNames());
						parameCallback.onCall(index,selProductList);
					}
					
				}
			},product,2);
		
		}
		
	}
	private class FavourableComAdapter extends ObjectAdapter {

		public FavourableComAdapter(Context _context) {
			super(_context);
		}

		private ProductPropertySelDialog proPropSelDialog;


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null)
			{
				convertView=inflateConvertView();
				holder=new ViewHolder();
				holder.checkbox=(TextView) convertView.findViewById(R.id.checkbox);
				holder.txtProductName=(TextView) convertView.findViewById(R.id.txt_name);
				holder.txtPrice=(TextView) convertView.findViewById(R.id.txt_price);
				holder.txtPrefPrice=(TextView) convertView.findViewById(R.id.txt_pref_price);
				holder.txtPropSel=(TextView) convertView.findViewById(R.id.txt_prop_sel);
				holder.productIcon=(NetworkImageView) convertView.findViewById(R.id.img_product_icon);
				convertView.setTag(holder);
			}else
			{
				holder=(ViewHolder) convertView.getTag();
			}
			final NewProduct product=(NewProduct) getItem(position);
	        if(selProductList.contains(product))
	        {
	            holder.checkbox.setSelected(true);
	        }else
	        {
	            holder.checkbox.setSelected(false);
	        }
			holder.productIcon.setImageUrl(product.getHeadUrl(),AppController.imageLoader);
			ViewUtils.setText(holder.txtProductName,product.getName());
			ViewUtils.setText(holder.txtPrice, product.getCurPriceStr());
			if(product.getPreference()>0)
			{
				ViewUtils.strikeThruText(holder.txtPrefPrice,product.getPrice());
			}
	        convertView.setOnClickListener(new View.OnClickListener() {
	        	
				@Override
				public void onClick(View v) {
	                if(selProductList.contains(product))
	                {
	                    selProductList.remove(product);
	                }else
	                {
	                    selProductList.add(product);
	                }
	                updateSingleRow(productListView,product);
	                parameCallback.onCall(index,selProductList);
					notifyDataSetChanged();
				}
			});
			if(!product.getSkuList().isEmpty())
			{
				ViewUtils.setVisible(holder.txtPropSel);
				holder.txtPropSel.setTag(holder.txtPrice);
				holder.txtPropSel.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(final View v) {
						if (proPropSelDialog == null) {
							proPropSelDialog = new ProductPropertySelDialog();
						}
						proPropSelDialog = new ProductPropertySelDialog();
						proPropSelDialog.show(new ParameCallBack() {
							
							@Override
							public void onCall(Object object) {
								if(object!=null)
								{
									Skus skus=(Skus) object;
									((TextView) v).setTextColor(getResources().getColor(R.color.light_grey));
									product.setSelSku(skus);
									TextView txtP=(TextView) v.getTag();
									ViewUtils.setText(txtP,"￥"+(skus.getPrice()-product.getPreference()));
									ViewUtils.setText(v, skus.getParamComNames());
									parameCallback.onCall(index,selProductList);
								}
								
							}
						},product,2);
					}
				});
			}else
			{
				ViewUtils.setGone(holder.txtPropSel);
			}

			
			return convertView;
		}

		@Override
		public int getLayoutId() {
			return R.layout.item_favourable_combo;
		}

		class ViewHolder{
			TextView checkbox,txtProductName,txtPrice,txtPrefPrice;
			NetworkImageView productIcon;
			TextView txtPropSel;

		}
	}
}