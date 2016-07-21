package com.a2345.mimeplayer.SourceContainer;

import android.annotation.SuppressLint;
import android.util.Log;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;

import org.json.JSONObject;

/**
 * Created by fanzf on 2015/12/8.
 */
public class LetvSource extends BaseSource {

    /**
     * http://www.le.com/ptv/vplay/25337573.html
     * http://api.mob.app.letv.com/play?tm=1468983440&playid=0&tss=ios&pcode=010110175&version=6.7.1&pid=10012078&vvid=3d316080b132506c8b0c6ded0bed4552_1468983438900&devid=3d316080b132506c8b0c6ded0bed4552&v=android_6710&res=json&cuid=3d316080b132506c8b0c6ded0bed4552&t=1468983439391&vastTag=18&n=1&did=7f52e71dbddcdd00&offline=0&ct=2%2C3%2C4&nv=android_6710_letvVideo&IPDXIP=ip%3D&country=CN&provinceid=9&districtid=126&citylevel=1&location=%E4%B8%8A%E6%B5%B7%E5%B8%82%7C%E6%B5%A6%E4%B8%9C%E6%96%B0%E5%8C%BA&region=CN&lang=chs
     * */
    @SuppressLint("DefaultLocale")
    public String getJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        long l = System.currentTimeMillis();
        long tm = getLeTime();
        String vasttag = "18";
        try {
            String htmlContent = HttpTools.getWebContent(url, null);
//            Log.e("fan", "htmlContent.." + htmlContent);
            String pid = PatternUtil.getValueForPattern(htmlContent, "\"pid\":([0-9]+),");
            Log.e("fan", "pid.." + pid);
            String tmpUrl = "http://api.mob.app.letv.com/play?tm=" + tm + "&playid=0&tss=ios&pcode=010110175&version=6.7.1&pid=" + pid + "&vvid=3d316080b132506c8b0c6ded0bed4552_" + l + "&devid=3d316080b132506c8b0c6ded0bed4552&v=android_6710&res=json&cuid=3d316080b132506c8b0c6ded0bed4552&t=" + System.currentTimeMillis() + "&vastTag=" + vasttag + "&n=1&did=7f52e71dbddcdd00&offline=0&ct=2%2C3%2C4&nv=android_6710_letvVideo&IPDXIP=ip%3D&country=CN&provinceid=9&districtid=126&citylevel=1&location=%E4%B8%8A%E6%B5%B7%E5%B8%82%7C%E6%B5%A6%E4%B8%9C%E6%96%B0%E5%8C%BA&region=CN&lang=chs";

//            tmpUrl = String.format(fixT, tm, pid, l, System.currentTimeMillis());
            Log.e("fan", "url.." + tmpUrl);
            htmlContent = HttpTools.getWebContent(tmpUrl, null);
            Log.e("fan", "htmlContent.." + htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }




        /*int i = url.lastIndexOf("/");
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
        }*/
        return resultObj.toString();
    }

    private long getLetv_Html5Key(long l) {
        for (int i = 0; i < 8; i++) {
            long l2 = l >> 1;
            long l3 = (0x1 & l) << 31;
            l = l2 + l3;
        }
        return 0xB074319 ^ l;
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
