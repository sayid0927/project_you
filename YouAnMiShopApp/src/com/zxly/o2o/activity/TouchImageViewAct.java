package com.zxly.o2o.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.widget.zoomView.TouchImageView;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ImageUtil;

import java.io.IOException;
import java.io.InputStream;

public class TouchImageViewAct extends Activity {
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar("照片");
        setContentView(R.layout.ease_touch_image_view);

        final TouchImageView image = (TouchImageView) findViewById(R.id.touch_image_view_sample_image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Drawable placeHolder = getResources().getDrawable(R.drawable.ic_launcher);
        String is = getIntent().getStringExtra("file_path");
        if (getIntent().getBooleanExtra("file_is_local", false)) {
            if (TextUtils.isEmpty(is)) {
                final InputStream pic;
                try {
                    pic = getResources().getAssets().open("eg.png");
                     bitmap = BitmapFactory.decodeStream(pic);
                    image.setLocalImageBitmap(bitmap);
                    pic.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                image.setLocalImageBitmap(BitmapUtil.getSDImg(this, is));
                addChangePhotoBtn();
            }
            //			File file = PicTools.getFile(AppController.getInstance().getPackageName(), is);
            // 判断图片是否已经保存在本地
            //			if (file != null && (file.getPath().contains(".jpg")||file.getPath().contains(".png"))) {
            //            				TileBitmapDrawable.attachTileBitmapDrawable(image, file.getPath(), placeHolder,
            //            						null);
            //			} else {
            //				setImage(image,is,R.drawable.icon_default,true);
            //			}
        } else {
            ImageUtil.setImage(image, is, R.drawable.ease_default_image, true);
            addChangePhotoBtn();
        }

    }

    protected void setUpActionBar(String title) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.tag_title);
            ((TextView) actionBar.getCustomView().findViewById(R.id.tag_title_title_name)).setText(title);
            findViewById(R.id.tag_title_btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void addChangePhotoBtn() {
        Button changeBtn = new Button(TouchImageViewAct.this);
        changeBtn.setBackgroundColor(Color.parseColor("#55000000"));
        changeBtn.setGravity(Gravity.CENTER);
        changeBtn.setText("更换照片");
        changeBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        changeBtn.setTextColor(Color.WHITE);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TouchImageViewAct.this.setResult(Constants.CHANGE_PIC);
                TouchImageViewAct.this.finish();
            }
        });
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
                                EaseUI.displayMetrics));
        layoutParams.gravity = Gravity.BOTTOM;

        // layoutParams.addRule(RelativeLayout.BELOW, R.id.touch_image_view_sample_image);
        addContentView(changeBtn, layoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
            bitmap=null;
        }
    }
}
