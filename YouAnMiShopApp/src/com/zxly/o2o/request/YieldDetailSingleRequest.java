package com.zxly.o2o.request;


import com.easemob.easeui.AppException;
import com.zxly.o2o.application.Config;
import com.zxly.o2o.model.CommissionProduct;
import com.zxly.o2o.model.KeyValue;
import com.zxly.o2o.model.YieldDetail;
import com.zxly.o2o.util.StringUtil;
import com.zxly.o2o.util.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsnx on 2015/12/22.
 */
public class YieldDetailSingleRequest extends BaseRequest {

    private int type;
    private List<KeyValue> keyValueLis=new ArrayList<KeyValue>();
    private List<CommissionProduct>  commissionProductList=new ArrayList<CommissionProduct>();
    private String userAccount;
    private String numberNo;
    private String orderNo;
    private long payTime;
    private int status;
    private long  residueTime;
    private float commission;
    public YieldDetailSingleRequest(int id,String serialNumber, int type)
    {
        addParams("number",serialNumber);
        addParams("type",type);
        addParams("id",id);
        this.type=type;

    }
    public int getType() {
        return type;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public String getNumberNo() {
        return numberNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public long getPayTime() {
        return payTime;
    }

    public int getStatus() {
        return status;
    }

    public long getResidueTime() {
        return residueTime;
    }

    public float getCommission() {
        return commission;
    }

    public List<KeyValue> getKeyValueLis() {
        return keyValueLis;
    }

    public List<CommissionProduct> getCommissionProductList() {
        return commissionProductList;
    }

    @Override
    protected void fire(String data) throws AppException {
        try {
            JSONObject jsonObject=new JSONObject(data);
            userAccount=jsonObject.optString("userAccount","");
            numberNo=jsonObject.optString("numberNo","");
            orderNo=jsonObject.optString("orderNo", "");
            payTime=jsonObject.optLong("payTime", 0);
            status=jsonObject.optInt("status");
            residueTime=jsonObject.optLong("residueTime");
            commission= (float) jsonObject.optDouble("commission");
            if(type== YieldDetail.YIEL_TYPE_FLOW)
            {
                if(jsonObject.has("fc"))
                {
                    JSONObject fcJson=jsonObject.getJSONObject("fc");
                    long rechargeTime=fcJson.optLong("rechargeTime", 0);
                    KeyValue kvTime=new KeyValue("充值时间", TimeUtil.formatTimeYMDHMS(rechargeTime));
                    keyValueLis.add(kvTime);
                    KeyValue kvMobile=new KeyValue("充值手机号",fcJson.optString("mobile", "-"));
                    keyValueLis.add(kvMobile);
                    int operatorsType=fcJson.optInt("operators", 0);
                    int trafficType=fcJson.optInt("trafficType",0);
                    String operatorName,trafficName;
                    switch (operatorsType)
                    {
                        case 1://移动
                            operatorName="移动";
                            break;
                        case 2://联通
                            operatorName="联通";
                            break;
                        case 3://电信
                            operatorName="电信";
                            break;
                        default:
                            operatorName="";
                    }
                    switch (trafficType)
                    {
                        case 1:
                            trafficName="全国";
                            break;
                        case 2:
                            trafficName="省内";
                            break;
                        default:
                            trafficName="";
                    }
                    KeyValue kvOperators=new KeyValue("运营商",operatorName);
                    keyValueLis.add(kvOperators);
                    KeyValue kvTrafficType=new KeyValue("流量类型",trafficName);
                    keyValueLis.add(kvTrafficType);
                    long flow=fcJson.optLong("flow");
                    String flowDesc;
                    if(flow<1024)
                    {
                        flowDesc=flow+"M";
                    }else
                    {
                        flowDesc=(flow/1024)+"G";
                    }
                    KeyValue kvFlow=new KeyValue("充值流量",flowDesc);
                    keyValueLis.add(kvFlow);
                    KeyValue kvPrice=new KeyValue("门店售价","￥"+fcJson.optString("price","-"));
                    keyValueLis.add(kvPrice);
                }
            }else if(type==YieldDetail.YIEL_TYPE_PROUDCT)
            {
                if(jsonObject.has("pc"))
                {
                    if(jsonObject.getJSONObject("pc").has("details"))
                    {
                        JSONArray  jsonArray=jsonObject.getJSONObject("pc").getJSONArray("details");
                        int length=jsonArray.length();
                        for(int i=0;i<length;i++)
                        {
                            JSONObject productJson=jsonArray.getJSONObject(i);
                            CommissionProduct cp=new CommissionProduct();
                            cp.setCommission((float) productJson.optDouble("commission"));
                            cp.setHeadUrl(productJson.optString("headUrl"));
                            cp.setName(productJson.optString("name"));
                            cp.setPcs(productJson.optInt("pcs"));
                            cp.setPrice((float) productJson.getDouble("price"));
                            cp.setId(productJson.optInt("productId"));
                            cp.setRemark(productJson.optString("remark"));
                            cp.setStatus(productJson.getInt("status"));
                            commissionProductList.add(cp);
                        }
                    }
                }
            }else if(type==YieldDetail.YIEL_TYPE_YB)
            {
                JSONObject insuranceOrder=jsonObject.getJSONObject("insuranceOrder");
                JSONObject productJSON=insuranceOrder.getJSONObject("product");
                JSONObject orderInfoJSON=insuranceOrder.getJSONObject("orderInfo");

                KeyValue kv1=new KeyValue("商品名称",productJSON.optString("name"));
                keyValueLis.add(kv1);
                KeyValue kv2=new KeyValue("用户账号",userAccount);
                keyValueLis.add(kv2);
                KeyValue kv3=new KeyValue("购买人",orderInfoJSON.optString("userName"));
                keyValueLis.add(kv3);
                KeyValue kv4=new KeyValue("手机号",orderInfoJSON.optString("userPhone"));
                keyValueLis.add(kv4);
                KeyValue kv5=new KeyValue("消费金额", StringUtil.getPrice((float)insuranceOrder.optDouble("price")));
                keyValueLis.add(kv5);
                KeyValue kv6=new KeyValue("支付时间", TimeUtil.formatOrderTime(payTime));
                keyValueLis.add(kv6);
                KeyValue kv7=new KeyValue("订单号", insuranceOrder.optString("orderNo"));
                keyValueLis.add(kv7);
                KeyValue kv8=new KeyValue("收益单号", numberNo);
                keyValueLis.add(kv8);

            }
        } catch (JSONException e) {
            throw JSONException(e);
        }

    }


    @Override
    protected String method() {
        return "pay/billDetail";
    }
}
