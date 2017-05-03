package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxly.o2o.adapter.SysMsgAdater;
import com.zxly.o2o.dialog.ShareDialog;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.DisCountSystemMsgDeTailRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.BitmapUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2016/6/13.
 */
public class SystemMsgAct extends BasicAct implements View.OnClickListener,PullToRefreshBase.OnRefreshListener{
    private View btnBack,btnPromotionNormal;
    private PullToRefreshListView mListView;
    private LoadingView loadingView;
    private int pageIndex;
    private boolean isLastData;
    private SysMsgAdater sysMsgAdater;
    private ShareDialog shareDialog;
    private String promotionDesc="",serverURL="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_sys_msg);
        btnBack=findViewById(R.id.btn_back);
        btnPromotionNormal=findViewById(R.id.btn_tuiguang);
        mListView= (PullToRefreshListView) findViewById(R.id.listview);
        loadingView= (LoadingView) findViewById(R.id.view_loading);
        btnBack.setOnClickListener(this);
        btnPromotionNormal.setOnClickListener(this);
        sysMsgAdater=new SysMsgAdater(this);
        mListView.setAdapter(sysMsgAdater);
        ViewUtils.setRefreshText(mListView);
        mListView.setDivideHeight(0);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mListView.setOnRefreshListener(this);
        pageIndex=1;
        loadingView.startLoading();
        loadData(pageIndex);
    }

    @Override
    public void onClick(View v) {
        if(v==btnBack)
        {

            finish();
        }else if(v==btnPromotionNormal){
            if(shareDialog==null)
            {
                shareDialog=new ShareDialog();
            }
           Object shareImage= BitmapUtil.drawableToBitmap(getResources().getDrawable(R.drawable.ddyh_icon));

            shareDialog.show("到店优惠享不停！",promotionDesc,serverURL , shareImage,null);
        }
    }
    public static void  start(Activity curAct)
    {
        Intent it=new Intent();
        it.setClass(curAct,SystemMsgAct.class);
        ViewUtils.startActivity(it,curAct);
    }
    private void loadData(final int pageIndex)
    {
        int pageSize=10;
        switch (pageIndex)
        {
            case 1:
                pageSize=10;
                break;
            case 3:
                pageSize=5;
                isLastData=true;
                break;


        }
        final DisCountSystemMsgDeTailRequest disCountSystemMsgDeTailRequest = new DisCountSystemMsgDeTailRequest(pageIndex, pageSize);
        disCountSystemMsgDeTailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                mListView.onRefreshComplete();
                loadingView.onLoadingComplete();
                promotionDesc=disCountSystemMsgDeTailRequest.getPromotionDesc();
                serverURL=disCountSystemMsgDeTailRequest.getServerURL();
                boolean isEmpty = disCountSystemMsgDeTailRequest.getGiftGetInfoList().isEmpty();
                if (isEmpty) {
                    if (pageIndex > 1) {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }
                } else {
                    if (pageIndex == 1) {
                        ViewUtils.setVisible(findViewById(R.id.linear2));
                        sysMsgAdater.clear();
                    }
                    sysMsgAdater.addItem(disCountSystemMsgDeTailRequest.getGiftGetInfoList(),true);
                }
                if(disCountSystemMsgDeTailRequest.hasNext){
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                } else {
                    mListView.setMode(PullToRefreshBase.Mode.DISABLED);
                }
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingComplete();
                mListView.onRefreshComplete();
            }
        });
        disCountSystemMsgDeTailRequest.start(this);
    }
    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;
            loadData(pageIndex);

        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            // 加载上拉数据
            if (!isLastData) {
                pageIndex=pageIndex+2;
                loadData(pageIndex);
            } else {
                mMainHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mListView.onRefreshComplete();
                    }
                }, 1000);
            }

        }
    }
}
