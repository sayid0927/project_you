package com.zxly.o2o.fragment;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.ChooseGroupPeopleAct;
import com.zxly.o2o.activity.FansAddAct;
import com.zxly.o2o.activity.FansDetailNewAct;
import com.zxly.o2o.activity.MainActivity;
import com.zxly.o2o.activity.OffLineFansEnteringAct;
import com.zxly.o2o.activity.YamCollegeDetailAct;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.FansGroupModel;
import com.zxly.o2o.model.FansInfo;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshExpandableListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FansFocusRequest;
import com.zxly.o2o.request.GetFansGroupRequest;
import com.zxly.o2o.request.GetFansInfoRequest;
import com.zxly.o2o.request.YamLessonRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.CollegeCourseView;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejun on 2016/8/24.
 */
public class FansListFragment extends BaseFragment implements ExpandableListView.OnGroupClickListener,
        PullToRefreshBase.OnRefreshListener {


    private PullToRefreshExpandableListView expandableListView;
    private LoadingView loadingView;
    private FansListAdapter fansListAdapter;
    private RelativeLayout layout_nodata_admin;
    private int expandGroupPosition = -1;
    //用来存放，没有二级列表的组名 作用：当点击没有二级数据的组时  不显示推送字样
    private List<FansGroupModel> noChildDataGroups = new ArrayList<FansGroupModel>();
    //一列列表数据
    private List<FansGroupModel> fansGroups = new ArrayList<FansGroupModel>();
    //二级列表数据
    private List<FansInfo> fansInfos = new ArrayList<FansInfo>();
    //一级数据和二级数据集合
    private Map<FansGroupModel, List<FansInfo>> fansAllDatas = new LinkedHashMap<FansGroupModel, List<FansInfo>>();
    private Map<FansGroupModel, List<FansInfo>> copyFansAllDatas = new LinkedHashMap<FansGroupModel, List<FansInfo>>();
    private boolean hasInit;
    private CollegeCourseView collegeCourseView;
    private RelativeLayout layout_nodata_salesman;
    private Button btn_gonext;
    private ImageView iv_nodata_admin;
    private FansGroupModel refreshGroup = new FansGroupModel();
    private ParameCallBack changeFansCallBack;
    private FansInfo refreshFans;
    private ParameCallBack _fansCountCallBack;
    private List<FansGroupModel> hasClickGroup = new ArrayList<FansGroupModel>();
    private FansGroupModel unGroup;
    private int outLineFansCount = -1;
    private FansGroupModel preLoadGroup;
    private ImageView iv_salsman;
    private int outLineFansGroupCount;
    private int lessonId;

    @Override
    protected void initView() {
        expandableListView = (PullToRefreshExpandableListView) findViewById(R.id.expand_listview);
        expandableListView.getRefreshableView().setSelector(android.R.color.transparent);
        expandableListView.getRefreshableView().setOnGroupClickListener(this);
        expandableListView.setPullToRefreshOverScrollEnabled(true);          //可刷新
        expandableListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        expandableListView.getRefreshableView().setDivider(null);//设置模式，此模式是可以上拉，
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        expandableListView.getRefreshableView().setGroupIndicator(null);
        ViewUtils.setRefreshListFromStartText(expandableListView);
        expandableListView.setOnRefreshListener(this);

        //无粉丝时且身份为店长时显示的布局
        layout_nodata_admin = (RelativeLayout) findViewById(R.id.layout_nodata_admin);
        iv_nodata_admin = (ImageView) findViewById(R.id.iv_nodata_admin);
        //无粉丝时身份为业务员时显示的布局
        layout_nodata_salesman = (RelativeLayout) findViewById(R.id.layout_nodata_salesman);
        iv_salsman = (ImageView) findViewById(R.id.iv_salsman);
        btn_gonext = (Button) findViewById(R.id.btn_gonext);
        btn_gonext.setEnabled(false);
//        showDefaultView(Config.fansCount);
        if (fansListAdapter == null) {
            fansListAdapter = new FansListAdapter(getActivity());
        }
        expandableListView.getRefreshableView().setAdapter(fansListAdapter);
//        findViewById(R.id.edit_search).setFocusable(false);
//        ((EditText) findViewById(R.id.edit_search)).setHint("请输入姓名或手机号");
//        findViewById(R.id.edit_search).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SeachPeopleFilterFirstAct.start(getActivity());
//            }
//        });

        expandableListView.getRefreshableView().setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener
                () {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int y = 0; y < expandableListView.getRefreshableView().getExpandableListAdapter().getGroupCount
                        (); y++) {
                    if (y != groupPosition) {
                        expandableListView.getRefreshableView().collapseGroup(y);
                    }
                }
            }
        });


    }

    /**
     * 显示默认图层
     *
     * @param fansCount
     */
    public void showDefaultView(int fansCount) {
        //处理无粉丝与有粉丝第一次进入的情况时显示默认图层
        if (Account.user.getRoleType() == Constants.USER_TYPE_ADMIN) {
            if (fansCount == 0) {
                if (PreferUtil.getInstance().getIsFirstOpenFans()) {
                    layout_nodata_admin.setVisibility(View.VISIBLE);
                    showCollegeCourse();
                    layout_nodata_admin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_nodata_admin.setVisibility(View.GONE);
                            if (collegeCourseView != null) {
                                collegeCourseView.setVisibility(View.GONE);
                            }
                        }
                    });
                }

            } else {
                //有粉丝但是是第一次进入该页面
                if (PreferUtil.getInstance().getIsFirstOpenFans()) {
                    iv_nodata_admin.setBackgroundResource(R.drawable.icon_nodata_brand);
                    layout_nodata_admin.setVisibility(View.VISIBLE);
                    showCollegeCourse();
                    layout_nodata_admin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_nodata_admin.setVisibility(View.GONE);
                            if (collegeCourseView != null) {
                                collegeCourseView.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    layout_nodata_admin.setVisibility(View.GONE);
                    if (collegeCourseView != null) {
                        collegeCourseView.setVisibility(View.GONE);
                    }
                }
            }
        } else {
//            if (collegeCourseView == null) {
//                collegeCourseView = new CollegeCourseView(getActivity(), 1);
//            }
            getLessonId();
            if (fansCount == 0) {
                if (PreferUtil.getInstance().getIsFirstOpenFans()) {
                    layout_nodata_salesman.setVisibility(View.VISIBLE);
                    layout_nodata_salesman.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_nodata_salesman.setVisibility(View.GONE);
                        }
                    });
                }

            } else {
                //有粉丝但是是第一次进入该页面
                if (PreferUtil.getInstance().getIsFirstOpenFans()) {
//                    btn_gonext.setBackgroundResource(R.drawable.icon_nodata_brand);
                    iv_salsman.setBackgroundResource(R.drawable.icon_nodata_brand);
                    layout_nodata_salesman.setVisibility(View.VISIBLE);
                    layout_nodata_salesman.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_nodata_salesman.setVisibility(View.GONE);
                        }
                    });
                } else {
                    layout_nodata_salesman.setVisibility(View.GONE);
                }
            }

            btn_gonext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YamCollegeDetailAct.start(getActivity(), lessonId);
                }
            });
        }

    }

    private void getLessonId() {
        final YamLessonRequest yamLessonRequest = new YamLessonRequest(1);
        yamLessonRequest.start();
        yamLessonRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (yamLessonRequest.getYamLessonInfo() != null) {
                    btn_gonext.setEnabled(true);
                    lessonId = yamLessonRequest.getYamLessonInfo().getId();
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    public void setHasInit(boolean b) {
        hasInit = b;
    }

    public void onResume() {
        super.onResume();
        if (!hasInit && Account.user != null && 0 == ((MainActivity) getActivity()).fragmentController.getCurrentTab
                ()) {

            loadGroupData(false);
        }
        hasInit = true;
    }

    /**
     * 获取一级列表数据
     */
    private void loadGroupData(boolean showDialog) {
        final GetFansGroupRequest getFansGroupRequest = new GetFansGroupRequest(showDialog);
        getFansGroupRequest.start();
        getFansGroupRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                hasClickGroup.clear();
                fansAllDatas.clear();
                copyFansAllDatas.clear();
                expandableListView.onRefreshComplete();
                loadingView.onLoadingComplete();
                fansGroups = getFansGroupRequest.getFansGroups();
                //二次验证是否显示默认界面
                Config.fansCount = getFansGroupRequest.getFansNum();
                //单独找出“线下录入粉丝”组人数
                Config.outLineFansGroupCount = fansGroups.get(0).getNum();
//                showDefaultView(getFansGroupRequest.getFansNum());
                if (_fansCountCallBack != null) {
                    _fansCountCallBack.onCall(getFansGroupRequest.getFansNum());
                }
                for (int i = 0; i < fansGroups.size(); i++) {
                    putDatas(fansGroups.get(i), fansInfos);
                    //预加载最后一组
                    if (i == fansGroups.size() - 1) {
                        preLoadGroup = fansGroups.get(i);
                    }
                }
                fansListAdapter.addContent(fansAllDatas);
                fansListAdapter.notifyDataSetChanged();
                showBottomRedPoint(fansGroups);
                //单独预加载最后这组 如果该组有成员
                if (preLoadGroup.getNum() > 0) {
                    loadFansData(preLoadGroup, false);
                }
//                PreferUtil.getInstance().setIsFirstOpenFans();
                UmengUtil.onEvent(getActivity(), "home_refresh_suc", null);
            }

            @Override
            public void onFail(int code) {
                expandableListView.onRefreshComplete();
                fansListAdapter.notifyDataSetChanged();
                loadingView.onLoadingFail();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("首页刷新失败", String.valueOf(code));
                UmengUtil.onEvent(getActivity(), "home_refresh_fail", map);
            }
        });

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                getFansGroupRequest.start(this);
            }
        });
    }

    private void loadFansData(final FansGroupModel fansGroupModel, boolean showDialog) {
        final GetFansInfoRequest getFansInfoRequest = new GetFansInfoRequest(fansGroupModel.getGroup(), showDialog);
        getFansInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<FansInfo> fansInfoList = getFansInfoRequest.getFansInfoList();
                boolean empty = getFansInfoRequest.getFansInfoList().isEmpty();
                //判断用户用户已经加载过组列表时，点击组加载该组粉丝的过程中有粉丝刚好注册成为了会员
                //导致组员数与粉丝真实数目不一致
                if (fansGroupModel.getNum() != fansInfoList.size()) {
                    ViewUtils.showToast("该粉丝列表已刷新");
                    fansGroupModel.setNum(fansInfoList.size());
                    if (fansInfoList.size() == 0) {
                        loadGroupData(true);
                        for (int i = 0; i < fansInfoList.size(); i++) {
                            expandableListView.getRefreshableView().collapseGroup(i);
                        }
                        return;
                    }
                }
                if (empty) {
                    //如果是空  那么在适配器中根据groupName  来判断点击组名时  推送 字样不显示
                    noChildDataGroups.add(fansGroupModel);
                } else {
                    //获取子数据返回成功后，标识该组子数据已加载过
                    fansGroupModel.setHasClick(true);
                    hasClickGroup.add(fansGroupModel);
                    if (fansGroupModel.getGroup().equals("线下录入粉丝") || fansGroupModel.getGroup().equals("新粉丝") ||
                            fansGroupModel.getGroup().equals("我关注的粉丝")) {
                    } else {
                        sortHasBehavior(fansInfoList);
                    }
                    putDatas(fansGroupModel, fansInfoList);
                    fansListAdapter.addContent(fansAllDatas);
                    //第一次加载完子数据后就将其保存在该map中
                    copyFansAllDatas.put(fansGroupModel, fansInfoList);
                    fansListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        getFansInfoRequest.start();
    }

    /**
     * 将有新轨迹的粉丝置顶
     *
     * @param fansInfoList
     */
    private void sortHasBehavior(List<FansInfo> fansInfoList) {
        //储存所有有新轨迹的会员集合
        List<FansInfo> hasNewList = new ArrayList<FansInfo>();
        Iterator<FansInfo> iterator = fansInfoList.iterator();
        while (iterator.hasNext()) {
            FansInfo next = iterator.next();
            //显示红点的会员
            if (next.getHasNew() == 1) {
                hasNewList.add(next);
                iterator.remove();
            }
        }
        fansInfoList.addAll(0, hasNewList);
    }

    @Override
    protected int layoutId() {
        return R.layout.win_fans_layout;
    }

    /**
     * 显示柚安米课程
     */
    private void showCollegeCourse() {
        if (collegeCourseView == null) {
            RelativeLayout.LayoutParams courseLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            courseLp.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
            courseLp.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            courseLp.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            courseLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            collegeCourseView = new CollegeCourseView(getActivity(), 1);
            collegeCourseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YamCollegeDetailAct.start(getActivity(), collegeCourseView.getId());
                }
            });
            content.addView(collegeCourseView, courseLp);
        } else {
            collegeCourseView.show();
        }

    }

    /**
     * 回调目的：在现在录入粉丝界面新增线下粉丝后要手动将线下粉丝组数目加一
     */
    CallBack offLineEnterAddNumCallBack = new CallBack() {
        @Override
        public void onCall() {
//            for (int i = 0; i < fansGroups.size(); i++) {
//                FansGroupModel outLinefansGroupModel = fansGroups.get(i);
//                if (outLinefansGroupModel.getGroup().contains("线下录入粉丝")) {
//                    //找出我关注的粉丝组
////                    outLinefansGroupModel.setNum(outLinefansGroupModel.getNum()+1);
//                    putDatas(outLinefansGroupModel, new ArrayList<FansInfo>());
//                    break;
//                }
//            }
//            fansListAdapter.addContent(fansAllDatas);
//            fansListAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 在线下录入粉丝中点击粉丝关注或者取消成功后的回调
     * 目的：更新我关注的粉丝组中的数据显示
     */
    ParameCallBack offLineEnterCallBack = new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            FansGroupModel fansGroupFoucus = new FansGroupModel();
            FansInfo childInfo = (FansInfo) object;
            for (int i = 0; i < fansGroups.size(); i++) {
                if (fansGroups.get(i).getGroup().contains("我关注的粉丝")) {
                    //找出我关注的粉丝组
                    fansGroupFoucus = fansGroups.get(i);
                }
            }
            //如果我关注的组已经加载过  那么就将该粉丝添加进去 否则直接将组的粉丝数量加一
            if (hasClickGroup.contains(fansGroupFoucus)) {
                //该粉丝回调过来是已关注的状态
                if (childInfo.getIsFocus() == 1) {
                    fansGroupFoucus.setNum(fansGroupFoucus.getNum() + 1);
                    List<FansInfo> fansFocusInfos = copyFansAllDatas.get(fansGroupFoucus);
                    if (fansFocusInfos != null) {
                        fansFocusInfos.add(childInfo);
                    }
                    putDatas(fansGroupFoucus, fansFocusInfos);
                } else {
                    fansGroupFoucus.setNum(fansGroupFoucus.getNum() - 1);
                    List<FansInfo> fansFocusInfos = copyFansAllDatas.get(fansGroupFoucus);
                    if (fansFocusInfos != null) {
                        fansFocusInfos.remove(childInfo);
                    }
                    putDatas(fansGroupFoucus, fansFocusInfos);
                }
            } else {
                //该粉丝回调过来是wei关注的状态
                if (childInfo.getIsFocus() == 1) {
                    fansGroupFoucus.setNum(fansGroupFoucus.getNum() + 1);
                } else {
                    fansGroupFoucus.setNum(fansGroupFoucus.getNum() - 1);
                }
                List<FansInfo> fansFocusInfos = copyFansAllDatas.get(fansGroupFoucus);
                if (fansFocusInfos == null) {
                    fansFocusInfos = new ArrayList<FansInfo>();
                }
                putDatas(fansGroupFoucus, fansFocusInfos);
            }
            fansListAdapter.addContent(fansAllDatas);
            fansListAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        //点击
        FansGroupModel fansGroupModel = (FansGroupModel) fansAllDatas.keySet().toArray()[groupPosition];
        if (groupPosition == 0) {
            //跳转至独立页面
            OffLineFansEnteringAct.start(getActivity(), offLineEnterCallBack, offLineEnterAddNumCallBack);

            UmengUtil.onEvent(getActivity(), new UmengUtil().FANS_OFFLINEFANS_CLICK, null);
            return true;
        }
        //如果没有二级数据 或者 该组二级数据num属性不为0但实际没数据返回那么就不让其展开
        if (fansGroupModel.getNum() == 0 || noChildDataGroups.contains(fansGroupModel)) {
            fansGroupModel.setShowTuisong(false);
            expandGroupPosition = -1;
            return true;
        }
        //如果子数据已经成功加载过了，那么就直接使用缓存数据
        //该组是闭合状态就做以下操作 否则不做任何操作
        if (!parent.isGroupExpanded(groupPosition)) {
            if (fansGroupModel.isHasClick()) {
                List<FansInfo> fansInfos = copyFansAllDatas.get(fansGroupModel);
                putDatas(fansGroupModel, fansInfos);
                fansListAdapter.addContent(fansAllDatas);
                fansListAdapter.notifyDataSetChanged();
            } else {
                loadFansData(fansGroupModel, true);
                fansListAdapter.notifyDataSetChanged();
            }
            expandGroupPosition = groupPosition;
        } else {
            expandGroupPosition = -1;
        }

        //false 展开  true 不展开
        return false;
    }

    /**
     * 将数据保存在map集合中
     *
     * @param fansGroupModel 组
     * @param fansDatas      该组下的数据
     * @return 返回map
     */
    private void putDatas(FansGroupModel fansGroupModel, List<FansInfo> fansDatas) {
        fansAllDatas.put(fansGroupModel, fansDatas);
    }


    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

        UmengUtil.onEvent(getActivity(), "home_refresh", null);

        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            int groupCount = expandableListView.getRefreshableView().getExpandableListAdapter().getGroupCount();
            //清除旧数据
            hasClickGroup.clear();
            copyFansAllDatas.clear();
            fansAllDatas.clear();
            expandGroupPosition = -1;
            for (int i = 0; i < groupCount; i++) {
                expandableListView.getRefreshableView().collapseGroup(i);
            }

            //加载数据

            loadGroupData(false);

        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
          /*  // 加载上拉数据
            for (int i = 0; i < groupCount; i++) {
                expandableListView.getRefreshableView().collapseGroup(i);
            }
            //加载数据
            loadGroupData(true);*/
            mMainHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    expandableListView.onRefreshComplete();
                }
            }, 500);
        }
    }

    public void onDataRefresh() {
        int groupCount = expandableListView.getRefreshableView().getExpandableListAdapter().getGroupCount();
        //清除旧数据
        copyFansAllDatas.clear();
        fansAllDatas.clear();
        expandGroupPosition = -1;
        for (int i = 0; i < groupCount; i++) {
            expandableListView.getRefreshableView().collapseGroup(i);
        }
        //加载数据
        loadGroupData(true);
    }

    public void setFansCoutCallBack(ParameCallBack fansCountCallBack) {
        this._fansCountCallBack = fansCountCallBack;
    }


    private class FansListAdapter extends BaseExpandableListAdapter {
        protected Map<FansGroupModel, List<FansInfo>> content = new LinkedHashMap<FansGroupModel, List<FansInfo>>();
        protected Context context;

        public FansListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getGroupCount() {
            return content.keySet().size();
        }

        public void clear() {
            content.clear();
        }

        public void addContent(Map<FansGroupModel, List<FansInfo>> _content) {
            if (content.isEmpty()) {
                content.putAll(_content);
            } else {
                content = _content;
            }
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return content.get(content.keySet().toArray()[groupPosition]).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return content.keySet().toArray()[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (getChildrenCount(groupPosition) == 0) {
                return new ArrayList<FansInfo>();
            } else {
                return content.get(content.keySet().toArray()[groupPosition]).get(childPosition);
            }
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
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder gViewHolder;
            if (convertView == null) {
                gViewHolder = new GroupViewHolder();
                //不能用此方法加载视图  会出现显示不正确的情况
//                convertView=inflateConvertView(R.layout.item_fans_group);
                convertView = LayoutInflater.from(context).inflate(R.layout.item_fans_group, parent, false);
                //子列表活动轨迹数
                gViewHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
                //组名
                gViewHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
                //组员数目
                gViewHolder.tv_child_cout = (TextView) convertView.findViewById(R.id.tv_child_cout);
                //推送按钮
                gViewHolder.tv_sendmessage = (TextView) convertView.findViewById(R.id.tv_sendmessage);
                gViewHolder.top_line = convertView.findViewById(R.id.top_line);
                gViewHolder.bottom_line = convertView.findViewById(R.id.bottom_line);
                gViewHolder.bottom_view = convertView.findViewById(R.id.bottom_view);
                gViewHolder.group_img = (ImageView) convertView.findViewById(R.id.group_img);
                gViewHolder.tv_addNew = (TextView) convertView.findViewById(R.id.tv_addNew);
                convertView.setTag(gViewHolder);
            } else {
                gViewHolder = (GroupViewHolder) convertView.getTag();
            }
            //// TODO: 2016/8/27  赋值

            final FansGroupModel fansGroupModel = (FansGroupModel) getGroup(groupPosition);
            gViewHolder.tv_group_name.setText(fansGroupModel.getGroup());
            gViewHolder.tv_child_cout.setText("(" + fansGroupModel.getNum() + ")");

            if (groupPosition == 0) {
                gViewHolder.tv_count.setVisibility(View.GONE);
                gViewHolder.top_line.setVisibility(View.GONE);
                gViewHolder.bottom_view.setVisibility(View.VISIBLE);
                gViewHolder.group_img.setBackgroundResource(R.drawable.icon_outline);
            } else {
                gViewHolder.top_line.setVisibility(View.VISIBLE);
                gViewHolder.bottom_view.setVisibility(View.GONE);
                gViewHolder.group_img.setBackgroundResource(R.drawable.icon_group_image);
            }

            if (fansGroupModel.getGroup().equals("线下录入粉丝")) {
                gViewHolder.tv_sendmessage.setVisibility(View.GONE);
                gViewHolder.tv_addNew.setVisibility(View.VISIBLE);
            } else {
                gViewHolder.tv_addNew.setVisibility(View.GONE);
                if (isExpanded) {
                    gViewHolder.tv_sendmessage.setVisibility(View.VISIBLE);
                } else {
                    gViewHolder.tv_sendmessage.setVisibility(View.GONE);
                }
            }

            if (fansGroupModel.getGroup().equals("线下录入粉丝") || fansGroupModel.getGroup().equals("新粉丝") ||
                    fansGroupModel.getGroup().equals("我关注的粉丝")) {
                gViewHolder.tv_count.setVisibility(View.GONE);
            } else {
                gViewHolder.tv_count.setText(fansGroupModel.getNewBehaviorNum() > 99 ? "99+" : fansGroupModel
                        .getNewBehaviorNum() + "");
                gViewHolder.tv_count.setVisibility(fansGroupModel.getNewBehaviorNum() == 0 ? View.GONE : View.VISIBLE);

                if (fansGroupModel.getNewBehaviorNum() < 10) {
                    gViewHolder.tv_count.setBackgroundResource(R.drawable.bg_behave);
                } else {
                    gViewHolder.tv_count.setBackgroundResource(R.drawable.bg_behavoir);
                }
            }

            gViewHolder.bottom_line.setVisibility(View.GONE);

            gViewHolder.tv_sendmessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //只在“我关注的粉丝组” 中做如下判断
                    //当我关注的粉丝组中全部是线下粉丝时  就给出提示  否则跳转
                    if (fansGroupModel.getGroup().equals("我关注的粉丝")) {
                        List<FansInfo> focusFansInfos = copyFansAllDatas.get(fansGroupModel);
                        int focusGroupCount = copyFansAllDatas.get(fansGroupModel).size();
                        //我关注的粉丝中线下粉丝的数量
                        outLineFansCount = 0;
                        for (int i = 0; i < focusGroupCount; i++) {
                            if (TextUtils.isEmpty(focusFansInfos.get(i).getImei())) {
                                outLineFansCount++;
                            }
                        }
                        if (outLineFansCount == getChildrenCount(groupPosition)) {
                            ViewUtils.showToast("不能推送给线下录入粉丝");
                            return;
                        }
                    }
                    List<FansInfo> fansInfos = copyFansAllDatas.get(fansGroupModel);
                    if (fansInfos.size() > 800) {
                        ChooseGroupPeopleAct.start((Activity) context, fansGroupModel.getGroup(), 2);
                    } else {
                        ChooseGroupPeopleAct.startAct((Activity) context, fansInfos, 2);
                    }

                    UmengUtil.onEvent(context, new UmengUtil().FANS_FANS_PUSH_CLICK, null);
                }
            });
            //线下录入粉丝  右侧新增点击事件
            gViewHolder.tv_addNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FansAddAct.start(getActivity(), fansAddCallBack);
                }
            });
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View
                convertView, ViewGroup parent) {
            final ChildViewHolder cViewHolder;

            if (groupPosition == 1) {
                UmengUtil.onEvent(context, new UmengUtil().FANS_NEWFANS_CLICK, null);
            }else  if(groupPosition == 2){
                UmengUtil.onEvent(context, new UmengUtil().FANS_STARFANS_CLICK, null);
            }

            if (convertView == null) {
                cViewHolder = new ChildViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_fans_child, parent, false);
                cViewHolder.img_notice = (ImageView) convertView.findViewById(R.id.img_notice);
                cViewHolder.img_user_head = (CircleImageView) convertView.findViewById(R.id.img_user_head);
                cViewHolder.tv_imei = (TextView) convertView.findViewById(R.id.tv_imei);
                cViewHolder.fans_name = (TextView) convertView.findViewById(R.id.fans_name);
                cViewHolder.layout_willingbuy = (LinearLayout) convertView.findViewById(R.id.layout_willingbuy);
                cViewHolder.star_first = (ImageView) convertView.findViewById(R.id.star_first);
                cViewHolder.star_second = (ImageView) convertView.findViewById(R.id.star_second);
                cViewHolder.star_third = (ImageView) convertView.findViewById(R.id.star_third);
                cViewHolder.txt_phone_brand = (TextView) convertView.findViewById(R.id.txt_phone_brand);
                cViewHolder.img_guanzhu = (ImageView) convertView.findViewById(R.id.img_guanzhu);
                cViewHolder.bottom_line = convertView.findViewById(R.id.bottom_line);
                cViewHolder.top_line = convertView.findViewById(R.id.top_line);
                cViewHolder.container = (RelativeLayout) convertView.findViewById(R.id.container);
                cViewHolder.layout_focus = (LinearLayout) convertView.findViewById(R.id.layout_focus);
                convertView.setTag(cViewHolder);
            } else {
                cViewHolder = (ChildViewHolder) convertView.getTag();
            }
            //TODO 赋值
            //还没真实数据 先强转  真实数据时删除强转
            final FansInfo childInfo = (FansInfo) getChild(groupPosition, childPosition);
            if (childPosition == getChildrenCount(groupPosition) - 1) {
                cViewHolder.bottom_line.setVisibility(View.GONE);
            } else {
                cViewHolder.bottom_line.setVisibility(View.VISIBLE);
            }
            if (expandGroupPosition == -1) {
                cViewHolder.top_line.setVisibility(View.GONE);
            } else {
                if (childPosition == 0) {
                    cViewHolder.top_line.setVisibility(View.VISIBLE);
                } else {
                    cViewHolder.top_line.setVisibility(View.GONE);
                }
            }
            //TODO 真实数据时
            //是否显示红点
            if (((FansGroupModel) getGroup(groupPosition)).getGroup().equals("新粉丝") || ((FansGroupModel) getGroup
                    (groupPosition)).getGroup().equals("我关注的粉丝")) {
                cViewHolder.img_notice.setVisibility(View.INVISIBLE);
            } else {
                cViewHolder.img_notice.setVisibility(childInfo.getHasNew() == 0 ? View.INVISIBLE : View.VISIBLE);
            }
            //粉丝只有默认头像
            //如果粉丝有姓名  那么就显示姓名与购买意向否则显示imei
            if (!TextUtils.isEmpty(childInfo.getName())) {
                cViewHolder.tv_imei.setVisibility(View.GONE);
                cViewHolder.fans_name.setVisibility(View.VISIBLE);
                cViewHolder.fans_name.setText(childInfo.getName());
                cViewHolder.layout_willingbuy.setVisibility(View.VISIBLE);
                if (childInfo.getBuyIntention() == 1) {
                    cViewHolder.star_first.setBackgroundResource(R.drawable.icon_lightstar);
                    cViewHolder.star_second.setBackgroundResource(R.drawable.icon_darkstar);
                    cViewHolder.star_third.setBackgroundResource(R.drawable.icon_darkstar);
                } else if (childInfo.getBuyIntention() == 2) {
                    cViewHolder.star_first.setBackgroundResource(R.drawable.icon_lightstar);
                    cViewHolder.star_second.setBackgroundResource(R.drawable.icon_lightstar);
                    cViewHolder.star_third.setBackgroundResource(R.drawable.icon_darkstar);
                } else if (childInfo.getBuyIntention() == 3) {
                    cViewHolder.star_first.setBackgroundResource(R.drawable.icon_lightstar);
                    cViewHolder.star_second.setBackgroundResource(R.drawable.icon_lightstar);
                    cViewHolder.star_third.setBackgroundResource(R.drawable.icon_lightstar);
                } else {
                    cViewHolder.star_first.setVisibility(View.GONE);
                    cViewHolder.star_second.setVisibility(View.GONE);
                    cViewHolder.star_third.setVisibility(View.GONE);
                }
            } else {
                cViewHolder.tv_imei.setVisibility(View.VISIBLE);
                cViewHolder.fans_name.setVisibility(View.GONE);
                cViewHolder.layout_willingbuy.setVisibility(View.GONE);
                cViewHolder.tv_imei.setText(childInfo.getImei());
            }
            //粉丝手机型号
            if (!TextUtils.isEmpty(childInfo.getPhoneModel())) {
                cViewHolder.txt_phone_brand.setText(childInfo.getPhoneModel());
            } else {
                cViewHolder.txt_phone_brand.setText("");
            }
            //是否关注
            cViewHolder.img_guanzhu.setBackgroundResource(childInfo.getIsFocus() == 0 ? R.drawable.icon_cancle_look :
                    R.drawable.icon_has_look);

            cViewHolder.layout_focus.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    final int isFocus = childInfo.getIsFocus();
                    FansFocusRequest fansFocusRequest = new FansFocusRequest(isFocus == 0 ? false : true, childInfo
                            .getFansId());
                    fansFocusRequest.start();
                    fansFocusRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {

                        private FansGroupModel fansGroupFoucus;

                        @Override
                        public void onOK() {
                            if (isFocus == 0) {
                                ViewUtils.showToast("关注成功");
                                UmengUtil.onEvent(context, new UmengUtil().FANS_FANS_FOLLOW_CLICK, null);
                            } else {
                                ViewUtils.showToast("取消关注成功");
                                UmengUtil.onEvent(context, new UmengUtil().FANS_FANS_UNFOLLOW_CLICK, null);
                            }
                            FansGroupModel group = (FansGroupModel) getGroup(groupPosition);
                            if (((FansGroupModel) getGroup(groupPosition)).getGroup().contains("我关注的粉丝")) {
                                //如果点击我关注的粉丝中的粉丝  那么就一定是关注状态 那么点击之后就需要数量减一 移除该粉丝
                                ((FansGroupModel) getGroup(groupPosition)).setNum(group.getNum() - 1 < 0 ? 0 : group.getNum() - 1);
                                List<FansInfo> fansFocusInfos = copyFansAllDatas.get(group);
                                fansFocusInfos.remove(childInfo);
                                putDatas(group, fansFocusInfos);
                            } else {
                                //点击其他分组  就存在已关注和未关注的状态
                                for (int i = 0; i < fansGroups.size(); i++) {
                                    if (fansGroups.get(i).getGroup().contains("我关注的粉丝")) {
                                        fansGroupFoucus = fansGroups.get(i);
                                    }
                                }
                                if (isFocus == 0) {
                                    fansGroupFoucus.setNum(fansGroupFoucus.getNum() + 1);
                                    //拿到我关注的粉丝组所有的粉丝  然后将关注的添加进去其添加
                                    List<FansInfo> fansFocusInfos = copyFansAllDatas.get(fansGroupFoucus);
                                    //说明 我关注的粉丝 组中的数据还没有加载过
                                    if (fansFocusInfos != null) {
                                        fansFocusInfos.add(childInfo);
                                    }
                                    List<FansInfo> fansInfos = copyFansAllDatas.get(getGroup(groupPosition));
                                    fansInfos.get(childPosition).setIsFocus(isFocus == 0 ? 1 : 0);
                                    putDatas((FansGroupModel) getGroup(groupPosition), fansInfos);
                                } else {
                                    fansGroupFoucus.setNum(fansGroupFoucus.getNum() - 1 < 0 ? 0 : fansGroupFoucus.getNum() - 1);
                                    List<FansInfo> fansFocusInfos = copyFansAllDatas.get(fansGroupFoucus);
                                    if (fansFocusInfos != null) {
                                        fansFocusInfos.remove(childInfo);
                                    }
                                    List<FansInfo> fansInfos = copyFansAllDatas.get(getGroup(groupPosition));
                                    fansInfos.get(childPosition).setIsFocus(isFocus == 0 ? 1 : 0);
                                    putDatas((FansGroupModel) getGroup(groupPosition), fansInfos);
                                }

                            }
                            fansListAdapter.addContent(fansAllDatas);
                            fansListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(int code) {

                        }
                    });
                }
            });

            //粉丝条目点击事件
            cViewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 跳转至粉丝详情页面
                    refreshFans = ((FansInfo) getChild(groupPosition, childPosition));
                    refreshGroup = (FansGroupModel) getGroup(groupPosition);
                    FansDetailNewAct.start(getActivity(), Long.parseLong(String.valueOf(childInfo.getFansId())), 1,
                            refrenshCallBack, clearBehavoirCallBack);
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public View inflateConvertView(int layoutId) {
            return LayoutInflater.from(context).inflate(layoutId, null);
        }
    }

    class GroupViewHolder {
        TextView tv_count;
        TextView tv_group_name;
        TextView tv_child_cout;
        TextView tv_sendmessage, tv_addNew;
        View top_line;
        View bottom_line, bottom_view;
        ImageView group_img;
    }

    class ChildViewHolder {
        ImageView img_notice;
        CircleImageView img_user_head;
        TextView tv_imei;
        TextView fans_name;
        LinearLayout layout_willingbuy;
        ImageView star_first, star_second, star_third;
        TextView txt_phone_brand;
        ImageView img_guanzhu;
        View bottom_line, top_line;
        RelativeLayout container;
        LinearLayout layout_focus;
    }

    ParameCallBack clearBehavoirCallBack = new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            if (refreshFans.getHasNew() == 1) {
                //将组的轨迹数量减去一
                Iterator<FansGroupModel> iterator = copyFansAllDatas.keySet().iterator();
                while (iterator.hasNext()) {
                    FansGroupModel next = iterator.next();
                    if (next.getGroup().equals(refreshGroup.getGroup())) {
                        next.setNewBehaviorNum(next.getNewBehaviorNum() - 1 < 0 ? 0 : next.getNewBehaviorNum() - 1);
                        Config.fansNewBehavoir = Config.fansNewBehavoir - 1;
                        if (Config.fansNewBehavoir <= 0) {
                            MainActivity.getIncetance().showKeDDRedPoint();
                        }
                    }
                }
                //将该会员的新轨迹提示去掉
                List<FansInfo> fansInfos = copyFansAllDatas.get(refreshGroup);
                for (int i = 0; i < fansInfos.size(); i++) {
                    if (fansInfos.get(i).getFansId() == refreshFans.getFansId()) {
                        fansInfos.get(i).setHasNew(0);
                    }
                }
                fansListAdapter.notifyDataSetChanged();
            }
        }
    };

    //修改粉丝备注  刷新该组回调
    ParameCallBack refrenshCallBack = new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            FansInfo childInfo = (FansInfo) object;
            refreshSingleFans(childInfo, refreshGroup);
            //找出其他组是否有改粉丝  如果有那么就都更新（粉丝有相同的组仅 新粉丝 我关注的粉丝 未分组粉丝）
            updateOtherGroup(childInfo);
            fansListAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 更新其他组（并且是已加载过粉丝的）可能含有该粉丝的组
     *
     * @param childInfo
     */
    private void updateOtherGroup(FansInfo childInfo) {
        //找出已经加载过的组别  也就是已经使用缓存的组别
        if (!fansGroups.isEmpty()) {
            //找出已经点击过的分组
            ArrayList<FansGroupModel> hasClickGroup = new ArrayList<FansGroupModel>();
            for (int i = 0; i < fansGroups.size(); i++) {
                if (fansGroups.get(i).isHasClick()) {
                    hasClickGroup.add(fansGroups.get(i));
                }
            }
            for (int i = 0; i < hasClickGroup.size(); i++) {
                if ("新粉丝".equals(hasClickGroup.get(i).getGroup())) {
                    refreshSingleFans(childInfo, hasClickGroup.get(i));
                }
                if ("我关注的粉丝".equals(hasClickGroup.get(i).getGroup())) {
                    refreshSingleFans(childInfo, hasClickGroup.get(i));
                }
                if ("未分组".equals(hasClickGroup.get(i).getGroup())) {
                    refreshSingleFans(childInfo, hasClickGroup.get(i));
                }
            }
        }
    }

    /**
     * 更新该组某个粉丝的信息
     *
     * @param childInfo
     */
    private void refreshSingleFans(FansInfo childInfo, FansGroupModel fansGroup) {
        if (childInfo != null) {
            List<FansInfo> fansInfoModels = copyFansAllDatas.get(fansGroup);

            for (int i = 0; i < fansInfoModels.size(); i++) {
                if (fansInfoModels.get(i).getFansId() == refreshFans.getFansId()) {
                    if (!TextUtils.isEmpty(childInfo.getRemarkName())) {
                        fansInfoModels.get(i).setName(childInfo.getRemarkName());
                    }
                    if (!TextUtils.isEmpty(childInfo.getPhone())) {
                        fansInfoModels.get(i).setPhone(childInfo.getPhone());
                    }
                    fansInfoModels.get(i).setBuyIntention(childInfo.getBuyIntention());
                }
            }
        }
    }

    //线下录入粉丝成功后刷新回调
    CallBack fansAddCallBack = new CallBack() {
        @Override
        public void onCall() {
            // loadGroupData();
            FansGroupModel group = (FansGroupModel) fansListAdapter.getGroup(0);
            group.setNum(group.getNum() + 1);
            fansListAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 判断是否会员新轨迹数大于0  显示tab红点
     *
     * @param fansGroupList
     */
    private void showBottomRedPoint(List<FansGroupModel> fansGroupList) {
        int newBehavoirCount = 0;
        for (int i = 0; i < fansGroupList.size(); i++) {
            newBehavoirCount = fansGroupList.get(i).getNewBehaviorNum() + newBehavoirCount;
        }
        Config.fansNewBehavoir = newBehavoirCount;
        MainActivity.getIncetance().showKeDDRedPoint();
    }
}
