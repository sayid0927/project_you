package com.zxly.o2o.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.easemob.easeui.AppException;
import com.easemob.easeui.widget.viewpagerindicator.PagerSlidingTabStrip;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.view.LoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2016/9/8.
 */
public class TerraceArticleFragement extends  BaseViewPageFragment {
    private LoadingView loadingView;
    private String[] tabName;
    private String[] articleTypeId;
    @Override
    protected void initView() {
        if(tabName==null||tabName.length<=0)
        {
            if (loadingView==null)
            {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                loadingView=new LoadingView(getActivity());
                content.addView(loadingView,layoutParams);
            }
        }else
        {
            initTab();
        }

    }
    private void initTab()
    {
        super.initView();
        tabs.setBackgroundColor(Color.parseColor("#f3f4f6"));
        tabs.setTabBackground(com.easemob.chatuidemo.R.drawable.background_tab);
        setTabsValue(tabs);
    }
    public  void setTabsValue(PagerSlidingTabStrip tabs) {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线颜色
        tabs.setDividerColor(Color.parseColor("#d9d9d9"));
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, AppController.displayMetrics));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, AppController.displayMetrics));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, AppController.displayMetrics));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(Color.parseColor("#626262"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(Color.parseColor("#ff5f19"));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(R.color.transparent);
        tabs.initTabStyles();
    }

    @Override
    protected void loadInitData() {
        if(tabName==null||tabName.length<=0)
        {
            loadingView.startLoading();
            new GetArticelTypeRequest().start();
        }
    }

    @Override
    protected String[] tabName() {
        return tabName;
    }

    @Override
    protected List<Fragment> fragments() {
        List<Fragment> fragmentList=new ArrayList<Fragment>();
        for(int i=0;i<articleTypeId.length;i++)
        {
            fragmentList.add(StoreArticleFragement.newInstance(2,articleTypeId[i]));
        }

        return fragmentList;
    }
    class GetArticelTypeRequest extends BaseRequest{

        public  GetArticelTypeRequest()
        {
            this.setOnResponseStateListener(new ResponseStateListener() {
                @Override
                public void onOK() {
                    loadingView.onLoadingComplete();
                    if(tabName!=null&&tabName.length>0)
                    {
                        initTab();
                    }

                }

                @Override
                public void onFail(int code) {
                    loadingView.onLoadingFail();
                }
            });
        }
        @Override
        protected String method() {
            return "/keduoduo/promote/articleTypes";
        }
        @Override
        protected void fire(String data) throws AppException {

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(data);
                int length = jsonArray.length();
                tabName=new String[length];
                articleTypeId=new String[length];
                for (int i = 0; i < length; i++) {
                    JSONObject atJson = jsonArray.getJSONObject(i);
                    tabName[i]=atJson.optString("codeName");
                    articleTypeId[i]=atJson.optString("id");
                }
            } catch (JSONException e) {
                throw JSONException(e);
            }

        }

    }
}
