package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

/**
 * Created by fanzf on 2015/12/8.
 */
public class LetvSource extends BaseSource {

    public String getJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        String fixT = "http://g3.letv.cn";
        String adoptT = "";
        String tmpUrl = "http://api.letv.com/mms/out/video/playJsonH5?platid=3&splatid=305&tss=mp4&id=";
        int i = url.lastIndexOf("/");
        int j = url.lastIndexOf(".");
        String id = url.substring(i, j).replaceAll("[^0-9]", "");
        Log.i("info", "id-->:" + id);
        long key = getLetv_Html5Key();
        Log.i("info", "key-->:" + key);
        tmpUrl = tmpUrl + id + "&dvtype=1300&accessyx=1&uid=0&domain=www.letv.com&tkey=" + key;
        Log.i("info", "tmpUrl-->:" + tmpUrl);
        HttpHeader header = new HttpHeader();
        header.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 8_1_2 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12B440 QQ/5.3.2.424 NetType/WIFI");
        try {
            String result = HttpTools.getWebContent(tmpUrl, header);
            Log.i("info", "1-->:" + result);
            JSONObject localJSONObject1 = new JSONObject(result).getJSONObject("playurl").getJSONObject("dispatch");
            if (localJSONObject1.has("1300")) {
                adoptT = localJSONObject1.getJSONArray("1300").getString(0);
                resultObj.put(Definition.HD, fixT + adoptT);

            }
            if (localJSONObject1.has("1000")) {
                adoptT = localJSONObject1.getJSONArray("1000").getString(0);
                resultObj.put(Definition.SUPER, fixT + adoptT);
            }
            if (localJSONObject1.has("350")) {
                adoptT = localJSONObject1.getJSONArray("350").getString(0);
                resultObj.put(Definition.HIGH, fixT + adoptT);
            }
            if (localJSONObject1.has("mp4")) {
                adoptT = localJSONObject1.getJSONArray("mp4").getString(0);
                resultObj.put(Definition.NORMAL, fixT + adoptT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }

    private long getLetv_Html5Key() {
        long l1 = getLeTime();
        for (int i = 0; i < 8; i++) {
            long l2 = l1 >> 1;
            long l3 = (0x1 & l1) << 31;
            l1 = l2 + l3;
        }
        return 0xB074319 ^ l1;
    }

    private static long getLeTime() {
        long l = System.currentTimeMillis() / 1000L;
        try {
            String url = "http://api.letv.com/time?timestamp=" + l;
            HttpHeader header = new HttpHeader();
            header.setUserAgent("User-Agent: SuperNode Downloader/1.0.9");
            String webContent = HttpTools.getWebContent(url, header);
            if (null != webContent) {
                if (webContent.contains("stime")) {
                    l = new JSONObject(webContent).getLong("stime");
                }
            }
            return l;
        } catch (Throwable e) {
            e.printStackTrace();

        }
        return l;
    }

}
