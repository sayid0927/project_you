
package com.yintong.secure.demo.env;

public class EnvConstants {
    private EnvConstants() {
    }

    /**
     * TODO 商户号，商户MD5 key 配置。本测试Demo里的“PARTNER”；强烈建议将私钥配置到服务器上，以免泄露。“MD5_KEY”字段均为测试字段。正式接入需要填写商户自己的字段
     */
    public static final String PARTNER_PREAUTH = "201504071000272504"; // 短信

    public static final String MD5_KEY_PREAUTH = "201504071000272504_test_20150417";

    public static final String PARTNER = "201505251000339502";

    public static final String MD5_KEY = "201408071000001546_test_20140815";

    // 商户（RSA）私钥 TODO 强烈建议将私钥配置到服务器上，否则有安全隐患
    // public static final String RSA_PRIVATE =
    // "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOilN4tR7HpNYvSBra/DzebemoAiGtGeaxa+qebx/O2YAdUFPI+xTKTX2ETyqSzGfbxXpmSax7tXOdoa3uyaFnhKRGRvLdq1kTSTu7q5s6gTryxVH2m62Py8Pw0sKcuuV0CxtxkrxUzGQN+QSxf+TyNAv5rYi/ayvsDgWdB3cRqbAgMBAAECgYEAj02d/jqTcO6UQspSY484GLsL7luTq4Vqr5L4cyKiSvQ0RLQ6DsUG0g+Gz0muPb9ymf5fp17UIyjioN+ma5WquncHGm6ElIuRv2jYbGOnl9q2cMyNsAZCiSWfR++op+6UZbzpoNDiYzeKbNUz6L1fJjzCt52w/RbkDncJd2mVDRkCQQD/Uz3QnrWfCeWmBbsAZVoM57n01k7hyLWmDMYoKh8vnzKjrWScDkaQ6qGTbPVL3x0EBoxgb/smnT6/A5XyB9bvAkEA6UKhP1KLi/ImaLFUgLvEvmbUrpzY2I1+jgdsoj9Bm4a8K+KROsnNAIvRsKNgJPWd64uuQntUFPKkcyfBV1MXFQJBAJGs3Mf6xYVIEE75VgiTyx0x2VdoLvmDmqBzCVxBLCnvmuToOU8QlhJ4zFdhA1OWqOdzFQSw34rYjMRPN24wKuECQEqpYhVzpWkA9BxUjli6QUo0feT6HUqLV7O8WqBAIQ7X/IkLdzLa/vwqxM6GLLMHzylixz9OXGZsGAkn83GxDdUCQA9+pQOitY0WranUHeZFKWAHZszSjtbe6wDAdiKdXCfig0/rOdxAODCbQrQs7PYy1ed8DuVQlHPwRGtokVGHATU=";
    public static final String RSA_PRIVATE =
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOx3lgfN5cbeAbYmbKqzfQUcQKutEJpMuMt8QcsDtTbPuU5Ftd+FUQAiZjp4U83oSTfhms8dQwTJrRXEkXaBJUB2NY2BXCE/qY/k3Z9IEh9tpxc+8tAOiHaPPPRxcpj/ppCegYW9BltND7uaIfUHRcPMX/kKsGsiSA1pDv3p6Q9pAgMBAAECgYAqvQ5zyKYABH4gst8vFxPAibOyk6wNh1JbDNFDZR8qke63E+hkRs82DAGYDclvzMz2+aKmTm6ffef3qDh2R684l3SDOLbHSKQKQeKn01xbBZHQ6IqS7jY7bpL+0Qhd8IXfQIuiImTTqNsngc6kO7wrAaV40j8Y6Hwbf5BGbPY+JQJBAPa4NT1mdp1oCOrLsbD1w8Qweq68GrEeorQgx/yGjN1y6yztcKx6UFPZWEgTy0SPnSknYK/qJXPUbVmxccz/KTcCQQD1XKbuF9uIqObqk2SnNUplK92AS+ZuDS64ESSRS5jKQ7sXIfQgaVUAzW4KiU9sS80qdjPGr/yCtSeZXbhvX1xfAkEAoDiAWp9v6EjngZNGkeUIfR/+i/scWmnKv6+KMDQwxp8amtKXmWrVP56l9ijkmGGrbk5kO9mS+OW7HcReYwJRgQJAQJjCAhEZ5SLCmKNxbmwjR/uCd1KEOhkSYbdxryb99NJcITz5LsdMb8el3vRDirlyLGmuO/L9QdQ7tq7r7bZndQJAZdpW052szlZdEaHcu4v/Hormq9rwabn5d0oLERppAs+im+h2cRkvgWEQlVf2ltteAgcJT+0bBOllufIIGF26Hw==";

    // 银通支付（RSA）公钥
    public static final String RSA_YT_PUBLIC =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";

}
