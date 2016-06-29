package com.a2345.mimeplayer.SourceContainer;

import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fanzf on 2016/5/3.
 */
public class KKLiveSource extends LiveSource{
    @Override
    public String getLiveJsonPlayUrl(String id) {
        /**http://kbpull.kktv8.com/livekktv/100616876/playlist.m3u8*/
        String fix = "http://kbpull.kktv8.com/livekktv/%s/playlist.m3u8";
        String playUrl = String.format(fix, id);
        JSONObject resultObj = new JSONObject();
        try {
            resultObj.put(Definition.NORMAL, playUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }
}
