package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.dialog.TwoBtnActionDialog;
import com.zxly.o2o.model.UsedProduct;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.UsedCollectRequest;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public class UsedProductCollectedAdapter extends ObjectAdapter {

    public UsedProductCollectedAdapter(Context _context) {
        super(_context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.itemIcon = (NetworkImageView) convertView.findViewById(R.id.img_item);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.txt_address);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txt_price);
            holder.productType = convertView.findViewById(R.id.img_plot_flag);
            holder.lineBottom = convertView.findViewById(R.id.line_bottom);
            holder.btnDel = convertView.findViewById(R.id.btn_del);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        final UsedProduct product = (UsedProduct) getItem(position);
        holder.itemIcon.setDefaultImageResId(R.drawable.used_product_def);
        holder.itemIcon.setImageUrl(product.getHeadUrl(), AppController.imageLoader);
        ViewUtils.setText(holder.txtName, product.getName());
        ViewUtils.setTextPrice(holder.txtPrice, product.getPrice());
        ViewUtils.setText(holder.txtTime, StringUtil.getShortTime(product.getPutawayTime()) + "发布");
        ViewUtils.setText(holder.txtAddress, product.getContact().getAreaName() + "-" + product.getContact().getVillageName());
        switch (product.getAssureParty()) {
            case 2:
            case 3:
                ViewUtils.setVisible(holder.productType);
                break;
            default:
                ViewUtils.setGone(holder.productType);
                break;
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
                                final UsedCollectRequest request = new UsedCollectRequest(product);
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

        if (position == getCount() - 1) {
            ViewUtils.setVisible(holder.lineBottom);
        } else {
            ViewUtils.setGone(holder.lineBottom);
        }
        return convertView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_used_collected_products;
    }

    class ViewHolder {
        NetworkImageView itemIcon;
        TextView txtPrice, txtAddress, txtName, txtTime;
        View lineBottom, productType, btnDel;
    }

}
