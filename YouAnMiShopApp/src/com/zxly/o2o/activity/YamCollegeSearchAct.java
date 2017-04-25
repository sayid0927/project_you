package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.CourseType;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CourseTypeRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.HashMap;
import java.util.List;

/**
 * @author fengrongjian 2016-9-13
 * @description 柚安米商搜索
 */
public class YamCollegeSearchAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private LoadingView loadingview;
    private EditText editSearch;
    private TextView btnCancel;
    private GridView gridView;
    private View contentLayout;
    private CourseTypeAdapter courseTypeAdapter;
    private List<CourseType> courseTypeList;
    static ParameCallBack callBack;
    private long selectArticleId;
    private String articleTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_yam_college_search);
        context = this;
        initViews();
        loadType();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public static void start(Activity curAct, ParameCallBack _callBack) {
        callBack = _callBack;
        Intent intent = new Intent(curAct, YamCollegeSearchAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.view_header).setFocusable(true);
        findViewById(R.id.view_header).requestFocus();
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "柚安米商学院");
        contentLayout = findViewById(R.id.content_layout);
        loadingview = (LoadingView) findViewById(R.id.view_loading);

        editSearch = (EditText) findViewById(R.id.edit_search);
        editSearch.setHint("请输入课程名称或选择课程类型");
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchKey = editSearch.getText().toString().trim();
                    if (StringUtil.isNull(searchKey) && StringUtil.isNull(articleTypeName)) {
                        ViewUtils.showToast("搜索条件为空");
                    } else {
                        YamCollegeListAct.start(YamCollegeSearchAct.this, Constants.YAM_COURSE_SEARCH, editSearch.getText().toString().trim(), selectArticleId);
                    }
                    return true;
                }
                return false;
            }
        });

        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        ViewUtils.setText(btnCancel, "搜索");
        btnCancel.setOnClickListener(this);
        ViewUtils.setVisible(btnCancel);

        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                keyBoardCancle();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        courseTypeAdapter = new CourseTypeAdapter(context);
        gridView.setAdapter(courseTypeAdapter);
    }

    private void loadType(){
        final CourseTypeRequest courseTypeRequest = new CourseTypeRequest(Account.user.getShopId());
        courseTypeRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                ViewUtils.setVisible(contentLayout);
                courseTypeList = courseTypeRequest.getCourseTypeList();
                courseTypeAdapter.clear();
                courseTypeAdapter.addItem(courseTypeList);
                courseTypeAdapter.initSelectMap();
                courseTypeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(int code) {
            }
        });
        courseTypeRequest.start(YamCollegeSearchAct.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_more:
                YamCollegeListAct.start(YamCollegeSearchAct.this, Constants.YAM_COURSE_ALL);
                break;
            case R.id.btn_cancel:
                String searchKey = editSearch.getText().toString().trim();
                if (StringUtil.isNull(searchKey) && StringUtil.isNull(articleTypeName)) {
                    ViewUtils.showToast("搜索条件为空");
                    return;
                }
//                YamCollegeSearchAct.this.finish();
                YamCollegeListAct.start(YamCollegeSearchAct.this, Constants.YAM_COURSE_SEARCH, searchKey, selectArticleId);
                break;
        }
    }

    class CourseTypeAdapter extends ObjectAdapter {
        private HashMap<Integer, Boolean> typeMap = new HashMap<Integer, Boolean>();

        public CourseTypeAdapter(Context context) {
            super(context);
        }

        public void initSelectMap() {
            for (int i = 0; i < getContent().size(); i++) {
                typeMap.put(i, false);
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflateConvertView();
                holder = new ViewHolder();
                holder.txtCourseType = (TextView) convertView
                        .findViewById(R.id.btn_course_type);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final CourseType courseType = (CourseType) getItem(position);
            ViewUtils.setText(holder.txtCourseType, courseType.getTypeName());
            if(typeMap.get(position)){
                holder.txtCourseType.setBackgroundResource(R.drawable.bg_tagpress_shape);
                holder.txtCourseType.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.txtCourseType.setBackgroundResource(R.drawable.bg_tagnormal_shape);
                holder.txtCourseType.setTextColor(context.getResources().getColor(R.color.gray_333333));
            }
            final long typeId = courseType.getTypeId();
            final String typeName = courseType.getTypeName();
            holder.txtCourseType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(typeMap.get(position)){
                        selectArticleId = 0;
                        articleTypeName = "";
                        initSelectMap();
                    } else {
                        selectArticleId = courseType.getTypeId();
                        articleTypeName = courseType.getTypeName();
                        initSelectMap();
                        typeMap.put(position, true);
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_course_type;
        }

        class ViewHolder {
            TextView txtCourseType;
        }

    }

}
