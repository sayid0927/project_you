package com.easemob.easeui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.easemob.chatuidemo.R;


/**
 * Created by Administrator on 2015/4/21.
 */
public class EaseMyFlipperView extends ViewFlipper {
    private Context context;

    public static int NODATA=2;
    public static int LOADSUCCESSFUL=3;
    public static int LOADFAIL=1;
    public static int LOADING=0;
    //    private OnAgainListener onAgainListener;

    public EaseMyFlipperView(Context context) {
        super(context);
        this.context = context;

        startAnim();

    }

    public View getRetryBtn(){
        return findViewById(R.id.ease_loading_fail_layout);
    }

    public void setEmptyImg(int id,String notice){
        Drawable topDrawable = getResources().getDrawable(id);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        ((TextView)findViewById(R.id.empty_data)).setCompoundDrawables(null, topDrawable, null, null);
        ((TextView)findViewById(R.id.empty_data)).setText(notice);
        findViewById(R.id.btn_loading).setVisibility(VISIBLE);

    }

    public View getEmptyBtn(String btnstr){
        ((TextView)findViewById(R.id.btn_loading)).setText(btnstr);
        return findViewById(R.id.btn_loading);
    }


    public void setFailImg(int id,String notice){
        ((ImageView)findViewById(R.id.loading_fail_imageView)).setImageResource(id);
        ((TextView)findViewById(R.id.loading_fail_text)).setText(notice);
    }

    public void setDisplayedChild(int whichChild, boolean isShowAnim) {
        if (isShowAnim)
            startAnim();
        super.setDisplayedChild(whichChild);
        cleanAnim();
    }

    public EaseMyFlipperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        startAnim();
    }


    public void cleanAnim() {
        this.setInAnimation(context, R.anim.ease_aphoa_nomal);
        this.setOutAnimation(context, R.anim.ease_aphoa_nomal);
    }

    public void startAnim() {
        this.setInAnimation(context, R.anim.ease_alpha_in_anim);
        this.setOutAnimation(context, R.anim.ease_alpha_out_anim);
    }


}
