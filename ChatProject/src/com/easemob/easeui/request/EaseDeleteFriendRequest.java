package com.easemob.easeui.request;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.HXModel;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.adapter.EaseContactAdapter;

/**
 * Created by Administrator on 2015/12/30.
 */
public class EaseDeleteFriendRequest extends HXNormalRequest {
    private long userID;

    public EaseDeleteFriendRequest(long friendsUserId) {
        addParams("friendsUserId", friendsUserId);
        addParams("shopId", Math.abs(EaseConstant.shopID));
        this.userID = friendsUserId;
    }

    @Override
    protected String method() {
        return "/friends/user/delete";
    }

    @Override
    protected void fire(String data) throws AppException {
        HXHelper.getInstance().cleanYAMContactList();  //清空缓存
        new UserDao(HXApplication.applicationContext).deleteYAMContact(HXApplication.getInstance()
                .parseUserFromID(userID, HXConstant.TAG_USER));
        HXHelper.getInstance().getYAMContactList();  //重新加载
    }
}
