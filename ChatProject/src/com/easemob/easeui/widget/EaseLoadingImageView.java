/*
 * 文件名：StoreLoadingImageView.java
 * 版权：Copyright 2015 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： StoreLoadingImageView.java
 * 修改人：wuchenhui
 * 修改时间：2015-3-11
 * 修改内容：新增
 */
package com.easemob.easeui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.easemob.chatuidemo.R;


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
public class EaseLoadingImageView extends ImageView {

    private final AnimationDrawable animationDrawable;
    //    AnimationDrawable animationDrawable;
    private Animation progressAnima;

    public EaseLoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        progressAnima = AnimationUtils.loadAnimation(context, R.anim.ease_loading_progressbar_anim);
//        progressAnima.setInterpolator(new LinearInterpolator());
        this.setImageResource(R.drawable.ease_loading_anim);
        animationDrawable = (AnimationDrawable) this.getDrawable();
    }

    @Override
    protected void onAttachedToWindow() {

//        this.startAnimation(progressAnima);
        animationDrawable.start();
        super.onAttachedToWindow();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility==View.GONE){
//            this.clearAnimation();
            animationDrawable.stop();
        }else if(visibility==View.VISIBLE){
            if(this.getAnimation()==null)
//            this.startAnimation(progressAnima);
            animationDrawable.start();
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
