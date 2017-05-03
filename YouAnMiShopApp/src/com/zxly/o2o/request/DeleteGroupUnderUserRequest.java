package com.zxly.o2o.request;

/**
 * Created by doctor_zheng on 2017/3/27.
 *
 * @des: 删除自定义组 下面的用户
 */

public class DeleteGroupUnderUserRequest extends  BaseRequest {

    public DeleteGroupUnderUserRequest(int groupId ,long userId){
        addParams("userId", userId);
        addParams("groupId",groupId);
    }
    @Override
    protected String method() {
        return "/keduoduo/member/deleteGroupUnderUser";
    }
}
