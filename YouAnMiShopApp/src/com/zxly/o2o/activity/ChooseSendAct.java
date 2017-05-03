package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.easemob.easeui.widget.viewpagerindicator.ViewPageFragmentAdapter;
import com.zxly.o2o.fragment.SendProductFragment;
import com.zxly.o2o.fragment.SendShopArticleFragment;
import com.zxly.o2o.fragment.UserDefinedSendFragment;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.PicTools;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MPagerSlidingTab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/5.
 * 选择推送内容
 */
public class ChooseSendAct extends BasicAct implements View.OnClickListener {
    private MPagerSlidingTab tabs;
    List<Fragment> fragments;
    private int curTab;
    private Uri fileUri;
    private File photo;
    private int actionType;
    private ViewPager pager;
    private UserDefinedSendFragment userDefinedSendFragment;
    private SendProductFragment sendProductFragment;
    private SendShopArticleFragment sendShopArticleFragment;
    public List<String> fansImeis;
    public List<Long> userIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.win_choose_sendcontent);

        fansImeis = (List<String>) getIntent().getSerializableExtra("fansImeis");
        userIds = (List<Long>) getIntent().getSerializableExtra("userIds");
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.txt_title)).setText("选择推送内容");
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (MPagerSlidingTab) findViewById(R.id.tabs);
        fragments = new ArrayList<Fragment>();
        userDefinedSendFragment = UserDefinedSendFragment.newInstance();
        sendProductFragment = SendProductFragment.newInstance();
        sendShopArticleFragment = SendShopArticleFragment.newInstance();
        fragments.add(userDefinedSendFragment);
        fragments.add(sendProductFragment);
        fragments.add(sendShopArticleFragment);

        String strings[] = {"自定义", "商品", "店铺文章"};
        pager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager(), fragments, strings));
        tabs.setViewPager(pager);
        setTabsValue();
        curTab=0;
        pager.setCurrentItem(curTab);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                keyBoardCancle();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线颜色
        tabs.setDividerColor(getResources().getColor(R.color.white));
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
//        // 设置Tab Indicator的高度
        tabs.setTextColor(Color.parseColor("#333333"));
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, getResources().getDisplayMetrics()));
//        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#ff5f19"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#ff5f19"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(R.color.transparent);
        tabs.initTabStyles();
    }

    public static void start(Activity curAct){
        Intent it=new Intent();
        it.setClass(curAct, ChooseSendAct.class);
        ViewUtils.startActivity(it, curAct);
    }

    public static void start(Activity curAct, ArrayList<String> fansImeis, ArrayList<Long> userIds){
        Intent it=new Intent();
        it.setClass(curAct, ChooseSendAct.class);
        it.putExtra("fansImeis", fansImeis);
        it.putExtra("userIds", userIds);
        ViewUtils.startActivity(it, curAct);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        UserDefinedSendFragment.newInstance().onActivityResult(requestCode, resultCode, data);

        actionType = requestCode;
        switch (requestCode) {
            case Constants.GET_PIC_FROM_CELLPHONE:

                if (data == null) {
                    mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                    ViewUtils.showToast("图片获取失败!");
                    actionType = -1;
                    return;
                }
                fileUri = data.getData();
                if (fileUri != null) {
                    photo = PicTools.getOutputPhotoFile(true, "");
                    resizeImage();
                } else {
                    mMainHandler.sendEmptyMessage(Constants.DELETE_PIC);
                }
                break;
            case Constants.GET_PIC_FROM_CAMERA:
                if(Constants.GET_PIC_FROM_CAMERA == resultCode){
                    userDefinedSendFragment.takePhoto(requestCode,resultCode,data);
                }
                break;
            case IMMultiPushActivity.SEARCH_PRODUCE_CODE:
                sendProductFragment.resetSearchData(requestCode, resultCode, data);
                break;
            case IMMultiPushActivity.SEARCH_ACTICLE_CODE:
                sendShopArticleFragment.resetSearchData(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void handleMessage(Message msg) {

        super.handleMessage(msg);

        switch (msg.what) {
            case Constants.GET_PIC_FROM_CELLPHONE: // 解决4.4以上的版本图片url获取不到
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    startActivityForResult(intent, Constants.GET_PIC_FROM_CELLPHONE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    startActivityForResult(intent, Constants.GET_PIC_FROM_CELLPHONE);
                }
                break;

            case Constants.GET_PIC_FROM_CAMERA:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photo = PicTools.getOutputPhotoFile(true, "");
                fileUri = Uri.fromFile(photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, Constants.GET_PIC_FROM_CAMERA);
                break;

        }
    }

    public void resizeImage() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(fileUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX",85);
        intent.putExtra("aspectY", 85);
        intent.putExtra("outputX", 85 * 7);
        intent.putExtra("outputY", 85 * 7);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, Constants.RESULT_PICTURE);
    }
}
