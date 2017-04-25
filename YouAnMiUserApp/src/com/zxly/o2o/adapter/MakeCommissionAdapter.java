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
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2015/7/16.
 */
public class MakeCommissionAdapter extends ObjectAdapter {
    public MakeCommissionAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_make_commission;
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
            holder.btnTuiguang=convertView.findViewById(R.id.btn_tuiguang);
            holder.lineBottom=convertView.findViewById(R.id.line_bottom);
            convertView.setTag(holder);
        }else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        final NewProduct product= (NewProduct) getItem(position);
        holder.itemIcon.setDefaultImageResId(R.drawable.product_def);
        holder.itemIcon.setImageUrl(product.getHeadUrl(),
                AppController.imageLoader);
        float curPrice = product.getPrice()-product.getPreference();
        ViewUtils.setText(holder.txtName, product.getName());
        if(product.getPreference()>0)
        {
            ViewUtils.strikeThruText(holder.txtOldPrice,  product.getPrice());
        }
        String comission;
        SpannableString ss1;
        if(product.getComission()>0)
        {
            comission="佣金：￥"+product.getComission();
            ss1=new SpannableString(comission);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#EB3434")), 3, comission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else
        {
            comission="无佣金";
            ss1=new SpannableString(comission);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0,  comission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.txtComission.setText(ss1);
        ViewUtils.setTextPrice(holder.txtPrice,curPrice);
        if (position == getCount() - 1) {
            ViewUtils.setVisible(holder.lineBottom);

        } else {
            ViewUtils.setInvisible(holder.lineBottom);
        }
        holder.btnTuiguang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PromoteCallbackConfirmRequest(product.getId(),1).start();
                ViewUtils.share(context,product.getName()+product.getShareUrl());
            }
        });
        return convertView;
    }
    class ViewHolder{
        NetworkImageView itemIcon;
        ImageView imgPlotFlag;
        TextView txtName,txtPrice,txtComission,txtOldPrice;
        View btnTuiguang ,lineBottom;
    }

}
