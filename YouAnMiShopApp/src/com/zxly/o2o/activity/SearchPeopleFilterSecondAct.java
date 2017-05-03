package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.adapter.FilterDataAdapter;
import com.zxly.o2o.model.FilterPeopleModel;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.SearchFilterUserRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.TagLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/8/30.
 */
public class SearchPeopleFilterSecondAct extends BasicAct implements View.OnClickListener,PullToRefreshBase.OnRefreshListener {

    private SearchPeopleFilterSecondAct context;
    private TextView tv_buyinshop;
    private TextView tag_cu;
    private TextView tag_cm;
    private PullToRefreshListView listView;
    private LoadingView loadingview;
    private TextView tag_cn;
    private int isFans;
    private TagLayout flowlayout;
    private List<String> types;
    private LinearLayout tv_search;
    private EditText edit_search;
    private int pageIndex;
    private List<FilterPeopleModel> filterPeopleModelList;
    private boolean isLastData;
    private FilterDataAdapter myAdapter;
    private int isReachStore;
    private List<String> operateType=new ArrayList<String>();
    private String filterKey;
    private ImageView iv_close;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_search_fansandmenbers_second);
        context =this;
        getIntentData();
        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();

    }

    private void getIntentData() {
        //1 粉丝  2 会员
        isFans = getIntent().getIntExtra("isFans", 0);
    }

    private void initView() {
        types=new ArrayList<String>();
        tv_buyinshop = (TextView) findViewById(R.id.tv_buyinshop);
        tv_buyinshop.setText(isFans==1?"到店购买过的粉丝":"到店购买过的会员");
        //联通
        tag_cu = (TextView) findViewById(R.id.tag_cu);
        //移动
        tag_cm = (TextView) findViewById(R.id.tag_cm);
        //电信
        tag_cn = (TextView) findViewById(R.id.tag_cn);
        ((TextView)findViewById(R.id.txt_title)).setText("搜索");
        listView = (PullToRefreshListView) findViewById(R.id.win_activity_list);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        flowlayout = (TagLayout) findViewById(R.id.flowlayout);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setIntercept(true);
        ViewUtils.setRefreshText(listView);
        tv_buyinshop.setOnClickListener(this);
        tag_cu.setOnClickListener(this);
        tag_cm.setOnClickListener(this);
        tag_cn.setOnClickListener(this);
        findViewById(R.id.layout_search).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        edit_search = (EditText)findViewById(R.id.edit_search);
        edit_search.setHint("请输入姓名或手机号");
        tv_search = (LinearLayout) findViewById(R.id.tv_search);
        tv_search.setOnClickListener(this);
        listView.setOnRefreshListener(this);
        myAdapter = new FilterDataAdapter(this);
        listView.setAdapter(myAdapter);
        listView.setVisibility(View.GONE);

        iv_close = (ImageView) findViewById(R.id.iv_close);

        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    //如果有选择筛选标签
                    filterKey = edit_search.getText().toString().trim();
                    //当用户既没有输入任何关键字也没有选择任何筛选条件时不做任何请求
                    if(TextUtils.isEmpty(filterKey)&&isReachStore==0&&operateType.size()==0){
                        return true;
                    }
                    showTag();
                    pageIndex = 1;
                    showFilterData(filterKey,pageIndex);
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
                ViewUtils.setVisible(findViewById(R.id.layout_filter));
                ViewUtils.setGone(findViewById(R.id.flowlayout));
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_buyinshop:
                changeBackground(tv_buyinshop);
                break;
            //联通
            case R.id.tag_cu:
                changeBackground(tag_cu);
                break;
            //移动
            case R.id.tag_cm:
                changeBackground(tag_cm);
                break;
            //电信
            case R.id.tag_cn:
                changeBackground(tag_cn);
                break;
            case R.id.tv_search:
                //如果有选择筛选标签
                filterKey = edit_search.getText().toString().trim();
                //当用户既没有输入任何关键字也没有选择任何筛选条件时不做任何请求
                if(TextUtils.isEmpty(filterKey)&&types.size()<=0){
                }else {
                    showTag();
                    pageIndex = 1;
                    showFilterData(filterKey,pageIndex);
                }
                break;
        }
    }

    /**
     * 提交搜索关键字 返回数据
     * @param filterStr
     */
    private void showFilterData(String filterStr, final int page) {
        dealParam(types);
        final SearchFilterUserRequest searchFilterUserRequest = new SearchFilterUserRequest(isReachStore, operateType, filterStr, isFans,page);
        searchFilterUserRequest.start();
        searchFilterUserRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                filterPeopleModelList = searchFilterUserRequest.getFilterPeopleModelList();
                boolean isEmpty = true;
                if (filterPeopleModelList != null && !filterPeopleModelList.isEmpty()) {
                    isEmpty = false;
                }
                listView.setVisibility(View.VISIBLE);
                listView.onRefreshComplete();
                if (isEmpty) {
                    if (page == 1) {
                        ViewUtils.setGone(listView);
                        loadingview.onDataEmpty("没有搜到相关内容",R.drawable.img_default_sad);
                    } else {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }
                } else {
                    loadingview.onLoadingComplete();
                    if (page == 1) {
                        myAdapter.clear();
                    }
                    myAdapter.addItem(filterPeopleModelList);
                    myAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFail(int code) {
                listView.onRefreshComplete();
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

    private void dealParam(List<String> types) {

        if(!types.isEmpty()){
            for (int i = 0; i < types.size(); i++) {
                if(types.get(i).contains("到店购买")){
                    isReachStore=2;
                }else if(types.get(i).equals("联通")){
                    operateType.add(2+"");
                }else if(types.get(i).equals("移动")){
                    operateType.add(1+"");
                }else if(types.get(i).equals("电信")){
                    operateType.add(3+"");
                }
            }
        }
    }

    private void showTag() {
        findViewById(R.id.layout_filter).setVisibility(View.GONE);
        findViewById(R.id.flowlayout).setVisibility(View.VISIBLE);
//        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.rightMargin = 1;
//        lp.bottomMargin = 1;
        flowlayout.removeAllViews();

        for (int i = 0; i < types.size(); i++) {
//            textView = new TextView(this);
            View view = LayoutInflater.from(this).inflate(R.layout.item_searchtag_shape, null, false);
            TextView textView = (TextView) view.findViewById(R.id.tv_tag);
            textView.setText(types.get(i));
            flowlayout.addView(view);
//            container.addView(view);
        }
    }

    private void changeBackground(TextView view) {
        if(types.contains(view.getText().toString())){
            view.setBackgroundResource(R
                    .drawable.bg_tagnormal_shape);
            view.setTextColor(Color.parseColor("#000a14"));
            types.remove(view.getText().toString());
        }else{
            view.setBackgroundResource(R
                    .drawable.bg_tagpress_shape);
            view.setTextColor(Color.parseColor("#ffffff"));
            types.add( view.getText().toString());
        }
    }

    public static  void start(Activity curAct,int isFans)
    {
        Intent intent = new Intent();
        intent.putExtra("isFans",isFans);
        intent.setClass(curAct, SearchPeopleFilterSecondAct.class);
        ViewUtils.startActivity(intent, curAct);
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
}
