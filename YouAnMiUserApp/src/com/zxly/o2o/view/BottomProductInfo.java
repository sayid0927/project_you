package com.zxly.o2o.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ListView;

import com.easemob.easeui.widget.MyWebView;
import com.easemob.easeui.widget.viewpagerindicator.PagerSlidingTabStrip;
import com.easemob.easeui.widget.viewpagerindicator.ViewPageFragmentAdapter;
import com.zxly.o2o.SnapScrollView.BottomPage;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.fragment.ProductDescFragment;
import com.zxly.o2o.fragment.ProductParameFragment;
import com.zxly.o2o.model.ProductParam;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2016/1/13.
 */
public class BottomProductInfo extends BottomPage {
    protected PagerSlidingTabStrip tabs;
    protected ViewPager pager;
    protected List<Fragment> fragments = new ArrayList<Fragment>();

    private boolean isShouldExpand = true;
    private int dividerColor = R.color.grey;
    private int underlineHeight = 1;
    private float indicatorHeight = 2.5f;
    private int tabTextSize = 17;
    private String selectedTextColor = "#FF5F19";
    private String indicatorColor = "#FF5F19";
    private int tabBackground = R.drawable.background_tab;
    private static String htmlContent;
    private static List<ProductParam> productParamList = new ArrayList<ProductParam>();
    private ProductDescFragment productDescFragment;
    private ProductParameFragment productParameFragment;
    public BottomProductInfo(Context context,String htmlContent,List<ProductParam> productParamList) {
        super(context);
        this.htmlContent=htmlContent;
        this.productParamList=productParamList;
        init();
    }
    private void init()
    {
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById( R.id.tabs);
        productDescFragment=new ProductDescFragment(htmlContent);
        productParameFragment=new ProductParameFragment(productParamList);
        if(!StringUtil.isNull(this.htmlContent))
        {
            fragments.add(productDescFragment);
            fragments.add(productParameFragment);
            pager.setAdapter(new ViewPageFragmentAdapter(((FragmentActivity) context).getSupportFragmentManager(), fragments, new String[]{"图文详情", "详细参数"}));
        }else
        {
            fragments.add(productParameFragment);
            fragments.add(productDescFragment);
            pager.setAdapter(new ViewPageFragmentAdapter(((FragmentActivity) context).getSupportFragmentManager(), fragments, new String[]{ "详细参数","图文详情"}));
        }

        tabs.setViewPager(pager);
        setTabsValue();

    }
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(isShouldExpand);
        // 设置Tab的分割线颜色
        tabs.setDividerColor(context.getResources().getColor(dividerColor));
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, AppController.displayMetrics));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, AppController.displayMetrics));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, AppController.displayMetrics));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor(indicatorColor));
        tabs.setTextColor(Color.parseColor("#666666"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor(selectedTextColor));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(tabBackground);
        tabs.initTabStyles();
    }

    @Override
    public boolean isAtTop() {
        if(pager.getCurrentItem()==1)
        {
            if( productParameFragment.getParamListView().getFirstVisiblePosition()==0)
            {
                return true;
            }
        }else
        {
           if(productDescFragment.getScrollView().getScrollY()<=0)
           {
               return true;
           }
        }
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.viewpage_container;
    }
}
