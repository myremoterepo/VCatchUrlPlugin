package com.a2345.mimeplayer.SourceContainer;

import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONException;
import org.json.JSONObject;

import io.vov.vitamio.utils.Log;

/**
 * Created by fanzf on 2016/4/26.
 */
public class QuanMinLiveSource extends LiveSource{

    /**http://www.quanmin.tv/star/29189*/
    @Override
    public String getLiveJsonPlayUrl(String url) {
        String fixI = "http://hls.quanmin.tv/live/%s/playlist.m3u8";
        String id = PatternUtil.getValueForPattern(url, "/([0-9]+)");
        Log.e("info", "id.." + id);
        String playUrl = String.format(fixI, id);
        JSONObject result = new JSONObject();
        try {
            result.put(Definition.NORMAL, playUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
