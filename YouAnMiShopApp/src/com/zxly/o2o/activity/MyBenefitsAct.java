package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.easeui.controller.EaseUI;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.TabEntity;
import com.flyco.tablayout.utils.ViewFindUtils;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.BenefitDialogAdapter;
import com.zxly.o2o.fragment.MyBenefitsFragment;
import com.zxly.o2o.model.BenefitVO;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.BenefitsGetSortRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.ExpandView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/12.
 */
public class MyBenefitsAct extends BasicAct {
    private String[] titles;

    private ArrayList<CustomTabEntity> tabs = new ArrayList<CustomTabEntity>();
    private View decorView;
    private CommonTabLayout tls;
    protected ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private BenefitsGetSortRequest benefitsGetSortRequest;
    private MyBenefitsFragment myValidBenefitsFragment, myInvalidBenefitsFragment;
    private TextView tvSort;
    private GridView gvPresent, gvDeduction;
    private BenefitDialogAdapter adPresent, adDeduction;
    private ExpandView expandView;
    private int curTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.benefits_activity);

        expandView = (ExpandView) findViewById(R.id.view_expand);

        addBackBtn();
        if(getIntent().getBooleanExtra("showRightBtn", true)){
            addRightBtn();
        }

        myValidBenefitsFragment = new MyBenefitsFragment();
        myInvalidBenefitsFragment = new MyBenefitsFragment();

        long discountId = getIntent().getLongExtra("discountId", 0);
        curTab=getIntent().getIntExtra("curTab",0);
        myValidBenefitsFragment.setDiscountId(discountId);
        myInvalidBenefitsFragment.setDiscountId(discountId);
        //Byte 1.领取记录 2.使用记录
        Bundle bundle1 = new Bundle();
        bundle1.putByte("type", (byte) 1);
        myValidBenefitsFragment.setArguments(bundle1);
        fragments.add(myValidBenefitsFragment);

        Bundle bundle2 = new Bundle();
        bundle2.putByte("type", (byte) 2);
        myInvalidBenefitsFragment.setArguments(bundle2);
        fragments.add(myInvalidBenefitsFragment);

        titles = new String[]{"未使用", "已使用"};

        for (String title : titles) {
            tabs.add(new TabEntity(title, 0, 0));
        }

        decorView = getWindow().getDecorView();
        /** indicator圆角色块 */
        tls = ViewFindUtils.find(decorView, R.id.ease_tl_tabs);
        tls.setTabData(tabs, this, R.id.fl_change, fragments);
        tls.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (expandView.isExpand()) {
                    //关闭并清空缓存数据
                    expandView.collapse();
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        if(curTab==1)
        {
            mMainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tls.setCurrentTab(curTab);
                }
            },1000);
        }

    }


    /*添加筛选按钮*/
    private void addRightBtn() {
        tvSort = new TextView(this);
        tvSort.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tvSort.setText("筛选");
        tvSort.setTextColor(Color.WHITE);
        tvSort.setGravity(Gravity.CENTER);
        tvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandView.isExpand()) {
                    expandView.collapse();
                } else {
                    LoadSortData();
                }
            }
        });


        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, FrameLayout.LayoutParams.WRAP_CONTENT, EaseUI
                        .displayMetrics),
                (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                                EaseUI.displayMetrics));
        layoutParams.gravity = Gravity.RIGHT;
        layoutParams.setMargins(0, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                        EaseUI.displayMetrics), 0);
        addContentView(tvSort, layoutParams);
    }


    /*添加返回按钮*/
    private void addBackBtn() {
        ImageView backBtn = new ImageView(this);
        backBtn.setImageResource(R.drawable.back_white);
        backBtn.setScaleType(ImageView.ScaleType.CENTER);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBenefitsAct.this.finish();
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, EaseUI.displayMetrics),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 88,
                        EaseUI.displayMetrics));
        addContentView(backBtn, layoutParams);
    }

    /*初始化dialog并打开*/
    private void initDialog() {
        // 一个自定义的布局，作为显示的内容

        View contentView = getLayoutInflater().inflate(
                R.layout.bennefits_sort_dialog, null);

        contentView.findViewById(R.id.bg_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandView.isExpand()) {
                    expandView.collapse();
                }
            }
        });

        gvDeduction = (GridView) contentView.findViewById(R.id.gv_deductions);
        gvPresent = (GridView) contentView.findViewById(R.id.gv_presents);

        adPresent = new BenefitDialogAdapter(this, expandView);
        adDeduction = new BenefitDialogAdapter(this, expandView);

        setBenefitVOs(benefitsGetSortRequest.getBenefitVOs(), getDiscountId());

        gvDeduction.setAdapter(adDeduction);
        gvPresent.setAdapter(adPresent);

        contentView.findViewById(R.id.btn_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandView.isExpand()) {
                    expandView.collapse();
                }

                Intent intent = new Intent();
                intent.setClass(MyBenefitsAct.this, MyBenefitsAct.class);
                intent.putExtra("discountId", 0);
                ViewUtils.startActivity(intent, MyBenefitsAct.this);
            }
        });

        //填充布局并显示
        expandView.setContentView(contentView);
        expandView.expand();

    }


    public static  void start(Activity curAct,long discountId,int curTab, boolean showRightBtn)
    {
        Intent intent = new Intent();
        intent.setClass(curAct, MyBenefitsAct.class);
        intent.putExtra("discountId", discountId);
        intent.putExtra("curTab", curTab);
        intent.putExtra("showRightBtn", showRightBtn);
        ViewUtils.startActivity(intent, curAct);
    }
    public static  void start(Activity curAct,long discountId, boolean showRightBtn)
    {
        start(curAct,discountId,0, showRightBtn);
    }

    /*加载筛选数据*/
    private void LoadSortData() {
        if (Account.user != null) {
            benefitsGetSortRequest = new BenefitsGetSortRequest(Account.user.getShopId
                    ());
            benefitsGetSortRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    initDialog();
                }

                @Override
                public void onFail(int code) {

                }
            });
            benefitsGetSortRequest.start();
        } else {
            ViewUtils.showToast("请重新登陆");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            long discountId = intent.getLongExtra("discountId", 0);

            if (tls.getCurrentTab() == 0) {
                myValidBenefitsFragment.reLoad(discountId);
            } else {
                myInvalidBenefitsFragment.reLoad(discountId);
            }
        }
    }

    /*获取对应的discountId*/
    private long getDiscountId() {
        if (tls.getCurrentTab() == 0) {
            return myValidBenefitsFragment.getDiscountId();
        } else {
            return myInvalidBenefitsFragment.getDiscountId();
        }
    }


    /*填充筛选数据*/
    public void setBenefitVOs(List<BenefitVO> benefitVOs, long discountId) {
        adDeduction.clear();
        adPresent.clear();
        if (benefitVOs != null && benefitVOs
                .size() > 0) {
            for (BenefitVO benfitVO : benefitVOs) {

                //设置是不是选中状态
                if (benfitVO.getDiscountId() == discountId) {
                    benfitVO.setCheck(true);
                }

                //根据不同类型添加到对应的适配器里
                if (benfitVO.getDiscountType() == 1) {
                    adDeduction.addItem(benfitVO);
                } else if (benfitVO.getDiscountType() == 2) {
                    adPresent.addItem(benfitVO);
                }
            }

        }
    }

}
