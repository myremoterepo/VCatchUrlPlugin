package com.a2345.mimeplayer.ValuePool;

/**
 * Created by Administrator on 2015/11/25.
 */
public class SourceInfo {
    private final String sourceName;//视频源
    private final String sourceUrl;//地址
    private final String sourceType;//长短视频

    public SourceInfo(String name, String url, String type) {
        this.sourceName = name;
        this.sourceUrl = url;
        this.sourceType = type;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getSourceType() {
        return sourceType;
    }

}
