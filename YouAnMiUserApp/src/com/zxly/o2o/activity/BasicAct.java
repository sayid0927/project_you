package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dx.libagent2.Agent2Yam;
import com.easemob.easeui.controller.EaseUI;
import com.umeng.analytics.MobclickAgent;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.service.NetBroadcastReceiver;
import com.zxly.o2o.util.ViewUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BasicAct extends FragmentActivity {
    public Handler mMainHandler;
    protected Fragment mContentFragment;
    protected FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*//透明状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//透明导航栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
        if (null != savedInstanceState) {
            finish();
            Intent intent = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
            android.os.Process.killProcess(android.os.Process.myPid());

        } else {
            fm = getSupportFragmentManager();
            mMainHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    BasicAct.this.handleMessage(msg);
                    return true;
                }
            });
            AppController.addAct(this);
        }

        NetBroadcastReceiver netBroadcastReceiver= new NetBroadcastReceiver();
        netBroadcastReceiver.onReceive(this,null);
        /**启用Agent2Yam服务*/
        this.startService(new Intent(this, Agent2Yam.class));

    }


    public void keyBoardCancle() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.cancelAll(this);
        AppController.remove(this);
    }

    protected boolean replaceFragment(int id, Fragment fragment) {
        boolean isAdded = false;
        if (mContentFragment == fragment) {
            return true;
        }
        Fragment f = fm.findFragmentByTag(fragment.getClass().getName());
        FragmentTransaction bt = fm.beginTransaction();
        if (mContentFragment != null) {
            bt.hide(mContentFragment);
        }
        if (f == null) {
            // bt.add(fragment, fragment.getClass().getName());
            bt.replace(id, fragment);
        } else {
            isAdded = true;
            bt.show(fragment);
        }
        bt.commit();
        mContentFragment = fragment;
        return isAdded;
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        /** 判断Agent2Yam服务是否运行*/
       if (!isServiceRunning(this, "com.dx.libagent2.Agent2Yam")) {
            this.startService(new Intent(this, Agent2Yam.class));
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void handleMessage(Message msg) {

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

    public void close() {
        super.finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }


    /**
     * 用来判断服务是否运行.
     *
     * @param context
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}