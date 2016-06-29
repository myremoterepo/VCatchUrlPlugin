package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

import java.net.URLDecoder;

public class PPtvLiveSource extends LiveSource {
    public String getLiveJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        String playUrl = null;
        try {
            String ppliveHTML = HttpTools.getWebContent(url, null);
            if ((ppliveHTML != null) && (ppliveHTML.contains("webcfg"))) {
                int id = ppliveHTML.indexOf("\"id\"");
                int idS = ppliveHTML.indexOf(":", id) + 1;
                int idE = ppliveHTML.indexOf(",", idS);
                String idString = ppliveHTML.substring(idS, idE);
                int ctx = ppliveHTML.indexOf("\"ctx\"");
                int ctxS = ppliveHTML.indexOf("\"", ppliveHTML.indexOf(":", ctx)) + 1;
                int ctxE = ppliveHTML.indexOf("\"", ctxS);
                String ctxString = ppliveHTML.substring(ctxS, ctxE);
                String[] localGroup = URLDecoder.decode(ctxString, "utf-8").split("&");
                for (int j = 0; j < localGroup.length; j++) {
                    if (localGroup[j].contains("kk=")) {
                        String kk = localGroup[j].split("=")[1];
                        playUrl = "http://web-play.pptv.com/web-m3u8-" + idString + ".m3u8?type=m3u8.web.pad&playback=0&kk=" + kk + "&o=v.pptv.com&rcc_id=0";
                        resultObj.put(Definition.NORMAL, playUrl);
                        Log.i("info", "playUrl-->:" + playUrl);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("info", "liveUrl-->:" + resultObj.toString());
        return resultObj.toString();
    }
}
