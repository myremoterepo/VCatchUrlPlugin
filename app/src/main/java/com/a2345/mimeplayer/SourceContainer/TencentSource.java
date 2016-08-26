package com.a2345.mimeplayer.SourceContainer;

import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.vov.vitamio.utils.StringUtils;

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
            String cdnurl = cdnRederect(url);
            if (cdnurl != null && cdnurl.length() > 0) {
                url = cdnurl;
            }
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

//    public String cdnRederect1(String url) {
//
//        BasicHttpParams httpParams = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
//        HttpConnectionParams.setSoTimeout(httpParams, 15000);
//        httpParams.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
//        // 这样就能拿到Location头了
//        DefaultHttpClient client = new DefaultHttpClient(httpParams);
//        HttpGet doGet = new HttpGet(url);
//        String cdnUrl = "";
//        try {
//            HttpResponse response = client.execute(doGet);
//            if (response.getStatusLine().getStatusCode() == 302) {
//                response.getEntity().getContentEncoding();
//                cdnUrl = EntityUtils.toString(response.getEntity());
//                Header header = response.getFirstHeader("Location");
//                if (header.getValue() != null)
//                    cdnUrl = header.getValue();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            cdnUrl = "";
//        }
//        Log.e("gex", "cdnRederect1 :" + cdnUrl);
//        return cdnUrl;
//    }


    /**
     * @return 模拟请求，获取cdn地址
     */
    public String cdnRederect(String loadUrl) {
        String cdnUrl="";
        try {
            URL url1 = new URL(loadUrl);
            HttpURLConnection connection =(HttpURLConnection)url1.openConnection();
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(false);
            connection.connect();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP){
                List<String> Locations = connection.getHeaderFields().get("Location");
                String b = null ;
                for (String location : Locations) {
                    int index = location.indexOf("src=http");
                    b = location.substring(index + 1);
                }
                cdnUrl = URLDecoder.decode(b, "UTF-8");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("gex", "cdnRederect2 :" + cdnUrl);
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

