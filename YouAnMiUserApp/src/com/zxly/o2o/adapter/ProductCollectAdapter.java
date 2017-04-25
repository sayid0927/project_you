package com.zxly.o2o.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.TwoBtnActionDialog;
import com.zxly.o2o.model.NewProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ProductCollectRequest;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-23
 * @since YIBA-O2O
 */
public class ProductCollectAdapter extends ObjectAdapter {


    public ProductCollectAdapter(Context _context) {
        super(_context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.itemIcon = (NetworkImageView) convertView.findViewById(R.id.img_item);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txt_price);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.lowPrice = (TextView) convertView.findViewById(R.id.txt_low_price);
            holder.txtActType = (TextView) convertView.findViewById(R.id.txt_act_type);
            holder.comboFlag = convertView.findViewById(R.id.combo_flag);
            holder.lineBottom = convertView.findViewById(R.id.line_bottom);
            holder.btnDel = convertView.findViewById(R.id.btn_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NewProduct product = (NewProduct) getItem(position);
        holder.itemIcon.setImageUrl(product.getHeadUrl(), AppController.imageLoader);
        float curPrice = product.getPrice() - product.getPreference();
        if (product.getIsPakage() == 1) {
            ViewUtils.setVisible(holder.comboFlag);
        } else {
            ViewUtils.setGone(holder.comboFlag);
        }
        if (product.getPreference() > 0) {
            ViewUtils.strikeThruText(holder.lowPrice, "￥" + product.getPrice());
            ViewUtils.setText(holder.txtPrice, "￥" + curPrice);
        } else {
            ViewUtils.setText(holder.txtPrice, "￥" + curPrice);
        }
        ViewUtils.setText(holder.txtName, product.getName());
        switch (product.getTypeCode()) {
            case 1:
                ViewUtils.setVisible(holder.txtActType);
                holder.txtActType.setText("抢购");
                break;
            case 2:
                holder.txtActType.setText("清仓");
                ViewUtils.setVisible(holder.txtActType);
                break;
            default:
                ViewUtils.setGone(holder.txtActType);
                break;
        }
        if (position == getCount() - 1) {
            ViewUtils.setVisible(holder.lineBottom);

        } else {
            ViewUtils.setInvisible(holder.lineBottom);
        }
        holder.btnDel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TwoBtnActionDialog().show(new ParameCallBackById() {

                    @Override
                    public void onCall(int id, Object object) {
                        switch (id) {
                            case R.id.dialog_btn_left:
                                product.setCollect(1);
                                final ProductCollectRequest request = new ProductCollectRequest(product);
                                request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

                                    @Override
                                    public void onOK() {
                                        product.setCollect(request.getCollect());
                                        content.remove(product);
                                        notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onFail(int code) {

                                    }
                                });
                                request.start(context);
                                break;
                            case R.id.dialog_btn_right:
                                (new ViewUtils()).share(context, product.getHeadUrl() + "《" + product.getName() + "》");

                                break;
                            case R.id.dialog_btn_bottom:
                                break;
                        }
                    }
                });
            }
        });
        return convertView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_products_collect;
    }

    class ViewHolder {
        NetworkImageView itemIcon;
        TextView txtPrice, lowPrice, txtName, txtActType;
        View lineBottom, btnDel, comboFlag;
    }


}
