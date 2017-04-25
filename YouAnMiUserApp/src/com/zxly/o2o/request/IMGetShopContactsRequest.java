package com.zxly.o2o.request;

import android.util.Log;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.chatuidemo.HXConstant;
import com.easemob.chatuidemo.HXHelper;
import com.easemob.chatuidemo.HXModel;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.EaseYAMUser;
import com.easemob.easeui.request.HXNormalRequest;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.controller.AppController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class IMGetShopContactsRequest extends BaseRequest {
    private boolean isOnlyLoadShopUsers=false;
    public static boolean isLoadingContact=false;

    public IMGetShopContactsRequest(boolean isOnlyLoadShopUsers) {
        addParams("shopId", Math.abs(EaseConstant.shopID));
        this.isOnlyLoadShopUsers=isOnlyLoadShopUsers;
        isLoadingContact=true;
    }



    @Override
    protected void fire(String data) throws AppException {
         List<EaseUser> getRoleUsers;
         List<EaseYAMUser> shopRoleUsers=new ArrayList<EaseYAMUser>();
        getRoleUsers = GsonParser.getInstance().fromJson(data, new TypeToken<List<EaseUser>>() {
        });

        if(getRoleUsers!=null) {
            for (EaseUser easeUser : getRoleUsers) {
                EaseYAMUser easeYAMUser = new EaseYAMUser();
                easeUser.setIsTop(1);
                easeUser.setEid(HXApplication.getInstance().parseUserFromID(easeUser.getUserId()
                        ==0?easeUser.getId():easeUser.getUserId(), HXConstant.TAG_SHOP));
                easeYAMUser.setFirendsUserInfo(easeUser);
                easeYAMUser.getFirendsUserInfo().setGroupId(101);  //101为业务员组 1为好友组
                shopRoleUsers.add(easeYAMUser);
            }

        }

        if(!isOnlyLoadShopUsers) {
            new IMGetContactsRequest(shopRoleUsers).start();
        }else{
            HXHelper.getInstance().cleanYAMContactList();  //清空缓存
            new HXModel(AppController.getInstance())
                    .saveYAMContactList(shopRoleUsers);
        }
    }

    @Override
    protected String method() {
        return "/huanxin/shop/role/group";
    }
}
