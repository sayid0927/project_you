package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.easeui.AppException;
import com.zxly.o2o.adapter.LikeAndCollectHistoyAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.User;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.pullrefresh.PullToRefreshListView;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.CommissionProductRequest;
import com.zxly.o2o.util.DataUtil;
import com.zxly.o2o.util.ViewUtils;
import com.zxly.o2o.view.LoadingView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenwu on 2015/12/8.
 */
public class LikeAndCollectHistoryAct extends BasicAct implements View.OnClickListener {

    public static int TYPE_PRODUCT=1;
    public static int TYPE_ARTICLE_PRODUCT=2;

    PullToRefreshListView mListView;
    ObjectAdapter adapter;
    DataRequest request;
    LoadingView loadingView;
    private static List<User> userList;

    private String  id;

    private int type;
    private int pageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_like_and_collect);

        id=getIntent().getStringExtra("id");
        type=getIntent().getIntExtra("type",0);

        TextView title= (TextView) findViewById(R.id.txt_title);
        title.setText(getIntent().getStringExtra("title"));

        View btnBack=findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        loadingView= (LoadingView) findViewById(R.id.view_loading);
        loadingView.startLoading();

        mListView= (PullToRefreshListView) findViewById(R.id.listview);
        ViewUtils.setRefreshText(mListView);
        mListView.setIntercept(true);
        adapter=new LikeAndCollectHistoyAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    loadData(1);
                }
                if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    // 加载上拉数据
                   loadData(pageIndex);
                }

            }
        });

        request=new DataRequest(id,type);
        request.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                reFreshUI(TYPE_LOAD_DATA_SUCCESS, pageIndex);
            }

            @Override
            public void onFail(int code) {
                reFreshUI(TYPE_LOAD_DATA_FAILD, pageIndex);
            }
        });

        loadData(1);

    }

    private void loadData(int pageIndex){
        this.pageIndex=pageIndex;
        request.setPageIndex(pageIndex);
        request.start(LikeAndCollectHistoryAct.this);
    }

    public static final int TYPE_LOAD_DATA_SUCCESS=1;
    public static final int TYPE_LOAD_DATA_FAILD=-1;
    private synchronized void reFreshUI(int type,int pageIndex){

        switch (type){

            case TYPE_LOAD_DATA_SUCCESS:
                if(pageIndex==1)
                    adapter.clear();

                if(!DataUtil.listIsNull(request.getUserList())){
                    adapter.addItem(request.getUserList());
                    request.getUserList().clear();
                    loadingView.onLoadingComplete();
                    this.pageIndex++;
                }else {
                    if(pageIndex==1){
                        loadingView.onDataEmpty();
                    }else {
                        ViewUtils.showToast("暂时没有更多了！");
                    }

                }

                mListView.onRefreshComplete();
                adapter.notifyDataSetChanged();
                break;

            case TYPE_LOAD_DATA_FAILD:
                if(adapter.getContent()==null){
                    loadingView.onLoadingFail();
                }else {
                    ViewUtils.showToast("数据加载失败,请检查你的网络！");
                }
                mListView.onRefreshComplete();
                break;

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userList=null;
    }

    public  static void start(List<User> userList,Activity curAct,String title){
        LikeAndCollectHistoryAct.userList=userList;
        if(!DataUtil.listIsNull(userList)) {
            Intent intent=new Intent(curAct,LikeAndCollectHistoryAct.class);
            intent.putExtra("title",title);
            ViewUtils.startActivity(intent, curAct);
        }
    }


    public  static void start(String id,int type,String title){
        Intent intent=new Intent(AppController.getInstance().getTopAct(),LikeAndCollectHistoryAct.class);
        intent.putExtra("id",id);
        intent.putExtra("type",type);
        intent.putExtra("title",title);
        ViewUtils.startActivity(intent, AppController.getInstance().getTopAct());
    }


    class DataRequest extends BaseRequest{

        List<User> userList=new ArrayList<User>();

        DataRequest(String id,int type){
            addParams("productId",id);
            addParams("type",type);
        }


        public void setPageIndex(int pageIndex){
            addParams("pageIndex",pageIndex);
        }

        @Override
        protected void fire(String data) throws AppException {
            try {
                JSONArray userArray=new JSONArray(data);
                int length=userArray.length();
                for(int i=0;i<length;i++) {
                    JSONObject joUser=userArray.getJSONObject(i);
                    User user=new User();
                    user.setId(joUser.optInt("userId"));
                    user.setName(joUser.optString("name"));
                    user.setOprateTime(joUser.optLong("opTime"));
                    user.setType((byte) joUser.optInt("type"));
                    user.setSignature(joUser.optString("sign"));
                    user.setThumHeadUrl(joUser.optString("imageUrl"));
                    userList.add(user);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }


        public List<User> getUserList() {
            return userList;
        }

        public void setUserList(List<User> userList) {
            this.userList = userList;
        }

        @Override
        protected String method() {
            return "product/record";
        }
    }

}

