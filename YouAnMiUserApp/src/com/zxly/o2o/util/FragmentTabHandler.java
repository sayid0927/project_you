package com.zxly.o2o.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


import com.zxly.o2o.o2o_user.R;

import java.util.List;


public class FragmentTabHandler {
	
	public static final int NO_ANIMATION=0;
	private List<Fragment> fragments;
	private FragmentActivity fragmentActivity; // Fragment所属的Activity
	private int fragmentContentId;            // Activity中所要被替换的区域的id
	private int currentTab;
	private boolean isInit=false;

	public FragmentTabHandler(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId) {
		this.fragments = fragments;
		this.fragmentActivity = fragmentActivity;
		this.fragmentContentId = fragmentContentId;
	}


	/**
	 * 切换带动画效果tab
	 */
	public void showTab(int idx) {	
		
		for (int i = 0; i < fragments.size(); i++) {
			if (idx == i) {			
				Fragment fragment = fragments.get(i);	
				FragmentTransaction ft = null;
				if(!isInit&&idx==0){
					ft=obtainFragmentTransaction(idx,NO_ANIMATION);
					isInit=true;
				}else{
					ft=obtainFragmentTransaction(idx,1);
				}
			
			    if(getCurrentFragment()!=null&&getCurrentFragment().isVisible()){			    	
					getCurrentFragment().onPause();	
					ft.hide(getCurrentFragment());			    	
			    }

			    currentTab = idx;
				if(fragment.isAdded()){
					fragment.onResume();
				}else{
					ft.add(fragmentContentId, fragment);
				}	
				
				ft.show(fragment);
				try {
					ft.commit();
				} catch (Exception e) {
		//	      Log.d("commitError"," -->"+e.toString());
				}
				
				return;
			}
			
		}
				
	}

	
	/**
	 * FragmentTransaction 
	 */
	private FragmentTransaction obtainFragmentTransaction(int index,int type) {
		FragmentTransaction   ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
		
		if(type==NO_ANIMATION)
			return ft;
		
		if (index > currentTab) {
			ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
		} else {
			ft.setCustomAnimations(R.anim.slide_right_in,R.anim.slide_right_out);
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
