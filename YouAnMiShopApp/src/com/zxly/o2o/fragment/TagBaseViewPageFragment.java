package com.zxly.o2o.fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zxly.o2o.adapter.CustomerViewpagerFragmentAdapter;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.view.FixedViewPager;

import java.util.List;

/**
 * 类QQ消息页面  消息和联系人页面切换
 */
public abstract class TagBaseViewPageFragment extends BaseFragment {

    protected FixedViewPager pager;
    private RadioButton btnFans;
    private RadioButton btnMenbers;
    private RadioGroup rg;

    @Override
    protected void initView() {
        pager = (FixedViewPager) findViewById(R.id.pager);
        //粉丝
        btnFans = (RadioButton) findViewById(R.id.btn_fans);
        //会员
        btnMenbers = (RadioButton) findViewById(R.id.btn_menbers);
        rg = (RadioGroup) findViewById(R.id.rg_group_id);
        pager.setAdapter(getAdapter());
        pager.setScanScroll(true);
        //设置viewpager不拦截触摸事件 目的：1 不处理的话侧滑删除控件会被抢夺滑动动作事件
        //2 使viewpager不能通过滑动切换页面  只能点击切换
        pager.setTouchIntercept(false);
        btnFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
            }
        });
        btnMenbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
            }
        });
    }

    protected  FragmentPagerAdapter getAdapter(){
        return new CustomerViewpagerFragmentAdapter(this.getActivity().getSupportFragmentManager(), fragments());
    }

    protected int viewPagerId() {
        return R.id.pager;
    }
    protected abstract List<Fragment> fragments();

    protected int tabsId() {
        return R.id.ease_tl_tabs;
    }

    @Override
    protected int layoutId() {

        return R.layout.tag_viewpager_container;
    }

}
