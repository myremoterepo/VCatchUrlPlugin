package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SouhuLiveSource extends LiveSource {
    public String getLiveJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        String mPlayUrl = null;
        String tvId = null;
        try {
            String webPageContent = HttpTools.getWebContent(url, null);
            String tvInfo = getTvInfo(webPageContent);
            if (tvInfo == null || tvInfo.length() < 1) {
                Log.i("info", "tvInfo is null");
                return null;
            }
            tvId = getTvId(tvInfo);
            if (tvId == null || tvId.length() < 1) {
                Log.i("info", "tvId is null");
                return null;
            }
            String str2 = "http://live.tv.sohu.com/live/player_json.jhtml?lid=" + tvId + "&type=1";
            String mHtmlContent = HttpTools.getWebContent(str2, null);
            mPlayUrl = getUrl(mHtmlContent, "\"hls\":\"(http.+?)\"");
            if (mPlayUrl == null || mPlayUrl.length() < 1) {
                String mLiveUrl = getUrl(mHtmlContent, "\"live\":\"(http.+?)\"");
                String mLiveHtmlContent = HttpTools.getWebContent(mLiveUrl, null);
                mPlayUrl = new JSONObject(mLiveHtmlContent).getString("url");
            }
            if (mPlayUrl == null || mPlayUrl.length() < 1) {
                Log.i("info", "mPlayUrl is null");
                return null;
            }
            resultObj.put(Definition.NORMAL, mPlayUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("info", "resultObj-->:" + resultObj.toString());
        return resultObj.toString();
    }

    private String getTvId(String content) {
        String mTvId = null;
        int startL = content.indexOf("=") + 1;
        int endL = content.indexOf(";", startL);
        if (startL > endL)
            return null;
        mTvId = content.substring(startL, endL);
        return mTvId;
    }

    private String getTvInfo(String content) {
        String mTvInfo = null;
        int startL = content.indexOf("var tvId");
        int endL = content.indexOf("</script>");
        if (startL > endL)
            return null;
        mTvInfo = content.substring(startL, endL);
        return mTvInfo;
    }

    private String getUrl(String content, String key) {
        String result = null;
        Matcher localMatcher = Pattern.compile(key).matcher(content);
        if (localMatcher.find())
            result = localMatcher.group(1);
        return result;
    }
}

