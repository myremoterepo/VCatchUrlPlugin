package com.a2345.mimeplayer.SourceContainer;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

import io.vov.vitamio.utils.Log;

/**
 * Created by fanzf on 2016/4/27.
 */
public class HuYaLiveSource extends LiveSource{
    @Override
    public String getLiveJsonPlayUrl(String url) {
        /**http://m.huya.com/kaerlol*/
        JSONObject result = new JSONObject();
        try {
            String htmlContent = HttpTools.getWebContent(url, null);
            int startL = htmlContent.indexOf("hasvedio");
            int sstartL = htmlContent.indexOf("'", startL) + 1;
            int endL = htmlContent.indexOf("'", sstartL);
            String mPlayUrl = htmlContent.substring(sstartL, endL);
            result.put(Definition.NORMAL, mPlayUrl);
            Log.i("info", "html.." + mPlayUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
