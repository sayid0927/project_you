package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.GiftGetInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2016/6/13.
 */
public class DisCountSystemMsgDeTailRequest extends BaseRequest {

    private List<GiftGetInfo> giftGetInfoList=new ArrayList<GiftGetInfo>();
    private String serverURL;
    private int isDiscount;
    private String promotionDesc;
    public List<GiftGetInfo> getGiftGetInfoList() {
        return giftGetInfoList;
    }
    public boolean hasNext = true;

    public DisCountSystemMsgDeTailRequest(int pageIndex, int pageSize)
    {
        addParams("pageIndex",pageIndex);
        addParams("pageSize",pageSize);
        addParams("shopId",Account.user.getShopId());
    }

    public String getPromotionDesc() {
        return promotionDesc;
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jData=new JSONObject(data);
            JSONArray jArray=jData.getJSONArray("resultData");
            StringBuilder shareUrl=new StringBuilder();
            shareUrl.append(jData.optString("serverURL"));
            shareUrl.append("index.html?from=xx").append("&price=null").append("&shopId=").append(Account.user.getShopId()).append("&promotionId=").append(Account.user.getId());
            isDiscount=jData.optInt("isDiscount",1);
            promotionDesc=jData.optString("promotionDesc");
            serverURL=shareUrl.toString();
            int length=jArray.length();
            if(length>0)
            {
                for(int i=0;i<length;i++)
                {
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    GiftGetInfo giftGetInfo=new GiftGetInfo();
                    giftGetInfo.setCouponStatus(jsonObject.optInt("couponStatus"));
                    giftGetInfo.setDiscountInfo(jsonObject.optString("discountInfo"));
                    giftGetInfo.setDiscountType(jsonObject.optInt("discountType"));
                    giftGetInfo.setUserName(jsonObject.optString("userName"));
                    giftGetInfo.setHeadUrl(jsonObject.optString("headUrl"));
                    giftGetInfoList.add(giftGetInfo);
                }
            }
            if(giftGetInfoList.size()<10){
                hasNext = false;
            }


        } catch (JSONException e) {
            throw JSONException(e);
        }

    }



    public int getIsDiscount() {
        return isDiscount;
    }

    public String getServerURL() {
        return serverURL;
    }

    @Override
    protected String method() {
        return "/discount/systemMsg/detail";
    }
}
