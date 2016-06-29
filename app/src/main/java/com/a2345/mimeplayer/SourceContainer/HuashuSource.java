package com.a2345.mimeplayer.SourceContainer;

import android.provider.Settings;
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
            if (url.contains("http://www.wasu.cn/wap")) {
                url = url.replace("http://www.wasu.cn/wap", "http://www.wasu.cn/");
            }
            Log.i("info", "url-->:" + url);
            String content = HttpTools.getWebContent(url, null);
            Log.i("info", "content-->:" + content);
            JSONObject localObject = new JSONObject("{" + content.substring(content.indexOf("_playId"), content.indexOf("playKeyword") - 2) + "}");
            String id = localObject.getString("_playId");
            String urlIn = localObject.getString("_playUrl");
            String key = localObject.getString("_playKey");
            String category = localObject.getString("_playCategory");
            url = String.format("http://www.wasu.cn/wap/Api/getVideoUrl/id/%s/key/%s/url/%s=", new Object[]{id, key, urlIn});
            content = HttpTools.getWebContent(url, null);
            playUrl = content.substring(content.indexOf("http"), content.indexOf("mp4") + 3) + "?vid=" + id + "&cid=" + category + "&version=PadPlayer_V1.4.0";
            Log.i("info", playUrl);
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
}
