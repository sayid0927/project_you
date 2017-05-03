package com.zxly.o2o.dialog;

import com.zxly.o2o.application.AppController;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


public class LoadingDialog {

    private Dialog dialog;
    private Context context;
    protected Animation progressAnima;
    private View contentView;
    private ImageView imgProgress;
    private AnimationDrawable animationDrawable;

    public LoadingDialog(Context context) {
        //		context = AppController.getInstance().getTopAct();
        this.context = context;
        dialog = new Dialog(context, R.style.dialog);
        dialog.setCancelable(false);
        this.dialog.getWindow().setGravity(Gravity.CENTER);
        //之前加载动画图片
//        progressAnima = AnimationUtils.loadAnimation(context,
//                R.anim.loading_progressbar_anim);
//        LinearInterpolator lin = new LinearInterpolator();
//        progressAnima.setInterpolator(lin);
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        imgProgress = (ImageView) contentView.findViewById(R.id.img_progress);

        //现在加载图片
        imgProgress.setImageResource(R.drawable.loading_ivset);
        animationDrawable = (AnimationDrawable) imgProgress.getDrawable();
    }

    public LoadingDialog() {
        context = AppController.getInstance().getTopAct();
        dialog = new Dialog(context, R.style.dialog);
        dialog.setCancelable(false);
        this.dialog.getWindow().setGravity(Gravity.CENTER);
//        progressAnima = AnimationUtils.loadAnimation(context,
//                R.anim.loading_progressbar_anim);
//        LinearInterpolator lin = new LinearInterpolator();
//        progressAnima.setInterpolator(lin);
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        imgProgress = (ImageView) contentView.findViewById(R.id.img_progress);
        //现在加载图片
        imgProgress.setImageResource(R.drawable.loading_ivset);
        animationDrawable = (AnimationDrawable) imgProgress.getDrawable();
    }


    public void show() {
        if (dialog.isShowing()) {
            return;
        }
        if(!((Activity)context).isFinishing()){
            dialog.getWindow().setContentView(contentView);
            dialog.show();
//            imgProgress.startAnimation(progressAnima);
            animationDrawable.start();
        }

    }
    public void show(CallBack callBack) {
        if (dialog.isShowing()) {
            return;
        }
        if(!((Activity)context).isFinishing()){
            dialog.getWindow().setContentView(contentView);
            dialog.show();
//            imgProgress.startAnimation(progressAnima);
            animationDrawable.start();
        }

        callBack.onCall();

    }

    public void dismiss() {
        this.dialog.cancel();
//        imgProgress.clearAnimation();
        animationDrawable.stop();
    }
}
