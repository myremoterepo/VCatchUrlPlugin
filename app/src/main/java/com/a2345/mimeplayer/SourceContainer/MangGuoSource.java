package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONArray;
import org.json.JSONObject;

public class MangGuoSource extends BaseSource {//只有长视频
    private static final String fixI = "http://pad.api.hunantv.com/video/getById?appVersion=3.1.1&device=iPad&osType=ios&osVersion=7.1.1&ticket=&videoId=";

    public String getJsonPlayUrl(String url) {
        JSONObject resultObject = new JSONObject();
        String id = url.substring(url.lastIndexOf("/")).replaceAll("[^0-9]", "");
        try {
            String htmlContent = HttpTools.getWebContent(fixI + id, null);
            JSONObject htmlObj = new JSONObject(htmlContent);
            JSONArray videoArr = htmlObj.getJSONObject("data").getJSONArray("videoSources");

            for (int i = 0; i < videoArr.length(); i++) {
                JSONObject itemObj = videoArr.getJSONObject(i);
                if (itemObj.has("url") && itemObj.getString("url") != null) {
                    String videoUrl = itemObj.getString("url");
                    if (videoUrl.startsWith("http")) {
                        String playUrl = getYunUrl(videoUrl);
                        int definition = itemObj.getInt("definition");
                        if (definition == 0) {
                            if (playUrl != null && playUrl.startsWith("http"))
                                resultObject.put(Definition.ORIGINAL, playUrl);
                        }
                        if (definition == 1) {
                            if (playUrl != null && playUrl.startsWith("http"))
                                resultObject.put(Definition.NORMAL, playUrl);
                        }
                        if (definition == 2) {
                            if (playUrl != null && playUrl.startsWith("http"))
                                resultObject.put(Definition.HIGH, playUrl);
                        }
                        if (definition == 3) {
                            if (playUrl != null && playUrl.startsWith("http"))
                                resultObject.put(Definition.SUPER, playUrl);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("info", "playUrl-->:" + resultObject.toString());
        return resultObject.toString();
    }

    private String getYunUrl(String videoUrl) {
        String playUrl = null;
        try {
            String htmlContent = HttpTools.getWebContent(videoUrl, null);
            JSONObject htmlObj = new JSONObject(htmlContent);
            if (htmlObj.has("info")) {
                playUrl = htmlObj.getString("info");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playUrl;
    }
}
