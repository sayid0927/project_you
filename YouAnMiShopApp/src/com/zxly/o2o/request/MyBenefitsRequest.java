package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.account.Account;
import com.zxly.o2o.model.BenefitListVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/12.
 */
public class MyBenefitsRequest extends BaseRequest {
private List<BenefitListVO> benefitVOs = new ArrayList<BenefitListVO>();

    public List<BenefitListVO> getBenefitVOs() {
        return benefitVOs;
    }

    @Override
    protected String method() {
        return "/discount/take/record";
    }

    public MyBenefitsRequest(long discountId, int pageIndex, long shopId, byte type) {
        addParams("pageIndex", pageIndex);
        addParams("shopId", Account.user.getShopId());
        if(discountId!=0) {
            addParams("discountId", discountId);
        }
        addParams("type", type);
    }

    @Override
    protected void fire(String data) throws AppException {

        TypeToken<List<BenefitListVO>> token = new TypeToken<List<BenefitListVO>>(){};
        benefitVOs=GsonParser.getInstance().fromJson(data,token);
    }
}
