package com.zxly.o2o.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.widget.viewpagerindicator.CirclePageIndicator;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.Banners;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;

/**
 * 新新手引导界面
 * Created by kenwu on 2015/9/6.
 */
public class UserGuideAct extends BasicAct {

    private ViewPager pager;

    private CirclePageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.win_user_guide);

        pager= (ViewPager) findViewById(R.id.pager);


        View[] views=new View[3];
        views[0]=getGuideImage(R.drawable.pic01);
        views[1]=getGuideImage(R.drawable.pic02);
        views[2]=getLastGuideLayout();

        MPagerAdapter adapter=new MPagerAdapter(views);
        pager.setAdapter(adapter);

        indicator=(CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }

    private View getGuideImage(int res){
        ImageView iv=new ImageView(this);
        iv.setLayoutParams(pager.getLayoutParams());
        iv.setImageResource(res);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        return iv;
    }

    private View getLastGuideLayout(){
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.page_userguide_last, null);

        View btn_experience=view.findViewById(R.id.btn_experience);
        btn_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserGuideAct.this, HomeAct.class);
                ViewUtils.startActivity(intent, UserGuideAct.this);
                PreferUtil.getInstance().setAppHasOpen();
                UserGuideAct.this.finish();
            }
        });

        return view;
    }


    static class MPagerAdapter extends PagerAdapter {

        private View[] views;

        public MPagerAdapter(View[] views){
            this.views=views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public Object instantiateItem(View arg0, int position) {

            ((ViewPager) arg0).addView(views[position], 0);

            return views[position];


        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }


    }



}
