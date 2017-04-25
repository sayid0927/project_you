package com.easemob.easeui.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.R;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.widget.EaseContactList;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/8.
 */
public class EaseSelectMembersActivity extends EaseBaseMyListPageActivity {
    EaseContactList listView;
    private TextView finish;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.ease_contact_pulltorefresh_layout);

        //int button finish;
        finish = ((TextView) setUpActionBar("群发人员")
                .getCustomView().findViewById(R.id.tag_title_right));
        finish.setText("完成");
        finish.setVisibility(View.VISIBLE);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //重新填充要推送的人员
                EaseConstant.users.clear();
                EaseConstant.getuiUsers.clear();
                for (EaseYAMUser user : HXHelper.yamContactList) {
                    if (user.isCheck()) {
                        EaseConstant.users.add(HXApplication.getInstance()
                                .parseUserFromID(user.getFirendsUserInfo().getId(),
                                        HXConstant.TAG_USER));
                        //将选中的用户加入至个推队列中
                        EaseConstant.getuiUsers.add(user.getFirendsUserInfo().getId()+"");
                    }
                }
                finish();
            }
        });
        setFlipper();

        //init list
        listView = (EaseContactList) findViewById(R.id.listview);

        EaseContactAdapter.unRegistList.clear();
        if (EaseConstant.isRegistMembers) {
            listView.init(HXHelper.yamContactList);
        } else {  //类型为未注册直接传空的list进入
            listView.init(new ArrayList<EaseYAMUser>());
        }
        listView.setShowSiderBar(false);
        listView.setIsShowCheckIcon(true);

        listView.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id < HXHelper.yamContactList.size()) {   //商户app有未注册的成员需要过滤掉
                    EaseYAMUser user = (EaseYAMUser) listView.getListView().getItemAtPosition(position);
                    if (user.isCheck()) {
                        user.setIsCheck(false);
                        ((ImageView) view.findViewById(R.id.check_icon)).setImageResource(R.drawable
                                .ease_check_normal);
                    } else {
                        user.setIsCheck(true);
                        ((ImageView) view.findViewById(R.id.check_icon)).setImageResource(R.drawable
                                .ease_check_press);
                    }
                    //                    itemClickLaunchIntent.putExtra(EaseConstant.USER_ID, username);
                }
            }
        });

        loadData();
    }


    @Override
    protected void loadData() {
        viewContainer.setDisplayedChild(3, true);


    }

}
