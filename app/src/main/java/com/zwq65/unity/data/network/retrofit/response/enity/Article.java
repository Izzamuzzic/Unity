package com.zwq65.unity.data.network.retrofit.response.enity;

import java.util.List;

/**
 * Created by zwq65 on 2017/08/30
 */

public class Article {
    /**
     * _id : 59a4ea09421aa901b9dc4652
     * createdAt : 2017-08-29T12:14:01.783Z
     * desc : 在任何非 MIUI 设备上体验小米系统级推送。
     * images : ["http://img.gank.io/1c974dca-f68d-4925-826e-863ac8a40d48"]
     * publishedAt : 2017-08-29T12:19:18.634Z
     * source : chrome
     * type : Android
     * url : https://github.com/Trumeet/MiPushFramework
     * used : true
     * who : 代码家
     */

    private String _id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;
    private List<String> images;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
