package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxly.o2o.adapter.FansGroupTagAdapter;
import com.zxly.o2o.adapter.MenberGroupTagAdapter;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.FansGroupModel;
import com.zxly.o2o.model.FansInfo;
import com.zxly.o2o.model.MenberGroupModel;
import com.zxly.o2o.model.MenberInfoModel;
import com.zxly.o2o.model.TagModel;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FilterFansRequest;
import com.zxly.o2o.request.FilterMenberRequest;
import com.zxly.o2o.request.GetFansGroupRequest;
import com.zxly.o2o.request.GetMenberGroupReuqest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.FlowTagLayout;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejun on 2016/9/8.
 * 筛选人员
 */
public class FilterPeopleAct extends BasicAct implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private TextView btn_title_right;
    private RadioButton txt_fans;
    private RadioButton txt_menbers;

    private TextView tag_cu_menber,tag_cm_menber,tag_cn_menber;
    private List<String> operator_types_menber=new ArrayList<String>();
    private List<String> operator_types_fans=new ArrayList<String>();
    private List<String> willBuys=new ArrayList<String>();
    private RadioButton rb_men_menber;
    private RadioButton rb_woman_menber;
    private int gender_menber;
    private int gender_fans;
    private LinearLayout buy_willing_one;
    private LinearLayout buy_willing_two;
    private LinearLayout buy_willing_three;
    private RadioButton rb_men_fans;
    private RadioButton rb_woman_fans;
    private TextView tag_cu_fans;
    private TextView tag_cm_fans;
    private TextView tag_cn_fans;
    private List<FansGroupModel> fansGroups=new ArrayList<FansGroupModel>();
    private List<MenberGroupModel> menberGroupModels=new ArrayList<MenberGroupModel>();
    private RelativeLayout layout_choose_group;
    private FlowTagLayout groupTag_fans;
    private FlowTagLayout flowlayout_menber;
    private FansGroupTagAdapter mTagAdapter;
    private MenberGroupTagAdapter menberAdapter;
    private List<String> chooseFansGroups;
    private List<Long> chooseMenbersGroups;
    static ParameCallBack parameCallBack1;
    static ParameCallBack parameCallBack2;
    private int menberCount;
    private RelativeLayout layout_menber_group;
    private LoadingView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.win_filter_menbers);
        findAndInitView();
    }

    /**
     * 获取分组（粉丝和会员一起）
     */
    private void getGroupData() {
        final GetFansGroupRequest getFansGroupRequest = new GetFansGroupRequest(true);
        getFansGroupRequest.start();
        getFansGroupRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                fansGroups = getFansGroupRequest.getFansGroups();
                Config.fansCount=getFansGroupRequest.getFansGroups().size();
                if(!fansGroups.isEmpty()){
                    List<TagModel> dataSource = new ArrayList<TagModel>();
                    for (int i = 0; i < fansGroups.size(); i++) {
                        //以下组不管有没有粉丝数都不展示
                        if(fansGroups.get(i).getGroup().equals("线下录入粉丝")||fansGroups.get(i).getGroup().equals("新粉丝")||fansGroups.get(i).getGroup().equals("我关注的粉丝")){
                        }else {
                            //其他组没有粉丝就不展示
                            if(fansGroups.get(i).getNum()!=0){
                                dataSource.add(new TagModel(fansGroups.get(i).getGroup(),0,false));
                            }
                        }
                    }
                    mTagAdapter.onlyAddAll(dataSource);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });

        final GetMenberGroupReuqest getMenberGroupRequest = new GetMenberGroupReuqest(false);
        getMenberGroupRequest.start();
        getMenberGroupRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                menberGroupModels = getMenberGroupRequest.getMenberGroups();
                if(!menberGroupModels.isEmpty()){
                    List<TagModel> dataSource = new ArrayList<TagModel>();
                    for (int i = 0; i < menberGroupModels.size(); i++) {
                        //会员组 只通过id辨别 并且再三与后台确认过id不会改变
                        //新会员id为1  这个组不显示  其他组如果会员数为0也不显示
                        if(menberGroupModels.get(i).getId()!=1&&menberGroupModels.get(i).getMemberCount()!=0){
                            dataSource.add(new TagModel(menberGroupModels.get(i).getName(),menberGroupModels.get(i).getId(),false));
                        }
                        menberCount=menberCount+menberGroupModels.get(i).getMemberCount();
                    }
                    menberAdapter.onlyAddAll(dataSource);
                    if(menberCount==0){
                        layout_menber_group.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFail(int code) {

            }
        });

    }

    private void findAndInitView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("筛选人员");
        btn_title_right = (TextView) findViewById(R.id.btn_title_right);
        btn_title_right.setVisibility(View.VISIBLE);
        btn_title_right.setText("确定");
        btn_title_right.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //保存筛选粉丝
                if(txt_fans.isChecked()){
                    filterFans();
                }else{
                    //保存筛选会员
                    filterMenber();
                }
            }
        });


        txt_fans = (RadioButton) findViewById(R.id.txt_fans);
        txt_menbers = (RadioButton) findViewById(R.id.txt_menbers);

        txt_fans.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    findViewById(R.id.layout_fans).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.layout_fans).setVisibility(View.GONE);
                }
            }
        });
        txt_menbers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    findViewById(R.id.layout_menbers).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.layout_menbers).setVisibility(View.GONE);
                }
            }
        });

        //点击粉丝展现组件
        buy_willing_one = (LinearLayout) findViewById(R.id.buy_willing_one);
        buy_willing_two = (LinearLayout) findViewById(R.id.buy_willing_two);
        buy_willing_three = (LinearLayout) findViewById(R.id.buy_willing_three);
        layout_choose_group = (RelativeLayout) findViewById(R.id.layout_choose_group);
        groupTag_fans = (FlowTagLayout)findViewById(R.id.tagflowlayout);

        buy_willing_one.setOnClickListener(this);
        buy_willing_two.setOnClickListener(this);
        buy_willing_three.setOnClickListener(this);
        rb_men_fans = (RadioButton) findViewById(R.id.rb_men_fans);
        rb_woman_fans = (RadioButton) findViewById(R.id.rb_woman_fans);
        rb_men_fans.setOnCheckedChangeListener(this);
        rb_woman_fans.setOnCheckedChangeListener(this);

        tag_cu_fans = (TextView) findViewById(R.id.tag_cu_fans);
        tag_cm_fans = (TextView) findViewById(R.id.tag_cm_fans);
        tag_cn_fans = (TextView) findViewById(R.id.tag_cn_fans);
        tag_cu_fans.setOnClickListener(this);
        tag_cm_fans.setOnClickListener(this);
        tag_cn_fans.setOnClickListener(this);

        //点击会员展现组件
        layout_menber_group = (RelativeLayout) findViewById(R.id.layout_menber_group);
        flowlayout_menber = (FlowTagLayout) findViewById(R.id.flowlayout_menber);
        //性别
        rb_men_menber = (RadioButton) findViewById(R.id.rb_men_menber);
        rb_woman_menber = (RadioButton) findViewById(R.id.rb_woman_menber);
        rb_men_menber.setOnCheckedChangeListener(this);
        rb_woman_menber.setOnCheckedChangeListener(this);

        //联通 移动 电信
        tag_cu_menber = (TextView) findViewById(R.id.tag_cu_menber);
        tag_cm_menber = (TextView) findViewById(R.id.tag_cm_menber);
        tag_cn_menber = (TextView) findViewById(R.id.tag_cn_menber);


        tag_cu_menber.setOnClickListener(this);
        tag_cm_menber.setOnClickListener(this);
        tag_cn_menber.setOnClickListener(this);


        mTagAdapter = new FansGroupTagAdapter(this);
        groupTag_fans.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        groupTag_fans.setAdapter(mTagAdapter);
        chooseFansGroups = new ArrayList<String>();
        groupTag_fans.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    chooseFansGroups.clear();
                    for (int i = 0; i < selectedList.size(); i++) {
                        chooseFansGroups.add(((TagModel)(parent.getAdapter().getItem(selectedList.get(i)))).getName());
                    }
                }
            }
        });

        menberAdapter =new MenberGroupTagAdapter(this);
        flowlayout_menber.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        flowlayout_menber.setAdapter(menberAdapter);
        chooseMenbersGroups = new ArrayList<Long>();
        flowlayout_menber.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    chooseMenbersGroups.clear();
                    for (int i = 0; i < selectedList.size(); i++) {
                        chooseMenbersGroups.add(((TagModel)(parent.getAdapter().getItem(selectedList.get(i)))).getId());
                    }
                }
            }
        });
        lv = (LoadingView) findViewById(R.id.lv);
        getGroupData();
        if(Config.fansCount==0){
            layout_choose_group.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    finish();
                break;

                //联通
                case R.id.tag_cu_menber:
                    if(operator_types_menber.contains(2+"")){
                        setBtnNormal(tag_cu_menber);
                        operator_types_menber.remove(2+"");
                    }else{
                        setBtnPress(tag_cu_menber);
                        operator_types_menber.add(2+"");
                    }
                break;
                //移动
                case R.id.tag_cm_menber:
                    if(operator_types_menber.contains(1+"")){
                        setBtnNormal(tag_cm_menber);
                        operator_types_menber.remove(1+"");
                    }else{
                        setBtnPress(tag_cm_menber);
                        operator_types_menber.add(1+"");
                    }
                    break;
                //电信
                case R.id.tag_cn_menber:
                    if(operator_types_menber.contains(3+"")){
                        setBtnNormal(tag_cn_menber);
                        operator_types_menber.remove(3+"");
                    }else{
                        setBtnPress(tag_cn_menber);
                        operator_types_menber.add(3+"");
                    }
                    break;
                case R.id.buy_willing_one:
                    if(willBuys.contains(1+"")){
                        setNormalBg(buy_willing_one);
                        willBuys.remove(1+"");
                    }else{
                        setPressBg(buy_willing_one);
                        willBuys.add(1+"");
                    }
                break;
                case R.id.buy_willing_two:
                    if(willBuys.contains(2+"")){
                        setNormalBg(buy_willing_two);
                        willBuys.remove(2+"");
                    }else{
                        setPressBg(buy_willing_two);
                        willBuys.add(2+"");
                    }
                    break;
                case R.id.buy_willing_three:
                    if(willBuys.contains(3+"")){
                        setNormalBg(buy_willing_three);
                        willBuys.remove(3+"");
                    }else{
                        setPressBg(buy_willing_three);
                        willBuys.add(3+"");
                    }
                    break;
                //联通
                case R.id.tag_cu_fans:
                    if(operator_types_fans.contains(2+"")){
                        setBtnNormal(tag_cu_fans);
                        operator_types_fans.remove(2+"");
                    }else{
                        setBtnPress(tag_cu_fans);
                        operator_types_fans.add(2+"");
                    }
                    break;
                //移动
                case R.id.tag_cm_fans:
                    if(operator_types_fans.contains(1+"")){
                        setBtnNormal(tag_cm_fans);
                        operator_types_fans.remove(1+"");
                    }else{
                        setBtnPress(tag_cm_fans);
                        operator_types_fans.add(1+"");
                    }
                    break;
                //电信
                case R.id.tag_cn_fans:
                    if(operator_types_fans.contains(3+"")){
                        setBtnNormal(tag_cn_fans);
                        operator_types_fans.remove(3+"");
                    }else{
                        setBtnPress(tag_cn_fans);
                        operator_types_fans.add(3+"");
                    }
                    break;
            }
    }

    /**
     * 为会员时  点击保存
     */
    private void filterMenber() {

        final FilterMenberRequest filterMenberRequest = new FilterMenberRequest(gender_menber, chooseMenbersGroups,
                operator_types_menber);
        filterMenberRequest.start();
        filterMenberRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<MenberInfoModel> menberInfoModelList = filterMenberRequest.getMenberInfoModelList();
                if(!menberInfoModelList.isEmpty()&&parameCallBack2!=null){
                    parameCallBack2.onCall(menberInfoModelList);
                }
                finish();

            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    /**
     * 为粉丝时 点击保存
     */
    private void filterFans() {
        final FilterFansRequest filterFansRequest = new FilterFansRequest(gender_fans, chooseFansGroups, willBuys,
                operator_types_fans);
        filterFansRequest.start();
        filterFansRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                List<FansInfo> fansInfoList = filterFansRequest.getFansInfoList();
                if(!fansInfoList.isEmpty()&&parameCallBack1!=null){
                    parameCallBack1.onCall(fansInfoList);
                }
                finish();
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private void setBtnNormal(TextView v) {
        v.setBackgroundResource(R
                .drawable.bg_tagnormal_shape);
        v.setTextColor(Color.parseColor("#000a14"));
    }

    private void setBtnPress(TextView v) {
        v.setBackgroundResource(R
                .drawable.bg_tagpress_shape);
        v.setTextColor(Color.parseColor("#ffffff"));
    }

    private void setNormalBg(View view){
        view.setBackgroundResource(R
                .drawable.bg_tagnormal_shape);
    }

    private void setPressBg(View view){
        view.setBackgroundResource(R
                .drawable.bg_tagpress_shape);
    }

    public static void start(Activity curAct, ParameCallBack _parameCallBack1, ParameCallBack _parameCallBack2) {
        parameCallBack1 = _parameCallBack1;
        parameCallBack2 = _parameCallBack2;
        Intent intent = new Intent(curAct, FilterPeopleAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    public static void startForResult(Activity curAct){
        Intent intent = new Intent(curAct, FilterPeopleAct.class);
        ViewUtils.startActivityForResult(intent,curAct,100);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId()==R.id.rb_men_menber&&isChecked){
            gender_menber=1;
        }
        if(buttonView.getId()==R.id.rb_woman_menber&&isChecked){
            gender_menber=2;
        }
        if(buttonView.getId()==R.id.rb_men_fans&&isChecked){
            gender_fans=1;
        }
        if(buttonView.getId()==R.id.rb_woman_fans&&isChecked){
            gender_fans=2;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        parameCallBack1 = null;
        parameCallBack2 = null;
    }
}
