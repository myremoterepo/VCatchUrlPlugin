package com.a2345.mimeplayer.SourceContainer;

import android.util.Base64;
import android.util.Log;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.MD5;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

import java.net.URLDecoder;
import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * Created by fanzf on 2016/3/3.
 */
public class M1095Source extends BaseSource {
    private static final String fix = "http://m.mapps.m1905.cn/Vod/vodDetail?id=";

    @Override
    public String getJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        String str1 = url.substring(url.lastIndexOf("/"), url.lastIndexOf(".")).replaceAll("[^0-9]", "");
        String imei = makeImei();
        HttpHeader header = new HttpHeader();
        header.setUserAgent("Apache-HttpClient/UNAVAILABLE (java 1.4)");
        header.setDid(imei);
        header.setKey(MD5.getString(imei + "m1905_2014"));
        header.setVer("100/41/2015050601");
        header.setPid("204");
        try {
            HttpTools.getWebContent("http://m.mapps.m1905.cn/Index/checkupdate", header);
            String htmlContent = HttpTools.getWebContent(fix + str1, header);
            Log.i("info", "htmlContent-->:" + htmlContent);
            String clrContent = URLDecoder.decode(decode(htmlContent), "UTF-8");
            Log.i("info", "clrContent-->:" + clrContent);
            JSONObject htmlObj = new JSONObject(clrContent);
            htmlObj = htmlObj.getJSONObject("data");
            String playerUrl;
            if (htmlObj.has("soonUrl")) {
                playerUrl = htmlObj.getString("soonUrl");
                if (playerUrl.startsWith("http")) {
                    resultObj.put(Definition.NORMAL, playerUrl);
                }
            }
            if (htmlObj.has("hdUrl")) {
                playerUrl = htmlObj.getString("hdUrl");
                if (playerUrl.startsWith("http")) {
                    resultObj.put(Definition.HIGH, playerUrl);
                }
            }
            if (htmlObj.has("sdUrl")) {
                playerUrl = htmlObj.getString("sdUrl");
                if (playerUrl.startsWith("http")) {
                    resultObj.put(Definition.SUPER, playerUrl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }


    private String a = "iufles8787rewjk1qkq9dj76";
    private String b = "vs0ld7w3";

    private String decode(String paramString) {
        try {
            Object localObject = new DESedeKeySpec(a.getBytes());
            localObject = SecretKeyFactory.getInstance("DESede").generateSecret((KeySpec) localObject);
            Cipher localCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            localCipher.init(2, (Key) localObject, new IvParameterSpec(b.getBytes()));
            paramString = new String(localCipher.doFinal(Base64.decode(paramString, Base64.DEFAULT)), "UTF-8");
            return paramString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String makeImei() {
        String imeiString = "35566778898256";//前14位
        char[] imeiChar = imeiString.toCharArray();
        int resultInt = 0;
        for (int i = 0; i < imeiChar.length; i++) {
            int a = Integer.parseInt(String.valueOf(imeiChar[i]));
            i++;
            final int temp = Integer.parseInt(String.valueOf(imeiChar[i])) * 2;
            final int b = temp < 10 ? temp : temp - 9;
            resultInt += a + b;
        }
        resultInt %= 10;
        resultInt = resultInt == 0 ? 0 : 10 - resultInt;
        return imeiString + resultInt;
    }

    @Override
    public String getJsonPlayUrlShort(String url) {
        return getJsonPlayUrl(url);
    }
}
