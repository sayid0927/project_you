package com.zxly.o2o.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.CallBack;


public class LoadingDialog  {

	private Dialog dialog; 
	private  Context context;
    protected Animation progressAnima;
	private View contentView;
    private ImageView imgProgress;
	private CallBack callBack;

	public LoadingDialog()
	{
		context = AppController.getInstance().getTopAct();
		dialog = new Dialog(context, R.style.dialog);
		dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
		this.dialog.getWindow().setGravity(Gravity.CENTER);
        progressAnima = AnimationUtils.loadAnimation(context,
                R.anim.loading_progressbar_anim);
        LinearInterpolator lin = new LinearInterpolator();
        progressAnima.setInterpolator(lin);
		contentView=LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        imgProgress=(ImageView)contentView.findViewById(R.id.img_progress);
	}

	public void setCancelable(boolean cancelable,CallBack callBack){
		dialog.setCancelable(cancelable);
		this.callBack = callBack;
	}

	public void show() {
		if (dialog.isShowing())
			return;
		if(!((Activity)context).isFinishing()) {
			dialog.show();
			dialog.getWindow().setContentView(contentView);
			imgProgress.startAnimation(progressAnima);
		}
	}
	public void dismiss() {
		this.dialog.cancel();
        imgProgress.clearAnimation();
		if(callBack!=null){
			callBack.onCall();
		}
	}
}
