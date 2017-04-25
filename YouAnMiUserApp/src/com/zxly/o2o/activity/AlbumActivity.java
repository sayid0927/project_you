package com.zxly.o2o.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zxly.o2o.adapter.SecondHandSelectedPicGVAdapter;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.MyImageManager;
import com.zxly.o2o.util.ParameCallBackById;
import com.zxly.o2o.util.PicTools;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class AlbumActivity extends Activity {
    private static ParameCallBackById callBack;
    private ArrayList<String> dataList = new ArrayList<String>();
    private HashMap<String, ImageView> hashMap = new HashMap<String, ImageView>();
    private ArrayList<String> selectedDataList = new ArrayList<String>();
    ;
    private ProgressBar progressBar;
    private SecondHandSelectedPicGVAdapter gridImageAdapter;
    private LinearLayout selectedImageLayout;
    private Button okButton;
    private HorizontalScrollView scrollview;
    private int MaxSelectedSize = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        selectedDataList = (ArrayList<String>) getIntent().getSerializableExtra("dataList");
        setUpActionBar("全部照片");
        init();
        initListener();

    }

    private void init() {

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        GridView mGridView = (GridView) findViewById(R.id.myGrid);
        gridImageAdapter = new SecondHandSelectedPicGVAdapter(this, dataList, selectedDataList = selectedDataList == null ? new ArrayList<String>() : selectedDataList);
        mGridView.setAdapter(gridImageAdapter);
        refreshData();
        selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
        okButton = (Button) findViewById(R.id.ok_button);
        scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);

        initSelectImage();

    }

    private void initSelectImage() {
        if (selectedDataList == null)
            return;
        for (final String path : selectedDataList) {
            if(path.contains("_default")){
                selectedDataList.remove(path);
                continue;
            }
            ImageView imageView = (ImageView) LayoutInflater.from(AlbumActivity.this).inflate(R.layout.choose_imageview, selectedImageLayout, false);
            selectedImageLayout.addView(imageView);
            hashMap.put(path, imageView);
            MyImageManager.from(AlbumActivity.this).displayImage(imageView, path, R.drawable.pic_normal, 100, 100);
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removePath(path);
                    gridImageAdapter.notifyDataSetChanged();
                }
            });
        }
        okButton.setText("完成(" + selectedDataList.size() + "/" + MaxSelectedSize + ")");
    }

    private void initListener() {

        gridImageAdapter.setOnItemClickListener(new SecondHandSelectedPicGVAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final ToggleButton toggleButton, int position, final String path, boolean isChecked) {

                if(position==0){
                   setResult(Constants.GET_PIC_FROM_CAMERA);
                   finish();
                }else{
                if (selectedDataList.size() >= MaxSelectedSize) {
                    toggleButton.setChecked(false);
                    if (!removePath(path)) {
                        ViewUtils.showToast("只能选择" + MaxSelectedSize + "张图片");
                    }
                    return;
                }

                if (isChecked) {
                    if (!hashMap.containsKey(path)) {
                        ImageView imageView = (ImageView) LayoutInflater.from(AlbumActivity.this).inflate(R.layout.choose_imageview, selectedImageLayout, false);
                        selectedImageLayout.addView(imageView);
                        imageView.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                int off = selectedImageLayout.getMeasuredWidth() - scrollview.getWidth();
                                if (off > 0) {
                                    scrollview.smoothScrollTo(off, 0);
                                }
                            }
                        }, 100);

                        hashMap.put(path, imageView);
                        selectedDataList.add(path);
                        MyImageManager.from(AlbumActivity.this).displayImage(imageView, path, R.drawable.pic_normal, 100, 100);
                        imageView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                toggleButton.setChecked(false);
                                removePath(path);

                            }
                        });
                        okButton.setText("完成(" + selectedDataList.size() + "/" + MaxSelectedSize + ")");
                    }
                } else {
                    removePath(path);
                }

            }}
        });

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("","======="+callBack);
                if (callBack != null) {
                    callBack.onCall(0, selectedDataList);
                } else {
                    Log.e("++++++++++++++++++++++","-----------======="+callBack);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    // intent.putArrayListExtra("dataList", dataList);
                    bundle.putStringArrayList("dataList", selectedDataList);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                }
                finish();

            }
        });

    }

    private boolean removePath(String path) {
        if (hashMap.containsKey(path)) {
            selectedImageLayout.removeView(hashMap.get(path));
            hashMap.remove(path);
            removeOneData(selectedDataList, path);
            okButton.setText("完成(" + selectedDataList.size() + "/" + MaxSelectedSize + ")");
            return true;
        } else {
            return false;
        }
    }

    private void removeOneData(ArrayList<String> arrayList, String s) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals(s)) {
                arrayList.remove(i);
                return;
            }
        }
    }

    private void refreshData() {

        new AsyncTask<Void, Void, ArrayList<String>>() {

            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected ArrayList<String> doInBackground(Void... params) {
                ArrayList<String> tmpList = new ArrayList<String>();

                if (tmpList.size() == 0) {
                    tmpList = PicTools.queryGallery(AlbumActivity.this);
                }
                return tmpList;
            }

            protected void onPostExecute(ArrayList<String> tmpList) {

                if (AlbumActivity.this.isFinishing()) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
                dataList.clear();
                dataList.add("_camera");
                for (int i = 0; i < tmpList.size(); i++) {
                    dataList.add(tmpList.get(i));
                }
                gridImageAdapter.notifyDataSetChanged();

            }

        }.execute();

    }


    @Override
    public void onBackPressed() {
     //   callBack.onCall(SecondhandInputFragment.GET_PIC, selectedDataList);
        finish();
        // super.onBackPressed();
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        // ImageManager2.from(AlbumActivity.this).recycle(dataList);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public static void start(Activity activity, Intent intent, ParameCallBackById _callBack) {
        callBack = _callBack;
        activity.startActivityForResult(intent, Constants.GET_PIC_FROM_CAMERA);
        activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

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
                 //   callBack.onCall(SecondhandInputFragment.GET_PIC, selectedDataList);
                    finish();
                }
            });
        }
    }

}
