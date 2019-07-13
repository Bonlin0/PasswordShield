package cn.adminzero.passwordshield_demo0;

import static cn.adminzero.passwordshield_demo0.util.AESUtils.aesDecodeStr;
import static cn.adminzero.passwordshield_demo0.util.AESUtils.aesEncryptStr;

/**
 * 加密模块的封装
 */
public class Encryption {
    /**
     * 加密函数封装:进行AES的加密
     * 输入:content 待加密的String串
     * 输出:加密之后的字符串
     */
    public static String encode(String contnet) {
        String key = getKey();//秘钥
        return aesEncryptStr(contnet, key);
    }

    /**
     * 解密函数封装:进行AES的解密
     * 输入:content 待解密的String串
     * 输出:解密之后的字符串
     */
    public static String decode(String content) {
        String key = getKey();//秘钥
        return aesDecodeStr(content, key);
    }

    /**
     * 获取秘钥KeyStore
     */
    public static String getKey() {
        return "key";
    }

}

