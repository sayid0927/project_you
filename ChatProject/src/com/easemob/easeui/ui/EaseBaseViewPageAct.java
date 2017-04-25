package com.easemob.easeui.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.ViewConfiguration;
import android.view.Window;

import com.easemob.chatuidemo.R;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.widget.viewpagerindicator.PagerSlidingTabStrip;
import com.easemob.easeui.widget.viewpagerindicator.ViewPageFragmentAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 2015/6/9.
 */
public abstract class EaseBaseViewPageAct extends EaseBaseActivity {
    protected PagerSlidingTabStrip tabs;
    protected ViewPager pager;
    protected List<Fragment> fragments = new ArrayList<Fragment>();

    private boolean isShouldExpand = true;
    private int dividerColor = R.color.ease_grey;
    private int underlineHeight = 1;
    private float indicatorHeight = 2.5f;
    private int tabTextSize = 15;
    private String selectedTextColor = "#dd2727";
    private String indicatorColor = "#dd2727";
    private int tabBackground = R.drawable.background_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        //        setOverflowShowingAlways();
        pager = (ViewPager) findViewById(viewPagerId());
        tabs = (PagerSlidingTabStrip) findViewById(tabsId());
        initView();
        pager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), fragments, tabName()));
        tabs.setViewPager(pager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTabsValue();
    }

    protected abstract void initView();

    protected abstract String[] tabName();

    protected int viewPagerId() {
        return R.id.pager;
    }

    protected int tabsId() {
        return R.id.ease_tl_tabs;
    }

    protected int layoutId() {
        return R.layout.ease_viewpage_container;
    }

    protected void goIndex(int index) {
        pager.setCurrentItem(index);
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(isShouldExpand);
        // 设置Tab的分割线颜色
        tabs.setDividerColor(getResources().getColor(dividerColor));
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, EaseUI.displayMetrics));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, EaseUI.displayMetrics));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, EaseUI.displayMetrics));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor(indicatorColor));
        tabs.setTextColor(Color.parseColor("#333333"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor(selectedTextColor));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(tabBackground);
        tabs.initTabStyles();
    }

    public void setIsShouldExpand(boolean isShouldExpand) {
        this.isShouldExpand = isShouldExpand;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public void setUnderlineHeight(int underlineHeight) {
        this.underlineHeight = underlineHeight;
    }

    public void setIndicatorHeight(float indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
    }

    public void setTabTextSize(int tabTextSize) {
        this.tabTextSize = tabTextSize;
    }

    public void setSelectedTextColor(String selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public void setIndicatorColor(String indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public void setTabBackground(int tabBackground) {
        this.tabBackground = tabBackground;
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //        		getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }


}