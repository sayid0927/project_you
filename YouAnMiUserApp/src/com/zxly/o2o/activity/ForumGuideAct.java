package com.zxly.o2o.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.CircleForumListAdapter;
import com.zxly.o2o.adapter.ObjectAdapter;
import com.zxly.o2o.fragment.CircleMainPageFragment;
import com.zxly.o2o.model.CircleForumVO;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ForumGuideRequest;
import com.zxly.o2o.request.ForumToAllConcernsRequest;
import com.zxly.o2o.util.PreferUtil;
import com.zxly.o2o.view.MyFlipperView;

import java.util.List;

/**
 * Created by Administrator on 2015/12/14.
 */
public class ForumGuideAct extends BasicAct {
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListenner();
    private ForumGuideRequest forumGuideRequest;
    private ListView mListView;
    private CircleForumListAdapter objectAdapter;
    private BDLocation mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_guide_layout);
        mListView = (ListView) findViewById(R.id.guide_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((CircleForumVO) objectAdapter.getItem(position)).setIsCheck(!((CircleForumVO) objectAdapter
                        .getItem(position)).isCheck());
                objectAdapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        objectAdapter = new CircleForumListAdapter(ForumGuideAct.this, -1, null);
        mListView.setAdapter(objectAdapter);

        //获取位置坐标信息
        getLocationMessage();

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 是否打开GPS
        option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        option.setIsNeedAddress(true);
        // option.setProdName("LocationDemo"); //
        // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        // option.setScanSpan(UPDATE_TIME);// 设置定时定位的时间间隔。单位毫秒
        option.setScanSpan(200000);
        mLocationClient.setLocOption(option);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    public void getLocationMessage() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();
    }

    protected void loadData() {
        if (forumGuideRequest == null) {
            forumGuideRequest = new ForumGuideRequest(mLocation.getCity());
            forumGuideRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    if (forumGuideRequest
                            .getObjects() == null || forumGuideRequest
                            .getObjects().size() == 0) {
                    } else {
                        objectAdapter.addItem(forumGuideRequest
                                .getObjects(), true);
                        findViewById(R.id.btn_attention).setClickable(true);

                        findViewById(R.id.btn_attention).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StringBuffer stringBuffer = new StringBuffer("");
                                for (int i = 0; i < objectAdapter.getCount() - 1; i++) {
                                    stringBuffer.append(((CircleForumVO) objectAdapter.getItem(i)).getId());
                                    stringBuffer.append(",");
                                }
                                stringBuffer
                                        .append(((CircleForumVO) objectAdapter
                                                .getItem(objectAdapter.getCount())).getId());

                                getConcerListRequest(stringBuffer.toString());

                            }
                        });
                    }
                }

                @Override
                public void onFail(int code) {

                }
            });
            forumGuideRequest.start();
        }
    }

    private void getConcerListRequest(String stringBuffer) {
        ForumToAllConcernsRequest forumToAllConcernsRequest = new ForumToAllConcernsRequest(stringBuffer
                );
        forumToAllConcernsRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                CircleMainPageFragment.isReLoad = true;
                if (Account.user != null) {
                    finish();
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
        forumToAllConcernsRequest.start();
        CircleMainPageFragment.isReLoad = true;

    }


    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location != null && location.getCity() != null) {
                PreferUtil.getInstance().setForumGuideIsShow(true);
                mLocation = location;
                loadData();

                Log.e("map", "addr:" + location.getAddrStr());
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
            }
        }
    }
}
