package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.easemob.easeui.widget.viewpagerindicator.PagerSlidingTabStrip;
import com.easemob.easeui.widget.viewpagerindicator.ViewPageFragmentAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.fragment.AccountRecordFragment;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengrongjian 2015-5-20
 * @description 资金记录界面
 */
public class PayAccountRecordAct extends BasicAct implements View.OnClickListener, ParameCallBack {
    private PagerSlidingTabStrip tabs;
    private List<Fragment> fragments;
    public int initTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_account_record);
        initTab = getIntent().getIntExtra("initTab", initTab);
        initViews();
    }

    /**
     * @param initTab  初始tab页面     0:"全部" 1:"购物" 2:"佣金" 3:"流量" 4:"延保" 5:"提现" 6:"退款"
     */
    public static void start(int initTab, Activity curAct) {
        Intent intent = new Intent(curAct, PayAccountRecordAct.class);
        intent.putExtra("initTab", initTab);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "账单明细");
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        fragments = new ArrayList<Fragment>();
        fragments.add(new AccountRecordFragment());
        fragments.add(new AccountRecordFragment());
        fragments.add(new AccountRecordFragment());
        fragments.add(new AccountRecordFragment());
        fragments.add(new AccountRecordFragment());
        fragments.add(new AccountRecordFragment());
//        fragments.add(new AccountRecordFragment());
//        String strings[] = {"全部", "购物", "佣金", "流量", "延保", "提现", "退款"};
        String strings[] = {"全部", "购物", "佣金", "流量", "延保", "提现"};
        pager.setAdapter(new ViewPageFragmentAdapter(
                getSupportFragmentManager(), fragments, strings));
        tabs.setViewPager(pager);
        setTabsValue();
        pager.setCurrentItem(initTab);
        if (initTab == 0) {
            ((AccountRecordFragment) fragments.get(0)).init(AccountRecordFragment.ACCOUNT_RECORD_ALL);
        } else if (initTab == 5) {
            ((AccountRecordFragment) fragments.get(5)).init(AccountRecordFragment.ACCOUNT_RECORD_TAKEOUT);
        }
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        ((AccountRecordFragment) fragments.get(i))
                                .init(AccountRecordFragment.ACCOUNT_RECORD_ALL);
                        break;
                    case 1:
                        ((AccountRecordFragment) fragments.get(i))
                                .init(AccountRecordFragment.ACCOUNT_RECORD_SHOPPING);
                        break;
                    case 2:
                        ((AccountRecordFragment) fragments.get(i))
                                .init(AccountRecordFragment.ACCOUNT_RECORD_COMMISSION);
                        break;
                    case 3:
                        ((AccountRecordFragment) fragments.get(i))
                                .init(AccountRecordFragment.ACCOUNT_RECORD_FLOW);
                        break;
                    case 4:
                        ((AccountRecordFragment) fragments.get(i))
                                .init(AccountRecordFragment.ACCOUNT_RECORD_INSURANCE);
                        break;
                    case 5:
                        ((AccountRecordFragment) fragments.get(i))
                                .init(AccountRecordFragment.ACCOUNT_RECORD_TAKEOUT);
                        break;
//                    case 6:
//                        ((AccountRecordFragment) fragments.get(i))
//                                .init(AccountRecordFragment.ACCOUNT_RECORD_REFUND);
//                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
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
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, Config.displayMetrics));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, Config.displayMetrics));
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
    public void onCall(Object object) {
        ((AccountRecordFragment) fragments.get(0)).init(AccountRecordFragment.ACCOUNT_RECORD_ALL);
    }

}
