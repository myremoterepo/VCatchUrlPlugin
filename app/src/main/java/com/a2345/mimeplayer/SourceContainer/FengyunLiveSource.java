package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

public class FengyunLiveSource extends LiveSource {

    public String getLiveJsonPlayUrl(String url) {
        if (!url.contains("www.fengyunzhibo.com"))
            return null;
        url = url.replace("www.fengyunzhibo.com", "m.fengyunzhibo.com");
        Log.i("info", "url-->:" + url);
        JSONObject resultObj = new JSONObject();
        String mPlayUrl = null;
        try {
            String webPageContent = HttpTools.getWebContent(url, null);
            String mData = PatternUtil.getValueForPattern(webPageContent, "data=");
            Log.i("info", "data-->:" + mData);
            if (mData == null || mData.length() < 1) {
                Log.i("info", "data is null");
                return null;
            }
            mPlayUrl = fengyunEncode(mData.substring(Integer.parseInt(mData.substring(0, 1)) + 1));
            if (mPlayUrl == null || mPlayUrl.length() < 1) {
                Log.i("info", "encode eorro");
                return null;
            }
            resultObj.put(Definition.NORMAL, mPlayUrl);
            Log.i("info", "mPlayUrl-->:" + mPlayUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }

    private String fengyunEncode(String code) {
        int[] fengyunDecodeChars = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};
        int c1, c2, c3, c4;
        int i, len;
        String out;
        len = code.length();
        i = 0;
        out = "";
        while (i < len) {
            do {
                c1 = fengyunDecodeChars[code.charAt(i++) & 0xff];
            }
            while (i < len && c1 == -1);
            if (c1 == -1) {
                break;
            }
            do {
                c2 = fengyunDecodeChars[code.charAt(i++) & 0xff];
            }
            while (i < len && c2 == -1);
            if (c2 == -1) {
                break;
            }
            out = out + (char) ((c1 << 2) | ((c2 & 0x30) >> 4));
            do {
                c3 = code.charAt(i++) & 0xff;
                if (c3 == 61) {
                    return out;
                }
                c3 = fengyunDecodeChars[c3];
            }
            while (i < len && c3 == -1);
            if (c3 == -1) {
                break;
            }
            out = out + (char) (((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));
            do {
                c4 = code.charAt(i++) & 0xff;
                if (c4 == 61) {
                    return out;
                }
                c4 = fengyunDecodeChars[c4];
            }
            while (i < len && c4 == -1);
            if (c4 == -1) {
                break;
            }
            out = out + (char) ((((c3 & 0x03) << 6) | c4));
        }
        return out;
    }
}
