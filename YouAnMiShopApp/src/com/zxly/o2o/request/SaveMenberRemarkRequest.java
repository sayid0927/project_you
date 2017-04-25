package com.zxly.o2o.request;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by hejun on 2016/9/10.
 * 写备注保存请求
 */
public class SaveMenberRemarkRequest extends BaseRequest{
    /**
     *
     * @param isBuyInShop 是否到店购买 0否1是
     * @param menberId 会员id
     * @param tagId 勾选的标签id
     * @param groupIds 新增的会员分组id
     * @param productName 商品名称 当为到店购买的时候不能为空
     * @param productPrice 商品价格 当为到店购买的时候不能为空
     * @param remarkContent 备注内容
     * @param remarkName 会员备注
     * @param shopId 门店id
     */
    public SaveMenberRemarkRequest(int isBuyInShop, long menberId, List<String> tagId,List<String> groupIds,String productName
    ,String productPrice,String remarkContent,String remarkName,long shopId){
        addParams("buyOffline",isBuyInShop);
        addParams("id",menberId);
        addParams("labelIds",tagId);
        addParams("newGroupIds",groupIds);
        addParams("productName",productName);
        if(TextUtils.isEmpty(productPrice)){
            addParams("productPrice","");
        }else {
            addParams("productPrice",Float.parseFloat(productPrice));
        }
        addParams("remark",remarkContent);
        addParams("remarkName",remarkName);
        addParams("shopId",shopId);
    }

    @Override
    protected boolean isShowLoadingDialog() {
        return false;
    }

    @Override
    protected String method() {
        return "/keduoduo/member/saveMember";
    }
}
