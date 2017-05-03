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
import com.zxly.o2o.fragment.MyOrderListFragment;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchenhui on 2015/5/25.
 */
public class ShippingListAct extends BasicAct implements View.OnClickListener {

    private int curType;
    List<TextView> allTabs;
    List<Fragment> fragments;
    ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.win_sending_orders);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pager = (ViewPager) findViewById(R.id.pager);
        fragments = new ArrayList<Fragment>();
        fragments.add(MyOrderListFragment.newInstance(MyOrderAct.TYPE_ORDER_SENDING, MyOrderAct.TYPE_ORDER_SENDING,true));
        fragments.add(MyOrderListFragment.newInstance(MyOrderAct.TYPE_ORDER_SENDED, MyOrderAct.TYPE_ORDER_SENDED,true));

        String strings[] = {"送货中", "已送货"};
        pager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), fragments, strings));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setCurTab(allTabs, i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        allTabs=new ArrayList<TextView>();
        allTabs.add((TextView) findViewById(R.id.label_sending));
        allTabs.add((TextView) findViewById(R.id.label_sended));
        allTabs.get(0).setOnClickListener(this);
        allTabs.get(1).setOnClickListener(this);
        setCurTab(allTabs,curType);
    }


    private void setCurTab(List<TextView> allTabs,int curPos){
        if(curType==curPos){
            return;
        }

        curType=curPos;
        pager.setCurrentItem(curPos);
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

    public static void start(Activity curAct){
    	Intent it=new Intent();
    	it.setClass(curAct, ShippingListAct.class);
    	ViewUtils.startActivity(it, curAct);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.label_sending:
                setCurTab(allTabs,0);
                break;

            case R.id.label_sended:
                setCurTab(allTabs,1);
                break;

            default:
                break;
        }
    }
}
