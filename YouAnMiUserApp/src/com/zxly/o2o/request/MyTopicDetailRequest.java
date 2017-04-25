package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.zxly.o2o.config.Config;
import com.zxly.o2o.model.ShopTopic;
import com.zxly.o2o.model.TopicReply;

import java.util.ArrayList;

/**
 *     @author huangbin  @version 创建时间：2015-1-24 上午11:40:06    类说明: 
 */
public class MyTopicDetailRequest extends MyCircleRequest {

    public MyTopicDetailRequest(long topicId, byte isShopTopic, int pageIndex) {
        addParams("topicId", topicId);
        addParams("isShopTopic", isShopTopic);
        addParams("pageIndex", pageIndex);
        addParams("shopId", Config.shopId);
    }

    @Override
    protected void fire(String data) throws AppException {
        ShopTopic myShopTopic = GsonParser.getInstance().getBean(data, ShopTopic.class);
        if (shopTopic == null) {
            shopTopic = new ShopTopic();
        }
        topicReplys = new ArrayList<TopicReply>();
        shopTopic.setIsPraise(myShopTopic.getIsPraise());
        shopTopic.setShareAmount(myShopTopic.getShareAmount());
        shopTopic.setIsCollect(myShopTopic.getIsCollect());
        shopTopic.setShareUrl(myShopTopic.getShareUrl());
        shopTopic.setCircleHeadUrl(myShopTopic.getCircleHeadUrl());
        shopTopic.setHeadUrlList(myShopTopic.getHeadUrlList());
        shopTopic.setOriginImageList(myShopTopic.getOriginImageList());
        topicReplys.addAll(myShopTopic.getReplyList());
        myShopTopic.getReplyList().clear();
        myShopTopic = null;
    }

    @Override
    protected String method() {
        return "/user/circle/detail";
    }
}
