package com.a2345.mimeplayer.SourceContainer;

import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fanzf on 2016/5/3.
 */
public class BoBoLiveSource extends LiveSource {
    @Override
    public String getLiveJsonPlayUrl(String id) {
        /**http://hls.wspull.bn.netease.com/pushstation/59135932/playlist.m3u8*/
        String fix = "http://hls.wspull.bn.netease.com/pushstation/%s/playlist.m3u8";
        String plaUrl = String.format(fix, id);
        JSONObject resultObj = new JSONObject();
        try {
            resultObj.put(Definition.NORMAL, plaUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }
}
