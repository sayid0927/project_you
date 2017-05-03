package com.zxly.o2o.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easemob.chatuidemo.HXApplication;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.UmengUtil;
import com.zxly.o2o.util.ViewUtils;

/**
 * @author fengrongjian 2015-6-24
 * @description 清除数据
 */
public class ClearDataAct extends BasicAct implements
        View.OnClickListener {
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_clear_data);
        initViews();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, ClearDataAct.class);
        ViewUtils.startActivity(intent, curAct);
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.txt_title)).setText("清除数据");
        findViewById(R.id.btn_clear_msg_list).setOnClickListener(this);
        findViewById(R.id.btn_clear_chat_log).setOnClickListener(this);
        findViewById(R.id.btn_clear_cache).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_clear_msg_list:
                createDialog(1, "确定清空消息列表？");
                UmengUtil.onEvent(ClearDataAct.this,new UmengUtil().CLEARDATA_INFOLIST_CLICK,null);

                break;
            case R.id.btn_clear_chat_log:
//                createDialog(2, "确定清空聊天记录？");
                break;
            case R.id.btn_clear_cache:
                createDialog(3, "确定清空缓存数据？");
                UmengUtil.onEvent(ClearDataAct.this,new UmengUtil().CLEARDATA_CACHEDATA_CLICK,null);

                break;
        }
    }

    private void createDialog(final int type, String title) {
        if(dialog == null){
            dialog = new Dialog(this, R.style.dialog);
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
        dialog.setContentView(R.layout.dialog_logout);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txt_title);
        ViewUtils.setText(txtTitle, title);
        dialog.findViewById(R.id.btn_done)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        switch (type) {
                            case 1:
                                HXApplication.getInstance()
                                        .deleteAllConversations(true);
                                break;
                            case 2:
                                HXApplication.getInstance()
                                        .deleteAllConversations(false);
                                break;
                        }
                        ViewUtils.showToast("清除成功");
                    }
                });
        dialog.findViewById(R.id.btn_cancel)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
    }

}
