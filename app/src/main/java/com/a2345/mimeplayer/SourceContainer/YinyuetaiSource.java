package com.a2345.mimeplayer.SourceContainer;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

/**
 * Created by fanzf on 2016/3/4.
 */
public class YinyuetaiSource extends BaseSource {
    @Override
    public String getJsonPlayUrlShort(String url) {
        JSONObject resultObj = new JSONObject();
        String vid = PatternUtil.getValueForPattern(url, "/([0-9]+)?");
        if (vid == null){
            vid = url.substring(url.lastIndexOf("/")).replaceAll("[^0-9]", "");
        }
        try {
            JSONObject videoObj = new JSONObject(HttpTools.getWebContent("http://www.yinyuetai.com/api/info/get-video-urls?videoId=" + vid, null));
            if (videoObj.has("hcVideoUrl")) {
                if (videoObj.getString("hcVideoUrl").startsWith("http"))
                    resultObj.put(Definition.NORMAL, videoObj.getString("hcVideoUrl"));
            }
            if (videoObj.has("hdVideoUrl")) {
                if (videoObj.getString("hdVideoUrl").startsWith("http"))
                    resultObj.put(Definition.HIGH, videoObj.getString("hdVideoUrl"));
            }
            if (videoObj.has("heVideoUrl")) {
                if (videoObj.getString("heVideoUrl").startsWith("http"))
                    resultObj.put(Definition.SUPER, videoObj.getString("heVideoUrl"));
            }
            return resultObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
