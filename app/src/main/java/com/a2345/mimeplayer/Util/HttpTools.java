package com.a2345.mimeplayer.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

/**
 * Created by fanzf on 2015/11/26.
 */
public class HttpTools {
    public static String getWebContent(String url, HttpHeader head) throws Exception {
        String mWebContent;
        URL reqUrl = new URL(url);
        URLConnection conn = reqUrl.openConnection();
        if (null != head) {
            if (head.getUserAgent() != null)
                conn.addRequestProperty("User-Agent", head.getUserAgent());
            if (head.getOrigin() != null)
                conn.addRequestProperty("Origin", head.getOrigin());
            if (head.getDeviceType() != null)
                conn.addRequestProperty("deviceType", head.getDeviceType());
            if (head.getReferer() != null)
                conn.addRequestProperty("Referer", head.getReferer());
            if (head.getHost() != null)
                conn.addRequestProperty("Host", head.getHost());
            if (head.getPid() != null)
                conn.addRequestProperty("pid", head.getPid());
            if (head.getKey() != null)
                conn.addRequestProperty("key", head.getKey());
            if (head.getDid() != null)
                conn.addRequestProperty("did", head.getDid());
            if (head.getVer() != null)
                conn.addRequestProperty("ver", head.getVer());
        }
        conn.addRequestProperty("Cache-Control", "max-age=0");
        conn.setConnectTimeout(60000);
        conn.setReadTimeout(60000);
        conn.connect();

        String contentEncoding = conn.getContentEncoding();
        InputStream is;
        if (null != contentEncoding && contentEncoding.contains("gzip")) {
            is = new GZIPInputStream(conn.getInputStream());
        } else {
            is = conn.getInputStream();
        }
        InputStream inputStream = new BufferedInputStream(is);
        byte[] bs = new byte[8 * 1024];
        int len = 0;
        StringBuffer sb = new StringBuffer();
        while ((len = inputStream.read(bs)) != -1) {
            String str = new String(bs, 0, len);
            sb.append(str);
        }
        mWebContent = sb.toString();
        return mWebContent;
    }


    public static String getMacAddress(Context context) {
        if(context == null) {
            return "";
        } else {
            String localMac = null;
            if(isWifiAvailable(context)) {
                localMac = getWifiMacAddress(context);
            }

            if(localMac != null && localMac.length() > 0) {
                localMac = localMac.replace(":", "-").toLowerCase();
                return localMac;
            } else {
                localMac = getMacFromCallCmd();
                if(localMac != null) {
                    localMac = localMac.replace(":", "-").toLowerCase();
                }

                return localMac;
            }
        }
    }

    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == 1;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";

        try {
            Process e = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(e.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while((line = br.readLine()) != null && !line.contains(filter)) {
                ;
            }

            result = line;
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return result;
    }


    private static String getMacFromCallCmd() {
        String result = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        if(result != null && result.length() > 0) {
            Log.d("NetWorkUtil", "cmd result : " + result);
            if(result.length() > 0 && result.contains("HWaddr")) {
                String Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
                if(Mac.length() > 1) {
                    result = Mac.replaceAll(" ", "");
                }
            }

            return result;
        } else {
            return null;
        }
    }

    private static String getWifiMacAddress(Context context) {
        String localMac = null;

        try {
            WifiManager e = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
            WifiInfo info = e.getConnectionInfo();
            if(e.isWifiEnabled()) {
                localMac = info.getMacAddress();
                if(localMac != null) {
                    localMac = localMac.replace(":", "-").toLowerCase();
                    return localMac;
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return null;
    }


}
