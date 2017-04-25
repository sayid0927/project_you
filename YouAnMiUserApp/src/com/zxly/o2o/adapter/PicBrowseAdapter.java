package com.zxly.o2o.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zxly.o2o.o2o_user.R;

import java.util.List;

public class PicBrowseAdapter extends PagerAdapter{

	private Context mContext;
    private List<String> imagePaths;
	
	public PicBrowseAdapter(Context mContext,List<String> imagePaths){
		this.mContext=mContext;
        this.imagePaths=imagePaths;           
	}
	
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}


	@Override
	public int getCount() {
	    if(imagePaths.size()==1){
            return 1;
        }
        return imagePaths.size()*1000;
	}

	@Override
	public Object instantiateItem(View arg0, int position) {   
        ImageView imageView = (ImageView) new ImageView(mContext);
        imageView.setImageResource(R.drawable.ic_launcher);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(-1, -2);
        imageView.setLayoutParams(params);
        if(imagePaths.size()!=1){
            position=position%imagePaths.size();
        }
        String imageUrl=imagePaths.get(position);
        imageView.setTag(imageUrl);
        ((ViewPager) arg0).addView(imageView, 0);
        return imageView;    
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}



}
