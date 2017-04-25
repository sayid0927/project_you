package com.zxly.o2o.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zxly.o2o.activity.GalleryViewPagerAct;
import com.zxly.o2o.activity.TouchImageViewAct;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.MyCirCleObject;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.util.Constants;

import java.util.List;

/**
 *     @author huangbin  @version 创建时间：2015-1-21 下午4:14:25    类说明: 
 */
public abstract class BasicMyCircleAdapter extends ObjectAdapter {

    public boolean isFastScrolling;
    protected Animation animation;

    public BasicMyCircleAdapter(Context _context) {
        super(_context);
    }


    public void setItemAnim(MyCirCleObject mObject, View mView, int position) {
        if (mObject.getIsShown()) {
            if (position == 0) {
                mView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.listview_first_item_anim));
                mObject.setIsShown(false);
            }
        } else if (MyCircleRequest.publishTopic != null && MyCircleRequest
                .publishTopic.getIsShown()&&position==0) {
            mView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.listview_first_item_anim));
            MyCircleRequest.publishTopic = null;
        } else {
            if (!isFastScrolling && position > Constants.PER_PAGE_SIZE - 1) {
                mView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.listview_item_anim));
            }
            if (position > 0) {
                mObject.setIsShown(true);
            }
        }
    }

    public void setItemAnim(MyCirCleObject mObject, View mView, int position, int all) {
        if (mObject.isNeedShowAddAnim()) {
            if (position == 0 || position == all - 1) {
                mView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.listview_first_item_anim));
                mObject.setIsNeedShowAddAnim(false);
                mObject.setIsShown(true);
            }
        } else if(!mObject.getIsShown()){
            if (!isFastScrolling && position > Constants.PER_PAGE_SIZE - 1) {
                mView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.listview_item_anim));
            }
            if (position > 0) {
                mObject.setIsShown(true);
            }
        }
    }

    public void showPic(List<String> urls, int item) {
        if(urls!=null&&urls.size()>item) {
            GalleryViewPagerAct
                    .start(AppController.getInstance().getTopAct(), urls.toArray(new String[urls.size()]),
                            item);
        }
    }

    public void showPic(String url, long time) {
        Intent intent = new Intent(context, TouchImageViewAct.class);

        intent.putExtra("file_path", url);
        if (time == 0) {
            intent.putExtra("file_is_local", true);
        } else {
            intent.putExtra("file_is_local", false);
        }

        context.startActivity(intent);
    }

}
