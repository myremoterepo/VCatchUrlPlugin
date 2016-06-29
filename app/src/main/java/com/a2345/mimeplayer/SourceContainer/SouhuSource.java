package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

import java.util.UUID;

public class SouhuSource extends BaseSource {
    private static final String fixI = "http://hot.vrs.sohu.com/ipad";
    private static final String fixII = ".m3u8?pg=1&pt=5&cv=5.0.0&qd=282&uid=";
    private static final String fixIII = "&sver=5.0.0&plat=6&ca=3&prod=app";
    private final String USERAGENT = "Mozilla/5.0 (Linux; Android 4.4.4; Nexus 5 Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.114 Mobile Safari/537.36";

    // 长视频
    public String getJsonPlayUrl(String url) {
        String playUrl = "";
        JSONObject resultObj = new JSONObject();
        String vid = null;
        String htmlContent = null;
        try {
            htmlContent = HttpTools.getWebContent(url, null);
            vid = PatternUtil.getValueForPattern(htmlContent, "vid=\"(\\d+)\"");
            if (vid == null || vid.length() < 1) {
                vid = PatternUtil.getValueForPattern(htmlContent, "vid: '(\\d+)'");
            }
            Log.i("info", "vid-->:" + vid);
            String tempurl = "http://hot.vrs.sohu.com/vrs_flash.action?vid=" + vid;
            HttpHeader header = new HttpHeader();
            header.setUserAgent(USERAGENT);
            String tempHtmlContent = HttpTools.getWebContent(tempurl, header);
            Log.i("info", "content-->:" + tempHtmlContent);
            String uid = UUID.randomUUID().toString().replaceAll("-", "");
            JSONObject tempObj = new JSONObject(tempHtmlContent);
            JSONObject dataObj = tempObj.getJSONObject("data");
            int pid = tempObj.getInt("pid");
            Log.i("info", "pid-->:" + pid);
            long time = System.currentTimeMillis() / 1000L;

            if (dataObj.has("oriVid")) {
                int oriVid = dataObj.getInt("oriVid");
                if (oriVid != 0) {
                    playUrl = fixI + oriVid
                            + "_" + time
                            + "_" + pid
                            + fixII + uid
                            + fixIII;
                    resultObj.put(Definition.ORIGINAL, playUrl);
                }
            }
            if (dataObj.has("norVid")) {
                int norVid = dataObj.getInt("norVid");
                if (norVid != 0) {
                    playUrl = fixI + norVid
                            + "_" + time
                            + "_" + pid
                            + fixII + uid
                            + fixIII;
                    resultObj.put(Definition.NORMAL, playUrl);
                }
            }
            if (dataObj.has("highVid")) {
                int highVid = dataObj.getInt("highVid");
                if (highVid != 0) {
                    playUrl = fixI + highVid
                            + "_" + time
                            + "_" + pid
                            + fixII + uid
                            + fixIII;
                    resultObj.put(Definition.HIGH, playUrl);
                }
            }
            if (dataObj.has("superVid")) {
                int superVid = dataObj.getInt("superVid");
                if (superVid != 0) {
                    playUrl = fixI + superVid
                            + "_" + time
                            + "_" + pid
                            + fixII + uid
                            + fixIII;
                    resultObj.put(Definition.SUPER, playUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("info", "play-->:" + resultObj.toString());
        return resultObj.toString();
    }


}
