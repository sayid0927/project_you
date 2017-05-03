package com.zxly.o2o.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.MainActivity;
import com.zxly.o2o.activity.PromotionArticleAct;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetFansAndMenberCountRequest;
import com.zxly.o2o.request.ShopTagRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/8/23.
 * 商户改版一期 客多多页面(首页)
 */
public class AllCustomerFragment extends TagBaseViewPageFragment implements View.OnClickListener{

    private int index=0;
    public RadioButton btn_fans;
    public RadioButton btn_menbers;
    private TextView btn_tuiguang;
    private FansListFragment fansListFragment;
    private MenberListFragment menberListFragment;
    private boolean shouldRfrensh;
    private boolean isShouldShow;

    @Override
    protected void initView() {
        super.initView();
        btn_tuiguang = (TextView) findViewById(R.id.btn_tuiguang);
        btn_tuiguang.setOnClickListener(this);
        btn_fans = (RadioButton) findViewById(R.id.btn_fans);
        btn_menbers = (RadioButton) findViewById(R.id.btn_menbers);
        if (Account.user!=null&&Account.user.getRoleType() == Constants.USER_TYPE_ADMIN) {
            ViewUtils.setVisible(btn_tuiguang);
        }else{
            ViewUtils.setGone(btn_tuiguang);
        }
        btn_fans.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    fansListFragment.onDataRefresh();
                }
            }
        });
        btn_menbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    menberListFragment.onDataRefresh();
                    PreferUtil.getInstance().setIsFirstOpenFans();
                }
            }
        });
    }


    public void setShouldShow(boolean shouldShow) {
        isShouldShow = shouldShow;
    }

    public void changeFansFragment(){
        btn_fans.setChecked(true);
        pager.setCurrentItem(0);
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

        final GetFansAndMenberCountRequest getFansAndMenberCountRequest = new GetFansAndMenberCountRequest();
        getFansAndMenberCountRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                //TODO 赋值
                Config.fansCount = getFansAndMenberCountRequest.getFansCount();
                Config.memberCount=getFansAndMenberCountRequest.getMenberCount();
                btn_fans.setText("粉丝("+Config.fansCount+")");
                btn_menbers.setText("会员("+Config.memberCount+")");
                //检测显示默认浮层
//                if(!Config.isShouldShow&&fansListFragment!=null){
//                    fansListFragment.showDefaultView(Config.fansCount);
//                    PreferUtil.getInstance().setIsFirstOpenFans();
//                }
                if(!Config.isShouldShow&&menberListFragment!=null){
                    menberListFragment.showNodataView(Config.memberCount,Config.fansCount);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        getFansAndMenberCountRequest.start();
    }

    public void setShouldRfrensh(boolean shouldRfrensh) {
        this.shouldRfrensh = shouldRfrensh;
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
            initBaseData();
        }

    }

    public void open(Activity activity, int index)
    {
        this.index = index;
    }

    @Override
    protected int layoutId() {
        return R.layout.win_customers;
    }

    @Override
    protected List<Fragment> fragments() {
        List<Fragment> list=new ArrayList<Fragment>();
        fansListFragment = new FansListFragment();
        fansListFragment.setFansCoutCallBack(fansCountCallBack);
        menberListFragment = new MenberListFragment();
        menberListFragment.setMenberCountCallBack(memberCountCallBack);
        list.add(fansListFragment);
        list.add(menberListFragment);
        return list;
    }

    ParameCallBack fansCountCallBack=new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            btn_fans.setText("粉丝("+object+")");
            Config.fansCount=(Integer) object;
        }
    };

    ParameCallBack memberCountCallBack=new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            btn_menbers.setText("会员("+object+")");
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

}


