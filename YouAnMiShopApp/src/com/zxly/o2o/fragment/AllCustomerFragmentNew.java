package com.zxly.o2o.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.easeui.widget.viewpagerindicator.PagerSlidingTabStrip;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.ChooseGroupPeopleAct;
import com.zxly.o2o.activity.MainActivity;
import com.zxly.o2o.activity.PromotionArticleAct;
import com.zxly.o2o.activity.SeachPeopleFilterFirstAct;
import com.zxly.o2o.activity.YamCollegeDetailAct;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetFansAndMenberCountRequest;
import com.zxly.o2o.request.ShopTagRequest;
import com.zxly.o2o.request.YamLessonRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CollegeCourseView;
import com.zxly.o2o.view.FixedViewPager;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hejun on 2016/11/24.
 */
public class AllCustomerFragmentNew extends BaseViewPageFragment implements View.OnClickListener {

    private String[] tabName;
    private TextView btn_tuiguang;
    private LoadingView loadingView;
    private FansListFragment fansListFragment;
    private MenberListFragment menberListFragment;
    private boolean shouldRfrensh;
    private boolean hasInit;
    private TextView txt_title;
    private CollegeCourseView collegeCourseView;
    private RelativeLayout layout_nodata_admin;
    private ImageView iv_nodata_admin;
    private RelativeLayout layout_nodata_salesman;
    private ImageView iv_salsman;
    private Button btn_gonext;
    private int lessonId;

