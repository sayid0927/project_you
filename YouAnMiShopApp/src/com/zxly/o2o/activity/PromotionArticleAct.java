package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.easemob.easeui.ui.EaseBaseViewPageAct;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.fragment.LocalArticleFragement;
import com.zxly.o2o.fragment.PromotionAcitcityFragment;
import com.zxly.o2o.fragment.PromotionArticleFragment;
import com.zxly.o2o.fragment.StoreArticleFragement;
import com.zxly.o2o.fragment.TerraceArticleFragement;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.FixedViewPager;

/**
 * Created by dsnx on 2016/9/1.
 */
public class PromotionArticleAct extends EaseBaseViewPageAct implements View.OnClickListener {
    private View btnBack,btnQrCode;
    private TextView txtTitle;
    private String[] tabName;
    @Override
    protected void initView() {
        AppController.addAct(this);
        ((FixedViewPager) pager).setTouchIntercept(false);
        btnBack=findViewById(R.id.btn_back);
        txtTitle= (TextView) findViewById(R.id.txt_title);
        btnQrCode=findViewById(R.id.btn_qrCode);
        this.setSelectedTextColor("#ff5f19");
        this.setIndicatorColor("#ff5f19");
        tabs.setIndicatorColor(Color.parseColor("#626262"));
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int tabPaddingLeft = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, dm);
       int tabPaddingRight = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, dm);
        tabs.setTabPaddingLeft(tabPaddingLeft);
        tabs.setTabPaddingRight(tabPaddingRight);
        int promotionType=getIntent().getIntExtra("promotionType",1);
        StoreArticleFragement storeArticleFragement = StoreArticleFragement.newInstance(1);
         LocalArticleFragement localArticleFragement = LocalArticleFragement.newInstance(1);
        fragments.add(storeArticleFragement);
        fragments.add(localArticleFragement);
        storeArticleFragement.setParam(callBack);
        fragments.add(new TerraceArticleFragement());
        fragments.add(PromotionArticleFragment.newInstance());
        if(promotionType==1)//包含活动
        {
            fragments.add(PromotionAcitcityFragment.newInstance());
            tabName=new String[]{"店铺文章","本地热文","网络热文","自定义文章","活动"};
//            tabName=new String[]{"店铺文章","网络热文","自定义文章","活动"};
            ViewUtils.setText(txtTitle,"找客");
        }else
        {
            ViewUtils.setGone(btnQrCode);
           tabName=new String[]{"店铺文章","本地热文","网络热文","自定义文章"};
//            tabName=new String[]{"店铺文章","网络热文","自定义文章"};
            ViewUtils.setText(txtTitle,"找客");
        }
        btnBack.setOnClickListener(this);
        btnQrCode.setOnClickListener(this);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.cancelAll(this);
        AppController.remove(this);
    }
    @Override
    protected String[] tabName() {
        return tabName;
    }
    @Override
    protected int tabsId() {
        return R.id.tabs;
    }

    @Override
    protected int layoutId() {
        return R.layout.win_promotion_article;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_back:
                finish();

                UmengUtil.onEvent(PromotionArticleAct.this,new UmengUtil().FIND_BACK_CLICK,null);

                break;
            case R.id.btn_qrCode:
                QrCodePromotionAct.start(this);

                break;
        }
    }

    /**
     *
     * @param curAct
     * @param promotionType 1:包含活动. 2:不包含活动
     *
     */
    public static  void start(Activity curAct,int promotionType)
    {
        Intent intent=new Intent();
        intent.putExtra("promotionType",promotionType);
        intent.setClass(curAct, PromotionArticleAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    CallBack callBack=new CallBack() {
        @Override
        public void onCall() {
            pager.setCurrentItem(1);
        }
    };
}
