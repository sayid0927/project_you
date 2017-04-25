package com.zxly.o2o.model;

/**
 * @description 支付宝, 微信商户参数
 */
public class PayParams {
    // 支付宝商户PID
    private String ali_partner;
    // 支付宝商户收款账号
    private String ali_seller;
    // 支付宝商户私钥，pkcs8格式
    private String ali_private_key;
    // 支付宝支付成功回调地址
    private String ali_notify_pay_url;

    //微信支付ApiKey
    private String weixin_api_key;
    //微信支付MchId
    private String weixin_mch_id;
    //微信支付AppId
    private String weixin_app_id;
    //微信支付成功回调地址
    private String weixin_notify_pay_url;

    public String getAliPartner() {
        return ali_partner;
    }

    public String getAliSeller() {
        return ali_seller;
    }

    public String getAliPrivateKey() {
        return ali_private_key;
    }

    public String getAliNotifyUrl() {
        return ali_notify_pay_url;
    }

    public String getWxApiKey() {
        return weixin_api_key;
    }

    public String getWxMchId() {
        return weixin_mch_id;
    }

    public String getWxAppId() {
        return weixin_app_id;
    }

    public String getWxNotifyUrl() {
        return weixin_notify_pay_url;
    }

    @Override
    public String toString() {
        return "PayParams{" +
                "ali_partner='" + ali_partner + '\'' +
                ", ali_seller='" + ali_seller + '\'' +
                ", ali_private_key='" + ali_private_key + '\'' +
                ", ali_notify_pay_url='" + ali_notify_pay_url + '\'' +
                ", weixin_api_key='" + weixin_api_key + '\'' +
                ", weixin_mch_id='" + weixin_mch_id + '\'' +
                ", weixin_app_id='" + weixin_app_id + '\'' +
                ", weixin_notify_pay_url='" + weixin_notify_pay_url + '\'' +
                '}';
    }
}
