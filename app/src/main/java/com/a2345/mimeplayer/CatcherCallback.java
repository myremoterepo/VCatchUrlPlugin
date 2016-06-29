package com.a2345.mimeplayer;


import com.a2345.mimeplayer.Util.GoPlayException;
import com.a2345.mimeplayer.ValuePool.VideoUrlInfo;

/**
 * Created by fanzf on 2015/12/1.
 */
public interface CatcherCallback {
    void onFail(GoPlayException mGoPlayException);
    void onSuccess(VideoUrlInfo mVideoUrlInfo);
}
