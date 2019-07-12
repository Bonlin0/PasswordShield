package cn.adminzero.passwordshield_demo0.util;

import java.security.SecureRandom;

import static cn.adminzero.passwordshield_demo0.util.LogUtils.d;

public class Random {
    public static String getRandom(int numbers) {
        byte[] key_byte = new byte[numbers];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key_byte);
        d(String.valueOf(key_byte.length));
        return new String(key_byte);
    }
}
