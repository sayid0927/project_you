package com.zxly.o2o.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.ProductInfoAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.ProductArticles;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2015/12/12.
 */
public class DiscoverAdapter extends ObjectAdapter implements  View.OnClickListener{
    public DiscoverAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_discover;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView==null)
        {
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.txtName= (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtContent=(TextView)convertView.findViewById(R.id.txt_content);
            holder.txtMinPrice=(TextView)convertView.findViewById(R.id.txt_minPrice);
            holder.txtMaxPrice=(TextView)convertView.findViewById(R.id.txt_maxPrice);
            holder.txtCount= (TextView) convertView.findViewById(R.id.txt_count);
            holder.imgProduct= (NetworkImageView) convertView.findViewById(R.id.img_product);
            holder.txtLikeCount= (TextView) convertView.findViewById(R.id.txt_likeCount);
            holder.btnProductInfo1=convertView.findViewById(R.id.btn_product_info1);
            holder.btnProductInfo2=convertView.findViewById(R.id.btn_product_info2);
            convertView.setTag(holder);
        }else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        final ProductArticles pa= (ProductArticles) getItem(position);
        holder.txtName.setText(pa.getName());
        holder.txtContent.setText("   "+pa.getContent());
        ViewUtils.setTextPrice(holder.txtMinPrice, pa.getPrice());
        ViewUtils.setText(holder.txtCount, position + 1);
        ViewUtils.setText(holder.txtLikeCount,pa.getEnjoyAmount());
        holder.imgProduct.setImageUrl(pa.getImageUrl(), AppController.imageLoader);
        holder.btnProductInfo1.setTag(pa);
        holder.btnProductInfo2.setTag(pa);
        holder.btnProductInfo1.setOnClickListener(this);
        holder.btnProductInfo2.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        ProductArticles pa= (ProductArticles) v.getTag();
        ProductInfoAct.start((Activity)this.context, pa.getProductId());
    }

    class ViewHolder{
        TextView txtName,txtContent,txtMinPrice,txtMaxPrice,txtCount,txtLikeCount;
        View btnProductInfo1,btnProductInfo2;
        NetworkImageView imgProduct;
    }
}
