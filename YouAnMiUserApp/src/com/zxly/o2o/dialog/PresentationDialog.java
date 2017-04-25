package com.zxly.o2o.dialog;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;

/**
 * Created by Administrator on 2016/7/9.
 */
public class PresentationDialog extends BaseDialog implements View.OnClickListener {

    private ImageView close;
    private NetworkImageView iv;
    private TextView pro_title;
    private TextView pro_price;
    private LinearLayout content;

    @Override
    protected void initView() {
        //活动图文关闭图片
        close = (ImageView) findViewById(R.id.close);
        //活动图片
        iv = (NetworkImageView) findViewById(R.id.activity_image);
        //活动标题
        pro_title = (TextView) findViewById(R.id.activity_title);
        //活动价格区间
        pro_price = (TextView) findViewById(R.id.activity_price);
        //
        content = (LinearLayout) findViewById(R.id.content);
        setEvent();

    }

    private void setEvent() {
        close.setOnClickListener(this);
        content.setOnClickListener(this);
    }

    /**
     *
     * @param headUrl 活动图片
     * @param id      活动id
     * @param title   活动标题
     * @param price   价格
     */
    public void show(String headUrl,int id,String title,String price){
        super.show();
        iv.setImageUrl(headUrl, AppController.imageLoader);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_presentation_activity;
    }


    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.close:
            dismiss();

            break;
        case R.id.content:
           //跳转至活动详情页面

            break;


        default:
            break;
    }
    }


}
