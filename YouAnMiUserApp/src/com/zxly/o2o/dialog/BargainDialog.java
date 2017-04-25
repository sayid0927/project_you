package com.zxly.o2o.dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.adapter.ProductParamSelAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Skus;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BargainRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public class BargainDialog extends BaseDialog implements View.OnClickListener {

	private ListView paramSelListView;
	private ProductParamSelAdapter ppsAdapter;
	private View btnClose;
	private TextView txtPrice, txtProductName, btnOk;
	private NetworkImageView productIcon;
	private EditText barginPrice;
	private NewProduct product;
	private Skus selSkus;

	public BargainDialog() {
		super();
		View footerView = LayoutInflater.from(context).inflate(
				R.layout.view_bargain_list_footer, null);
		barginPrice=(EditText) footerView.findViewById(R.id.bargin_price);
		paramSelListView.addFooterView(footerView);
		ViewUtils.setText(btnOk, "确定");
		ppsAdapter = new ProductParamSelAdapter(this.context, new CallBack() {

			@Override
			public void onCall() {
				Skus sku = ppsAdapter.getSelSku();
				if (sku != null) {
					selSkus = sku;
					btnOk.setEnabled(true);
					ViewUtils.setTextPrice(txtPrice,
							 (sku.getPrice() - BargainDialog.this.product.getPreference()));
				} else {
					selSkus = null;
					ViewUtils.setText(txtPrice, BargainDialog.this.product.getCurPriceStr());
					btnOk.setEnabled(false);
				
				}

			}
		});
		paramSelListView.setAdapter(ppsAdapter);

	}

	@Override
	public int getLayoutId() {

		return R.layout.dialog_property_sel;
	}

	@Override
	protected void initView() {
		paramSelListView = (ListView) findViewById(R.id.lv_param_sel);
		btnClose = findViewById(R.id.btn_close);

		txtPrice = (TextView) findViewById(R.id.txt_price);
		txtProductName = (TextView) findViewById(R.id.txt_product_name);
		productIcon = (NetworkImageView) findViewById(R.id.img_product);
		btnOk = (TextView) findViewById(R.id.btn_ok);
		btnClose.setOnClickListener(this);

		btnOk.setOnClickListener(this);

	}

	public void show(NewProduct _product) {
		super.show();
		this.product = _product;
		selSkus=this.product.getSelSku();
		ppsAdapter.addItem(_product.getProductSKUParamList(), true);
		ppsAdapter.setSkuList(_product.getSkuList());
		ppsAdapter.setSelSku(selSkus);
		if (this.product.getSelSku() != null) {
			ViewUtils.setTextPrice(txtPrice,this.product.getSelSku().getPrice()-product.getPreference());
		}else
		{
			ViewUtils.setText(txtPrice, this.product.getCurPriceStr());
		}
		ViewUtils.setText(txtProductName, this.product.getName());
		productIcon.setImageUrl(this.product.getHeadUrl(),
				AppController.imageLoader);
		ppsAdapter.notifyDataSetChanged();
	}


	@Override
	protected boolean isLimitHeight() {

		return true;
	}
	@Override
	public void onClick(View v) {
		if(v==btnClose)
		{
			dismiss();
		}else if(v==btnOk)
		{

			if(selSkus!=null)
			{
				String txtPrice=barginPrice.getText().toString();
				if(!StringUtil.isNull(txtPrice))
				{
					float price=Float.valueOf(txtPrice);
					if(price<=selSkus.getPrice())
					{
						new BargainRequest(price, this.product.getId(),selSkus).start(this);
						dismiss();
					}else
					{
						ViewUtils.showToast("不能大于当前价!");
					}
					
					
				}else
				{
					ViewUtils.showToast("请输入您想要的价格");
				}
			}else
			{
				ViewUtils.showToast("请选择商品属性");
			}
			
		}
		
		}

	
}
