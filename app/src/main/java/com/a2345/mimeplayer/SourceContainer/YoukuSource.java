package com.a2345.mimeplayer.SourceContainer;

import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.a2345.mimeplayer.Util.HttpHeader;
import com.a2345.mimeplayer.Util.HttpTools;
import com.a2345.mimeplayer.Util.MD5;
import com.a2345.mimeplayer.ValuePool.Definition;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2015/11/25.
 */
public class YoukuSource extends BaseSource {

    public String getJsonPlayUrl(String url) {
        JSONObject resultObj = new JSONObject();
        if (url == null) {
            return null;
        }

        String mId = getId(url);
        if (mId == null) {
            return null;
        }
        String mArguments = MD5.convert(Build.ID);
        Log.i("info", "id-->:" + Build.ID);
        Log.i("info", "MD5-->:" + mArguments);
        Log.i("info", "mId-->:" + mId);
        try {
            String mPlayUrl = get(mId, mArguments);
            if (null != mPlayUrl) {
                resultObj.put(Definition.NORMAL, mPlayUrl);
            }
            Log.i("info", "result-->:" + resultObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj.toString();
    }

    private String get(String mId, String mDid) throws Exception {
        String str2 = null;
        if (mId == null) {
            str2 = "";
            return str2;
        }

        String str6 = "http://i.play.api.3g.youku.com/common/v3/play?&audiolang=1&brand=apple&btype=iPhone5,2&ctype=%s&deviceid=0f607264fc6318a92b9e13c65db7cd3c&did="
                + mDid
                + "&format=1,3,6,7&guid=7066707c5bdc38af1621eaf94a6fe779&id=%s&idfa=&local_point=0&local_time=0&local_vid=%s&network=WIFI&os=ios&os_ver=8.3&ouid=c65a56e1cc620ea10f825bbd5a3a2c15790950e1&pid=69b81504767483cf&point=1&scale=2&vdid=1232DDAA-3181-4EA7-8FDA-9E072E10B2B1&ver=4.5%s";

        Object[] arrayOfObject1 = new Object[4];
        arrayOfObject1[0] = "20";
        arrayOfObject1[1] = mId;
        arrayOfObject1[2] = mId;
        arrayOfObject1[3] = "4.5";
        String str7 = String.format(str6, arrayOfObject1);
        Log.i("info", "str7-->:" + str7);
        String mDetails = "Youku HD;3.9.4;iPhone OS;7.1.2;iPad4,1";
        HttpHeader mYoukuHead = new HttpHeader();
        mYoukuHead.setUserAgent(mDetails);
        String mTmpContent = HttpTools.getWebContent(str7, mYoukuHead);
        if (null == mTmpContent || mTmpContent.length() < 1) {
            str2 = "";
            return str2;
        }

        JSONObject mTmpObj = new JSONObject(mTmpContent);
        if (!mTmpObj.has("data")) {
            str2 = "";
            return str2;
        }

        String mData = mTmpObj.getString("data");
        String mClearTxt = new String(decrypt(Base64.decode(mData.getBytes("UTF-8"), Base64.DEFAULT), "qwer3as2jin4fdsa"), "UTF-8");

        JSONObject localJSONObject2 = new JSONObject(mClearTxt);
        if (localJSONObject2.has("sid_data")) {
            JSONObject localJSONObject3 = localJSONObject2.getJSONObject("sid_data");
            String str18 = localJSONObject3.getString("token");
            String str20 = localJSONObject3.getString("oip");
            String str22 = localJSONObject3.getString("sid");
            Object[] arrayOfObject2 = new Object[3];
            arrayOfObject2[0] = str22;
            arrayOfObject2[1] = mId;
            arrayOfObject2[2] = str18;
            String str23 = String.format("%s_%s_%s", arrayOfObject2);
            String str24 = "9e3633aadde6bfec";
            String str25 = URLEncoder.encode(getEp(str23, str24), "utf-8");
            String str26 = "http://pl.youku.com/playlist/m3u8?ts=%s&keyframe=1&vid=%s&sid=%s&token=%s&oip=%s&type=%s&did="
                    + mDid + "&ctype=%s&ev=1&ep=%s";
            Object[] arrayOfObject3 = new Object[8];
            Long localLong = Long.valueOf(System.currentTimeMillis() / 1000L);
            arrayOfObject3[0] = localLong;
            arrayOfObject3[1] = mId;
            arrayOfObject3[2] = str22;
            arrayOfObject3[3] = str18;
            arrayOfObject3[4] = str20;
            arrayOfObject3[5] = "hd2";
            arrayOfObject3[6] = "20";
            arrayOfObject3[7] = str25;
            str2 = String.format(str26, arrayOfObject3);
        }
        return str2;
    }

    private String getEp(String paramString1, String paramString2)
            throws Exception {
        byte[] arrayOfByte1 = paramString1.getBytes("UTF-8");
        int i = arrayOfByte1.length;

        int j = 0;
        int k = (i / 16 + 1) * 16;
        byte[] arrayOfByte2 = new byte[k];

        while (j < k) {

            if (j >= i) {
                arrayOfByte2[j] = 0;
            } else {
                Byte m = arrayOfByte1[j];
                arrayOfByte2[j] = m;
            }
            j += 1;
        }
        byte[] arrayOfByte3 = paramString2.getBytes("UTF-8");
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte3,
                "AES");
        Cipher localCipher = Cipher.getInstance("AES/ECB/NoPadding");
        localCipher.init(1, localSecretKeySpec);
        return Base64.encodeToString(localCipher.doFinal(arrayOfByte2), Base64.DEFAULT);
    }

    public static byte[] decrypt(byte[] paramArrayOfByte, String paramString) {
        byte[] resultBytes = null;
        try {
            KeyGenerator localKeyGenerator = KeyGenerator.getInstance("AES");
            byte[] arrayOfByte1 = paramString.getBytes("utf-8");
            SecureRandom localSecureRandom = new SecureRandom(arrayOfByte1);
            localKeyGenerator.init(localSecureRandom);
            String str = "AES/ECB/NoPadding";
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte1, str);
            Cipher localCipher = Cipher.getInstance("AES/ECB/NoPadding");
            localCipher.init(2, localSecretKeySpec);
            resultBytes = localCipher.doFinal(paramArrayOfByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultBytes;
    }

    //从传过来的地址截取字符串
    private String getId(String url) {
        String id = null;
        if (url.contains("v_show")) {
            int mStartLoc = url.indexOf("id_") + 3;
            int mEndLoc = url.lastIndexOf(".html");
            id = url.substring(mStartLoc, mEndLoc);
        }
        return id;
    }

    public String getJsonPlayUrlShort(String url) {
        return getJsonPlayUrl(url);
    }
}
