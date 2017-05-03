package com.zxly.o2o.fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.PromotionArticleAct;
import com.zxly.o2o.adapter.BenefitsListAdapter;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyBenefitsRequest;
import com.zxly.o2o.util.ViewUtils;

/**
 * Created by Administrator on 2016/6/12.
 */
public class MyBenefitsFragment extends BaseListViewFragment {
    private byte type;  //Byte 1.领取记录 2.使用记录
    private long discountId;


    @Override
    protected void loadData() {
        if (Account.user != null) {
            final MyBenefitsRequest myBenefitsRequest =
                    new MyBenefitsRequest(discountId, page, Account.user.getShopId(), type);
            myBenefitsRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
                @Override
                public void onOK() {
                    if (myBenefitsRequest.getBenefitVOs() == null ||
                            myBenefitsRequest.getBenefitVOs().size() == 0) {
                        if (page == 1) {
                            viewContainer.setDisplayedChild(2, false);
                            viewContainer.setBtnText("去推广");
                            viewContainer.getBtnClick().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PromotionArticleAct.start(getActivity(),1);
                                }
                            });
                        } else {
                            isLastPage = true;
                        }
                    } else {
                        if (page == 1) {
                            objectAdapter.clear();
                        }
                        viewContainer.setDisplayedChild(3, false);
                        objectAdapter.addItem(myBenefitsRequest.getBenefitVOs(), true);
                    }
                    mListView.onRefreshComplete();
                }

                @Override
                public void onFail(int code) {
                    viewContainer.setDisplayedChild(1, false);
                    mListView.onRefreshComplete();
                }
            });
            myBenefitsRequest.start();
        } else {
            ViewUtils.showToast("请重新登陆");
        }
    }

    public void setDiscountId(long discountId) {
        this.discountId = discountId;
    }

    @Override
    protected void initListView(Bundle bundle) {
        mListView.setDivideHeight(0);
        if (bundle != null) {
            this.type = bundle.getByte("type");
        }

        objectAdapter = new BenefitsListAdapter(getActivity());
    }

    public void reLoad(long discountId) {
        this.discountId = discountId;
        ((BenefitsListAdapter) objectAdapter).setDiscountId(discountId);
        loadData();
    }

    public long getDiscountId(){
        return discountId;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
