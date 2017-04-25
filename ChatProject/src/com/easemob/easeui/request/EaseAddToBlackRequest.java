package com.easemob.easeui.request;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.easemob.chatuidemo.HXApplication;
import com.easemob.easeui.AppException;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.controller.EaseUI;

/**
 * Created by Administrator on 2015/12/22.
 */
public class EaseAddToBlackRequest extends HXNormalRequest{

    public EaseAddToBlackRequest(long friendsUserId){
        addParams("friendsUserId",friendsUserId);
        addParams("shopId",Math.abs(EaseConstant.shopID));
    }

    @Override
    protected String method() {
        return "/friends/black/add";
    }

    @Override
    protected void fire(String data) throws AppException {
    }
}
