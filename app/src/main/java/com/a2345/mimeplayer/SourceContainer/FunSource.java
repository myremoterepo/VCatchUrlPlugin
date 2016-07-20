package com.a2345.mimeplayer.SourceContainer;

import android.util.Log;

import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONArray;
import org.json.JSONObject;

public class FunSource extends BaseSource {
    private String fix = "http://play.kanketv.com/playerCode2.0/play/api?videoType=M&playerId=283771&linkUrl=%s&msgType=play&flash=html";

    public String getJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        String ss = String.format(fix, url);
        Log.i("info", "mHtmlcontent-->:" + ss);
        try {
            String mHtmlContent = HttpTools.getWebContent(ss, null);
            Log.i("info", "mHtmlcontent-->:" + mHtmlContent);
            JSONArray array1 = new JSONArray(mHtmlContent);
            if (array1 != null && array1.length() > 0){
                for (int i = 0; i < array1.length(); i++){
                    JSONArray array2 = array1.getJSONArray(i);
                    if (array2 != null && array2.length() > 0){
                        JSONObject obj = array2.getJSONObject(0);
                        if (obj != null && obj.getString("high") != null && obj.getString("link") != null){
                            String definition = obj.getString("high");
                            String playUrl = obj.getString("link");
                            if (definition.equals("流畅")){
                                resultObj.put(Definition.ORIGINAL, playUrl);
                            } else if (definition.equals("标清")){
                                resultObj.put(Definition.NORMAL, playUrl);
                            } else if (definition.equals("高清")){
                                resultObj.put(Definition.HIGH, playUrl);
                            } else if (definition.equals("超清")){
                                resultObj.put(Definition.SUPER, playUrl);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }

    public String getJsonPlayUrlShort(String url) {
        return getJsonPlayUrl(url);
    }

}
