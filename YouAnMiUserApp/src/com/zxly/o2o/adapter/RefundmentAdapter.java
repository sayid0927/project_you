package com.zxly.o2o.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.activity.RefundmentDetailActivity;
import com.zxly.o2o.activity.RefundmentListActivity;
import com.zxly.o2o.model.RefundmentDetail;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MListView;

/**
 * Created by Administrator on 2015/5/20.
 */
public class RefundmentAdapter extends ObjectAdapter {
    private final int TYPE_1 = 0;
    private final int TYPE_2 = 1;
    private Context mContext;

    public RefundmentAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.refundment_product_item;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        return ((RefundmentDetail) content.get(position)).getBuyItem()
                .getType() == Constants.PRODUCT_TYPE_PACKAGE ? TYPE_2 : TYPE_1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       final RefundmentDetail product = (RefundmentDetail) content.get(position);
        int type = getItemViewType(position);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            switch (type) {
                case 1:
                    viewHolder = new PakageProductHolder();
                    convertView = inflateConvertView(R.layout.refundment_package_item);
                    ((PakageProductHolder) viewHolder).listView = (MListView) convertView
                            .findViewById(R.id.listview);
                    ((PakageProductHolder) viewHolder).listView.setGetTouchEvent(false);
                    convertView.findViewById(R.id.taocan_icon).setVisibility(View.VISIBLE);

                    break;
                case 0:
                    viewHolder = new SingleProductHolder();
                    convertView = inflateConvertView(R.layout.refundment_product_item);
                    ((SingleProductHolder) viewHolder).item_name = (TextView) convertView
                            .findViewById(R.id.item_name);
                    ((SingleProductHolder) viewHolder).text_comboInfo = (TextView) convertView
                            .findViewById(R.id.text_comboInfo);
                    ((SingleProductHolder) viewHolder).item_price = (TextView) convertView
                            .findViewById(R.id.item_price);
                    ((SingleProductHolder) viewHolder).item_sum = (TextView) convertView
                            .findViewById(R.id.item_sum);
                    ((SingleProductHolder) viewHolder).item_icon = (NetworkImageView) convertView
                            .findViewById(R.id.item_icon);
                    break;
                default:
                    convertView = inflateConvertView(R.layout.refundment_package_item);
            }
            viewHolder.refundment_sum_cost = (TextView) convertView
                    .findViewById(R.id.refundment_sum_cost);
            viewHolder.refundment_sum_cost = (TextView) convertView
                    .findViewById(R.id.refundment_sum_cost);
            viewHolder.price = (TextView) convertView
                    .findViewById(R.id.price);
            viewHolder.order_number_text = (TextView) convertView
                    .findViewById(R.id.order_number_text);
            viewHolder.refundment_status = (TextView) convertView
                    .findViewById(R.id.refundment_status);
            viewHolder.order_number_type = (TextView) convertView
                    .findViewById(R.id.order_number_type);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setItemData(viewHolder, product, type,position);   //加载数据

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RefundmentListActivity) mContext).clickItem = product;
                Intent intent = new Intent();
                intent.setClass(mContext, RefundmentDetailActivity.class);
                intent.putExtra("refundId", product.getId());
                intent.putExtra("orderNo", product.getOrderNo());
                ViewUtils.startActivity(intent, (RefundmentListActivity)mContext);
            }
        });
        return convertView;
    }

    private void setItemData(ViewHolder viewHolder, RefundmentDetail product,
                             int type,int position) {
        switch (type) {
            case 0:
                SingleProductHolder singleProductHolder = (SingleProductHolder) viewHolder;
                singleProductHolder.item_sum.setText("×" + product.getBuyItem().getPcs());
                singleProductHolder.item_name.setText(
                        product.getBuyItem().getProducts().get(0).getName());
                singleProductHolder.text_comboInfo.setText(
                        product.getBuyItem().getProducts().get(0).getRemark());
                singleProductHolder.item_price.setText(
                        "￥" + product.getBuyItem().getProducts().get(0).getPrice());
                ImageUtil.setImage(singleProductHolder.item_icon,
                        product.getBuyItem().getProducts().get(0).getHeadUrl(),
                        R.drawable.product_def, null);
                break;
            case 1:
                PakageProductHolder pakageProductHolder = (PakageProductHolder) viewHolder;
                CommProductAdapter commPrdoAdapter = (CommProductAdapter) pakageProductHolder.listView
                        .getAdapter();
                if (commPrdoAdapter == null) {
                    commPrdoAdapter = new CommProductAdapter(context,true);
                    pakageProductHolder.listView.setAdapter(commPrdoAdapter);
                }
                commPrdoAdapter.clear();
                commPrdoAdapter.addItem(product.getBuyItem().getProducts(), true);
                break;
        }
        viewHolder.order_number_type.setText(product.getRefundType()== 1 ? "仅退款" : "退货退款");
        viewHolder.order_number_text.setText("订单号: " + product.getOrderNo());
        viewHolder.price.setText("商品实付款: ￥" + product.getRealprice());
        viewHolder.refundment_sum_cost.setText("￥" + product.getRefundPrice());
        switch (product.getStatus()) {
            case Constants.REFUND_ORDER_APPLY:
                viewHolder.refundment_status.setText("商家确认中");
                break;
            case Constants.REFUND_ORDER_CONFIRMING:
                viewHolder.refundment_status.setText("同意退款");
                break;
            case Constants.REFUND_ORDER_CONFIRMED:
                viewHolder.refundment_status.setText("待确认商品");
                break;
            case Constants.REFUND_ORDER_REFUND_DONE:
                viewHolder.refundment_status.setText("退款成功");
                break;
            case Constants.REFUND_ORDER_REJECT:
                viewHolder.refundment_status.setText("退款驳回");
                break;
            case Constants.REFUND_ORDER_CANCEL:
                viewHolder.refundment_status.setText("退款取消");
                break;
        }
    }

    class ViewHolder {
        TextView order_number_type,
                order_number_text,
                refundment_status,
                refundment_sum_cost,
                price;
    }

    class SingleProductHolder extends ViewHolder {
        TextView item_name, item_sum,text_comboInfo, item_price;
        NetworkImageView item_icon;
    }

    class PakageProductHolder extends ViewHolder {
        MListView listView;
    }

}
