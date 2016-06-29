package com.a2345.mimeplayer.SourceContainer;

import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class BaseSource {

    //==长视频开始=============================================================================================================================
    public String getJsonPlayUrl(String url) {
        return null;
    }

    public String getPlayUrl(String url) {
        String videoUrl = getJsonPlayUrl(url);
        return getChooseUrl(videoUrl);
    }
    //==长视频结束 ==================================================================================

    //==短视频开始===================================================================================
    public String getJsonPlayUrlShort(String url) {
        return null;
    }

    public String getPlayUrlShort(String url) {
        String videoUrl = getJsonPlayUrlShort(url);
        return getChooseUrl(videoUrl);
    }
    //==短视频结束===================================================================================

    //==直播结束=====================================================================================
    public String getJsonPlayUrlLive(String url) {
        return null;
    }

    public String getPlayUrlLive(String url) {
        String videoUrl = getJsonPlayUrlShort(url);
        return getChooseUrl(videoUrl);
    }
    //==直播结束=====================================================================================

    /**
     * 解析json数据，获取播放地址
     * 获取顺序：超清-高清-普通-流畅
     * 获取失败返回null
     * */
    private String getChooseUrl(String videoUrl) {
        if (videoUrl == null || videoUrl.length() < 1) {
            return null;
        } else {
            try {
                JSONObject videoObj = new JSONObject(videoUrl);
                if (videoObj.has(Definition.SUPER) && videoObj.getString(Definition.SUPER).startsWith("http")) {
                    return videoObj.getString(Definition.SUPER);
                }
                if (videoObj.has(Definition.HIGH) && videoObj.getString(Definition.HIGH).startsWith("http")) {
                    return videoObj.getString(Definition.HIGH);
                }
                if (videoObj.has(Definition.NORMAL) && videoObj.getString(Definition.NORMAL).startsWith("http")) {
                    return videoObj.getString(Definition.NORMAL);
                }
                if (videoObj.has(Definition.ORIGINAL) && videoObj.getString(Definition.ORIGINAL).startsWith("http")) {
                    return videoObj.getString(Definition.ORIGINAL);
                }
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
    }
}
