package com.zxly.o2o.activity;


import com.zxly.o2o.fragment.MyCommisionFragment;

/**
 * Created by Benjamin on 2015/6/5.
 */
public class MyCommisionListAct extends BaseViewPageAct {

    @Override
    protected void initView() {
        setUpActionBar("佣金列表");


        for (int i = 1; i < 3; i++) {
            fragments.add(MyCommisionFragment.newInstance(i));
        }
    }

    @Override
    protected String[] tabName() {
        return new String[]{"全部", "进行中"};
    }
}