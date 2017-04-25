package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxly.o2o.model.AddressCity;
import com.zxly.o2o.model.AddressProvince;
import com.zxly.o2o.shop.R;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends ObjectAdapter {
    private List<AddressProvince> provinceList = null;
    private List<AddressCity> cityList = null;

    private ArrayList<String> dataList = null;

    private int currentState = 0;


    public AddressAdapter(Context context) {
        super(context);
    }

    public void setProvinceList(List<AddressProvince> provinceList) {
        this.provinceList = provinceList;
    }

    public void setDataList(ArrayList<String> dataList) {
        this.dataList = dataList;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AddressItemHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new AddressItemHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            convertView.setTag(holder);
        } else {
            holder = (AddressItemHolder) convertView.getTag();
        }
        holder.txtName.setText((String) getItem(position));
        return convertView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_area;
    }

    class AddressItemHolder {
        TextView txtName;
    }

}
