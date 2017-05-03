package com.zxly.o2o.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.easemob.easeui.ui.EaseHXMainFragment;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.fragment.CustomArticleFragment;
import com.zxly.o2o.fragment.GuaranteeManageFragmnet;
import com.zxly.o2o.fragment.GuaranteePayFragment;
import com.zxly.o2o.fragment.H5CustomArticleDetailFragment;
import com.zxly.o2o.fragment.ScanResultFragment;
import com.zxly.o2o.fragment.TaskTargetListFragment;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by kenwu on 2015/12/17.
 */
public class FragmentListAct extends BasicAct {

    private static ParameCallBack callBack;

    public static final int PAGE_TASK_TARGET=1;

    public static final int PAGE_CUSTOM_ARTICLE_LIST=2;

    public static final int PAGE_CUSTOM_ARTICLE_DETAIL=3;

    public static final int PAGE_SCANER_RESULT=4;

    public static final int PAGE_GUARANTEE_MANAGE=5;

    public static final int PAGE_GUARANTEE_PAY=6;
    public static final int PAGE_GUARANTEE_MSG=7;
    public static final int PAGE_INDUSTRIES_ARTICLE_LIST=8;

    Fragment fragment;
    FragmentTransaction ft;

    public View btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_common);

        View btnBack=findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                UmengUtil.onEvent(FragmentListAct.this,new UmengUtil().INSURANCE_BACK_CLICK,null);
            }
        });

        btnShare=findViewById(R.id.btn_share);

        int pageId=getIntent().getIntExtra("pageId",-1);


        if(pageId==-1)
            finish();


        ((TextView)findViewById(R.id.txt_title)).setText(getIntent().getStringExtra("pageName"));

        ft = getSupportFragmentManager().beginTransaction();

        switch (pageId){
            case PAGE_TASK_TARGET:
                ViewUtils.setGone(findViewById(R.id.layout_title));
                fragment= TaskTargetListFragment.newInstance();
                ft.add(R.id.layout_content, fragment);
                ft.show(fragment);
                ft.commit();
                break;

            case PAGE_CUSTOM_ARTICLE_LIST:
                fragment= CustomArticleFragment.newInstance(1);
                ((CustomArticleFragment)fragment).setArticleDeleteCallBack(callBack);
                ft.add(R.id.layout_content, fragment);
                ft.show(fragment);
                ft.commit();
                break;

            case PAGE_SCANER_RESULT:
                fragment= ScanResultFragment.newInstance(getIntent().getExtras());
                ft.add(R.id.layout_content, fragment);
                ft.show(fragment);
                ft.commit();
                break;
            case PAGE_INDUSTRIES_ARTICLE_LIST:
                fragment= CustomArticleFragment.newInstance(2);
                ((CustomArticleFragment)fragment).setArticleDeleteCallBack(callBack);
                ft.add(R.id.layout_content, fragment);
                ft.show(fragment);
                ft.commit();
                break;
            case PAGE_CUSTOM_ARTICLE_DETAIL:
                Bundle bundle=getIntent().getExtras();
                if(bundle.getString("wxUrl")!=null){
                    bundle.putString("pageTitle","自定义文章");
                }else {
                    bundle.putString("pageTitle","文章详情");
                }
                fragment= H5CustomArticleDetailFragment.newInstance(bundle);
                ((H5CustomArticleDetailFragment)fragment).setArticleRefreshCallBack(callBack);
                ft.add(R.id.layout_content, fragment);
                ft.show(fragment);
                ft.commit();
                break;

            case PAGE_GUARANTEE_MANAGE:
                fragment= GuaranteeManageFragmnet.newInstance();
                ft.add(R.id.layout_content, fragment);
                ft.show(fragment);
                ft.commit();
                break;

            case PAGE_GUARANTEE_PAY:
                fragment= GuaranteePayFragment.newInstance(getIntent().getExtras());
                ft.add(R.id.layout_content, fragment);
                ft.show(fragment);
                ft.commit();
                break;
            case PAGE_GUARANTEE_MSG:
                fragment= new EaseHXMainFragment();
                ft.add(R.id.layout_content, fragment);
                ft.show(fragment);
                ft.commit();
                break;
            default:
                break;
        }

        UmengUtil.onEvent(FragmentListAct.this,new UmengUtil().INSURANCE_ENTER,null);
     //  PromotionAcitcityFragment fragment=PromotionAcitcityFragment.newInstance();
     //   Fragment fragment=PromotionFragment.newInstance();
    // Fragment fragment= PromotionArticleFragment.newInstance();

    }


    @Override
    protected void onDestroy() {
        callBack=null;
        super.onDestroy();
    }

    public static void start(String pageName,int pageId){
        Intent it = new Intent();
        it.setClass(AppController.getInstance().getTopAct(),FragmentListAct.class);
        it.putExtra("pageId", pageId);
        it.putExtra("pageName", pageName);
        ViewUtils.startActivity(it, AppController.getInstance().getTopAct());
    }

    public static void start(String pageName,int pageId,ParameCallBack callBack){
        Intent it = new Intent();
        it.setClass(AppController.getInstance().getTopAct(),FragmentListAct.class);
        it.putExtra("pageId", pageId);
        it.putExtra("pageName", pageName);
        FragmentListAct.callBack=callBack;
        ViewUtils.startActivity(it,AppController.getInstance().getTopAct());
    }


    public static void start(String pageName,int pageId,Bundle extData,ParameCallBack callBack){
        FragmentListAct.callBack=callBack;
        Intent it = new Intent();
        it.setClass(AppController.getInstance().getTopAct(), FragmentListAct.class);
        it.putExtra("pageId", pageId);
        it.putExtra("pageName", pageName);
        it.putExtras(extData);
        ViewUtils.startActivity(it, AppController.getInstance().getTopAct());
    }


    public void onCall(Object data){
        if(callBack!=null)
            callBack.onCall(data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}
