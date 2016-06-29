package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BaoFengSource extends BaseSource {
    private static final String fixI = "http://minfo.baofeng.net/asp_c/";
    private static final String fixII = "-n-50-r-1-s-0-p-1.json?callback=_callbacks__0i742doz4";
    private static final String fixIII = "http://rd.p2p.baofeng.net/queryvp.php?type=3&gcid=";
    private static final String fixIIII = "&callback=_callbacks_0i7308d91&";

    // return json String data
    public String getJsonPlayUrl(String url) {
        String playURL = "";
        JSONObject resultObject = new JSONObject();

        String baofengHTML;
        HttpHeader header = new HttpHeader();
        header.setUserAgent("Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        header.setReferer("http://m.hd.baofeng.com");
        try {
            baofengHTML = HttpTools.getWebContent(url, header);
            if (baofengHTML != null) {
                String info_pianyaun = PatternUtil.getValueForPattern(baofengHTML, "\"info_pianyuan\":(.+)]") + "]";
                Log.i("info", "info_pianyaun-->:" + info_pianyaun);
                JSONArray pyArr = new JSONArray(info_pianyaun);
                JSONObject pyObj;
                String aid;
                String wid;
                String type;
                String iid;
                String len;
                String drama;
                String ip;
                String port;
                String path;
                String key;
                for (int i = 0; i < pyArr.length(); i++) {
                    pyObj = (JSONObject) pyArr.get(i);
                    aid = pyObj.getString("aid");
                    wid = pyObj.getString("wid");
                    type = pyObj.getString("hd_type");
                    String iidURL = fixI + wid + "/" + Integer.parseInt(aid) % 500 + "/" + aid + fixII;
                    String _callbacks__0i742doz4 = HttpTools.getWebContent(iidURL, header);
                    int start2 = _callbacks__0i742doz4.indexOf("{");
                    int end2 = _callbacks__0i742doz4.lastIndexOf("}");
                    String iidString = _callbacks__0i742doz4.substring(start2, end2 + 1);
                    JSONObject iidJSON = new JSONObject(iidString);
                    JSONArray video_list = iidJSON.getJSONArray("video_list");
                    if (url.contains("drama")) {
                        if (url.contains("drama=")) {
                            drama = PatternUtil.getValueForPattern(url, "drama=(\\d+)");
                        } else {
                            int indexOfVideo = url.lastIndexOf(".");
                            int endLocation = url.lastIndexOf("-");
                            drama = url.substring(endLocation + 1, indexOfVideo);
                        }
                        JSONObject locationOfVideo = video_list.getJSONObject(Integer.parseInt(drama) - 1);
                        iid = locationOfVideo.getString("iid");
                        len = locationOfVideo.getString("size");
                    } else {
                        iid = video_list.getJSONObject(video_list.length() - 1).getString("iid");
                        len = video_list.getJSONObject(video_list.length() - 1).getString("size");
                    }
                    long l = System.currentTimeMillis();
                    playURL = fixIII + iid + fixIIII + l;
                    String query = HttpTools.getWebContent(playURL, header);
                    String quy = query.substring(query.indexOf("(") + 1, query.lastIndexOf(")")).replaceAll("\'", "\"");
                    Log.i("info", "quy-->:" + quy);
                    JSONObject quyObj = new JSONObject(quy);
                    ip = quyObj.getString("ip");
                    port = quyObj.getString("port");
                    path = quyObj.getString("path");
                    key = quyObj.getString("key");
                    playURL = decodingBao(ip, port, path, key, len);
                    if (playURL != null && playURL.length() > 0) {
                        if ("1920P".equals(type)) {
                            resultObject.put(Definition.HD, playURL);
                        }
                        if ("1080P".equals(type)) {
                            resultObject.put(Definition.SUPER, playURL);
                        }
                        if ("720P".equals(type)) {
                            resultObject.put(Definition.HIGH, playURL);
                        }
                        if ("480P".equals(type)) {
                            resultObject.put(Definition.NORMAL, playURL);
                        }
                    }
                }
            }
//
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("info", "playUrl--->:" + resultObject.toString());
        return resultObject.toString();

    }


    public String decodingBao(String ip, String e, String f, String a, String filelen) {
        String y = "";
        HashMap<String, String> q = new HashMap<String, String>();
        q.put("b", "0");
        q.put("a", "1");
        q.put("o", "2");
        q.put("f", "3");
        q.put("e", "4");
        q.put("n", "5");
        q.put("g", "6");
        q.put("h", "7");
        q.put("t", "8");
        q.put("m", "9");
        q.put("l", ".");
        q.put("c", "A");
        q.put("p", "B");
        q.put("z", "C");
        q.put("r", "D");
        q.put("y", "E");
        q.put("s", "F");
        String[] c = ip.split(",");
        ArrayList<String> h = new ArrayList<String>();
        ArrayList<String> m = new ArrayList<String>();
        for (int s = 0; s < c.length; s++) {
            y = "";
            String w = c[s];
            int Ma = w.length();
            for (int tb = 0; tb < Ma; tb++)
                y += q.get(w.substring(tb, tb + 1));
            h.add(y);
        }
        for (int j = 0; j < h.size(); j++) {
            y = "http://" + h.get(j) + ":" + e + "/" + f + "?key=" + a;
            m.add(y);
        }
        y += "&filelen=" + filelen;
        return y;
    }


}
