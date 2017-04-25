package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zxly.o2o.activity.MyCommisionDetailAct;
import com.zxly.o2o.adapter.CommisionAdapter;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.MyCommissionVO;
import com.zxly.o2o.model.OrderCommVO;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.request.BaseRequest.ResponseStateListener;
import com.zxly.o2o.request.MyCommissionListRequest;
import com.zxly.o2o.util.StringUtil;

public class MyCommisionFragment extends BaseListViewFragment
        implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener {
    private int fragmentPage = 1;
    private TextView headView;

    public static MyCommisionFragment newInstance(int param1) {
        MyCommisionFragment fragment = new MyCommisionFragment();
        Bundle args = new Bundle();
        args.putInt("fragmentPage", param1);
        fragment.setArguments(args);
        return fragment;
    }

    protected void loadData() {
        myRequest = new MyCommissionListRequest(page, fragmentPage);
        myRequest.setOnResponseStateListener(new ResponseStateListener() {
            @Override
            public void onOK() {
                MyCommissionVO myCommissionVO = ((MyCommissionListRequest) myRequest).myCommissionVO;
                if (page == 1) {

                    // 添加headView的点击事件
                    if (headView == null) {
                        if(getActivity()==null)
                            return ;
                        headView = (TextView) getActivity().getLayoutInflater()
                                .inflate(android.R.layout.simple_spinner_item, null);
                        headView.setBackgroundResource(R.drawable.my_commision_banner);
                        headView.setTextColor(getResources().getColor(R.color.white));
                        headView.setGravity(Gravity.CENTER_VERTICAL);
                        if (fragmentPage == 1) {
                            StringUtil.setTextSpan(9,
                                    9 + myCommissionVO.getTotalCommission().toString().length(), 28,
                                    0,
                                    Html.fromHtml(String.format(
                                            getString(R.string.commision_list_head_banner),
                                            myCommissionVO.getTotalCommission().toString())),
                                    headView,
                                    AppController.displayMetrics);
                        } else {
                            StringUtil.setTextSpan(10,
                                    10 + myCommissionVO.getTotalCommission().toString().length(), 28,
                                    0,
                                    Html.fromHtml(String.format(
                                            getString(R.string.commision_list_head_banner2),
                                            myCommissionVO.getTotalCommission().toString())),
                                    headView,
                                    AppController.displayMetrics);
                        }

                        mListView.addH(headView);
                        mListView.setDivideHeight((int) TypedValue
                                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                                        AppController.displayMetrics));
                        viewContainer.setDisplayedChild(3, true);
                    } else {
                        viewContainer.setDisplayedChild(3, false);
                    }
                    objectAdapter.clear();
                }
                objectAdapter.addItem(myCommissionVO.getOrderComms(), true);
                mListView.onRefreshComplete();
            }

            @Override
            public void onFail(int code) {
                if(getActivity()==null)
                    return ;
                viewContainer.setDisplayedChild(2, true);
                mListView.onRefreshComplete();
            }
        });
        myRequest.start(MyCommisionFragment.this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initListView(Bundle bundle) {
        fragmentPage = bundle.getInt("fragmentPage");

        objectAdapter = new CommisionAdapter(getActivity());
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position>1)
        {
            MyCommisionDetailAct.start(getActivity(),((OrderCommVO) objectAdapter.getContent().get(position-2 )));
        }

    }

}
