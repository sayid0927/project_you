package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.UserAddress;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

public class PersonalAddressAdapter extends ObjectAdapter {

    private OnAddressClickListener listener = null;
    private UserAddress defUserAddress = null;
    private UserAddress selectedAddress = null;

    public PersonalAddressAdapter(Context context, OnAddressClickListener listener) {
        super(context);
        this.listener = listener;
    }

    public void setSelectedAddress(UserAddress selectedAddress){
        this.selectedAddress = selectedAddress;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AddressViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new AddressViewHolder();
            holder.viewTopLine = convertView.findViewById(R.id.view_top_line);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtMobile = (TextView) convertView.findViewById(R.id.txt_mobile);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.txt_address);
            holder.btnSetDefault = (TextView) convertView
                    .findViewById(R.id.btn_set_default);
            holder.btnEdit = convertView.findViewById(R.id.btn_edit);
            holder.btnDel = convertView.findViewById(R.id.btn_del);
            convertView.setTag(holder);
        } else {
            holder = (AddressViewHolder) convertView.getTag();
        }
        final UserAddress address = (UserAddress) getItem(position);
        if (position == 0) {
            ViewUtils.setGone(holder.viewTopLine);
        } else {
            ViewUtils.setVisible(holder.viewTopLine);
        }
        holder.txtName.setText(address.getName());
        holder.txtMobile.setText(address.getMobilePhone());
        holder.txtAddress.setText(address.getAddress());
        if (1 == address.getIsDefault()) {
            holder.btnSetDefault.setSelected(true);
            defUserAddress = address;
        } else {
            holder.btnSetDefault.setSelected(false);
        }
        holder.btnEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listener.onEditClicked(address);
            }
        });
        holder.btnDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelClicked(address);
            }
        });
        holder.btnSetDefault.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (address.getIsDefault() == 2) {
                    listener.onDefaultClicked(address);
                }
            }
        });
        return convertView;
    }

    public UserAddress getDeliveryAddress() {
        if(selectedAddress != null){
            return selectedAddress;
        } else {
            return defUserAddress;
        }
    }

    public void resetDefUserAddress() {
        defUserAddress = null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_personal_address;
    }

    class AddressViewHolder {
        View viewTopLine;
        TextView txtName;
        TextView txtMobile;
        TextView txtAddress;
        TextView btnSetDefault;
        View btnEdit, btnDel;
    }

    public interface OnAddressClickListener {
        void onEditClicked(UserAddress address);

        void onDelClicked(UserAddress address);

        void onDefaultClicked(UserAddress address);
    }

}
