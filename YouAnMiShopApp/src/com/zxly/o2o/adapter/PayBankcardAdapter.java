package com.zxly.o2o.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.UserBankCard;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;

public class PayBankcardAdapter extends ObjectAdapter {
    private int selectedPosition = -1;
    private boolean isSelectedShow = false;
    private boolean isFogShow = false;

    public PayBankcardAdapter(Context context) {
        super(context);
    }

    public void setSelected(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setSelectedShow(boolean isSelectedShow) {
        this.isSelectedShow = isSelectedShow;
    }

    public void setFogShow(boolean isFogShow) {
        this.isFogShow = isFogShow;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflateConvertView();
            holder = new ViewHolder();
            holder.txtBankName = (TextView) convertView
                    .findViewById(R.id.txt_bank_name);
            holder.txtBankcardNumber = (TextView) convertView.findViewById(R.id.txt_bankcard_number);
            holder.txtBankcardType = (TextView) convertView
                    .findViewById(R.id.txt_card_type);
            holder.imgBankLogo = (NetworkImageView) convertView
                    .findViewById(R.id.img_bank_logo);
            holder.headLine = convertView
                    .findViewById(R.id.head_line);
            holder.imgRightArrow = (ImageView) convertView
                    .findViewById(R.id.img_right_arrow);
            holder.viewFog = convertView.findViewById(R.id.view_fog);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserBankCard userBankCard = (UserBankCard) getItem(position);
        if (0 == position) {
            ViewUtils.setGone(holder.headLine);
        } else {
            ViewUtils.setVisible(holder.headLine);
        }
        holder.imgBankLogo.setDefaultImageResId(R.drawable.img_shortcut_pay);
        holder.imgBankLogo.setImageUrl(userBankCard.getImageBanner(),
                AppController.imageLoader);
        ViewUtils.setText(holder.txtBankName, userBankCard.getBankName());
        String bankNumber = userBankCard.getBankNumber();
        ViewUtils.setText(holder.txtBankcardNumber, StringUtil.getHiddenString(bankNumber));
        if (2 == userBankCard.getBankType()) {
//            holder.imgCardType.setBackgroundResource(R.drawable.img_pay_bankcard_savings);
            ViewUtils.setText(holder.txtBankcardType, "储蓄卡");
        } else if (3 == userBankCard.getBankType()) {
//            holder.imgCardType.setBackgroundResource(R.drawable.img_pay_bankcard_credit);
            ViewUtils.setText(holder.txtBankcardType, "信用卡");
        }
        ViewUtils.setGone(holder.imgRightArrow);

        if (isSelectedShow) {
            ViewUtils.setVisible(holder.imgRightArrow);
            if (position == selectedPosition) {
                holder.imgRightArrow.setBackgroundResource(R.drawable.checkbox_press);
            } else {
                holder.imgRightArrow.setBackgroundResource(R.drawable.checkbox_normal);
            }
        } else {
            ViewUtils.setGone(holder.imgRightArrow);
        }
        if (isFogShow) {
            if (1 == userBankCard.getWithdrawType()) {
                ViewUtils.setGone(holder.viewFog);
            } else {
                ViewUtils.setVisible(holder.viewFog);
            }
        } else {
            ViewUtils.setGone(holder.viewFog);
        }
        return convertView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_pay_bankcard;
    }

    class ViewHolder {
        View headLine;
        NetworkImageView imgBankLogo;
        TextView txtBankName;
        TextView txtBankcardNumber;
        //        ImageView imgCardType;
        TextView txtBankcardType;
        ImageView imgRightArrow;
        View viewFog;
    }

}
