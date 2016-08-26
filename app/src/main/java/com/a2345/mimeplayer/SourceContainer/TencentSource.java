package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fanzf on 2015/12/1.
 */
public class TencentSource extends BaseSource {
    /**http://h5vv.video.qq.com/getinfo?vid=e0020786eop&platform=10901&otype=json&defn=auto&low_login=1&_={1}*/
    /**
     * vid:"8O933NmPt5v"
     */

    @Override
    public String getJsonPlayUrl(String url) {
        String vid = null;
        try {
            Log.e("gex", ".start.." + url);
            cdnRederect(url);
            Log.e("gex", ".startddd.............");
            if (url.contains("vid=")) {
                vid = PatternUtil.getValueForPattern(url, "vid=([0-9a-z]+)");
                Log.e("parseurl", "1vid.." + vid);
            } else {
                String htmlContent = HttpTools.getWebContent(url, null);
                if (htmlContent.contains("VIDEO_INFO")) {
                    vid = getVid(htmlContent);
                    Log.e("parseurl", "2vid.." + vid);
                } else {
                    /*url = url.replace("prev/", "cover/");
                    String com = PatternUtil.getValueForPattern(url, "(cover/(r/){0,1})");
                    Log.e("parseurl", "com.." + com);
                    url = url.replace(com, "x/cover/");*/
                    if (url.contains("prev/")) {
                        url = url.replace("prev/", "cover/");
                    }
                    /*if (url.contains("cover/")) {
                        url = url.replace("cover/", "x/cover/");
                    }*/
                    Log.e("fan", "url.." + url);
                    htmlContent = HttpTools.getWebContent(url, null);
                    Log.e("fan", "htmlContent.." + htmlContent);
                    vid = getVidFromHtml(htmlContent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getSource(vid);
    }
    /**
     * @return 模拟请求，获取cdn地址
     */
    public String cdnRederect(String loadUrl) {
        URL url = null;
        String cdnUrl="";
        try {
            url = new URL(loadUrl);
            URLConnection connection = url.openConnection();
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            connection.connect();
            Set<Map.Entry<String, List<String>>> entrySet = headerFields.entrySet();
            Iterator<Map.Entry<String, List<String>>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> next = iterator.next();
                String key = next.getKey();
                List<String> value = next.getValue();
                Log.e("gex", "key :"+key+"   valus-size :" + value.toString());
//                if (!StringUtils.isEmpty(key) && key.equals("Content-Type")) {
//                    if (value != null)
//                        Log.e("gex", "Content-Type :" + value.toString());
//                    if (value != null && value.toString().contains("mpegurl")) {
//                        isM3u8 = true;
//                        mInfo.setVideoFormat("m3u8");
//                        break;
//                    }
//                    if (value != null && value.toString().contains("mp4") || value.toString().contains("MP4") || value.toString().contains("Mp4")) {
//                        mInfo.setVideoFormat("mp4");
//                    } else {
//                        mInfo.setVideoFormat("");
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cdnUrl;
    }


    private String getVidFromHtml(String htmlContent) {
        String vid;
        if (htmlContent.contains("VIDEO_INFO")) {
            vid = getVid(htmlContent);
            Log.e("parseurl", "3vid.." + vid);
        } else {
            String url = PatternUtil.getValueForPattern(htmlContent, "url=\'(.+)\"");
            if (url == null) {
                url = PatternUtil.getValueForPattern(htmlContent, "url=(.+)\"");
            }
            if (url != null && !url.startsWith("http")) {
                url = "http://v.qq.com" + url;
            }
            try {
                htmlContent = HttpTools.getWebContent(url, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            vid = getVid(htmlContent);
            Log.e("parseurl", "4vid.." + vid);
        }
        return vid;
    }

    @Override
    public String getJsonPlayUrlShort(String url) {
        String vid = null;
        String htmlContent = null;
        try {
            htmlContent = HttpTools.getWebContent(url, null);
            if (url.contains("vid=")) {
                vid = PatternUtil.getValueForPattern(url, "vid=([0-9a-z]+)");
                Log.e("parseurl", "1vid.." + vid);
            } else {
                if (htmlContent.contains("VIDEO_INFO")) {
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

    private String getSource(String vid) {
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


    private String getVid(String htmlContent) {
        String vid = null;
        int i = htmlContent.indexOf("VIDEO_INFO");
        int i1 = htmlContent.indexOf("vid:", i);
        int i2 = htmlContent.indexOf("\"", i1) + 1;
        int i3 = htmlContent.indexOf("\",", i2);
        vid = htmlContent.substring(i2, i3);
        return vid;
    }
}

