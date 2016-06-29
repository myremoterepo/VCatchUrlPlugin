package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.PatternUtil;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by fanzf on 2015/12/11.
 */
public class PPtvSource extends BaseSource {
    private final String KK = "\"kk\":\"([^\"]+)\"";
    private final String HIGHLIGHT = "\"hilight_id\":(\\d+)";
    private final String CHANNEL = "\"channel_id\":(\\d+)";
    private final String VIPORPAY = "\"isVipOrPay\":(\\d+)";
    private final String COMPLETE = "\"complete\":(\\d+)";
    private final String ENCODE = "getPlayEncode_\\((.*)\\);";

    public String getJsonPlayUrl(String url) {
        JSONObject resultObject = new JSONObject();
        String kk = "", hilight_id = "", isVipOrPay = "", complete = "";
        String type = "mpptv", domain = "m.pptv.com", rccid = "wap_007";
        try {
            url = url.replace("v.pptv.com", "m.pptv.com");
            String htmlContent = HttpTools.getWebContent(url, null);
            if (null == htmlContent || htmlContent.length() < 1)
                return null;

            kk = PatternUtil.getValueForPattern(htmlContent, KK);
            Log.i("info", "kk-->:" + kk);
            hilight_id = PatternUtil.getValueForPattern(htmlContent, HIGHLIGHT);
            if (null == hilight_id)
                hilight_id = PatternUtil.getValueForPattern(htmlContent, CHANNEL);
            Log.i("info", "hilight_id-->:" + hilight_id);
            isVipOrPay = PatternUtil.getValueForPattern(htmlContent, VIPORPAY);
            Log.i("info", "isVipOrPay-->:" + isVipOrPay);
            complete = PatternUtil.getValueForPattern(htmlContent, COMPLETE);
            Log.i("info", "complete-->:" + complete);

            String tmpUrl = "http://web-play.pptv.com/webplay3-0-" + hilight_id +
                    ".xml?version=4&type=" + type +
                    "&kk=" + kk +
                    "&fwc=" + isVipOrPay +
                    "&complete=" + complete +
                    "&o=" + domain +
                    "&rcc_id=" + rccid +
                    "&cb=getPlayEncode_";
            Log.i("info", "tmpUrl--->:" + tmpUrl);
            String result = HttpTools.getWebContent(tmpUrl, null);
            result = PatternUtil.getValueForPattern(result, ENCODE);
            if (null == result || result.length() < 1)
                return null;
            JSONArray tmpArr = getResultArr(result);

            for (int i = 0; i < tmpArr.length(); i++) {
                JSONObject jsonObject = (JSONObject) tmpArr.get(i);
                Log.i("info", "jsonObj-->:" + jsonObject.toString());
                String playUrl = putTogether(jsonObject);
                String destiny = jsonObject.getString("destiny");
                if ("normal".equals(destiny)) {
                    resultObject.put(Definition.NORMAL, playUrl);
                } else if ("high".equals(destiny)) {
                    resultObject.put(Definition.HIGH, playUrl);
                } else if ("super".equals(destiny)) {
                    resultObject.put(Definition.SUPER, playUrl);
                } else if ("hd".equals(destiny)) {
                    resultObject.put(Definition.HD, playUrl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("info", "playUrl-->:" + resultObject.toString());
        return resultObject.toString();
    }

    private String putTogether(JSONObject obj) {
        String url = null;
        try {
            if (!obj.isNull("rid") && !obj.isNull("hostId")) {
                url = "http://" + obj.getString("hostId") + "/" + obj.getString("rid").replace(".mp4", "") + ".m3u8?type=mpptv";
                if (!obj.isNull("key")) {
                    url = url + "&k=" + obj.getString("key");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private JSONArray getResultArr(String content) {
        JSONArray resultArr = new JSONArray();
        JSONObject normalObj = new JSONObject();
        JSONObject highObj = new JSONObject();
        JSONObject superObj = new JSONObject();
        JSONObject hdObj = new JSONObject();

        try {
            normalObj.put("destiny", "normal");
            highObj.put("destiny", "high");
            superObj.put("destiny", "super");
            hdObj.put("destiny", "hd");
            JSONObject obj = new JSONObject(content);
            JSONArray childNodes = obj.getJSONArray("childNodes");

            for (int i = 0; i < childNodes.length(); i++) {
                JSONObject child = childNodes.getJSONObject(i);
                JSONArray thirdArray = null;
                if (child.getString("tagName").equals("channel")) {
                    if (!child.isNull("childNodes")) {
                        JSONArray array = child.getJSONArray("childNodes");
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject mobj = array.getJSONObject(j);
                            if (mobj.getString("tagName").equals("file")) {
                                thirdArray = mobj.getJSONArray("childNodes");
                                for (int m = 0; m < thirdArray.length(); m++) {
                                    JSONObject bObj = thirdArray.getJSONObject(m);
                                    int ftChannel = bObj.getInt("ft");
                                    if (ftChannel == -1)
                                        return null;//ft的判断
                                    if (bObj.getInt("width") < 720 && bObj.getInt("width") > 100) {
                                        normalObj.put("rid", bObj.getString("rid"));
                                        normalObj.put("ft", ftChannel);
                                    } else if (bObj.getInt("width") < 1080) {
                                        highObj.put("rid", bObj.getString("rid"));
                                        highObj.put("ft", ftChannel);
                                    } else if (bObj.getInt("width") < 1920) {
                                        superObj.put("rid", bObj.getString("rid"));
                                        superObj.put("ft", ftChannel);
                                    } else if (bObj.getInt("width") > 0) {
                                        hdObj.put("rid", bObj.getString("rid"));
                                        hdObj.put("ft", ftChannel);
                                    }
                                }
                            }

                        }
                    }
                }
                if (child.getString("tagName").equals("dt")) {
                    JSONArray dtArrat = child.getJSONArray("childNodes");
                    int ftDt = child.getInt("ft");
                    if (ftDt == -1)
                        return null;//ft的判断
                    for (int n = 0; n < dtArrat.length(); n++) {
                        JSONObject nObj = dtArrat.getJSONObject(n);
                        if (nObj.getString("tagName").equals("sh")) {
                            JSONArray shArray = nObj.getJSONArray("childNodes");
                            if (ftDt == normalObj.getInt("ft")) {
                                normalObj.put("hostId", shArray.getString(0));
                            } else if (ftDt == highObj.getInt("ft")) {
                                highObj.put("hostId", shArray.getString(0));
                            } else if (ftDt == superObj.getInt("ft")) {
                                superObj.put("hostId", shArray.getString(0));
                            } else if (ftDt == hdObj.getInt("ft")) {
                                hdObj.put("hostId", shArray.getString(0));
                            }
                        } else if (nObj.getString("tagName").equals("key")) {
                            JSONArray shArray = nObj.getJSONArray("childNodes");
                            if (ftDt == normalObj.getInt("ft")) {
                                normalObj.put("key", shArray.getString(0));
                            } else if (ftDt == highObj.getInt("ft")) {
                                highObj.put("key", shArray.getString(0));
                            } else if (ftDt == superObj.getInt("ft")) {
                                superObj.put("key", shArray.getString(0));
                            } else if (ftDt == hdObj.getInt("ft")) {
                                hdObj.put("key", shArray.getString(0));
                            }
                        }
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resultArr.put(normalObj);
        resultArr.put(highObj);
        resultArr.put(superObj);
        resultArr.put(hdObj);
        return resultArr;
    }

    public String getJsonPlayUrlShort(String url) {
        JSONObject resultObject = new JSONObject();
        String kk = "", hilight_id = "", isVipOrPay = "", complete = "";
        String type = "mpptv", domain = "m.pptv.com", rccid = "wap_007";
        try {
            String htmlContent = HttpTools.getWebContent(url, null);
            if (null == htmlContent || htmlContent.length() < 1)
                return null;

            kk = PatternUtil.getValueForPattern(htmlContent, KK);
            Log.i("info", "kk-->:" + kk);
            hilight_id = PatternUtil.getValueForPattern(htmlContent, HIGHLIGHT);
            if (null == hilight_id)
                hilight_id = PatternUtil.getValueForPattern(htmlContent, CHANNEL);
            Log.i("info", "hilight_id-->:" + hilight_id);
            isVipOrPay = PatternUtil.getValueForPattern(htmlContent, VIPORPAY);
            Log.i("info", "isVipOrPay-->:" + isVipOrPay);
            complete = PatternUtil.getValueForPattern(htmlContent, COMPLETE);
            Log.i("info", "complete-->:" + complete);

            String tmpUrl = "http://web-play.pptv.com/webplay3-0-" + hilight_id +
                    ".xml?version=4&type=" + type +
                    "&kk=" + kk +
                    "&fwc=" + isVipOrPay +
                    "&complete=" + complete +
                    "&o=" + domain +
                    "&rcc_id=" + rccid +
                    "&cb=getPlayEncode_";
            Log.i("info", "tmpUrl--->:" + tmpUrl);
            String result = HttpTools.getWebContent(tmpUrl, null);
            result = PatternUtil.getValueForPattern(result, ENCODE);
            if (null == result || result.length() < 1)
                return null;
            JSONArray tmpArr = getResultArr(result);

            for (int i = 0; i < tmpArr.length(); i++) {
                JSONObject jsonObject = (JSONObject) tmpArr.get(i);
                Log.i("info", "jsonObj-->:" + jsonObject.toString());
                String playUrl = putTogether(jsonObject);
                String destiny = jsonObject.getString("destiny");
                if ("normal".equals(destiny)) {
                    resultObject.put(Definition.NORMAL, playUrl);
                } else if ("high".equals(destiny)) {
                    resultObject.put(Definition.HIGH, playUrl);
                } else if ("super".equals(destiny)) {
                    resultObject.put(Definition.SUPER, playUrl);
                } else if ("hd".equals(destiny)) {
                    resultObject.put(Definition.HD, playUrl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("info", "playUrl-->:" + resultObject.toString());
        return resultObject.toString();
    }
}
