package com.zxly.o2o.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;

import com.easemob.easeui.widget.viewpagerindicator.ViewPageFragmentAdapter;
import com.zxly.o2o.fragment.MyOrderListFragment;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MPagerSlidingTab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuchenhui on 2015/5/25.
 */
public class MyOrderAct extends BasicAct{
	public static final int ORDER_REQUEST_ALL = 0;
	public static final int ORDER_REQUEST_WAIT_FOR_SEND = 1;
    public static final int ORDER_REQUEST_SENDED = 2;
	public static final int ORDER_REQUEST_RECEIVED = 3;
    public static final int ORDER_REQUEST_REFUND = 4;
    public static final int ORDER_REQUEST_FINISH = 5;

	/**列表类型 1:订单  2:送货单**/
	public static final int TYPE_ORDER_LIST = 0;
	public static final int TYPE_ORDER_SENDING = 1;
	public static final int TYPE_ORDER_SENDED = 2;

    private MPagerSlidingTab tabs;
    List<Fragment> fragments;
    
    private int curStatus;
    private int curTab;


    private View layoutTips,btnCloseTips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.win_orders);

        layoutTips=findViewById(R.id.layout_tips);
        btnCloseTips=findViewById(R.id.btn_close_tips);

        btnCloseTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutTips.setVisibility(View.GONE);
                PreferUtil.getInstance().getIsFirstOpen();
            }
        });

        if(PreferUtil.getInstance().getIsFirstOpen()){
           layoutTips.setVisibility(View.VISIBLE);
        }


        initTitle("我的订单", this);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        tabs = (MPagerSlidingTab) findViewById(R.id.tabs);
        fragments = new ArrayList<Fragment>();
        fragments.add(MyOrderListFragment.newInstance(TYPE_ORDER_LIST,ORDER_REQUEST_ALL));
        fragments.add(MyOrderListFragment.newInstance(TYPE_ORDER_LIST,ORDER_REQUEST_WAIT_FOR_SEND));
        fragments.add(MyOrderListFragment.newInstance(TYPE_ORDER_LIST,ORDER_REQUEST_SENDED));
        fragments.add(MyOrderListFragment.newInstance(TYPE_ORDER_LIST,ORDER_REQUEST_RECEIVED));
        fragments.add(MyOrderListFragment.newInstance(TYPE_ORDER_LIST, ORDER_REQUEST_REFUND));
        fragments.add(MyOrderListFragment.newInstance(TYPE_ORDER_LIST,ORDER_REQUEST_FINISH));

        Intent it=getIntent();
        if(it!=null){
        	curStatus=it.getIntExtra("status", 0);
        }

        switch (curStatus) {
			case ORDER_REQUEST_ALL :
				curTab=0;
				break;
			case ORDER_REQUEST_WAIT_FOR_SEND :
				curTab=1;
				break;		
			case ORDER_REQUEST_SENDED :
				curTab=2;
				break;				
			case ORDER_REQUEST_RECEIVED :
				curTab=3;
				break;
            case ORDER_REQUEST_REFUND:
                curTab=4;
                break;
            case ORDER_REQUEST_FINISH :
                curTab=5;
                break;
			default :
				break;
		}
               
        String strings[] = {"全部", "待发货", "待收货","已收货","退款","结束"};
        pager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), fragments, strings));
        tabs.setViewPager(pager);
        setTabsValue();
        pager.setCurrentItem(curTab);
        UmengUtil.onEvent(MyOrderAct.this,new UmengUtil().ORDER_ENTER,null);
    }



    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
//	/	tabs.setShouldExpand(true);
        // 设置Tab的分割线颜色
        tabs.setDividerColor(getResources().getColor(R.color.white));
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
//        // 设置Tab Indicator的高度
        tabs.setTextColor(Color.parseColor("#333333"));
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, getResources().getDisplayMetrics()));
//        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#ff5f19"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#ff5f19"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(R.color.transparent);
        tabs.initTabStyles();
    }


    private void initTitle(String titleName, final Activity activity){
        View backBtn=findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();

                UmengUtil.onEvent(MyOrderAct.this,new UmengUtil().ORDER_BACK_CLICK,null);
            }
        });
    }
    
    public static void startMyorderAct(Activity curAct,int status){
    	Intent it=new Intent();
    	it.setClass(curAct, MyOrderAct.class);
    	it.putExtra("status",status);
    	ViewUtils.startActivity(it, curAct);
    }

}
