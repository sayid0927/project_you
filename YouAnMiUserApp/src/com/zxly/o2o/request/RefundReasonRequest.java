package com.zxly.o2o.request;

import com.easemob.easeui.AppException;
import com.easemob.easeui.utils.GsonParser;
import com.google.gson.reflect.TypeToken;
import com.zxly.o2o.model.RefundResonVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 2015/7/28.
 */
public class RefundReasonRequest extends BaseRequest{
    public List<RefundResonVO> refundResonVOs = new ArrayList<RefundResonVO>();

    public RefundReasonRequest(){
    }
    @Override
    protected String method() {
        return "/order/applyRefund/init";
    }

    @Override
    protected void fire(String data) throws AppException {
        TypeToken<List<RefundResonVO>> dataList = new TypeToken<List<RefundResonVO>>() {};
        refundResonVOs = GsonParser.getInstance().fromJson(data, dataList);
    }
}
