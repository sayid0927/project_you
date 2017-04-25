package com.flyco.tablayout.utils;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.easemob.chatuidemo.R;

import java.util.ArrayList;


public class FragmentChangeManager {
    private FragmentManager fm;
    private int containerViewId;
    /** Fragment切换数组 */
    private ArrayList<Fragment> fragments;
    /** 当前选中的Tab */
    private int currentTab;

    private  final int NO_ANIMATION=0;
    private boolean isInit=false;
    private FragmentActivity fa;

    public FragmentChangeManager(FragmentActivity fa,FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments) {
        this.fm = fm;
        this.fa = fa;
        this.containerViewId = containerViewId;
        this.fragments = fragments;
        initFragments();
    }



    /** 初始化fragments */
    private void initFragments() {
        for (Fragment fragment : fragments) {
            fm.beginTransaction().add(containerViewId, fragment).hide(fragment).commit();
        }

        setFragments(0);
    }

    /** 界面切换控制 */
    public void setFragments(int index) {



        for (int i = 0; i < fragments.size(); i++) {
            FragmentTransaction ft ;
            if(!isInit&&currentTab==0){
                ft=obtainFragmentTransaction(index,NO_ANIMATION);
                isInit=true;
            }else{
                ft=obtainFragmentTransaction(index,1);
            }
            Fragment fragment = fragments.get(i);
            if (i == index) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = index;
    }

    /**
     * FragmentTransaction
     */
    private FragmentTransaction obtainFragmentTransaction(int index,int type) {
        FragmentTransaction   ft = fa.getSupportFragmentManager().beginTransaction();

        if(type==NO_ANIMATION)
            return ft;

        if (index > currentTab) {
            ft.setCustomAnimations(R.anim.ease_slide_left_in, R.anim.ease_slide_left_out);
        } else {
            ft.setCustomAnimations(R.anim.ease_slide_right_in, R.anim.ease_slide_right_out);
        }
        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }
}