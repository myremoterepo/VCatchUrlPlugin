package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

public class Ku6Source extends BaseSource {
    // return a string playUrl
    public String getJsonPlayUrlShort(String url) {
        String playUrl;
        JSONObject resultObj = new JSONObject();
        try {
            if (url != null) {
                String vid = PatternUtil.getValueForPattern(url.substring(url.lastIndexOf("/")), "/(.+).html");
                Log.e("fan", "vid.." + vid);
                String str2 = "http://v.ku6.com/fetch.htm?t=getVideo4Player&vid=";
                String str3 = "&stype=mp4&cb=jQuery191011046156589873135_1427248273399";
                long l = System.currentTimeMillis();
                String str4 = str2 + vid + str3 + "&_=" + l;
                String str5 = HttpTools.getWebContent(str4, null);
                int j = str5.indexOf("(");
                int j1 = j + 1;
                int j2 = str5.indexOf(")", j1);
                String ku6jQueryString = str5.substring(j1, j2);
                Log.i("info", "ku6jQueryString-->:" + ku6jQueryString);
                JSONObject ku6jQueryJSON = new JSONObject(ku6jQueryString);
                JSONObject data = ku6jQueryJSON.getJSONObject("data");
                playUrl = data.getString("f");
                if (playUrl.startsWith("http")) {
                    resultObj.put(Definition.NORMAL, playUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("TAG", "--playUrl-->:" + resultObj.toString());
        return resultObj.toString();
    }
}
