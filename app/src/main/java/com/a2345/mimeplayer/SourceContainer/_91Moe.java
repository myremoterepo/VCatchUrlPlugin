package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;

import org.json.JSONObject;

/**
 * Created by fanzf on 2016/8/18.
 */
public class _91Moe extends BaseSource {

    @Override
    public String getJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        try {
            String htmlContent = HttpTools.getWebContent(url, null);
            Log.e("fan", "htmlContent.." + htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.i("info", resultObj.toString());
        return resultObj.toString();
    }
}
