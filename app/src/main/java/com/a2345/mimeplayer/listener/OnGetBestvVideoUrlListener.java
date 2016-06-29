package com.a2345.mimeplayer.listener;

import com.a2345.mimeplayer.bean.BestvVideoUrlInfo;

import java.util.ArrayList;

/**
 * Created by fanzf on 2016/6/28.
 */
public interface OnGetBestvVideoUrlListener {
    void onBestvVideoUrlInfo(String var1, ArrayList<BestvVideoUrlInfo> var2);

    void onError(String var1, int var2, String var3);
}
