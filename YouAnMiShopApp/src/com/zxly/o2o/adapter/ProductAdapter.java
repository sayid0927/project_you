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
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.model.Product;
import com.zxly.o2o.util.ShareListener;
import com.zxly.o2o.request.PromoteCallbackConfirmRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2015/7/8.
 */
public class ProductAdapter extends ObjectAdapter {

    private ShareDialog shareDialog;
    private   int type;//1:上架 0:未上架
    public ProductAdapter(Context _context,int type) {
        super(_context);
        this.type=type;
    }
    public ProductAdapter(Context _context) {
        super(_context);
        this.type=1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_product;
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
            holder.btnTuiguang= (TextView) convertView.findViewById(R.id.btn_tuiguang);
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

        switch (product.getTypeCode()) {
            case 1:
                ViewUtils.setVisible(holder.imgPlotFlag);
                holder.imgPlotFlag.setBackgroundResource(R.drawable.qianggou);
                break;
            case 2:
                holder.imgPlotFlag.setBackgroundResource(R.drawable.youhui);
                ViewUtils.setVisible(holder.imgPlotFlag);
                break;
            default:
                ViewUtils.setGone(holder.imgPlotFlag);
                break;
        }
        if(product.getPreference()>0)
        {
           ViewUtils.strikeThruText(holder.txtOldPrice, product.getPrice());
        }
        if(type==1)
        {
            String commission;
            SpannableString ss1;
            if(product.getComission()>0)
            {
                commission="佣金：￥"+product.getComission();
                ss1=new SpannableString(commission);
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#EB3434")), 3, commission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.btnTuiguang.setText("推广");
            }else
            {
                commission="无佣金";
                ss1=new SpannableString(commission);
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 0,  commission.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.btnTuiguang.setText("分享");
            }
            holder.txtComission.setText(ss1);
            holder.btnTuiguang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(shareDialog==null)
                        shareDialog=new ShareDialog();

                    shareDialog.show(product.getName(), product.getShareUrl(), product.getHeadUrl(), new ShareListener() {
                        @Override
                        public void onComplete(Object var1) {
                            new PromoteCallbackConfirmRequest(product.getId(), 1,product.getName()).start();
                        }

                        @Override
                        public void onFail(int errorCode) {

                        }
                    });
                }
            });
        }else
        {
            ViewUtils.setGone( holder.txtComission);
            ViewUtils.setGone( holder.btnTuiguang);
        }

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
        TextView txtName,txtPrice,txtComission,txtOldPrice;
        TextView btnTuiguang;
        View lineBottom;
    }
}
