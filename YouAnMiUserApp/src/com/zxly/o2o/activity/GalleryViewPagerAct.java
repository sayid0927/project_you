package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.easeui.widget.zoomView.GalleryViewPager;
import com.easemob.easeui.widget.zoomView.TouchImageView;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.ImageUtil;
import com.zxly.o2o.util.PicTools;
import com.zxly.o2o.util.ViewUtils;

import java.io.File;

public class GalleryViewPagerAct extends FragmentActivity {
    private static String[] imageUrls;
    private static int curItem;
    private TextView txtIconSum;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageUrls = null;
    }

    public static void start(Activity curAct, String[] urls) {
        start(curAct, urls, 0);
    }

    public static void start(Activity curAct, String[] urls, int _curItem) {
        if (urls != null) {
            imageUrls = urls;
            curItem = _curItem;
            Intent it = new Intent(curAct, GalleryViewPagerAct.class);
            ViewUtils.startActivity(it, curAct);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ease_gallery_view_pager);

        final GalleryViewPager gallery = (GalleryViewPager) findViewById(R.id.gallery_view_pager_gallery);
        txtIconSum = (TextView) findViewById(R.id.txt_icon_sum);
        gallery.setAdapter(new GalleryAdapter());
        gallery.setOffscreenPageLimit(1);
        gallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                ViewUtils.setText(txtIconSum, (index + 1) + "/" + imageUrls.length);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        gallery.setCurrentItem(curItem);
        ViewUtils.setText(txtIconSum, (curItem + 1) + "/" + imageUrls.length);
    }

    private final class GalleryAdapter extends FragmentStatePagerAdapter {


        GalleryAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Fragment getItem(int position) {
            return GalleryFragment.getInstance(imageUrls[position]);
        }


    }

    public static final class GalleryFragment extends Fragment {

        public static GalleryFragment getInstance(String imageId) {
            final GalleryFragment instance = new GalleryFragment();
            final Bundle params = new Bundle();
            params.putString("imageId", imageId);
            instance.setArguments(params);

            return instance;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View v = inflater.inflate(R.layout.ease_gallery_view_pager_item, null);

            final TouchImageView image = (TouchImageView) v.findViewById(R.id.gallery_view_pager_item_image);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

            final String is = getArguments().getString("imageId");

            if (is != null && is.contains("http://") && (is.contains(".jpg") || is.contains(".png"))) {
                ImageUtil.setImage(image, is, R.drawable.icon_default, null);
            } else {
                File file = PicTools.getFile(AppController.getInstance().getPackageName(), is);
                // 判断图片是否已经保存在本地
                if (file != null && (file.getPath().contains(".jpg") || file.getPath().contains(".png"))) {
                    image.setLocalImageBitmap(BitmapUtil.getSDImg(AppController.getInstance(), is));
                } else {
                    ImageUtil.setImage(image, is, R.drawable.icon_default, null);
                }
            }

            return v;
        }

    }
}
