package cn.adminzero.passwordshield_demo0.util;

import androidx.annotation.RequiresApi;

import cn.adminzero.passwordshield_demo0.MyApplication;

import static cn.adminzero.passwordshield_demo0.util.AESUtils.aesDecodeStr;
import static cn.adminzero.passwordshield_demo0.util.AESUtils.aesEncryptStr;

/**
 * 加密模块的封装
 */
public class Encryption {
    private MyStorage myStorage;
    private String key;

    /**
     * 加密函数封装:进行AES的加密
     * 输入:content 待加密的String串
     * 输出:加密之后的字符串
     */
    public Encryption() {
        myStorage = new MyStorage();
        key = getKey();
    }

    public String encode(String contnet) {
        return aesEncryptStr(contnet, key);
    }

    /**
     * 解密函数封装:进行AES的解密
     * 输入:content 待解密的String串
     * 输出:解密之后的字符串
     */

    public String decode(String content) {
        return aesDecodeStr(content, key);
    }

    /**
     * 获取秘钥KeyStore
     */
    @RequiresApi(api = 23)
    private String getKey() {
        String pKey = myStorage.getData(MyApplication.KEY);
        pKey = MyKeyStore.decryptKey(pKey);
        return pKey;

    }

}

