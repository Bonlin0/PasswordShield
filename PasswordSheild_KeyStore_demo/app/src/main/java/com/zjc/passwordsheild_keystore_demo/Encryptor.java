package com.zjc.passwordsheild_keystore_demo;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static com.zjc.passwordsheild_keystore_demo.util.UtilLog.d;

/**
 * Created by erfli on 2/24/17.
 */

class Encryptor {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String Alias = "PasswordSheild";

    Encryptor() {

    }

    public static boolean containsAlias(KeyStore keyStore, String alias) throws KeyStoreException {
        return keyStore.containsAlias(alias);
    }

    //加密函数  返回加密的字节串
    @RequiresApi(api = Build.VERSION_CODES.M)
    byte[] encryptText(final String textToEncrypt)
            throws NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException {

        //Returns a Cipher object that implements the specified transformation.
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        //依照加密方式初始化ciper--使用getSecretKey获取加密的秘钥
        //initializes this cipher with a key.....
        //init(int opmode, java.security.Key key)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());


        //Encrypts or decrypts data in a single-part operation, or finishes a multiple-part operation.
        return (cipher.doFinal(textToEncrypt.getBytes("UTF-8")));
    }

    //获取秘钥 作为解密的init(opmode,key)的key参数
    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    private SecretKey getSecretKey() throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

        keyGenerator.init(
                new KeyGenParameterSpec.Builder(Alias,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build());
        SecretKey key = keyGenerator.generateKey();
        d(key.toString());


        //添加基本的信息: setUserAuthenticationRequired(true)  用户基本的信息
        //keyGenerator.init(256);
        return key;
    }
}
