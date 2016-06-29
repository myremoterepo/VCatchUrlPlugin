package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

/**
 * Created by fanzf on 2016/5/4.
 */
public class BiliBiliSource extends BaseSource {

    @Override
    public String getJsonPlayUrl(String url) {
        /**http://www.bilibili.com/m/html5?aid=3485327&page=1*/
        /**
         "img": "http://i1.hdslb.com/video/3c/3c178d565d5418d954d9ccd1c02af8f7.jpg",
         "cid": "http://comment.bilibili.com/5540518.xml",
         "src": "http://cn-zjhz7-dx.acgvideo.com/vg6/9/b7/5540518-1.mp4?expires=1462368000&ssig=92CvfM_cKXU_hKwBl_w1xw&oi=1961100940&internal=1&rate=0"
         * */
        String fix = "http://www.bilibili.com/m/html5?aid=%s&page=1";
        String aid = PatternUtil.getValueForPattern(url, "/av([0-9]+)");
        Log.i("info", "aid.." + aid);
        String tmpUrl = String.format(fix, aid);
        JSONObject resultObj = new JSONObject();
        try {
            String htmlContent = HttpTools.getWebContent(tmpUrl, null);
            JSONObject htmlObj = new JSONObject(htmlContent);
            String playUrl = htmlObj.getString("src");
            resultObj.put(Definition.NORMAL, playUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }
}
