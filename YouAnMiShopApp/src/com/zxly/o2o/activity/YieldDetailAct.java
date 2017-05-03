package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.easemob.easeui.ui.EaseBaseViewPageAct;
import com.zxly.o2o.fragment.YieldDetailFragment;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by dsnx on 2015/12/15.
 */
public class YieldDetailAct extends EaseBaseViewPageAct {
    private View btnBack;
    @Override
    protected void initView() {
        btnBack=findViewById(R.id.btn_back);
        this.setSelectedTextColor("#ff5f19");
        this.setIndicatorColor("#ff5f19");
        fragments.add(YieldDetailFragment.getInstance(0));
        fragments.add(YieldDetailFragment.getInstance(1));
        fragments.add(YieldDetailFragment.getInstance(2));
        fragments.add(YieldDetailFragment.getInstance(4));
        fragments.add(YieldDetailFragment.getInstance(3));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                UmengUtil.onEvent(YieldDetailAct.this,new UmengUtil().INCOME_BACK_CLICK,null);
            }
        });
    }

    @Override
    protected String[] tabName() {
        return new String[]{"全部","商品","流量","延保","应用 "};
    }

    @Override
    protected int tabsId() {
        return R.id.tabs;
    }

    @Override
    protected int layoutId() {
        return R.layout.win_earnings_detail;
    }
    public static  void start(Activity curAct)
    {
        Intent intent=new Intent();
        intent.setClass(curAct, YieldDetailAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

}
