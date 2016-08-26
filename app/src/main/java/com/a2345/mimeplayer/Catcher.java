package com.a2345.mimeplayer;

import android.util.Log;

import com.a2345.mimeplayer.SourceContainer.AcfunSource;
import com.a2345.mimeplayer.SourceContainer.BaoFengSource;
import com.a2345.mimeplayer.SourceContainer.BaseSource;
import com.a2345.mimeplayer.SourceContainer.BiliBiliSource;
import com.a2345.mimeplayer.SourceContainer.BoBoLiveSource;
import com.a2345.mimeplayer.SourceContainer.CNTVSource;
import com.a2345.mimeplayer.SourceContainer.ChuShouLiveSource;
import com.a2345.mimeplayer.SourceContainer.DouYuLiveSource;
import com.a2345.mimeplayer.SourceContainer.FunSource;
import com.a2345.mimeplayer.SourceContainer.HuYaLiveSource;
import com.a2345.mimeplayer.SourceContainer.HuashuSource;
import com.a2345.mimeplayer.SourceContainer.IqiyiSource;
import com.a2345.mimeplayer.SourceContainer.KKLiveSource;
import com.a2345.mimeplayer.SourceContainer.Ku6Source;
import com.a2345.mimeplayer.SourceContainer.LetvSource;
import com.a2345.mimeplayer.SourceContainer.LiveSource;
import com.a2345.mimeplayer.SourceContainer.M1095Source;
import com.a2345.mimeplayer.SourceContainer.MangGuoSource;
import com.a2345.mimeplayer.SourceContainer.PPtvSource;
import com.a2345.mimeplayer.SourceContainer.QuanMinLiveSource;
import com.a2345.mimeplayer.SourceContainer.SouhuLiveSource;
import com.a2345.mimeplayer.SourceContainer.SouhuSource;
import com.a2345.mimeplayer.SourceContainer.TencentLiveSource;
import com.a2345.mimeplayer.SourceContainer.TencentSource;
import com.a2345.mimeplayer.SourceContainer.TudouSource;
import com.a2345.mimeplayer.SourceContainer.YinyuetaiSource;
import com.a2345.mimeplayer.SourceContainer.YoukuSource;
import com.a2345.mimeplayer.ValuePool.Definition;
import com.a2345.mimeplayer.ValuePool.SourceInfo;
import com.a2345.mimeplayer.ValuePool.VideoUrlInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fanzf on 2015/11/26.
 */
public class Catcher {

    //    static String name;
    static String url;
    static String type;
    static VideoUrlInfo mVideoUrlInfo;

    public static void get(SourceInfo info, int position, String definition, CatcherCallback mCatcherCallback) {
        if (null != info) {
            String mPlayUrl = "";
            mVideoUrlInfo = new VideoUrlInfo();
            String name = info.getSourceName();
            url = info.getSourceUrl();
            type = info.getSourceType();
            if (("long").equals(type)) {
                mPlayUrl = getJsonPlayUrl(name, definition);
            } else if (("short").equals(type)) {
                mPlayUrl = getJsonShortPlayUrl(name, definition);
            } else if ("live".equals(type)) {
                mPlayUrl = getLiveJsonPlayUrl(name);
            } else if ("test".equals(type)) {
                mPlayUrl = url;
            }
            if (null != mPlayUrl) {
                mVideoUrlInfo.setPlayUrl(mPlayUrl);
            }
            mVideoUrlInfo.setSource(name);
            mVideoUrlInfo.setType(type);
            mCatcherCallback.onSuccess(mVideoUrlInfo);
        }
    }


    //返回长视频json字符串播放地址
    private static String getJsonPlayUrl(String name, String definition) {
        if (null == name || null == url) {
            return null;
        }
        BaseSource source = null;
        String mPlayUrl = null;

        if (name.equals("youku")) {
            source = new YoukuSource();
        } else if (name.equals("qq")) {
            source = new TencentSource();
        } else if (name.equals("letv")) {
            source = new LetvSource();
        } else if (name.equals("pptv")) {
            source = new PPtvSource();
        } else if (name.equals("acfun")) {
            source = new AcfunSource();
        } else if (name.equals("fun")) {
            source = new FunSource();
        } else if (name.equals("cntv")) {
            source = new CNTVSource();
        } else if (name.equals("baofeng")) {
            source = new BaoFengSource();
        } else if (name.equals("wasu")) {
            source = new HuashuSource();
        } else if (name.equals("hunantv")) {
            source = new MangGuoSource();
        } else if (name.equals("sohu")) {
            source = new SouhuSource();
        } else if (name.equals("iqiyi")) {
            source = new IqiyiSource();
        } else if (name.equals("1905")) {
            source = new M1095Source();
        } else if (name.equals("tudou")) {
            source = new TudouSource();
        } else if (name.equals("bilibili")) {
            source = new BiliBiliSource();
        }
        if (null != source) {
            mPlayUrl = source.getPlayUrl(url);
            Log.i("info", "mPlayUrl->:" + mPlayUrl);
        }
        return mPlayUrl;
    }


    //返回短视频json字符串播放地址
    private static String getJsonShortPlayUrl(String name, String definition) {
        if (null == name || null == url) {
            return null;
        }
        BaseSource source = null;
        String mPlayUrl = null;
        if (name.equals("youkku")) {
            source = new YoukuSource();
        } else if (name.equals("qq")) {
            source = new TencentSource();
        } else if (name.equals("letv")) {
            source = new LetvSource();
        } else if (name.equals("pptv")) {
            source = new PPtvSource();
        } else if (name.equals("acfun")) {
            source = new AcfunSource();
        } else if (name.equals("fun")) {
            source = new FunSource();
        } else if (name.equals("wasu")) {
            source = new HuashuSource();
        } else if (name.equals("iqiyi")) {
            source = new IqiyiSource();
        } else if (name.equals("yinyuetai")) {
            source = new YinyuetaiSource();
        } else if (name.equals("ku6")) {
            source = new Ku6Source();
        } else if (name.equals("1905")) {
            source = new M1095Source();
        }
        if (null != source) {
            mPlayUrl = source.getPlayUrlShort(url);
            Log.i("info", "mPlayUrl->:" + mPlayUrl);
        }
        return mPlayUrl;
    }


    private static String getLiveJsonPlayUrl(String name) {
        if (null == name || null == url) {
            return null;
        }
        LiveSource source = null;
        String mPlayUrl = null;
        if (name.equals("qq")) {
            source = new TencentLiveSource();
        } else if (name.equals("sohu")) {
            source = new SouhuLiveSource();
        } else if (name.equals("quanmin")) {
            source = new QuanMinLiveSource();
        } else if (name.equals("douyu")) {
            source = new DouYuLiveSource();
        } else if (name.equals("huya")) {
            source = new HuYaLiveSource();
        } else if (name.equals("chushou")) {
            source = new ChuShouLiveSource();
        } else if (name.equals("bobo")) {
            source = new BoBoLiveSource();
        } else if (name.equals("kk")) {
            source = new KKLiveSource();
        }
        if (null != source) {
            mPlayUrl = source.getLiveJsonPlayUrl(url);
            try {
                mPlayUrl = new JSONObject(mPlayUrl).getString(Definition.NORMAL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("info", "mPlayUrl->:" + mPlayUrl);
        return mPlayUrl;
    }

}
