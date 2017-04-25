package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.model.Pakage;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2015/5/20.
 */
public class RefundmentDetailAdapter extends ObjectAdapter {

    public RefundmentDetailAdapter(Context context) {
        super(context);

    }

    @Override
    public int getLayoutId() {
        return R.layout.refundment_package_item;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Object object = getItem(position);
        PakageProductHolder pph;
        if (convertView == null) {
            pph = new PakageProductHolder();
            convertView = inflateConvertView();
            convertView.setTag(pph);
        } else {
            pph = (PakageProductHolder) convertView.getTag();
        }
        holder = pph;
        Pakage pakage = (Pakage) object;
        ViewUtils.setText(pph.txtBuySum, pakage.getPcs());
        ViewUtils.setTextPrice(pph.txtTotalPrice, pakage.getPrice());

        CommProductAdapter commPrdoAdapter = (CommProductAdapter) pph.listView.getAdapter();
        if (commPrdoAdapter == null) {
            commPrdoAdapter = new CommProductAdapter(context);
            pph.listView.setAdapter(commPrdoAdapter);
        }
        commPrdoAdapter.clear();
        commPrdoAdapter.addItem(pakage.getProductList(), true);

        return convertView;
    }

    class ViewHolder {

        TextView checkbox, txtTotalPrice, txtDel;
        TextView txtBuySum, txtSubtract, txtAdd;

    }

    class PakageProductHolder extends ViewHolder {
        ListView listView;
    }
}
