package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

public class HuashuLiveSource extends LiveSource {

    public String getJsonPlayUrl(String url) {
        return getLiveJsonPlayUrl(url);
    }

    // return json string playUrl
    public String getLiveJsonPlayUrl(String url) {
        JSONObject resultObject = new JSONObject();
        String playUrl = "";
        if (url.contains("live.wasu.cn"))
            url = url.replace("live.wasu.cn", "www.wasu.cn/wap/live");
        try {
            String htmlContent = HttpTools.getWebContent(url, null);
            int i = htmlContent.indexOf("'vid'") + "'vid'".length();
            int j = htmlContent.indexOf("'", i) + 1;
            String vid = htmlContent.substring(j, htmlContent.indexOf("'", j));
            Log.i("info", "vid-->:" + vid);
            int m = htmlContent.indexOf("'url'") + "'url'".length();
            int n = htmlContent.indexOf("'", m) + 1;
            String urlIn = htmlContent.substring(n, htmlContent.indexOf("'", n));
            Log.i("info", "urlIn-->:" + urlIn);
            playUrl = urlIn + "?vid=" + vid + "&version=MIPlayer_V1.4.0&wsiphost=local";
            if (playUrl != null && playUrl.length() > 0) {
                resultObject.put(Definition.NORMAL, playUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("info", "resultObject-->:" + resultObject.toString());
        return resultObject.toString();

    }

}
