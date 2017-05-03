package com.zxly.o2o.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import com.easemob.easeui.widget.viewpagerindicator.ViewPageFragmentAdapter;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.view.MPagerSlidingTab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenwu on 2016/4/26.
 */
public class GuaranteeManageFragmnet extends BaseFragment {
    private MPagerSlidingTab tabs;
    List<Fragment> fragments;

    public static GuaranteeManageFragmnet newInstance(){
        GuaranteeManageFragmnet f=new GuaranteeManageFragmnet();
        return f;
    }

    @Override
    protected void initView() {
        fragments = new ArrayList<Fragment>();
        fragments.add(GuaranteeListFragment.newInstance(GuaranteeListFragment.TYPE_AVAILABLE));
        fragments.add(GuaranteeListFragment.newInstance(GuaranteeListFragment.TYPE_NOT_AVAILABLE));

        String strings[] = {"我的单","失效单"};
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        //((FixedViewPager)pager).setTouchIntercept(false);
        tabs = (MPagerSlidingTab) findViewById(R.id.tabs);
        pager.setAdapter(new ViewPageFragmentAdapter(getActivity().getSupportFragmentManager(), fragments, strings));
        tabs.setViewPager(pager);
        setTabsValue();
        pager.setCurrentItem(0);

    }

    @Override
    protected int layoutId() {
        return R.layout.win_guarantee_manage;
    }


    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线颜色
        tabs.setDividerColor(getResources().getColor(R.color.white));
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getActivity().getResources().getDisplayMetrics()));
//        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, getActivity().getResources().getDisplayMetrics()));
//        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getActivity().getResources().getDisplayMetrics()));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#ff5f19"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#ff5f19"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(R.color.transparent);
        tabs.initTabStyles();
    }
}
