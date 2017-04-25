package com.zxly.o2o.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;

import com.easemob.easeui.controller.EaseUI;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.model.NetWork;
import com.zxly.o2o.model.PayParams;
import com.zxly.o2o.o2o_user.R;
import com.zxly.o2o.util.ImeiUtil;
import com.zxly.o2o.util.NetworkUtil;
import com.zxly.o2o.util.PreferUtil;

import java.util.Properties;

/**
 *     @author dsnx  @version 创建时间：2015-1-6 上午11:40:18    类说明: 有用配置文件相关(内网,外网
 * 方便切换)
 */
public class Config {
    private static Properties prop;
    public static String dataBaseUrl;
    public static String h5Url;
    public static int densityDpi;
    public static int screenWidth;
    public static int screenHeight;
    public static float scaleRate;
    public static long shopId;
    public static int shopVersionCode;
    public static String promotionCode;
    public static String mac;
    public static String ip;
    public static String imei;
    public static int servieProvider;
    public static  DisplayMetrics displayMetrics;
    public static String shareUrl;
    public static String discountUrl;
    public static String newDiscountDetailUrl;
    public static long timeOffset = 0;
    public static String clientId;
    public static String getuiClientId;
    public static String articleUrl;//文章优化版本 h5服务地址
    //原来的商户号
//    public static final String VIRTUAL_PARTNER = "201509071000488503";//虚拟交易商品号
//    public static final String PARTNER="201509011000481511";//实物交易商户号

    //新的商户号
    public static final String VIRTUAL_PARTNER = "201603041000749732";//虚拟交易商品号
    public static final String PARTNER="201603041000750674";//实物交易商户号

    /** 系统DES传输签名密钥，常量不可变动 */
    public static final String USER_SIGN_KEY = "YAMPLMKA";
    public static String accessKey;//用户秘钥

    public static int serverShopversionNo;//服务器版本
    public static String serverVersionName;
    public static String AppUpdateUrl;
    public static int versionCodeCur;
    public static String appSerialNo; //app 版本号标注app发包日期时间


    // 商户（RSA）私钥 TODO 强烈建议将私钥配置到服务器上，否则有安全隐患
    // public static final String RSA_PRIVATE =
    // "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOilN4tR7HpNYvSBra/DzebemoAiGtGeaxa+qebx/O2YAdUFPI+xTKTX2ETyqSzGfbxXpmSax7tXOdoa3uyaFnhKRGRvLdq1kTSTu7q5s6gTryxVH2m62Py8Pw0sKcuuV0CxtxkrxUzGQN+QSxf+TyNAv5rYi/ayvsDgWdB3cRqbAgMBAAECgYEAj02d/jqTcO6UQspSY484GLsL7luTq4Vqr5L4cyKiSvQ0RLQ6DsUG0g+Gz0muPb9ymf5fp17UIyjioN+ma5WquncHGm6ElIuRv2jYbGOnl9q2cMyNsAZCiSWfR++op+6UZbzpoNDiYzeKbNUz6L1fJjzCt52w/RbkDncJd2mVDRkCQQD/Uz3QnrWfCeWmBbsAZVoM57n01k7hyLWmDMYoKh8vnzKjrWScDkaQ6qGTbPVL3x0EBoxgb/smnT6/A5XyB9bvAkEA6UKhP1KLi/ImaLFUgLvEvmbUrpzY2I1+jgdsoj9Bm4a8K+KROsnNAIvRsKNgJPWd64uuQntUFPKkcyfBV1MXFQJBAJGs3Mf6xYVIEE75VgiTyx0x2VdoLvmDmqBzCVxBLCnvmuToOU8QlhJ4zFdhA1OWqOdzFQSw34rYjMRPN24wKuECQEqpYhVzpWkA9BxUjli6QUo0feT6HUqLV7O8WqBAIQ7X/IkLdzLa/vwqxM6GLLMHzylixz9OXGZsGAkn83GxDdUCQA9+pQOitY0WranUHeZFKWAHZszSjtbe6wDAdiKdXCfig0/rOdxAODCbQrQs7PYy1ed8DuVQlHPwRGtokVGHATU=";

    //旧秘钥
//    public static final String RSA_PRIVATE =
//            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOx3lgfN5cbeAbYmbKqzfQUcQKutEJpMuMt8QcsDtTbPuU5Ftd+FUQAiZjp4U83oSTfhms8dQwTJrRXEkXaBJUB2NY2BXCE/qY/k3Z9IEh9tpxc+8tAOiHaPPPRxcpj/ppCegYW9BltND7uaIfUHRcPMX/kKsGsiSA1pDv3p6Q9pAgMBAAECgYAqvQ5zyKYABH4gst8vFxPAibOyk6wNh1JbDNFDZR8qke63E+hkRs82DAGYDclvzMz2+aKmTm6ffef3qDh2R684l3SDOLbHSKQKQeKn01xbBZHQ6IqS7jY7bpL+0Qhd8IXfQIuiImTTqNsngc6kO7wrAaV40j8Y6Hwbf5BGbPY+JQJBAPa4NT1mdp1oCOrLsbD1w8Qweq68GrEeorQgx/yGjN1y6yztcKx6UFPZWEgTy0SPnSknYK/qJXPUbVmxccz/KTcCQQD1XKbuF9uIqObqk2SnNUplK92AS+ZuDS64ESSRS5jKQ7sXIfQgaVUAzW4KiU9sS80qdjPGr/yCtSeZXbhvX1xfAkEAoDiAWp9v6EjngZNGkeUIfR/+i/scWmnKv6+KMDQwxp8amtKXmWrVP56l9ijkmGGrbk5kO9mS+OW7HcReYwJRgQJAQJjCAhEZ5SLCmKNxbmwjR/uCd1KEOhkSYbdxryb99NJcITz5LsdMb8el3vRDirlyLGmuO/L9QdQ7tq7r7bZndQJAZdpW052szlZdEaHcu4v/Hormq9rwabn5d0oLERppAs+im+h2cRkvgWEQlVf2ltteAgcJT+0bBOllufIIGF26Hw==";

