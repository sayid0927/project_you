/*
 * 文件名：StoreLoadingImageView.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： StoreLoadingImageView.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-11
 * 修改内容：新增
 */
package com.zxly.o2o.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.zxly.o2o.o2o_user.R;

/**
 * TODO 添加类的一句话简单描述。
 * <p/>
 * TODO 详细描述
 * <p/>
 * TODO 示例代码
 * <p/>
 * <pre>
 * </pre>
 *
 * @author wuchenhui
 * @version YIBA-O2O 2015-3-11
 * @since YIBA-O2O
 */
public class StoreLoadingImageView extends ImageView {

//    AnimationDrawable animationDrawable;
    private Animation progressAnima;

    public StoreLoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressAnima = AnimationUtils.loadAnimation(context, R.anim.loading_progressbar_anim);
        progressAnima.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onAttachedToWindow() {

        this.startAnimation(progressAnima);
        super.onAttachedToWindow();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility==View.GONE){
            this.clearAnimation();
        }else if(visibility==View.VISIBLE){
            if(this.getAnimation()==null)
            this.startAnimation(progressAnima);
        }
    }

    @Override
    protected void onDetachedFromWindow() {

//        try {
//            if (animationDrawable.isRunning()) {
//                animationDrawable.stop();
//            }
//        } catch (Exception e) {
//            Log.d("imageStart", "stop-->" + e.toString());
//        }
        super.onDetachedFromWindow();
    }
}
