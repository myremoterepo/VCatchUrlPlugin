package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

public class HuashuSource extends BaseSource {

    //return a json string playUrl
    public String getJsonPlayUrl(String url) {
        String playUrl = "";
        JSONObject resultObj = new JSONObject();
        try {
            if (!url.contains("/wap/")) {
                url = url.replace("http://www.wasu.cn/", "http://www.wasu.cn/wap/");
            }
            Log.i("info", "url-->:" + url);
            String content = HttpTools.getWebContent(url, null);
            int i1 = content.indexOf("videoInfo =");
            int i2 = content.indexOf("}", i1);
            String videoInfo = content.substring(i1, i2);
            String vid = getValue(videoInfo, "vid");

            String key = getValue(videoInfo, "key");
            String _url = getValue(videoInfo, "url");
            Log.e("fan", "vid.." + vid + "\nkey.." + key + "\n_url.." + _url);
            String tmpUrl = "http://apiontime.wasu.cn/Auth/getVideoUrl?id=%1$s&key=%2$s&url=%3$s&type=jsonp&callback=?";
            tmpUrl = String.format(tmpUrl, vid, key, _url);
            Log.e("fan", "tmpUrl.." + tmpUrl);
            content = HttpTools.getWebContent(tmpUrl, null);
            Log.e("fan", "content.." + content);
            int j = content.indexOf("\"") + 1;
            playUrl =content.substring(j, content.indexOf("\"", j)).replace("\\", "");
            Log.e("fan", "playUrl.." + playUrl);
            resultObj.put(Definition.NORMAL, playUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("info", "playUrl-->:" + resultObj.toString());

        return resultObj.toString();
    }

    public String getJsonPlayUrlShort(String url) {
        return getJsonPlayUrl(url);
    }

    private String getValue(String content, String key) {
        int i1 = content.indexOf(key);
        int i2 = content.indexOf(":", i1);
        int i3 = content.indexOf("'", i2) + 1;
        int i4 = content.indexOf("'", i3);
        return content.substring(i3, i4);

    }
}
