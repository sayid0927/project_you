package com.zxly.o2o.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.easemob.easeui.widget.zoomView.TouchImageView;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.ImageUtil;

public class TouchImageViewAct extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ease_touch_image_view);

        final TouchImageView image = (TouchImageView) findViewById(R.id.touch_image_view_sample_image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        final Drawable placeHolder = getResources().getDrawable(R.drawable.ic_launcher);
        String is = getIntent().getStringExtra("file_path");
        if (getIntent().getBooleanExtra("file_is_local", false)) {

            image.setLocalImageBitmap(BitmapUtil.getSDImg(this, is));
            //			File file = PicTools.getFile(AppController.getInstance().getPackageName(), is);
            // 判断图片是否已经保存在本地
            //			if (file != null && (file.getPath().contains(".jpg")||file.getPath().contains(".png"))) {
            //				TileBitmapDrawable.attachTileBitmapDrawable(image, file.getPath(), placeHolder,
            //						null);
            //			} else {
            //				setImage(image,is,R.drawable.icon_default,true);
            //			}
        } else {
            ImageUtil.setImage(image, is, R.drawable.icon_default, null);
        }
    }

}
