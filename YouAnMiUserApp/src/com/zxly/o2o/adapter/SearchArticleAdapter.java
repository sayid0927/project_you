package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.ProductArticles;
import com.zxly.o2o.o2o_user.R;

/**
 * Created by dsnx on 2015/12/14.
 */
public class SearchArticleAdapter extends ObjectAdapter {

    public SearchArticleAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_articel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.imgArticel= (NetworkImageView) convertView.findViewById(R.id.img_article);
            holder.txtArticleTitle= (TextView) convertView.findViewById(R.id.txt_article_title);
            holder.txtLikeCount= (TextView) convertView.findViewById(R.id.txt_likeCount);
            convertView.setTag(holder);
        }else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        ProductArticles productArticles= (ProductArticles) getItem(position);
        holder.imgArticel.setImageUrl(productArticles.getImageUrl(), AppController.imageLoader);
        holder.txtArticleTitle.setText(productArticles.getTitle());
        holder.txtLikeCount.setText(productArticles.getEnjoyAmount()+"");
        return convertView;
    }



    private class ViewHolder{
        NetworkImageView imgArticel;
        TextView  txtArticleTitle;
        TextView  txtLikeCount;
    }
}
