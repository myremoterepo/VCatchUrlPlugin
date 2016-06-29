package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

/**
 * Created by fanzf on 2016/5/3.
 */
public class ChuShouLiveSource extends LiveSource {
    @Override
    public String getLiveJsonPlayUrl(String id) {
        /**
         * http://chushou.tv/room/m-33591.htm
         * playUrl="http://wslive-hls.kascend.com/chushou_live/616d4d08423e41bcb271b9b304b58de2/playlist.m3u8"*/
        String fix = "http://chushou.tv/room/m-%s.htm";
        String tmpUrl = String.format(fix, id);
        JSONObject resultObj = new JSONObject();
        try {
            String htmlContent = HttpTools.getWebContent(tmpUrl, null);
            String playUrl = PatternUtil.getValueForPattern(htmlContent, "playUrl=\"(.+)\"");
            Log.i("info", "playUr.." + playUrl);
            resultObj.put(Definition.NORMAL, playUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }
}
