package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.BenefitVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/13.
 */
public class BenefitsGetSortRequest extends BaseRequest{
    private List<BenefitVO> benefitVOs=new ArrayList<BenefitVO>();

    public BenefitsGetSortRequest(long shopId){
        addParams("shopId",shopId);
    }

    @Override
    protected String method() {
        return "/discount/coupon/queryParam";
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<BenefitVO>> token= new TypeToken<List<BenefitVO>>(){};
        benefitVOs=GsonParser.getInstance().fromJson(data,token);
    }

    public List<BenefitVO> getBenefitVOs(){
        return benefitVOs;
    }
}
