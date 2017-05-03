package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.AddPeopleInfo;
import com.zxly.o2o.model.MenberGroupModel;
import com.zxly.o2o.model.MenberInfoModel;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CreateNewGroupRequest;
import com.zxly.o2o.request.GetAllMenbersRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.NoDoubleClickListener;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.LoadingView;
import com.zxly.o2o.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hejun on 2016/8/30.
 * 添加会员组成员页面
 */
public class AddPeopleToGroupAct extends BasicAct {

    private static ParameCallBack _callBack;
    private ListView listView;
    private SideBar sidebar;
    private List<AddPeopleInfo> peopleList=new ArrayList<AddPeopleInfo>();
    private TextView txt_title;
    private TextView btn_title_right;
    private List<String> userList;
    private List<MenberInfoModel> chooseList;
    private CheckBox img_check;
    private MyAdapter myAdapter;
    private Dialog dialog;
    private String title;
    private EditText edit_search;
    private List<MenberInfoModel> menberInfoModelList;
    private LinearLayout head;
    private LoadingView loadingview;
    private TextView floating_header;
    private List<MenberInfoModel> chooseMenberInfoModelList=new ArrayList<MenberInfoModel>();
    private boolean changeImage;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addpeople_togroup);
        findAndInitViews();
        loadData();
    }

    private void findAndInitViews() {
        chooseList=new ArrayList<MenberInfoModel>();
        findViewById(R.id.btn_back).setOnClickListener(noDoubleClickListener);
        txt_title = (TextView) findViewById(R.id.txt_title);
        if(getIntent()!=null){
            title = getIntent().getStringExtra("title");
        }
        txt_title.setText(title);
        edit_search = (EditText) findViewById(R.id.edit_search);
        edit_search.setHint("请输入会员昵称/备注名/电话号码搜索");
        btn_title_right = (TextView) findViewById(R.id.btn_title_right);
        btn_title_right.setVisibility(View.VISIBLE);
        btn_title_right.setText("保存");
        btn_title_right.setOnClickListener(noDoubleClickListener);
        loadingview = (LoadingView)findViewById(R.id.view_loading);
        floating_header = (TextView) findViewById(R.id.floating_header);
        sidebar = (SideBar) findViewById(R.id.sidebar);
//        sidebar.setTextView(floating_header);
        listView= (ListView) findViewById(R.id.list_view);
        //listview头部
        head = (LinearLayout) LayoutInflater.from(AddPeopleToGroupAct.this).inflate(R.layout.head_addall,null,false);
        head.findViewById(R.id.layout_addall).setOnClickListener(noDoubleClickListener);
        img_check = (CheckBox) head.findViewById(R.id.img_check);
        listView.addHeaderView(head);
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==0&&myAdapter.getCount()==0){
                    loadingview.onLoadingComplete();
                    head.setVisibility(View.VISIBLE);
                }
                if(TextUtils.isEmpty(s.toString())&&menberInfoModelList.size()!=chooseList.size()&&menberInfoModelList.size()!=0){
                    if(img_check.isChecked()){
                        changeImage=true;
                        img_check.setChecked(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String conten = s.toString().trim();
                if (!TextUtils.isEmpty(conten)) {
                    Pattern p = Pattern.compile("[0-9]*");
                    Matcher m = p.matcher(conten);
                    if(m.matches() ){
                        //输入的为数字
                        List<MenberInfoModel> fansInfos = filterNumberData(conten);
                        if(fansInfos.size()==0){
                            loadingview.onDataEmpty("无结果",R.drawable.img_default_tired);
                            head.setVisibility(View.INVISIBLE);
                            floating_header.setVisibility(View.INVISIBLE);
                        }else if(fansInfos.size()>0){
                            loadingview.onLoadingComplete();
                            head.setVisibility(View.VISIBLE);
                        }else {
                            head.setVisibility(View.VISIBLE);
                            loadingview.onLoadingComplete();
                        }
                        if(fansInfos.size()!=chooseList.size()&&fansInfos.size()!=0){
                            if(img_check.isChecked()){
                                changeImage=true;
                                img_check.setChecked(false);
                            }
                        }
                        myAdapter.clear();
                        myAdapter.addItem(fansInfos);
                        chooseMenberInfoModelList.clear();
                        chooseMenberInfoModelList.addAll(fansInfos);

                    }else {
                        //输入的为其他
                        List<MenberInfoModel> fansInfos = filterNameData(conten);
                        if(fansInfos.size()==0){
                            loadingview.onDataEmpty("无结果",R.drawable.img_default_tired);
                            head.setVisibility(View.INVISIBLE);
                            floating_header.setVisibility(View.INVISIBLE);
                        }else if(fansInfos.size()>0){
                            loadingview.onLoadingComplete();
                            head.setVisibility(View.VISIBLE);
                        }else {
                            head.setVisibility(View.VISIBLE);
                            loadingview.onLoadingComplete();
                        }
                        if(fansInfos.size()!=chooseList.size()&&fansInfos.size()!=0){
                            if(img_check.isChecked()){
                                changeImage=true;
                                img_check.setChecked(false);
                            }
                        }
                        myAdapter.clear();
                        myAdapter.addItem(fansInfos);
                        chooseMenberInfoModelList.clear();
                        chooseMenberInfoModelList.addAll(fansInfos);
                    }
                }else {
                    myAdapter.clear();
                    myAdapter.addItem(menberInfoModelList);
                    chooseMenberInfoModelList.clear();
                    chooseMenberInfoModelList.addAll(menberInfoModelList);
                }
                myAdapter.notifyDataSetChanged();
            }
        });
        img_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    changeImage=false;
                    for (int i = 0; i < chooseMenberInfoModelList.size(); i++) {
                        if(!chooseList.contains(chooseMenberInfoModelList.get(i))){
                            chooseList.add(chooseMenberInfoModelList.get(i));
                        }
                    }
                }else{
                    if(!changeImage){
                        for (int i = 0; i < chooseMenberInfoModelList.size(); i++) {
                            if(chooseList.contains(chooseMenberInfoModelList.get(i))){
                                chooseList.remove(chooseMenberInfoModelList.get(i));
                            }
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
        });

        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = myAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });
    }

    private List<MenberInfoModel> filterNameData(String conten) {
        List<MenberInfoModel> filterData = new ArrayList<MenberInfoModel>();
        if(menberInfoModelList!=null){
            for (int i = 0; i < menberInfoModelList.size(); i++) {
                if (menberInfoModelList.get(i).getRemarkName().contains(conten)||menberInfoModelList.get(i).getNickname().contains(conten)||menberInfoModelList.get(i).getUserName().contains(conten)) {
                    filterData.add(menberInfoModelList.get(i));
                }
            }
        }
        return filterData;
    }

    private List<MenberInfoModel> filterNumberData(String conten) {
        List<MenberInfoModel> filterData = new ArrayList<MenberInfoModel>();
        if(menberInfoModelList!=null){
            for (int i = 0; i < menberInfoModelList.size(); i++) {
                if (!TextUtils.isEmpty(menberInfoModelList.get(i).getMobilePhone())&&menberInfoModelList.get(i).getMobilePhone().contains(conten)) {
                    filterData.add(menberInfoModelList.get(i));
                }
            }
        }
        return filterData;
    }


    private void loadData() {
        final GetAllMenbersRequest getAllMenbersRequest = new GetAllMenbersRequest();
        getAllMenbersRequest.start();
        getAllMenbersRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                menberInfoModelList = getAllMenbersRequest.getMenberInfoModelList();
                chooseMenberInfoModelList.clear();
                chooseMenberInfoModelList.addAll(menberInfoModelList);
                if(menberInfoModelList.isEmpty()){
                    head.setVisibility(View.GONE);
                }else {
//                    sortList(menberInfoModelList);
                    myAdapter.addItem(menberInfoModelList,true);
//                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(int code) {
                loadingview.onDataEmpty("加载失败");
            }
        });
    }

    private void sortList(List<MenberInfoModel> menberInfoModelList) {
//        Collator.getInstance(java.util.Locale.CHINA);
        Collections.sort(menberInfoModelList, new Comparator<MenberInfoModel>() {

            @Override
            public int compare(MenberInfoModel lhs, MenberInfoModel rhs) {
//                if (lhs.getNameAbbr()
//                        .equals(rhs.getNameAbbr())) {
//                    return lhs.getUserName()
//                            .compareTo(rhs.getUserName());
//                } else {
//                    if ("#".equals(lhs.getNameAbbr())) {
//                        return 1;
//                    } else if ("#".equals(rhs.getNameAbbr())) {
//                        return -1;
//                    }
//                    return lhs.getNameAbbr()
//                            .compareTo(rhs.getNameAbbr());
//                }
                if ("#".equals(lhs.getNameAbbr())) {
                        return 0;
                    } else if ("#".equals(rhs.getNameAbbr())) {
                        return 0;
                }else {
                    return lhs.getNameAbbr()
                            .compareTo(rhs.getNameAbbr());
                }
            }
        });
    }

    /**
     * 保存该会员组
     * 组员bu可为空
     */
    private void savaGroup() {
        List<Long> chooseIds=new ArrayList<Long>();
        if(chooseList.size()!=0){
            for (int i = 0; i < chooseList.size(); i++) {
                chooseIds.add(chooseList.get(i).getId());
            }
        }
        else{
            ViewUtils.showToast("请选择要添加的会员");
            return;
        }
        final CreateNewGroupRequest createNewGroupRequest = new CreateNewGroupRequest(chooseIds, title);
        createNewGroupRequest.start();
        createNewGroupRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                int id = createNewGroupRequest.getId();
                MenberGroupModel menberGroupModel = new MenberGroupModel();
                menberGroupModel.setName(title);
                menberGroupModel.setMemberCount(chooseList.size());
                menberGroupModel.setHasClick(false);
                menberGroupModel.setId(id);
                menberGroupModel.setIsCustomerGroup(1);
                if(_callBack!=null){
                    _callBack.onCall(menberGroupModel);
                }
                finish();

                UmengUtil.onEvent(AddPeopleToGroupAct.this,new UmengUtil().HOME_NEWGROUP_SAVE_SUC,null);
            }

            @Override
            public void onFail(int code) {
                    ViewUtils.showToast("新建失败，请重试");
                UmengUtil.onEvent(AddPeopleToGroupAct.this,new UmengUtil().HOME_NEWGROUP_SAVE_FAIL,null);
            }
        });

    }

    private void showConfirmDialog() {
        if(dialog == null){
            dialog = new Dialog(this, R.style.dialog);
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if(!isFinishing()){
            dialog.show();
        }
        dialog.setContentView(R.layout.dialog_save_confirm);
        ((TextView)dialog.findViewById(R.id.txt_title)).setText("\""+title+"\""+"还未保存，是否保存？");
        dialog.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 保存组 返回前一页
                savaGroup();
            }
        });

        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
    }

    public static void start(Activity curAct, String title, ParameCallBack callBack) {
        _callBack =callBack;
        Intent intent = new Intent(curAct, AddPeopleToGroupAct.class);
        intent.putExtra("title",title);
        ViewUtils.startActivity(intent, curAct);
    }

    class MyAdapter extends ObjectAdapter implements SectionIndexer {


        public MyAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_search_filterdata;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView=inflateConvertView();
                viewHolder=new ViewHolder();
                viewHolder.img_user_head= (CircleImageView) convertView.findViewById(R.id.img_user_head);
                viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_phone= (TextView) convertView.findViewById(R.id.tv_phone);
                viewHolder.img_check= (ImageView) convertView.findViewById(R.id.img_check);
                viewHolder.layout_container= (RelativeLayout) convertView.findViewById(R.id.layout_container);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final MenberInfoModel menberInfoModel = (MenberInfoModel) getItem(position);
            if(!TextUtils.isEmpty(menberInfoModel.getRemarkName())){
                viewHolder.tv_name.setText(menberInfoModel.getRemarkName());
            }else if(!TextUtils.isEmpty(menberInfoModel.getNickname())){
                viewHolder.tv_name.setText(menberInfoModel.getNickname());
            }else if(!TextUtils.isEmpty(menberInfoModel.getUserName())){
                viewHolder.tv_name.setText(menberInfoModel.getUserName());
            }
            viewHolder.img_user_head.setImageUrl(menberInfoModel.getHeadUrl(),R.drawable.default_head_big);
            viewHolder.tv_phone.setText(menberInfoModel.getMobilePhone());
            if(chooseList.contains(menberInfoModel)){
                viewHolder.img_check.setBackgroundResource(R.drawable.icon_check_press);
            }else{
                viewHolder.img_check.setBackgroundResource(R.drawable.icon_check_normal);
            }

            viewHolder.layout_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(chooseList.contains(menberInfoModel)){
                        chooseList.remove(menberInfoModel);
                    }else {
                        chooseList.add(menberInfoModel);
                    }
                    myAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }

        @Override
        public Object[] getSections() {
            return null;
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = menberInfoModelList.get(i).getNameAbbr();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == sectionIndex) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public int getSectionForPosition(int position) {
            if(menberInfoModelList.size()>0){
                return menberInfoModelList.get(position).getNameAbbr().charAt(0);
            }else{
                return 0;
            }

        }

        class ViewHolder{
            CircleImageView img_user_head;
            TextView tv_name;
            TextView tv_phone;
            ImageView img_check;
            RelativeLayout layout_container;
        }
    }

    NoDoubleClickListener noDoubleClickListener=new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()){
                case R.id.btn_back:
                    showConfirmDialog();
                    break;
                case R.id.btn_title_right:
                    savaGroup();


                    break;
                case R.id.layout_addall:

                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _callBack=null;
    }
}
