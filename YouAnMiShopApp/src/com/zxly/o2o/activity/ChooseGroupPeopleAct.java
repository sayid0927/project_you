package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.FansInfo;
import com.zxly.o2o.model.MenberInfoModel;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FilterFansRequest;
import com.zxly.o2o.request.GetFansInfoRequest;
import com.zxly.o2o.request.GetMenberInfoRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hejun on 2016/9/8.
 * 选择群推人员
 */
public class ChooseGroupPeopleAct extends BasicAct{

    private TextView txt_count;
    private ListView listview;
    private LoadingView loadingview;
    private ArrayList<String> userList;
    private CheckBox img_check;
    private MyAdapter myAdapter;
    private List<FansInfo> fansInfoList;
    private ArrayList<FansInfo> chooseList;

    private List<MenberInfoModel> menberInfoList;
    private ArrayList<MenberInfoModel> chooseMemberList;

    private List<Object> initialList;
    private List<Object> templeList;
    private ArrayList<Object> mixChooseList;
    private List<Object> notCheckList;//保存新增前没有被选中的
    private static final int REQUESTCODE_PICK = 1;
    int groupId;//群推会员
    private boolean isChooseAllFans;
    private static ChooseGroupPeopleAct instance;
    private List<FansInfo> fansList;
    private int loadMenberData;
    private int type;//1 会员  2 粉丝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.win_choosegroup_send);
        instance =this;
        findAndInitView();
        getIntentData();
    }

    private void getIntentData() {
        //粉丝
        String groupName = getIntent().getStringExtra("groupName");
        //会员
        groupId = getIntent().getIntExtra("groupId", 0);
        //是否是无会员时  点击“立即跟粉丝打个招呼吧”按钮进入 不需要id或者组名
        isChooseAllFans =getIntent().getBooleanExtra("isChooseAllFans",false);
        type =getIntent().getIntExtra("type",-1);
        menberInfoList=getIntent().getParcelableArrayListExtra("menberInfoList");
        fansList = getIntent().getParcelableArrayListExtra("fansList");
        if(isChooseAllFans){
            getAllFansData();
        }else {

            if(type==1){
                if(menberInfoList!=null){
                    initialList.addAll(menberInfoList);
                    myAdapter.addItem(initialList);
                    myAdapter.notifyDataSetChanged();
                    img_check.setChecked(true);
                }else if(groupId!=0){
                    getMemberWithGroupName(groupId);
                }
            }else {
                if(fansList!=null){
                    Iterator<FansInfo> iterator = fansList.iterator();
                    while (iterator.hasNext()){
                        FansInfo next = iterator.next();
                        //筛选掉线下粉丝
                        if(next.getIsOffline()==1||TextUtils.isEmpty(next.getImei())){
                            iterator.remove();
                        }
                    }
                    initialList.addAll(fansList);
                    myAdapter.addItem(initialList);
                    myAdapter.notifyDataSetChanged();
                    img_check.setChecked(true);
                }else if(!TextUtils.isEmpty(groupName)){
                    getDataWithGroupName(groupName);
                }
            }
           /* if (menberInfoList != null) {//群推会员
//                getMemberWithGroupName(groupId);
                initialList.addAll(menberInfoList);
                myAdapter.addItem(initialList);
                myAdapter.notifyDataSetChanged();
                img_check.setChecked(true);
            } else if (fansList!=null) {//群推粉丝
//                getDataWithGroupName(groupName);
                initialList.addAll(fansList);
                myAdapter.addItem(initialList);
                myAdapter.notifyDataSetChanged();
                img_check.setChecked(true);
            }*/
        }

    }

    private void getAllFansData() {
        final FilterFansRequest filterFansRequest = new FilterFansRequest(0,null,null,null);
        filterFansRequest.start();
        filterFansRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                initialList.addAll(filterFansRequest.getFansInfoList());
                myAdapter.addItem(initialList);
                myAdapter.notifyDataSetChanged();
                if(filterFansRequest.getFansInfoList().size()!=0){
                    img_check.setChecked(true);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });

        /*final GetAllFansInfoRequest getAllFansInfoRequest = new GetAllFansInfoRequest();
        getAllFansInfoRequest.start();
        getAllFansInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                initialList.addAll(getAllFansInfoRequest.getAllFansList());
                myAdapter.addItem(initialList);
                myAdapter.notifyDataSetChanged();
                img_check.setChecked(true);
            }

            @Override
            public void onFail(int code) {

            }
        });*/
    }


    private void getDataWithGroupName(final String groupName) {
        final GetFansInfoRequest getFansInfoRequest = new GetFansInfoRequest(groupName,true);
        getFansInfoRequest.start();
        getFansInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                //如果是从我关注的粉丝组点击推送进来的  那么就需要对其中可能存在的线下粉丝筛选
                List<FansInfo> fansInfoList = getFansInfoRequest.getFansInfoList();
                if(groupName.equals("我关注的粉丝")){
                    Iterator<FansInfo> iterator = fansInfoList.iterator();
                    while (iterator.hasNext()){
                        FansInfo next = iterator.next();
                        if(TextUtils.isEmpty(next.getImei())){
                            iterator.remove();
                        }
                    }
                }
                initialList.addAll(fansInfoList);
                myAdapter.addItem(initialList);
                myAdapter.notifyDataSetChanged();
                img_check.setChecked(true);
            }

            @Override
            public void onFail(int code) {
                loadingview.onDataEmpty("加载失败");
            }
        });
    }

    private void getMemberWithGroupName(int groupId) {
        final GetMenberInfoRequest getMenberInfoRequest = new GetMenberInfoRequest(groupId);
        getMenberInfoRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                initialList.addAll(getMenberInfoRequest.getMenberInfoList());
                myAdapter.addItem(initialList);
                myAdapter.notifyDataSetChanged();
                img_check.setChecked(true);
            }

            @Override
            public void onFail(int code) {
                loadingview.onDataEmpty("加载失败");
            }
        });
        getMenberInfoRequest.start(this);
    }

    private void findAndInitView() {
        initialList = new ArrayList<Object>();
        mixChooseList = new ArrayList<Object>();
        chooseList = new ArrayList<FansInfo>();
        chooseMemberList = new ArrayList<MenberInfoModel>();
        notCheckList=new ArrayList<Object>();
        //listview头部
        LinearLayout head = (LinearLayout) LayoutInflater.from(ChooseGroupPeopleAct.this).inflate(R.layout.head_addall, null, false);
        head.findViewById(R.id.layout_addall).setOnClickListener(noDoubleClickListener);
        img_check = (CheckBox) head.findViewById(R.id.img_check);
        ((TextView) head.findViewById(R.id.tv_addall)).setText("全选");
        //人数
        txt_count = (TextView) findViewById(R.id.txt_count);
        listview = (ListView) findViewById(R.id.listview);
        loadingview = (LoadingView) findViewById(R.id.view_loading);
        listview.addHeaderView(head);
        myAdapter = new MyAdapter(this);
        listview.setAdapter(myAdapter);

        findViewById(R.id.btn_back).setOnClickListener(noDoubleClickListener);
        findViewById(R.id.btn_top_right).setOnClickListener(noDoubleClickListener);
        findViewById(R.id.btn_done).setOnClickListener(noDoubleClickListener);
        img_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mixChooseList.clear();
                    notCheckList.clear();
                    mixChooseList.addAll(myAdapter.getContent());
                } else {
                    mixChooseList.clear();
                    notCheckList.clear();
                    notCheckList.addAll(myAdapter.getContent());
                }
                txt_count.setText(mixChooseList.size() + "");
                myAdapter.clear();
                myAdapter.addItem(initialList);
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    //粉丝
    ParameCallBack parameCallBack1 = new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            if (object != null) {
                List<FansInfo> fansInfoList = (List<FansInfo>) object;

                initialList.addAll(fansInfoList);
                Set<Object> removeSamFansInfos =new HashSet<Object>();
                removeSamFansInfos.addAll(initialList);
//效率不高舍弃
//                Set<Object> removeSamFansInfo =new HashSet<Object>();
//                removeSamFansInfo.addAll(initialList);
//                //去重粉丝  返回所有粉丝
//                List<FansInfo> removeSamFansInfos = removeSameFans(initialList, fansInfoList);
//                //去掉混合数据中的所有粉丝
//                Iterator<Object> iterator = initialList.iterator();
//                while (iterator.hasNext()){
//                    Object next = iterator.next();
//                    if(next instanceof FansInfo){
//                        //删除之前混合数据中的粉丝
//                        iterator.remove();
//                    }
//                }
                //添加去重后的所有会员
                initialList.clear();
                initialList.addAll(removeSamFansInfos);
                myAdapter.clear();
                myAdapter.addItem(initialList);
                mixChooseList.clear();
                mixChooseList.addAll(initialList);
                setCheckImage();
//效率不高舍弃
//                for (int i = 0; i < initialList.size(); i++) {
//                    //再次添加的粉丝中又包含前面没有勾选的  那么就再次勾选
//                    for (int i1 = 0; i1 < fansInfoList.size(); i1++) {
//                        if(!mixChooseList.contains(fansInfoList.get(i1))&&initialList.get(i).equals(fansInfoList.get(i1))){
//                            mixChooseList.add(fansInfoList.get(i1));
//                        }
//                    }
//                    //之前的粉丝没有新增的粉丝  那么就默认选中
//                    if(!notCheckList.contains(initialList.get(i))&&!mixChooseList.contains(initialList.get(i))){
//                        mixChooseList.add(initialList.get(i));
//                    }
//
//                }
                if(mixChooseList.size()==initialList.size()){
                    img_check.setChecked(true);
                }
                txt_count.setText(mixChooseList.size() + "");
                myAdapter.notifyDataSetChanged();
//                img_check.setChecked(false);
//                img_check.setChecked(true);
            }
        }
    };

    /**
     * 筛选返回的粉丝集合中与前面粉丝集合相同的  去重
     * @param fansInfoList  开始选好的会员及粉丝混合集合
     * @param fansInfoListAdd  后面添加的会员或者粉丝（不会同时有）
     * @return 返回去重后fans集合
     */
    private List<FansInfo> removeSameFans(List<Object> fansInfoList, List<FansInfo> fansInfoListAdd) {
        //临时集合
        List<FansInfo> templeFans=new ArrayList<FansInfo>();
        if (!fansInfoList.isEmpty()){
            for (Object object : fansInfoList) {
                if (object instanceof FansInfo) {
                    templeFans.add((FansInfo) object);
                }
            }
        }
        templeFans.addAll(fansInfoListAdd);
        ArrayList<FansInfo> newList = new ArrayList<FansInfo>();
        Set<Integer> set = new HashSet<Integer>();
        for (FansInfo item:templeFans){
            if (set.add(item.getFansId())){
                newList.add(item);
            }
        }
        return newList;
    }

    //会员
    ParameCallBack parameCallBack2 = new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            if (object != null) {
                List<MenberInfoModel> menberInfoModelList = (List<MenberInfoModel>) object;

                initialList.addAll(menberInfoModelList);
                Set<Object> removeSamMenberInfos =new HashSet<Object>();
                removeSamMenberInfos.addAll(initialList);

                //去重会员  返回所有会员
//                List<MenberInfoModel> removeSamMenberInfos = removeSameMenber(initialList,menberInfoModelList);
                //去掉混合数据中的所有会员
//                Iterator<Object> iterator = initialList.iterator();
//                while (iterator.hasNext()){
//                    Object next = iterator.next();
//                    if(next instanceof MenberInfoModel){
//                        //删除之前混合数据中的会员
//                        iterator.remove();
//                    }
//                }
                //添加去重后的所有会员
                initialList.clear();
                initialList.addAll(removeSamMenberInfos);
                myAdapter.clear();
                myAdapter.addItem(initialList);
                mixChooseList.clear();
                mixChooseList.addAll(initialList);
                setCheckImage();

//                for (int i = 0; i < initialList.size(); i++) {
//                    //再次添加的会员中又包含前面没有勾选的  那么就再次勾选
//                    for (int i1 = 0; i1 < menberInfoModelList.size(); i1++) {
//                        if(!mixChooseList.contains(menberInfoModelList.get(i1))&&initialList.get(i).equals(menberInfoModelList.get(i1))){
//                            mixChooseList.add(menberInfoModelList.get(i1));
//                        }
//                    }
//                    //之前的粉丝没有新增的会员  那么就默认选中
//                    if(!notCheckList.contains(initialList.get(i))&&!mixChooseList.contains(initialList.get(i))){
//                        mixChooseList.add(initialList.get(i));
//                    }
//                }
                if(mixChooseList.size()==initialList.size()){
                    img_check.setChecked(true);
                }
                txt_count.setText(mixChooseList.size() + "");
                myAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 作用：返回的数据中如果包含之前没有选中的，那么返回的时候照样得将这些数据选中状态
     */
    private void setCheckImage() {
        Map<String,Boolean> map=new HashMap<String,Boolean>();
        for (int i = 0; i < initialList.size(); i++) {
            if(initialList.get(i) instanceof MenberInfoModel){
                map.put("men"+((MenberInfoModel)initialList.get(i)).getId(),true);
            }else {
                map.put("fan"+((FansInfo)initialList.get(i)).getFansId(),true);
            }
        }

        for (Object notCheck: notCheckList) {
            if(notCheck instanceof MenberInfoModel){
                if(!map.get("men"+((MenberInfoModel) notCheck).getId())){
                    mixChooseList.remove(notCheck);
                }
            }else {
                if(!map.get("fan"+((FansInfo) notCheck).getFansId())){
                    mixChooseList.remove(notCheck);
                }
            }
        }
    }

    private ArrayList<MenberInfoModel> removeSameMenber(List<Object> menberInfoList, List<MenberInfoModel> menberInfoModelListAdd) {
        //临时集合
        List<MenberInfoModel> templeMenber=new ArrayList<MenberInfoModel>();
        if (!menberInfoList.isEmpty()){
            for (Object object : menberInfoList) {
                if (object instanceof MenberInfoModel) {
                    templeMenber.add((MenberInfoModel) object);
                }
            }
        }

        templeMenber.addAll(menberInfoModelListAdd);
        ArrayList<MenberInfoModel> newList = new ArrayList<MenberInfoModel>();
        Set<Long> set = new HashSet<Long>();
        for (MenberInfoModel item:templeMenber){
            if (set.add(item.getId())){
                newList.add(item);
            }
        }
        return newList;
    }

    private ArrayList<Long> getUserIdsByMemberList(ArrayList<Object> chooseMemberList) {
        ArrayList<Long> userIds = new ArrayList<Long>();
        for (Object object : chooseMemberList) {
            if (object instanceof MenberInfoModel) {
                userIds.add(((MenberInfoModel) object).getId());
            }
        }
        return userIds;
    }

    private ArrayList<String> getFansImeisByList(ArrayList<Object> chooseMemberList) {
        ArrayList<String> fansImeis = new ArrayList<String>();
        for (Object object : chooseMemberList) {
            if (object instanceof FansInfo) {
                //判断该粉丝是否是线下录入的  是的就不添加
                if(((FansInfo) object).getIsOffline()==0){
                    fansImeis.add(((FansInfo) object).getImei());
                }
            }
        }
        return fansImeis;
    }

    public static ChooseGroupPeopleAct getInstance() {
        return instance;
    }

    class MyAdapter extends ObjectAdapter {

        private int TYPE_FANS = 0;
        private int TYPE_MEMBER = 1;
        private int TYPE_COUNT = 2;

        public MyAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getItemViewType(int position) {
            Object object = getItem(position);
            if (object instanceof FansInfo) {
                return TYPE_FANS;
            } else if (object instanceof MenberInfoModel) {
                return TYPE_MEMBER;
            }
            return TYPE_MEMBER;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_COUNT;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            int type = getItemViewType(position);
            if (convertView == null) {
                if (type == TYPE_FANS) {//粉丝
                    convertView = inflateConvertView(R.layout.item_choose_grouppeople);
                    viewHolder = new FansViewHolder();
                    ((FansViewHolder) viewHolder).tv_imei = (TextView) convertView.findViewById(R.id.tv_imei);
                    ((FansViewHolder) viewHolder).txt_phone_brand = (TextView) convertView.findViewById(R.id.txt_phone_brand);
                    ((FansViewHolder) viewHolder).fans_name = (TextView) convertView.findViewById(R.id.fans_name);
                    ((FansViewHolder) viewHolder).layout_willingbuy = (LinearLayout) convertView.findViewById(R.id.layout_willingbuy);
                    ((FansViewHolder) viewHolder).star_first = (ImageView) convertView.findViewById(R.id.star_first);
                    ((FansViewHolder) viewHolder).star_second = (ImageView) convertView.findViewById(R.id.star_second);
                    ((FansViewHolder) viewHolder).star_third = (ImageView) convertView.findViewById(R.id.star_third);
                } else if (type == TYPE_MEMBER) {//会员
                    convertView = inflateConvertView(R.layout.item_choose_groupmember);
                    viewHolder = new MemberViewHolder();
                    ((MemberViewHolder) viewHolder).tv_name = (TextView) convertView.findViewById(R.id.txt_member_name);
                    ((MemberViewHolder) viewHolder).tv_phone = (TextView) convertView.findViewById(R.id.txt_member_phone);
                } else {
                    viewHolder = new ViewHolder();
                }
                viewHolder.img_user_head = (CircleImageView) convertView.findViewById(R.id.img_user_head);
                viewHolder.img_check = (ImageView) convertView.findViewById(R.id.img_check);
                viewHolder.layout_container = (RelativeLayout) convertView.findViewById(R.id.layout_container);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final Object object = getItem(position);

            if (type == TYPE_FANS){
                FansViewHolder fansViewHolder = (FansViewHolder) viewHolder;
                final FansInfo fansInfo = (FansInfo) object;
                //如果粉丝有姓名  那么就显示姓名与购买意向否则显示imei
                if (!TextUtils.isEmpty(fansInfo.getName())) {
                    fansViewHolder.tv_imei.setVisibility(View.GONE);
                    fansViewHolder.fans_name.setVisibility(View.VISIBLE);
                    fansViewHolder.fans_name.setText(fansInfo.getName());
                    fansViewHolder.layout_willingbuy.setVisibility(View.VISIBLE);
                    if (fansInfo.getBuyIntention() == 1) {
                        fansViewHolder.star_first.setBackgroundResource(R.drawable.icon_lightstar);
                        fansViewHolder.star_second.setBackgroundResource(R.drawable.icon_darkstar);
                        fansViewHolder.star_third.setBackgroundResource(R.drawable.icon_darkstar);
                    } else if (fansInfo.getBuyIntention() == 2) {
                        fansViewHolder.star_first.setBackgroundResource(R.drawable.icon_lightstar);
                        fansViewHolder.star_second.setBackgroundResource(R.drawable.icon_lightstar);
                        fansViewHolder.star_third.setBackgroundResource(R.drawable.icon_darkstar);
                    } else if (fansInfo.getBuyIntention() == 3) {
                        fansViewHolder.star_first.setBackgroundResource(R.drawable.icon_lightstar);
                        fansViewHolder.star_second.setBackgroundResource(R.drawable.icon_lightstar);
                        fansViewHolder.star_third.setBackgroundResource(R.drawable.icon_lightstar);
                    }else {
                        fansViewHolder.star_first.setVisibility(View.GONE);
                        fansViewHolder.star_second.setVisibility(View.GONE);
                        fansViewHolder.star_third.setVisibility(View.GONE);
                    }
                } else {
                    fansViewHolder.tv_imei.setVisibility(View.VISIBLE);
                    fansViewHolder.fans_name.setVisibility(View.GONE);
                    fansViewHolder.layout_willingbuy.setVisibility(View.GONE);
                    fansViewHolder.tv_imei.setText(fansInfo.getImei());
                }
                fansViewHolder.txt_phone_brand.setText(fansInfo.getPhoneModel());
            } else if(type == TYPE_MEMBER){
                MemberViewHolder memberViewHolder = (MemberViewHolder) viewHolder;
                final MenberInfoModel menberInfoModel = (MenberInfoModel) object;
                if(!TextUtils.isEmpty(menberInfoModel.getRemarkName())){
                    ViewUtils.setText(memberViewHolder.tv_name, menberInfoModel.getRemarkName());
                }else {
                    ViewUtils.setText(memberViewHolder.tv_name, menberInfoModel.getNickname());
                }
                ViewUtils.setText(memberViewHolder.tv_phone, menberInfoModel.getUserName());
            }

            if (mixChooseList.contains(object)) {
                viewHolder.img_check.setBackgroundResource(R.drawable.icon_check_press);
            } else {
                viewHolder.img_check.setBackgroundResource(R.drawable.icon_check_normal);
            }

            viewHolder.layout_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mixChooseList.contains(object)) {
                        mixChooseList.remove(object);
                        notCheckList.add(object);
                    } else {
                        mixChooseList.add(object);
                        if(notCheckList.contains(object)){
                            notCheckList.remove(object);
                        }
                    }
                    txt_count.setText(mixChooseList.size() + "");
                    notifyDataSetChanged();
                    if(mixChooseList.size()==0){
                        img_check.setChecked(false);
                    }
                    if(mixChooseList.size()==myAdapter.getCount()){
                        img_check.setChecked(true);
                    }
                }
            });
            return convertView;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_choose_grouppeople;
        }

        class ViewHolder {
            CircleImageView img_user_head;
            ImageView img_check;
            RelativeLayout layout_container;

        }

        class FansViewHolder extends ViewHolder {
            TextView tv_imei;
            TextView txt_phone_brand;
            TextView fans_name;
            LinearLayout layout_willingbuy;
            ImageView star_first, star_second, star_third;
        }

        class MemberViewHolder extends ViewHolder {
            //            CircleImageView img_user_head;
            TextView tv_name;
            TextView tv_phone;
        }
    }

    public static void start(Activity curAct, String groupName,int type) {
        Intent intent = new Intent(curAct, ChooseGroupPeopleAct.class);
        intent.putExtra("type",type);
        intent.putExtra("groupName", groupName);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void startAct(Activity curAct, List<FansInfo> fansList,int type) {
        Intent intent = new Intent(curAct, ChooseGroupPeopleAct.class);
        intent.putExtra("type",type);
        intent.putParcelableArrayListExtra("fansList", (ArrayList<? extends Parcelable>) fansList);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, int groupId,int type) {
        Intent intent = new Intent(curAct, ChooseGroupPeopleAct.class);
        intent.putExtra("type",type);
        intent.putExtra("groupId", groupId);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, List<MenberInfoModel> menberInfoList,int type) {
        Intent intent = new Intent(curAct, ChooseGroupPeopleAct.class);
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("menberInfoList", (ArrayList<? extends Parcelable>) menberInfoList);
        bundle.putInt("type",type);
        intent.putExtras(bundle);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void start(Activity curAct, boolean isChooseAllFans) {
        Intent intent = new Intent(curAct, ChooseGroupPeopleAct.class);
        intent.putExtra("isChooseAllFans", isChooseAllFans);
        ViewUtils.startActivity(intent, curAct);
    }


    NoDoubleClickListener noDoubleClickListener=new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    finish();
                    break;
                //新增
                case R.id.btn_top_right:
                    FilterPeopleAct.start(ChooseGroupPeopleAct.this, parameCallBack1, parameCallBack2);
//                    FilterPeopleAct.startForResult(ChooseGroupPeopleAct.this);
                    break;
                //确定
                case R.id.btn_done:
                    if (mixChooseList.size() != 0) {
                        ArrayList<Long> userIds = getUserIdsByMemberList(mixChooseList);
                        ArrayList<String> fansImeis = getFansImeisByList(mixChooseList);
                        ChooseSendAct.start(ChooseGroupPeopleAct.this, fansImeis, userIds);
                    } else {
                        ViewUtils.showToast("推送人员不能为空哦");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&data!=null){
            ArrayList<Parcelable> list = data.getParcelableArrayListExtra("list");
        }
    }
}
