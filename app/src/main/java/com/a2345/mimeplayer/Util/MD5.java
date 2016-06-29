package com.a2345.mimeplayer.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fanzf on 2015/11/26.
 */
public class MD5 {
    public static String convert(String paramString) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(paramString.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static final String getString(String paramString) {
        try {
            char[] arrayOfChar1 = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
            byte[] arrayOfByte1 = paramString.getBytes();
            try {
                MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
                localMessageDigest.update(arrayOfByte1);
                byte[] arrayOfByte2 = localMessageDigest.digest();
                int i = arrayOfByte2.length;
                char[] arrayOfChar2 = new char[i * 2];
                int k = 0;
                for (int j = 0; j < i; j++) {
                    int m = arrayOfByte2[j];
                    int n = k + 1;
                    arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
                    k = n + 1;
                    arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
                }
                return new String(arrayOfChar2);
            } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
                while (true) {
                    localNoSuchAlgorithmException.printStackTrace();
                    String str = null;
                }
            }
        } finally {
        }
    }
}
