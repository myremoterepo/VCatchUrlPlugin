package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by fanzf on 2015/12/1.
 */
public class TencentSource extends BaseSource {
    /**http://h5vv.video.qq.com/getinfo?vid=e0020786eop&platform=10901&otype=json&defn=auto&low_login=1&_={1}*/
    /**vid:"8O933NmPt5v"*/

    @Override
    public String getJsonPlayUrl(String url) {
        String vid = null;
        try {
            String htmlContent = HttpTools.getWebContent(url, null);
            if (url.contains("vid=")){
                vid = PatternUtil.getValueForPattern(url, "vid=([0-9a-z]+)");
                Log.e("parseurl", "1vid.." + vid);
            } else {
                if (htmlContent.contains("VIDEO_INFO")){
                    vid = getVid(htmlContent);
                    Log.e("parseurl", "2vid.." + vid);
                } else {
                    url = PatternUtil.getValueForPattern(htmlContent, "url=\'(.+)\"");
                    if (url == null){
                        url = PatternUtil.getValueForPattern(htmlContent, "url=(.+)\"");
                    }
                    if (url != null && !url.startsWith("http")){
                        url = "http://v.qq.com" + url;
                    }
                    htmlContent = HttpTools.getWebContent(url, null);
                    vid = getVid(htmlContent);
                    Log.e("parseurl", "3vid.." + vid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getSource(vid);
    }

    @Override
    public String getJsonPlayUrlShort(String url) {
        String vid = null;
        String htmlContent = null;
        try {
            htmlContent = HttpTools.getWebContent(url, null);
            if (url.contains("vid=")){
                vid = PatternUtil.getValueForPattern(url, "vid=([0-9a-z]+)");
                Log.e("parseurl", "1vid.." + vid);
            } else {
                if (htmlContent.contains("VIDEO_INFO")){
                    vid = getVid(htmlContent);
                    Log.e("parseurl", "2vid.." + vid);
                } else {
                    vid = PatternUtil.getValueForPattern(url, "/([0-9a-z]+).html");
                    Log.e("parseurl", "3vid.." + vid);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return getSource(vid);
    }

    private String getSource(String vid){
        String fix = "http://h5vv.video.qq.com/getinfo?vid=%s&platform=10901&otype=json&defn=auto&low_login=1&_={1}";
        JSONObject resultObj = new JSONObject();
        String tmpUrl = String.format(fix, vid);
        Log.e("parseurl", "tmpUrl.." + tmpUrl);
        String htmlContent = null;
        try {
            htmlContent = HttpTools.getWebContent(tmpUrl, null);
            int startL = htmlContent.indexOf("{");
            String content = htmlContent.substring(startL, htmlContent.lastIndexOf(";"));
            Log.e("parseurl", "content.." + content);
            JSONObject htmlObj = new JSONObject(content);
            JSONObject vl = htmlObj.getJSONObject("vl");
            JSONArray vi = vl.getJSONArray("vi");
            JSONObject viF = vi.getJSONObject(0);
            String fn = viF.getString("fn");
            Log.e("parseurl", "fn.." + fn);
            String fvkey = viF.getString("fvkey");
            Log.e("parseurl", "fvkey.." + fvkey);
            JSONObject ul = viF.getJSONObject("ul");
            JSONArray ui = ul.getJSONArray("ui");
            JSONObject uiF = ui.getJSONObject(0);
            String urlFix = uiF.getString("url");
            Log.e("parseurl", "urlFix.." + urlFix);
            String playUrl = urlFix + fn + "?sdtfrom=v7010&vkey=" + fvkey;
            resultObj.put(Definition.NORMAL, playUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }


    private String getVid(String htmlContent){
        String vid = null;
        int i = htmlContent.indexOf("VIDEO_INFO");
        int i1 = htmlContent.indexOf("vid:", i);
        int i2 = htmlContent.indexOf("\"", i1) + 1;
        int i3 = htmlContent.indexOf("\",", i2);
        vid = htmlContent.substring(i2, i3);
        return vid;
    }
}

