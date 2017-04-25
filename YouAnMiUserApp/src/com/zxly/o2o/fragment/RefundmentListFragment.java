package com.zxly.o2o.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.adapter.RefundmentAdapter;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.RefundmentDetail;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.pullrefresh.PullToRefreshBase;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.RefundmentListRequest;
import com.zxly.o2o.view.MyFlipperView;

import java.util.List;

/**
 * Created by benjamin on 2015/5/20.
 */
public class RefundmentListFragment extends BaseListViewFragment
        implements PullToRefreshBase.OnRefreshListener {
    private int fragmentPage = 1;

    public void setFragmentPage(int fragmentPage) {
        this.fragmentPage = fragmentPage;
    }

    private void setFlipper() {
        if (viewContainer == null) {
            viewContainer = (MyFlipperView) findViewById(R.id.list_layout);
            viewContainer.getRetryBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    viewContainer.setDisplayedChild(0, true);
                    loadData();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isTokenInvaild) {
            isTokenInvaild = false;
            setFlipper();
            mMainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            }, 1000);

        }
    }

    public void doUpdate(RefundmentDetail clickItem, long ismodify, int isCancelApply) {
        if (ismodify > 0) {
            switch (fragmentPage) {
                case 1:
                    for (int i = 0; i < objectAdapter.getContent().size(); i++) {
                        if (((RefundmentDetail) objectAdapter.getContent().get(i))
                                .getId() == clickItem.getId()) {
                            ((RefundmentDetail)objectAdapter.getContent().get(i)).setRefundPrice(ismodify);
                            objectAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < objectAdapter.getContent().size(); i++) {
                        if (((RefundmentDetail) objectAdapter.getContent().get(i))
                                .getId() == clickItem.getId()) {
                            ((RefundmentDetail)objectAdapter.getContent().get(i)).setRefundPrice(
                                    ismodify);
                            objectAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                    break;
                case 3:

                    break;
            }
        }

        switch (isCancelApply) {
            case 1:  //申请取消
                switch (fragmentPage) {
                    case 1:

                        break;
                    case 2:
                        objectAdapterRemoveItem(
                                clickItem);
                        break;
                    case 3:
                        if(objectAdapter.getContent().size()==0){
                            viewContainer.setDisplayedChild(3);
                        }
                        objectAdapter.addItem2Head(
                                clickItem,
                                true);
                        break;
                }
                break;
            case 2:  //退款删除
                objectAdapterRemoveItem(
                        clickItem);
                break;
        }
    }


    private void objectAdapterRemoveItem(RefundmentDetail o) {
        for (int i = 0; i < objectAdapter.getContent().size(); i++) {
            if (((RefundmentDetail) objectAdapter.getContent().get(i)).getId() == o.getId()) {
                objectAdapter.getContent().remove(i);
                objectAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    protected void loadData() {
//       new getOnlineOne();
        myRequest = new RefundmentListRequest(Account.user.getId(), fragmentPage - 1, page);
        myRequest.setOnResponseStateListener(responseStateListener);
        myRequest.start(this);
    }

    @Override
    protected void initListView(Bundle bundle) {
        objectAdapter = new RefundmentAdapter(content.getContext());
        mListView.setDivideHeight((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, Config.displayMetrics));
    }


    private BaseRequest.ResponseStateListener responseStateListener = new BaseRequest.ResponseStateListener() {

        @Override
        public void onOK() {
            isTokenInvaild = false;

            List<RefundmentDetail> refundmentList = ((RefundmentListRequest) myRequest).refundmentList;
            if (refundmentList == null || refundmentList.size() == 0) {
                if (page == 1) {
                    viewContainer.setDisplayedChild(2, false);
                }
                isLastPage = true;
            } else {
                if (page == 1) {
                    viewContainer.setDisplayedChild(3, false);
                    objectAdapter.clear();
                    objectAdapter.addItem2Head(refundmentList, true);
                } else {
                    objectAdapter.addItem(refundmentList, true);
                }
            }

            mListView.onRefreshComplete();
        }

        @Override
        public void onFail(int code) {
            if (code == 20101) {
                isTokenInvaild = false;
            }
            mListView.onRefreshComplete();
            viewContainer.setDisplayedChild(1, true);
        }
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

}
