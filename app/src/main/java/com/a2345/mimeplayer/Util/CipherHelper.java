package com.a2345.mimeplayer.Util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by fanzf on 2016/6/28.
 */
public class CipherHelper {
    private static final String TAG = "CipherHelper";
    private static String hexString = "0123456789ABCDEF";

    public CipherHelper() {
    }

    public static String encryptMD5(String key) {
        String cacheKey;
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(key.getBytes());
            cacheKey = bytesToHexString(e.digest());
        } catch (NoSuchAlgorithmException var3) {
            cacheKey = String.valueOf(key.hashCode());
        }

        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < bytes.length; ++i) {
            String hex = Integer.toHexString(255 & bytes[i]);
            if(hex.length() == 1) {
                sb.append('0');
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    public static byte[] hexString2Bytes(String s) {
        s = s.toUpperCase();
        byte[] bytes = new byte[s.length() / 2];

        for(int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte)(hexString.indexOf(s.charAt(i * 2)) << 4 | hexString.indexOf(s.charAt(i * 2 + 1)));
        }

        return bytes;
    }

    public static byte[] decryptAES128(byte[] data, byte[] key, byte[] iv) {
        try {
            IvParameterSpec e = new IvParameterSpec(iv);
            SecretKeySpec _key = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(2, _key, e);
            byte[] decryptedData = cipher.doFinal(data);
            return decryptedData;
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static byte[] decryptDES(String data, String key) {
        return decryptDES(hexString2Bytes(data), key);
    }

    public static byte[] decryptDES(byte[] data, String key) {
        try {
            SecureRandom e = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(2, securekey, e);
            byte[] result = cipher.doFinal(data);
            Log.i("CipherHelper", "from [" + bytesToHexString(data) + "] to [" + new String(result) + "]");
            return hexString2Bytes(new String(result));
        } catch (Throwable var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static String encryptDES(String data, String key) {
        try {
            SecureRandom e = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, e);
            byte[] result = cipher.doFinal(data.getBytes());
            Log.i("CipherHelper", "from [" + data + "] to [" + bytesToHexString(result).toLowerCase() + "]");
            return bytesToHexString(result);
        } catch (Throwable var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static String generateCDNToken() {
        String uid = "android_test";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
        Log.i("CipherHelper", timestamp);
        return encryptDES(uid + "_" + timestamp, "BestV_+8");
    }

    public static byte[] hmacsha256(byte[] content, byte[] key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(content);
    }

    public static String UrlEnc(String s) {
        String res = "";

        try {
            res = URLEncoder.encode(s, "utf-8");
        } catch (UnsupportedEncodingException var3) {
            res = "";
        }

        return res;
    }
}
