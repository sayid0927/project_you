package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.BehaviorType;
import com.zxly.o2o.model.FansInfo;
import com.zxly.o2o.model.RemarkBaseInfo;
import com.zxly.o2o.model.TagModel;
import com.zxly.o2o.model.TargetTypeNameConstants;
import com.zxly.o2o.model.YamLessonInfo;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshExpandableListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FansBehaviorRequest;
import com.zxly.o2o.request.FansCallRecordRequest;
import com.zxly.o2o.request.FansRemarkRequest;
import com.zxly.o2o.request.GetFansDetailRequest;
import com.zxly.o2o.request.YamLessonRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CollegeCourseView;
import com.zxly.o2o.view.TagLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejun on 2016/9/27.
 */
public class FansDetailNewAct extends BasicAct implements View.OnClickListener,PullToRefreshBase.OnRefreshListener,ExpandableListView
        .OnGroupClickListener {
    //普通备注内容
    private static final int TYPE_COMMON = 1;
    //电话回访
    private static final int TYPE_CALL = 2;
    //短信回访
    private static final int TYPE_MESSAGE = 3;
    private static final int TYPE_4 = 4;
    private static final int TYPE_5 = 5;
    private static final int TYPE_6 = 6;
    private static ParameCallBack _callBack;
    private static ParameCallBack _offLineFansCallBack;
    private static ParameCallBack _clearBehavoirCallBack;
    private FansDetailNewAct context;
    private long fansId;
    private TextView btnTopRight;
    private PullToRefreshExpandableListView expandableListView;
    private LinearLayout head;
    private  static final int TYPE_1=1;
    private static final int TYPE_2=2;
    private static final int TYPE_3 = 3;
    private FansDetailAdapter fansDetailAdapter;
    private String[] groupStrings;
    //一列列表数据
    private List<String> remarkGroups=new ArrayList<String>();
    //二级列表数据(包含备注信息与消费及行为信息两种类别，后两种一样的类型)
    private List<RemarkBaseInfo> childInfos=new ArrayList<RemarkBaseInfo>();
    //一级数据和二级数据集合
    private   Map<String, List<RemarkBaseInfo>> remarkAllDatas = new LinkedHashMap<String, List<RemarkBaseInfo>>();
    private  Map<String, List<RemarkBaseInfo>> copyRemarkAllDatas = new LinkedHashMap<String, List<RemarkBaseInfo>>();
    private boolean isLastData;
    private int pageIndexComsum=1;
    private int pageIndexBehavior=1;
    private List<String> hasClickGroup=new ArrayList<String>();
    private int pageIndexRemark=1;
    private TextView txt_phone_brand;
    private TextView txt_phone_imei;
    private TextView txt_install_time;
    private TextView txt_install_type;
    private FansInfo fansDtail;
    private int detailType;
    private TextView txt_info;
    private TextView txt_phone_num;
    private Button btn_send_message;
    private TextView txt_phone_brandinfo;
    private List<TagModel> tagList;
    private List<String> chooseTagIds=new ArrayList<String>();
    private boolean isEmptyRemark=true;
    private boolean isEmptyConsumption=true;
    private boolean isEmptyBehavior=true;
    private RelativeLayout layout_bottom;
    private CollegeCourseView collegeCourseView;
    private TextView txt_stickgroup;
    private LinearLayout stick_head;
    private int indicatorGroupHeight;
    private int indicatorGroupId;
    private RelativeLayout footerView;
    private TextView txtTypeName;
    private TextView txtTitle;
    private TextView txtTime;
    private TextView txtRenshu;
    private View btnClose;
    private NetworkImageView imgHead;
    private LinearLayout layout_text;
    private TextView text1;
    private TextView text2;
    private YamLessonInfo yamLessonInfo;
    private RelativeLayout layout_lesson;
    private String emptyRemark="";
    private String emptyComsuption="";
    private String emptyBehavior="";
    private boolean isFirstUseConsumptionCache;
    private boolean isFirstUseBehaviorCache;
    private int newBehaviorCount;//消费轨迹与行为轨迹未读总数
    private boolean hasClear;
    private TextView txtLabel1;
    private TextView txtLabel2;
    private String[] label;//课程标签
    private boolean showRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_detail_fans);
        context = this;
        fansId = getIntent().getLongExtra("fansId", 1017);
        // 0 普通粉丝  1 线下录入粉丝(不同的粉丝在备注信息的第一个子布局有不同的显示 3种)
        detailType = getIntent().getIntExtra("detailType", 0);
        loadData();
    }

    private void initViews(FansInfo fansDtail) {
        findViewById(R.id.btn_back).setOnClickListener(this);

        btnTopRight = (TextView) findViewById(R.id.btn_top_right);
        btnTopRight.setVisibility(View.VISIBLE);
        btnTopRight.setText("推送");
        btnTopRight.setTextSize(15);
        Drawable drawable = getResources().getDrawable(R.drawable.icon_push_normal);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        btnTopRight.setCompoundDrawables(drawable, null, null, null);
        btnTopRight.setCompoundDrawablePadding(10);
        btnTopRight.setTextColor(getResources().getColor(R.color.white));
        btnTopRight.setOnClickListener(this);
        findViewById(R.id.btn_remark).setOnClickListener(this);

        //普通粉丝头部
        if(fansDtail.getIsOffline()==0){
            ViewUtils.setText(findViewById(R.id.txt_title), "详情");
            head = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.head_detailfans,null,false);
            txt_phone_brand = (TextView) head.findViewById(R.id.txt_phone_brand);
            txt_phone_imei = (TextView) head.findViewById(R.id.txt_phone_imei);

        }else{
            //线下粉丝不显示推送
            ViewUtils.setGone(btnTopRight);
            ViewUtils.setText(findViewById(R.id.txt_title), "线下粉丝详情");
            head=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.head_detailfans_outline,null,false);
            txt_info = (TextView) head.findViewById(R.id.txt_info);
            txt_phone_num = (TextView) head.findViewById(R.id.txt_phone_num);
            btn_send_message = (Button) head.findViewById(R.id.btn_send_message);
            txt_phone_brandinfo = (TextView) head.findViewById(R.id.txt_phone_brandinfo);
            btn_send_message.setOnClickListener(this);
            txt_phone_num.setOnClickListener(this);
        }

        txt_install_time = (TextView) head.findViewById(R.id.txt_install_time);
        txt_install_type = (TextView) head.findViewById(R.id.txt_install_type);

        //顶部滑动时常驻头部布局
        stick_head = (LinearLayout) findViewById(R.id.stick_head);
        txt_stickgroup = (TextView) findViewById(R.id.txt_stickgroup);
        //底部写备注  布局
        layout_bottom = (RelativeLayout) findViewById(R.id.layout_bottom);

        expandableListView = (PullToRefreshExpandableListView) findViewById(R.id.expand_listview);
        expandableListView.getRefreshableView().setSelector(android.R.color.transparent);
        expandableListView.getRefreshableView().setOnGroupClickListener(this);
        expandableListView.setPullToRefreshOverScrollEnabled(true);          //可刷新
        expandableListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        expandableListView.getRefreshableView().setDivider(null);
        expandableListView.getRefreshableView().setGroupIndicator(null);
        expandableListView.setOnRefreshListener(this);
        ViewUtils.setRefreshListText(expandableListView);

        if (fansDetailAdapter == null) {
            fansDetailAdapter = new FansDetailAdapter(this);
        }
        footerView =(RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_expand_footer,null,false);
        if(PreferUtil.getInstance().getIsFirstOpenFansDetail()){
            initFooterView();
        }

        expandableListView.getRefreshableView().addHeaderView(head);
        expandableListView.getRefreshableView().setAdapter(fansDetailAdapter);

        expandableListView.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int npos = view.pointToPosition(0, 0);// 其实就是firstVisibleItem
                if (npos == AdapterView.INVALID_POSITION)// 如果第一个位置值无效
                    return;
                //当前第一行所属的组id
                int gid = expandableListView.getRefreshableView().getPackedPositionGroup(expandableListView.getRefreshableView().getExpandableListPosition(npos));
                //当前第一行的子id
                int cid=expandableListView.getRefreshableView().getPackedPositionChild(expandableListView.getRefreshableView().getExpandableListPosition(npos));
                //当前第二行的子id
                if(cid==AdapterView.INVALID_POSITION){
                    View groupView = expandableListView.getChildAt(npos
                            - expandableListView.getRefreshableView().getFirstVisiblePosition());// 第一行的view
                    indicatorGroupHeight = groupView.getHeight();
                    stick_head.setVisibility(View.GONE);
                }else {
                    stick_head.setVisibility(View.VISIBLE);
                    txt_stickgroup.setText(groupStrings[gid]);
                }

                if (indicatorGroupHeight == 0) {
                    return;
                }

                if (gid != indicatorGroupId) {// 如果指示器显示的不是当前group
                    indicatorGroupId = gid;
                    // 为此指示器增加点击事件
                    stick_head.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            expandableListView.getRefreshableView().collapseGroup(indicatorGroupId);
                        }
                    });
                }
                //往上推的效果
                int showHeight = indicatorGroupHeight;
                int nEndPos = expandableListView.getRefreshableView().pointToPosition(0, indicatorGroupHeight);// 第二个item的位置
                if (nEndPos == AdapterView.INVALID_POSITION)// 如果无效直接返回
                    return;
                long pos2 = expandableListView.getRefreshableView().getExpandableListPosition(nEndPos);
                int groupPos2 = ExpandableListView.getPackedPositionGroup(pos2);// 获取第二个group的id
                if (groupPos2 != indicatorGroupId) {// 如果不等于指示器当前的group
                    View viewNext = expandableListView.getRefreshableView().getChildAt(nEndPos
                            - expandableListView.getRefreshableView().getFirstVisiblePosition());
                    showHeight = viewNext.getTop();
                }
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) stick_head
                        .getLayoutParams();
                layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
                stick_head.setLayoutParams(layoutParams);
            }
        });

        //展开某组关闭其他组
        expandableListView.getRefreshableView().setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int y=0;y<expandableListView.getRefreshableView().getExpandableListAdapter().getGroupCount();y++){
                    if(y!=groupPosition){
                        expandableListView.getRefreshableView().collapseGroup(y);
                    }
                }
            }
        });

        //组信息不从后台获取  初始化组信息
        //为0时为粉丝   其他则是线下录入粉丝无行为轨迹
        if(fansDtail.getIsOffline()==0){
            groupStrings = new String[]{"备注信息", "消费轨迹", "行为轨迹"};
            for (int i = 0; i < groupStrings.length; i++) {
                putDatas(groupStrings[i],childInfos);
                fansDetailAdapter.addContent(remarkAllDatas);
                fansDetailAdapter.notifyDataSetChanged();
                copyRemarkAllDatas.put(groupStrings[i],childInfos);
            }
        }else{
            groupStrings = new String[]{"备注信息", "消费轨迹"};
            for (int i = 0; i < groupStrings.length; i++) {
                putDatas(groupStrings[i],childInfos);
                fansDetailAdapter.addContent(remarkAllDatas);
                fansDetailAdapter.notifyDataSetChanged();
                copyRemarkAllDatas.put(groupStrings[i],childInfos);
            }
        }

    }

    private void initFooterView() {
        layout_lesson = (RelativeLayout) footerView.findViewById(R.id.layout_lesson);
        layout_lesson.setEnabled(false);
        txtTypeName = (TextView)footerView.findViewById(R.id.txt_typename);
        txtTitle =(TextView)footerView.findViewById(R.id.txt_title);
        txtTime = (TextView) footerView.findViewById(R.id.txt_time);
        txtLabel1 = (TextView) footerView.findViewById(R.id.txt_label1);
        txtLabel2 = (TextView) footerView.findViewById(R.id.txt_label2);
        txtRenshu = (TextView) footerView.findViewById(R.id.txt_renshu);
        btnClose =footerView.findViewById(R.id.btn_close);
        imgHead = (NetworkImageView) footerView.findViewById(R.id.img_head_icon);
        //粉丝与会员详情页面 需要显示的布局
        layout_text = (LinearLayout) footerView.findViewById(R.id.layout_text);
        text1 = (TextView) footerView.findViewById(R.id.text1);
        text2 = (TextView) footerView.findViewById(R.id.text2);
        text1.setText("粉丝成为会员就能为您带来长期利益");
        text2.setText("学习下如何引导他去注册会员吧~猛戳教程");
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.setGone(footerView);
            }
        });
        final YamLessonRequest yamLessonRequest = new YamLessonRequest(2);
        yamLessonRequest.start();
        yamLessonRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                yamLessonInfo = yamLessonRequest.getYamLessonInfo();
                label=yamLessonInfo.getLabel();
                if(yamLessonInfo!=null){
                    ViewUtils.setText(txtTypeName,"："+ yamLessonInfo.getTypeName());
                    ViewUtils.setText(txtTitle, yamLessonInfo.getTitle());
                    if(label!=null)
                    {
                        int length=label.length;
                        for(int i=0;i<length;i++)
                        {
                            switch (i)
                            {
                                case 0:
                                    ViewUtils.setVisible(txtLabel1);
                                    txtLabel1.setText(label[i]);
                                    break;
                                case 1:
                                    ViewUtils.setVisible(txtLabel2);
                                    txtLabel2.setText(label[i]);
                                    break;
                            }
                        }
                    }

                    txtTime.setText(TimeUtil.formatTimeHHMMDD(yamLessonInfo.getCreateTime()));
                    imgHead.setImageUrl(yamLessonInfo.getImage(), AppController.imageLoader);
                    Spanned txtlearnCount= Html.fromHtml("<font color=\"#f49126\">" + yamLessonInfo.getLearnCount() + "&nbsp;</font>"+"人正在学习");
                    txtRenshu.setText(txtlearnCount);
                    layout_lesson.setEnabled(true);
                    expandableListView.getRefreshableView().addFooterView(footerView);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        layout_lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YamCollegeDetailAct.start(context, yamLessonInfo.getId());
            }
        });


    }

    /**
     * 会员基本信息获取
     */
    private void loadData() {
        final GetFansDetailRequest getMenberDetailRequest = new GetFansDetailRequest(fansId);
        getMenberDetailRequest.start();
        getMenberDetailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                fansDtail = getMenberDetailRequest.getFansDtail();
                detailType=fansDtail.getIsOffline();
                newBehaviorCount=fansDtail.getNewBehaviorCount();
                    fillData(fansDtail);
                    showDefaultType(fansDtail);
            }

            @Override
            public void onFail(int code) {

            }
        });
    }



    /**
     * 进入详情页面 检查是否有新的轨迹消息  优先检查消费轨迹再行为轨迹 并打开
     * @param fansDtail
     */
    private void showDefaultType(FansInfo fansDtail) {
        if(fansDtail.getHasNewShopping()==1){
            //由于详情的组别 顺序不会改变  所以直接打开对应序号
            //优先展示消费轨迹
            loadNewConsumptionDatakData(fansId,1,false,false);
            //没有新行为那么就预加载
            if(fansDtail.getHasNewBehavior()==0){
                loadNewBehaviorData(fansId,1,false,false);
            }
            loadRemarkData(fansId,1,false,false);
            return;
        }else if(fansDtail.getHasNewBehavior()==1&&fansDtail.getIsOffline()==0){//保证身份是普通粉丝 线下粉丝是没有行为轨迹的
            if(fansDtail.getHasNewShopping()==0){
                loadNewBehaviorData(fansId,1,false,false);
            }
            loadNewConsumptionDatakData(fansId,1,false,false);
            loadRemarkData(fansId,1,false,false);
        }else {
            //没有默认打开的组 那就预加载  避免出现点击分组会闪现默认提示
            //既没有新消费轨迹也没有新行为轨迹
            if(fansDtail.getHasNewShopping()==0&&fansDtail.getHasNewBehavior()==0){
                loadNewConsumptionDatakData(fansId,1,false,false);
                loadNewBehaviorData(fansId,1,false,false);
                showRemark =true;
                loadRemarkData(fansId,1,false,false);
            }
        }
    }

    /**
     * 填充数据
     * @param fansDtail
     */
    private void fillData(FansInfo fansDtail) {
        initViews(fansDtail);
        if(fansDtail.getIsOffline()==0){
            txt_phone_brand.setText(fansDtail.getPhoneModel());
            txt_phone_imei.setText(fansDtail.getImei());
        }else{
            String gender=fansDtail.getGender()==1?"男":"女";
            txt_info.setText(fansDtail.getName()+"     "+gender);
            txt_phone_num.setText(fansDtail.getPhone());
            txt_phone_brandinfo.setText(fansDtail.getPhoneModel());
        }
        txt_install_time.setText(EaseConstant.formatOrderLongTime(fansDtail.getInstallTime()));
        txt_install_type.setText(fansDtail.getIsOffline()==1?"线下录入":"安装App");
//        loadRemarkData(fansId,1,false,false);
    }

    /**
     * 将数据保存在map集合中
     * @param remarkGroupName 组名
     * @param remarkInfos 该组下的数据
     * @return 返回map
     */
    private void putDatas(String remarkGroupName,List<RemarkBaseInfo> remarkInfos){
            remarkAllDatas.put(remarkGroupName,remarkInfos);
    }



    /**
     * 获取备注信息类数据
     * @param pageIndex
     */
    private void loadRemarkData(long fansId, final int pageIndex, final boolean isRefrensh,boolean showDialog){
        final FansRemarkRequest getNewRemarkReqeust = new FansRemarkRequest(fansId,10,pageIndex,showDialog);
        getNewRemarkReqeust.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<RemarkBaseInfo> newRemarkInfoList = getNewRemarkReqeust.getFansRemarkList();
                expandableListView.onRefreshComplete();
                boolean empty = newRemarkInfoList.isEmpty();
                if(empty){
                    //如果是空
                    if (pageIndex == 1) {
                        isEmptyRemark = true;
                        emptyRemark="您还没对这位粉丝添加任何备注哦，立即备注吧。";
                    } else {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }
                    fansDetailAdapter.notifyDataSetChanged();
                }else{
                    //获取子数据返回成功后，标识该组子数据已加载过
                    isEmptyRemark = false;
                    if(!isRefrensh){
                        hasClickGroup.add("备注信息");
                        putDatas("备注信息", newRemarkInfoList);
                        fansDetailAdapter.addContent(remarkAllDatas);
                        fansDetailAdapter.notifyDataSetChanged();
                        //第一次加载完子数据后就将其保存在该map中
                        copyRemarkAllDatas.put("备注信息",newRemarkInfoList);

                    }else{
                        //下拉刷新
                        if(pageIndex==1){
                            dealRefrenshData("备注信息",newRemarkInfoList);
                        }else{
                            dealUpRefrenshData("备注信息",newRemarkInfoList);
                        }
                    }
                }
                if(showRemark&&!expandableListView.getRefreshableView().isGroupExpanded(1)&&!expandableListView.getRefreshableView().isGroupExpanded(2)){
                    if(!TextUtils.isEmpty(fansDtail.getPhone())||!TextUtils.isEmpty(fansDtail.getName())||!fansDtail.getLabels().isEmpty()||fansDtail.getGender()>0){
                        expandableListView.getRefreshableView().expandGroup(0);
                    }
                }
            }


            @Override
            public void onFail(int code) {

            }
        });
        getNewRemarkReqeust.start();
    }

    private void dealRefrenshData(String gruopname,List<RemarkBaseInfo> data){
        remarkAllDatas.get(gruopname).clear();
        remarkAllDatas.get(gruopname).addAll(data);
        fansDetailAdapter.addContent(remarkAllDatas);
        fansDetailAdapter.notifyDataSetChanged();
        copyRemarkAllDatas.get(gruopname).clear();
        copyRemarkAllDatas.get(gruopname).addAll(data);
    }

    private void dealUpRefrenshData(String gruopname,List<RemarkBaseInfo> data){
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).getIsNew()==1){
                newBehaviorCount--;
            }
        }
        if(!hasClear&&newBehaviorCount<=0){
            clearMenberBehavoirPoint();
        }
        remarkAllDatas.get(gruopname).addAll(data);
        fansDetailAdapter.addContent(remarkAllDatas);
        fansDetailAdapter.notifyDataSetChanged();
