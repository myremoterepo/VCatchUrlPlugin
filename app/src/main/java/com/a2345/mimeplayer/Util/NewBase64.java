package com.a2345.mimeplayer.Util;

/**
 * Created by Administrator on 2015/11/24.
 */
public class NewBase64 {
    public static String base64encode(String e) {
        int n = 0;
        int h = 0;
        int c = 0;
        int r = 0;
        int a = e.length();
        String A = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        StringBuffer buf = new StringBuffer();
        for (r = 0; r < a;) {
            c = 255 & e.charAt(r++);
            if (r == a) {
                buf.append(A.charAt(c >> 2));
                buf.append(A.charAt((3 & c) << 4));
                buf.append("==");
                break;
            }

            h = e.charAt(r++);
            if (r == a) {
                buf.append(A.charAt(c >> 2));
                buf.append(A.charAt((3 & c) << 4 | (240 & h) >> 4));
                buf.append(A.charAt((15 & h) << 2));
                buf.append("=");
                break;
            }
            n = e.charAt(r++);
            buf.append(A.charAt(c >> 2));
            buf.append(A.charAt((3 & c) << 4 | (240 & h) >> 4));
            buf.append(A.charAt((15 & h) << 2 | (192 & n) >> 6));
            buf.append(A.charAt(63 & n));
        }
        return buf.toString();
    }

    public static String base64decode(String e) {
        int o = 0;
        int r = 0;
        int s = 0;
        int d = 0;
        int t = 0;
        int i = 0;
        int a = 0;
        String n = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        StringBuffer buf = new StringBuffer();
        int p = 0;
        e = e.replaceAll("[^0-9a-zA-Z+/=]", "");
        for (p = 0; p < e.length();) {
            o = n.indexOf(e.charAt(p++));
            r = n.indexOf(e.charAt(p++));
            s = n.indexOf(e.charAt(p++));
            d = n.indexOf(e.charAt(p++));
            t = o << 2 | r >> 4;
            i = (15 & r) << 4 | s >> 2;
            a = (3 & s) << 6 | d;
            buf.append((char) (t));
            if (64 != s) {
                buf.append((char) i);
            }
            if (64 != d) {
                buf.append((char) a);
            }
        }
        return utf8Decode(buf.toString());
    }

    public static String utf8Decode(String e) {
        StringBuffer buf = new StringBuffer();
        int a = 0;
        int c2 = 0;
        int c3 = 0;
        for (int i = 0; i < e.length();) {
            a = e.charAt(i);
            if (128 > a) {
                buf.append((char) a);
                i++;
            } else {
                if (a > 191 && 224 > a) {
                    c2 = e.charAt(i + 1);
                    buf.append((char) ((31 & a) << 6 | 63 & c2));
                    i += 2;
                } else {
                    c2 = e.charAt(i + 1);
                    c3 = e.charAt(i + 2);
                    buf.append((char) ((15 & a) << 12 | (63 & c2) << 6 | 63 & c3));
                    i += 3;
                }
            }
        }
        return buf.toString();
    }
}
