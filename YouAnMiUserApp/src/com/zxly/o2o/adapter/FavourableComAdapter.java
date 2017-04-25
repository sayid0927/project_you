package com.zxly.o2o.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.ProductPropertySelDialog;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.model.Skus;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ResourceAsColor") public class FavourableComAdapter extends ObjectAdapter {

	private ProductPropertySelDialog proPropSelDialog;
    private ListView listView;
	private List<NewProduct> selProductList=new ArrayList<NewProduct>();
	private ParameCallBack callBack;
	public FavourableComAdapter(Context _context, ParameCallBack callBack, ListView listView) {
		super(_context);
        this.listView=listView;
		this.callBack=callBack;
	}

	public List<NewProduct> getSelProductList() {
		return selProductList;
	}
	public void addProductList(List<NewProduct> list)
	{
		if(list!=null&&!list.isEmpty())
		{
			selProductList.addAll(list);
		}
	}
	public void addProduct(NewProduct product)
	{
		if(product!=null)
		{
			selProductList.add(product);
		}
	}


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
		holder.productIcon.setImageUrl(product.getHeadUrl(), AppController.imageLoader);
		ViewUtils.setText(holder.txtProductName,product.getName());
		ViewUtils.setText(holder.txtPrice, product.getCurPriceStr());
		if(product.getPreference()>0)
		{
			ViewUtils.setText(holder.txtPrefPrice, "优惠  ￥"+ StringUtil.getFormatPrice(product.getPreference()));
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
                updateSingleRow(listView,product);
				callBack.onCall(selProductList);
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
								((TextView) v).setTextColor(R.color.darkgray);
								TextView txtP=(TextView) v.getTag();
								ViewUtils.setText(txtP,skus.getPrice()-product.getPreference());
								ViewUtils.setText(v, "已选"+skus.getParamComNames());
								callBack.onCall(selProductList);
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