//        copyRemarkAllDatas.get(gruopname).addAll(data);
//        if(gruopname.equals("备注信息")){
//            pageIndexRemark++;
//        }else if(gruopname.equals("消费轨迹")){
//            pageIndexRemark++;
//        }else if(gruopname.equals("行为轨迹")){
//            pageIndexBehavior++;
//        }
    }

        /**
         * 清楚该会员在会员列表轨迹红点及组轨迹数减一的回调
         */
        private void clearMenberBehavoirPoint() {
            hasClear =true;
            if(_clearBehavoirCallBack!=null){
                _clearBehavoirCallBack.onCall(fansId);
            }
        }


    /**
     * 获取消费轨迹
     * @param menberId
     * @param pageIndex
     */
    private void loadNewConsumptionDatakData(long menberId, final int pageIndex, final boolean isRefrensh,boolean showLoading){
        final FansBehaviorRequest fansBehaviorRequest = new FansBehaviorRequest(menberId,2,10, pageIndex,showLoading);
        fansBehaviorRequest.start();
        fansBehaviorRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<RemarkBaseInfo> menberConsumptionDataList = fansBehaviorRequest
                        .getFansBehaviorList();
                expandableListView.onRefreshComplete();
                boolean empty = menberConsumptionDataList.isEmpty();
                if(empty){
                    //如果是空
                    if (pageIndex == 1) {
                        isEmptyConsumption = true;
                        if(fansDtail.getIsOffline()==1){
                            emptyComsuption="粉丝还没有购买记录呢!";
                        }else {
                            emptyComsuption="粉丝还没注册呢，点击推送引导注册!";
                        }
                    } else {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }
                }else{
                    isEmptyConsumption = false;
                    if(!isRefrensh){
                        //获取子数据返回成功后，标识该组子数据已加载过
                        hasClickGroup.add("消费轨迹");
                        putDatas("消费轨迹", menberConsumptionDataList);
                        fansDetailAdapter.addContent(remarkAllDatas);
                        fansDetailAdapter.notifyDataSetChanged();
                        //第一次加载完子数据后就将其保存在该map中
                        copyRemarkAllDatas.put("消费轨迹",menberConsumptionDataList);
                    }else{
                        //下拉刷新
                        if(pageIndex==1){
                            dealRefrenshData("消费轨迹",menberConsumptionDataList);
                        }else{
                            dealUpRefrenshData("消费轨迹",menberConsumptionDataList);
                        }
                    }
                    //有新消费轨迹  那么就展开
                    if(fansDtail.getHasNewShopping()==1){
                        for (int i = 0; i < menberConsumptionDataList.size(); i++) {
                            if(menberConsumptionDataList.get(i).getIsNew()==1){
                                newBehaviorCount--;
                            }
                        }
                        if(!hasClear&&newBehaviorCount<=0){
                            clearMenberBehavoirPoint();
                        }
                        expandableListView.getRefreshableView().expandGroup(1);
                    }else {
                        //没有新的消费轨迹   但有旧的消费轨迹且没有新的行为轨迹
                        if(fansDtail.getHasNewBehavior()==0&&!menberConsumptionDataList.isEmpty()){
                            expandableListView.getRefreshableView().expandGroup(1);
                        }
                    }
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }


    /**
     * 获取行为轨迹
     * @param menberId
     * @param pageIndex
     */
    private void loadNewBehaviorData(long menberId, final int pageIndex, final boolean isRefrensh,boolean showLoading){
        final FansBehaviorRequest getNewBehaviorRequest = new FansBehaviorRequest(menberId,1,10, pageIndex,showLoading);
        getNewBehaviorRequest.start();
        getNewBehaviorRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<RemarkBaseInfo> behaviorDataList = getNewBehaviorRequest.getFansBehaviorList();
                expandableListView.onRefreshComplete();
                boolean empty = behaviorDataList.isEmpty();
                if(empty){
                    //如果是空
                    if (pageIndex == 1) {
                        isEmptyBehavior = true;
                        emptyBehavior="粉丝最近三个月都没有留下痕迹呢,点击推送送点好料~";
                    } else {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }
                }else{
                    //获取子数据返回成功后，标识该组子数据已加载过
                    isEmptyBehavior = false;
                    if(!isRefrensh){
                        hasClickGroup.add("行为轨迹");
                        putDatas("行为轨迹", behaviorDataList);
                        fansDetailAdapter.addContent(remarkAllDatas);
                        fansDetailAdapter.notifyDataSetChanged();
                        //第一次加载完子数据后就将其保存在该map中
                        copyRemarkAllDatas.put("行为轨迹",behaviorDataList);
                    }else{
                        //下拉刷新
                        if(pageIndex==1){
                            dealRefrenshData("行为轨迹",behaviorDataList);
                        }else{
                            dealUpRefrenshData("行为轨迹",behaviorDataList);
                        }
                    }
                    if(fansDtail.getHasNewBehavior()==1){
                        for (int i = 0; i < behaviorDataList.size(); i++) {
                            if(behaviorDataList.get(i).getIsNew()==1){
                                newBehaviorCount--;
                            }
                        }
                        if(!hasClear&&newBehaviorCount<=0){
                            clearMenberBehavoirPoint();
                        }
                    }
                    //没有新的消费轨迹 有新的行为轨迹 不是线下录入粉丝
                    if(fansDtail.getHasNewShopping()!=1&&fansDtail.getHasNewBehavior()==1&&fansDtail.getIsOffline()==0){
                        expandableListView.getRefreshableView().expandGroup(2);
                    }
                    //新增判断 最开始是只有新的轨迹才会展开  现新增没有新的但有数据就按规则展开
                    if(fansDtail.getIsOffline()==0&&fansDtail.getHasNewShopping()!=1&&fansDtail.getHasNewBehavior()!=1&&!behaviorDataList.isEmpty()&&!expandableListView.getRefreshableView().isGroupExpanded(1)){

                        expandableListView.getRefreshableView().expandGroup(2);
                    }
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_top_right:
                ArrayList<String> fansImeis = new ArrayList<String>();
                fansImeis.add(fansDtail.getImei());
                ChooseSendAct.start(this, fansImeis, null);
                break;
            case R.id.btn_remark:
//                FansAddAct.start(this);
                WriteFansRemarkAct.start(this,fansId,detailType,_callBack,fansDetailCallBack);
                break;
            case R.id.btn_send_message:
                SendPhoneMessageAct.start(this,fansId,fansDtail.getPhone(),1);
                break;
            case R.id.txt_phone_num:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +fansDtail.getPhone()));
                startActivity(intent);
                new FansCallRecordRequest(fansId).start();
                break;
        }
    }

    ParameCallBack fansDetailCallBack=new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            FansInfo fansDtail= (FansInfo) object;
            updateFansDetail(fansDtail);
        }
    };

    private void updateFansDetail(FansInfo fansDtailUpdate) {
        if(fansDtail.getIsOffline()==0){
                txt_phone_brand.setText(fansDtailUpdate.getPhoneModel());
                if(!TextUtils.isEmpty(fansDtailUpdate.getRemarkName())){
                    fansDtail.setName(fansDtailUpdate.getRemarkName());
                }
            if (!TextUtils.isEmpty(fansDtailUpdate.getPhone())){
                fansDtail.setPhone(fansDtailUpdate.getPhone());
            }
            fansDtail.setGender(fansDtailUpdate.getGender());
            if(fansDtail.getHasRemark()==0){
                fansDtail.setHasRemark(1);
            }
        }else{
                String gender=fansDtailUpdate.getGender()==1?"男":"女";
                txt_info.setText(fansDtailUpdate.getRemarkName()+"     "+gender);
                txt_phone_num.setText(fansDtailUpdate.getPhone());
                txt_phone_brandinfo.setText(fansDtailUpdate.getPhoneModel());
            if(fansDtail.getHasRemark()==0){
                fansDtail.setHasRemark(1);
            }
            if(!TextUtils.isEmpty(fansDtailUpdate.getRemarkContent())){
                isEmptyRemark=false;
            }
            if(_offLineFansCallBack!=null){
                _offLineFansCallBack.onCall(fansDtailUpdate);
            }
        }
        //更新标签
        fansDtail.setLabels(fansDtailUpdate.getLabels());
        fansDtail.setLabelStr(fansDtailUpdate.getLabelStr());
        //返回后重新加载
        loadRemarkData(fansId,1,false,false);
        loadNewConsumptionDatakData(fansId,1,false,false);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            refreshLoadData();
            if(expandableListView.getRefreshableView().isGroupExpanded(0)){
                pageIndexRemark=1;
                loadRemarkData(fansId,pageIndexRemark,true,true);
            }else if(expandableListView.getRefreshableView().isGroupExpanded(1)){
                pageIndexComsum=1;
                loadNewConsumptionDatakData(fansId,pageIndexComsum,true,true);
            }else if(expandableListView.getRefreshableView().isGroupExpanded(2)){
                pageIndexBehavior=1;
                loadNewBehaviorData(fansId,pageIndexBehavior,true,true);
            }else{
                expandableListView.onRefreshComplete();
            }

        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            if(expandableListView.getRefreshableView().isGroupExpanded(0)){
                pageIndexRemark++;
                loadRemarkData(fansId,pageIndexRemark,true,true);
            }else if(expandableListView.getRefreshableView().isGroupExpanded(1)){
                pageIndexComsum++;
                loadNewConsumptionDatakData(fansId,pageIndexComsum,true,true);
            }else if(expandableListView.getRefreshableView().isGroupExpanded(2)){
                pageIndexBehavior++;
                loadNewBehaviorData(fansId,pageIndexBehavior,true,true);
            }else{
                expandableListView.onRefreshComplete();
            }

        }
    }

    private void refreshLoadData() {
        final GetFansDetailRequest getMenberDetailRequest = new GetFansDetailRequest(fansId);
        getMenberDetailRequest.start();
        getMenberDetailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                FansInfo refreshFansDtail = getMenberDetailRequest.getFansDtail();
                if(fansDtail.getIsOffline()==0){
                    txt_phone_brand.setText(fansDtail.getPhoneModel());
                    txt_phone_imei.setText(fansDtail.getImei());
                    fansDtail.setLabels(refreshFansDtail.getLabels());
                    fansDtail.setPhone(refreshFansDtail.getPhone());
                    fansDtail.setName(refreshFansDtail.getName());
                    fansDtail.setGender(refreshFansDtail.getGender());
                }else {
                    String gender=refreshFansDtail.getGender()==1?"男":"女";
                    txt_info.setText(refreshFansDtail.getName()+"     "+gender);
                    txt_phone_num.setText(refreshFansDtail.getPhone());
                    txt_phone_brandinfo.setText(refreshFansDtail.getPhoneModel());
                    fansDtail.setLabels(refreshFansDtail.getLabels());
                }

            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
        if(!parent.isGroupExpanded(groupPosition)){
            if(groupPosition==0){
                if(!hasClickGroup.contains("备注信息")){
                    //之前未加载过数据那么就加载数据
                    pageIndexRemark =1;
                    loadRemarkData(fansId,pageIndexRemark,false,true);
                }else{
                    //再次点击 之前已经有加载数据那么就用缓存数据
                    useCacheData("备注信息",true);
                }
            }
            if(groupPosition==1){
                //是否有消费
                if(!hasClickGroup.contains("消费轨迹")){
                    pageIndexComsum=1;
                    loadNewConsumptionDatakData(fansId, pageIndexComsum,false,true);
                }else{
                    isFirstUseConsumptionCache =true;
                    useCacheData("消费轨迹", true);
                }
            }
            if(groupPosition==2){
                if(!hasClickGroup.contains("行为轨迹")){
                    pageIndexBehavior=1;
                    loadNewBehaviorData(fansId, pageIndexBehavior,false,true);
                }else{
                    //如果有消费轨迹也有行为轨迹  那么会先打开消费轨迹  但行为轨迹的已经加载过数据了
                    if(fansDtail.getHasNewShopping()==1&&fansDtail.getHasNewBehavior()==1&&!isFirstUseBehaviorCache){
                        isFirstUseBehaviorCache =false;
                    }
                    if(fansDtail.getHasNewShopping()==0&&fansDtail.getHasNewBehavior()==1&&!isFirstUseBehaviorCache){
                        isFirstUseBehaviorCache =true;
                    }
                    useCacheData("行为轨迹",true);
                    isFirstUseBehaviorCache=true;
                }
            }
        }else {
            //当第一组是收起状态时  底部写备注按钮显示

        }
        return false;
    }

    /**
     * 根据组名获取组下数据（缓存）
     * @param groupName
     */
    private void useCacheData(String groupName,boolean isFirst) {
        List<RemarkBaseInfo> remarkInfo = copyRemarkAllDatas.get(groupName);
        //第二次点击行为轨迹时使用的缓存数据  将缓存数据中的新轨迹标识重置（标识已经看过）
        if(!groupName.equals("备注信息")&&!remarkInfo.isEmpty()&&isFirst){
            Iterator<RemarkBaseInfo> iterator = remarkInfo.iterator();
            while (iterator.hasNext()){
                RemarkBaseInfo next = iterator.next();
                if(next.getIsNew()==1){
                    next.setIsNew(0);
                }
            }
        }
        putDatas(groupName, remarkInfo);
        fansDetailAdapter.addContent(remarkAllDatas);
        fansDetailAdapter.notifyDataSetChanged();
    }


    private class FansDetailAdapter extends BaseExpandableListAdapter {
        protected Map<String, List<RemarkBaseInfo>> content = new LinkedHashMap<String, List<RemarkBaseInfo>>();
        protected Context context;
        public FansDetailAdapter(Context context) {
            this.context=context;
        }

        @Override
        public int getGroupCount() {
            return content.keySet().size();
        }

        public void clear() {
            content.clear();
        }

        public void addContent(Map<String, List<RemarkBaseInfo>> _content) {
            if (content.isEmpty()) {
                content.putAll(_content);
            } else {
                content=_content;
            }

        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if(groupPosition==0){
                if(isEmptyRemark){//没有备注  那么只有一个子viwe
                    return 1;
                }else {
                    //这个组特别点  有备注的时候  子view也会多一个
                    return content.get(content.keySet().toArray()[groupPosition]).size()+1;
                }
            }else if(groupPosition==1){
                if(fansDtail.getHasNewShopping()==0&&isEmptyConsumption){
                    return 1;
                }else {
                    return content.get(content.keySet().toArray()[groupPosition]).size();
                }
            }else if(groupPosition==2){
                if(fansDtail.getHasNewBehavior()==0&&isEmptyBehavior){
                    return 1;
                }else{
                    return content.get(content.keySet().toArray()[groupPosition]).size();
                }
            }else {
                return content.get(content.keySet().toArray()[groupPosition]).size();
            }

        }

        @Override
        public Object getGroup(int groupPosition) {
            return content.keySet().toArray()[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return content.get(content.keySet().toArray()[groupPosition]).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder gViewHolder;
            if(convertView==null){
                gViewHolder=new GroupViewHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.item_outlinefans_group,parent,false);
                gViewHolder.group_name= (TextView) convertView.findViewById(R.id.group_name);
                gViewHolder.icon_arrow= (ImageView) convertView.findViewById(R.id.icon_arrow);
                gViewHolder.tv_group_remarks= (TextView) convertView.findViewById(R.id.tv_group_remarks);
                convertView.setTag(gViewHolder);
            }else{
                gViewHolder= (GroupViewHolder) convertView.getTag();
            }
            //// TODO: 2016/8/27  赋值

            String groupNme = (String) getGroup(groupPosition);
            gViewHolder.group_name.setText(groupNme);
            //设置箭头指向
            if(isExpanded){
                gViewHolder.icon_arrow.setBackgroundResource(R.drawable.turn_up_small);
            }else{
                gViewHolder.icon_arrow.setBackgroundResource(R.drawable.turn_dowm_small);
            }
            gViewHolder.tv_group_remarks.setVisibility(View.GONE);
            gViewHolder.tv_group_remarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WriteFansRemarkAct.start(FansDetailNewAct.this,fansId,detailType,_callBack,fansDetailCallBack);
                }
            });
            return convertView;
        }
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildFirViewHolder firViewHolder=null;
            ChildSecViewholder secViewHolder=null;
            ChildThirdViewHolder childThirdViewHolder=null;
            ChildFourthViewHolder childFourthViewHolder=null;
            ChildFiveViewHolder childFiveViewHolder=null;
            ChildSixViewHolder childSixViewHolder=null;
            int type=getChildType(groupPosition,childPosition);
            if(convertView==null){
                switch (type){
                    case TYPE_1:
                            firViewHolder=new ChildFirViewHolder();
                            convertView=LayoutInflater.from(context).inflate(R.layout.item_outlinefans_child,parent,false);
                            firViewHolder.remark_msg= (TextView) convertView.findViewById(R.id.remark_msg);
                            firViewHolder.txt_time= (TextView) convertView.findViewById(R.id.txt_time);
                            firViewHolder.txt_remarker= (TextView) convertView.findViewById(R.id.txt_remarker);
                            convertView.setTag(firViewHolder);
                        break;
                    case TYPE_2:
                            secViewHolder=new ChildSecViewholder();
                            convertView=LayoutInflater.from(context).inflate(R.layout.item_outline_child_line,parent,false);
                            secViewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
                            secViewHolder.top_line=convertView.findViewById(R.id.top_line);
                            secViewHolder.bottom_line=convertView.findViewById(R.id.bottom_line);
                            secViewHolder.iv_orbit= (ImageView) convertView.findViewById(R.id.iv_orbit);
                            secViewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
                            convertView.setTag(secViewHolder);
                        break;
                    case TYPE_3:
                            childSixViewHolder=new ChildSixViewHolder();
                            convertView=LayoutInflater.from(context).inflate(R.layout.item_fansdetail_typec,parent,false);
                            childSixViewHolder.layout_tag_typec= (TagLayout) convertView.findViewById(R.id.layout_tag_typec);
                            childSixViewHolder.layout_container= (FrameLayout) convertView.findViewById(R.id.layout_container);
                            childSixViewHolder.layout_tag= (RelativeLayout) convertView.findViewById(R.id.layout_tag);
                            childSixViewHolder.notag_layout= (RelativeLayout) convertView.findViewById(R.id.notag_layout);
                            convertView.setTag(childSixViewHolder);
                        break;
                    case TYPE_4:
                            childFiveViewHolder=new ChildFiveViewHolder();
                            convertView=LayoutInflater.from(context).inflate(R.layout.item_fansdetail_typea,parent,false);
                            childFiveViewHolder.btn_remarks= (TextView) convertView.findViewById(R.id.btn_remarks);
                            childFiveViewHolder.txt_default_info= (TextView) convertView.findViewById(R.id.txt_default_info);
                            convertView.setTag(childFiveViewHolder);
                        break;
                    case TYPE_5:
                            childThirdViewHolder=new ChildThirdViewHolder();
                            convertView=LayoutInflater.from(context).inflate(R.layout.item_fansdetail_typeb,parent,false);
                            childThirdViewHolder.txt_phone_info= (TextView) convertView.findViewById(R.id.txt_phone_info);
                            childThirdViewHolder.btn_send_message= (Button) convertView.findViewById(R.id.btn_send_message);
                            childThirdViewHolder.txt_name= (TextView) convertView.findViewById(R.id.txt_name);
                            childThirdViewHolder.txt_gender= (TextView) convertView.findViewById(R.id.txt_gender);
                            childThirdViewHolder.layout_tag_typeb= (TagLayout) convertView.findViewById(R.id.layout_tag_typeb);
                            childThirdViewHolder.tag_container= (RelativeLayout) convertView.findViewById(R.id.tag_container);
                            childThirdViewHolder.default_text= (RelativeLayout) convertView.findViewById(R.id.default_text);
                            childThirdViewHolder.hasRemark_layout= (RelativeLayout) convertView.findViewById(R.id.hasRemark_layout);
                        childThirdViewHolder.btn_remarks= (TextView) convertView.findViewById(R.id.btn_remarks);
                        childThirdViewHolder.txt_default_info= (TextView) convertView.findViewById(R.id.txt_default_info);
                            convertView.setTag(childThirdViewHolder);
                        break;
                    case TYPE_6:
                            childFourthViewHolder=new ChildFourthViewHolder();
                            convertView=LayoutInflater.from(context).inflate(R.layout.item_fansdetail_typed,parent,false);
                            childFourthViewHolder.txt_default= (TextView) convertView.findViewById(R.id.txt_default);
                            convertView.setTag(childFourthViewHolder);
                        break;
                }
            }else {
                switch (type){
                    case TYPE_1:
                            firViewHolder= (ChildFirViewHolder) convertView.getTag();
                        break;
                    case TYPE_2:
                            secViewHolder= (ChildSecViewholder) convertView.getTag();
                        break;
                    case TYPE_3:
                            childSixViewHolder= (ChildSixViewHolder) convertView.getTag();
                        break;
                    case TYPE_4:
                            childFiveViewHolder= (ChildFiveViewHolder) convertView.getTag();
                        break;
                    case TYPE_5:
                            childThirdViewHolder= (ChildThirdViewHolder) convertView.getTag();
                        break;
                    case TYPE_6:
                            childFourthViewHolder= (ChildFourthViewHolder) convertView.getTag();
                        break;
                }
            }
            switch (type){
                case TYPE_1:
                    //保障第一组的第二行的数据是从数据集中第一个加载起
                    RemarkBaseInfo remarkInfoModel = (RemarkBaseInfo) getChild(groupPosition, childPosition-1);
                    //1 普通 2，电话回访 3，短信回访
                    switch (remarkInfoModel.getType()){
                        case TYPE_COMMON:
                            firViewHolder.remark_msg.setText(remarkInfoModel.getContent());
                            firViewHolder.txt_remarker.setText(remarkInfoModel.getSalesman()+"备注");
                            break;
                        case TYPE_CALL:
                            firViewHolder.remark_msg.setText("电话回访");
                            firViewHolder.txt_remarker.setText(remarkInfoModel.getSalesman());
                            break;
                        case TYPE_MESSAGE:
                            firViewHolder.remark_msg.setText("短信回访");
                            firViewHolder.txt_remarker.setText(remarkInfoModel.getSalesman());
                            break;
                        default:
                            firViewHolder.remark_msg.setText(remarkInfoModel.getContent());
                            firViewHolder.txt_remarker.setText(remarkInfoModel.getSalesman()+"备注");
                            break;
                    }
                    firViewHolder.txt_time.setText(EaseConstant.formatOrderLongTime(remarkInfoModel.getTime()));

                    break;
                case TYPE_2:
                    RemarkBaseInfo remarkBaseInfo = (RemarkBaseInfo) getChild(groupPosition, childPosition);
                    secViewHolder.tv_time.setText(EaseConstant.formatBehaviorLongTime(remarkBaseInfo.getCreateTime()));
                    String targetType = new TargetTypeNameConstants(remarkBaseInfo.getTargetType()).getTargetType();
                    String name = new BehaviorType(remarkBaseInfo.getType()).getName();
                    String content = remarkBaseInfo.getContent();
                    if(content.length()>15){
                        content.substring(0,15);
                    }
                    if(name.equals("注册")){
                        secViewHolder.tv_content.setText(name);
                    }else if(name.equals("购物车")){
                        secViewHolder.tv_content.setText("添加"+targetType+"“"+content+"”"+"到购物车");
                    }else {
                        secViewHolder.tv_content.setText(name+targetType+"“"+content+"”");
                    }
                    if(childPosition==0){
                        secViewHolder.top_line.setVisibility(View.INVISIBLE);
                    }else {
                        secViewHolder.top_line.setVisibility(View.VISIBLE);
                    }
                    if(remarkBaseInfo.getIsNew()==0){
                        secViewHolder.iv_orbit.setBackgroundResource(R.drawable.icon_orbit_normal);
                        secViewHolder.tv_content.setTextColor(Color.parseColor("#333333"));
                        secViewHolder.tv_time.setTextColor(Color.parseColor("#333333"));
                    }else{
                        secViewHolder.iv_orbit.setBackgroundResource(R.drawable.icon_orbit_new);
                        secViewHolder.tv_content.setTextColor(Color.parseColor("#ff0000"));
                        secViewHolder.tv_time.setTextColor(Color.parseColor("#ff0000"));
                    }

                    break;
                case TYPE_3:
                    if(fansDtail.getLabels().size()!=0){
                        childSixViewHolder.notag_layout.setVisibility(View.GONE);
                        childSixViewHolder.layout_tag.setVisibility(View.VISIBLE);
                        childSixViewHolder.layout_container.setVisibility(View.VISIBLE);
                        setTagLayout(childSixViewHolder.layout_tag_typec,fansDtail.getLabels());
                    }else {
                        childSixViewHolder.layout_tag.setVisibility(View.GONE);
                        if(fansDtail.getIsOffline()==1&&isEmptyRemark){
                            childSixViewHolder.notag_layout.setVisibility(View.VISIBLE);
                        childSixViewHolder.layout_container.setVisibility(View.VISIBLE);
                        }else {
                            childSixViewHolder.notag_layout.setVisibility(View.GONE);
                            childSixViewHolder.layout_container.setVisibility(View.GONE);
                        }
                    }
                    break;
                case TYPE_4:
                    childFiveViewHolder.txt_default_info.setText(emptyRemark);
                    childFiveViewHolder.btn_remarks.setVisibility(View.GONE);
                    childFiveViewHolder.btn_remarks.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            WriteFansRemarkAct.start((Activity) context,fansId,detailType,_callBack,fansDetailCallBack);
                        }
                    });
                    break;
                case TYPE_5:
                    if(fansDtail.getHasRemark()==1){
                        childThirdViewHolder.hasRemark_layout.setVisibility(View.VISIBLE);
                        childThirdViewHolder.default_text.setVisibility(View.GONE);
                        childThirdViewHolder.txt_phone_info.setText(fansDtail.getPhone());
                        childThirdViewHolder.txt_name.setText(fansDtail.getName());
                        if(fansDtail.getGender()==1){
                            childThirdViewHolder.txt_gender.setText("男");
                        }else if(fansDtail.getGender()==2){
                            childThirdViewHolder.txt_gender.setText("女");
                        }
                        childThirdViewHolder.btn_send_message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SendPhoneMessageAct.start((Activity) context,fansId,fansDtail.getPhone().toString(),1);
                            }
                        });

                        if(!TextUtils.isEmpty(fansDtail.getPhone())){
                            childThirdViewHolder.btn_send_message.setVisibility(View.VISIBLE);
                            childThirdViewHolder.txt_phone_info.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new FansCallRecordRequest(fansId).start();
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +fansDtail.getPhone()));
                                    startActivity(intent);
                                }
                            });
                        }else {
                            childThirdViewHolder.btn_send_message.setVisibility(View.GONE);
                        }
                        if(fansDtail.getLabels().size()!=0){
                            childThirdViewHolder.tag_container.setVisibility(View.VISIBLE);
                            setTagLayout(childThirdViewHolder.layout_tag_typeb,fansDtail.getLabels());
                        }else {
                            childThirdViewHolder.tag_container.setVisibility(View.GONE);
                        }
                    }else {
                            childThirdViewHolder.hasRemark_layout.setVisibility(View.GONE);
                            childThirdViewHolder.default_text.setVisibility(View.VISIBLE);
                            childThirdViewHolder.txt_default_info.setText(emptyRemark);
                            childThirdViewHolder.btn_remarks.setVisibility(View.GONE);
                    }

                    break;
                case TYPE_6:
                    if(groupPosition==1){
                        childFourthViewHolder.txt_default.setText(emptyComsuption);
                    }else if(groupPosition==2){
                        childFourthViewHolder.txt_default.setText(emptyBehavior);
                    }
                    break;
            }
            return convertView;
        }

        @Override
        public int getChildType(int groupPosition, int childPosition) {
            if (groupPosition == 0) {
                if(childPosition==0){
                    //备注信息组  第一个子布局 当身份为线下录入粉丝时 只显示tag标签布局
                    if(fansDtail.getIsOffline()==1){
                        //有备注信息时需显示标签
                        return TYPE_3;
                    }else{
                        //身份不是线下录入的时候  没有备注的情况下
//                        if(fansDtail.getHasRemark()==0){
//                            return TYPE_4;
//                        }else {
                            //身份不是线下录入的时候  有备注 那么就显示全部的资料
                            return TYPE_5;
//                        }
                    }
                }else{
                    //正常的备注信息布局
                    return  TYPE_1;
                }

            } else if(groupPosition==1){
                //处于第二组别  消费轨迹时   没有消费轨迹的时候  需要显示默认信息在第一个子viwe
                if(childPosition==0){
                    if(fansDtail.getHasNewShopping()==0&&isEmptyConsumption){
                        return TYPE_6;//没有消费轨迹  那么是第一行的时候显示默认提示信息
                    }else {
                        return TYPE_2;//有消费轨迹  那么从第一行起显示数据
                    }
                }else {
                    return TYPE_2;
                }


            }else if(groupPosition==2){
                if(childPosition==0){
                    if(fansDtail.getHasNewBehavior()==0&&isEmptyBehavior){
                        return TYPE_6;
                    }else {
                        return TYPE_2;
                    }
                }else{
                    return TYPE_2;
                }
            }else{
                return TYPE_2;
            }
        }
        //二级布局类型数量(虽然只有两种type,但因为子布局有三个 所以实质上也是三种 只不过是有两种一样的  所以需要设置为三)
        @Override
        public int getChildTypeCount() {
            return 7;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public View inflateConvertView(int layoutId) {
            return LayoutInflater.from(context).inflate(layoutId, null);
        }



        class GroupViewHolder {
            TextView group_name;
            TextView tv_group_remarks;
            ImageView icon_arrow;
        }
        //第一种二级布局viewholder
        class ChildFirViewHolder {
            TextView remark_msg,txt_time,txt_remarker;
        }
        //第二种二级布局viewholder
        class ChildSecViewholder{
            TextView tv_content;
            View top_line;
            ImageView iv_orbit;
            View bottom_line;
            TextView tv_time;
        }
        class ChildThirdViewHolder{
            //有备注时  手机号码 姓名 性别 发短信按钮--type5
            TextView txt_phone_info,txt_name,txt_gender;
            Button btn_send_message;
            TagLayout layout_tag_typeb;
            RelativeLayout tag_container,default_text,hasRemark_layout;
            TextView btn_remarks,txt_default_info;
        }
        class ChildFourthViewHolder{
            TextView txt_default;
        }

        class ChildFiveViewHolder{
            //无备注时  显示的写备注按钮---type4
            TextView btn_remarks,txt_default_info;
        }
        class ChildSixViewHolder{
            //身份为线下录入的粉丝时 只显示tag标签--type3
            TagLayout layout_tag_typec;
            FrameLayout layout_container;
            RelativeLayout layout_tag;
            RelativeLayout notag_layout;
        }
    }

    /**
     * 根据用户的标签集获取标签名称然后显示
     * @param flowLayout 标签布局控件
     * @param labels 用户所有的标签集
     */
    private void setTagLayout(TagLayout flowLayout, List<String> labels) {
        //所有的标签集
        tagList = Config.tagList;
        //用户所有的标签
//        String[] labelId = new String[labels.size()];
//        labels.toArray(labelId);
        if(!tagList.isEmpty()){
            chooseTagIds.clear();
            for (int i = 0; i < tagList.size(); i++) {
                for (int i1 = 0; i1 < labels.size(); i1++) {
                    if(Long.parseLong(labels.get(i1))==tagList.get(i).getId()){
                        //选中的tag
                        chooseTagIds.add(tagList.get(i).getName()+"");
                    }
                }
            }
        }

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.rightMargin = 1;
        lp.bottomMargin = 1;
        lp.leftMargin=1;
        lp.topMargin=1;
        flowLayout.removeAllViews();

        for (int i = 0; i < labels.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_orange_tag, null, false);
            TextView textView = (TextView) view.findViewById(R.id.tv_tag);
            textView.setText(chooseTagIds.get(i));
            flowLayout.addView(view);
        }
    }

    public static void start(Activity curAct, long fansId,int detailType) {
        Intent intent = new Intent(curAct, FansDetailNewAct.class);
        intent.putExtra("fansId", fansId);
        intent.putExtra("detailType",detailType);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void startResult(Activity curAct, long fansId,int detailType,ParameCallBack offLineFansCallBack) {
        _offLineFansCallBack =offLineFansCallBack;
        Intent intent = new Intent(curAct, FansDetailNewAct.class);
        intent.putExtra("fansId", fansId);
        intent.putExtra("detailType",detailType);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, long fansId, int detailType, ParameCallBack callBack,ParameCallBack clearBehavoirCallBack) {
        _callBack =callBack;
        _clearBehavoirCallBack =clearBehavoirCallBack;
        Intent intent = new Intent(curAct, FansDetailNewAct.class);
        intent.putExtra("fansId", fansId);
        intent.putExtra("detailType",detailType);//最初粉丝type为传入 现在改为接口获取
        ViewUtils.startActivity(intent, curAct);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _clearBehavoirCallBack=null;
        _offLineFansCallBack=null;
    }
}
