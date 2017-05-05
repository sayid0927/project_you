package com.zxly.o2o.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.zxly.o2o.application.AppController;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author dsnx
 * @version YIBA-O2O 2014-12-23
 * @since YIBA-O2O
 */
public abstract class BaseFragment extends Fragment {
	protected ViewGroup content;
	protected Handler mMainHandler;

	protected LayoutInflater inflater;










	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 inflater = getActivity().getLayoutInflater();
		content = (ViewGroup) inflater.inflate(layoutId(),  null, false);

		mMainHandler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				BaseFragment.this.handleMessage(msg);
				return true;
			}
		});

		if (getArguments() != null) {
			initView(getArguments());
		}else {
			initView();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		ViewGroup p = (ViewGroup) content.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
		}
		return content;
	}


	/**当fragment可视的时候才加载数据*/
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			loadInitData();
		}
	}

	protected void loadInitData(){}

	protected abstract void initView();
	protected  void initView(Bundle bundle){};

	protected abstract int layoutId();

	protected View findViewById(int id) {
		return content.findViewById(id);
	}

	protected void handleMessage(Message msg) {

	}


    @Override
    public void onDestroy() {
        super.onDestroy();
        AppController.cancelAll(this);
    }
	@Override
	public void onResume() {
		super.onResume();
		//MobclickAgent.onResume(this.getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
	//	MobclickAgent.onPause(this.getActivity());
	}


	public Handler getMainHandler(){
		return mMainHandler;
	}
	
	public Message obtainMessage() {
		return mMainHandler.obtainMessage();
	}

	public Message obtainMessage(int what) {
		return mMainHandler.obtainMessage(what);
	}

	public Message obtainMessage(int what, Object obj) {
		return mMainHandler.obtainMessage(what, obj);
	}

	public Message obtainMessage(int what, int arg1, int arg2) {
		return mMainHandler.obtainMessage(what, arg1, arg2);
	}

	public void post(Runnable r) {
		mMainHandler.post(r);
	}


	public void keyBoardCancle() {
		View view = getActivity().getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Activity
					.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	public void  showKeyboard()
	{
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
						.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 500);
	}
}
