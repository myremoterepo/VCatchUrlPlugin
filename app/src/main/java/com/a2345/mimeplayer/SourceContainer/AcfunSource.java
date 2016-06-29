package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by fanzf on 2015/12/16.
 */
public class AcfunSource extends BaseSource {
    private final String FIX = "http://api.acfun.tv/apiserver/content/info?contentId=";
    private final String CHANNELID = "ac([0-9]+)";
    private final String KUID = "\"danmakuId\":([0-9]+),";
    private final String ORINGIN = "http://m.acfun.tv";
    private final String REFERER = "http://m.acfun.tv/ykplayer?date=undefined";
    private final String USERAGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36";
    private final String TYPE = "2";
    private final String HOST = "api.aixifan.com";

    public String getJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        String tmpUrl, webPageContent, playUrl;
        String channelId;

        channelId = PatternUtil.getValueForPattern(url, CHANNELID);
        if (null == channelId)
            return null;
        Log.i("info", "channelId-->:" + channelId);
        tmpUrl = FIX + channelId;
        try {
            webPageContent = HttpTools.getWebContent(tmpUrl, null);
            if (null == webPageContent)
                return null;
            String kuId = PatternUtil.getValueForPattern(webPageContent, KUID);
            if(null == kuId)
                return null;
            Log.i("info", "kuId-->:" + kuId);
            tmpUrl = "http://api.aixifan.com/plays/" + kuId + "/realSource";
            HttpHeader header = new HttpHeader();
            header.setOrigin(ORINGIN);
            header.setDeviceType(TYPE);
            header.setReferer(REFERER);
            header.setUserAgent(USERAGENT);
            header.setHost(HOST);
            webPageContent = HttpTools.getWebContent(tmpUrl, header);
            if (null == webPageContent)
                return null;
            Log.i("info", "webPageContent-->:" + webPageContent);
            JSONObject tmpObj = new JSONObject(webPageContent);
            if (!tmpObj.has("data") || !tmpObj.getJSONObject("data").has("files"))
                return null;
            JSONArray files = tmpObj.getJSONObject("data").getJSONArray("files");
            if (files.length() < 1)
                return null;
            for (int i = 0; i < files.length(); i++){
                if (null == files.getJSONObject(i))
                    continue;
                JSONObject itemObj = files.getJSONObject(i);
                if (null == itemObj.getString("description") || null == itemObj.getJSONArray("url"))
                    continue;
                String description = itemObj.getString("description");
                JSONArray urlArr = itemObj.getJSONArray("url");
                if (urlArr.length() < 1)
                    continue;
                playUrl = urlArr.getString(0);
                if ("原画".equals(description)){
                    resultObj.put(Definition.ORIGINAL, playUrl);
                } else if ("标清".equals(description)){
                    resultObj.put(Definition.NORMAL, playUrl);
                } else if ("高清".equals(description)){
                    resultObj.put(Definition.HIGH, playUrl);
                } else if ("超清".equals(description)){
                    resultObj.put(Definition.SUPER, playUrl);
                }
            }
            Log.i("info", resultObj.toString());
            return resultObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
