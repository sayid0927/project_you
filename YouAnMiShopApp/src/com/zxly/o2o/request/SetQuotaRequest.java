package com.zxly.o2o.request;

import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.PostInfo;
import com.zxly.o2o.util.ViewUtils;


/**
 * Created by dsnx on 2015/7/7.
 */
public class SetQuotaRequest extends BaseRequest implements  BaseRequest.ResponseStateListener{

    public SetQuotaRequest(PostInfo postInfo,int nextContinue) {
        addParams("shopId", Account.user.getShopId());
        addParams("userId",Account.user.getId());
        addParams("postInfo",copy(postInfo));
        addParams("nextContinue",nextContinue);
        setOnResponseStateListener(this);
    }
    private PostInfo copy(PostInfo _postInfo)
    {
        PostInfo postInfo1=new PostInfo();
        postInfo1.setPostId(_postInfo.getPostId());
        postInfo1.setPostName(_postInfo.getPostName());
        postInfo1.tasks=_postInfo.tasks;
        return postInfo1;
    }

    @Override
    protected String method() {
        return "/promote/setQuota";
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return true;
    }

    @Override
    public void onOK() {
        ViewUtils.showToast("设置成功!");
    }

    @Override
    public void onFail(int code) {

    }
}
