package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

public class IqiyiLiveSource extends LiveSource {

    public String getJsonPlayUrl(String url) {
        return getLiveJsonPlayUrl(url);
    }


    //json字符串
    public String getLiveJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        String mPlayUrl = null;
        String channelId = null;
        try {
            String webPageContent = HttpTools.getWebContent(url, null);
            String livePlayInfo = getLivePlayInfo(webPageContent, "livePlayInfo =");
            if (livePlayInfo == null || livePlayInfo.length() < 1) {
                Log.i("info", "livePlayInfo is null");
                return null;
            }
            channelId = getChannelId(livePlayInfo, ":");
            Log.i("info", "channelId-->:" + channelId);
            if (channelId == null || channelId.length() < 1) {
                Log.i("info", "channelId is null");
                return null;
            }
            String str2 = "http://video.kascend.com/osm-video/JsRunService/run.htm?appkey=webserver&jsparams=%7Bsid%3A1023%2Ctvid%3A" + channelId + "%7D";
            String str3 = HttpTools.getWebContent(str2, null).replaceAll("\r\n", "").replaceAll("\n", "").trim();
            String str4 = "http://cache.video.qiyi.com/jp/liven/" + channelId + "?" + str3;
            String str5 = PatternUtil.getValueForPattern(HttpTools.getWebContent(str4, null), "\"(http:\\\\/\\\\/m3u8\\.live\\..+?\\.m3u8.+?)\"").replaceAll("\\\\/", "/");
            if (str5 == null || str5.length() < 1) {
                return null;
            }
            int i = str5.lastIndexOf("http://");
            mPlayUrl = str5.substring(i);
            Log.i("info", "mPlayUrl-->:" + mPlayUrl);
            resultObj.put(Definition.NORMAL, mPlayUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();

    }

    private String getLivePlayInfo(String content, String key) {
        String mLivePlayInfo = null;
        int i = content.indexOf(key);
        int startL = content.indexOf("{", i) + 1;
        int endL = content.indexOf("}", startL);
        if (startL > endL)
            return null;
        mLivePlayInfo = content.substring(startL, endL).trim();
        return mLivePlayInfo;
    }

    private String getChannelId(String content, String key) {
        String mChannelId = null;
        int startL = content.indexOf(key) + 1;
        int endL = content.indexOf(",", startL);
        if (startL > endL)
            return null;
        mChannelId = content.substring(startL, endL);
        return mChannelId;
    }

}
