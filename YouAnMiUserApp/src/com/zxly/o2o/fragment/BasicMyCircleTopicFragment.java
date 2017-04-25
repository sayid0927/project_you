package com.zxly.o2o.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.activity.MyCircleSecondAct;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.model.TopicReply;
import com.zxly.o2o.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.zxly.o2o.request.BaseRequest;
import com.zxly.o2o.request.MyCircleRequest;
import com.zxly.o2o.util.Constants;

import java.util.List;

/**
 *     @author huangbin  @version 创建时间：2015-2-4 下午1:48:23    类说明: 
 */
public abstract class BasicMyCircleTopicFragment extends BaseListViewFragment implements OnItemClickListener, OnRefreshListener {
    protected boolean isReplys;

    protected BaseRequest.ResponseStateListener responseStateListener = new BaseRequest.ResponseStateListener() {

        @Override
        public void onOK() {
            isTokenInvaild = false;

            if (isReplys) {
                List<TopicReply> topicReplys = ((MyCircleRequest)myRequest).topicReplys;
                if (topicReplys == null || topicReplys.size() == 0) {
                    if (page == 1) {
                        viewContainer.setDisplayedChild(2, false);
                    }
                    isLastPage = true;
                } else {
                    if (page == 1) {
                        viewContainer.setDisplayedChild(3, false);
                        objectAdapter.clear();
                        objectAdapter.addItem2Head(topicReplys, true);
                    } else {
                        objectAdapter.addItem(topicReplys, true);
                    }
                }
            } else {
                List<ShopTopic> shopTopics = ((MyCircleRequest)myRequest).shopTopics;
                if (shopTopics == null || shopTopics.size() == 0) {
                    if (page == 1) {
                        viewContainer.setDisplayedChild(2, false);
                    }
                    isLastPage = true;
                } else {
                    if (page == 1) {
                        viewContainer.setDisplayedChild(3, false);
                        objectAdapter.clear();
                        objectAdapter.addItem2Head(shopTopics, true);
                    } else {
                        objectAdapter.addItem(shopTopics, true);
                    }
                }
            }

            mListView.onRefreshComplete();
        }

        @Override
        public void onFail(int code) {
            if (code == 20101) {  //token无效
                isTokenInvaild = false;
            }
            mListView.onRefreshComplete();
            viewContainer.setDisplayedChild(1, true);
        }
    };



    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        if (isReplys) {
            MyCircleRequest.shopTopic = ((TopicReply) objectAdapter.getItem(position - 1)).getTopicVO();
            MyCircleRequest.shopTopic.setIsMyReply(true);
        } else {
            MyCircleRequest.shopTopic = (ShopTopic) objectAdapter.getItem(position - 1);
            MyCircleRequest.shopTopic.getUserVO().setNickname(Account.user.getNickName());
            MyCircleRequest.shopTopic.getUserVO().setThumHeadUrl(Account.user.getThumHeadUrl());
        }
        if (MyCircleRequest.shopTopic != null) {
            MyCircleSecondAct.MyCircleLunch(getActivity(), Constants.FORUM_COMMUNITY_DETAIL,"");
        }
    }

}