    @Override
    protected void initView() {

        UmengUtil.onEvent(getActivity(), "home_client_enter",null);
        if(tabName==null||tabName.length<=0)
        {

            if (loadingView ==null)
            {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                loadingView =new LoadingView(getActivity());
                content.addView(loadingView,layoutParams);
            }
        }else
        {
            initTab();
        }
        btn_tuiguang = (TextView) findViewById(R.id.btn_tuiguang);
        txt_title = (TextView) findViewById(R.id.txt_title);

        //无粉丝时且身份为店长时显示的布局
        layout_nodata_admin = (RelativeLayout) findViewById(R.id.layout_nodata_admin);
        iv_nodata_admin = (ImageView) findViewById(R.id.iv_nodata_admin);
        //无粉丝时身份为业务员时显示的布局
        layout_nodata_salesman = (RelativeLayout) findViewById(R.id.layout_nodata_salesman);
        iv_salsman = (ImageView) findViewById(R.id.iv_salsman);
        btn_gonext = (Button) findViewById(R.id.btn_gonext);
        btn_gonext.setEnabled(false);

        if(Account.user != null)
        txt_title.setText(Account.user.getShopName());
        btn_tuiguang.setOnClickListener(this);
        findViewById(R.id.edit_search).setFocusable(false);
        ((EditText)findViewById(R.id.edit_search)).setHint("请输入姓名或手机号");
        findViewById(R.id.edit_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeachPeopleFilterFirstAct.start(getActivity());
                UmengUtil.onEvent(getActivity(),"home_searchbox_click",null);
            }
        });
        if(Account.user != null) {
            if (Account.user.getRoleType() == Constants.USER_TYPE_ADMIN) {
                ViewUtils.setVisible(btn_tuiguang);
            } else {
                ViewUtils.setGone(btn_tuiguang);
            }
        }
    }

    private void initTab() {
        pager = (FixedViewPager) findViewById(viewPagerId());
        tabs = (PagerSlidingTabStrip) findViewById(tabsId());
        this.setSelectedTextColor("#ff5f19");
        this.setIndicatorColor("#ff5f19");
        tabs.setIndicatorColor(Color.parseColor("#626262"));
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        int tabPaddingLeft = (int) TypedValue
//                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, dm);
//        int tabPaddingRight = (int) TypedValue
//                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, dm);
//        tabs.setTabPaddingLeft(tabPaddingLeft);
//        tabs.setTabPaddingRight(tabPaddingRight);
//        pager.setAdapter(new ViewPageFragmentAdapter(getActivity().getSupportFragmentManager(), fragments, tabName()));
        pager.setAdapter(getAdapter());
        tabs.setViewPager(pager);
        pager.setTouchIntercept(false);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                HashMap<String, String> map = new HashMap<String, String>();
                switch (i){
                    case 0:
                        fansListFragment.onDataRefresh();
                        showFansDefaultView(Config.memberCount);
                        map.put("店铺粉丝", String.valueOf(1));
                        UmengUtil.onEvent(getActivity(),"home_toptab_click",map);
                        break;
                    case 1:
                        menberListFragment.onDataRefresh();
                        PreferUtil.getInstance().setIsFirstOpenFans();
                        showMenberDefaultView();
                        map.put("我的会员", String.valueOf(2));
                        UmengUtil.onEvent(getActivity(),"home_toptab_click",map);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setTabsValue();
    }


    @Override
    public void onResume() {
        super.onResume();
        if ( Account.user != null && 0 == ((MainActivity) getActivity()).fragmentController.getCurrentTab()) {
            //账号冲突后导致数据不刷新
            if(shouldRfrensh&&fansListFragment!=null&&menberListFragment!=null){
                fansListFragment.setHasInit(false);
                menberListFragment.setHasInit(false);
                shouldRfrensh=false;
            }
            if(!hasInit){
                initBaseData();
                hasInit=true;
            }
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.win_allcustomer;
    }

    @Override
    protected String[] tabName() {
        return tabName;
    }

    @Override
    protected List<Fragment> fragments() {
        List<Fragment> fragments=new ArrayList<Fragment>();
        fansListFragment = new FansListFragment();
//        fansListFragment.setFansCoutCallBack(fansCountCallBack);
        fragments.add(fansListFragment);
        menberListFragment = new MenberListFragment();
//        menberListFragment.setMenberCountCallBack(memberCountCallBack);
        fragments.add(menberListFragment);
        return fragments;
    }


    ParameCallBack fansCountCallBack=new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            tabName[0]="店铺粉丝("+object+")";
            Config.fansCount=(Integer) object;
        }
    };

    ParameCallBack memberCountCallBack=new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            tabName[1]="我的会员("+object+")";
            Config.memberCount=(Integer)object;
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tuiguang:
                //跳转推广
                PromotionArticleAct.start(getActivity(),1);
                break;
        }
    }

    @Override
    protected void loadInitData() {
        if(tabName==null||tabName.length<=0)
        {
            loadingView.startLoading();
            getFansAndMenberCount();
//            initBaseData();
        }
    }

    private void initBaseData() {
        final ShopTagRequest shopTagRequest = new ShopTagRequest();
        shopTagRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if(Config.tagList!=null){
                    Config.tagList.clear();
                }
                Config.tagList = shopTagRequest.getTagModelList();
            }

            @Override
            public void onFail(int code) {

            }
        });

        shopTagRequest.start();

        getFansAndMenberCount();
    }

    private void getFansAndMenberCount() {

        final GetFansAndMenberCountRequest getFansAndMenberCountRequest = new GetFansAndMenberCountRequest();
        getFansAndMenberCountRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                //TODO 赋值
                tabName=new String[2];
                Config.fansCount = getFansAndMenberCountRequest.getFansCount();
                Config.memberCount=getFansAndMenberCountRequest.getMenberCount();
                tabName[0]="店铺粉丝("+Config.fansCount+")";
                tabName[1]="我的会员("+Config.memberCount+")";
                //检测显示默认浮层
//                if(!Config.isShouldShow&&fansListFragment!=null){
//                    fansListFragment.showDefaultView(Config.fansCount);
//                    PreferUtil.getInstance().setIsFirstOpenFans();
//                }
                loadingView.onLoadingComplete();
                showFansDefaultView(getFansAndMenberCountRequest.getFansCount());
                if(tabName!=null&&tabName.length>0)
                {
                    initTab();
                }
//                if(!Config.isShouldShow&&menberListFragment!=null){
//                    menberListFragment.showNodataView(Config.memberCount,Config.fansCount);
//                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        getFansAndMenberCountRequest.start();
    }

    private void showMenberDefaultView() {
        if(Config.memberCount==0){
            findViewById(R.id.layout_nodata_all).setVisibility(View.VISIBLE);
        }
        if(Config.memberCount==0){
            findViewById(R.id.layout_nodata_all).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.txt_fanscount)).setText("店铺粉丝("+Config.fansCount+")");
            //如果粉丝总数与线下录入粉丝数相等 就说明没有可推送的粉丝
            if(Config.fansCount!=0&&Config.fansCount==Config.outLineFansGroupCount){
                findViewById(R.id.btn_sendmsg_tofans).setVisibility(View.GONE);
            }else if(Config.fansCount==0){
                findViewById(R.id.btn_sendmsg_tofans).setVisibility(View.GONE);
            }else {
                findViewById(R.id.btn_sendmsg_tofans).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_sendmsg_tofans).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChooseGroupPeopleAct.start(getActivity(),true);

                    }
                });
            }
            findViewById(R.id.txt_fanscount).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(0);
                    findViewById(R.id.layout_nodata_all).setVisibility(View.GONE);
                }
            });
        }else {
            findViewById(R.id.layout_nodata_all).setVisibility(View.GONE);
        }
    }


    private void showFansDefaultView(int fansCount) {
        //处理无粉丝与有粉丝第一次进入的情况时显示默认图层
        if (Account.user.getRoleType() == Constants.USER_TYPE_ADMIN) {
            if (fansCount == 0) {
                if(PreferUtil.getInstance().getIsFirstOpenFans()){
                    layout_nodata_admin.setVisibility(View.VISIBLE);
                    showCollegeCourse();
                    layout_nodata_admin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_nodata_admin.setVisibility(View.GONE);
                            if(collegeCourseView!=null){
                                collegeCourseView.setVisibility(View.GONE);
                            }
                        }
                    });
                }

            } else {
                //有粉丝但是是第一次进入该页面
                if (PreferUtil.getInstance().getIsFirstOpenFans()) {
                    iv_nodata_admin.setBackgroundResource(R.drawable.icon_nodata_brand);
                    layout_nodata_admin.setVisibility(View.VISIBLE);
                    showCollegeCourse();
                    layout_nodata_admin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_nodata_admin.setVisibility(View.GONE);
                            if(collegeCourseView!=null){
                                collegeCourseView.setVisibility(View.GONE);
                            }
                        }
                    });
                }else {
                    layout_nodata_admin.setVisibility(View.GONE);
                    if(collegeCourseView!=null){
                        collegeCourseView.setVisibility(View.GONE);
                    }
                }
            }
        } else {
//            if (collegeCourseView == null) {
//                collegeCourseView = new CollegeCourseView(getActivity(), 1);
//            }
            getLessonId();
            if (fansCount == 0) {
                if(PreferUtil.getInstance().getIsFirstOpenFans()){
                    layout_nodata_salesman.setVisibility(View.VISIBLE);
                    layout_nodata_salesman.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_nodata_salesman.setVisibility(View.GONE);
                        }
                    });
                }

            } else {
                //有粉丝但是是第一次进入该页面
                if (PreferUtil.getInstance().getIsFirstOpenFans()) {
//                    btn_gonext.setBackgroundResource(R.drawable.icon_nodata_brand);
                    iv_salsman.setBackgroundResource(R.drawable.icon_nodata_brand);
                    layout_nodata_salesman.setVisibility(View.VISIBLE);
                    layout_nodata_salesman.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_nodata_salesman.setVisibility(View.GONE);
                        }
                    });
                }else {
                    layout_nodata_salesman.setVisibility(View.GONE);
                }
            }

            btn_gonext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YamCollegeDetailAct.start(getActivity(), lessonId);
                }
            });
        }
        PreferUtil.getInstance().setIsFirstOpenFans();
    }



    public void setShouldRfrensh(boolean shouldRfrensh) {
        this.shouldRfrensh = shouldRfrensh;
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
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, Config.displayMetrics));
//        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, Config.displayMetrics));
//        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, Config.displayMetrics));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#ff5f19"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#ff5f19"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(R.color.transparent);
        tabs.initTabStyles();
    }

    /**
     * 显示柚安米课程
     */
    private void showCollegeCourse() {
        if (collegeCourseView == null) {
            RelativeLayout.LayoutParams courseLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            courseLp.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
            courseLp.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            courseLp.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            courseLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            collegeCourseView = new CollegeCourseView(getActivity(), 1);
            collegeCourseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YamCollegeDetailAct.start(getActivity(), collegeCourseView.getId());
                }
            });
            content.addView(collegeCourseView, courseLp);
        } else {
            collegeCourseView.show();
        }

    }

    private void getLessonId() {
        final YamLessonRequest yamLessonRequest = new YamLessonRequest(1);
        yamLessonRequest.start();
        yamLessonRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if(yamLessonRequest.getYamLessonInfo()!=null){
                    btn_gonext.setEnabled(true);
                    lessonId = yamLessonRequest.getYamLessonInfo().getId();
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }
}
