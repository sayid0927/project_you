package com.zxly.o2o.fragment;

import android.os.Bundle;

import com.zxly.o2o.adapter.MyTopicReplysAdapter;
import com.zxly.o2o.request.MyTopicReplysRequest;
import com.zxly.o2o.util.UMengAgent;

/**
 *     @author huangbin  @version 创建时间：2015-2-4 下午2:13:15    类说明: 
 */
public class MyTopicReplysFragment extends BasicMyCircleTopicFragment {

	@Override
	protected void loadData() {
		isReplys=true;
		myRequest = new MyTopicReplysRequest(page);
		myRequest.setOnResponseStateListener(responseStateListener);
		myRequest.start(this);

	}

	@Override
	protected void initListView(Bundle bundle) {
		objectAdapter = new MyTopicReplysAdapter(content.getContext());
		UMengAgent.onEvent(getActivity(), UMengAgent.my_rely_topics_list_page);
	}

}
