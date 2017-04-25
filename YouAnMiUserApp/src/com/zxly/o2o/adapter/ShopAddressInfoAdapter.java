package com.zxly.o2o.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2016/10/12.
 */
public class ShopAddressInfoAdapter extends ObjectAdapter {
    private UserAddress  curSelectAddress;
    public ShopAddressInfoAdapter(Context _context) {
        super(_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_shop_address;
    }

    public void setCurSelectAddress(UserAddress curSelectAddress) {
        this.curSelectAddress = curSelectAddress;
    }

    public UserAddress getCurSelectAddress() {
        return curSelectAddress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            convertView=inflateConvertView();
            holder=new ViewHolder();
            holder.btnCheck= (ImageView) convertView.findViewById(R.id.btn_check);
            holder.btnCall= (ImageView) convertView.findViewById(R.id.btn_call);
            holder.txtShopName= (TextView) convertView.findViewById(R.id.txt_shop_name);
            holder.txtAddress= (TextView) convertView.findViewById(R.id.txt_address);
            holder.txtTel= (TextView) convertView.findViewById(R.id.txt_tel);
            convertView.setTag(holder);
        }else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        final UserAddress userAddress= (UserAddress) getItem(position);
        if(userAddress==curSelectAddress)
        {
            holder.btnCheck.setSelected(true);
        }else
        {
            holder.btnCheck.setSelected(false);
        }
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("tel:"+userAddress.getMobilePhone()));
                context.startActivity(intent);
            }
        });
        ViewUtils.setText(holder.txtShopName,userAddress.getName());
        ViewUtils.setText(holder.txtAddress,userAddress.getAddress());
        ViewUtils.setText(holder.txtTel,userAddress.getMobilePhone());
        return convertView;
    }
    class ViewHolder{
        ImageView btnCheck,btnCall;
        TextView txtShopName;
        TextView txtAddress;
        TextView txtTel;
    }
}
