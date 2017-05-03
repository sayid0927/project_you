package com.zxly.o2o.fragment;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.TypedValue;

import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.widget.viewpagerindicator.PagerSlidingTabStrip;
import com.easemob.easeui.widget.viewpagerindicator.ViewPageFragmentAdapter;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.view.FixedViewPager;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseViewPageFragment extends BaseFragment {

	protected PagerSlidingTabStrip tabs;
	protected FixedViewPager pager;
	private boolean isShouldExpand = true;
	private int dividerColor = com.easemob.chatuidemo.R.color.ease_grey;
	private int underlineHeight = 1;
	private float indicatorHeight = 2.5f;
	private int tabTextSize = 15;
	private String selectedTextColor = "#dd2727";
	private String indicatorColor = "#dd2727";
	private int tabBackground = com.easemob.chatuidemo.R.drawable.background_tab;
	protected List<Fragment> fragments = new ArrayList<Fragment>();
	@Override
	protected void initView() {
		pager = (FixedViewPager) findViewById(viewPagerId());
		tabs = (PagerSlidingTabStrip) findViewById(tabsId());
		pager.setAdapter(getAdapter());
		tabs.setViewPager(pager);

	}


	protected abstract String[] tabName();

	protected  FragmentPagerAdapter getAdapter(){
		return new ViewPageFragmentAdapter(this.getChildFragmentManager(), fragments(), tabName());
	}

	protected int viewPagerId() {
		return R.id.pager;
	}
	protected abstract List<Fragment> fragments();

	protected int tabsId() {
		return R.id.tabs;
	}

	@Override
	protected int layoutId() {

		return R.layout.viewpage_container;
	}
	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	public void setTabsValue(PagerSlidingTabStrip tabs) {
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
	public void setSelectedTextColor(String selectedTextColor) {
		this.selectedTextColor = selectedTextColor;
	}
	public void setIndicatorColor(String indicatorColor) {
		this.indicatorColor = indicatorColor;
	}
}
