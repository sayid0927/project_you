package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxly.o2o.adapter.YamCourseAdapter;
import com.zxly.o2o.model.CollegeCourse;
import com.zxly.o2o.model.CourseType;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CourseListRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.List;

/**
 * @author fengrongjian 2016-8-24
 * @description 柚安米商学院
 */
public class YamCollegeListAct extends BasicAct implements View.OnClickListener, PullToRefreshBase.OnRefreshListener {
    private Context context;
    private LoadingView loadingView;
    private EditText editSearch;
    private TextView txtCourseType, btnCancel, btnMore;
    private PullToRefreshListView listView;
    private View headerView, contentLayout, viewSearch;
    private YamCourseAdapter adapter;
    private List<CollegeCourse> collegeCourseList;
    private int type;//1.全部教程；2.最新教程； 3.搜索教程
    private int pageIndex = 1;
    private CourseType courseType;
    private String articleName;
    private long articleTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_yam_college);
        context = this;
        type = getIntent().getIntExtra("type", 0);
        articleName = getIntent().getStringExtra("articleName");
        articleTypeId = getIntent().getLongExtra("articleTypeId", 0);
        initViews();

        if (type == Constants.YAM_COURSE_NEW) {
            loadData(pageIndex);
        } else if (type == Constants.YAM_COURSE_ALL) {
            loadData(pageIndex);
        } else if (type == Constants.YAM_COURSE_SEARCH) {
            loadData(pageIndex);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public static void start(Activity curAct, int type) {
        Intent intent = new Intent(curAct, YamCollegeListAct.class);
        intent.putExtra("type", type);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, int type, String articleName, long articleTypeId) {
        Intent intent = new Intent(curAct, YamCollegeListAct.class);
        intent.putExtra("type", type);
        intent.putExtra("articleName", articleName);
        intent.putExtra("articleTypeId", articleTypeId);
        ViewUtils.startActivity(intent, curAct);
    }

    ParameCallBack callBack = new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            if (object instanceof CourseType) {
                courseType = (CourseType) object;
            } else {
                articleName = (String) object;
            }
            loadData(pageIndex);
            finish();
        }
    };

    private void initViews() {
        findViewById(R.id.view_header).setFocusable(true);
        findViewById(R.id.view_header).requestFocus();
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "柚安米商学院");
        contentLayout = findViewById(R.id.content_layout);

        loadingView = (LoadingView) findViewById(R.id.view_loading);

        listView = (PullToRefreshListView) findViewById(R.id.list_view);
        listView.setIntercept(true);
        ViewUtils.setRefreshText(listView);
        if (type == Constants.YAM_COURSE_NEW) {
            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            listView.setMode(PullToRefreshBase.Mode.BOTH);
        }
        listView.setOnRefreshListener(this);
        LayoutInflater layoutInflater =
                (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        headerView = layoutInflater.inflate(R.layout.head_college_course, null);
        viewSearch =headerView.findViewById(R.id.view_search);
        viewSearch.setOnClickListener(this);
        editSearch = (EditText) headerView.findViewById(R.id.edit_search);
        editSearch.setOnClickListener(this);
        editSearch.setFocusable(false);
        editSearch.setFocusableInTouchMode(false);

        btnCancel = (TextView) headerView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        btnMore = (TextView) headerView.findViewById(R.id.btn_more);
        btnMore.setOnClickListener(this);
        txtCourseType = (TextView) headerView.findViewById(R.id.txt_course_type);

        adapter = new YamCourseAdapter(context, YamCollegeListAct.this);

        if (type == Constants.YAM_COURSE_NEW) {
            ViewUtils.setText(txtCourseType, "最新教程");
            listView.addH(headerView);
        } else if (type == Constants.YAM_COURSE_ALL) {
            ViewUtils.setText(txtCourseType, "全部教程");
            ViewUtils.setGone(btnMore);
            listView.addH(headerView);
        } else if (type == Constants.YAM_COURSE_SEARCH) {
        }
        listView.setAdapter(adapter);
    }

    private void loadData(final int pageId) {
        final CourseListRequest courseListRequest = new CourseListRequest(articleName, articleTypeId, pageId, 10);
        courseListRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                ViewUtils.setVisible(contentLayout);
                collegeCourseList = courseListRequest.getCollegeCourseList();
                if (!DataUtil.listIsNull(collegeCourseList)) {
                    if (pageId == 1) {
                        adapter.clear();
                    }
                    adapter.addItem(collegeCourseList, true);
                    pageIndex++;
                    loadingView.onLoadingComplete();
                } else {
                    if (pageId == 1) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        if (type == Constants.YAM_COURSE_SEARCH) {
                            loadingView.onDataEmpty("没有搜索到相关内容",R.drawable.img_default_sad);
                        } else {
                            loadingView.onDataEmpty("暂无内容",R.drawable.img_default_tired);
                        }
                    } else {
                        ViewUtils.showToast("最后一页了");
                        loadingView.onLoadingComplete();
                    }
                }
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingFail();
                if (listView.isRefreshing()) {
                    listView.onRefreshComplete();
                }
            }
        });
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                loadingView.startLoading();
                courseListRequest.start(this);
            }
        });
        if (!listView.isRefreshing()) {
            loadingView.startLoading();
        }
        courseListRequest.start(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_more:
                YamCollegeListAct.start(YamCollegeListAct.this, Constants.YAM_COURSE_ALL);
                break;
            case R.id.view_search:
            case R.id.edit_search:
                YamCollegeSearchAct.start(YamCollegeListAct.this, callBack);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            pageIndex = 1;
            loadData(pageIndex);
        }
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            loadData(pageIndex);
        }
    }


}
