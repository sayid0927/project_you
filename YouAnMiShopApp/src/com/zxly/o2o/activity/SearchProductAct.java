package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.adapter.IMSelectProduceAdapter;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.SearchProductRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

/**
 * Created by dsnx on 2015/12/14.
 */
public class SearchProductAct extends BasicAct implements PullToRefreshBase.OnRefreshListener{

    private PullToRefreshListView listView;
    private IMSelectProduceAdapter adapter;
    private int pageIndex=1;
    private String searchkey;
    private boolean isLastData;
    private TextView btnSearch;
    private EditText txtSearchContent;
    private LoadingView loadingview;
    private View btnDel,btnBack;
    private static ParameCallBack callBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_search);
        listView = (PullToRefreshListView) findViewById(R.id.listview);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        btnSearch= (TextView) findViewById(R.id.btn_search);
        btnBack=findViewById(R.id.btn_back);
        txtSearchContent= (EditText) findViewById(R.id.txt_search_content);
        btnDel= findViewById(R.id.btn_del);
        ViewUtils.setRefreshText(listView);
        listView.setOnRefreshListener(this);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        adapter = new IMSelectProduceAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Gson gson = new Gson();
//                String result = gson.toJson(adapter.getItem(position-1));

//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
                // intent.putArrayListExtra("dataList", dataList);
//                bundle.putString("object", result);
//                intent.putExtras(bundle);
//                setResult(IMMultiPushActivity.SEARCH_PRODUCE_CODE, intent);
                callBack.onCall(adapter.getItem(position-1));
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchkey = txtSearchContent.getText().toString();
                if (!StringUtil.isNull(searchkey)) {
                    pageIndex = 1;
                    loadData(pageIndex, searchkey);
                    keyBoardCancle();
                } else {
                    ViewUtils.showToast("请输入关键词!");
                }
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSearchContent.setText("");
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showKeyboard();
    }


    private void loadData(final int _pageIndex,String searchkey)
    {
        if(StringUtil.isNull(searchkey))
        {
            return ;
        }
        final SearchProductRequest reqeust=new SearchProductRequest(searchkey,_pageIndex);
        reqeust.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                boolean isEmpty = reqeust.getProducts().isEmpty();
                listView.onRefreshComplete();
                if (isEmpty) {
                    if (_pageIndex == 1) {
                        ViewUtils.setGone(listView);
                        loadingview.onDataEmpty("没有搜索到相关内容",R.drawable.img_default_sad);
                    } else {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }

                } else {
                    ViewUtils.setVisible(listView);
                    loadingview.onLoadingComplete();
                    if (_pageIndex == 1) {
                        adapter.clear();
                    }
                    adapter.addItem(reqeust.getProducts());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFail(int code) {
                listView.onRefreshComplete();
                loadingview.onDataEmpty("加载失败");
            }
        });
        if(pageIndex==1)
        {
            loadingview.startLoading();
        }

        reqeust.start(this);
    }

    public static  void start(Activity curAct)
    {
        Intent intent = new Intent(curAct, SearchProductAct.class);
        ViewUtils.startActivityForResult(intent, curAct, IMMultiPushActivity.SEARCH_PRODUCE_CODE);
    }

    public static  void start(Activity curAct, ParameCallBack _callBack)
    {
        callBack = _callBack;
        Intent intent = new Intent(curAct, SearchProductAct.class);
        ViewUtils.startActivityForResult(intent, curAct, IMMultiPushActivity.SEARCH_PRODUCE_CODE);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;

            loadData(pageIndex,searchkey);

        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            // 加载上拉数据
            if (!isLastData) {
                pageIndex++;
                loadData(pageIndex,searchkey);
            } else {
                mMainHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        listView.onRefreshComplete();
                    }
                }, 1000);
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callBack = null;
    }
}
