package com.zxly.o2o.fragment;

import android.os.Bundle;

import com.zxly.o2o.adapter.MyTopicsAdapter;
import com.zxly.o2o.request.MyTopicsRequest;
import com.zxly.o2o.util.UMengAgent;

/**
 *     @author huangbin  @version 创建时间：2015-2-4 下午1:33:32    类说明: 
 */
public class MyTopicsFragment extends BasicMyCircleTopicFragment {

    @Override
    protected void loadData() {
        isReplys = false;
        myRequest = new MyTopicsRequest(page);
        myRequest.setOnResponseStateListener(responseStateListener);
        myRequest.start(this);
    }

    @Override
    protected void initListView(Bundle bundle) {
        objectAdapter = new MyTopicsAdapter(content.getContext());
        UMengAgent.onEvent(getActivity(), UMengAgent.my_publish_topics_list_page);
    }


}
