package com.a2345.mimeplayer.Util;


import com.a2345.mimeplayer.ValuePool.VideoUrlInfo;

/**
 * Created by fanzf on 2015/12/1.
 */
public class GoPlayException {
    private int errorCode;
    private String errorInfo;
    private VideoUrlInfo errorVideoUrlInfo;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public VideoUrlInfo getErrorVideoUrlInfo() {
        return errorVideoUrlInfo;
    }

    public void setErrorVideoUrlInfo(VideoUrlInfo errorVideoUrlInfo) {
        this.errorVideoUrlInfo = errorVideoUrlInfo;
    }
}
