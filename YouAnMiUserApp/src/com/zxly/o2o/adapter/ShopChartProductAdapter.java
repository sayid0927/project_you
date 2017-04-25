package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2015/12/28.
 */
public class ShopChartProductAdapter extends ObjectAdapter {
    public ShopChartProductAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_shop_cart_product;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder;
        if(convertView==null)
        {
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.txtProductName=(TextView) convertView.findViewById(R.id.item_name);
            holder.txtPrice=(TextView) convertView.findViewById(R.id.txt_price);
            holder.txtPrefPrice= (TextView) convertView.findViewById(R.id.txt_pref_price);
            holder.icon=(NetworkImageView) convertView.findViewById(R.id.item_icon);
            holder.lineBottom=convertView.findViewById(R.id.line_bottom);
            holder.txtRemark=(TextView) convertView.findViewById(R.id.txt_remark);
            convertView.setTag(holder);
        }else
        {
            holder=(ViewHolder) convertView.getTag();
        }
        NewProduct np=(NewProduct) getItem(position);
        ViewUtils.setText(holder.txtProductName, np.getName());


        ViewUtils.setTextPrice(holder.txtPrice, np.getCurPrice());
        if(np.getPreference()>0)
        {
            ViewUtils.setTextPrice(holder.txtPrefPrice, np.getPrice());
        }

        ViewUtils.setText(holder.txtRemark,np.getRemark());
        holder.icon.setImageUrl(np.getHeadUrl(), AppController.imageLoader);
        if (position == getCount() - 1) {
            ViewUtils.setGone(holder.lineBottom);

        } else {
            ViewUtils.setVisible(holder.lineBottom);
        }
        return convertView;
    }

    class ViewHolder{
        View lineBottom;
        TextView txtProductName, txtPrice,txtPrefPrice,txtRemark;
        NetworkImageView icon;
    }

}
