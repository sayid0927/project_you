package com.zxly.o2o.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.easemob.easeui.EaseConstant;
import com.zxly.o2o.activity.MyCircleSecondAct;
import com.zxly.o2o.adapter.CircleForumListAdapter;
import com.zxly.o2o.model.CircleForumVO;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.ForumDisconcernRequest;
import com.zxly.o2o.request.ForumMainRequest;
import com.zxly.o2o.util.CallBack;
import com.zxly.o2o.util.Constants;
import com.zxly.o2o.view.MyFlipperView;

/**
 * Created by Administrator on 2015/12/16.
 */
public class CircleAllListFragment extends BaseListViewFragment {
    private ForumDisconcernRequest forumDisconcernRequest;

    @Override
    protected void loadData() {
        forumDisconcernRequest = new ForumDisconcernRequest(page);
        forumDisconcernRequest.setOnResponseStateListener(new BaseRequest.ResponseStateListener() {
            @Override
            public void onOK() {
                if (forumDisconcernRequest.circleForumVOs.size() != 0) {
                    if(page==1){
                        objectAdapter.clear();
                    }
                    objectAdapter.addItem(forumDisconcernRequest.circleForumVOs, true);
                    if (isFirstLoad) {
                        viewContainer.setDisplayedChild(MyFlipperView.LOADSUCCESSFUL, true);
                        isFirstLoad = false;
                    }
                } else {
                    isLastPage = true;
                    if(page==1)
                    viewContainer.setDisplayedChild(MyFlipperView.NODATA, true);
                }
                mListView.onRefreshComplete();
            }

            @Override
            public void onFail(int code) {
                viewContainer.setDisplayedChild(MyFlipperView.LOADFAIL, true);
                mListView.onRefreshComplete();
            }
        });
        forumDisconcernRequest.start();
    }

    @Override
    protected void initListView(Bundle bundle) {
        objectAdapter = new CircleForumListAdapter(content.getContext(), -1,new  CallBack(){

            @Override
            public void onCall() {
                page=1;
                loadData();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent();
//        if (position == 0) {
//            intent.putExtra("isShop", (byte) 0);  //是
//        } else {
//            intent.putExtra("isShop", (byte) 1);  //否
//        }
//        intent.putExtra("mycircle_second_title",
//                ((CircleForumVO) objectAdapter.getItem(position)).getName());
//        intent.putExtra("circleId", ((CircleForumVO) objectAdapter.getItem(position)).getId());
//        intent.setClass(getActivity(), MyCircleSecondAct.class);
//        EaseConstant.startActivity(intent, getActivity());

        MyCircleSecondAct.start(((CircleForumVO) objectAdapter.getItem((int)id))
                        .getId(), ((CircleForumVO) objectAdapter.getItem((int)id)).getName(), Constants
                        .FORUM_COMMUNITY, (byte) 1, getActivity()
        );

//        MyCircleSecondAct.MyCircleLunch(getActivity(), Constants.FORUM_COMMUNITY, "店铺交流");
    }
}
