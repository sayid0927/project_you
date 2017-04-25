package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.adapter.ActivityProductAdapter;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshGridView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.SearchReqeust;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dsnx on 2015/12/14.
 */
public class SearchProductAct extends BasicAct implements PullToRefreshBase.OnRefreshListener{

    public static final int  TYPE_PRODUCT=1;
    public static final int TYPE_PARTS=2;
    public static final int TYPE_PAD=4;
    private PullToRefreshGridView mGridView;
    private ActivityProductAdapter adapter;
    private int pageIndex=1;
    private String searchkey;
    private boolean isLastData;
    private TextView btnSearch,txtSearchCount;
    private EditText txtSearchContent;
    private LoadingView loadingview;
    private View btnDel,btnBack;
    private int type = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_search_product);
//        type=getIntent().getIntExtra("type",0);
        mGridView= (PullToRefreshGridView) findViewById(R.id.gridView);
        mGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        btnSearch= (TextView) findViewById(R.id.btn_search);
        txtSearchCount= (TextView) findViewById(R.id.txt_search_count);
        btnBack=findViewById(R.id.btn_back);
        txtSearchContent= (EditText) findViewById(R.id.txt_search_content);
        btnDel= findViewById(R.id.btn_del);
        ViewUtils.setRefreshText(mGridView);
        mGridView.setOnRefreshListener(this);
        adapter = new ActivityProductAdapter(this);
        mGridView.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchkey = txtSearchContent.getText().toString();
                Pattern p = Pattern.compile("^[a-zA-Z0-9\\u4e00-\\u9fa5]+$");
                Matcher m = p.matcher(searchkey);
                if(!m.matches() ){
                    ViewUtils.showToast("请输入汉字，字母，数字类");
                    return ;
                }
                if (!StringUtil.isNull(searchkey)) {
                    pageIndex = 1;
                    loadData(pageIndex, searchkey);
                    keyBoardCancle();
                    txtSearchContent.clearFocus();

                } else {
                    ViewUtils.showToast("请输入关键词!");
                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

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
        final SearchReqeust reqeust=new SearchReqeust(type,searchkey,_pageIndex);
        reqeust.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                boolean isEmpty = reqeust.getListProduct().isEmpty();
                mGridView.onRefreshComplete();
                if (isEmpty) {

                    if (_pageIndex == 1) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        loadingview.onDataEmpty("无结果",R.drawable.kb_icon_d);
                    } else {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }

                } else {
                    loadingview.onLoadingComplete();
                    if (_pageIndex == 1) {
                        adapter.clear();
                    }
                    ViewUtils.setText(txtSearchCount,"共"+reqeust.getListProduct().size()+"条搜索结果");
                    adapter.addItem(reqeust.getListProduct());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFail(int code) {
                mGridView.onRefreshComplete();
                loadingview.onDataEmpty("加载失败");
            }
        });
        if(pageIndex==1)
        {
            loadingview.startLoading();
        }

        reqeust.start(this);
    }

    public static  void start(Activity curAct,int type)
    {
        Intent intent = new Intent(curAct, SearchProductAct.class);
        intent.putExtra("type",type);
        ViewUtils.startActivity(intent, curAct);
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
                        mGridView.onRefreshComplete();
                    }
                }, 1000);
            }

        }

    }
}
