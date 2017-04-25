package com.zxly.o2o.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.ui.ContactListFragment;
import com.easemob.chatuidemo.ui.ConversationListFragment;
import com.easemob.chatuidemo.utils.EaseCallBack;
import com.easemob.easeui.EaseConstant;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.utils.TabEntity;
import com.flyco.tablayout.utils.ViewFindUtils;
import com.zxly.o2o.shop.R;
import com.zxly.o2o.util.ViewUtils;

import java.util.ArrayList;


/**
 * Created by hejun on 2016/9/30.
 */
public class AllMessageAct extends BasicAct{


    private ContactListFragment contactListFragment;
    private ConversationListFragment conversationListFragment;
    protected ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    protected EaseCallBack newMsgCallBack;
    private String[] titles;
    private ArrayList<CustomTabEntity> tabs = new ArrayList<CustomTabEntity>();
    private View decorView;
    private CommonTabLayout tls;
    private TextView btn_back;
    private TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_allmessage);
        initView();
    }

    private void initView() {
        btn_back = (TextView) findViewById(R.id.btn_back);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("消息");
        contactListFragment =new ContactListFragment();
        conversationListFragment = new ConversationListFragment();
        conversationListFragment.setCallBack(newMsgCallBack);
        fragments.add(conversationListFragment);
        fragments.add(contactListFragment);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (EaseConstant.shopID < 0) {
            titles = new String[]{"消息", "粉丝"};
        } else {
            titles = new String[]{"消息", "联系人"};
        }

        for (String title : titles) {
            tabs.add(new TabEntity(title, 0, 0));
        }

        decorView = this.getWindow().getDecorView();
        /** indicator圆角色块 */
        tls = ViewFindUtils.find(decorView, com.easemob.chatuidemo.R.id.ease_tl_tabs);
        tls.setTabData(tabs, this, com.easemob.chatuidemo.R.id.fl_change, fragments);
    }

    @Override
    public void onStop() {
//        EMChatManager.getInstance().unregisterEventListener(this);
        HXHelper helper = HXHelper.getInstance();
        helper.popActivity(this);

        super.onStop();
    }

    @Override
    public void onResume() {
        HXHelper helper = HXHelper.getInstance();
        helper.pushActivity(this);
        super.onResume();
    }

    public static void start(Activity curAct) {
        Intent intent = new Intent(curAct, AllMessageAct.class);
        ViewUtils.startActivity(intent, curAct);
    }
}
