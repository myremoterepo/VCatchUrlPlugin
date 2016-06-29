package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

public class TencentLiveSource extends LiveSource {
    public String getLiveJsonPlayUrl(String url) {
        Log.i("info", "url-->:" + url);
        JSONObject resultObj = new JSONObject();
        String playId = null;
        try {
            String webPageContent = HttpTools.getWebContent(url, null);
            String channelInfo = getChannelInfo(webPageContent, "var ChannelInfo");
            if (channelInfo == null || channelInfo.length() < 1) {
                return null;
            }
            playId = getPlayId(channelInfo, "playid");
            if (playId == null || playId.length() < 1) {
                return null;
            }
            String mUrl = "http://info.zb.qq.com/?callback=jQuery191035902317287400365_"
                    + System.currentTimeMillis()
                    + "&low_login=1&cnlid="
                    + playId
                    + "&host=qq.com&cmd=2&qq=0&txvjsv=2.0&stream=2&debug=&ip=&sdtfrom=213&_="
                    + System.currentTimeMillis();
            Log.i("info", "mUrl-->:" + mUrl);
            String result = getValue(HttpTools.getWebContent(mUrl, null), "playurl");
            Log.i("info", "liveurl-->:" + result);
            if (result.length() < 1) {
                return null;
            }
            resultObj.put(Definition.NORMAL, result);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Log.i("info", "tencentliveplayUrl-->:" + resultObj.toString());
        return resultObj.toString();
    }

    private String getChannelInfo(String content, String infoName) {
        String channelInfo = null;
        int i = content.indexOf(infoName);
        int startL = content.indexOf("{", i) + 1;
        int endL = content.indexOf("}", startL);
        if (startL > endL)
            return null;
        channelInfo = content.substring(startL, endL);
        return channelInfo;
    }

    private String getPlayId(String content, String key) {
        String value = null;
        int i = content.indexOf(key);
        int startL = content.indexOf("\'", i) + 1;
        int endL = content.indexOf("\'", startL);
        if (startL > endL)
            return null;
        value = content.substring(startL, endL);
        return value;
    }

    private String getValue(String paramString1, String paramString2) {
        int i = paramString1.indexOf(paramString2);
        i = paramString1.indexOf("\"", paramString2.length() + i + 2);
        return paramString1.substring(i + 1, paramString1.indexOf("\"", i + 1));
    }

}

