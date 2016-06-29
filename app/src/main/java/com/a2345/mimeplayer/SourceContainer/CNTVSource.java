package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by fanzf on 2016/2/29.
 */
public class CNTVSource extends BaseSource {

    public String getJsonPlayUrl(String url) {

        JSONObject resultObj = new JSONObject();
        try {
            String str1 = getVideoPlayUrl(url);
            Log.i("info", "str1-->:" + str1);
            if (str1 != null) {
                resultObj.put(Definition.NORMAL, str1);
                return resultObj.toString();
            }
            String str2 = getSecond(url);
            Log.i("info", "str2-->:" + str1);
            if (str2 != null) {
                resultObj.put(Definition.NORMAL, str2);
                return resultObj.toString();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getSecond(String paramString)
            throws Exception {
        String str2 = HttpTools.getWebContent(paramString, null);
        String str3 = null;
        if (str2.contains("\"videoCenterId\",")) {
            String str7 = str2.substring(str2.indexOf("\"videoCenterId\",") + "\"videoCenterId\",".length(), str2.length());
            str3 = str7.substring(0, str7.indexOf(");")).replaceAll("\"", "").replaceAll(" ", "");
        }
        if (str2.contains("videoCenterId: ")) {
            String str6 = str2.substring(str2.indexOf("videoCenterId: ") + "videoCenterId: ".length());
            str3 = str6.substring(0, str6.indexOf("',")).replaceAll("'", "");
        }
        if ((str3 == null) || (str3.trim().length() == 0))
            return null;
        String str4 = HttpTools.getWebContent("http://vdn.apps.cntv.cn/api/getIpadVideoInfo.do?pid=" + str3 + "&tai=ipad", null);
        int i = str4.indexOf("'", str4.indexOf("html5VideoData"));
        int j = str4.lastIndexOf("'", str4.lastIndexOf("getHtml5VideoData"));
        JSONObject localJSONObject2 = new JSONObject(str4.substring(i + 1, j));
        if (localJSONObject2.has("hls_url")) {
            String str5 = localJSONObject2.getString("hls_url");
            if ((str5.length() == 0) && (localJSONObject2.has("video"))) {
                JSONObject localJSONObject4 = localJSONObject2.getJSONObject("video");
                if (localJSONObject4.has("chapters")) {
                    JSONArray localJSONArray5 = localJSONObject4.getJSONArray("chapters");
                    if (localJSONArray5.length() == 1)
                        str5 = localJSONArray5.getJSONObject(0).getString("url");
                }
            }
            if (str5.length() == 0)
                return null;
            return str5;
        }
        return null;
    }

    public String getVideoPlayUrl(String url)
            throws Exception {
        String webContent = HttpTools.getWebContent(url, null);
        String id = PatternUtil.getValueForPattern(webContent, "\"videoCenterId\",\"(.+?)\"");
        url = String.format("http://vdn.apps.cntv.cn/api/getHttpVideoInfo.do?pid=%s&tz=-8&from=000tv&url=%s&idl=32&idlr=32&modifyed=false", new Object[]{id, url});
        webContent = HttpTools.getWebContent(url, null);
        url = PatternUtil.getValueForPattern(webContent, "\"hls_url\":\"(.+?)\"");
        return url;
    }

    public String getJsonPlayUrlShort(String url) {
        return null;
    }
}