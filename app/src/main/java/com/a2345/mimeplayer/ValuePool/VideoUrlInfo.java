package com.a2345.mimeplayer.ValuePool;

/**
 * Created by fanzf on 2015/12/1.
 */
public class VideoUrlInfo {
    private String name;//名字
    private String source;//源
    private String playUrl;//播放地址
    private String size;//大小
    private String type;//类型(mp4,m3u8...)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
