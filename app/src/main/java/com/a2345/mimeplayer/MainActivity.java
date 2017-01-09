package com.a2345.mimeplayer;

import com.a2345.mimeplayer.Adapter.MyAdapter;
import com.a2345.mimeplayer.Util.GoPlayException;
import com.a2345.mimeplayer.ValuePool.Definition;
import com.a2345.mimeplayer.ValuePool.SourceInfo;
import com.a2345.mimeplayer.ValuePool.VideoUrlInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    ListView mSourceList;
    List<SourceInfo> mSourceListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;

        initData();
        onBindDataToView();
    }


    /**
     * 获取手机的Mac地址
     */
    public static String getLocalMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String mac = info.getMacAddress();
            return mac;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.to_player) {
            Intent mToPlayerIntent = new Intent();
            mToPlayerIntent.setClass(this, ProtoPlayerActivity.class);
            startActivity(mToPlayerIntent);
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void onBindDataToView() {
        mSourceList.setAdapter(new MyAdapter(this, mSourceListData));
        mSourceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("info", "position-->:" + position);
                SourceInfo info = mSourceListData.get(position);
                String definition = Definition.NORMAL;
                onPlay(info, position, definition);
            }
        });

    }

    CatcherCallback mCatcherCallback = new CatcherCallback() {
        @Override
        public void onFail(GoPlayException mGoPlayException) {

        }

        @Override
        public void onSuccess(VideoUrlInfo mVideoUrlInfo) {
            if (null != mVideoUrlInfo) {
                if (null != mVideoUrlInfo.getPlayUrl() && mVideoUrlInfo.getPlayUrl().startsWith("http")) {
                    String playerUrl = mVideoUrlInfo.getPlayUrl();
                    Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                    intent.putExtra("url", playerUrl);
                    Log.e("parseurl", "playerUrl.." + playerUrl);
                    startActivity(intent);
                }
            }
        }
    };

    private void onPlay(final SourceInfo info, final int position, final String definition) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Catcher.get(info, position, definition, mCatcherCallback);
            }
        }).start();
    }


    private void initData() {
        mSourceList = (ListView) findViewById(R.id.source_list);
        mSourceListData = new ArrayList<>();

        String name = "Test Player";
        String type = "test";
        String url = "http://play2.kandian.360.cn/vod_xinwen/vod-media/1324763_3502394_20160818095725.m3u8";
        SourceInfo infoTest = new SourceInfo(name, url, type);
        mSourceListData.add(infoTest);

        name = "youku";
        type = "long";
        url = "http://v.youku.com/v_show/id_XMTY0OTExMDc2MA==.html?from=s1.8-3-1.1";
        SourceInfo info = new SourceInfo(name, url, type);
        mSourceListData.add(info);

        name = "qq";
        type = "long";
//        url = "http://v.qq.com/x/cover/8ohpnw9xn06irsd.html?ptag=2345.moviepay";
        url = "http://v.qq.com/x/cover/9j687ivvhjsqbzm.html?vid=d0021pz6zmn";
//        url = "http://v.qq.com/x/cover/4h61zy68rn2moy4.html?ptag=2345.variety";
//        url = "http://v.qq.com/x/page/n0021zrdqeh.html";
        SourceInfo infoQQ = new SourceInfo(name, url, type);
        mSourceListData.add(infoQQ);

        name = "pptv";
        type = "long";
        url = "http://v.pptv.com/show/MdIPjPRayghr6fU.html?rcc_id=2345daohangcl?rcc_id=2345daohangneiye";
        SourceInfo infoPPTV = new SourceInfo(name, url, type);
        mSourceListData.add(infoPPTV);

        name = "cntv";
        type = "long";
        url = "http://tv.cntv.cn/video/VSET100261379617/8b1e3255e6c1428b9036cbe87d68393f";
        SourceInfo infoCNTV = new SourceInfo(name, url, type);
        mSourceListData.add(infoCNTV);

        name = "1905";
        type = "long";
        url = "http://www.1905.com/vod/play/976938.shtml?fr=wwwmdb_data_playbutton_2_20140918";
        SourceInfo infoM1905 = new SourceInfo(name, url, type);
        mSourceListData.add(infoM1905);

        name = "baofeng";
        type = "long";
        url = "http://www.baofeng.com/play/234/play-795734.html";
        SourceInfo infoBaoFeng = new SourceInfo(name, url, type);
        mSourceListData.add(infoBaoFeng);

        name = "hunantv";
        type = "long";
        url = "http://www.mgtv.com/v/3/156506/f/1860458.html";
        SourceInfo infoMangguo = new SourceInfo(name, url, type);
        mSourceListData.add(infoMangguo);

        name = "sohu";
        type = "long";
        url = "http://tv.sohu.com/20160102/n433271530.shtml";
        SourceInfo infoSouhu = new SourceInfo(name, url, type);
        mSourceListData.add(infoSouhu);

        name = "yinyuetai";
        type = "short";
        url = "http://v.yinyuetai.com/video/2654289?f=SY-ZZLX-FT-3";
        SourceInfo infoYinyuetai = new SourceInfo(name, url, type);
        mSourceListData.add(infoYinyuetai);

        name = "tudou";
        type = "long";
        url = "http://www.tudou.com/albumplay/k-mE2Y8SHpU/eqyKXOAN2FQ.html";
        SourceInfo infoTudou = new SourceInfo(name, url, type);
        mSourceListData.add(infoTudou);

        name = "bilibili";
        type = "long";
        url = "http://www.bilibili.com/video/av1262543/index_1.html?from=2345_app";
        SourceInfo infoBiLi = new SourceInfo(name, url, type);
        mSourceListData.add(infoBiLi);

        name = "ku6";
        type = "short";
        url = "http://v.ku6.com/show/9LsZ5BMm3ZoHHHuuLcQ88A...html?hpsrc=1_25_1_1_0";
        SourceInfo infoKu6 = new SourceInfo(name, url, type);
        mSourceListData.add(infoKu6);

        name = "wasu";
        type = "long";
        url = "http://www.wasu.cn/Play/show/id/7818423";
        SourceInfo infoHuashu = new SourceInfo(name, url, type);
        mSourceListData.add(infoHuashu);

        name = "letv";
        type = "long";
        url = "http://www.le.com/ptv/vplay/25337573.html";
        SourceInfo infoLetv = new SourceInfo(name, url, type);
        mSourceListData.add(infoLetv);

        name = "acfun";
        type = "long";
        url = "http://www.acfun.tv/v/ac2778231?hmsr=2345-%E5%BD%B1&hmpl=&hmcu=&hmkw=&hmci=";
        SourceInfo infoAcfun = new SourceInfo(name, url, type);
        mSourceListData.add(infoAcfun);

        name = "fun";
        type = "long";
        url = "http://www.fun.tv/vplay/g-105597/";
        SourceInfo infoFun = new SourceInfo(name, url, type);
        mSourceListData.add(infoFun);

        name = "iqiyi";
        type = "short";
        url = "http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=d4459703f7f58c271b37e55805a368ad&tvId=457472500&coop=coop_153_2345&bd=1&fullscreen=1&autoplay=0";
        SourceInfo infoIqiyi = new SourceInfo(name, url, type);
        mSourceListData.add(infoIqiyi);

        name = "quanmin";
        type = "live";
        url = "http://www.quanmin.tv/v/1851785";
        SourceInfo infoQuanMin = new SourceInfo(name, url, type);
        mSourceListData.add(infoQuanMin);

        name = "douyu";
        type = "live";
        url = "http://www.douyu.com/52";
        SourceInfo infoDouYu = new SourceInfo(name, url, type);
        mSourceListData.add(infoDouYu);

        name = "huya";
        type = "live";
        url = "http://m.huya.com/kaerlol";
        SourceInfo infoHuYa = new SourceInfo(name, url, type);
        mSourceListData.add(infoHuYa);

        name = "chushou";
        type = "live";
        url = "33591";
        SourceInfo infoChuShou = new SourceInfo(name, url, type);
        mSourceListData.add(infoChuShou);

        name = "bobo";
        type = "live";
        url = "55303340";
        SourceInfo infoBoBo = new SourceInfo(name, url, type);
        mSourceListData.add(infoBoBo);

        name = "kk";
        type = "live";
        url = "92622685";
        SourceInfo infoKK = new SourceInfo(name, url, type);
        mSourceListData.add(infoKK);
    }
}
