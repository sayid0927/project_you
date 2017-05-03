package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.dialog.CreateNewGroupDialog;
import com.zxly.o2o.fragment.MenberListFragment;
import com.zxly.o2o.model.MenberGroupModel;
import com.zxly.o2o.model.MenberInfoModel;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshExpandableListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.DeleteGroupRequest;
import com.zxly.o2o.request.GetMenberGroupReuqest;
import com.zxly.o2o.request.GetMenberInfoRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.SlideDelete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class PurchaseGroupAct extends BasicAct implements PullToRefreshBase.OnRefreshListener {
    private TextView mTv_back;
    /*
    private TextView mTv_back;
    private PullToRefreshExpandableListView mListView;
    private Map<MenberGroupModel, List<MenberInfoModel>> mDatas;
    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_group);
        mDatas = MenberListFragment.purchaseHabitAllDatas;

        Log.d("zzll", "mDatas size: "+mDatas.size());
        initView();
        initListener();



    }

    private void initListener() {
        mTv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initView() {

        mTv_back = (TextView) findViewById(R.id.btn_back);

        mListView = (PullToRefreshExpandableListView) findViewById(R.id.expand_listview);
        mLoadingView = (LoadingView) findViewById(R.id.view_loading);

        MemberListAdapter mAdapter = new MemberListAdapter(this,mListView);
        mAdapter.addContent(mDatas);
        mListView.getRefreshableView().setAdapter(mAdapter);
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_group);
        initView();

    }

    private static final int NORMAL_TYPE = 0;
    private static final int GROUP_TYPE = 1;
    private PullToRefreshExpandableListView expandableListView;
    private LoadingView loadingView;
    private MenbersListAdapter menbersListAdapter;
    private List<SlideDelete> slideDeleteArrayList = new ArrayList<SlideDelete>();
    private Dialog dialog;
    private RelativeLayout layout_nodate;
    //用来存放，没有二级列表的组名 作用：当点击没有二级数据的组时  不显示推送字样
    private List<MenberGroupModel> noChildDataGroups = new ArrayList<MenberGroupModel>();
    //一列列表数据
    private List<MenberGroupModel> menberGroupModels = new ArrayList<MenberGroupModel>();
    //二级列表数据
    private List<MenberInfoModel> menberInfos = new ArrayList<MenberInfoModel>();
    //一级数据和二级数据集合
    private Map<MenberGroupModel, List<MenberInfoModel>> menberAllDatas = new LinkedHashMap<MenberGroupModel, List<MenberInfoModel>>();
    private Map<MenberGroupModel, List<MenberInfoModel>> copymenberAllDatas = new LinkedHashMap<MenberGroupModel, List<MenberInfoModel>>();
    public static Map<MenberGroupModel, List<MenberInfoModel>> purchaseHabitAllDatas = new LinkedHashMap<MenberGroupModel, List<MenberInfoModel>>();
    private int expandGroupPosition = -1;
    private boolean hasInit;
    private LinearLayout head;
    private MenberGroupModel refreshGroup = new MenberGroupModel();
    private MenberListFragment instance;
    private CallBack changeFansCallBack;
    private ParameCallBack _memberCountCallBack;
    private List<MenberGroupModel> menberGroups;
    //所有点击的组集合
    private List<MenberGroupModel> hasClickGroup = new ArrayList<MenberGroupModel>();
    private List<MenberGroupModel> notShowGroup = new ArrayList<MenberGroupModel>();
    private MenberInfoModel refreshMenber;
    private Map<Integer, MenberInfoModel> locationMap;
    private int key;
    private MenberGroupModel preLoadGroup;

    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    protected void initView() {
        //instance = this;
        //dialog = new Dialog(getActivity(), R.style.dialog);
        // head = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.head_create_group, null, false);
        //让按照机型分组 和 按购买行为分组 条目不再滑动

        // initGroup();

        mTv_back = (TextView) findViewById(R.id.btn_back);

        expandableListView = (PullToRefreshExpandableListView) findViewById(R.id.expand_listview);
        expandableListView.getRefreshableView().setSelector(android.R.color.transparent);
        expandableListView.setPullToRefreshOverScrollEnabled(true);          //可刷新
        expandableListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        expandableListView.getRefreshableView().setDivider(null);//设置模式，此模式是可以上拉，
        loadingView = (LoadingView) findViewById(R.id.view_loading);
        expandableListView.getRefreshableView().setGroupIndicator(null);
        ViewUtils.setRefreshListFromStartText(expandableListView);
        expandableListView.setOnRefreshListener(this);
       /* head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateNewGroupDialog();
                showKeyboard();
            }
        });*/
        //无会员时显示的布局
//        layout_nodate = (RelativeLayout) findViewById(R.id.layout_nodata);
        if (menbersListAdapter == null) {
            menbersListAdapter = new MenbersListAdapter(this);

        }
       //expandableListView.getRefreshableView().addHeaderView(head);
         expandableListView.getRefreshableView().setAdapter(menbersListAdapter);
        expandableListView.getRefreshableView().setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                for (int y = 0; y < expandableListView.getRefreshableView().getExpandableListAdapter().getGroupCount(); y++) {
                    if (y != groupPosition) {
                        expandableListView.getRefreshableView().collapseGroup(y);
                    }
                }
            }
        });


        mTv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

   /* */

    /**
     * @des: 对按照机型分组 和按照 购买行为分组的 操作
     *//*
    private void initGroup() {
        RelativeLayout rl_purchase_group = (RelativeLayout) head.findViewById(R.id.layout_purchase_group);
        RelativeLayout rl_model_group = (RelativeLayout) head.findViewById(R.id.layout_model_group);

        //购买行为 点击事件 调转到PurchaseGroupAct
        rl_purchase_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PurchaseGroupAct.class);

                startActivity(intent);


            }
        });

        //机型点击事件 调到新的 ModelGroupAct
        rl_model_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }*/
    public void showNodataView(int menberCount, int fansCount) {
        if (menberCount == 0) {
            layout_nodate.setVisibility(View.VISIBLE);
            //如果粉丝总数与线下录入粉丝数相等 就说明没有可推送的粉丝
            if (fansCount != 0 && fansCount == Config.outLineFansGroupCount) {
                findViewById(R.id.btn_sendmsg_tofans).setVisibility(View.GONE);
            } else if (fansCount == 0) {
                findViewById(R.id.btn_sendmsg_tofans).setVisibility(View.GONE);
            } else {
                findViewById(R.id.btn_sendmsg_tofans).setVisibility(View.VISIBLE);
            }
        } else {
            layout_nodate.setVisibility(View.GONE);
        }
    }

    private void showCreateNewGroupDialog() {
        new CreateNewGroupDialog().show(callBack);
    }

    ParameCallBack callBack = new ParameCallBack() {
        @Override
        public void onCall(Object result) {
            //当在新建组页面创建新的组别成功后回调此方法  手动加入组别及组人数  刷新
            MenberGroupModel menberGroupModel = (MenberGroupModel) result;
            putDatas(menberGroupModel, menberInfos);
            menbersListAdapter.addContent(menberAllDatas);
            menbersListAdapter.notifyDataSetChanged();
            Config.groupList.add(menberGroupModel);
        }
    };


 /*   @Override
    protected int layoutId() {
        return R.layout.win_menbers_layout;
    }*/

    public void setHasInit(boolean b) {
        hasInit = b;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!hasInit && Account.user != null) {

            loadingView.startLoading();
            //加载数据
            loadGroupData(false);
        }
        hasInit = true;

    }

  /*public void onResume() {
        super.onResume();
        if (!hasInit && Account.user != null && 0 == ((MainActivity) getActivity()).fragmentController.getCurrentTab()) {
            //加载数据
            loadGroupData(false);
        }
        hasInit = true;
    }*/

    /**
     * 获取一级列表数据
     */
    public void loadGroupData(boolean showDialog) {
        final GetMenberGroupReuqest getMenberGroupRequest = new GetMenberGroupReuqest(showDialog);
        getMenberGroupRequest.start();
        getMenberGroupRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                menberGroups = getMenberGroupRequest.getMenberGroups();
                notShowGroup.clear();
                menberAllDatas.clear();
                purchaseHabitAllDatas.clear();
                if (_memberCountCallBack != null) {
                    //设置会员总数的回调
                    _memberCountCallBack.onCall(getMenberGroupRequest.getMemberCount());
                }
                //人数变化检查是否显示默认层
                Config.memberCount = getMenberGroupRequest.getMemberCount();
//                showNodataView(getMenberGroupRequest.getMemberCount(),Config.fansCount);
                //筛选出 不是自定义分组并且组内人数为0的
                expandableListView.onRefreshComplete();
              // head.setVisibility(View.VISIBLE);
                if (menberAllDatas != null) {
                    menberAllDatas.clear();
                }
                if (copymenberAllDatas != null) {
                    copymenberAllDatas.clear();
                }
                loadingView.onLoadingComplete();
                //筛选出 不是自定义分组并且组内人数为0的
                Iterator<MenberGroupModel> iterator = menberGroups.iterator();
                while (iterator.hasNext()) {
                    MenberGroupModel next = iterator.next();
                    if (next.getIsCustomerGroup() == 0 && next.getMemberCount() == 0) {
                        //新会员  我的所有会员除外
                        if (next.getId() == 1 || next.getId() == 2) {
                        } else {
                            notShowGroup.add(next);
                            iterator.remove();
                        }
                    }
                }
                for (int i = 0; i < menberGroups.size(); i++) {
                    putDatas(menberGroups.get(i), menberInfos);
                    //预加载最后一组
                    if (i == menberGroups.size() - 1) {
                        preLoadGroup = menberGroups.get(i);
                    }
                }
                Config.groupList.clear();
                Config.groupList.addAll(notShowGroup);
                Config.groupList.addAll(menberGroups);
                expandableListView.getRefreshableView().setAdapter(menbersListAdapter);
                menbersListAdapter.addContent(menberAllDatas);
                menbersListAdapter.notifyDataSetChanged();
                showBottomRedPoint(menberGroups);
                //如果该组会员数大于0  那么就预加载最后一组
                if (preLoadGroup.getMemberCount() > 0 && preLoadGroup.getMemberCount() < 20) {
                    loadFansData(preLoadGroup, false);
                }
            }

            @Override
            public void onFail(int code) {
                expandableListView.onRefreshComplete();
                head.setVisibility(View.GONE);
//                loadingView.onLoadingFail("加载失败",true);
                loadingView.onLoadingFail();
            }
        });
        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                loadingView.startLoading();
                getMenberGroupRequest.start(this);
            }
        });
    }

    /**
     * 判断是否会员新轨迹数大于0  显示tab红点
     *
     * @param menberGroups
     */
    private void showBottomRedPoint(List<MenberGroupModel> menberGroups) {
        for (int i = 0; i < menberGroups.size(); i++) {
            if (menberGroups.get(i).getId() == 2) {
                Config.menberNewBehavoir = menberGroups.get(i).getNewMsgMemberCount();
            }
        }
        MainActivity.getIncetance().showKeDDRedPoint();

    }

    private void loadFansData(final MenberGroupModel menberGroupModel, boolean isShowLoading) {
        final GetMenberInfoRequest getMenberInfoRequest = new GetMenberInfoRequest(menberGroupModel.getId());
        getMenberInfoRequest.setIsshowLoading(isShowLoading);
        getMenberInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                hasClickGroup.clear();
                List<MenberInfoModel> fansInfoList = getMenberInfoRequest.getMenberInfoList();
                int size = fansInfoList.size();
                boolean empty = getMenberInfoRequest.getMenberInfoList().isEmpty();
                //目的：由于后台存在调组功能，所以当调岗然后又处于此页面时 检查两个人数是否相同
                //不同就更新组的人数为最新 以实际获取的会员人数为准
                if (menberGroupModel.getMemberCount() != size) {
                    ViewUtils.showToast("该会员列表已刷新");
                    closeAllGroup(fansInfoList.size());
                    menberGroupModel.setMemberCount(size);
                    //当该组人数全在后台调走后  那么就重新刷新组信息（也就是删除该组）
                    if (size == 0) {
                        loadGroupData(true);
                        return;
                    }
                }
                if (empty) {
                    //如果是空  那么在适配器中根据groupName  来判断点击组名时  推送 字样不显示
                    noChildDataGroups.add(menberGroupModel);
                } else {
                    menberGroupModel.setHasClick(true);
                    hasClickGroup.add(menberGroupModel);
                    if (menberGroupModel.getId() == 2) {
                        sortHasBehavior(fansInfoList);
                    }
                    putDatas(menberGroupModel, fansInfoList);
                    menbersListAdapter.addContent(menberAllDatas);
                    copymenberAllDatas.put(menberGroupModel, fansInfoList);
                    menbersListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        getMenberInfoRequest.start();
    }

    /**
     * 将返回的粉丝集合中有新轨迹的粉丝排至最前面
     *
     * @param fansInfoList
     */
    private void sortHasBehavior(List<MenberInfoModel> fansInfoList) {
        //储存所有有新轨迹的会员集合
        List<MenberInfoModel> hasNewList = new ArrayList<MenberInfoModel>();
        //用来存储有新轨迹的会员的位置
        locationMap = new HashMap<Integer, MenberInfoModel>();
        for (int i = 0; i < fansInfoList.size(); i++) {
            if (fansInfoList.get(i).getIsNewBehavior() == 1) {
//                locationMap.put(i,fansInfoList.get(i));
                fansInfoList.get(i).setLoacation(i);
            }
        }
        Iterator<MenberInfoModel> iterator = fansInfoList.iterator();
        while (iterator.hasNext()) {
            MenberInfoModel next = iterator.next();
            //显示红点的会员
            if (next.getIsNewBehavior() == 1) {
                hasNewList.add(next);
                iterator.remove();
            }
        }
        Collections.sort(hasNewList, new Comparator<MenberInfoModel>() {
            @Override
            public int compare(MenberInfoModel lhs, MenberInfoModel rhs) {
                long lastBehaviorTime = lhs.getLastBehaviorTime();
                long lastBehaviorTime1 = rhs.getLastBehaviorTime();
                if (lastBehaviorTime > lastBehaviorTime1) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        fansInfoList.addAll(0, hasNewList);
    }

    MenberGroupModel model = new MenberGroupModel();

    List<MenberInfoModel> infoModels = new ArrayList<MenberInfoModel>();

    /**
     * 将数据保存在map集合中
     *
     * @param menberGroupModel 组名
     * @param menberDatas      该组下的数据
     * @return 返回map
     * @des 除了新会员和 我的所有会员的数据 保留外,替他的都不显示
     */
    private void putDatas(MenberGroupModel menberGroupModel, List<MenberInfoModel> menberDatas) {

        if (menberGroupModel.getIsCustomerGroup() == 0) {
            String name = menberGroupModel.getName();
            int id = menberGroupModel.getId();
            if (name.contains("新会员")) {
                //menberAllDatas.put(menberGroupModel, menberDatas);

                //Log.d("zzll", "menberAllDatas " + menberAllDatas.size());
            } else if (name.contains("我的所有会员")) {
              //  menberAllDatas.put(menberGroupModel, menberDatas);
                //插入一条空的数据

               // menberAllDatas.put(model, infoModels);
            } else {

                if (id >= 3 && id <= 7) {
                    //按照购买行为分组的数据集合
                    menberAllDatas.put(menberGroupModel, menberDatas);
                    return;
                }

            }
        } else {
            //menberAllDatas.put(menberGroupModel, menberDatas);
        }

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            hasClickGroup.clear();
            int groupCount = expandableListView.getRefreshableView().getExpandableListAdapter().getGroupCount();
            //清除旧数据
            copymenberAllDatas.clear();
            menberAllDatas.clear();
            expandGroupPosition = -1;
            closeAllGroup(groupCount);
            //加载数据
            loadGroupData(false);

        } else if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
         /*   // 加载上拉数据
            closeAllGroup(groupCount);
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

    private void closeAllGroup(int groupCount) {
        for (int i = 0; i < groupCount; i++) {
            expandableListView.getRefreshableView().collapseGroup(i);
        }
    }

    public void onDataRefresh() {
        int groupCount = expandableListView.getRefreshableView().getExpandableListAdapter().getGroupCount();
        //清除旧数据
        copymenberAllDatas.clear();
        menberAllDatas.clear();
        expandGroupPosition = -1;
        closeAllGroup(groupCount);
        //加载数据
        loadGroupData(true);
    }

    public void setMenberCountCallBack(ParameCallBack memberCountCallBack) {
        this._memberCountCallBack = memberCountCallBack;
    }


    public class MenbersListAdapter extends BaseExpandableListAdapter {
        protected Map<MenberGroupModel, List<MenberInfoModel>> content = new LinkedHashMap<MenberGroupModel, List<MenberInfoModel>>();
        protected Context context;

        public MenbersListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getGroupCount() {
            return content.keySet().size();
        }

/*        @Override
        public int getGroupTypeCount() {
            return 2;
        }

        @Override
        public int getGroupType(int groupPosition) {
            if(((MenberGroupModel)getGroup(groupPosition)).getIsCustomerGroup()==1){
                return TYPE_2;
            }else {
                return TYPE_1;
            }
        }*/

        public void clear() {
            content.clear();
        }

        public void addContent(Map<MenberGroupModel, List<MenberInfoModel>> _content) {
            if (content.isEmpty()) {
                content.putAll(_content);
            } else {
                content = _content;
            }


            Log.d("zzll", "content size: " + content.size());

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

        /**
         * @return
         * @des: 支持多种类型的条目
         */
        @Override
        public int getGroupTypeCount() {
            return super.getGroupTypeCount() + 1;

        }


        @Override
        public int getGroupType(int groupPosition) {

            if (groupPosition == 2) {
                //条目为2的时候 添加 自定义分组的条目
                return GROUP_TYPE;

            } else {
                return NORMAL_TYPE;
            }


        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {

            // int group_position = groupPosition;
/*
            if (groupPosition == 2) {
                //自定义组
                View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);

                return view;

            }*/
            final MenberGroupModel menberGroupModel = (MenberGroupModel) getGroup(groupPosition);
            ;

           /* if (groupPosition > 2) {
                menberGroupModel = (MenberGroupModel) getGroup(groupPosition - 1);
            } else {

                // content.keySet().toArray()[groupPosition];

                menberGroupModel = (MenberGroupModel) getGroup(groupPosition);
            }*/
            final GroupViewHolder gViewHolder;

            if (convertView == null) {
                gViewHolder = new GroupViewHolder();
                //不能用此方法加载视图  会出现显示不正确的情况
//                convertView=inflateConvertView(R.layout.item_fans_group);
                convertView = LayoutInflater.from(context).inflate(R.layout.item_menbers_group, parent, false);
                //子列表活动轨迹数
                gViewHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
                //组名
                gViewHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
                //组员数目
                gViewHolder.tv_child_cout = (TextView) convertView.findViewById(R.id.tv_child_cout);
                //推送按钮
                gViewHolder.tv_sendmessage = (TextView) convertView.findViewById(R.id.tv_sendmessage);
                gViewHolder.layout_container = (RelativeLayout) convertView.findViewById(R.id.layout_container);
                gViewHolder.top_line = convertView.findViewById(R.id.top_line);
                gViewHolder.layout_sidedelete = (SlideDelete) convertView.findViewById(R.id.layout_sidedelete);
                gViewHolder.layout_sidedelete.setDragEnable(false);
                gViewHolder.layout_delete = (LinearLayout) convertView.findViewById(R.id.layout_delete);
                gViewHolder.group_img = (ImageView) convertView.findViewById(R.id.group_img);
                convertView.setTag(gViewHolder);
            } else {
                gViewHolder = (GroupViewHolder) convertView.getTag();
            }
            //// TODO: 2016/8/27  赋值

            gViewHolder.tv_group_name.setText(menberGroupModel.getName());
            gViewHolder.tv_child_cout.setText("(" + menberGroupModel.getMemberCount() + ")");
            if (groupPosition == 0) {
                gViewHolder.top_line.setVisibility(View.GONE);
            } else {
                gViewHolder.top_line.setVisibility(View.VISIBLE);
            }

            if (menberGroupModel.getId() == 2) {
                int newMsgMemberCount = menberGroupModel.getNewMsgMemberCount();
                if (menberGroupModel.getNewMsgMemberCount() == 0) {
                    ViewUtils.setGone(gViewHolder.tv_count);
                } else {
                    ViewUtils.setVisible(gViewHolder.tv_count);
                    if (newMsgMemberCount > 99) {
                        gViewHolder.tv_count.setText("99+");
                    } else {
                        gViewHolder.tv_count.setText(newMsgMemberCount + "");
                    }
                    if (newMsgMemberCount < 10) {
                        gViewHolder.tv_count.setBackgroundResource(R.drawable.bg_behave);
                    } else {
                        gViewHolder.tv_count.setBackgroundResource(R.drawable.bg_behavoir);
                    }
                }
            } else {
                gViewHolder.tv_count.setVisibility(View.GONE);
            }

            if (isExpanded) {
                gViewHolder.tv_sendmessage.setVisibility(View.VISIBLE);
            } else {
                gViewHolder.tv_sendmessage.setVisibility(View.GONE);
            }

            if (menberGroupModel.getIsCustomerGroup() == 0) {
                gViewHolder.layout_delete.setVisibility(View.GONE);
                gViewHolder.layout_sidedelete.setDragEnable(false);
//                gViewHolder.layout_sidedelete.isShowDelete(false);
                gViewHolder.group_img.setBackgroundResource(R.drawable.icon_group_image);
            } else {
                gViewHolder.layout_delete.setVisibility(View.VISIBLE);
                gViewHolder.layout_sidedelete.setDragEnable(true);
                gViewHolder.group_img.setBackgroundResource(R.drawable.icon_selfdefine);
            }

            if (menberGroupModel.getIsCustomerGroup() != 0) {
                gViewHolder.layout_sidedelete.setOnSlideDeleteListener(new SlideDelete.OnSlideDeleteListener() {//接口回调，为了避免同时出现多个item的删除视图的问题
                    @Override
                    public void onOpen(SlideDelete slideDelete) {

                        closeOtherItem();
                        slideDeleteArrayList.add(slideDelete);
                    }

                    @Override
                    public void onClose(SlideDelete slideDelete) {
                        slideDeleteArrayList.remove(slideDelete);
                    }
                });

            } else {
                gViewHolder.layout_sidedelete.setDragEnable(false);
                gViewHolder.layout_sidedelete.setOnSlideDeleteListener(null);
            }

            gViewHolder.layout_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (menberGroupModel.getMemberCount() == 0 || noChildDataGroups.contains(menberGroupModel)) {
                        menberGroupModel.setShowTuisong(false);
                        expandGroupPosition = -1;
                    } else {
                        if (isExpanded) {
                            expandableListView.getRefreshableView().collapseGroup(groupPosition);
                            expandGroupPosition = -1;
                        } else if (slideDeleteArrayList.size() == 0) {

                            expandableListView.getRefreshableView().expandGroup(groupPosition, true);
                            if (menberGroupModel.isHasClick()) {
                                List<MenberInfoModel> menberInfos = copymenberAllDatas.get(menberGroupModel);
                                putDatas(menberGroupModel, menberInfos);
                                menbersListAdapter.addContent(menberAllDatas);
                                menbersListAdapter.notifyDataSetChanged();
                            } else {

                                loadFansData(menberGroupModel, true);
                                menbersListAdapter.notifyDataSetChanged();
                            }
                            expandGroupPosition = groupPosition;
                        } else {

                            closeOtherItem();
                        }
                    }

                }
            });

            gViewHolder.layout_delete.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    DeleteGroupRequest deleteGroupRequest = new DeleteGroupRequest(((MenberGroupModel) getGroup
                            (groupPosition)).getId());
                    deleteGroupRequest.start();
                    deleteGroupRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                        @Override
                        public void onOK() {
                            ViewUtils.showToast("删除成功");
                            closeAllGroup(menbersListAdapter.getGroupCount());
                            loadGroupData(true);
                        }

                        @Override
                        public void onFail(int code) {

                        }
                    });
                }
            });

            gViewHolder.tv_sendmessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<MenberInfoModel> menberInfoModels = copymenberAllDatas.get(menberGroupModel);
                    if (menberInfoModels.size() > 800) {
                        ChooseGroupPeopleAct.start((Activity) context, menberGroupModel.getId(), 1);
                    } else {
                        ChooseGroupPeopleAct.start((Activity) context, menberInfoModels, 1);
                    }
                }
            });
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, ViewGroup parent) {
            final MenberInfoModel menberInfoModel = (MenberInfoModel) getChild(groupPosition, childPosition);
          /*  if (groupPosition == 2) {
                return null;
            }*/

            ChildViewHolder cViewHolder;
            if (convertView == null) {
                cViewHolder = new ChildViewHolder();
//                convertView=inflateConvertView(R.layout.item_fans_child);
                convertView = LayoutInflater.from(context).inflate(R.layout.item_menbers_child, parent, false);
                cViewHolder.img_notice = (ImageView) convertView.findViewById(R.id.img_notice);
                cViewHolder.img_user_head = (CircleImageView) convertView.findViewById(R.id.img_user_head);
                cViewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                cViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                cViewHolder.layout_willingbuy = (LinearLayout) convertView.findViewById(R.id.layout_willingbuy);
                cViewHolder.tv_buyonline = (TextView) convertView.findViewById(R.id.tv_buyonline);
                cViewHolder.bottom_line = convertView.findViewById(R.id.bottom_line);
                cViewHolder.tv_call_time = (TextView) convertView.findViewById(R.id.tv_call_time);
                cViewHolder.tv_msg_time = (TextView) convertView.findViewById(R.id.tv_msg_time);
                cViewHolder.top_line = convertView.findViewById(R.id.top_line);
                cViewHolder.iv_telep = (ImageView) convertView.findViewById(R.id.iv_telep);
                cViewHolder.iv_msg = (ImageView) convertView.findViewById(R.id.iv_msg);
                cViewHolder.layout_container = (RelativeLayout) convertView.findViewById(R.id.layout_container);
                convertView.setTag(cViewHolder);
            } else {
                cViewHolder = (ChildViewHolder) convertView.getTag();
            }
            //TODO 赋值

            cViewHolder.tv_name.setText(getChild(groupPosition, childPosition).toString());
            if (!TextUtils.isEmpty(menberInfoModel.getRemarkName())) {
                cViewHolder.tv_name.setText(menberInfoModel.getRemarkName());
            } else {
                cViewHolder.tv_name.setText(menberInfoModel.getNickname());
            }
            cViewHolder.img_user_head.setImageUrl(menberInfoModel.getHeadUrl(), R.drawable.default_head_big);

            cViewHolder.tv_buyonline.setVisibility(menberInfoModel.getIsBuyOnline() == 1 ? View.VISIBLE : View.GONE);
            cViewHolder.tv_phone.setText(TextUtils.isEmpty(menberInfoModel.getMobilePhone()) ? "" : menberInfoModel.getMobilePhone());
            if (((MenberGroupModel) getGroup(groupPosition)).getId() == 2) {
                cViewHolder.img_notice.setVisibility(menberInfoModel.getIsNewBehavior() == 1 ? View.VISIBLE : View.INVISIBLE);
            } else {
                cViewHolder.img_notice.setVisibility(View.INVISIBLE);
            }
            if (menberInfoModel.getLastPhoneTime() == 0) {
                cViewHolder.iv_telep.setVisibility(View.GONE);
                cViewHolder.tv_call_time.setVisibility(View.GONE);
            } else {
                cViewHolder.iv_telep.setVisibility(View.VISIBLE);
                cViewHolder.tv_call_time.setVisibility(View.VISIBLE);
                long lastPhoneTime = menberInfoModel.getLastPhoneTime();
                String simpleTime = EaseConstant.getSimpleTime(lastPhoneTime);
                cViewHolder.tv_call_time.setText(simpleTime);
            }
            if (menberInfoModel.getLastSmsTime() == 0) {
                cViewHolder.iv_msg.setVisibility(View.GONE);
                cViewHolder.tv_msg_time.setVisibility(View.GONE);
            } else {
                cViewHolder.iv_msg.setVisibility(View.VISIBLE);
                cViewHolder.tv_msg_time.setVisibility(View.VISIBLE);
                cViewHolder.tv_msg_time.setText(EaseConstant.getSimpleTime(menberInfoModel.getLastSmsTime()));
            }
            if (childPosition == getChildrenCount(groupPosition) - 1) {
                cViewHolder.bottom_line.setVisibility(View.GONE);
            } else {
                cViewHolder.bottom_line.setVisibility(View.VISIBLE);
            }

            if (childPosition == 0) {
                cViewHolder.top_line.setVisibility(View.VISIBLE);
            } else {
                cViewHolder.top_line.setVisibility(View.GONE);
            }

            cViewHolder.layout_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshGroup = (MenberGroupModel) getGroup(groupPosition);
                    refreshMenber = menberInfoModel;
                    OutLineFansDetailAct.startCallBack(PurchaseGroupAct.this, menberInfoModel.getId(), refrenshCallBack, clearBehavoirCallBack);
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

        class GroupViewHolder {
            TextView tv_count;
            TextView tv_group_name;
            TextView tv_child_cout;
            TextView tv_sendmessage;
            View top_line;
            SlideDelete layout_sidedelete;
            RelativeLayout layout_container;
            LinearLayout layout_delete;
            ImageView group_img;
        }

        class ChildViewHolder {
            ImageView img_notice;
            CircleImageView img_user_head;
            TextView tv_phone;
            TextView tv_name;
            LinearLayout layout_willingbuy;
            TextView tv_buyonline;
            View bottom_line, top_line;
            TextView tv_call_time, tv_msg_time;
            ImageView iv_telep, iv_msg;
            RelativeLayout layout_container;
        }

    }

    /**
     * 清楚点击有轨迹的会员返回后清楚新轨迹回调
     */
    ParameCallBack clearBehavoirCallBack = new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            if (refreshMenber.getIsNewBehavior() == 1) {
                long menberId = (Long) object;
                //将组的轨迹数量减去一
                Iterator<MenberGroupModel> iterator = copymenberAllDatas.keySet().iterator();
                while (iterator.hasNext()) {
                    MenberGroupModel next = iterator.next();
                    if (next.getId() == 2) {
                        next.setNewMsgMemberCount(next.getNewMsgMemberCount() - 1 < 0 ? 0 : next.getNewMsgMemberCount() - 1);
                        Config.menberNewBehavoir = Config.menberNewBehavoir - 1;
                        if (Config.menberNewBehavoir <= 0) {
                            MainActivity.getIncetance().showKeDDRedPoint();
                        }
                    }
                }

                //拿到所有的该组会员
                List<MenberInfoModel> menberInfoModels = copymenberAllDatas.get(refreshGroup);
//                for (int i = 0; i < menberInfoModels.size(); i++) {
//                    if(menberInfoModels.get(i).getId()==menberId){
//                        menberInfoModels.get(i).setIsNewBehavior(0);
//                    }
//                }
                //遍历该组会员  找到刚刚已经浏览过的有轨迹的会员  去掉轨迹红点  并且重新添加到之前的位置
                Iterator<MenberInfoModel> menbersIterator = menberInfoModels.iterator();
                while (menbersIterator.hasNext()) {
                    MenberInfoModel next = menbersIterator.next();
                    if (next.getId() == refreshMenber.getId()) {

                        menbersIterator.remove();
                    }
                }
                //将该会员的新轨迹提示去掉
                refreshMenber.setIsNewBehavior(0);
                menberInfoModels.add(refreshMenber.getLoacation(), refreshMenber);
                menbersListAdapter.notifyDataSetChanged();
            }
        }
    };

    //会员写备注之后 保存成功后更新会员页面的该会员信息
    ParameCallBack refrenshCallBack = new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            MenberInfoModel menberDtail = (MenberInfoModel) object;
            //获取写备注时新添加的分组名集合
            if (menberDtail.getLocationGroupName().size() != 0) {
                addMemberToNewGroup(menberDtail);
            }
            //找出所有组（已经点击过的）有该会员的  如果有那么就都更新
            updateOtherGroup(menberDtail);
            menbersListAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 如果写备注 该会员新添加的会员组就会调用这个方法
     * 将该粉丝添加到新添的会员组中  如果新添的会员组已经加载过数据那么就增加数据到该组  没有那么就将该组会员数加一
     *
     * @param menberDtail
     */
    private void addMemberToNewGroup(MenberInfoModel menberDtail) {
        //新增组
        List<MenberGroupModel> locationGroupName = menberDtail.getLocationGroupName();
        for (int i = 0; i < locationGroupName.size(); i++) {
            //已经点击过的组别中是否有选择的分组
            if (hasClickGroup.contains(locationGroupName.get(i))) {
                List<MenberInfoModel> menberInfoModels = copymenberAllDatas.get(locationGroupName.get(i));
                menberInfoModels.add(menberDtail);
            } else {
                //没有点击过的
                for (int i1 = 0; i1 < menberGroups.size(); i1++) {
                    if (locationGroupName.get(i).getId() == menberGroups.get(i1).getId()) {
                        menberGroups.get(i1).setMemberCount(menberGroups.get(i1).getMemberCount() + 1);
                    }
                }

            }
            //默认不显示的分组中是否选择了（默认不显示的分组肯定是没有点击过的）如果含有  那么就将该默认不显示的组会员数加一 然后添加到数据中
            if (notShowGroup.contains(locationGroupName.get(i))) {
                for (int i1 = 0; i1 < notShowGroup.size(); i1++) {
                    if (locationGroupName.get(i).getId() == notShowGroup.get(i1).getId()) {
                        notShowGroup.get(i1).setMemberCount(notShowGroup.get(i1).getMemberCount() + 1);
                        copymenberAllDatas.put(notShowGroup.get(i1), new ArrayList<MenberInfoModel>());
                    }
                }
            }
        }

    }

    private void updateOtherGroup(MenberInfoModel menberDtail) {
        //找出已经加载过的组别  也就是已经使用缓存的组别
        if (!menberGroups.isEmpty()) {
            //找出已经点击过的分组
            ArrayList<MenberGroupModel> hasClickGroup = new ArrayList<MenberGroupModel>();
            for (int i = 0; i < menberGroups.size(); i++) {
                if (menberGroups.get(i).isHasClick()) {
                    hasClickGroup.add(menberGroups.get(i));
                }
            }
            for (int i = 0; i < hasClickGroup.size(); i++) {
                refreshSingleMembers(menberDtail, hasClickGroup.get(i));
            }
        }
    }

    private void refreshSingleMembers(MenberInfoModel menberDtail, MenberGroupModel refreshGroup) {
        if (menberDtail != null && refreshGroup != null) {
            List<MenberInfoModel> menberInfoModels = copymenberAllDatas.get(refreshGroup);
            for (int i = 0; i < menberInfoModels.size(); i++) {
                if (menberInfoModels.get(i).getId() == menberDtail.getId()) {
                    if (!TextUtils.isEmpty(menberDtail.getRemarkName())) {
                        menberInfoModels.get(i).setRemarkName(menberDtail.getRemarkName());
                    }
                }
            }
            menbersListAdapter.notifyDataSetChanged();
        }
    }

    private void closeOtherItem() {
//        System.out.println("closeOtherItem");
        // 采用Iterator的原因是for是线程不安全的，迭代器是线程安全的
        ListIterator<SlideDelete> slideDeleteListIterator = slideDeleteArrayList.listIterator();
        while (slideDeleteListIterator.hasNext()) {
            SlideDelete slideDelete = slideDeleteListIterator.next();
            slideDelete.isShowDelete(false);
        }
        slideDeleteArrayList.clear();
    }


}
