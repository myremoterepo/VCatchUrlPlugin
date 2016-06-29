package com.a2345.mimeplayer.Util;

/**
 * Created by fanzf on 2015/11/26.
 */
public class HttpHeader {
    private String mUserAgent;
    private String mOrigin;
    private String mDeviceType;
    private String mReferer;
    private String mHost;

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        mHost = host;
    }

    private String did;
    private String key;
    private String ver;
    private String pid;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public void setOrigin(String mOrigin) {
        this.mOrigin = mOrigin;
    }

    public String getDeviceType() {
        return mDeviceType;
    }

    public void setDeviceType(String mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    public String getReferer() {
        return mReferer;
    }

    public void setReferer(String mReferer) {
        this.mReferer = mReferer;
    }

    public String getUserAgent() {
        return mUserAgent;
    }

    public void setUserAgent(String mUserAgent) {
        this.mUserAgent = mUserAgent;
    }
}
