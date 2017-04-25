package com.zxly.o2o.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.utils.HXNewMsgCallBack;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.o2o_user.R;

/**
 * Created by Administrator on 2015/7/31.
 */
public class RedPoint extends TextView {
    private boolean isShowText;

    public RedPoint(Context context) {
        this(context, null);
    }

    public RedPoint(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RedPoint(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.redPoint);
        isShowText = a.getBoolean(R.styleable.redPoint_redIsShowText, false);
        a.recycle();
        HXHelper.getInstance().addListener(newMsgReceivedListener);
        showRedPoint();
    }

    public void cleanRedPoint() {
        if (isShowText) {
            HXApplication.getInstance().unReadMsgCount = 0;
            this.setText("");
        }
        this.setVisibility(View.INVISIBLE);
    }

    public void showRedPoint(){
        if (HXApplication.getInstance().unReadMsgCount > 0) {
            if (isShowText) {
                this.setText(String.valueOf(HXApplication.getInstance().unReadMsgCount
                        > 99 ? 99 + "+" : HXApplication.getInstance().unReadMsgCount));
            }
            this.setVisibility(View.VISIBLE);
        }else{
            cleanRedPoint();
        }
    }


    HXNewMsgCallBack newMsgReceivedListener = new HXNewMsgCallBack() {
        @Override
        public void onCall(EMMessage emMessage) {
            if (AppController.actList.size() > 0) {
                AppController.getInstance().getTopAct().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isShowText) {
                            RedPoint.this.setText(String.valueOf(HXApplication.getInstance().unReadMsgCount
                                    > 99 ? 99 + "+" : HXApplication.getInstance().unReadMsgCount));
                        }
                        RedPoint.this.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    };
}
