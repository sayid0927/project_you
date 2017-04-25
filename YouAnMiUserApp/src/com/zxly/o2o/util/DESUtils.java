/*
 * 文件名：DESUtils.java
 * 版权：深圳柚安米科技有限公司版权所有
 * 修改人：刘红艳
 * 修改时间：2016年6月12日
 * 修改内容：新增
 */
package com.zxly.o2o.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

//import org.apache.commons.codec.binary.Base64;


/**
 * DES加密工具类。
 * <p>
 * 
 * @author 刘红艳
 * @since 2.2.4
 */
public class DESUtils {

    // 算法名称
    public static final String KEY_ALGORITHM = "DES";
    // 算法名称/加密模式/填充方式
    // DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
    public static final String CIPHER_ALGORITHM = "DES/ECB/NoPadding";


    /**
     * 
     * 生成密钥key对象
     * 
     * @param keyStr
     *            密钥字符串
     * @return 密钥对象
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Exception
     */
    private static SecretKey keyGenerator(String keyStr) throws Exception {
        byte input[] = keyStr.getBytes();
        DESKeySpec desKey = new DESKeySpec(input);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        return securekey;
    }


    /**
     * 加密数据
     * 
     * @param data
     *            待加密数据
     * @param key
     *            密钥
     * @return 加密后的数据
     */
    public static String encrypt(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);
        // 实例化Cipher对象，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecureRandom random = new SecureRandom();
        // 初始化Cipher对象，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, deskey, random);
        byte[] results = cipher.doFinal(data.getBytes());
        /*
         * //
         * 该部分是为了与加解密在线测试网站（http://tripledes.online-domain-tools.com/）的十六进制结果进行核对
         * System.out.println("siez:" + results.length); for (int i = 0; i <
         * results.length; i++) { System.out.print(results[i] + " "); }
         * System.out.println();
         */
        // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
//        return Base64.encodeBase64String(results);
        return Base64.encode(results);
    }


    /**
     * 解密数据
     * 
     * @param data
     *            待解密数据
     * @param key
     *            密钥
     * @return 解密后的数据
     */
    public static String decrypt(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化Cipher对象，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        // 执行解密操作
//        return new String(cipher.doFinal(Base64.decodeBase64(data)));
        return new String(cipher.doFinal(Base64.decode(data)));
    }


    public static void main(String[] args) throws Exception {
        String source = "abcdefghabcdefghabcdefghabcdefgh";
        System.out.println("原文: " + source);
        String key = "asdfghjk";// 8位的密码
        String encryptData = encrypt(source, key);
        System.out.println("加密后: " + encryptData);
        String decryptData = decrypt(encryptData, key);
        System.out.println("解密后: " + decryptData);
    }
}
