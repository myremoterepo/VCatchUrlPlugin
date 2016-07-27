package com.a2345.mimeplayer;

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

import com.a2345.mimeplayer.Adapter.MyAdapter;
import com.a2345.mimeplayer.Util.GoPlayException;
import com.a2345.mimeplayer.ValuePool.Definition;
import com.a2345.mimeplayer.ValuePool.SourceInfo;
import com.a2345.mimeplayer.ValuePool.VideoUrlInfo;

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

        String name = "youku";
        String type = "long";
        String url = "http://v.youku.com/v_show/id_XMTY0OTExMDc2MA==.html?from=s1.8-3-1.1";
        SourceInfo info = new SourceInfo(name, url, type);
        mSourceListData.add(info);

        name = "Test Player";
        type = "test";
        url = "http://pl.youku.com/playlist/m3u8?ts=1467105471&keyframe=1&vid=XMTYyMjE4MDkwNA==&sid=04671054729472053c9c2&token=9150&oip=1879991562&type=hd2&did=9bc64e589d4162d33c77f80a19dfe323&ctype=20&ev=1&ep=y6ub3LkuptUtHc0CoqXUCIa2r%2F0wztwO7klWoJiAp6KZAk5Sv%2B1kxUoC7UBNUq7m%0A";
        SourceInfo infoTest = new SourceInfo(name, url, type);
        mSourceListData.add(infoTest);

        name = "qq";
        type = "long";
//        url = "http://v.qq.com/cover/7/7dxcpwu7xlr2304/8e0pd98BFvt.html?ptag=2345.tv";
        url = "http://v.qq.com/cover/r/r11kl0o4vpdo3ah/g0019hf26el.html?ptag=2345.tv";
        SourceInfo infoQQ = new SourceInfo(name, url, type);
        mSourceListData.add(infoQQ);

        name = "letv";
        type = "long";
        url = "http://www.le.com/ptv/vplay/25337573.html";
        SourceInfo infoLetv = new SourceInfo(name, url, type);
        mSourceListData.add(infoLetv);

        name = "pptv";
        type = "long";
        url = "http://m.pptv.com/show/o4Fe3UWrG1m8OsY.html?rcc_id=wap_007";
        SourceInfo infoPPTV = new SourceInfo(name, url, type);
        mSourceListData.add(infoPPTV);

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

        name = "cntv";
        type = "long";
        url = "http://tv.cntv.cn/video/VSET100261379617/8b1e3255e6c1428b9036cbe87d68393f";
        SourceInfo infoCNTV = new SourceInfo(name, url, type);
        mSourceListData.add(infoCNTV);

        name = "baofeng";
        url = "http://www.baofeng.com/play/134/play-796134.html";
        SourceInfo infoBaoFeng = new SourceInfo(name, url, type);
        mSourceListData.add(infoBaoFeng);

        name = "wasu";
        type = "short";
        url = "http://www.wasu.cn/Play/show/id/7234516?refer=2345.com";
        SourceInfo infoHuashu = new SourceInfo(name, url, type);
        mSourceListData.add(infoHuashu);

        name = "hunantv";
        type = "long";
        url = "http://www.mgtv.com/v/3/156506/f/1860458.html";
        SourceInfo infoMangguo = new SourceInfo(name, url, type);
        mSourceListData.add(infoMangguo);

        name = "sohu";
        type = "long";
        url = "http://tv.sohu.com/20150914/n421072256.shtml?txid=351f624a85d86201915c41c0e4d05d20";
        SourceInfo infoSouhu = new SourceInfo(name, url, type);
        mSourceListData.add(infoSouhu);

        name = "iqiyi";
        type = "short";
        url = "http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=d4459703f7f58c271b37e55805a368ad&tvId=457472500&coop=coop_153_2345&bd=1&fullscreen=1&autoplay=0";
        SourceInfo infoIqiyi = new SourceInfo(name, url, type);
        mSourceListData.add(infoIqiyi);

        name = "yinyuetai";
        type = "short";
        url = "http://www.yinyuetai.com/mv/video-url/656255";
        SourceInfo infoYinyuetai = new SourceInfo(name, url, type);
        mSourceListData.add(infoYinyuetai);

        name = "ku6";
        type = "short";
        url = "http://m.ku6.com/show/l_ZftDWKMp-AGf95rXTOoQ...html";
        SourceInfo infoKu6 = new SourceInfo(name, url, type);
        mSourceListData.add(infoKu6);

        name = "1905";
        type = "long";
        url = "http://www.1905.com/vod/play/976938.shtml?fr=wwwmdb_data_playbutton_2_20140918";
        SourceInfo infoM1905 = new SourceInfo(name, url, type);
        mSourceListData.add(infoM1905);

        name = "tudou";
        type = "long";
        url = "http://www.tudou.com/albumplay/k-mE2Y8SHpU/eqyKXOAN2FQ.html";
        SourceInfo infoTudou = new SourceInfo(name, url, type);
        mSourceListData.add(infoTudou);

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

        name = "bilibili";
        type = "long";
        url = "http://www.bilibili.com/video/av1262543/index_1.html?from=2345_app";
        SourceInfo infoBiLi = new SourceInfo(name, url, type);
        mSourceListData.add(infoBiLi);

    }
}
