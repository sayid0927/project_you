package com.shyz.downloadutil;

import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.zxly.o2o.o2o_user.R;

import java.util.List;

/**
 * 已经未下载完成页面
 * @author fengruyi
 *
 */
public class TaskDoingFragment extends _BaseFragment {
	private ListView mListView;
	private ListAppDownLoadingAdapter doingLoadAdapter;
	private View emptyView;
	private DownloadManager manager;
	
	public void handleInfoMessage(Message msg) {
		
		
	}
 
	public int getContentViewId() {
		
		return R.layout.fragment_downloading;
	}
  
	public void initViewAndData() {
		manager = DownloadManager.createDownloadManager(getActivity());
		mListView = obtainView(R.id.lv_default);
		emptyView  =obtainView(R.id.emptyview);
		mListView.setEmptyView(emptyView);
		List<DownLoadTaskInfo> doinglist = manager.getDoingTask();
		doingLoadAdapter = new ListAppDownLoadingAdapter(getActivity(), doinglist,manager);
		mListView.setAdapter(doingLoadAdapter);
		
	}
	public void onResume() {
		super.onResume();
		if(doingLoadAdapter!=null){
			doingLoadAdapter.notifyDataSetChanged();
		}
	}
	/**
	 * 未下载任务有任务完成时把该任务移到已完成中
	 * @param taskInfo
	 */
    public void removeTask(DownLoadTaskInfo taskInfo){
    	doingLoadAdapter.removeItem(taskInfo);
    }
    
	

}
