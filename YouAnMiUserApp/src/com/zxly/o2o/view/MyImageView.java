package com.zxly.o2o.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.util.CallBack;

/**
 * Created by dsnx on 2016/2/23.
 */
public class MyImageView extends NetworkImageView {


    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if(bm!=null)
        {
            ViewGroup.LayoutParams layoutParams=this.getLayoutParams();
            float imgX;
            float imgY;
            imgX=bm.getWidth();
            imgY=bm.getHeight();
            float s=imgX/imgY;
            imgY= Config.screenWidth/s;
            layoutParams.height= (int) imgY;
            this.setLayoutParams(layoutParams);

        }
        super.setImageBitmap(bm);

    }
}
