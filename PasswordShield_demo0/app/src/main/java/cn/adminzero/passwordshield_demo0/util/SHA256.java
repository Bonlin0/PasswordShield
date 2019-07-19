package cn.adminzero.passwordshield_demo0.util;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    /***
     * 利用Apache的工具类实现SHA-256加密
     * @param str 加密后的报文

     * @return

     */

    public static String Sha256(String str) {

        MessageDigest messageDigest;

        String encdeStr = "";

        try {

            messageDigest = MessageDigest.getInstance("SHA-256");

            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));

            encdeStr = new String(Hex.encodeHexString(hash));

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        return encdeStr.substring(0,32);

    }

    public static String Sha512(String str) {

        MessageDigest messageDigest;

        String encdeStr = "";

        try {

            messageDigest = MessageDigest.getInstance("SHA-256");

            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));

            encdeStr = new String(Hex.encodeHexString(hash));

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        return encdeStr.substring(3,59);

    }



}