    //新秘钥
    public static final String RSA_PRIVATE =
            "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANdYEMKW7ck39yEXAnz0JYuMbUfjKMxzmlz/SzvrVzw8WEJrAvPvp67jGUkLBO7+kdO1gD9bwDvS9iDoBJIAyq4A2kiIhPYeVC+h+o+YceTNZuZ28DlxaCZB6cEApkL53mS8xEcLYO3+KRrULRCFm6Q0RFZXQ44OZoziMvcwUe1tAgMBAAECgYANh/UVpRIv8JpQs/duDpFFYX+EYtuvbRwn98qsay9CE7CgC8VVuptYu+Bc3Jpkwg47tU3nrUY4/BqHGs0et3oEs3so0cJEJGAj8HdSX+QQ1uL2nLH0FSaCYS9HEsul9FkyPvEE628NLcYqqggoW7a3Q3eGiXE3DRMnYIHWcGUCKQJBAOwF2HnAa/E9V/9ZFsgdZHcqop+/2G8XebWb3YhVPcdIxh4ppZv8Dh+zGnuIZdwb4pc1LgvILu8RTQPdVP7AdzcCQQDpkiZlXBtbMuv88R3aACifmoo73OdX8Xb621zQTH9qljROlGwxWqQjYbU8veG/z+PLIyy17PsEVxYdK8yTk4p7AkAJ9TEt6dVkBy+IQWz5omvTr0PPM5vhC/+BjwLotpf1qXdaXG11SEwcQEU0wwtxHugPcxnvO1HnmjZCnHryiJI/AkBgohC0mrjOsHCmA3OgP7SO47cRMUGdRs881gi+PQfyJxaY9BsXCDMkWEZNTRna672Iy5Kx6cuobgc+JmLLI52FAkB3ip5IwrudTZpcYEwgMsUe/7Hh2TkH8CC8+LYMuXDUrdSLtU6OD1zIMwiUfs7qm+TnHKre/YxLNWrjCLOEJwpO";

    // 银通支付（RSA）公钥
    public static final String RSA_YT_PUBLIC =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";


    //支付宝, 微信支付商户参数对象
    public static PayParams payParams;
    //个推配置数据
    public static String id;
    public static String key;
    public static String secret;
    public static String hxInfo;


    public static void init(Context ctx) {
        LoadProperty();
        dataBaseUrl = getConfig("data_base_url").trim();
        displayMetrics = EaseUI.displayMetrics;
        densityDpi = displayMetrics.densityDpi;
        scaleRate = (float) densityDpi / (float) DisplayMetrics.DENSITY_MEDIUM;
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        initShopInfo(ctx);
        NetWork netWork = NetworkUtil.getMacAndIp();
        mac = netWork.mac;
        ip = netWork.ip;

        imei = ImeiUtil.getImei(ctx);
        servieProvider = ImeiUtil.getServiceProvider(ctx);
    }

    public static long serverTime() {
        return System.currentTimeMillis() + timeOffset;
    }
    public static void initShopInfo(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            shopId = appInfo.metaData.getInt("SHOP_ID");
            shopVersionCode = appInfo.metaData.getInt("VERSION_CODE");
            promotionCode=appInfo.metaData.getString("promotionCode");
            appSerialNo=appInfo.metaData.getString("SN");
            //获取目前app的个推配置数据仅用来检测配置是否正确 无其他用途
            id = appInfo.metaData.getString("PUSH_APPID");
            key = appInfo.metaData.getString("PUSH_APPKEY");
            secret = appInfo.metaData.getString("PUSH_APPSECRET");
          //  hxInfo = HXApplication.getInstance().getHxInfo();

            //初始化H5版本号
            if(PreferUtil.getInstance().getH5VersionCode()==0){
                PreferUtil.getInstance().setH5VersionCode(appInfo.metaData.getInt("H5_VERSION_CODE"));
                PreferUtil.getInstance().setH5StyleId(appInfo.metaData.getInt("H5_STYLE_ID"));
            }

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void LoadProperty() {
        prop = new Properties();
        try {
            prop.load(AppController.getInstance().getResources()
                    .openRawResource(R.raw.config));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getConfig(String name) {
        return prop.getProperty(name);
    }

    public static int getIntConfig(String name) {
        return Integer.valueOf(prop.getProperty(name));
    }


    public static String getH5CachePath(Context context)
    {
        return "/data/data/" + context.getPackageName() + "/dist/";
    }


}
