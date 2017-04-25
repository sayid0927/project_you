package com.easemob.easeui.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.easemob.chatuidemo.R;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.GetuiTypeMessageAdapter;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.model.GetuiTypeMsg;
import com.easemob.easeui.request.GetuiTypeMsgRequest;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.widget.EaseMyFlipperView;
import com.easemob.easeui.widget.easepullrefresh.EasePullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/7/18.
 * 个推消息列表页面
 */
public class GetuiTypeMessageActivity extends EaseBaseMyListPageActivity{

    private String title;
    private GetuiTypeMessageAdapter getuiTypeMessageAdapter;
    private int typeMsg;
    private static final int REFRENSH = 1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRENSH:
                    page=1;
                    loadData();
                    getuiTypeMessageAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.ease_pull_to_refresh_layout);
        page = 1; //init pageIndex
        title = getIntent().getStringExtra("title");
        //消息类型
        typeMsg = getIntent().getIntExtra("typeMsg", -1);
        setUpActionBar(title);
        setFlipper();
        initListView();
        loadData();
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.em_delete_message, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.delete_conversation){
//            Toast.makeText(this,"dianjile",Toast.LENGTH_SHORT).show();
//        }
//        return true;
//    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, GetuiTypeMessageActivity.class);
        EaseConstant.startActivity(intent, curAct);
    }


    private void initListView() {
        setListView();
        mListView.setMode(EasePullToRefreshBase.Mode.BOTH);
        mListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        mListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        mListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        getuiTypeMessageAdapter = new GetuiTypeMessageAdapter(GetuiTypeMessageActivity.this,handler);
        mListView.setAdapter(getuiTypeMessageAdapter);
//        registerForContextMenu(mListView);
    }

    @Override
    protected void loadData() {
            final GetuiTypeMsgRequest getuiTypeMsgRequest = new GetuiTypeMsgRequest(page, typeMsg);
            getuiTypeMsgRequest.setOnResponseStateListener(new HXNormalRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    if(getuiTypeMsgRequest.getuiTypeMsgList!=null&&getuiTypeMsgRequest.getuiTypeMsgList.size()!=0){
                        if (page == 1) {
                            getuiTypeMessageAdapter.clear();
                            viewContainer.setDisplayedChild(EaseMyFlipperView.LOADSUCCESSFUL, false);
                        }
                        getuiTypeMessageAdapter.addItem(getuiTypeMsgRequest.getuiTypeMsgList);
                    }else{
                        if(page==1){
                            viewContainer.setDisplayedChild(EaseMyFlipperView.NODATA, true);
                            if(EaseConstant.shopID<0){
                                viewContainer.setEmptyImg(R.drawable.img_default_tired,"暂无内容");

                            }
                        }
                        isLastPage=1;
                    }
                    mListView.onRefreshComplete();
                    getuiTypeMessageAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFail(int code) {
                    viewContainer.setDisplayedChild(EaseMyFlipperView.LOADFAIL,false);
                    if(EaseConstant.shopID<0){
                        viewContainer.setFailImg(R.drawable.img_default_shy,"您的手机网络不太顺畅哦!");
                    }
                    mListView.onRefreshComplete();
                }
            });

        getuiTypeMsgRequest.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterForContextMenu(mListView);
    }
}
