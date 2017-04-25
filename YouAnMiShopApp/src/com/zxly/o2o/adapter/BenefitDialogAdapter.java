package com.zxly.o2o.adapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;
import com.zxly.o2o.activity.MyBenefitsAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.dialog.BaseDialog;
import com.zxly.o2o.model.BenefitVO;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.ExpandView;

/**
 * Created by Administrator on 2016/6/12.
 */
public class BenefitDialogAdapter extends ObjectAdapter {
    private ExpandView baseDialog;

    public BenefitDialogAdapter(Context context, ExpandView dialog) {
        super(context);
        baseDialog = dialog;

    }


    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        final BenefitVO benefitVO = (BenefitVO) getItem(position);
        if (convertView == null) {
            final TextView textView = new TextView(context);
            textView.setPadding((int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, EaseUI.displayMetrics), 0,
                    (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, EaseUI.displayMetrics), 0);
            textView.setSingleLine();
            textView.setMinWidth((int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, EaseUI.displayMetrics));
            textView.setGravity(Gravity.CENTER);
            GridView.LayoutParams layoutParams = new GridView.LayoutParams((int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, GridView.LayoutParams.WRAP_CONTENT, EaseUI
                            .displayMetrics),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
                            EaseUI.displayMetrics));
            textView.setLayoutParams(layoutParams);


            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            textView.setTextColor(Color.WHITE);
                            textView.setBackgroundResource(R.drawable.xx_sanxuan_press);
                            break;
                        case MotionEvent.ACTION_UP:
                            if (baseDialog != null) {
                                if(baseDialog.isExpand()){
                                    baseDialog.collapse();
                                }
                            }
                            Intent intent = new Intent();
                            intent.setClass(context, MyBenefitsAct.class);
                            intent.putExtra("discountId", benefitVO.getDiscountId());
                            ViewUtils.startActivity(intent, (Activity) context);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            textView.setTextColor(Color.parseColor("#051828"));
                            textView.setBackgroundResource(R.drawable.btn_grey_orange_selector);
                            break;
                    }

                    return true;
                }
            });


            convertView = textView;
        }


            if (benefitVO.isCheck()) {
                convertView.setBackgroundResource(R.drawable.xx_sanxuan_press);
                ((TextView)convertView).setTextColor(Color.WHITE);
            } else {
                ((TextView)convertView).setTextColor(Color.parseColor("#051828"));
                convertView.setBackgroundResource(R.drawable.btn_grey_orange_selector);
            }



//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        ((TextView) convertView).setText(benefitVO.getDiscountInfo());

        return convertView;
    }
}
