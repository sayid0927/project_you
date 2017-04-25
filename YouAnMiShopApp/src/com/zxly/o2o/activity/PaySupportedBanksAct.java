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
import com.zxly.o2o.application.Config;
import com.zxly.o2o.fragment.ApplyBanksFragment;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-5-22
 * @description 支付银行界面
 */
public class PaySupportedBanksAct extends BasicAct implements View.OnClickListener, ParameCallBack{
	private PagerSlidingTabStrip tabs;
	private List<Fragment> fragments;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_pay_supported_bank);
		type = getIntent().getIntExtra("type", -1);
		initViews();
	}

	public static void start(Activity curAct, int type) {
		Intent intent = new Intent(curAct, PaySupportedBanksAct.class);
		intent.putExtra("type", type);
		ViewUtils.startActivity(intent, curAct);
	}

	private void initViews(){
		findViewById(R.id.btn_back).setOnClickListener(this);
		((TextView) findViewById(R.id.txt_title)).setText("支持银行");
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		fragments = new ArrayList<Fragment>();
		if(3 == type){
			fragments.add(new ApplyBanksFragment());
			String strings[] = { "储蓄卡" };
			((ApplyBanksFragment)fragments.get(0)).init(ApplyBanksFragment.BANK_SAVINGS_CARD, type);
			pager.setAdapter(new ViewPageFragmentAdapter(
					getSupportFragmentManager(), fragments, strings));
		} else {
			fragments.add(new ApplyBanksFragment());
			fragments.add(new ApplyBanksFragment());
			String strings[] = { "储蓄卡", "信用卡" };
			((ApplyBanksFragment)fragments.get(0)).init(ApplyBanksFragment.BANK_SAVINGS_CARD, type);
			pager.setAdapter(new ViewPageFragmentAdapter(
					getSupportFragmentManager(), fragments, strings));
		}
		tabs.setViewPager(pager);
		setTabsValue();
		tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i1) {
			}

			@Override
			public void onPageSelected(int i) {
				switch (i) {
				case 0:
					((ApplyBanksFragment) fragments.get(i))
							.init(ApplyBanksFragment.BANK_SAVINGS_CARD, type);
					break;
				case 1:
					((ApplyBanksFragment) fragments.get(i))
							.init(ApplyBanksFragment.BANK_CREDIT_CARD, type);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int i) {
			}
		});
	}

    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线颜色
        tabs.setDividerColor(getResources().getColor(R.color.grey));
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, Config.displayMetrics));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, Config.displayMetrics));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, Config.displayMetrics));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#ff5f19"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#ff5f19"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(R.drawable.background_tab);
        tabs.initTabStyles();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onCall(Object object) {
    }

}
