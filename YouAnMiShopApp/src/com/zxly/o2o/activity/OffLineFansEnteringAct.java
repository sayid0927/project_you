package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.model.FansInfo;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.FansFocusRequest;
import com.zxly.o2o.request.GetFansInfoRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ParameCallBack;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.CircleImageView;
import com.zxly.o2o.view.LoadingView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dsnx on 2016/9/27.
 */
public class OffLineFansEnteringAct extends BasicAct implements PullToRefreshBase.OnRefreshListener ,View.OnClickListener {

    private static ParameCallBack _callBack;
    private static CallBack _addNumCallBck;
    private ImageView btnBack;
    private PullToRefreshListView mListView;
    private LoadingView loadingView;
    private OffLineFansAdapter adapter;
    private View btnSearch,btnAddFans;
    private EditText edit_search;
    private List<FansInfo> fansInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_off_line_fans_entering);
        btnBack= (ImageView) findViewById(R.id.btn_back);
        loadingView=(LoadingView) findViewById(R.id.view_loading11);
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        edit_search = (EditText) findViewById(R.id.edit_search);
        edit_search.setHint("请输入姓名或手机号");
        btnAddFans=findViewById(R.id.btn_add_fans);
        ViewUtils.setRefreshText(mListView);
        mListView.setDivideHeight(0);
        mListView.setOnRefreshListener(this);
        adapter=new OffLineFansAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setIntercept(true);
        btnBack.setOnClickListener(this);
        btnAddFans.setOnClickListener(this);
        loadData(false);

        edit_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if(arg3==0&&adapter.getCount()==0){
                    loadingView.onLoadingComplete();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable edit) {
                String conten = edit.toString().trim();

                if (!TextUtils.isEmpty(conten)) {
                    Pattern p = Pattern.compile("[0-9]*");
                    Matcher m = p.matcher(conten);
                    if(m.matches() ){
                        //输入的为数字
                        List<FansInfo> fansInfos = filterNumberData(conten);
                        if(fansInfos.size()==0){
                            loadingView.onDataEmpty("没有搜到相关内容",R.drawable.img_default_sad);
                        }else {
                            loadingView.onLoadingComplete();
                        }
                        adapter.clear();
                        adapter.addItem(fansInfos);

                    }else {
                        //输入的为其他
                        List<FansInfo> fansInfos = filterNameData(conten);
                        if(fansInfos.size()==0){
                            loadingView.onDataEmpty("没有搜到相关内容",R.drawable.img_default_sad);
                        }else {
                            loadingView.onLoadingComplete();
                        }
                        adapter.clear();
                        adapter.addItem(fansInfos);
                    }
                }else {
                    adapter.clear();
                    adapter.addItem(fansInfoList);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 根据电话号码模糊搜索
     * @param content
     * @return
     */
    private List<FansInfo> filterNumberData(String content) {
        List<FansInfo> filterData = new ArrayList<FansInfo>();
        if(fansInfoList!=null){
            for (int i = 0; i < fansInfoList.size(); i++) {
                if (!TextUtils.isEmpty(fansInfoList.get(i).getPhone())&&fansInfoList.get(i).getPhone().contains(content)) {
                    filterData.add(fansInfoList.get(i));
                }
            }
        }
        return filterData;
    }

    /**
     * 根据姓名搜索
     * @param content
     * @return
     */
    private List<FansInfo> filterNameData(String content) {
        List<FansInfo> filterData = new ArrayList<FansInfo>();
        if(fansInfoList!=null){
            for (int i = 0; i < fansInfoList.size(); i++) {
                if (!TextUtils.isEmpty(fansInfoList.get(i).getName())&&fansInfoList.get(i).getName().contains(content)) {
                    filterData.add(fansInfoList.get(i));
                }
            }
        }
        return filterData;
    }

    public static void start(Activity act,ParameCallBack callBack,CallBack addNumCallBck)
    {
        _callBack =callBack;
        _addNumCallBck =addNumCallBck;
        Intent intent=new Intent();
        intent.setClass(act, OffLineFansEnteringAct.class);
        ViewUtils.startActivity(intent, act);
    }
    private void loadData(final boolean isRefresh)
    {
        final GetFansInfoRequest request=new GetFansInfoRequest("线下录入粉丝",true);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                fansInfoList = request.getFansInfoList();
                mListView.onRefreshComplete();
                if(!DataUtil.listIsNull(request.getFansInfoList()))
                {
                    loadingView.onLoadingComplete();
                    if(isRefresh) adapter.clear();
                    adapter.addItem(request.getFansInfoList(),true);
                }else
                {
                    loadingView.onDataEmpty("把线下客户也记录进来，统一查看，更方便哦",true,R.drawable.img_default_happy);
                    loadingView.setBtnText("去新增");
                    findViewById(R.id.layout_search).setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFail(int code) {
                loadingView.onLoadingFail();
                mListView.onRefreshComplete();
            }
        });
        request.start(this);
        loadingView.startLoading();

        loadingView.setOnAgainListener(new LoadingView.OnAgainListener() {
            @Override
            public void onLoading() {
                FansAddAct.startForResult(OffLineFansEnteringAct.this,callBack);
            }
        });
    }
    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
            loadData(true);
        }
        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            // 加载上拉数据
            mMainHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mListView.onRefreshComplete();
                }
            }, 500);
        }
    }

    @Override
    public void onClick(View v) {
        if(v==btnBack)
        {
            finish();
        }else if(v==btnSearch)
        {
            SeachPeopleFilterFirstAct.start(OffLineFansEnteringAct.this,true);
        }else  if(v==btnAddFans)
        {
            FansAddAct.startForResult(OffLineFansEnteringAct.this,callBack);
            UmengUtil.onEvent(OffLineFansEnteringAct.this,new UmengUtil().FANS_ADDFANS_CLICK,null);
        }
    }

    ParameCallBack callBack=new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            FansInfo childInfo= (FansInfo) object;
            if(adapter.getCount()==0){
                loadingView.setVisibility(View.GONE);
                findViewById(R.id.layout_search).setVisibility(View.VISIBLE);
            }
            adapter.addItem2Head(childInfo,true);
            //回调粉丝首页线下分组  数目加一回调
            if(_addNumCallBck!=null){
                _addNumCallBck.onCall();
            }
        }
    };

    class OffLineFansAdapter extends ObjectAdapter
    {

        public OffLineFansAdapter(Context _context) {
            super(_context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_fans_child;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null)
            {
                holder=new ViewHolder();
                convertView= LayoutInflater.from(context).inflate(getLayoutId(),parent,false);
                holder.img_notice= (ImageView) convertView.findViewById(R.id.img_notice);
                holder.img_user_head= (CircleImageView) convertView.findViewById(R.id.img_user_head);
                holder.tv_imei= (TextView) convertView.findViewById(R.id.tv_imei);
                holder.fans_name= (TextView) convertView.findViewById(R.id.fans_name);
                holder.layout_willingbuy= (LinearLayout) convertView.findViewById(R.id.layout_willingbuy);
                holder.star_first= (ImageView) convertView.findViewById(R.id.star_first);
                holder.star_second= (ImageView) convertView.findViewById(R.id.star_second);
                holder.star_third= (ImageView) convertView.findViewById(R.id.star_third);
                holder.txt_phone_brand= (TextView) convertView.findViewById(R.id.txt_phone_brand);
                holder.img_guanzhu= (ImageView) convertView.findViewById(R.id.img_guanzhu);
                holder.top_line=convertView.findViewById(R.id.top_line);
                holder.bottom_line=convertView.findViewById(R.id.bottom_line);
                holder.layout_focus= (LinearLayout) convertView.findViewById(R.id.layout_focus);
                convertView.setTag(holder);

            }else
            {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.img_notice.setVisibility(View.GONE);
            holder.top_line.setVisibility(View.GONE);
            final FansInfo childInfo= (FansInfo) getItem(position);
            if(position==getCount()-1){
                holder.bottom_line.setVisibility(View.GONE);
            }else {
                holder.bottom_line.setVisibility(View.VISIBLE);
            }
            //粉丝条目点击事件
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FansDetailNewAct.startResult(OffLineFansEnteringAct.this,Long.parseLong(String.valueOf(childInfo.getFansId())),2,updateCallBack);

                }
            });
            if(!TextUtils.isEmpty(childInfo.getName())){
                holder.tv_imei.setVisibility(View.GONE);
                holder.fans_name.setVisibility(View.VISIBLE);
                holder.fans_name.setText(childInfo.getName());
                int buyIntention=childInfo.getBuyIntention();
                if(buyIntention>0)
                {
                    holder.layout_willingbuy.setVisibility(View.VISIBLE);
                    switch (buyIntention)
                    {
                        case 1:
                            holder.star_first.setBackgroundResource(R.drawable.icon_lightstar);
                            holder.star_second.setBackgroundResource(R.drawable.icon_darkstar);
                            holder.star_third.setBackgroundResource(R.drawable.icon_darkstar);
                            break;
                        case 2:
                            holder.star_first.setBackgroundResource(R.drawable.icon_lightstar);
                            holder.star_second.setBackgroundResource(R.drawable.icon_lightstar);
                            holder.star_third.setBackgroundResource(R.drawable.icon_darkstar);
                            break;
                        case 3:
                            holder.star_first.setBackgroundResource(R.drawable.icon_lightstar);
                            holder.star_second.setBackgroundResource(R.drawable.icon_lightstar);
                            holder.star_third.setBackgroundResource(R.drawable.icon_lightstar);
                            break;
                    }
                }else
                {
                    holder.layout_willingbuy.setVisibility(View.GONE);
                }
            }else {
                holder.layout_willingbuy.setVisibility(View.GONE);
                holder.tv_imei.setVisibility(View.VISIBLE);
                holder.fans_name.setVisibility(View.GONE);
                holder.tv_imei.setText(childInfo.getImei());
            }
            //粉丝手机型号
            if(!TextUtils.isEmpty(childInfo.getPhoneModel())){
                holder.txt_phone_brand.setText(childInfo.getPhoneModel());
            }else{
                holder.txt_phone_brand.setText("");
            }
            //是否关注
            holder.img_guanzhu.setBackgroundResource(childInfo.getIsFocus()==0?R.drawable.icon_cancle_look:R.drawable.icon_has_look);
            holder.layout_focus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int isFocus = childInfo.getIsFocus();
                    childInfo.setIsFocus(isFocus==0?1:0);
                    FansFocusRequest fansFocusRequest = new FansFocusRequest(isFocus == 0 ? false : true, childInfo
                            .getFansId());

                    fansFocusRequest.start();
                    fansFocusRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                        @Override
                        public void onOK() {
                            updateSingleRow(mListView.getRefreshableView(),childInfo);
                            if(isFocus==0){
                                ViewUtils.showToast("关注成功");


                            }else {
                                ViewUtils.showToast("取消关注成功");


                            }
                            _callBack.onCall(childInfo);
                        }

                        @Override
                        public void onFail(int code) {

                        }
                    });
                }
            });
            return convertView;
        }
    }


    class ViewHolder{
        ImageView img_notice;
        CircleImageView img_user_head;
        TextView tv_imei;
        TextView fans_name;
        LinearLayout layout_willingbuy;
        ImageView star_first,star_second,star_third;
        TextView txt_phone_brand;
        ImageView img_guanzhu;
        View top_line,bottom_line;
        LinearLayout layout_focus;
    }

    ParameCallBack updateCallBack=new ParameCallBack() {
        @Override
        public void onCall(Object object) {
            FansInfo childInfo= (FansInfo) object;
            List<Object> content = adapter.getContent();
            for (int i = 0; i < content.size(); i++) {
                if(((FansInfo)content.get(i)).getFansId()==childInfo.getFansId()){
                    ((FansInfo)content.get(i)).setName(childInfo.getRemarkName());
                    ((FansInfo)content.get(i)).setBuyIntention(childInfo.getBuyIntention());
                    ((FansInfo)content.get(i)).setPhoneModel(childInfo.getPhoneModel());
                }
            }
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _addNumCallBck=null;
        _callBack=null;
    }
}
