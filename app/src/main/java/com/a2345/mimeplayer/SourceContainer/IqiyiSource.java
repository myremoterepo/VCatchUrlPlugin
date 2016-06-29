package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fanzf on 2016/3/3.
 */
public class IqiyiSource extends BaseSource {
    @Override
    public String getJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        try {
            String player = getJSONObject(url);
            resultObj.put(Definition.NORMAL, player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }

    private static boolean isplay = false;
    private String tvid;
    private String vid;

    private String getHtml(String paramString) {
        isplay = true;
        try {
            paramString = HttpTools.getWebContent(paramString, null).trim();
            isplay = false;
            return paramString;
        } catch (Exception e) {
            e.printStackTrace();
            isplay = false;
            paramString = null;
        } finally {
            isplay = false;
        }

        return paramString;
    }

    public String getJSONObject(String paramString1) throws Exception {
        if (paramString1 == null)
            return null;
        HttpHeader header = new HttpHeader();
        header.setUserAgent("Mozilla/5.0 (Linux; Android 4.4.4; Nexus 5 Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.114 Mobile Safari/537.36");
        String htmlContent = HttpTools.getWebContent(paramString1, header);
        String tvid = PatternUtil.getValueForPattern(htmlContent, "Q.PageInfo.playInfo.tvid = \"(.+?)\";");
        if (tvid == null)
            tvid = PatternUtil.getValueForPattern(htmlContent, "\"tvid\" : \"(.+?)\",");
        String vid = PatternUtil.getValueForPattern(htmlContent, "Q.PageInfo.playInfo.vid = \"(.+?)\";");
        if (vid == null)
            vid = PatternUtil.getValueForPattern(htmlContent, "\"vid\" : \"(.+?)\",");
        this.tvid = tvid;
        this.vid = vid;
        htmlContent = HttpTools.getWebContent("http://cache.video.qiyi.com/vp/" + tvid + "/" + vid + "/", header);
        Log.i("info", "htmlContent1-->:" + htmlContent);
        if (htmlContent.contains("\"tkl\":[")) {
            JSONObject htmlObj = new JSONObject(htmlContent);
            if (htmlObj.has("tkl")) {
                JSONArray tkl = htmlObj.getJSONArray("tkl");
                String url = getSource(tkl);
                return url;
            }
        }
        return null;
    }

    private String getSource(JSONArray tkl) {
        String playUrl = null;
        for (int i = 0; i < tkl.length(); i++) {
            JSONObject tklObj = null;
            try {
                tklObj = tkl.getJSONObject(i);
                if (tklObj.has("vs")) {
                    JSONArray vs = tklObj.getJSONArray("vs");
                    for (int j = 0; j < vs.length(); j++) {
                        JSONObject vsObj = vs.getJSONObject(j);
                        if (vsObj.has("m3u8Url")) {
                            String m3u8Url = vsObj.getString("m3u8Url");
                            playUrl = "http://cache.m.iqiyi.com/dc/dt/-0-f45bc84a7ea643209b29a72b0c1e385f/text/c0f2ab25x11111111x777a6171" + m3u8Url;
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return playUrl;
    }

    @Override
    public String getJsonPlayUrlShort(String url) {
        return getJsonPlayUrl(url);
    }
}

