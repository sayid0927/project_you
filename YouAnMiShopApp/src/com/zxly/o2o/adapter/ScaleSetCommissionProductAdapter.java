package com.zxly.o2o.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2015/7/9.
 */
public class ScaleSetCommissionProductAdapter extends ObjectAdapter {
    public ScaleSetCommissionProductAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_scale_set_comission_product;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.itemIcon= (NetworkImageView) convertView.findViewById(R.id.img_item_icon);
            holder.imgPlotFlag= (ImageView) convertView.findViewById(R.id.img_plot_flag);
            holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtPrice= (TextView) convertView.findViewById(R.id.txt_price);
            holder.txtComission= (TextView) convertView.findViewById(R.id.txt_comission);
            holder.txtOldPrice= (TextView) convertView.findViewById(R.id.txt_old_price);
            holder.txtScale= (TextView) convertView.findViewById(R.id.txt_scale);
            holder.lineBottom=convertView.findViewById(R.id.line_bottom);
            convertView.setTag(holder);
        }else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        final Product product= (Product) getItem(position);

        holder.itemIcon.setDefaultImageResId(R.drawable.product_def);
        holder.itemIcon.setImageUrl(product.getHeadUrl(),
                AppController.imageLoader);
        float curPrice = product.getPrice()-product.getPreference();
        ViewUtils.setText(holder.txtName, product.getName());
        if(product.getPreference()>0)
        {
            ViewUtils.strikeThruText(holder.txtOldPrice,  product.getPrice());
        }
        String commission;
        SpannableString ss1;
        if(product.getComission()>0)
        {
            commission="佣金：￥"+product.getComission();
            ViewUtils.setText(holder.txtScale,product.getRate()+"%");
            ss1=new SpannableString(commission);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#EB3434")), 3, commission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else
        {
            commission="无佣金";
            ss1=new SpannableString(commission);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0,  commission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.txtComission.setText(ss1);
        ViewUtils.setTextPrice(holder.txtPrice, curPrice);
        if (position == getCount() - 1) {
            ViewUtils.setVisible(holder.lineBottom);

        } else {
            ViewUtils.setInvisible(holder.lineBottom);
        }

        return convertView;
    }
    class ViewHolder{
        NetworkImageView itemIcon;
        ImageView imgPlotFlag;
        TextView txtName,txtPrice,txtComission,txtOldPrice,txtScale;
        View lineBottom;
    }
}
