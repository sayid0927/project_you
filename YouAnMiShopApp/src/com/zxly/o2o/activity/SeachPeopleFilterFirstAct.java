package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.o2o.adapter.FilterDataAdapter;
import com.zxly.o2o.model.FilterPeopleModel;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.SearchFilterUserRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/8/29.
 * 筛选页面（首页点击搜索）
 */
public class SeachPeopleFilterFirstAct extends BasicAct implements PullToRefreshBase.OnRefreshListener,View.OnClickListener{

    private TextView fans;
    private TextView menber;
    private PullToRefreshListView listView;
    private LoadingView loadingview;
    private SeachPeopleFilterFirstAct context;
    private TextView btn_search;
    private EditText edit_search;
    private List<FilterPeopleModel> filterPeopleModelList=new ArrayList<FilterPeopleModel>();
    private FilterDataAdapter myAdapter;
    private int pageIndex;
    private String filterKey;
    private boolean isLastData;
    private RelativeLayout tag_layout;
    private boolean showTagLayout;
    private ImageView iv_close;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_search_fansandmenbers_first);
        context =this;
        showTagLayout =getIntent().getBooleanExtra("showTagLayout",false);
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        UmengUtil.onEvent(SeachPeopleFilterFirstAct.this,new UmengUtil().SEARCH_ENTER,null);
    }

    private void initView() {
        fans = (TextView) findViewById(R.id.tag_fans_search);
        menber = (TextView) findViewById(R.id.tag_menber_search);
        tag_layout = (RelativeLayout) findViewById(R.id.tag_layout);
        ((TextView)findViewById(R.id.txt_title)).setText("搜索");
        listView = (PullToRefreshListView) findViewById(R.id.win_activity_goods_list);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setIntercept(true);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        ViewUtils.setRefreshText(listView);
        if(showTagLayout){
            //当从线下录入粉丝进入时  初始状态不需要显示粉丝会员标签布局
            ViewUtils.setGone(tag_layout);
        }
        fans.setOnClickListener(this);
        menber.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        edit_search = (EditText) findViewById(R.id.edit_search);
        edit_search.setHint("请输入姓名或手机号");
        //搜索
        btn_search = (TextView) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
//        edit_search.clearFocus();
//        edit_search.setSelected(false);
        listView.setOnRefreshListener(this);
        myAdapter = new FilterDataAdapter(this);
        listView.setAdapter(myAdapter);
        listView.setVisibility(View.GONE);
        iv_close = (ImageView) findViewById(R.id.iv_close);


        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_SEARCH){
                    filterKey = edit_search.getText().toString().trim();
                    if(TextUtils.isEmpty(filterKey)){
                        return true;
                    }
                    showFilterData(filterKey,1);
                }
                return false;
            }
        });

        edit_search.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    iv_close.setVisibility(View.VISIBLE);
                } else {
                    iv_close.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_search.getText().clear();
                hideSoftKeyboard();
                ViewUtils.setGone(listView);
                loadingview.setVisibility(View.GONE);
                ViewUtils.setVisible(tag_layout);
            }
        });
    }

    protected void hideSoftKeyboard() {
        if (this.getWindow().getAttributes().softInputMode !=
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (this.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        {
            if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                pageIndex = 1;

                showFilterData(filterKey,pageIndex);

            } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                // 加载上拉数据
                if (!isLastData) {
                    pageIndex++;
                    showFilterData(filterKey,pageIndex);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                UmengUtil.onEvent(SeachPeopleFilterFirstAct.this,new UmengUtil().SEARCH_BACK_CLICK,null);
                break;
            case R.id.tag_fans_search:
                SearchPeopleFilterSecondAct.start(SeachPeopleFilterFirstAct.this,1);

                break;
            case R.id.tag_menber_search:
                SearchPeopleFilterSecondAct.start(SeachPeopleFilterFirstAct.this,2);
                break;
            case R.id.btn_search:
                filterKey = edit_search.getText().toString().trim();
                if(TextUtils.isEmpty(filterKey)){
                    return;
                }
                    showFilterData(filterKey,1);
                UmengUtil.onEvent(SeachPeopleFilterFirstAct.this,new UmengUtil().SEARCH_BT_CLICK,null);
                break;
            default:
                break;
        }
    }

    /**
     * 提交搜索关键字 返回数据
     * @param filterStr
     */
    private void showFilterData(String filterStr, final int page) {
        final SearchFilterUserRequest searchFilterUserRequest = new SearchFilterUserRequest(0, new ArrayList<String>(), filterStr, -1,page);
        if(showTagLayout){
            searchFilterUserRequest.addParams("isOffLineFans",2);
        }
        searchFilterUserRequest.start();
        searchFilterUserRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                filterPeopleModelList = searchFilterUserRequest.getFilterPeopleModelList();
                boolean isEmpty = filterPeopleModelList.isEmpty();
                listView.onRefreshComplete();
                loadingview.onLoadingComplete();
                ViewUtils.setGone(tag_layout);

                listView.setVisibility(View.VISIBLE);
                if (isEmpty) {

                    if (page == 1) {
                        ViewUtils.setGone(listView);
                        loadingview.onDataEmpty("没有搜到相关内容",R.drawable.img_default_sad);
                    } else {
                        isLastData = true;

                        ViewUtils.showToast("亲! 没有更多了");
                    }

                } else {

                    if (page == 1) {
                        myAdapter.clear();
                    }
                    myAdapter.addItem(filterPeopleModelList);
                    myAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFail(int code) {
                ViewUtils.setGone(tag_layout);
                listView.onRefreshComplete();
//                loadingview.onDataEmpty("加载失败");
                loadingview.onLoadingFail("您的手机网络不太顺畅哦!",R.drawable.img_default_shy);
            }
        });

        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingview.startLoading();
                searchFilterUserRequest.start();
            }
        });
    }


    public static  void start(Activity curAct)
    {
        Intent intent = new Intent();
        intent.setClass(curAct, SeachPeopleFilterFirstAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    public static  void start(Activity curAct,boolean showTagLayout)
    {
        Intent intent = new Intent();
        intent.putExtra("showTagLayout",showTagLayout);
        intent.setClass(curAct, SeachPeopleFilterFirstAct.class);
        ViewUtils.startActivity(intent, curAct);
    }
}
