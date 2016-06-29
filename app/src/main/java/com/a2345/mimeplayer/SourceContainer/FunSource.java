package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;

public class FunSource extends BaseSource {
    private String fix = "http://play.kanketv.com/playerCode2.0/play/api?videoType=M&playerId=283771&linkUrl=%s&msgType=play&flash=html";

    public String getJsonPlayUrl(String url) {
        String ss = String.format(fix, url);
        Log.i("info", "mHtmlcontent-->:" + ss);
        try {
            String mHtmlContent = HttpTools.getWebContent(ss, null);
            Log.i("info", "mHtmlcontent-->:" + mHtmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getJsonPlayUrlShort(String url) {
        return getJsonPlayUrl(url);
    }


}
