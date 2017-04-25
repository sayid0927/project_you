package com.zxly.o2o.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.easemob.easeui.widget.viewpagerindicator.PagerSlidingTabStrip;
import com.easemob.easeui.widget.viewpagerindicator.ViewPageFragmentAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.fragment.MyOrderListFragment;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchenhui on 2015/5/25.
 */
public class MyOrderAct extends BasicAct {

	private PagerSlidingTabStrip tabs;
    List<Fragment> fragments;
    public int curStatus;
    public int initTab;
    public static ParameCallBack callBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_myorder);

        Intent it=getIntent();

        if(it!=null){
            curStatus=it.getIntExtra("status",curStatus);
        }else {
            curStatus=Constants.ORDER_REQUEST_ALL;
        }

        initTitle("我的订单", this);

        fragments = new ArrayList<Fragment>();
        fragments.add(MyOrderListFragment.newInstance(Constants.ORDER_REQUEST_ALL));
        fragments.add(MyOrderListFragment.newInstance(Constants.ORDER_REQUEST_WAIT_ROR_PAY));
        fragments.add(MyOrderListFragment.newInstance(Constants.ORDER_REQUEST_WAIT_ROR_TAKE));
        fragments.add(MyOrderListFragment.newInstance(Constants.ORDER_REQUEST_SUCCESS));
        fragments.add(MyOrderListFragment.newInstance(Constants.ORDER_REQUEST_FINISH));
        String strings[] = {"全部", "待付款", "待发货","待收货", "已结束"};


        switch (curStatus) {
            case Constants.ORDER_REQUEST_ALL :
                initTab =0;
                break;
            case Constants.ORDER_REQUEST_WAIT_ROR_PAY :
                initTab =1;
                break;
            case Constants.ORDER_REQUEST_WAIT_ROR_TAKE :
                initTab =2;
                break;
            case Constants.ORDER_REQUEST_SUCCESS :
                initTab =3;
                break;
            case Constants.ORDER_REQUEST_FINISH :
                initTab =4;
                break;

            default :
                break;
        }

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        //((FixedViewPager)pager).setTouchIntercept(false);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), fragments, strings));
        tabs.setViewPager(pager);
        setTabsValue();
        pager.setCurrentItem(initTab);

//        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//setCurrentTab(i);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//            }
//        });


    }


//    private void setCurrentTab(int pos){
//        ((MyOrderListFragment)fragments.get(pos)).updateData();
//    }

    /**
     * 每当从别的界面回来都必须刷一下当前界面（不用回调 是应为可能有的退款中，待确认等定时任务的状态也可能会在同一时间段发生改变）
     */
    @Override
    protected void onResume() {
        super.onResume();
        //((MyOrderListFragment)fragments.get(initTab)).updateData();
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线颜色
        tabs.setDividerColor(getResources().getColor(R.color.grey));
        // 设置Tab底部线的高度
       tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, Config.displayMetrics));
//        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, Config.displayMetrics));
//        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, Config.displayMetrics));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#ff5f19"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#ff5f19"));
        // 取消点击Tab时的背景色
      //  tabs.setTabBackground(R.drawable.background_tab);
        tabs.initTabStyles();
    }
    
    
    private void initTitle(String titleName, final Activity activity){
        View backBtn=findViewById(R.id.tag_title_btn_back);
        TextView title= (TextView) findViewById(R.id.tag_title_title_name);
        title.setText(titleName);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public static void start(int curStatus,Activity curAct){
        Intent it=new Intent();
        it.setClass(curAct, MyOrderAct.class);
        it.putExtra("status",curStatus);
        ViewUtils.startActivity(it, curAct);
    }
    
}
