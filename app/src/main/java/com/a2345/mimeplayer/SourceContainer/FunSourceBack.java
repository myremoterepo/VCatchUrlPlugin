package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONArray;
import org.json.JSONObject;

public class FunSourceBack extends BaseSource {
    private String fix = "http://pm.funshion.com/v7/media/play/?id=%s&cl=mweb&uc=25";
    private String fixII = "http://pv.funshion.com/v7/video/play/?id=%s&cl=mweb&uc=25";
    private String fixI = "http://jobsfe.funshion.com/play/v1/mp4/";

    public String getJsonPlayUrl(String url) {
//        Log.e("info", "sss1111-->:" + nMethod("21tlBbbw7jIBDs/2gAVF0cL+Q92qpnRTAOh8eSAd7SrPCURNUOm2orSkI="));
//        Log.e("info", "sss2222-->:" + getMethodE(nMethod("21Hq5JLnD1k6inQMicnsMbdGXufXsO42gARJLe0iKa2qqxvxwv60S/7HhZ6KHRnKHV4p83ASZ7yvM=")));
        String result = null;
        try {
            String vid = getVid(url);
            Log.i("info", "vid-->:" + vid);
            String rUrl = "http://pm.funshion.com/v5/media/play/?id=" + vid + "&isajax=1&ca=ilikefunshionapp&cl=aphone";
            result = getResult(rUrl, vid, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("info", "result-->:" + result);
        return result;
    }


    public String getJsonPlayUrlShort(String url) {
        String result = null;
        try {
            String vid = getVid(url);
            Log.i("info", "vid-->:" + vid);
            if (vid != null) {
                String rUrl = "http://pv.funshion.com/v5/video/play/?id=" + vid + "&isajax=1&ca=ilikefunshionapp&cl=aphone";
                result = getResult(rUrl, vid, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("info", "result-->:" + result);
        return result;
    }

    private String[] getToken(String code, String vid, int type) {
        String[] result = new String[3];
        String url = null;
        String token = null;
        String infohash = null;
        String filename;
        if (type == 0) {
            url = String.format(fix, vid);
        } else if (type == 1) {
            url = String.format(fixII, vid);
        }
        try {
            String htmlContent = HttpTools.getWebContent(url, null);
            JSONObject htmlObj = new JSONObject(htmlContent);
            JSONArray playlist = htmlObj.getJSONArray("playlist");
            for (int i = 0; i < playlist.length(); i++) {
                JSONObject playObj = playlist.getJSONObject(i);
                String codeIn = playObj.getString("code");
                if (codeIn.equals(code)) {
                    JSONArray tokenArr = playObj.getJSONArray("playinfo");
                    token = tokenArr.getJSONObject(0).getString("token");
                    infohash = tokenArr.getJSONObject(0).getString("infohash");
                    filename = tokenArr.getJSONObject(0).getString("filename");
                    result[0] = token;
                    result[1] = infohash;
                    result[2] = filename;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getResult(String url, String vid, int type) {
        JSONObject resultObj = new JSONObject();
        try {
            String htmlContent = HttpTools.getWebContent(url, null);
            JSONObject obj1 = new JSONObject(htmlContent);
            JSONArray array = obj1.getJSONArray("mp4");
            String code;
            String token;
            String file;
            String infohash;
            String playUrl;
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                code = obj.getString("code");
                token = getToken(code, vid, type)[0];
                infohash = getToken(code, vid, type)[1];
                file = getToken(code, vid, type)[2];
                if (infohash == null || token == null) continue;
                token = nMethod(token);
                infohash = nMethod(infohash).substring(0, 40);
                token = getMethodE(token);
                Log.i("info", "token...." + token + "...infohash..." + infohash + "....filename.." + file);
                playUrl = fixI + infohash + ".mp4?token=" + token.trim();
                if (file == null) {
                    playUrl = playUrl + "&file=" + file;
                }
                if ("tv".equals(code))
                    resultObj.put(Definition.ORIGINAL, playUrl);
                if ("dvd".equals(code))
                    resultObj.put(Definition.NORMAL, playUrl);
                if ("hd".equals(code))
                    resultObj.put(Definition.HIGH, playUrl);
                if ("sdvd".equals(code))
                    resultObj.put(Definition.HD, playUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();

    }

    private String getVid(String url) {
        String vid = PatternUtil.getValueForPattern(url, "v-(\\d+)");
        if (vid == null) {
            String htmlContent = null;
            try {
                HttpHeader header = new HttpHeader();
                header.setReferer("http://www.fun.tv/movie");
                header.setUserAgent("Mozilla/5.0 (Linux; U; en-us; KFAPWI Build/JDQ39) AppleWebKit/535.19 (KHTML, like Gecko) Silk/3.13 Safari/535.19 Silk-Accelerated=true");
                htmlContent = HttpTools.getWebContent(url, header);
                htmlContent = htmlContent.substring(htmlContent.indexOf("yesClient"), htmlContent.length() - 1);
                vid = PatternUtil.getValueForPattern(htmlContent, "data-vid=\"(\\d+)\"");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vid;
    }

    private String getMethodE(String txt) {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        int n = txt.length();
        String t = "";
        int o;
        int a;
        int r;
        for (int i = 0; n > i; ) {
            o = 255 & txt.charAt(i++);
            if (i == n) {
                t += s.charAt(o >> 2);
                t += s.charAt((3 & o) << 4);
                t += "==";
                break;
            }
            a = txt.charAt(i++);
            if (i == n) {
                t += s.charAt(o >> 2);
                t += s.charAt((3 & o) << 4 | (240 & a) >> 4);
                t += s.charAt((15 & a) << 2);
                t += "=";
                break;
            }
            r = txt.charAt(i++);
            t += s.charAt(o >> 2);
            t += s.charAt((3 & o) << 4 | (240 & a) >> 4);
            t += s.charAt((15 & a) << 2 | (192 & r) >> 6);
            t += s.charAt(63 & r);
        }
        return t;
    }


    private String nMethod(String in) {
        String t = "";
        String n = "";
        String a = "";
        if (in.length() == 28 && in.lastIndexOf("0") == in.length()) {
            t = in.substring(0, 27) + "=";
            Log.i("info", "t...." + t);
            n = iMethod(t);
            a = oMethod(n);
            return sMethod(a);
        } else {
            t = in.substring(2, in.length());
            Log.i("info", "t...." + t);
            n = iMethod(t);
            return oMethod(n);
        }
    }

    private String iMethod(String in) {
        int[] a = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};
        int s = in.length();
        String d = "";
        int t;
        int i;
        int n;
        int o;
        for (int r = 0; s > r; ) {
            do
                t = a[255 & in.charAt(r++)];
            while (s > r && -1 == t);
            if (-1 == t)
                break;
            do
                i = a[255 & in.charAt(r++)];
            while (s > r && -1 == i);
            if (-1 == i)
                break;
            d += (char) (t << 2 | (48 & i) >> 4);
            do {
                n = 255 & in.charAt(r++);
                if (61 == n)
                    return d;
                n = a[n];
            } while (s > r && -1 == n);
            if (-1 == n)
                break;
            d += (char) ((15 & i) << 4 | (60 & n) >> 2);
            do {
                o = 255 & in.charAt(r++);
                if (61 == o)
                    return d;
                o = a[o];
            } while (s > r && -1 == o);
            if (-1 == o)
                break;
            d += (char) ((3 & n) << 6 | o);
        }
        return d;
    }

    private String oMethod(String in) {
        String t = "";
        for (int i = 0; i < in.length(); ) {
            int n = in.charAt(i++);
            int o = in.charAt(i++);
            t += (char) (aMethod(n, o)[0]);
            t += (char) (aMethod(n, o)[1]);
            if (i == in.length() - 1) {
                t += (char) (in.charAt(i));
                break;
            }
        }
        return t;
    }

    private String sMethod(String in) {
        in += "";
        String n;
        String o = "";
        for (int t = 0; t < in.length(); t++) {
            n = Integer.toHexString((int) in.charAt(t)).toUpperCase();
            o += n.length() < 2 ? "0" + n : n;
        }
        return o;
    }

    private int[] aMethod(int in1, int in2) {
        int[] re = new int[2];
//        String[] i = {"ed0", "261", "cd3", "a62"};
        String[] i = {"af0", "121", "73", "8c2"};
        int n = 256;
        int[] o = {0, 0, 0, 0};
        for (int k = 0; k < i.length; k++) {
            o[Integer.parseInt(i[k].substring(i[k].length() - 1))] = Integer.parseInt(i[k].substring(0, i[k].length() - 1), 16);
        }
        re[0] = (in1 * o[0] + in2 * o[2]) % n;
        re[1] = (in1 * o[1] + in2 * o[3]) % n;
        return re;
    }
}
