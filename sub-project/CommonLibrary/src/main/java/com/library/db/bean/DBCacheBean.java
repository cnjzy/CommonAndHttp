package com.library.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by jzy on 2017/6/16.
 */
@DatabaseTable(tableName = "cache")
public class DBCacheBean extends DBBaseBean {

    @DatabaseField
    private String cache_id;
    @DatabaseField
    private int type;
    @DatabaseField
    private String url;
    @DatabaseField
    private String json;
    @DatabaseField
    private long date = System.currentTimeMillis();
    @DatabaseField
    private String key = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getCache_id() {
        return cache_id;
    }

    public void setCache_id(String cache_id) {
        this.cache_id = cache_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "DBCacheBean{" +
                "cache_id='" + cache_id + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", json='" + json + '\'' +
                ", date=" + date +
                ", key='" + key + '\'' +
                '}';
    }
}
