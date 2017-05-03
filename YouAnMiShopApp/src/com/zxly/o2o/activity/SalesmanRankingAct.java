package com.zxly.o2o.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.easemob.easeui.widget.viewpagerindicator.ViewPageFragmentAdapter;
import com.zxly.o2o.fragment.SalesmanRankingListFragment;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchenhui on 2015/5/25.
 */
public class SalesmanRankingAct extends BasicAct implements View.OnClickListener {

    public static final int PROMOTION_TYPE_USER=0;
    public static final int PROMOTION_TYPE_ARTICLE=1;
    public static final int PROMOTION_TYPE_PRODUCT=2;
    List<Fragment> fragments;
    List<TextView> allTabs;
    ViewPager pager;
    public int curType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.win_salesman_ranking);

        TextView btnUserPromotion= (TextView) findViewById(R.id.btn_user_promotion);
        TextView btnArticlePromotion= (TextView) findViewById(R.id.btn_article_promotion);
        TextView btnProductPromotion= (TextView) findViewById(R.id.btn_product_promotion);

        btnArticlePromotion.setOnClickListener(this);
        btnProductPromotion.setOnClickListener(this);
        btnUserPromotion.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

        allTabs=new ArrayList<TextView>();
        allTabs.add(btnUserPromotion);
        allTabs.add(btnArticlePromotion);
        allTabs.add(btnProductPromotion);

        pager = (ViewPager) findViewById(R.id.pager);

        fragments = new ArrayList<Fragment>();
        fragments.add(SalesmanRankingListFragment.newInstance(PROMOTION_TYPE_USER));
        fragments.add(SalesmanRankingListFragment.newInstance(PROMOTION_TYPE_ARTICLE));
        fragments.add(SalesmanRankingListFragment.newInstance(PROMOTION_TYPE_PRODUCT));


        curType=PROMOTION_TYPE_USER;
        String strings[] = {"会员推广", "文章推广","商品推广"};
        pager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), fragments, strings));


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setCurTab(allTabs,i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setCurTab(List<TextView> allTabs,int curPos){

        if(curType==curPos){
            return;
        }


        curType=curPos;
        pager.setCurrentItem(curPos);
        ((SalesmanRankingListFragment)fragments.get(curPos)).refreshData();

        for (int i=0;i<allTabs.size();i++){
            if(i==curPos){
                allTabs.get(i).setTextColor(getResources().getColor(R.color.orange_ff5f19));
                allTabs.get(i).setBackgroundResource(R.drawable.bg_tap);
            }else {
                allTabs.get(i).setTextColor(getResources().getColor(R.color.white));
                allTabs.get(i).setBackgroundResource(R.color.transparent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_back:
                finish();
                break;


            case R.id.btn_user_promotion:
                setCurTab(allTabs, PROMOTION_TYPE_USER);
                break;

            case R.id.btn_article_promotion:
                setCurTab(allTabs, PROMOTION_TYPE_ARTICLE);
                break;

            case R.id.btn_product_promotion:
                setCurTab(allTabs,PROMOTION_TYPE_PRODUCT);
                break;



            default:
                break;
        }

    }


    public static void start(Activity curAct){
        Intent it=new Intent();
        it.setClass(curAct, SalesmanRankingAct.class);
        ViewUtils.startActivity(it, curAct);
    }

}
