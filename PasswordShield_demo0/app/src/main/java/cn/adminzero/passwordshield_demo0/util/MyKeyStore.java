package cn.adminzero.passwordshield_demo0.util;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class MyKeyStore {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static byte[] iv;

    /**
     * 加密秘钥文本
     * 输入待加密的alias
     * 输入待加密的文本
     * 输出加密后的文本
     */

    @RequiresApi(api = 23)
    public static String encryptKey(String alias, String textToEncrypt) {
        try {
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(alias));
            iv = cipher.getIV();
            byte[] result = cipher.doFinal(textToEncrypt.getBytes());
            return toHexString(result);
        } catch (Exception e) {
            Log.d("debug", e.getMessage());
        }
        return null;


    }

    /**
     * 解密加密后的秘钥文本
     * 输入待解密的alias
     * 输入待解密的加密文本
     * 输出解密后的文本
     */
    @RequiresApi(api = 23)
    public static String decryptKey(String alias, String textToDecrypt) {
        try {
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            final GCMParameterSpec spec = new GCMParameterSpec(128, getIv());
            cipher.init(Cipher.DECRYPT_MODE, getKey(alias), spec);
            return new String(cipher.doFinal(toByteArray(textToDecrypt)));
        } catch (Exception e) {
            Log.d("debug", "decryptKey error");
            Log.d("debug", e.getMessage());
        }
        return null;

    }

    //生成秘钥
    @RequiresApi(api = 23)
    public static void createKey(String ALIAS) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .build());

            SecretKey key = keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("debug", "秘钥生成失败");
            Log.d("debug", e.getMessage());
        }

    }

    //返回秘钥
    @RequiresApi(api = 23)
    public static SecretKey getKey(String ALIAS) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(ALIAS)) {
                createKey(ALIAS);
            }
            SecretKey key = (SecretKey) keyStore.getKey(ALIAS, null);
            return key;
        } catch (Exception e) {
            Log.d("debug", e.getMessage());
            Log.d("debug", "getKey函数出错");

        }
        return null;

    }

    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }

    public static byte[] toByteArray(String hexString) {
        if (TextUtils.isEmpty(hexString))
            throw new IllegalArgumentException("this hexString must not be empty");

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    public static byte[] getIv() {
        return iv;
    }


}
