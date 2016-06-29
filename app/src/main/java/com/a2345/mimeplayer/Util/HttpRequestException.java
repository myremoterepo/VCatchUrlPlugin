package com.a2345.mimeplayer.Util;

/**
 * Created by fanzf on 2015/12/9.
 */
public class HttpRequestException {
    private int errorCode;
    private String errorInfo;

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
}
