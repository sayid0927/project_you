package com.zxly.o2o.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * 文件名：EncryptionUtils.java
 * 版权：Copyright 2014 Yiba Tech. Co. Ltd. All Rights Reserved. 
 * 描述： EncryptionUtils.java
 * 修改人：刘红艳
 * 修改时间：2014年12月22日
 * 修改内容：新增
 */

/**
 * TODO 数据加解密工具类。
 * <p>
 * TODO 
 * <p>
 * TODO 
 * <pre>
 * </pre>
 * 
 * @author     刘红艳
 * @version    YIBA-O2O 2014年12月22日
 * @since      YIBA-O2O
 */
public class EncryptionUtils
{
    /**明文密码最大长度*/
    private static final int PWD_PLAINTEXT_MAX_LENGTH = 16;
    
    /**密码加密次数*/
    private static final int PWD_ENCRYPT_TIME = 2;
    
    /**
     * 32位MD5加密大写
     * @param source
     * @return
     */
    public static String md5(String source)
    {
        StringBuffer buf = new StringBuffer("");
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++)
            {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return buf.toString().toUpperCase();
        
    }
    
    /***
     * 
     * TODO APP端传输加密密码。
     * 
     * @param data 明文
     * @return 密文
     */
    public static String md5TransferPwd(String data)
    {
        return md5OneTime(data);
    }
    
    /**
     * 
     * TODO APP端注册用户密码加密方式。
     * 
     * @param data 传输的密文
     * @return 加密后的入库密文
     */
    public static String md5SavePwd(String data)
    {
        return md5OneTime(data);
    }
    
    /**
     * 
     * TODO 商户网站密码加密，区分大小写。
     * 
     * @param data 明文 
     * @return 密文
     */
    public static String encryptionPwd(String data)
    {
        String cryptograph = data;
        for (int i = 0; i < PWD_ENCRYPT_TIME; i++)
        {
            cryptograph = md5OneTime(cryptograph);
        }
        return cryptograph;
    }
    
    private static String md5OneTime(String data)
    {
        
        if (StringUtil.isNull(data))
        {
            return null;
        }
        String cryptograph = data;
        if (data.length() > PWD_PLAINTEXT_MAX_LENGTH)
        {
            cryptograph = data.substring(0, PWD_PLAINTEXT_MAX_LENGTH);
        }
        cryptograph = md5(cryptograph);
        return cryptograph;
        
    }

    public static String md5Pwd(String source) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte[] b = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }


    /**
     * 32位MD5加密小写
     *
     * @param source
     * @return
     */
    public static String md5Lower(String source) {
        return md5Pwd(source).toLowerCase();
    }

    /**
     * 和前端MD5加密后保存到数据库
     *
     * @return
     */
    public static String MD5Swap(String source) {
        String target = md5Lower(source);
        int indx = target.length()/2;
        target = target.substring(indx) + target.substring(0, indx);
        return target;
    }

}
