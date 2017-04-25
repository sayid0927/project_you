package com.zxly.o2o.activity;

import android.content.Intent;

import com.zxly.o2o.fragment.RefundmentListFragment;
import com.zxly.o2o.model.RefundmentDetail;

public class RefundmentListActivity extends BaseViewPageAct {
    private RefundmentListFragment fragment1, fragment2, fragment3;

    public RefundmentDetail clickItem=new RefundmentDetail();

    @Override
    protected void initView() {
        setUpActionBar("退款列表");
        fragment1 = new RefundmentListFragment();
        fragment1.setFragmentPage(1);
        fragment2 = new RefundmentListFragment();
        fragment2.setFragmentPage(2);
        fragment3 = new RefundmentListFragment();
        fragment3.setFragmentPage(3);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        pager.setOffscreenPageLimit(3);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        long ismodify = intent.getLongExtra("ismodify", -1);
        int isCancelApply = intent.getIntExtra("type", -1);

        if (clickItem.getStatus() == 6 && isCancelApply == 1) {
            return;   //本身是已经取消的状态的话，回来这里不需要刷新fragment
        }
        fragment1.doUpdate(clickItem, ismodify, isCancelApply);
        fragment2.doUpdate(clickItem, ismodify, isCancelApply);
        fragment3.doUpdate(clickItem, ismodify, isCancelApply);
    }

    @Override
    protected String[] tabName() {
        return new String[]{"全部", "退款中", "已结束"};
    }
}