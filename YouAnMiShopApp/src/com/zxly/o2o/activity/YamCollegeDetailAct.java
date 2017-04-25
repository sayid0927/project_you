package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.widget.MyWebView;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.CollegeCourse;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CourseDetailRequest;
import com.zxly.o2o.request.CourseNewestRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fengrongjian 2016-8-31
 * @description 柚安米商学院详细页
 */
public class YamCollegeDetailAct extends BasicAct implements View.OnClickListener {
    private Context context;
    private LoadingView loadingview;
    private ListView listView;
    private View headerView;
    private List<CollegeCourse> collegeCourseList;
    private long articleId;
    private TextView txtLabel1, txtLabel2;
    private MyWebView webView;
    private MyAdapter myAdapter;
    private String htmlContent = "";
    private String videoUrl;
    private ArrayList<String> videoUrlList = new ArrayList<String>();
    private ArrayList<Object> objectArrayList = new ArrayList<Object>();
    private boolean hasVideo = false;
    private String regEx="(http{1})(\\S+)(.mp4)";

    private String testUrl = "http://gslb.miaopai.com/stream/3D~8BM-7CZqjZscVBEYr5g__.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_course_detail);
        context = this;
        initViews();
        articleId = getIntent().getLongExtra("articleId", 0);
        if (0 != articleId) {
            loadData();
        }
    }

    private boolean findVideoUrlAndReplaceHtmlContent(String html){
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);

        if (m.find()) {
            if (!"".equals(m.group())) {
                hasVideo = true;
                videoUrl = m.group();
                videoUrlList.add(videoUrl);
                htmlContent = html.replace(videoUrl, "");
                findVideoUrlAndReplaceHtmlContent(htmlContent);
            }
        }
        return hasVideo;
    }

    public static void start(Activity curAct, long articleId) {
        Intent intent = new Intent(curAct, YamCollegeDetailAct.class);
        intent.putExtra("articleId", articleId);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ViewUtils.setText(findViewById(R.id.txt_title), "柚安米商学院");
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        listView = (ListView) findViewById(R.id.list_view);
        LayoutInflater layoutInflater =
                (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        headerView = layoutInflater.inflate(R.layout.head_course_detail, null);
        txtLabel1 = (TextView) headerView.findViewById(R.id.txt_label1);
        txtLabel2 = (TextView) headerView.findViewById(R.id.txt_label2);
        listView.addHeaderView(headerView);
        webView = (MyWebView) headerView.findViewById(R.id.web_view);
        myAdapter = new MyAdapter(context);
        listView.setAdapter(myAdapter);
    }

    private void loadData() {
        final CourseDetailRequest courseDetailRequest = new CourseDetailRequest(articleId);
        courseDetailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                loadingview.onLoadingComplete();
                CollegeCourse collegeCourse = courseDetailRequest.getCollegeCourse();

                htmlContent = collegeCourse.getContent();
                if (findVideoUrlAndReplaceHtmlContent(htmlContent)) {
                    objectArrayList.addAll(videoUrlList);
                }

                ViewUtils.setText(headerView.findViewById(R.id.txt_course_title), collegeCourse.getTitle());
                if (!StringUtil.isNull(htmlContent)) {
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setSupportZoom(false);
                    webView.getSettings().setBuiltInZoomControls(false);
                    webView.getSettings().setUseWideViewPort(true);
                    webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.loadData(StringUtil.getStyleHtml(htmlContent), "text/html; charset=UTF-8", null);
                }

                ArrayList<String> lable = collegeCourse.getLable();
                if (DataUtil.listIsNull(lable)) {
                    ViewUtils.setGone(txtLabel1);
                    ViewUtils.setGone(txtLabel2);
                } else {
                    if (lable.size() == 1) {
                        if(!StringUtil.isNull(lable.get(0))){
                            ViewUtils.setVisible(txtLabel1);
                            ViewUtils.setText(txtLabel1, lable.get(0));
                        } else {
                            ViewUtils.setGone(txtLabel1);
                        }
                        ViewUtils.setGone(txtLabel2);
                    } else if (lable.size() == 2) {
                        if(!StringUtil.isNull(lable.get(0))){
                            ViewUtils.setVisible(txtLabel1);
                            ViewUtils.setText(txtLabel1, lable.get(0));
                        } else {
                            ViewUtils.setGone(txtLabel1);
                        }
                        if(!StringUtil.isNull(lable.get(1))){
                            ViewUtils.setVisible(txtLabel2);
                            ViewUtils.setText(txtLabel2, lable.get(1));
                        } else {
                            ViewUtils.setGone(txtLabel2);
                        }
                    }
                }
                ViewUtils.setText(headerView.findViewById(R.id.txt_study_count), collegeCourse.getReadAmount());
                ViewUtils.setText(headerView.findViewById(R.id.txt_time), StringUtil.getDateByMillis(collegeCourse.getCreateTime()));

                loadListData();
            }

            @Override
            public void onFail(int code) {
                loadingview.onLoadingFail();
            }
        });
        loadingview.setOnAgainListener(new LoadingView.OnAgainListener() {

            @Override
            public void onLoading() {
                loadingview.startLoading();
                courseDetailRequest.start(this);
            }
        });
        loadingview.startLoading();
        courseDetailRequest.start(YamCollegeDetailAct.this);
    }

    private void loadListData() {
        final CourseNewestRequest courseNewestRequest = new CourseNewestRequest(articleId);
        courseNewestRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

            @Override
            public void onOK() {
                collegeCourseList = courseNewestRequest.getCollegeCourseList();
                if (!DataUtil.listIsNull(collegeCourseList)) {
                    objectArrayList.add(0);
                    objectArrayList.addAll(collegeCourseList);
                }
                myAdapter.addItem(objectArrayList, true);
                ViewUtils.setVisible(listView);
            }

            @Override
            public void onFail(int code) {
            }
        });
        courseNewestRequest.start(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private class MyAdapter extends ObjectAdapter {
        private final int TYPE_VIDEO = 0;
        private final int TYPE_COURSE = 1;
        private final int TYPE_MORE_COURSE = 2;
        private final int TYPE_COUNT = 3;

        public MyAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getItemViewType(int position) {
            Object object = getItem(position);
            if (object instanceof CollegeCourse) {
                return TYPE_COURSE;
            } else if (object instanceof String) {
                return TYPE_VIDEO;
            } else {
                return TYPE_MORE_COURSE;
            }
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_COUNT;
        }

        @Override
        public int getLayoutId() {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            int type = getItemViewType(position);
            if (convertView == null) {
                if (type == TYPE_VIDEO) {
                    convertView = inflateConvertView(R.layout.item_study_course_video);
                    holder = new VideoViewHolder();
                    ((VideoViewHolder) holder).btnPlayVideo = (TextView) convertView.findViewById(R.id.btn_video);
                } else if (type == TYPE_COURSE) {
                    convertView = inflateConvertView(R.layout.item_study_course);
                    holder = new CourseViewHolder();
                    ((CourseViewHolder) holder).imgCourse = (NetworkImageView) convertView
                            .findViewById(R.id.img_study_course);
                    ((CourseViewHolder) holder).txtTitle = (TextView) convertView
                            .findViewById(R.id.txt_title);
                    ((CourseViewHolder) holder).txtLabel1 = (TextView) convertView.findViewById(R.id.txt_label1);
                    ((CourseViewHolder) holder).txtLabel2 = (TextView) convertView.findViewById(R.id.txt_label2);
                    ((CourseViewHolder) holder).txtTime = (TextView) convertView
                            .findViewById(R.id.txt_time);
                    ((CourseViewHolder) holder).txtCount = (TextView) convertView
                            .findViewById(R.id.txt_study_count);
                } else if (type == TYPE_MORE_COURSE) {
                    convertView = inflateConvertView(R.layout.item_study_course_divider);
                    holder = new DividerViewHolder();
                    ((DividerViewHolder) holder).btnMoreCourse = convertView.findViewById(R.id.btn_course_more);
                } else {
                    holder = new ViewHolder();
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Object object = getItem(position);

            if (type == TYPE_VIDEO) {
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                final String videoUrl = (String) object;
                videoViewHolder.btnPlayVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(videoUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "video/*");
                        startActivity(intent);
                    }
                });
            } else if (type == TYPE_MORE_COURSE) {
                DividerViewHolder dividerViewHolder = (DividerViewHolder) holder;
                dividerViewHolder.btnMoreCourse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YamCollegeListAct.start(YamCollegeDetailAct.this, Constants.YAM_COURSE_ALL);
                    }
                });
            } else if (type == TYPE_COURSE) {
                CourseViewHolder courseViewHolder = (CourseViewHolder) holder;
                final CollegeCourse collegeCourse = (CollegeCourse) object;

                courseViewHolder.imgCourse.setImageUrl(collegeCourse.getIconUrl(),
                        AppController.imageLoader);
                ViewUtils.setText(courseViewHolder.txtTitle, collegeCourse.getTitle());

                ArrayList<String> lable = collegeCourse.getLable();
                if (DataUtil.listIsNull(lable)) {
                    ViewUtils.setGone(courseViewHolder.txtLabel1);
                    ViewUtils.setGone(courseViewHolder.txtLabel2);
                } else {
                    if (lable.size() == 1) {
                        if (!StringUtil.isNull(lable.get(0))) {
                            ViewUtils.setVisible(courseViewHolder.txtLabel1);
                            ViewUtils.setText(courseViewHolder.txtLabel1, lable.get(0));
                        } else {
                            ViewUtils.setGone(courseViewHolder.txtLabel1);
                        }
                        ViewUtils.setGone(courseViewHolder.txtLabel2);
                    } else if (lable.size() == 2) {
                        if (!StringUtil.isNull(lable.get(0))) {
                            ViewUtils.setVisible(courseViewHolder.txtLabel1);
                            ViewUtils.setText(courseViewHolder.txtLabel1, lable.get(0));
                        } else {
                            ViewUtils.setGone(courseViewHolder.txtLabel1);
                        }
                        if (!StringUtil.isNull(lable.get(1))) {
                            ViewUtils.setVisible(courseViewHolder.txtLabel2);
                            ViewUtils.setText(courseViewHolder.txtLabel2, lable.get(1));
                        } else {
                            ViewUtils.setGone(courseViewHolder.txtLabel2);
                        }
                    }
                }
                ViewUtils.setText(courseViewHolder.txtTime, StringUtil.getDateByMillis(collegeCourse.getCreateTime()));
                ViewUtils.setText(courseViewHolder.txtCount, collegeCourse.getReadAmount());
                final long articleId = collegeCourse.getArticleId();
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YamCollegeDetailAct.start(YamCollegeDetailAct.this, articleId);
                    }
                });
            }
            return convertView;
        }

        class ViewHolder {
        }

        class VideoViewHolder extends ViewHolder {
            TextView btnPlayVideo;
        }

        class CourseViewHolder extends ViewHolder {
            NetworkImageView imgCourse;
            TextView txtTitle, txtLabel1, txtLabel2, txtTime, txtCount;
        }

        class DividerViewHolder extends ViewHolder {
            View btnMoreCourse;
        }
    }

}
