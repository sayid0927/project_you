package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.request.GetuiMsgClickRequest;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetDefineMsgRequest;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.MGridView;
import com.zxly.o2o.view.MListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/29.
 */
public class ShowDefineGetuiAct extends BasicAct {

    private TextView txt_cotent;
    private ArrayList<String> imgUrls = new ArrayList<String>();
    private MyGridViewAdapter myGridViewAdapter;
    private TextView txt_title;
    private TextView btn_back;
    private String title="";
    private int what;
    private int id;
    private MListView list_view;
    private long dataId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_define_getui);
        getIntentData();
        findView();
        loadData();
    }

    private void getIntentData() {
        dataId = getIntent().getLongExtra("dataId",0);
        what = getIntent().getIntExtra("what", 0);
        id = getIntent().getIntExtra("id", 0);
        markReadMsgRequest(dataId);
    }

    private void loadData() {
        final GetDefineMsgRequest getDefineMsgRequest = new GetDefineMsgRequest(id);
        getDefineMsgRequest.start();
        getDefineMsgRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                String content = getDefineMsgRequest.getContent();
                String imgUrls = getDefineMsgRequest.getImgUrls();

                txt_cotent.setText(content);
                if (!TextUtils.isEmpty(imgUrls)) {
                    dealUrls(imgUrls);
                }

            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private void dealUrls(String imgUrlsStr) {
        if (imgUrlsStr.contains(",")) {
            String[] split = imgUrlsStr.split(",");
            for (int i = 0; i < split.length; i++) {
                imgUrls.add(split[i].toString());
            }
        } else {
            imgUrls.add(imgUrlsStr);
        }
        myGridViewAdapter.addItem(imgUrls);
        myGridViewAdapter.notifyDataSetChanged();

    }

    private void findView() {
        txt_cotent = (TextView) findViewById(R.id.txt_cotent);
        list_view = (MListView) findViewById(R.id.list_view);
        txt_title = (TextView) findViewById(R.id.txt_title);
        btn_back = (TextView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_title.setText("详情");
        if (myGridViewAdapter == null) {
            myGridViewAdapter = new MyGridViewAdapter(ShowDefineGetuiAct.this);
        }
        list_view.setAdapter(myGridViewAdapter);
    }

    class MyGridViewAdapter extends ObjectAdapter {


        public MyGridViewAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_define_getui;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler;
            if (convertView == null) {
                convertView = inflateConvertView();
                viewHodler = new ViewHodler();
                viewHodler.img_getui = (NetworkImageView) convertView.findViewById(R.id.img_getui);
                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            String url = (String) getItem(position);
            viewHodler.img_getui.setImageUrl(url,
                    AppController.imageLoader);

            viewHodler.img_getui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPic(imgUrls, position);
                }
            });
            return convertView;
        }

        class ViewHodler {
            NetworkImageView img_getui;
        }
    }

    public void showPic(List<String> urls, int item) {
        if (urls.size() > item) {
            GalleryViewPagerAct
                    .start(AppController.getInstance().getTopAct(), urls.toArray(new String[urls.size()]),
                            item);
        }
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, ShowDefineGetuiAct.class);
        ViewUtils.startActivity(intent, curAct);

    }

    private void markReadMsgRequest(long dataId) {
        GetuiMsgClickRequest getuiMsgClickRequest = new GetuiMsgClickRequest(dataId);
        getuiMsgClickRequest.start();
    }
}
