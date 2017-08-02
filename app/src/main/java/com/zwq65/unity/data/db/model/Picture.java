package com.zwq65.unity.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zwq65 on 2017/08/01
 */

@Entity(nameInDb = "Picture")
public class Picture {
    @Id
    private String _id;
    @Property(nameInDb = "created_at")
    private String createdAt;
    @Property(nameInDb = "desc")
    private String desc;
    @Property(nameInDb = "source")
    private String source;
    @Property(nameInDb = "type")
    private String type;
    @Property(nameInDb = "url")
    private String url;
    @Property(nameInDb = "who")
    private String who;
    @Generated(hash = 314158784)
    public Picture(String _id, String createdAt, String desc, String source,
            String type, String url, String who) {
        this._id = _id;
        this.createdAt = createdAt;
        this.desc = desc;
        this.source = source;
        this.type = type;
        this.url = url;
        this.who = who;
    }
    @Generated(hash = 1602548376)
    public Picture() {
    }
    public String get_id() {
        return this._id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getSource() {
        return this.source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getWho() {
        return this.who;
    }
    public void setWho(String who) {
        this.who = who;
    }

}