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

    static String name;
    static String url;
    static String type;
    static VideoUrlInfo mVideoUrlInfo;

    public static void get(SourceInfo info, int position, String definition, CatcherCallback mCatcherCallback) {
        if (null != info) {
            String mPlayUrl = "";
            mVideoUrlInfo = new VideoUrlInfo();
            name = info.getSourceName();
            url = info.getSourceUrl();
            type = info.getSourceType();
            if (("long").equals(type)) {
                mPlayUrl = getJsonPlayUrl(position, definition);
            } else if (("short").equals(type)) {
                mPlayUrl = getJsonShortPlayUrl(position, definition);
            } else if ("live".equals(type)){
                mPlayUrl = getLiveJsonPlayUrl(position);
            } else if ("test".equals(type)){
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
    private static String getJsonPlayUrl(int position, String definition) {
        if (null == name || null == url) {
            return null;
        }
        BaseSource source = null;
        String mPlayUrl = null;

        switch (position) {
            case 0://youku
                source = new YoukuSource();
                break;
            case 1://qq
                source = new TencentSource();
                break;
            case 2://letv
                source = new LetvSource();
                break;
            case 3://pptv
                source = new PPtvSource();
                break;
            case 4://acfun
                source = new AcfunSource();
                break;
            case 5://fun
                source = new FunSource();
                break;
            case 6://cntv
                source = new CNTVSource();
                break;
            case 7://baofeng
                source = new BaoFengSource();
                break;
            case 8://wasu
                source = new HuashuSource();
                break;
            case 9://mangguo
                source = new MangGuoSource();
                break;
            case 10://sohu
                source = new SouhuSource();
                break;
            case 11://iqiyi
                source = new IqiyiSource();
                break;
            case 14://1905
                source = new M1095Source();
                break;
            case 15://tudou
                source = new TudouSource();
                break;
            case 22://bili
                source = new BiliBiliSource();
                break;
            default:
                break;
        }
        if (null != source) {
            mPlayUrl = source.getPlayUrl(url);
//            mPlayUrl = "http://www.id97.com/uploads/m3u8/177779.m3u8";
            Log.i("info", "mPlayUrl->:" + mPlayUrl);
        }
       return mPlayUrl;
    }


    //返回短视频json字符串播放地址
    private static String getJsonShortPlayUrl(int position, String definition) {
        if (null == name || null == url) {
            return null;
        }
        BaseSource source = null;
        String mPlayUrl = null;

        switch (position) {
            case 0://youku
                source = new YoukuSource();
                break;
            case 1://qq
                source = new TencentSource();
                break;
            case 2://letv
                source = new LetvSource();
                break;
            case 3://pptv
                source = new PPtvSource();
                break;
            case 4://acfun
                source = new AcfunSource();
                break;
            case 5://fun
                source = new FunSource();
                break;
            case 8://wasu
                source = new HuashuSource();
                break;
            case 11://iqiyi
                source = new IqiyiSource();
                break;
            case 12://yinyuetai
                source = new YinyuetaiSource();
                break;
            case 13://ku6
                source = new Ku6Source();
                break;
            case 14://1905
                source = new M1095Source();
                break;
            default:
                break;
        }
        if (null != source) {
            mPlayUrl = source.getPlayUrlShort(url);
            Log.i("info", "mPlayUrl->:" + mPlayUrl);
        }
        return mPlayUrl;
    }


    private static String getLiveJsonPlayUrl(int position){
        if (null == name || null == url) {
            return null;
        }
        LiveSource source = null;
        String mPlayUrl = null;

        switch (position) {
            case 1://qq
                source = new TencentLiveSource();
                break;
            case 10://sohu
                source = new SouhuLiveSource();
                break;
            case 16://quanmin
                source = new QuanMinLiveSource();
                break;
            case 17://douyu
                source = new DouYuLiveSource();
                break;
            case 18://huya
                source = new HuYaLiveSource();
                break;
            case 19://chushou
                source = new ChuShouLiveSource();
                break;
            case 20://bobo
                source = new BoBoLiveSource();
                break;
            case 21://kk
                source = new KKLiveSource();
                break;
            default:
                break;
        }
        if (null != source) {
            mPlayUrl = source.getLiveJsonPlayUrl(url);
//            Log.i("info", "...||.." + mPlayUrl);
            try {
                mPlayUrl = new JSONObject(mPlayUrl).getString(Definition.NORMAL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        mPlayUrl = "http://cv.6rooms.com/ots/53/ba/3ng4kb83DsyAmbRUh.mp4?key1=127714582&key2=1156424431&key3=1032272876&key4=1507876242";
        Log.i("info", "mPlayUrl->:" + mPlayUrl);
        return mPlayUrl;
    }

}
