package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.ui.ChatActivity;
import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.model.BehaviorType;
import com.zxly.o2o.model.FansDetailGroup;
import com.zxly.o2o.model.MenberInfoModel;
import com.zxly.o2o.model.RemarkBaseInfo;
import com.zxly.o2o.model.TagModel;
import com.zxly.o2o.model.TargetTypeNameConstants;
import com.zxly.o2o.model.YamLessonInfo;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshExpandableListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.GetMenberDetailRequest;
import com.zxly.o2o.request.GetNewBehaviorRequest;
import com.zxly.o2o.request.GetNewConsumptionDataRequest;
import com.zxly.o2o.request.GetNewRemarkReqeust;
import com.zxly.o2o.request.MarkCallRequest;
import com.zxly.o2o.request.YamLessonRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.TimeUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.TagLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejun on 2016/9/6.
 * 点击会员--线下粉丝详情页面
 */
public class OutLineFansDetailAct  extends BasicAct implements View.OnClickListener, ExpandableListView
        .OnGroupClickListener,PullToRefreshBase.OnRefreshListener {

    private static final int TYPE_3 =3 ;
    private static final int TYPE_4 =4 ;
    private static final int TYPE_5 =5 ;
    private static final int TYPE_COMMON = 1;
    private static final int TYPE_CALL = 2;
    private static final int TYPE_MESSAGE = 3;
    private static ParameCallBack _callBack;
    private static ParameCallBack _clearBehavoirCallBack;
    private CircleImageView head_image;
    private ImageView gender;
    private TextView tv_name;
    private Button btn_send_message;
    private LinearLayout tv_tuisong;
    private LinearLayout tv_sendmsg;
    private LinearLayout tv_call;
    private PullToRefreshExpandableListView expandableListView;
    private String[] groupStrings;
    private OutLineFansDetailAdapter outLineFansDetailAdapter;
    private final int TYPE_1=1;
    private final int TYPE_2=2;
    private long menberId;
    private LinearLayout stick_head;
    private TextView phone_num;
    private TextView brand;
    private MenberInfoModel menberDtail=new MenberInfoModel();
    private List<FansDetailGroup>  group=new ArrayList<FansDetailGroup>();
    private List<String> hasClickGroup=new ArrayList<String>();

    //一列列表数据
    private List<String> remarkGroups=new ArrayList<String>();
    //二级列表数据(包含备注信息与消费及行为信息两种类别，后两种一样的类型)
    private List<RemarkBaseInfo> childInfos=new ArrayList<RemarkBaseInfo>();
    //一级数据和二级数据集合
    private   Map<String, List<RemarkBaseInfo>> remarkAllDatas = new LinkedHashMap<String, List<RemarkBaseInfo>>();
    private  Map<String, List<RemarkBaseInfo>> copyRemarkAllDatas = new LinkedHashMap<String, List<RemarkBaseInfo>>();
    private int pageIndexRemark=1;
    private LinearLayout head;
    private boolean isLastData;
    private int pageIndexComsum=1;
    private int pageIndexBehavior=1;
    private Intent intent;
    private List<TagModel> tagList;
    private OutLineFansDetailAct context;
    private List<String> chooseTagIds=new ArrayList<String>();
    private boolean isEmptyRemark=true;
    private boolean isEmptyConsumption=true;
    private boolean isEmptyBehavior=true;
    private boolean showDefault=true;
    private TextView txt_stickgroup;
    private int indicatorGroupHeight;
    private int indicatorGroupId;
    private TextView tv_head_remarks;
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
    private String[] label;
    private String chatName;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.win_outline_fansdetail);
        initView();
        loadData();
    }

    ParameCallBack callBackRefresh=new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            showDefault = false;
            //写备注的数据  只传此页面需要的数据
            MenberInfoModel callBackmenberDtail= (MenberInfoModel) object;
            if(menberDtail!=null){
                menberDtail.setRemarkName(callBackmenberDtail.getRemarkName());
                menberDtail.setLabels(callBackmenberDtail.getLabels());
            }
            fillData(menberDtail);
            loadNewConsumptionDatakData(menberId,1,false,false);
        }
    };


    /**
     * 会员基本信息获取
     */
    private void loadData() {
        final GetMenberDetailRequest getMenberDetailRequest = new GetMenberDetailRequest(menberId);
        getMenberDetailRequest.start();
        getMenberDetailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                menberDtail = getMenberDetailRequest.getMenberDtail();
                newBehaviorCount=menberDtail.getNewBehaviorCount();
                fillData(menberDtail);
                if(showDefault){
                    //从客多多首页进入
                    showDefaultType(menberDtail);
                }else {
                    //从写会员备注保存后返回
                    loadRemarkData(menberId,1,false,false);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    /**
     * 进入详情页面 检查是否有新的轨迹消息  优先检查消费轨迹再行为轨迹 并打开
     * @param menberDtail
     */
    private void showDefaultType(MenberInfoModel menberDtail) {
        if(menberDtail.getIsNewConsumption()==1){
            //由于详情的组别 顺序不会改变  所以直接打开对应序号
            loadNewConsumptionDatakData(menberId,1,false,false);
            //没有新行为轨迹就预加载
            if(menberDtail.getIsNewBehavior()==0){
                loadNewBehaviorData(menberId,1,false);
            }
            //优先展示消费轨迹
            return;
        }else if(menberDtail.getIsNewBehavior()==1){
            //没有新的消费轨迹就预加载
            if(menberDtail.getIsNewConsumption()==0){
                loadNewBehaviorData(menberId,1,false);
            }
            loadNewConsumptionDatakData(menberId,1,false,false);
        }else {
            //没有默认打开的组 那就预加载  避免出现点击分组会闪现默认提示
            if(menberDtail.getIsNewConsumption()==0&&menberDtail.getIsNewBehavior()==0){
                loadNewConsumptionDatakData(menberId,1,false,false);
                loadNewBehaviorData(menberId,1,false);
            }
        }
    }
    /**
     * 填充数据
     * @param menberDtail
     */
    private void fillData(MenberInfoModel menberDtail) {
        head_image.setImageUrl(menberDtail.getHeadUrl(), R.drawable.default_head_big);
        gender.setImageResource(menberDtail.getGender()==2?R.drawable.icon_women:R.drawable.icon_man);
        tv_name.setText(menberDtail.getNickname());
        if(!TextUtils.isEmpty(menberDtail.getRemarkName())){
            tv_name.setText(menberDtail.getRemarkName());
            chatName =menberDtail.getRemarkName();
        }else if(!TextUtils.isEmpty(menberDtail.getNickname())){
            tv_name.setText(menberDtail.getNickname());
            chatName =menberDtail.getNickname();
        }else {
            tv_name.setText(menberDtail.getUserName());
            chatName =menberDtail.getUserName();
        }
        brand.setText(menberDtail.getDeviceName());
        phone_num.setText(menberDtail.getMobilePhone());
        //预加载 不然会出现子项第一次打开显示默认布局的bug
        loadRemarkData(menberId,1,false,false);
    }

    private void initView() {
        menberId =getIntent().getLongExtra("id",0);
        head = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.head_fans_detail,null,false);
        head_image = (CircleImageView) head.findViewById(R.id.head_image);
        gender = (ImageView) head.findViewById(R.id.gender);
        tv_name = (TextView) head.findViewById(R.id.name);
        phone_num = (TextView) head.findViewById(R.id.phone_num);
        brand = (TextView) head.findViewById(R.id.brand);
        btn_send_message = (Button) head.findViewById(R.id.btn_send_message);
        tv_tuisong = (LinearLayout) head.findViewById(R.id.tv_tuisong);
        tv_sendmsg = (LinearLayout) head.findViewById(R.id.tv_sendmsg);
        tv_call = (LinearLayout) head.findViewById(R.id.tv_call);
        ((TextView)findViewById(R.id.txt_title)).setText("详情");

        findViewById(R.id.btn_back).setOnClickListener(this);
        head_image.setOnClickListener(this);
        btn_send_message.setOnClickListener(this);
        tv_tuisong.setOnClickListener(this);
        tv_sendmsg.setOnClickListener(this);
        tv_call.setOnClickListener(this);
        expandableListView = (PullToRefreshExpandableListView) findViewById(R.id.expand_listview);
        expandableListView.getRefreshableView().setSelector(android.R.color.transparent);
        expandableListView.getRefreshableView().setOnGroupClickListener(this);
        expandableListView.setPullToRefreshOverScrollEnabled(true);          //可刷新
        expandableListView.setMode(PullToRefreshBase.Mode.BOTH);
        expandableListView.getRefreshableView().setDivider(null);
        expandableListView.getRefreshableView().setGroupIndicator(null);
        expandableListView.setOnRefreshListener(this);
        ViewUtils.setRefreshListText(expandableListView);
        if (outLineFansDetailAdapter == null) {
            outLineFansDetailAdapter = new OutLineFansDetailAdapter(this);
        }
        footerView =(RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_expand_footer,null,false);
        if(PreferUtil.getInstance().getIsFirstOpenMenbersDetail()){
            initFooterView();
        }
        expandableListView.getRefreshableView().addHeaderView(head);
        expandableListView.getRefreshableView().setAdapter(outLineFansDetailAdapter);
        //顶部滑动时常驻头部布局
        stick_head = (LinearLayout) findViewById(R.id.stick_head);
        tv_head_remarks = (TextView) findViewById(R.id.tv_head_remarks);
        txt_stickgroup = (TextView) findViewById(R.id.txt_stickgroup);
        stick_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < groupStrings.length; i++) {
                    expandableListView.getRefreshableView().collapseGroup(i);
                }
            }
        });
        tv_head_remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteMenbersRemarksAct.start( context,menberDtail.getRemarkName(),menberDtail.getNickname(),menberDtail.getGender(),menberDtail.getMobilePhone(),menberDtail.getId(),_callBack,callBackRefresh);
            }
        });

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
                    if(gid!=0){
                        tv_head_remarks.setVisibility(View.GONE);
                    }
                    txt_stickgroup.setText(groupStrings[gid]);
                }

                if (indicatorGroupHeight == 0) {
                    return;
                }

                if (gid !=   indicatorGroupId) {// 如果指示器显示的不是当前group
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
        groupStrings = new String[]{"备注信息", "消费轨迹", "行为轨迹"};
        for (int i = 0; i < groupStrings.length; i++) {
            putDatas(groupStrings[i],childInfos);
            outLineFansDetailAdapter.addContent(remarkAllDatas);
            outLineFansDetailAdapter.notifyDataSetChanged();
            copyRemarkAllDatas.put(groupStrings[i],childInfos);
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
        text1.setText("客户虐我千万遍,我待客户如初恋");
        text2.setText("如何维护与老客户之间的感情?猛戳教程");
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.setGone(footerView);
            }
        });
        final YamLessonRequest yamLessonRequest = new YamLessonRequest(3);
        yamLessonRequest.start();
        yamLessonRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                yamLessonInfo = yamLessonRequest.getYamLessonInfo();
                label =yamLessonInfo.getLabel();
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
     * 获取备注信息类数据
     * @param menberId
     * @param pageIndex
     */
    private void loadRemarkData(long menberId, final int pageIndex, final boolean isRefrensh,boolean isShowDialog){
        final GetNewRemarkReqeust getNewRemarkReqeust = new GetNewRemarkReqeust(menberId,pageIndex,isShowDialog);
        getNewRemarkReqeust.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<RemarkBaseInfo> newRemarkInfoList = getNewRemarkReqeust.getRemarkInfoModelList();
                expandableListView.onRefreshComplete();
                boolean empty = newRemarkInfoList.isEmpty();
                if(empty){
                    //如果是空
                    if (pageIndex == 1) {
                        newRemarkInfoList.clear();
                        isEmptyRemark = true;
                        emptyRemark="您还没对这位会员添加任何备注哦,立即备注吧";
                    } else {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }
                }else{
                    //获取子数据返回成功后，标识该组子数据已加载过
                    isEmptyRemark = false;
                    pageIndexRemark++;
                    if(!isRefrensh){
                        hasClickGroup.add("备注信息");
                        putDatas("备注信息", newRemarkInfoList);
                        outLineFansDetailAdapter.addContent(remarkAllDatas);
                        outLineFansDetailAdapter.notifyDataSetChanged();
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
        outLineFansDetailAdapter.addContent(remarkAllDatas);
        outLineFansDetailAdapter.notifyDataSetChanged();
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
        outLineFansDetailAdapter.addContent(remarkAllDatas);
        outLineFansDetailAdapter.notifyDataSetChanged();
    }

    /**
     * 清楚该会员在会员列表轨迹红点及组轨迹数减一的回调
     */
    private void clearMenberBehavoirPoint() {
        hasClear =true;
        if(_clearBehavoirCallBack!=null){
            _clearBehavoirCallBack.onCall(menberId);
        }
    }

    /**
     * 获取消费轨迹
     * @param menberId
     * @param pageIndex
     */
    private void loadNewConsumptionDatakData(long menberId, final int pageIndex, final boolean isRefrensh,boolean showDialog){
        final GetNewConsumptionDataRequest getNewConsumptionDataReqeust = new GetNewConsumptionDataRequest(menberId, pageIndex,showDialog);
        getNewConsumptionDataReqeust.start();
        getNewConsumptionDataReqeust.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<RemarkBaseInfo> menberConsumptionDataList = getNewConsumptionDataReqeust
                        .getMenberConsumptionDataList();
                expandableListView.onRefreshComplete();
                boolean empty = menberConsumptionDataList.isEmpty();
                if(empty){
                    //如果是空
                    if (pageIndex == 1) {
                        isEmptyConsumption = true;
                        emptyComsuption="暂无消费轨迹,点击推送送点好料~";
                    } else {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }
                }else{
                    //获取子数据返回成功后，标识该组子数据已加载过
                    isEmptyConsumption = false;
                    pageIndexComsum++;
                    if(!isRefrensh){
                        hasClickGroup.add("消费轨迹");
                        putDatas("消费轨迹", menberConsumptionDataList);
                        outLineFansDetailAdapter.addContent(remarkAllDatas);
                        outLineFansDetailAdapter.notifyDataSetChanged();
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
                }
                if(menberDtail.getIsNewConsumption()==1){
                    for (RemarkBaseInfo remarkBaseInfo : menberConsumptionDataList) {
                        if (remarkBaseInfo.getIsNew()==1){
                            newBehaviorCount--;
                        }
                    }
                    if(!hasClear&&newBehaviorCount<=0){
                        clearMenberBehavoirPoint();
                    }
                    expandableListView.getRefreshableView().expandGroup(1);
                }else {
                    if(menberDtail.getIsNewBehavior()!=1&&!menberConsumptionDataList.isEmpty()){
                        expandableListView.getRefreshableView().expandGroup(1);
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
    private void loadNewBehaviorData(final long menberId, final int pageIndex, final boolean isRefrensh){
        final GetNewBehaviorRequest getNewBehaviorRequest = new GetNewBehaviorRequest(menberId, pageIndex);
        getNewBehaviorRequest.start();
        getNewBehaviorRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<RemarkBaseInfo> behaviorDataList = getNewBehaviorRequest.getNewBehaviorDataList();
                expandableListView.onRefreshComplete();
                boolean empty = behaviorDataList.isEmpty();
                if(empty){
                    //如果是空
                    if (pageIndex == 1) {
                        isEmptyBehavior = true;
                        emptyBehavior="暂无行为轨迹,点击推送送点好料~";
                    } else {
                        isLastData = true;
                        ViewUtils.showToast("亲! 没有更多了");
                    }
                }else{
                    //获取子数据返回成功后，标识该组子数据已加载过
                    isEmptyBehavior = false;
                    pageIndexBehavior++;
                    if(!isRefrensh){
                        hasClickGroup.add("行为轨迹");
                        putDatas("行为轨迹", behaviorDataList);
                        outLineFansDetailAdapter.addContent(remarkAllDatas);
                        outLineFansDetailAdapter.notifyDataSetChanged();
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
                }
                if(menberDtail.getIsNewBehavior()==1){
                    for (RemarkBaseInfo remarkBaseInfo : behaviorDataList) {
                        if (remarkBaseInfo.getIsNew()==1){
                            newBehaviorCount--;
                        }
                    }
                    if(!hasClear&&newBehaviorCount<=0){
                        clearMenberBehavoirPoint();
                    }
                    if(menberDtail.getIsNewConsumption()!=1&&menberDtail.getIsNewBehavior()==1){
                        expandableListView.getRefreshableView().expandGroup(2);
                    }
                }else {
                    if(menberDtail.getIsNewConsumption()!=1&&!behaviorDataList.isEmpty()&&!expandableListView.getRefreshableView().isGroupExpanded(1)){
                        expandableListView.getRefreshableView().expandGroup(2);
                    }
                }


            }

            @Override
            public void onFail(int code) {

            }
        });
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

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        if(!parent.isGroupExpanded(groupPosition)){
            if(groupPosition==0){
                if(!hasClickGroup.contains("备注信息")){
                    //之前未加载过数据那么就加载数据
                    loadRemarkData(menberId,1,false,true);
                }else{
                    //再次点击 之前已经有加载数据那么就用缓存数据
                    useCacheData("备注信息",true);
                }
            }
            if(groupPosition==1){
                //是否有消费
                if(!hasClickGroup.contains("消费轨迹")){
                    loadNewConsumptionDatakData(menberId, 1,false,true);
                }else{
                    useCacheData("消费轨迹",true);
                }
            }
            if(groupPosition==2){
                if(!hasClickGroup.contains("行为轨迹")){
                    loadNewBehaviorData(menberId, 1,false);
                }else{
                    //如果有消费轨迹也有行为轨迹  那么会先打开消费轨迹  但行为轨迹的已经加载过数据了
                    if(menberDtail.getIsNewConsumption()==1&&menberDtail.getIsNewBehavior()==1&&!isFirstUseBehaviorCache){
                        isFirstUseBehaviorCache =false;
                    }
                    if(menberDtail.getIsNewConsumption()==0&&menberDtail.getIsNewBehavior()==1&&!isFirstUseBehaviorCache){
                        isFirstUseBehaviorCache =true;
                    }
                    useCacheData("行为轨迹",true);
                    isFirstUseBehaviorCache=true;
                }
            }
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
        outLineFansDetailAdapter.addContent(remarkAllDatas);
        outLineFansDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            //聊天
            case R.id.btn_send_message:
                intent = new Intent(this, ChatActivity.class);
                String chatUserId = HXApplication.getInstance().parseUserFromID(menberDtail.getId(),
                        HXConstant.TAG_USER);
                intent.putExtra(HXConstant.EXTRA_USER_ID, chatUserId);
                intent.putExtra("userName",chatName);
                this.startActivity(intent);
                break;
            //推送
            case R.id.tv_tuisong:
                ArrayList<Long> userIds = new ArrayList<Long>();
                userIds.add(menberDtail.getId());
                ChooseSendAct.start(this, null, userIds);
                break;
            //短信
            case R.id.tv_sendmsg:
                SendPhoneMessageAct.start(this,menberId,menberDtail.getMobilePhone(),2);
                break;
            //电话
            case R.id.tv_call:
                //跳转至系统拨打电话界面
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                Uri data = Uri.parse("tel:" +"15800000000");
//                intent.setData(data);
//                startActivity(intent);
                //直接拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +menberDtail.getMobilePhone()));
                startActivity(intent);
                new MarkCallRequest(menberId).start();
                break;
            case R.id.head_image:
                DetailPersonalAct.start(this,menberId);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            reloadBaseData();
            if(expandableListView.getRefreshableView().isGroupExpanded(0)){
                pageIndexRemark=1;
                loadRemarkData(menberId,pageIndexRemark,true,true);
            }else if(expandableListView.getRefreshableView().isGroupExpanded(1)){
                pageIndexComsum=1;
                loadNewConsumptionDatakData(menberId,pageIndexComsum,true,true);
            }else if(expandableListView.getRefreshableView().isGroupExpanded(2)){
                pageIndexBehavior=1;
                loadNewBehaviorData(menberId,pageIndexBehavior,true);
            }else{
                expandableListView.onRefreshComplete();
            }
        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            if(expandableListView.getRefreshableView().isGroupExpanded(0)){
                loadRemarkData(menberId,pageIndexRemark,true,true);
            }else if(expandableListView.getRefreshableView().isGroupExpanded(1)){
                loadNewConsumptionDatakData(menberId,pageIndexComsum,true,true);
            }else if(expandableListView.getRefreshableView().isGroupExpanded(2)){
                loadNewBehaviorData(menberId,pageIndexBehavior,true);
            }else{
                expandableListView.onRefreshComplete();
            }

        }
    }

    private void reloadBaseData() {
        final GetMenberDetailRequest getMenberDetailRequest = new GetMenberDetailRequest(menberId);
        getMenberDetailRequest.start();
        getMenberDetailRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                menberDtail = getMenberDetailRequest.getMenberDtail();
                fillData(menberDtail);
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private class OutLineFansDetailAdapter extends BaseExpandableListAdapter {
        protected Map<String, List<RemarkBaseInfo>> content = new LinkedHashMap<String, List<RemarkBaseInfo>>();
        protected Context context;
        public OutLineFansDetailAdapter(Context context) {
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
                //会否有备注信息
                if(isEmptyRemark){
                    return 1;
                }else {
                    return content.get(content.keySet().toArray()[groupPosition]).size()+1;
                }
            }else if(groupPosition==1){
                //是否有消费轨迹
                if(menberDtail.getIsNewConsumption()==0&&isEmptyConsumption){
                    return 1;
                }else {
                    return content.get(content.keySet().toArray()[groupPosition]).size();
                }
            }else if(groupPosition==2){
                //是否有行为轨迹
                if(menberDtail.getIsNewBehavior()==0&&isEmptyBehavior){
                    return 1;
                }else {
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
                //不能用此方法加载视图  会出现显示不正确的情况
                convertView= LayoutInflater.from(context).inflate(R.layout.item_outlinefans_group,parent,false);
                gViewHolder.group_name= (TextView) convertView.findViewById(R.id.group_name);
                gViewHolder.tv_remarks= (TextView) convertView.findViewById(R.id.tv_group_remarks);
                gViewHolder.icon_arrow= (ImageView) convertView.findViewById(R.id.icon_arrow);
                convertView.setTag(gViewHolder);
            }else{
                gViewHolder= (GroupViewHolder) convertView.getTag();
            }
            //// TODO: 2016/8/27  赋值

            String groupNme = (String) getGroup(groupPosition);
            gViewHolder.group_name.setText(groupNme);
            if(groupPosition==0){
                    gViewHolder.tv_remarks.setVisibility(View.VISIBLE);
            }else{
                gViewHolder.tv_remarks.setVisibility(View.GONE);
            }

            gViewHolder.tv_remarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WriteMenbersRemarksAct.start((Activity) context,menberDtail.getRemarkName(),menberDtail.getNickname(),menberDtail.getGender(),menberDtail.getMobilePhone(),menberDtail.getId(),_callBack,callBackRefresh);

                }
            });
            //设置箭头指向
            if(isExpanded){
                gViewHolder.icon_arrow.setBackgroundResource(R.drawable.turn_up_small);
            }else{
                gViewHolder.icon_arrow.setBackgroundResource(R.drawable.turn_dowm_small);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildFirViewHolder firViewHolder;
            ChildSecViewholder secViewHolder;
            ChildThirdViewHolder thirdViewHolder;
            ChildFourthViewHolder fourthViewHolder;
            ChildFiveViewHolder childFiveViewHolder;
            int type=getChildType(groupPosition,childPosition);
            switch (type){
                case TYPE_1:
                    if(convertView==null){
                        firViewHolder=new ChildFirViewHolder();
                        convertView=LayoutInflater.from(context).inflate(R.layout.item_outlinefans_child,parent,false);
                        firViewHolder.remark_msg= (TextView) convertView.findViewById(R.id.remark_msg);
                        firViewHolder.txt_time= (TextView) convertView.findViewById(R.id.txt_time);
                        firViewHolder.txt_remarker= (TextView) convertView.findViewById(R.id.txt_remarker);
                        convertView.setTag(firViewHolder);
                    }else{
                        firViewHolder= (ChildFirViewHolder) convertView.getTag();
                    }
                    //保障有备注时 是从第一个数据开始填充的
                    RemarkBaseInfo remarkInfoModel = (RemarkBaseInfo) getChild(groupPosition, childPosition-1);
                    //1 普通 2，电话回访 3，短信回访
                    switch (remarkInfoModel.getRemarkType()){
                        case TYPE_COMMON:
                            firViewHolder.remark_msg.setText(remarkInfoModel.getContent());
                            firViewHolder.txt_remarker.setText(remarkInfoModel.getCreatorName()+"备注");
                            break;
                        case TYPE_CALL:
                            firViewHolder.remark_msg.setText("电话回访");
                            firViewHolder.txt_remarker.setText(remarkInfoModel.getCreatorName());
                            break;
                        case TYPE_MESSAGE:
                            firViewHolder.remark_msg.setText("短信回访");
                            firViewHolder.txt_remarker.setText(remarkInfoModel.getCreatorName());
                            break;
                        default:
                            firViewHolder.remark_msg.setText(remarkInfoModel.getContent());
                            firViewHolder.txt_remarker.setText(remarkInfoModel.getCreatorName()+"备注");
                            break;
                    }
                    firViewHolder.txt_time.setText(EaseConstant.formatOrderLongTime(remarkInfoModel.getCreateTime()));
                    break;
                case TYPE_2:
                    if(convertView==null){
                        secViewHolder=new ChildSecViewholder();
                        convertView=LayoutInflater.from(context).inflate(R.layout.item_outline_child_line,parent,false);
                        secViewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
                        secViewHolder.top_line=convertView.findViewById(R.id.top_line);
                        secViewHolder.bottom_line=convertView.findViewById(R.id.bottom_line);
                        secViewHolder.iv_orbit= (ImageView) convertView.findViewById(R.id.iv_orbit);
                        secViewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
                        convertView.setTag(secViewHolder);
                    }else{
                        secViewHolder= (ChildSecViewholder) convertView.getTag();
                    }
                    RemarkBaseInfo remarkBaseInfo = (RemarkBaseInfo) getChild(groupPosition, childPosition);
                    String targetType = new TargetTypeNameConstants(remarkBaseInfo.getTargetType()).getTargetType();
                    String name = new BehaviorType(remarkBaseInfo.getType()).getName();
                    String content = remarkBaseInfo.getContent();
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
                    secViewHolder.tv_time.setText(EaseConstant.formatBehaviorLongTime(remarkBaseInfo.getCreateTime()));
                    break;
                case TYPE_3:
                    if(convertView==null){
                        thirdViewHolder=new ChildThirdViewHolder();
                        convertView=LayoutInflater.from(context).inflate(R.layout.item_fansdetail_typec,parent,false);
                        thirdViewHolder.layout_tag_typec= (TagLayout) convertView.findViewById(R.id.layout_tag_typec);
                        thirdViewHolder.layout_container= (FrameLayout) convertView.findViewById(R.id.layout_container);
                        thirdViewHolder.layout_tag= (RelativeLayout) convertView.findViewById(R.id.layout_tag);
                        thirdViewHolder.notag_layout= (RelativeLayout) convertView.findViewById(R.id.notag_layout);
                        convertView.setTag(thirdViewHolder);
                    }else {
                        thirdViewHolder= (ChildThirdViewHolder) convertView.getTag();
                    }
                    if(menberDtail.getLabels().size()==0||isEmptyRemark){
                        //有备注无标签 该行不显示--也就是第一行不可见
                        if(menberDtail.getLabels().size()==0&&!isEmptyRemark){
                            thirdViewHolder.layout_container.setVisibility(View.GONE);
                            thirdViewHolder.notag_layout.setVisibility(View.GONE);
                            thirdViewHolder.layout_tag.setVisibility(View.GONE);
                        }
                        //有标签无备注 显示标签
                        if(menberDtail.getLabels().size()!=0&&isEmptyRemark){
                            thirdViewHolder.notag_layout.setVisibility(View.GONE);
                            thirdViewHolder.layout_tag.setVisibility(View.VISIBLE);
                            setTagLayout(thirdViewHolder.layout_tag_typec,menberDtail.getLabels());
                        }
                        //无备注无标签  显示默认提示信息
                        if(menberDtail.getLabels().size()==0&&isEmptyRemark){
                            thirdViewHolder.layout_container.setVisibility(View.VISIBLE);
                            thirdViewHolder.notag_layout.setVisibility(View.VISIBLE);
                            thirdViewHolder.layout_tag.setVisibility(View.GONE);
                        }
//                        thirdViewHolder.notag_layout.setVisibility(View.GONE);
                    }else {
                        //有标签有备注  显示标签
                        thirdViewHolder.layout_tag.setVisibility(View.VISIBLE);
                        thirdViewHolder.notag_layout.setVisibility(View.GONE);
                        thirdViewHolder.layout_container.setVisibility(View.VISIBLE);
                        setTagLayout(thirdViewHolder.layout_tag_typec,menberDtail.getLabels());
                    }
                    //设置标签
                    break;
                case TYPE_4:
                    if(convertView==null){
                        fourthViewHolder=new ChildFourthViewHolder();
                        convertView=LayoutInflater.from(context).inflate(R.layout.item_fansdetail_typea,parent,false);
                        fourthViewHolder.btn_remarks= (TextView) convertView.findViewById(R.id.btn_remarks);
                        fourthViewHolder.txt_default_info= (TextView) convertView.findViewById(R.id.txt_default_info);
                        convertView.setTag(fourthViewHolder);
                    }else {
                        fourthViewHolder= (ChildFourthViewHolder) convertView.getTag();
                    }
                    fourthViewHolder.txt_default_info.setText(emptyRemark);
                    fourthViewHolder.btn_remarks.setVisibility(View.GONE);
                    fourthViewHolder.btn_remarks.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            WriteMenbersRemarksAct.start((Activity) context,menberDtail.getUserName(),menberDtail.getNickname(),menberDtail.getGender(),menberDtail.getMobilePhone(),menberDtail.getId(),_callBack,callBackRefresh);
                        }
                    });
                    break;
                case TYPE_5:
                    if(convertView==null){
                        childFiveViewHolder=new ChildFiveViewHolder();
                        convertView=LayoutInflater.from(context).inflate(R.layout.item_fansdetail_typed,parent,false);
                        childFiveViewHolder.txt_default= (TextView) convertView.findViewById(R.id.txt_default);
                        convertView.setTag(childFiveViewHolder);
                    }else {
                        childFiveViewHolder= (ChildFiveViewHolder) convertView.getTag();
                    }
                    if(groupPosition==1){
                        childFiveViewHolder.txt_default.setText(emptyComsuption);
                    }else if(groupPosition==2){
                        childFiveViewHolder.txt_default.setText(emptyBehavior);
                    }
                    break;
            }
            return convertView;
        }

        @Override
        public int getChildType(int groupPosition, int childPosition) {
            if (groupPosition == 0) {
                //无备注时显示的布局
                if(childPosition==0){
//                    if(isEmptyRemark){
//                        return TYPE_4;
//                    }else {
                        return TYPE_3;

//                        if(menberDtail.getLabels().size()!=0){
//                            return TYPE_3;
//                        }else {
//                            return TYPE_1;
//                        }
//                    }
                }else {
                    return  TYPE_1;
                }
            } else if(groupPosition==1){
                if(childPosition==0){
                    if(menberDtail.getIsNewConsumption()==0&&isEmptyConsumption){
                        //没有消费轨迹时
                        return TYPE_5;
                    }else{
                        return TYPE_2;
                    }
                }else {
                    return TYPE_2;
                }

            }else if(groupPosition==2){
                if(childPosition==0){
                    if(menberDtail.getIsNewBehavior()==0&&isEmptyBehavior){
                        //没有行为轨迹
                        return TYPE_5;
                    }else {
                        return TYPE_2;
                    }
                }else {
                    return TYPE_2;
                }
            }else {
                return TYPE_2;
            }
        }
        //二级布局类型数量(虽然只有两种type,但因为子布局有三个 所以实质上也是三种 只不过是有两种一样的  所以需要设置为三)
        //这个地方可以动态的设置成一级列表的个数
        @Override
        public int getChildTypeCount() {
            return 6;
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
            TextView tv_remarks;
            ImageView icon_arrow;
        }
        //第一种二级布局viewholder
        class ChildFirViewHolder {
            ImageView img_notice;
            CircleImageView img_user_head;
            TextView remark_msg,txt_time,txt_remarker;
            View bottom_line;
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
            TagLayout layout_tag_typec;
            FrameLayout layout_container;
            RelativeLayout layout_tag;
            RelativeLayout notag_layout;
        }
        class ChildFourthViewHolder{
            TextView txt_default_info,btn_remarks;
        }
        class ChildFiveViewHolder{
            TextView txt_default;
        }
    }

    private void setTagLayout(TagLayout flowLayout, List<String> labels) {

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.rightMargin = 1;
        lp.bottomMargin = 1;
        lp.leftMargin=1;
        lp.topMargin=1;
        flowLayout.removeAllViews();

        for (int i = 0; i < labels.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_orange_tag, null, false);
            TextView textView = (TextView) view.findViewById(R.id.tv_tag);
            textView.setText(labels.get(i));
            flowLayout.addView(view);
        }
    }

    public static void start(Activity curAct, long menberId){
        Intent it=new Intent();
        it.putExtra("id",menberId);
        it.setClass(curAct, OutLineFansDetailAct.class);
        ViewUtils.startActivity(it, curAct);
    }
    public static void startCallBack(Activity curAct, long menberId,ParameCallBack callBack,ParameCallBack clearBehavoirCallBack){
        _callBack=callBack;
        _clearBehavoirCallBack =clearBehavoirCallBack;
        Intent it=new Intent();
        it.putExtra("id",menberId);
        it.setClass(curAct, OutLineFansDetailAct.class);
        ViewUtils.startActivity(it, curAct);
    }
}
