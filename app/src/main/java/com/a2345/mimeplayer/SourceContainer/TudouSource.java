package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

/**
 * Created by fanzf on 2016/3/17.
 */
public class TudouSource extends BaseSource {
    @Override
    public String getJsonPlayUrl(String url) {
        String htmlContent = null;
        String vcode = null;
        try {
            htmlContent = HttpTools.getWebContent(url, null);
            int i = htmlContent.indexOf("vcode:", htmlContent.indexOf("pageConfig"));
            if (i > 0) {
                i = htmlContent.indexOf("\"", i);
                int j = htmlContent.indexOf("\"", i + 1);
                if (j > i) {
                    vcode = htmlContent.substring(i + 1, j);
                }
            }
            if (vcode != null) {
                Log.i("info", "vcode-->:" + vcode);
                String urlYouku = "http://v.youku.com/v_show/id_" + vcode + ".html";
                YoukuSource youku = new YoukuSource();
                return youku.getJsonPlayUrl(urlYouku);
            } else {
                JSONObject resultObj = new JSONObject();
                String iid = PatternUtil.getValueForPattern(htmlContent, "iid: ([0-9]+)");
                Log.i("info", "iid-->:" + iid);
                String urlTudou = "http://vr.tudou.com/v2proxy/v2?it=" + iid.trim() + "&st=52&pw=";
                String playUrl = HttpTools.getWebContent(urlTudou, null);
                if (playUrl != null && playUrl.startsWith("http")) {
                    resultObj.put(Definition.NORMAL, playUrl);
                }
                return resultObj.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
