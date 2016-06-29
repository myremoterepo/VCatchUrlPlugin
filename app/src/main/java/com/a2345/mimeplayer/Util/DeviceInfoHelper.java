package com.a2345.mimeplayer.Util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Created by fanzf on 2016/3/3.
 */
public class DeviceInfoHelper {
    /**
     * 获取手机的imei
     *
     * @return
     */
    public static String getImei(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            if (imei == null || imei == "") {
                imei = getAdviceId(context);
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param context
     * @return
     */
    public static String getAdviceId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }
}
