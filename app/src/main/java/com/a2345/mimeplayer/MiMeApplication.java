package com.a2345.mimeplayer;

import android.app.Application;

import com.a2345.mimeplayer.Util.DeviceInfoHelper;

/**
 * Created by fanzf on 2016/2/26.
 */
public class MiMeApplication extends Application {
    public static String imei;

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setImei(DeviceInfoHelper.getImei(this));
    }
}
