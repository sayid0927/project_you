package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zxly.o2o.adapter.SearchArticleAdapter;
import com.zxly.o2o.model.ProductArticles;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.SearchReqeust;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dsnx on 2015/12/14.
 */
public class SearchArticleAct extends BasicAct implements PullToRefreshBase.OnRefreshListener{

    private PullToRefreshListView articleListView;
    private SearchArticleAdapter searchArticleAdapter;
    private int pageIndex=1;
    private String searchkey;
    private boolean isLastData;
    private TextView btnSearch,txtSearchCount;
    private EditText txtSearchContent;
    private LoadingView loadingview;
    private View btnDel,btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_search_article);
        articleListView= (PullToRefreshListView) findViewById(R.id.articel_listview);
        btnSearch= (TextView) findViewById(R.id.btn_search);
        txtSearchContent= (EditText) findViewById(R.id.txt_search_content);
        txtSearchCount= (TextView) findViewById(R.id.txt_search_count);
        btnDel= findViewById(R.id.btn_del);
        btnBack=findViewById(R.id.btn_back);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        ViewUtils.setRefreshText(articleListView);
        searchArticleAdapter=new SearchArticleAdapter(this);
        articleListView.setAdapter(searchArticleAdapter);
        articleListView.setOnRefreshListener(this);
        articleListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
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
                } else {
                    ViewUtils.showToast("请输入关键词!");
                }
            }
        });
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                final ProductArticles pa = (ProductArticles) searchArticleAdapter.getItem(arg2 - 1);
                DiscoverInfoAct.start(SearchArticleAct.this, pa.getId());

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
        final SearchReqeust reqeust=new SearchReqeust(3,searchkey,_pageIndex);
        reqeust.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                boolean isEmpty = reqeust.getListArticle().isEmpty();
                articleListView.onRefreshComplete();
                if (isEmpty) {
                    if (_pageIndex == 1) {
                        searchArticleAdapter.clear();
                        searchArticleAdapter.notifyDataSetChanged();
                        loadingview.onDataEmpty("无结果",R.drawable.kb_icon_d);
                    } else {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }

                } else {
                    loadingview.onLoadingComplete();
                    if (_pageIndex == 1) {
                        searchArticleAdapter.clear();
                    }
                    ViewUtils.setText(txtSearchCount,"共"+reqeust.getListArticle().size()+"条搜索结果");
                    searchArticleAdapter.addItem(reqeust.getListArticle());
                    searchArticleAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFail(int code) {
                articleListView.onRefreshComplete();
                loadingview.onDataEmpty("加载失败");
            }
        });
        if(pageIndex==1)
        {
            loadingview.startLoading();
        }

        reqeust.start(this);
    }
    public static void start(Activity curAct)
    {
        Intent intent = new Intent(curAct, SearchArticleAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        {
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
                            articleListView.onRefreshComplete();
                        }
                    }, 1000);
                }

            }

        }
    }
}
