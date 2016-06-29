package com.a2345.mimeplayer.SourceContainer;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;
import org.json.JSONObject;

import io.vov.vitamio.utils.Log;

/**
 * Created by fanzf on 2016/4/27.
 */
public class DouYuLiveSource extends LiveSource {
    @Override
    public String getLiveJsonPlayUrl(String url) {
        /**http://m.douyu.com/html5/live?roomId=264254*/
        JSONObject resultObj = new JSONObject();
        String fix = "http://m.douyu.com/html5/live?roomId=%s";
        String id = PatternUtil.getValueForPattern(url, "/([0-9]+)");
        Log.e("info", "id.." + id);
        String mTmpUrl = String.format(fix, id);
        try {
            String htmlContent = HttpTools.getWebContent(mTmpUrl, null);
            JSONObject htmlObj = new JSONObject(htmlContent);
            String mPlayUrl = htmlObj.getJSONObject("data").getString("hls_url");
            resultObj.put(Definition.NORMAL, mPlayUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }
}
