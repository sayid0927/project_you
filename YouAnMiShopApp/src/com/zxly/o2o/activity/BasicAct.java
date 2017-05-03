package com.zxly.o2o.activity;

import android.app.Activity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bugtags.library.Bugtags;
import com.umeng.analytics.MobclickAgent;
import com.zxly.o2o.application.AppController;
import com.zxly.o2o.shop.R;

import java.util.Timer;
import java.util.TimerTask;

public abstract class BasicAct extends FragmentActivity {
    public Handler mMainHandler;
    protected Fragment mContentFragment;
    protected FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    public void setOnBack(View backView) {
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        Bugtags.onResume(this);
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        Bugtags.onPause(this);
        MobclickAgent.onPause(this);
    }

    protected void handleMessage(Message msg) {

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

}